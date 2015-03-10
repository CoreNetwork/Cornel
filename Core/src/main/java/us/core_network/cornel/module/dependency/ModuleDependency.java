package us.core_network.cornel.module.dependency;

import us.core_network.cornel.module.Module;

public class ModuleDependency  extends Dependency {
    private String moduleName;

    protected ModuleDependency(String moduleName, Module module) {
        super(module);
        this.moduleName = moduleName;
    }

    @Override
    public boolean isMet() {
        return false;
    }

    @Override
    public void registerListeners() {

    }
}
