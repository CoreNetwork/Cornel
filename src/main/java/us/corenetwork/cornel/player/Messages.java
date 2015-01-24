package us.corenetwork.cornel.player;

import org.bukkit.ChatColor;

public class Messages
{
    /**
     * Translate color codes in string
     * @param message Message with colorcodes in &&lt;CODE&gt; format.
     * @return Message with translated color codes that can be displayed on client.
     *
     */
    public static String applyColors(String message)
    {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
