package us.core_network.cornel.custom;

import net.minecraft.server.v1_8_R1.DispenserRegistry;
import net.minecraft.server.v1_8_R1.IInventory;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagList;
import net.minecraft.server.v1_8_R1.NBTTagString;
import net.minecraft.server.v1_8_R1.PlayerInventory;
import org.bukkit.ChatColor;
import org.bukkit.CoalType;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;
import us.core_network.cornel.common.Messages;
import us.core_network.cornel.items.InventoryUtil;

import static org.junit.Assert.*;

public class PerksUtilTest
{
    @Before
    public void initMinecraftBlocks()
    {
        DispenserRegistry.c(); //Initialize dispenser registry before accessing Blocks class (Minecraft code complains otherwise)
    }

    @Test
    public void testGoldenNames() throws Exception
    {
        //Bukkit stuff is not working  properly without actual server running so lets resort to NMS with bukkit proxy.
        ItemStack nmsItem = new net.minecraft.server.v1_8_R1.ItemStack(Items.ARMOR_STAND);
        assertFalse(PerksUtil.hasGoldenName(nmsItem));

        CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(nmsItem);
        ItemMeta meta = craftItemStack.getItemMeta();
        meta.setDisplayName(Messages.applyFormattingCodes("&6foo"));
        craftItemStack.setItemMeta(meta);
        assertTrue(PerksUtil.hasGoldenName(nmsItem));
    }

    @Test
    public void testPatternNBT() throws Exception
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList patterns = new NBTTagList();
        tag.set("Patterns", patterns);

        NBTTagCompound pattern = new NBTTagCompound();
        pattern.set("Pattern", new NBTTagString("foo"));
        patterns.add(pattern);
        assertFalse(PerksUtil.isSupposedToBePerkBanner(tag));

        pattern = new NBTTagCompound();
        pattern.set("Pattern", new NBTTagString("bri"));
        patterns.add(pattern);
        assertTrue(PerksUtil.isSupposedToBePerkBanner(tag));
    }

    @Test
    public void testArmorStandNBT() throws Exception
    {
        NBTTagCompound tag = new NBTTagCompound();
        assertFalse(PerksUtil.isSupposedToBePerkArmorStand(tag));

        tag.setBoolean("ShowArms", false);
        assertFalse(PerksUtil.isSupposedToBePerkArmorStand(tag));

        tag.setBoolean("ShowArms", true);
        assertTrue(PerksUtil.isSupposedToBePerkArmorStand(tag));
    }

}