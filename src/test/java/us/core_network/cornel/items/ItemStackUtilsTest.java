package us.core_network.cornel.items;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_8_R1.Block;
import net.minecraft.server.v1_8_R1.Blocks;
import net.minecraft.server.v1_8_R1.DispenserRegistry;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemStackUtilsTest
{
    @Before
    public void initMinecraftBlocks()
    {
        DispenserRegistry.c(); //Initialize dispenser registry before accessing Blocks class (Minecraft code complains otherwise)
    }

    @Test
    public void testGetInternalNMSStack() throws Exception
    {
        ItemStack nmsItemStack = new ItemStack(Blocks.DIRT, 1);
        CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(nmsItemStack);

        EntityDamageEvent event = new EntityDamageEvent(null, null, 10);

        assertSame(nmsItemStack, ItemStackUtils.getInternalNMSStack(craftItemStack));
    }

    @Test
    public void testGetStackName() throws Exception
    {
        ItemStack nmsItemStack = new ItemStack(Blocks.DIRT, 1);
        CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(nmsItemStack);

        ItemMeta meta = craftItemStack.getItemMeta();
        meta.setDisplayName("TestName");
        craftItemStack.setItemMeta(meta);

        assertEquals(ItemStackUtils.getStackName(nmsItemStack), "TestName");
    }

    @Test
    public void testHasTag() throws Exception
    {
        assertFalse(ItemStackUtils.hasTag(null, "dummy"));

        ItemStack nmsItemStack = new ItemStack(Blocks.DIRT, 1);
        CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(nmsItemStack);

        ItemMeta meta = craftItemStack.getItemMeta();
        meta.setDisplayName("TestName");
        craftItemStack.setItemMeta(meta);

        assertTrue(ItemStackUtils.hasTag(nmsItemStack, "display"));
        assertFalse(ItemStackUtils.hasTag(nmsItemStack, "HideFlags"));
    }

    @Test
    public void testReplaceStringsInItem() throws Exception
    {
        ItemStack nmsItemStack = new ItemStack(Blocks.DIRT, 1);
        org.bukkit.inventory.ItemStack stack = CraftItemStack.asCraftMirror(nmsItemStack);

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("Test <VariableA> Test");

        List<String> lore = Arrays.asList(new String[]{"Test <VariableB> Test", "Test <VariableC> Test"});
        meta.setLore(lore);

        stack.setItemMeta(meta);

        stack = ItemStackUtils.replaceStringInItem(stack, "<VariableA>", "A");
        stack = ItemStackUtils.replaceStringInItem(stack, "<VariableB>", "B");
        stack = ItemStackUtils.replaceStringInItem(stack, "<VariableC>", "C");

        meta = stack.getItemMeta();

        assertEquals("Test A Test", meta.getDisplayName());
        assertEquals("Test B Test", meta.getLore().get(0));
        assertEquals("Test C Test", meta.getLore().get(1));
    }
}