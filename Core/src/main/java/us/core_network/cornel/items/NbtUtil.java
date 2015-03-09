package us.core_network.cornel.items;

import java.util.Set;
import net.minecraft.server.v1_8_R1.NBTTagCompound;

public class NbtUtil
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
}
