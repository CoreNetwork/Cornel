package us.core_network.cornel.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R1.NBTBase;
import net.minecraft.server.v1_8_R1.NBTTagByte;
import net.minecraft.server.v1_8_R1.NBTTagByteArray;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagDouble;
import net.minecraft.server.v1_8_R1.NBTTagFloat;
import net.minecraft.server.v1_8_R1.NBTTagInt;
import net.minecraft.server.v1_8_R1.NBTTagIntArray;
import net.minecraft.server.v1_8_R1.NBTTagList;
import net.minecraft.server.v1_8_R1.NBTTagLong;
import net.minecraft.server.v1_8_R1.NBTTagShort;
import net.minecraft.server.v1_8_R1.NBTTagString;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import us.core_network.cornel.misc.ArrayConvert;
import us.core_network.cornel.player.Messages;

public class NbtYaml
{
    /**
     * Folder that contains all YAML NBT tag files.
     */
    public static File YAML_FOLDER = new File("plugins\\Cornel\\nbt");

    /**
     * Loads NBTTagCompound from YAML file.
     * @param fileName Name of the YAML file without .yml extension. File must be in {@link NbtYaml#YAML_FOLDER}.
     * @return Tag read from the file.
     * @throws FileNotFoundException Thrown if YAML does not exist.
     * @throws IOException Thrown if YAML file could not be loaded.
     * @throws InvalidConfigurationException Thrown if provided file is not valid YAML file.
     */
    public static NBTTagCompound loadFromFile(String fileName) throws FileNotFoundException, IOException, InvalidConfigurationException
    {
        YamlConfiguration yaml = new YamlConfiguration();

        File file = new File(YAML_FOLDER, fileName + ".yml");
        yaml.load(file);

        return loadFromNodes(yaml.getValues(false));
    }

    /**
     * Loads NBTTagCompound from YAML node map.
     * @param section YAML node map.
     * @return Tag read from specified node map.
     */
    public static NBTTagCompound loadFromNodes(Map<?, ?> section)
    {
        NBTTagCompound newTag = new NBTTagCompound();

        for (Map.Entry<?, ?> e : section.entrySet())
        {
            NBTBase tag =  loadTag(e.getValue(), e.getKey().equals("compound"));
            newTag.set((String) e.getKey(), tag);
        }

        return newTag;
    }

    /**
     * Load single NBT tag from YAML node.
     * @param tag YAML node.
     * @return NBT Tag read from that node or null if that node is not valid tag.
     */
    public static NBTBase loadTag(Object tag)
    {
        return loadTag(tag, false);
    }

    /**
     * Load single NBT tag from YAML node.
     * @param tag YAML node.
     * @param isCompound Does provided yaml node contain compound node that contains multiple child nodes?
     * @return NBT Tag read from that node or null if that node is not valid tag.
     */
    private static NBTBase loadTag(Object tag, boolean isCompound)
    {
        if (tag instanceof String)
        {
            return new NBTTagString(Messages.applyFormattingCodes((String) tag));
        }
        else if (tag instanceof ArrayList)
        {
            NBTTagList list = new NBTTagList();
            for (Object o : (ArrayList) tag)
                list.add(loadTag(o));

            return list;
        }
        else if (tag instanceof MemorySection || tag instanceof LinkedHashMap)
        {
            Map<String, Object> map;

            if (tag instanceof MemorySection)
            {
                MemorySection section = (MemorySection) tag;
                map = section.getValues(false);
            }
            else
                map = (Map) tag;

            if (isCompound)
            {
                NBTTagCompound compound = new NBTTagCompound();

                for (Map.Entry<String, Object> ee : map.entrySet())
                {
                    NBTBase eTag = loadTag(ee.getValue(), ee.getKey().equals("compound"));
                    compound.set(ee.getKey(), eTag);
                }

                return compound;
            }

            for (Map.Entry<String, Object> e : map.entrySet())
            {
                if (e.getKey().equals("byte"))
                {
                    return new NBTTagByte((byte) (int) (Integer) e.getValue());
                }
                else if (e.getKey().equals("short"))
                {
                    return new NBTTagShort((short) (int) (Integer) e.getValue());
                }
                else if (e.getKey().equals("int"))
                {
                    return new NBTTagInt((Integer) e.getValue());
                }
                else if (e.getKey().equals("long"))
                {
                    return new NBTTagLong((long) (int) (Integer) e.getValue());
                }
                else if (e.getKey().equals("float"))
                {
                    return new NBTTagFloat((float) (int) (Integer) e.getValue());
                }
                else if (e.getKey().equals("double"))
                {
                    return new NBTTagDouble((double) (int) (Integer) e.getValue());
                }
                else if (e.getKey().equals("byteArray"))
                {
                    return new NBTTagByteArray(ArrayConvert.convert(((ArrayList<Integer>) e.getValue()).toArray(new Byte[0])));
                }
                else if (e.getKey().equals("intArray"))
                {
                    return new NBTTagIntArray(ArrayConvert.convert(((ArrayList<Integer>) e.getValue()).toArray(new Integer[0])));
                }
                else if (e.getKey().equals("compound"))
                {
                    NBTTagCompound compound = new NBTTagCompound();

                    Map<String, Object> inMap = null;

                    if (e.getValue() instanceof MemorySection)
                    {
                        MemorySection section = (MemorySection) e.getValue();
                        inMap = section.getValues(false);
                    }
                    else
                        inMap = (Map) e.getValue();

                    for (Map.Entry<String, Object> ee : inMap.entrySet())
                    {
                        NBTBase eTag = loadTag(ee.getValue(), ee.getKey().equals("compound"));
                        compound.set(ee.getKey(), eTag);
                    }

                    return compound;
                }
            }
        }

        return null;
    }

