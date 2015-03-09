package us.core_network.cornel.module;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public abstract class Module {
    private Module parent = null;
    private Set<Module> children = new HashSet<>();
    private Plugin plugin = null;
    private Set<RegisteredListener> registeredListeners = new HashSet<>();
    private Set<Dependency> dependencies = new HashSet<>();
    private State state = State.INVALID;
    private Logger logger;
    private String name;

    private Module(String name) {
        this.name = name;
        setupLogger();
    }

    private void setupLogger() {
        logger = Logger.getLogger("Module." + name);
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        logger.addHandler(new ModuleLoggerHandler(this));
    }

    protected Module(Module parent, String name) {
        this(name);
        this.parent = parent;
        this.parent.children.add(this);
    }

    protected Module(Plugin plugin, String name) {
        this(name);
        this.plugin = plugin;
    }

    public Module getParent() {
        return parent;
    }

    public Set<Module> getChildren() {
        return children;
    }

    public Plugin getPlugin() {
        if (plugin == null && getParent() != null) {
            return getParent().getPlugin();
        }
        if (plugin == null) {
            throw new IllegalStateException("Root module must have a plugin instance");
        }
        return plugin;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    protected void setState(State state) {
        if (this.state != state) {
            if (!this.state.isValidNextState(state)) {
                getLogger().warning("Invalid state traversal " + this.state.name() + " -> " + state.name());
                getLogger().warning("We'll allow this for now for development purposes.");
            }
            this.state = state;
            onStateChanged(this.state);
        }
    }

    protected void onStateChanged(State state) {
        switch (state) {
            case PRE_WORLD:
                getLogger().info("Module enabled pre world.");
                onPreWorld();
                break;
            case ENABLED:
                getLogger().info("Module enabled post world.");
                onEnable();
                break;
            case DISABLED:
                getLogger().info("Module disabled.");
                onDisable();
                break;
        }
    }

    protected void onDisable() {
    }

    protected void onEnable() {
    }

    protected void onPreWorld() {
    }

    /**
     * Registers a listener for Bukkit events. This listener will be removed once the module is unloaded
     * @param listener listener instance with EventHandlers
     */
    protected void registerEvents(final Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());

        Arrays.asList(listener.getClass().getMethods())
                .stream()
                .filter(m -> m.getAnnotation(EventHandler.class) != null)
                .forEach(m -> {
                    Parameter p[] = m.getParameters();
                    if (p.length == 1) {
                        Class<?> clazz = p[0].getType();
                        if (clazz.isAssignableFrom(Event.class)) {
                            try {
                                Method handlerList = clazz.getMethod("getHandlerList");
                                handlerList.setAccessible(true);
                                HandlerList list = (HandlerList) handlerList.invoke(null);
                                Arrays.asList(list.getRegisteredListeners())
                                        .stream()
                                        .filter(l -> l.getListener() == listener)
                                        .forEach(registeredListeners::add);
                            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void resolveDependency(Dependency dependency) {
        boolean allMet = resolveDependencies();
        if (allMet && getState().isValidNextState(State.PRE_WORLD, State.ENABLED)) {
            loadInternally();
        }
    }

    public boolean resolveDependencies() {
        Stream<Dependency> filter = dependencies.stream().filter(d -> !d.isMet());
        if (filter.count() > 1) {
            filter.forEach(Dependency::registerListeners);
            return false;
        }
        return true;
    }

    public void loadInternally() {
        // check if conditions are met
        if (!getState().isValidNextState(State.PRE_WORLD, State.ENABLED)) {
            return;
        }
        if (!resolveDependencies()) {
            return;
        }
        if (getParent() != null && !getParent().getState().isLoaded()) {
            return;
        }

        try {
            if (Bukkit.getWorlds().size() == 0) {
                setState(State.PRE_WORLD);
                // TODO register event for when worlds are loaded to trigger onEnable
            } else {
                setState(State.ENABLED);
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error while loading module: ", e);
            return;
        }

        children.forEach(Module::loadInternally);
    }

    public void unloadInternally() {
        if (!getState().isValidNextState(State.DISABLED)) {
            return;
        }
        // remove all listeners and timers
        children.forEach(Module::unloadInternally);

        try {
            setState(State.DISABLED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public State getState() {
        return state;
    }

    protected Logger getLogger() {
        return logger;
    }

    public String getName() {
        return name;
    }

    /**
     * Describes the state this module is in.
     */
    public static enum State {
        /**
         * Module has not yet been initialized.
         */
        INVALID(false),
        /**
         * State just before the config is loaded.
         */
        PRE_CONFIG_LOAD(false),
        /**
         * State just after the config has been loaded.
         */
        CONFIG_LOADED(false),
        /**
         * Before the worlds are loaded. For this to be triggered, plugin.yml must contain load: STARTUP
         */
        PRE_WORLD(true),
        /**
         * After the worlds are loaded.
         */
        ENABLED(true),
        /**
         * Module has been disabled. Usually you don't need to listen for this because all events and scheduled tasks
         * are removed from the system automatically.
         */
        DISABLED(false);

        State(boolean loaded) {
            this.loaded = loaded;
        }

        static {
            INVALID.nextPossibleStates.add(PRE_CONFIG_LOAD);
            PRE_CONFIG_LOAD.nextPossibleStates.add(CONFIG_LOADED);
            CONFIG_LOADED.nextPossibleStates.add(PRE_WORLD);
            CONFIG_LOADED.nextPossibleStates.add(ENABLED);
            CONFIG_LOADED.nextPossibleStates.add(DISABLED);
            PRE_WORLD.nextPossibleStates.add(ENABLED);
            PRE_WORLD.nextPossibleStates.add(DISABLED);
            ENABLED.nextPossibleStates.add(DISABLED);
            DISABLED.nextPossibleStates.add(PRE_WORLD);
            DISABLED.nextPossibleStates.add(ENABLED);
            DISABLED.nextPossibleStates.add(PRE_CONFIG_LOAD);
        }

        private final boolean loaded;
        private final Set<State> nextPossibleStates = new HashSet<>();

        public boolean isLoaded() {
            return loaded;
        }

        /**
         * Returns if all of the given states are possible next states to this one.
         * @param next array or vararg of the states that should be checked.
         * @return wether the given states are all valid next states.
         */
        public boolean isValidNextState(State ... next) {
            for (State s : next) {
                if (!nextPossibleStates.contains(s)) {
                    return false;
                }
            }
            return true;
        }
    }
}
