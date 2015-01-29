package us.core_network.cornel.module;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 * Use this dependency if your module depends on another plugin. It will be loaded as soon as the plugin is enabled
 */
public class PluginDependency extends Dependency implements Listener {
    private String pluginName;

    public PluginDependency(String pluginName, Module module) {
        super(module);
        this.pluginName = pluginName;
    }


    @Override
    public boolean isMet() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public void registerListeners() {
        getModule().registerEvents(this);
    }

    @EventHandler
    public void onPluginEnabled(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals(pluginName)) {
            getModule().resolveDependency(this);
        }
    }
}
