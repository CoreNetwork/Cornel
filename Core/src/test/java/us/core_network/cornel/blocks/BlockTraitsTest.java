package us.core_network.cornel.blocks;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.Material;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTraitsTest
{
    @Test
    public void testBlockTraits() throws Exception
    {
        assertFalse(BlockTraits.NO_PISTON_PUSH_BLOCKS.contains(Material.DIRT));
        assertTrue(BlockTraits.NO_PISTON_PUSH_BLOCKS.contains(Material.OBSIDIAN));

        assertFalse(BlockTraits.FLUID_BLOCKS.contains(Material.DIRT));
        assertTrue(BlockTraits.FLUID_BLOCKS.contains(Material.WATER));
    }
}