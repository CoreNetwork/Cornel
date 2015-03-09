package us.core_network.cornel.player;

import org.bukkit.command.CommandSender;

public class PlayerUtil
{
    /**
     * Check if specific {@link org.bukkit.command.CommandSender} has permission. This method automatically checks for .* nodes which would otherwise have to be added manually.
     * @param player {@link org.bukkit.command.CommandSender} that needs checking.
     * @param permission Permission to check.
     * @return <code>true</code> if specified {@link org.bukkit.command.CommandSender} has specified permission.
     */
    public static boolean hasPermission(CommandSender player, String permission)
    {
        while (true)
        {
            if (player.hasPermission(permission))
                return true;

            if (permission.length() < 2)
                return false;

            if (permission.endsWith("*"))
                permission = permission.substring(0, permission.length() - 2);

            int lastIndex = permission.lastIndexOf(".");
            if (lastIndex < 0)
                return false;

            permission = permission.substring(0, lastIndex).concat(".*");
        }
    }
}
