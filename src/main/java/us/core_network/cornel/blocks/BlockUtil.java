package us.core_network.cornel.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

public class BlockUtil
{
    /**
     * Check if Block is in inside world border.
     * @param block Block to check.
     * @return <code>true</code> if block is inside world border.
     */
    public static boolean isInWorldBorderBounds(Block block)
    {
        net.minecraft.server.v1_8_R1.WorldBorder nmsWorldBorder = ((CraftWorld) block.getWorld()).getHandle().af();
        double halfSize = nmsWorldBorder.h() / 2;
        return      block.getX() > nmsWorldBorder.f() - halfSize
                &&  block.getX() < nmsWorldBorder.f() + halfSize
                &&  block.getZ() > nmsWorldBorder.g() - halfSize
                &&  block.getZ() < nmsWorldBorder.g() + halfSize;
    }

    /**
     * @return Location in the center of the specified block.
     */
    public static Location getLocationInBlockCenter(Block block)
    {
        return new Location(block.getWorld(), block.getX() + 0.5, block.getY(), block.getZ() + 0.5);
    }
}
