package us.core_network.cornel.module;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.annotated.AnnotatedObjectConfiguration;
import com.flowpowered.cerealization.config.yaml.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import us.core_network.cornel.event.module.ModuleStateChangedEvent;
import us.core_network.cornel.module.dependency.Dependency;

import java.io.File;
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
    private Set<Dependency> dependencies = new HashSet<>();
    private State state = State.INVALID;
    private Logger logger;
    private String name;
    /**
     * Property that decides where the configuration of the module will be serialized to. <br/>
     * <ul>
     *     <li>
     *         <h3>Store in parent module's file</h3>
     *         <pre>parent</pre>
     *         This will create a subpath with the module's name
     *     </li>
     *     <li>
     *         <h3>Store in parent module's file with specific path</h3>
     *         <pre>parent:path.to.config</pre>
     *     </li>
     *     <li>
     *         <h3>Store in own file</h3>
     *         <pre>modulename.yml</pre>
     *     </li>
     *     <li>
     *         <h3>Store in own file with specific path</h3>
     *         <pre>config.yml:path.to.config</pre>
     *     </li>
     * </ul>
     */
    private String configPath;
    private Object config;
    private String[] configNode;
    private ModuleBukkitManager bukkitManager = new ModuleBukkitManager(this);
    private Configuration configuration;

    private Module(String name, String configPath, Object config) {
        this.name = name;
        this.config = config;
        this.configPath = configPath;
    }

    protected Module(Module parent, String name, String configPath, Object config) {
        this(name, configPath, config);
        this.parent = parent;
        this.parent.children.add(this);
        setupLogger();
        setupConfig();
    }

    protected Module(Plugin plugin, String name, String configPath, Object config) {
        this(name, configPath, config);
        this.plugin = plugin;
        setupLogger();
        setupConfig();
    }

    private void setupLogger() {
        logger = Logger.getLogger(getLoggerName());
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        logger.addHandler(new ModuleLoggerHandler(this));
    }

    private String getLoggerName() {
        return "Plugin." + getPlugin().getName() + ":Module." + name;
    }

    private void setupConfig() {
        // TODO figure out sub-submodules paths
        boolean ownFile = false;
        String pathInFile = null;
        String parse[] = configPath.split(":");
        String fileName = null;
        if (parse[0].endsWith(".yml")) {
            ownFile = true;
            fileName = parse[0];
        }
        if (parse.length > 1) {
            pathInFile = parse[1];
        }
        if (pathInFile == null && !ownFile) {
            pathInFile = getName();
        }

        configNode = null;

        if (pathInFile != null) {
            configNode = pathInFile.split("\\.");
        }

        if (ownFile && configNode == null) {
            configuration = new RootConfiguration(new YamlConfiguration(new File(getPlugin().getDataFolder(), fileName)), config);
        } else if (ownFile) {
            AnnotatedObjectConfiguration aoc = new AnnotatedObjectConfiguration(new YamlConfiguration(new File(getPlugin().getDataFolder(), fileName)));
            configuration = aoc;
            aoc.add(config, configNode);
        } else {
            AnnotatedObjectConfiguration aoc = new AnnotatedObjectConfiguration(getParent().configuration);
            configuration = aoc;
            aoc.add(config, configNode);
        }
    }

    public void saveConfig() {
        try {
            configuration.save();
        } catch (ConfigurationException e) {
            getLogger().log(Level.SEVERE, "Error while saving config " + configPath + ": ", e);
        }
    }

    /**
     * Gets the bukkit event and schedule manager. This manager keeps track of all registered events and tasks to unregister
     * them whenever this module is disabled.
     * @return this modules bukkit manager.
     */
    public ModuleBukkitManager getBukkitManager() {
        return bukkitManager;
    }

    public Module getParent() {
        return parent;
    }

    public Set<Module> getChildren() {
        return children;
    }

    public Module getRoot() {
        if (getParent() != null) {
            return getParent().getRoot();
        } else {
            return this;
        }
    }

    public Module getModuleByName(String name) {
        if (getName().equals(name)) {
            return this;
        } else if (!getChildren().isEmpty()) {
            return getChildren()
                    .stream()
                    .map((module) -> module.getModuleByName(name))
                    .filter((module) -> module != null)
                    .findFirst()
                    .get();
        } else {
            return null;
        }
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
        if (!getState().isValidNextState(State.PRE_CONFIG_LOAD)) {
            getLogger().warning("Could not enable module because the state isn't correct (" + getState().name() + ").");
            return;
        }
        setState(State.PRE_CONFIG_LOAD);
        try {
            configuration.load();
            configuration.save();
        } catch (ConfigurationException e) {
            getLogger().log(Level.SEVERE, "Error while loading config " + configPath + ":", e);
        }
        setState(State.CONFIG_LOADED);
        // check if conditions are met
        if (!getState().isValidNextState(State.PRE_WORLD, State.ENABLED)) {
            getLogger().warning("Could not enable module because the state isn't correct (" + getState().name() + ").");
            return;
        }
        if (!resolveDependencies()) {
            getLogger().info("Could not enable module because we're still waiting on dependencies to resolve.");
            return;
        }
        if (getParent() != null && !getParent().getState().isLoaded()) {
            getLogger().info("Could not enable module because the parent module isn't loaded.");
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
        getBukkitManager().removeAll();
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

    protected void setState(State state) {
        if (this.state != state) {
            if (!this.state.isValidNextState(state)) {
                getLogger().warning("Invalid state traversal " + this.state.name() + " -> " + state.name());
                getLogger().warning("We'll allow this for now for development purposes.");
            }
            ModuleStateChangedEvent event = new ModuleStateChangedEvent(this, this.state, state);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.state = state;
                onStateChanged(this.state);
            }
        }
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

        private final boolean loaded;
        private final Set<State> nextPossibleStates = new HashSet<>();

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
