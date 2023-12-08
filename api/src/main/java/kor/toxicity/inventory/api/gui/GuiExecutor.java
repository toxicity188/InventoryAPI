package kor.toxicity.inventory.api.gui;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface GuiExecutor {
    void onInitialize(@NotNull GuiHolder holder);
    boolean onClick(@NotNull GuiHolder holder, boolean isPlayerInventory, int clickedSlot, @NotNull ItemStack clickedItem, @NotNull ItemStack cursorItem, @NotNull MouseButton button);
    void onEnd(@NotNull GuiHolder holder);
}
