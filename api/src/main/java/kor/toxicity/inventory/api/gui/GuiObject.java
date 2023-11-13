package kor.toxicity.inventory.api.gui;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface GuiObject {
    @NotNull GuiObjectGenerator getGenerator();
    @NotNull Component asComponent();
    @NotNull GuiObject append(@NotNull GuiObject object);
}
