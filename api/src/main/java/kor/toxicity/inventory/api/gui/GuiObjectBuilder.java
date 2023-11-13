package kor.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

public interface GuiObjectBuilder {
    @NotNull GuiObjectBuilder setXOffset(int xOffset);
    @NotNull GuiObject build();


    default FontObjectBuilder asFont() {
        return (FontObjectBuilder) this;
    }
    default ImageObjectGenerator asImage() {
        return (ImageObjectGenerator) this;
    }
}
