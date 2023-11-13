package kor.toxicity.inventory.registry;

import kor.toxicity.inventory.api.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FontRegistryImpl implements Registry<Font> {
    private final Map<String, Font> fontMap = new HashMap<>();
    @Override
    public @Nullable Font getByName(@NotNull String name) {
        return fontMap.get(name);
    }

    @Override
    public @NotNull Collection<@NotNull Font> getAllValues() {
        return fontMap.values();
    }

    @Override
    public @NotNull Set<@NotNull String> getAllKeys() {
        return fontMap.keySet();
    }

    @Override
    public void register(@NotNull String name, @NotNull Font font) {
        fontMap.put(name, font);
    }

    public void clear() {
        fontMap.clear();
    }
}
