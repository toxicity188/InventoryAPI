package kor.toxicity.inventory.api.gui;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class GuiHolder implements InventoryHolder {
    private final @NotNull Inventory inventory;
    @Getter
    private final @NotNull GuiType type;
    @Getter
    private long delay = 4;

    @Getter
    private GuiExecutor executor;

    @Getter
    @Setter
    private GuiHolder parent;

    @Getter
    @Setter
    private boolean locked;

    public GuiHolder(@NotNull Component component, int size, @NotNull GuiType type) {
        inventory = Bukkit.createInventory(this, size, component);
        this.type = type;
    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open(@NotNull Player player) {
        executor.onInitialize(this);
        player.openInventory(inventory);
    }

    public void setExecutor(@NotNull GuiExecutor executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    public void setDelay(long delay) {
        this.delay = Math.max(delay, 1);
    }
}
