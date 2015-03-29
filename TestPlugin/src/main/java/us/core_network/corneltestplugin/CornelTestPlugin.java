package us.core_network.corneltestplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class CornelTestPlugin extends JavaPlugin {
    private CoreModule root;
    private SubModule sub;
    @Override
    public void onEnable() {
        root = new CoreModule(this);
        sub = new SubModule(root);
        root.loadInternally();
    }

    @Override
    public void onDisable() {
        root.unloadInternally();
    }
}
