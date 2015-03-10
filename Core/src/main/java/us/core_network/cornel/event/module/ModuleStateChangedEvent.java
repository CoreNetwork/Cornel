package us.core_network.cornel.event.module;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.core_network.cornel.module.Module;

public class ModuleStateChangedEvent extends Event implements Cancellable {
    private static HandlerList HANDLER_LIST = new HandlerList();
    private Module module;
    private Module.State oldState, newState;
    private boolean cancelled;

    public ModuleStateChangedEvent(Module module, Module.State oldState, Module.State newState) {
        this.module = module;
        this.oldState = oldState;
        this.newState = newState;
    }

    public Module getModule() {
        return module;
    }

    public Module.State getOldState() {
        return oldState;
    }

    public Module.State getNewState() {
        return newState;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
