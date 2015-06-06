package us.core_network.cornel.items;

import net.minecraft.server.v1_8_R3.DispenserRegistry;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import net.minecraft.server.v1_8_R3.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.CoalType;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryUtilTest
{
    @Before
    public void initMinecraftBlocks()
    {
        DispenserRegistry.c(); //Initialize dispenser registry before accessing Blocks class (Minecraft code complains otherwise)
    }

    @Test
    public void testInventoryUtil() throws Exception
    {
        //Bukkit stuff is not working  properly without actual server running so lets resort to NMS with bukkit proxy.
        IInventory nmsInventory = new PlayerInventory(null);
        Inventory inventory = new CraftInventory(nmsInventory);

        assertEquals(InventoryUtil.getFreeInventorySlots(inventory), 40);

        nmsInventory.setItem(0, new net.minecraft.server.v1_8_R3.ItemStack(Items.COAL, 2, CoalType.COAL.getData()));
        assertEquals(InventoryUtil.getFreeInventorySlots(inventory), 39);

        InventoryUtil.removeItems(inventory, Material.COAL, Short.MAX_VALUE, 1);
        assertEquals(InventoryUtil.getFreeInventorySlots(inventory), 39);
        assertEquals(nmsInventory.getItem(0).count, 1);

        nmsInventory.setItem(1,  new net.minecraft.server.v1_8_R3.ItemStack(Items.COAL, 1, CoalType.COAL.getData()));
        nmsInventory.setItem(2,  new net.minecraft.server.v1_8_R3.ItemStack(Items.COAL, 10, CoalType.CHARCOAL.getData()));

        InventoryUtil.removeItems(inventory, Material.COAL, CoalType.COAL.getData(), 2);
        assertSame(nmsInventory.getItem(0), null);
        assertSame(nmsInventory.getItem(1), null);
        assertEquals(nmsInventory.getItem(2).count, 10);
    }
}