    /**
     * Saves NBTTagCompound into YAML file.
     * @param fileName Name of the YAML file without .yml extension. File must be in {@link NbtYaml#YAML_FOLDER}.
     * @param tag tag to save.
     * @throws IOException Thrown if YAML file could not be saved.
     * @throws ReflectiveOperationException Thrown when something went wrong while accessing reflection classes in Minecraft code (usually when class was not properly updated to new Minecraft version).
     */

    public static void saveToFile(String fileName, NBTTagCompound tag) throws IOException, ReflectiveOperationException
    {
        YamlConfiguration yaml = new YamlConfiguration();

        Set<String> tagKeys = tag.c();
        for (String key : tagKeys) {
            addTag(yaml, key, tag.get(key));
        }

        yaml.save(new File(YAML_FOLDER, fileName + ".yml"));
    }

    /**
     * Adds NBT tag to YAML section.
     * @param yaml YAML section to add tag to.
     * @param name Name of the tag.
     * @param tag Tag to add.
     * @throws ReflectiveOperationException Thrown when something went wrong while accessing reflection classes in Minecraft code (usually when class was not properly updated to new Minecraft version).
     */
    public static void addTag(ConfigurationSection yaml, String name, NBTBase tag) throws ReflectiveOperationException{
        switch (tag.getTypeId()) {
            case 1: // Byte
                yaml.set(name + ".byte", ((NBTTagByte) tag).f());
                break;
            case 2: // Short
                yaml.set(name + ".short", ((NBTTagShort) tag).e());
                break;
            case 3: // Integer
                yaml.set(name + ".int", ((NBTTagInt) tag).d());
                break;
            case 4: // Long
                yaml.set(name + ".long", ((NBTTagLong) tag).c());
                break;
            case 5: // Float
                yaml.set(name + ".float", ((NBTTagFloat) tag).h());
                break;
            case 6: // Double
                yaml.set(name + ".double", ((NBTTagDouble) tag).g());
                break;
            case 7: // Byte Array
                yaml.set(name + ".byteArray", ArrayConvert.convert(((NBTTagByteArray) tag).c()));
                break;
            case 11: // Int array
                yaml.set(name + ".intArray", ArrayConvert.convert(((NBTTagIntArray) tag).c()));
                break;
            case 8: // String
                yaml.set(name, ((NBTTagString) tag).a_());
                break;
            case 9: // List
                NBTTagList listTag = (NBTTagList) tag;

                Field listField = NBTTagList.class.getDeclaredField("list");
                listField.setAccessible(true);

                List<NBTBase> tags = (List) listField.get(listTag);

                List list = new ArrayList();
                if (tags.get(0).getTypeId() == 8)
                {
                    for (int i = 0; i < tags.size(); i++) {
                        list.add(((NBTTagString)tags.get(i)).a_());
                    }
                }
                else
                {
                    for (int i = 0; i < tags.size(); i++) {
                        ConfigurationSection listSection = new YamlConfiguration()
                                .createSection("foo");
                        addTagWithoutName(listSection, tags.get(i));
                        list.add(listSection);
                    }
                }


                yaml.set(name, list.toArray());

                break;
            case 10: // Compound
                ConfigurationSection newSection = yaml.createSection(name).createSection("compound");

                NBTTagCompound compoundTag = (NBTTagCompound) tag;
                Set<String> tagKeys = compoundTag.c();
                for (String key : tagKeys) {
                    addTag(newSection, key, compoundTag.get(key));
                }
        }
    }

    /**
     * Adds nameless NBT tag to YAML section. This is used when tag does not have specific name (for example when tag is item in a list).
     * @param yaml YAML section to add tag to.
     * @param tag Tag to add..
     * @throws ReflectiveOperationException Thrown when something went wrong while accessing reflection classes in Minecraft code (usually when class was not properly updated to new Minecraft version).
     */
    public static void addTagWithoutName(ConfigurationSection yaml, NBTBase tag) throws ReflectiveOperationException{
        switch (tag.getTypeId())
        {
            case 1: // Byte
                yaml.set("byte", ((NBTTagByte) tag).f());
                break;
            case 2: // Short
                yaml.set("short", ((NBTTagShort) tag).e());
                break;
            case 3: // Integer
                yaml.set("int", ((NBTTagInt) tag).d());
                break;
            case 4: // Long
                yaml.set("long", ((NBTTagLong) tag).c());
                break;
            case 5: // Float
                yaml.set("float", ((NBTTagFloat) tag).h());
                break;
            case 6: // Double
                yaml.set("double", ((NBTTagDouble) tag).g());
                break;
            case 7: // Byte Array
                yaml.set("byteArray", ArrayConvert.convert(((NBTTagByteArray) tag).c()));
                break;
            case 11: // Int array
                yaml.set("intArray", ArrayConvert.convert(((NBTTagIntArray) tag).c()));
                break;
            case 10: // Compound
                ConfigurationSection newSection = yaml.createSection("compound");

                NBTTagCompound compoundTag = (NBTTagCompound) tag;
                Set<String> tagKeys = compoundTag.c();
                for (String key : tagKeys)
                {
                    addTag(newSection, key, compoundTag.get(key));
                }
        }
    }
}
