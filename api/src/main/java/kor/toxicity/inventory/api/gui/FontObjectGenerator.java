package kor.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

public interface FontObjectGenerator extends GuiObjectGenerator {
    @NotNull GuiFont getFont();
}
