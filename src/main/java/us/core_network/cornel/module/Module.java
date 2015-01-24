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
import java.util.stream.Stream;

public abstract class Module {
    private Module parent = null;
    private Set<Module> children = new HashSet<>();
    private Plugin plugin = null;
    private Set<RegisteredListener> registeredListeners = new HashSet<>();
    private Set<Dependency> dependencies = new HashSet<>();
    private boolean loaded = false;

    protected Module(Module parent) {
        this.parent = parent;
        this.parent.children.add(this);
    }

    protected Module(Plugin plugin) {
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

    public boolean isLoaded() {
        return loaded;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    /**
     * Called after every dependency is fulfilled and the config file has been loaded
     */
    public abstract void onLoad();

    /**
     * Called before the module is unloaded
     */
    public abstract void onUnload();

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
        if (allMet && !isLoaded()) {
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
        if (isLoaded()) {
            return;
        }
        if (!resolveDependencies()) {
            return;
        }
        if (getParent() != null && !getParent().isLoaded()) {
            return;
        }

        try {
            onLoad();
        } catch (Exception e) {
            // TODO log
            e.printStackTrace();
            return;
        }

        children.forEach(Module::loadInternally);

        loaded = true;
    }

    public void unloadInternally() {
        // remove all listeners and timers
        children.forEach(Module::unloadInternally);

        try {
            onUnload();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loaded = false;
    }
}
