package us.core_network.cornel.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

public class BlockUtil
{
    /**
     * Check if Block is in inside world border.
     * @param block Block to check.
     * @return <code>true</code> if block is inside world border.
     */
    public static boolean isInWorldBorderBounds(Block block)
    {
        net.minecraft.server.v1_8_R2.WorldBorder nmsWorldBorder = ((CraftWorld) block.getWorld()).getHandle().getWorldBorder();
        double halfSize = nmsWorldBorder.getSize() / 2;
        return      block.getX() > nmsWorldBorder.getCenterX() - halfSize
                &&  block.getX() < nmsWorldBorder.getCenterX() + halfSize
                &&  block.getZ() > nmsWorldBorder.getCenterZ() - halfSize
                &&  block.getZ() < nmsWorldBorder.getCenterZ() + halfSize;
    }

    /**
     * @return Location in the center of the specified block.
     */
    public static Location getLocationInBlockCenter(Block block)
    {
        return new Location(block.getWorld(), block.getX() + 0.5, block.getY(), block.getZ() + 0.5);
    }
}
