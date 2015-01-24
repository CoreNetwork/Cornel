package us.core_network.cornel.items;

import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.junit.Test;

import static org.junit.Assert.*;

public class NbtUtilTest
{
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

        NbtUtil.combineTags(destination, source);

        assertEquals(destination.getString("text"), "sourceText");
        assertEquals(destination.getInt("number"), 100);
        assertEquals(destination.getBoolean("exists"), true);
    }
}