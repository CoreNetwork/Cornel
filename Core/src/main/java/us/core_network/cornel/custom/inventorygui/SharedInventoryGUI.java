package us.core_network.cornel.custom.inventorygui;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

/**
 * <p>Class that is used when there is only one common GUI (for example GUI that controls specific block that multiple players can right click).
 * It automatically keeps track of all players currently viewing the GUI, allowing you to easily perform stuff on all players
 * (for example switching all viewers to different GUI screen).</p>
 *
 * <p>This class is supposed to only have one common instance instead of creating new one for every player.</p>
 *
 * <p>Every player sees exactly the same GUI with this class. If you want different per-player GUIs under same control group use {@link us.core_network.cornel.custom.inventorygui.InventoryGUIGroup}.</p>
 * @see us.core_network.cornel.custom.inventorygui.InventoryGUI
 */
public abstract class SharedInventoryGUI extends InventoryGUI
{
    private List<HumanEntity> viewers;

    public SharedInventoryGUI()
    {
        viewers = new LinkedList<>();
    }

    /**
     * @return List of players that are currently viewing this GUI (have this GUI open).
     */
    @Override
    public List<HumanEntity> getViewers()
    {
        return viewers;
    }

    /**
     * @return <code>true</code> if any player is currently viewing this GUI (have this GUI open).
     */
    public boolean isAnyPlayerViewing()
    {
        return !viewers.isEmpty();
    }

    @Override
    public void onClose(CraftHumanEntity player)
    {
        viewers.remove(player);
    }

    @Override
    public void onOpen(CraftHumanEntity player)
    {
        viewers.add(player);
    }

}
