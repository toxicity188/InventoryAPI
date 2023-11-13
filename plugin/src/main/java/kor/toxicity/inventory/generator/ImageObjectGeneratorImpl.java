package kor.toxicity.inventory.generator;

import kor.toxicity.inventory.api.gui.*;
import kor.toxicity.inventory.util.AdventureUtil;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class ImageObjectGeneratorImpl implements ImageObjectGenerator, Comparable<ImageObjectGeneratorImpl> {

    private static final Key GUI_KEY = Key.key("inventory:gui");
    private static int serialNumber = 0xD0000;

    public static void initialize() {
        serialNumber = 0xD0000;
    }

    private final GuiImage image;
    private final int height;
    private final int ascent;
    @Getter
    private final String serialChar;
    private final int compareNumber;

    public ImageObjectGeneratorImpl(GuiImage image, int height, int ascent) {
        this.image = image;
        this.height = height;
        this.ascent = ascent;
        compareNumber = ++serialNumber;
        serialChar = AdventureUtil.parseChar(compareNumber);
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
        return new ImageObjectBuilder() {
            private int xOffset;
            @Override
            public @NotNull GuiObjectBuilder setXOffset(int xOffset) {
                this.xOffset = xOffset;
                return this;
            }

            @Override
            public @NotNull GuiObject build() {
                return new ImageObjectImpl(ImageObjectGeneratorImpl.this, xOffset);
            }
        };
    }

    @Override
    public @NotNull Key getFontKey() {
        return GUI_KEY;
    }

    @Override
    public @NotNull GuiImage getImage() {
        return image;
    }

    @Override
    public int compareTo(@NotNull ImageObjectGeneratorImpl o) {
        return Integer.compare(compareNumber, o.compareNumber);
    }
}
