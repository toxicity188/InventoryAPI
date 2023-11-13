package kor.toxicity.inventory.api.gui;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class GuiHolder implements InventoryHolder {
    private final @NotNull Inventory inventory;
    @Getter
    private final @NotNull GuiType type;

    @Getter
    @Setter
    private GuiExecutor executor;

    @Getter
    @Setter
    private GuiHolder parent;

    public GuiHolder(@NotNull Component component, int size, @NotNull GuiType type) {
        inventory = Bukkit.createInventory(this, size, component);
        this.type = type;
    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
