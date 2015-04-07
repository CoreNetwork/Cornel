package us.core_network.cornel.items;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_8_R2.Block;
import net.minecraft.server.v1_8_R2.Blocks;
import net.minecraft.server.v1_8_R2.DispenserRegistry;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.Items;
import net.minecraft.server.v1_8_R2.Material;
import org.bukkit.CoalType;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
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

    @Test
    public void testAreItemsEqual() throws Exception
    {
        //Two equal items
        ItemStack a = new ItemStack(Items.COAL, 1);
        ItemStack b = new ItemStack(Items.COAL, 10);
        assertTrue(ItemStackUtils.areItemsEqual(a, b));

        //Two equal materials with different durabilities
        a.setData(CoalType.COAL.getData());
        b.setData(CoalType.CHARCOAL.getData());
        assertFalse(ItemStackUtils.areItemsEqual(a, b));

        //Two different durabilities, but with item type that ignores durability
        a = new ItemStack(Items.COMPASS, 1, 100);
        b = new ItemStack(Items.COMPASS, 1, 10);
        assertTrue(ItemStackUtils.areItemsEqual(a, b));

        //Two items without tags
        a = new ItemStack(Items.SKULL, 1);
        b = new ItemStack(Items.SKULL, 1);
        assertTrue(ItemStackUtils.areItemsEqual(a, b));

        CraftItemStack craftStackA = CraftItemStack.asCraftMirror(a);
        CraftItemStack craftStackB = CraftItemStack.asCraftMirror(b);
        SkullMeta metaA = (SkullMeta) craftStackA.getItemMeta();
        SkullMeta metaB = (SkullMeta) craftStackB.getItemMeta();
        metaA.setOwner("OwnerA");
        metaB.setOwner("OwnerB");

        //Item with tag and item without tag
        craftStackA.setItemMeta(metaA);
        assertFalse(ItemStackUtils.areItemsEqual(a, b));

        //Two items with different tags
        craftStackB.setItemMeta(metaB);
        assertFalse(ItemStackUtils.areItemsEqual(a, b));

        //Two items with same tags
        metaB.setOwner("OwnerA");
        craftStackB.setItemMeta(metaB);
        assertTrue(ItemStackUtils.areItemsEqual(a, b));
    }
}