package us.core_network.cornel.custom.inventorygui;

import java.util.List;
import net.minecraft.server.v1_8_R2.ChatComponentText;
import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IInventory;
import net.minecraft.server.v1_8_R2.ItemStack;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Class that allows easy creation of chest-based GUI in Minecraft.
 * <p>Before using it, you have to init {@link us.core_network.cornel.custom.inventorygui.InventoryGUIManager} by calling {@link us.core_network.cornel.custom.inventorygui.InventoryGUIManager#boostrap(org.bukkit.plugin.Plugin)}.</p>
 * <p>After that you can simply create instance of this inventory and display it to player using {@link org.bukkit.entity.HumanEntity#openInventory(org.bukkit.inventory.Inventory)}.</p>
 * Generally, every player is supposed to have its own instance of InventoryGUI which allows greater control over what individual player sees.
 */
public abstract class InventoryGUI extends CraftInventory
{
    public InventoryGUI()
    {
        super(new GUIVanillaInventory());
        ((GUIVanillaInventory) getInventory()).setParent(this);
    }

    /**
     * Method is run when player clicks item in the inventory.
     * @param player Player that clicked the item.
     * @param clickType Type of the click that player performed.
     * @param slot Slot that player clicked (0 is top left slot, 1 is slot next to it, 9 is slot right below it etc.).
     */
    public abstract void onClick(HumanEntity player, ClickType clickType, int slot);

    /**
     * Method is run when player opens inventory.
     * @param player Player that opened inventory.
     */
    public void onOpen(CraftHumanEntity player)
    {
    }

    /**
     * Method is run when player closes inventory.
     * @param player Player that closed inventory.
     */
    public void onClose(CraftHumanEntity player)
    {
    }

    /**
     * @return Title of the inventory window.
     */
    public abstract String getTitle();

    /**
     * @return Height of the inventory window (3 means regular single chest window, 6 is double chest etc.).
     */
    public int getHeight()
    {
        return 3;
    }

    private void clickEvent(InventoryClickEvent event)
    {
        int slot = event.getRawSlot();
        if (slot > 26)
            return;

        onClick(event.getWhoClicked(), event.getClick(), slot);
    }

    protected static class GUIVanillaInventory implements IInventory
    {
        private InventoryGUI parent;
        private ItemStack[] items;

        protected GUIVanillaInventory()
        {

        }

        protected void setParent(InventoryGUI parent)
        {
            this.parent = parent;
            this.items = new ItemStack[9 * parent.getHeight()];
        }

        @Override
        public int getSize()
        {
            return items.length;
        }

        @Override
        public ItemStack getItem(int i)
        {
            return items[i];
        }

        @Override
        public ItemStack splitStack(int i, int i2)
        {
            return null;
        }

        @Override
        public ItemStack splitWithoutUpdate(int i)
        {
            return null;
        }

        @Override
        public void setItem(int i, ItemStack itemStack)
        {
            items[i] = itemStack;
        }


        @Override
        public int getMaxStackSize()
        {
            return 64;
        }

        @Override
        public void update()
        {

        }

        @Override
        public boolean a(EntityHuman entityHuman)
        {
            return true;
        }

        @Override
        public void startOpen(EntityHuman entityHuman)
        {

        }

        @Override
        public void closeContainer(EntityHuman entityHuman)
        {

        }

        @Override
        public boolean b(int i, ItemStack itemStack)
        {
            return true;
        }

        @Override
        public int getProperty(int i)
        {
            return 0;
        }

        @Override
        public void b(int i, int i1)
        {

        }

        @Override
        public int g()
        {
            return 0;
        }

        @Override
        public void l()
        {

        }

        @Override
        public ItemStack[] getContents()
        {
            return new ItemStack[0];
        }

        @Override
        public void onOpen(CraftHumanEntity craftHumanEntity)
        {
            parent.onOpen(craftHumanEntity);
        }

        @Override
        public void onClose(CraftHumanEntity craftHumanEntity)
        {
            parent.onClose(craftHumanEntity);
        }

        public void click(InventoryClickEvent event)
        {
            parent.clickEvent(event);
        }

        @Override
        public List<HumanEntity> getViewers()
        {
            return null;
        }

        @Override
        public InventoryHolder getOwner()
        {
            return null;
        }

        @Override
        public void setMaxStackSize(int i)
        {
        }

        @Override
        public String getName()
        {
            return parent.getTitle();
        }

        @Override
        public boolean hasCustomName()
        {
            return true;
        }

        @Override
        public IChatBaseComponent getScoreboardDisplayName()
        {
            return new ChatComponentText(getName());
        }
    }
}
