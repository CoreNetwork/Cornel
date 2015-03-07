package us.core_network.cornel.java;

import org.bukkit.Material;

public class EnumUtil
{
    /**
     * Find enum entry with specified name. Method automatically handles spaces (converts them to underscores) and case sensitivity.
     * @param enumList List of enum objects. Usually you can just use output of the <code>.values()</code> of the specific enum.
     * @param name Name of the enum entry you want to find.
     * @return Enum entry with specified name or <code>null</code> if no such entry exists.
     */
    public static Enum findEnum(Enum[] enumList, String name)
    {
        for (Enum enumEntry : enumList)
        {
            String entryName = enumEntry.name();
            entryName = entryName.replace("_", " ");
            if (entryName.equalsIgnoreCase(name))
                return enumEntry;
        }

        return null;
    }

    /**
     * Retuns preetier name of the enum entry.
     * @param enumEntry Enum to get name from.
     * @return Pretty name.
     */
    public static String getPrettyEnumName(Enum enumEntry) {
        String name = enumEntry.name();
        name = name.replaceAll("_", " "); //Remove spaces
        name = name.toLowerCase();

        return name;
    }

}
