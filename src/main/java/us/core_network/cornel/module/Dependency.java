package us.core_network.cornel.module;

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
