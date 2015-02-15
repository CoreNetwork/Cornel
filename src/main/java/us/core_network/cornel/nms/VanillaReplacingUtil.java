package us.core_network.cornel.nms;

import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R1.BiomeBase;
import net.minecraft.server.v1_8_R1.BiomeMeta;
import net.minecraft.server.v1_8_R1.EntityTypes;
import us.core_network.cornel.java.ReflectionUtils;

/**
 * Util for replacing vanilla stuff with our stuff (like entities or blocks)
 */
public class VanillaReplacingUtil
{
    /**
     * Replaces vanilla mob class with our custom class.
     * @param name Vanilla name of the entity (Savegame ID on <a href="http://minecraft.gamepedia.com/Data_values#Entity_IDs">Minecraft Wiki</a>).
     * @param id Vanilla ID of the entity.
     * @param oldClass Vanilla entity class.
     * @param newClass Our new entity class.
     */
    public static void replaceMob(String name, int id, Class oldClass, Class newClass)
    {
        //Replace  entity type
        ((Map) ReflectionUtils.getStatic(EntityTypes.class, "c")).put(name, newClass);
        ((Map) ReflectionUtils.getStatic(EntityTypes.class, "d")).put(newClass, name);
        ((Map) ReflectionUtils.getStatic(EntityTypes.class, "e")).put(id, newClass);
        ((Map) ReflectionUtils.getStatic(EntityTypes.class, "f")).put(newClass, id);

        //Replace all entity types in biomes
        BiomeBase[] biomes = (BiomeBase[]) ReflectionUtils.getStatic(BiomeBase.class, "biomes");
        for (BiomeBase biome : biomes)
        {
            if (biome == null)
                continue;

            replaceMobsInBiomeMeta((List<BiomeMeta>) ReflectionUtils.get(BiomeBase.class, biome, "aw"), oldClass, newClass);
            replaceMobsInBiomeMeta((List<BiomeMeta>) ReflectionUtils.get(BiomeBase.class, biome, "at"), oldClass, newClass);
            replaceMobsInBiomeMeta((List<BiomeMeta>) ReflectionUtils.get(BiomeBase.class, biome, "au"), oldClass, newClass);
            replaceMobsInBiomeMeta((List<BiomeMeta>) ReflectionUtils.get(BiomeBase.class, biome, "av"), oldClass, newClass);
        }

    }

    private static void replaceMobsInBiomeMeta(List<BiomeMeta> meta, Class oldClass, Class newClass)
    {
        for (BiomeMeta m : meta)
        {
            if (m.b.equals(oldClass))
            {
                m.b = newClass;
            }
        }
    }

}

