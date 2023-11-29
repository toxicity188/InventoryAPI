package kor.toxicity.inventory.api.gui;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface GuiObject {
    GuiObject EMPTY = new GuiObject() {
        @Override
        public @NotNull GuiObjectGenerator getGenerator() {
            throw new UnsupportedOperationException("This is a empty object.");
        }

        @Override
        public @NotNull Component asComponent() {
            return Component.empty();
        }

        @Override
        public @NotNull GuiObject append(@NotNull GuiObject object) {
            return object;
        }
    };
    @NotNull GuiObjectGenerator getGenerator();
    @NotNull Component asComponent();
    @NotNull GuiObject append(@NotNull GuiObject object);
}
