package kor.toxicity.inventory.api.gui;

import kor.toxicity.inventory.api.InventoryAPI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public record Gui(@NotNull Component name, int size, int space, @NotNull Map<Integer, ItemStack> contents, @NotNull Function<Player, GuiObject> object) {
    public static @NotNull Gui of(@NotNull Function<Player, GuiObject> object) {
        return new Gui(Component.empty(), 54, -8, Collections.emptyMap(), object);
    }
    public static @NotNull Builder builder(@NotNull Function<Player, GuiObject> object) {
        return new Builder(object);
    }
    public void openGui(@NotNull Player player, @NotNull GuiType type, long delay, @NotNull GuiExecutor executor) {
        InventoryAPI.getInstance().openGui(player, this, type, delay, executor);
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Builder {
        private @NotNull Component component = Component.empty();
        private int size = 54;
        @Setter
        private int space = -8;
        private @NotNull Map<Integer, ItemStack> contents = Collections.emptyMap();
        private final Function<Player, GuiObject> object;

        public void setSize(int size) {
            if (size % 9 != 0) throw new RuntimeException();
            this.size = size;
        }
        public void setComponent(@NotNull Component component) {
            this.component = Objects.requireNonNull(component);
        }
        public void setContents(@NotNull Map<Integer, ItemStack> contents) {
            this.contents = Objects.requireNonNull(contents);
        }
        public @NotNull Gui build() {
            return new Gui(component, size, space, contents, object);
        }
    }
}
