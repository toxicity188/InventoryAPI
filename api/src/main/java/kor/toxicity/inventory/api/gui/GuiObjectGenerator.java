package kor.toxicity.inventory.api.gui;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface GuiObjectGenerator {
    int getAscent();
    int getHeight();
    @NotNull GuiObjectBuilder builder();
    @NotNull Key getFontKey();
}
