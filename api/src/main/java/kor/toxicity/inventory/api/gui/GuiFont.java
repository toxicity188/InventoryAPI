package kor.toxicity.inventory.api.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;

@Getter
public final class GuiFont implements GuiResource {

    public static final FontRenderContext CONTEXT = new FontRenderContext(null, true, true);
    public static final double VERTICAL_SIZE = 1.4;

    private final @NotNull String name;
    private final @NotNull Font font;
    private final int size;
    private final @NotNull Map<Character, Function<Integer, Integer>> fontWidth;
    private final @NotNull List<Character> availableChar;
    private final @NotNull FontMetrics metrics;

    public GuiFont(@NotNull Font font, @NotNull String name) {
        this(font, name, 16);
    }
    public GuiFont(@NotNull Font font, @NotNull String name, int size) {
        this(font, name, size, 0);
    }
    public GuiFont(@NotNull Font font, @NotNull String name, int size, double width) {
        this(font, name, size, width, 1);
    }
    public GuiFont(@NotNull Font font, @NotNull String name, int size, double width, double widthMultiplier) {
        this.font = font.deriveFont((float) size);
        this.name = name;
        this.size = size;
        var widthMap = new TreeMap<@NotNull Character, @NotNull Function<Integer, Integer>>();
        IntStream.rangeClosed(Character.MIN_VALUE, Character.MAX_VALUE).mapToObj(i -> (char) i).filter(font::canDisplay).forEach(c -> {
            var str = this.font.getStringBounds(Character.toString(c), CONTEXT);
            widthMap.put(c, i -> (int) Math.round((str.getWidth() * i  / VERTICAL_SIZE / str.getHeight() + width) * widthMultiplier));
        });
        fontWidth = ImmutableMap.copyOf(widthMap);
        var graphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        metrics = graphics.getFontMetrics(this.font);
        graphics.dispose();
        var c = new ArrayList<>(widthMap.keySet());
        availableChar = ImmutableList.copyOf(c.subList(0, c.size() - c.size() % 16));
    }
}
