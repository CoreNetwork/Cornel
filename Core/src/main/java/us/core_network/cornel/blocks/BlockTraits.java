package us.core_network.cornel.blocks;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

public class BlockTraits {
    /**
     * Set of materials that can't be pushed by pistons
     */
    public static final Set<Material> NO_PISTON_PUSH_BLOCKS = new HashSet<>();

    /**
     * Set of materials that are fluids
     */
    public static final Set<Material> FLUID_BLOCKS = new HashSet<>();

    private static void populate(Set<Material> set, Material... mats) {
        Collections.addAll(set, mats);
    }

    static {
        populate(NO_PISTON_PUSH_BLOCKS, Material.OBSIDIAN, Material.FURNACE, Material.BURNING_FURNACE,
                Material.CHEST, Material.TRAPPED_CHEST, Material.HOPPER, Material.ENCHANTMENT_TABLE,
                Material.BREWING_STAND);

        populate(FLUID_BLOCKS, Material.WATER, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.LAVA);
    }
}
