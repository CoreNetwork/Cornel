package us.core_network.cornel.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Storage class that contains block and some parameters required for placing the block relative to the origin.
 * Arrays of these can be made easily in code and then pasted in.
 */
public class SchematicBlock {
	private int modX;
    private int modY;
    private int modZ;
    private int rotation;
    private Material material;
    private boolean onlyInAir;

    /**
     * @param modX X offset of this block away from the origin block.
     * @param modY Y offset of this block away from the origin block.
     * @param modZ Z offset of this block away from the origin block.
     * @param material Material of the block.
     */
	public SchematicBlock(int modX, int modY, int modZ, Material material)
	{
		this(modX, modY, modZ, material, false);
	}

    /**
     * @param modX X offset of this block away from the origin block.
     * @param modY Y offset of this block away from the origin block.
     * @param modZ Z offset of this block away from the origin block.
     * @param material Material of the block.
     * @param onlyInAir Should this block only be placed if there is air below (as a ledge for example).
     */
	public SchematicBlock(int modX, int modY, int modZ, Material material, boolean onlyInAir)
	{
		this.modX = modX;
		this.modY = modY;
		this.modZ = modZ;
		this.material = material;
		this.onlyInAir = onlyInAir;
		rotation = 0;
	}

    /**
     * Place array of {@link us.core_network.cornel.blocks.SchematicBlock} with specified origin block.
     * @param schematic Array of {@link us.core_network.cornel.blocks.SchematicBlock}.
     * @param origin Origin block.
     */
	public static void placeSchematic(SchematicBlock[] schematic, Block origin)
	{
		for (SchematicBlock sBlock : schematic)
		{
			
			Block block = origin.getRelative(sBlock.modX, sBlock.modY, sBlock.modZ);

            //Do not paste over bedrock
			if (block.getType() == Material.BEDROCK)
			{
				continue;
			}

			if (sBlock.onlyInAir && block.getRelative(BlockFace.DOWN).getType().isSolid())
			{
				continue;
			}
			
			if (sBlock.material == Material.PORTAL)
			{
				byte data;
				if (sBlock.rotation == 1 || sBlock.rotation == 3)
					data = 2;
				else
					data = 1;

				block.setTypeIdAndData(Material.PORTAL.getId(), data, false);
				continue;
			}
			
			block.setType(sBlock.material);
		}
	}

    /**
     * Relatively rotate array of {@link us.core_network.cornel.blocks.SchematicBlock} around the origin
     * @param original Original array of {@link us.core_network.cornel.blocks.SchematicBlock}.
     * @param rotation Clockwise rotation number (<code>0</code> = 0 degrees, <code>1</code> = 90 degrees, <code>2</code> - 180 degrees, <code>0</code> = 270 degrees)
     * @return Rotated array of {@link us.core_network.cornel.blocks.SchematicBlock}.
     */
	public static SchematicBlock[] getRotatedSchematic(SchematicBlock[] original, int rotation)
	{
		if (rotation == 0)
		{
			return original;
		}
		else if (rotation == 1)
		{
			SchematicBlock[] schematic = new SchematicBlock[original.length];
			for (int i = 0; i < original.length; i++)
			{
				schematic[i] = new SchematicBlock(-original[i].modZ, original[i].modY, original[i].modX, original[i].material, original[i].onlyInAir);
				schematic[i].rotation = (original[i].rotation + rotation) % 4;
			}
			return schematic;
		}
		else if (rotation == 2)
		{
			SchematicBlock[] schematic = new SchematicBlock[original.length];
			for (int i = 0; i < original.length; i++)
			{
				schematic[i] = new SchematicBlock(original[i].modZ, original[i].modY, -original[i].modX, original[i].material, original[i].onlyInAir);
                schematic[i].rotation = (original[i].rotation + rotation) % 4;
			}
			return schematic;
		}
		else
		{
			SchematicBlock[] schematic = new SchematicBlock[original.length];
			for (int i = 0; i < original.length; i++)
			{
				schematic[i] = new SchematicBlock(-original[i].modZ, original[i].modY, original[i].modX, original[i].material, original[i].onlyInAir);
                schematic[i].rotation = (original[i].rotation + rotation) % 4;
			}
			return schematic;
		}
	}
}
