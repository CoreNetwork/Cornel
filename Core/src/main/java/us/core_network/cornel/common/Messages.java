package us.core_network.cornel.common;

import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.core_network.cornel.player.PlayerUtil;

public class Messages
{
    //TODO: Support for multi-line messages in List<String> form (after new config system)

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

    /**
     * Apply formatting codes and send message.
     * @param message Message to send.
     * @param recipient Recipient of the message.
     */
    public static void send(String message, CommandSender recipient)
    {
        message = applyFormattingCodes(message);

        final String newLine = "\\[NEWLINE\\]";
        String[] lines = message.split(newLine);

        for (int i = 0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();

            if (i == 0)
                continue;

            int lastColorChar = lines[i - 1].lastIndexOf(ChatColor.COLOR_CHAR);
            if (lastColorChar == -1 || lastColorChar >= lines[i - 1].length() - 1)
                continue;

            char lastColor = lines[i - 1].charAt(lastColorChar + 1);
            lines[i] = Character.toString(ChatColor.COLOR_CHAR).concat(Character.toString(lastColor)).concat(lines[i]);
        }

        for (int i = 0; i < lines.length; i++)
            recipient.sendMessage(lines[i]);
    }

    /**
     * Apply formatting codes and send message to all online players.
     * @param message Message to send.
     */
    public static void broadcast(String message)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            send(message, p);
        }

    }

    /**
     * Apply formatting codes and send message to all online players except one.
     * @param message Message to send.
     * @param exclusion Name of the player that won't receive the message.
     */
    public static void broadcastWithExclusion(String message, String exclusion)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (!p.getName().equals(exclusion))
                send(message, p);
        }

    }

    /**
     * Apply formatting codes and send message to all players on the list.
     * @param message Message to send.
     * @param players List of players that wil receive the message.
     */
    public static void multicast(String message, List<Player> players)
    {
        for (Player p : players)
        {
            send(message, p);
        }
    }

    /**
     * Apply formatting codes and send message to all players with specified permission.
     * @param message Message to send.
     * @param permission Permission that player needs to receive the message.
     */
    public static void sendWithPermissions(String message, String permission)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
           if (PlayerUtil.hasPermission(p, permission))
                send(message, p);
        }
    }


}
