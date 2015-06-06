package us.core_network.cornel.items;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.DispenserRegistry;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NbtUtilTest
{
    @Before
    public void initMinecraftBlocks()
    {
        DispenserRegistry.c(); //Initialize dispenser registry before accessing Blocks class (Minecraft code complains otherwise)
    }

    @Test
    public void testCombineTags() throws Exception
    {
        NBTTagCompound source = new NBTTagCompound();
        NBTTagCompound destination = new NBTTagCompound();

        source.setString("text", "sourceText");
        source.setInt("number", 100);

        destination.setString("text", "destinationText");
        destination.setInt("number", 200);
        destination.setBoolean("exists", true);

        NbtUtils.combineTags(destination, source);

        assertEquals(destination.getString("text"), "sourceText");
        assertEquals(destination.getInt("number"), 100);
        assertEquals(destination.getBoolean("exists"), true);
    }

    @Test
    public void testReplaceStringInNBT() throws Exception
    {
        ItemStack nmsItemStack = new ItemStack(Blocks.DIRT, 1);
        CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(nmsItemStack);

        ItemMeta meta = craftItemStack.getItemMeta();
        meta.setDisplayName("Test <VariableA> Test");

        List<String> lore = Arrays.asList(new String[] { "Test <VariableB> Test", "Test <VariableC> Test" });
        meta.setLore(lore);

        craftItemStack.setItemMeta(meta);

        NBTTagCompound tag = nmsItemStack.getTag();
        NbtUtils.replaceStringInNBT(tag, "<VariableA>", "A");
        NbtUtils.replaceStringInNBT(tag, "<VariableB>", "B");
        NbtUtils.replaceStringInNBT(tag, "<VariableC>", "C");

        meta = craftItemStack.getItemMeta();

        assertEquals("Test A Test", meta.getDisplayName());
        assertEquals("Test B Test", meta.getLore().get(0));
        assertEquals("Test C Test", meta.getLore().get(1));
    }

    @Test
    public void testTagLoadingSaving() throws Exception
    {
        ItemStack nmsItemStack = new ItemStack(Blocks.DIRT, 1);
        CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(nmsItemStack);

        ItemMeta meta = craftItemStack.getItemMeta();
        meta.setDisplayName("TestName");
        craftItemStack.setItemMeta(meta);

        byte[] tagData = NbtUtils.getNBT(nmsItemStack);

        nmsItemStack = new ItemStack(Blocks.DIRT, 1);
        NbtUtils.loadNBT(tagData, nmsItemStack);

        assertEquals("TestName", CraftItemStack.asCraftMirror(nmsItemStack).getItemMeta().getDisplayName());

    }
}