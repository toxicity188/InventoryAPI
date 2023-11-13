package kor.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

public interface ImageObjectGenerator extends GuiObjectGenerator {
    @NotNull GuiImage getImage();
}
