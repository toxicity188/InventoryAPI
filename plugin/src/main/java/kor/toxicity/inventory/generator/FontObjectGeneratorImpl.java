package kor.toxicity.inventory.generator;

import kor.toxicity.inventory.api.gui.*;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FontObjectGeneratorImpl implements FontObjectGenerator {

    private final GuiFont font;
    private final int height;
    private final int ascent;
    @Getter
    private final String fontTitle;
    private final Key key;

    public FontObjectGeneratorImpl(GuiFont font, int height, int ascent) {
        this.font = font;
        this.height = height;
        this.ascent = ascent;
        fontTitle = font.getName() + "_" + height + "_" + ascent;
        key = Key.key("inventory:font/" + fontTitle);
    }

    @Override
    public @NotNull GuiFont getFont() {
        return font;
    }

    @Override
    public int getAscent() {
        return ascent;
    }

    @Override
    public int getHeight() {
        return height;
    }


    @Override
    public @NotNull GuiObjectBuilder builder() {
        return new FontObjectBuilder() {
            private int space = 6;
            private String text = "none";
            private int xOffset = 0;
            private Style style = Style.empty();

            @Override
            public @NotNull FontObjectBuilder setSpace(int space) {
                this.space = space;
                return this;
            }

            @Override
            public @NotNull FontObjectBuilder setText(@NotNull String text) {
                this.text = Objects.requireNonNull(text);
                return this;
            }

            @Override
            public @NotNull GuiObjectBuilder setXOffset(int xOffset) {
                this.xOffset = xOffset;
                return this;
            }

            @Override
            public @NotNull FontObjectBuilder setStyle(@NotNull Style style) {
                this.style = Objects.requireNonNull(style);
                return this;
            }

            @Override
            public @NotNull GuiObject build() {
                return new FontObjectImpl(FontObjectGeneratorImpl.this, text, style, space, xOffset);
            }
        };
    }

    @Override
    public @NotNull Key getFontKey() {
        return key;
    }
}
