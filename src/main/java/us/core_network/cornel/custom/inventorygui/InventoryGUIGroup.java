package us.core_network.cornel.custom.inventorygui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftHumanEntity;
import us.core_network.cornel.custom.inventorygui.InventoryGUI;

/**
 * This class is useful when you have one thing that can be controlled using multiple players (for example one block that multiple players can right click).
 * It allows synchronizing between multiple open inventory GUIs and ensuring action one player performs is reflected on others.
 *
 * <p>All your GUIs must extend {@link us.core_network.cornel.custom.inventorygui.InventoryGUIGroup.InventoryGUIGroupWindow}.</p>
 *
 * <p>Synchronization is achieved by registering all actions as methods of this class and then iterating through {@link us.core_network.cornel.custom.inventorygui.InventoryGUIGroup#openedWindows} for each action.</p>
 *
 * @see us.core_network.cornel.custom.inventorygui.InventoryGUIGroup.InventoryGUIGroupWindow
 * @see us.core_network.cornel.custom.inventorygui.InventoryGUI
 */
public abstract class InventoryGUIGroup<T extends InventoryGUIGroup.InventoryGUIGroupWindow>
{
    protected List<T> openedWindows;

    public InventoryGUIGroup()
    {
        openedWindows = new ArrayList<T>();
    }

    public boolean isAnyWindowOpened()
    {
        return !openedWindows.isEmpty();
    }

    /**
     * Class that represents single inventory window opened by one player.
     *
     * @see us.core_network.cornel.custom.inventorygui.InventoryGUI
     */
    public static abstract class InventoryGUIGroupWindow extends InventoryGUI
    {
        private InventoryGUIGroup parent;

        /**
         * Player associated with the inventory (can be <code>null</code> if inventory was not yet open).
         */
        protected CraftHumanEntity player;

        /**
         * @param parent {@link us.core_network.cornel.custom.inventorygui.InventoryGUIGroup} that this window belongs to. After calling this constructor, this window will be automatically linked with the group.
         */
        public InventoryGUIGroupWindow(InventoryGUIGroup parent)
        {
            this.parent = parent;
        }

        @Override
        public void onClose(CraftHumanEntity player)
        {
            parent.openedWindows.remove(this);
        }

        @Override
        public void onOpen(CraftHumanEntity player)
        {
            this.player = player;
            parent.openedWindows.add(this);
        }
    }
}
