package us.core_network.cornel.module;

import com.flowpowered.cerealization.config.*;
import com.flowpowered.cerealization.config.annotated.AnnotatedObjectConfiguration;
import com.flowpowered.cerealization.config.annotated.AnnotatedSubclassConfiguration;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

public class RootConfiguration extends AnnotatedSubclassConfiguration {
    public static final String CONFIG_PATH_CONSTANT = "CONFIG_PATH_CONSTANT";
    Object configObject;
    AnnotatedObjectConfiguration objectConfiguration;
    Configuration base;
    Set<String> keys = new LinkedHashSet<>();

    public RootConfiguration(Configuration baseConfig, Object configObject) {
        super(baseConfig);
        this.base = baseConfig;
        this.configObject = configObject;
        objectConfiguration = new AnnotatedObjectConfiguration(new MapConfiguration());
        objectConfiguration.add(configObject, CONFIG_PATH_CONSTANT);


    }

    @Override
    public void load(ConfigurationNodeSource source) throws ConfigurationException {
        super.load(source);
        objectConfiguration.load();
        ConfigurationNode nodeToLoad = objectConfiguration.getChild(CONFIG_PATH_CONSTANT);
        nodeToLoad.getKeys(true).forEach(key -> nodeToLoad.getChild(key).setValue(base.getChild(key).getValue(nodeToLoad.getChild(key))));
        objectConfiguration.load(objectConfiguration.getConfiguration());
    }

    @Override
    public void save(ConfigurationNodeSource source) throws ConfigurationException {
        objectConfiguration.save();
        ConfigurationNode nodeToSave = objectConfiguration.getChild(CONFIG_PATH_CONSTANT);
        for (String key : nodeToSave.getKeys(true)) {
            source.getChild(key).setValue(nodeToSave.getNode(key).getValue());
        }
        super.save(source);
    }
}
