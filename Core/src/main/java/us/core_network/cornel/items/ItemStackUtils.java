package us.core_network.cornel.items;

import java.util.List;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.core_network.cornel.java.ReflectionUtils;

public class ItemStackUtils
{
    /**
     * Get NMS item stack from bukkit item stack. This is much faster than {@link org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack#asNMSCopy(org.bukkit.inventory.ItemStack) CraftItemStack.asNMSCopy} as it just retrieves internal variable rather than rebuilding whole NMS stack from scratch.
     * @param bukkitItemStack Bukkit item stack.
     * @return NMS item stack or <code>null</code> if provided bukkit stack is not CraftItemStack and does not contain NMS item stack.
     */
    public static net.minecraft.server.v1_8_R1.ItemStack getInternalNMSStack(ItemStack bukkitItemStack)
    {
        if (!(bukkitItemStack instanceof CraftItemStack))
            return null;

        return (net.minecraft.server.v1_8_R1.ItemStack) ReflectionUtils.get(CraftItemStack.class, bukkitItemStack, "handle");
    }

    /**
     * Gets custom name from the NMS itemstack.
     * @param nmsStack Stack to get name from.
     * @return Custom name or <code>null</code> if not set.
     */
    public static String getStackName(net.minecraft.server.v1_8_R1.ItemStack nmsStack)
    {
        if (nmsStack == null)
            return null;

        if (!nmsStack.hasTag())
            return null;

        NBTTagCompound displayTag = (NBTTagCompound) nmsStack.getTag().get("display");
        if (displayTag == null)
            return null;

        if (!displayTag.hasKey("Name"))
            return null;

        return displayTag.getString("Name");
    }

    /**
     * Method to check if NMS stack contains specific tag.
     * @param nmsStack Stack to check tag on.
     * @param tag Name of the tag to check.
     * @return <code>true</code> if NMS stack contains the tag.
     */
    public static boolean hasTag(net.minecraft.server.v1_8_R1.ItemStack nmsStack, String tag)
    {
        if (nmsStack == null)
            return false;

        if (!nmsStack.hasTag())
            return false;

        return nmsStack.getTag().hasKey(tag);
    }

    /**
     * Replace all occurences of the string with another string in item's title and lore.
     * @param item Item to replace strings in.
     * @param source String to search for.
     * @param replacement Replacement for said string.
     * @return Copy of the ItemStack with replaced strings.
     */
    public static ItemStack replaceStringInItem(ItemStack item, String source, String replacement)
    {
        if (!item.hasItemMeta())
            return item;

        item = item.clone();

        ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName())
            meta.setDisplayName(meta.getDisplayName().replace(source, replacement));

        if (meta.hasLore())
        {
            List<String> lore = meta.getLore();
            for (int i = 0; i < lore.size(); i++)
            {
                lore.set(i, lore.get(i).replace(source, replacement));
            }

            meta.setLore(lore);
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Checks if given ItemStacks contain exact same item type. Includes check for durability and NBT tags.
     * @param a First ItemStack.
     * @param b Second ItemStack
     * @return <code>true</code> if ItemStacks contains same item.
     */
    public static boolean areItemsEqual(net.minecraft.server.v1_8_R1.ItemStack a, net.minecraft.server.v1_8_R1.ItemStack b)
    {
        if (a.getItem() != b.getItem())
            return false;

        // Item.k() returns true if durability of the item matters.
        if (a.getData() != Short.MAX_VALUE && b.getData() != Short.MAX_VALUE && a.getItem().k() && a.getData() != b.getData())
            return false;

        if (a.hasTag() != b.hasTag())
            return false;

        return !a.hasTag() || a.getTag().equals(b.getTag());
    }
}
