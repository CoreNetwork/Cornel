package us.core_network.cornel.module.dependency;

import us.core_network.cornel.module.Module;

public abstract class Dependency {
    private Module module;

    protected Dependency(Module module) {
        this.module = module;
    }

    public abstract boolean isMet();

    public abstract void registerListeners();

    public Module getModule() {
        return module;
    }
}
