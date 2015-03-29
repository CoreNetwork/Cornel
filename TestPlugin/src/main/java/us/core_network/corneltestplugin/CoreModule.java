package us.core_network.corneltestplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import us.core_network.cornel.java.CLevel;
import us.core_network.cornel.module.Module;

public class CoreModule extends Module implements Listener {
    protected CoreModule(Plugin plugin) {
        super(plugin, "Root", "testplugin.yml", TestPluginConfig.class);
    }

    @Override
    protected void onEnable() {
        getLogger().info("Config value for root is: " + TestPluginConfig.testValue);

        getBukkitManager().registerEvents(this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        getLogger().log(CLevel.POSITIVE, event.getMessage());
        if (event.getMessage().equals("disable")) {
            unloadInternally();
        }
    }
}
