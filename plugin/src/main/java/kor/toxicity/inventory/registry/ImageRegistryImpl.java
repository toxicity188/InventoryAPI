package kor.toxicity.inventory.registry;

import kor.toxicity.inventory.api.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ImageRegistryImpl implements Registry<BufferedImage> {
    private final Map<String, BufferedImage> fontMap = new HashMap<>();
    @Override
    public @Nullable BufferedImage getByName(@NotNull String name) {
        return fontMap.get(name);
    }

    @Override
    public @NotNull Collection<@NotNull BufferedImage> getAllValues() {
        return fontMap.values();
    }

    @Override
    public @NotNull Set<@NotNull String> getAllKeys() {
        return fontMap.keySet();
    }

    @Override
    public void register(@NotNull String name, @NotNull BufferedImage bufferedImage) {
        fontMap.put(name, bufferedImage);
    }

    public void clear() {
        fontMap.clear();
    }
}
