package kor.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public record GuiImage(@NotNull BufferedImage image, @NotNull String name) implements GuiResource {
}