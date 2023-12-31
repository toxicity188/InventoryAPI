package kor.toxicity.inventory.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PluginReloadEndEvent extends Event implements InventoryAPIEvent {
    public PluginReloadEndEvent() {
        super(true);
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
