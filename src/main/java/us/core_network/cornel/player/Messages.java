package us.core_network.cornel.player;

import org.bukkit.ChatColor;

public class Messages
{
    /**
     * Translate formatting codes in string
     * @param message Message with gormating codes in &amp;&lt;CODE&gt; format.
     * @return Message with translated formatted codes that can be displayed on client.
     *
     */
    public static String applyFormattingCodes(String message)
    {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
