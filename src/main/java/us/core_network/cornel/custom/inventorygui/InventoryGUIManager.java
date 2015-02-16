package us.core_network.cornel.custom.inventorygui;

import net.minecraft.server.v1_8_R1.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listener class that triggers all InventoryGUI events
 */
public class InventoryGUIManager implements Listener
{
    private static boolean registered = false;
    public static void boostrap(Plugin plugin)
    {
        if (registered)
            return;

        plugin.getServer().getPluginManager().registerEvents(new InventoryGUIManager(), plugin);
        registered = true;
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event)
    {
        CraftInventory inventory = (CraftInventory) event.getInventory();
        IInventory nmsInventory = inventory.getInventory();
        if (nmsInventory instanceof InventoryGUI.GUIVanillaInventory)
        {
            try
            {
                ((InventoryGUI.GUIVanillaInventory) nmsInventory).click(event);
            }
            finally
            {
                event.setCancelled(true);
            }
        }
    }

}
