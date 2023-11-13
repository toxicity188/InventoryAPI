package kor.toxicity.inventory.api.gui;

import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

public interface FontObjectBuilder extends GuiObjectBuilder {
    @NotNull FontObjectBuilder setSpace(int space);
    @NotNull FontObjectBuilder setText(@NotNull String text);
    @NotNull FontObjectBuilder setStyle(@NotNull Style style);
}
