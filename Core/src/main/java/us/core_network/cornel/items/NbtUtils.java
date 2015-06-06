package us.core_network.cornel.items;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import net.minecraft.server.v1_8_R3.NBTReadLimiter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.inventory.ItemStack;

public class NbtUtils
{
    /**
     * Adds all tags from source NBTTagCompound into destination NBTTagCompound, overwriting existing tags if they exist.
     * @param destination NBTTagCompound that will recieve elements from source.
     * @param source NBTTagCompound containing elements that will be added to destination tag.
     */
    public static void combineTags(NBTTagCompound destination, NBTTagCompound source)
    {
        Set<String> tagKeys = source.c();
        for (String key : tagKeys) {
            destination.set(key, source.get(key));
        }
    }

    /**
     * Convenience function to directly get lore from NBT tag
     * @param tag NBT tag to get lore from.
     * @return NBTTagList that is lore of the item or <code>null</code> if one does not exist.
     */
    public static NBTTagList getLore(NBTTagCompound tag)
    {
        if (tag == null)
            return null;

        NBTTagCompound displayTag = (NBTTagCompound) tag.get("display");
        if (displayTag == null)
            return null;

        return (NBTTagList) displayTag.get("Lore");
    }


    /**
     * Replace all occurences of the string with another string in NBT tag that contains item title and lore.
     * @param tag NBT tag to replace occurences in.
     * @param source String to search for.
     * @param replacement Replacement string.
     */
    public static void replaceStringInNBT(NBTTagCompound tag, String source, String replacement)
    {
        if (tag == null)
            return;

        NBTTagCompound displayTag = (NBTTagCompound) tag.get("display");
        if (displayTag == null)
            return;


        if (displayTag.hasKey("Name"))
            displayTag.setString("Name", displayTag.getString("Name").replace(source, replacement));

        if (displayTag.hasKey("Lore"))
        {
            NBTTagList lore = (NBTTagList) displayTag.get("Lore");
            NBTTagList newLore = new NBTTagList();
            for (int i = 0; i < lore.size(); i++)
            {
                newLore.add(new NBTTagString(lore.getString(i).replace(source, replacement)));
            }

            displayTag.set("Lore", newLore);
        }
    }

    /**
     * Function that Extracts NBT tags from the item as byte array.
     * @param stack Item to get stack from.
     * @return NBT tag converted to byte array.
     */
    public static byte[] getNBT(net.minecraft.server.v1_8_R3.ItemStack stack)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(byteStream);
        NBTTagCompound tag = stack.getTag();
        if (tag == null)
            return new byte[0];

        try {
            Method method = NBTTagCompound.class.getDeclaredMethod("write", DataOutput.class);
            method.setAccessible(true);

            method.invoke(tag, dataOutput);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return byteStream.toByteArray();
    }

    public static NBTReadLimiter UNLIMTED_NBT_READER_INSTANCE = new UnlimitedNBTLimiter();
    private static class UnlimitedNBTLimiter extends NBTReadLimiter
    {
        public UnlimitedNBTLimiter()
        {
            super(0);
        }

        @Override
        public void a(long l)
        {
        }
    }

    /**
     * Loads NBT tag from byte array and inserts it into item.
     * @param nbt byte array to load NBT stack from.
     * @param stack Item stack to load NBT stack into.
     */
    public static void loadNBT(byte[] nbt, net.minecraft.server.v1_8_R3.ItemStack stack)
    {
        if (nbt == null || nbt.length == 0)
            return;

        NBTTagCompound tag = new NBTTagCompound();

        ByteArrayInputStream stream = new ByteArrayInputStream(nbt);
        DataInputStream dataInput = new DataInputStream(stream);

        try {
            Method method = NBTTagCompound.class.getDeclaredMethod("load", DataInput.class, Integer.TYPE, NBTReadLimiter.class);
            method.setAccessible(true);

            method.invoke(tag, dataInput, 0, UNLIMTED_NBT_READER_INSTANCE);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        stack.setTag(tag);
    }


}
