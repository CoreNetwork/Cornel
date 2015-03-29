package us.core_network.corneltestplugin;

import us.core_network.cornel.module.Module;

public class SubModule extends Module {
    protected SubModule(Module parent) {
        super(parent, "SubModule", "parent:submod", SubModuleConfig.class);
    }

    @Override
    protected void onEnable() {
        getLogger().info("Config value sub is " + SubModuleConfig.otherValue);
    }
}
