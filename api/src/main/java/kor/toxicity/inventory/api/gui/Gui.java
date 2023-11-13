package kor.toxicity.inventory.api.gui;

import kor.toxicity.inventory.api.InventoryAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public record Gui(@NotNull Component name, int size, @NotNull Map<Integer, ItemStack> contents, @NotNull Function<@NotNull Player, @NotNull GuiObject> object) {
    public void openGui(@NotNull Player player, @NotNull GuiType type, @NotNull GuiExecutor executor) {
        InventoryAPI.getInstance().openGui(player, this, type, executor);
    }
}
