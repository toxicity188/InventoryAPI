package kor.toxicity.inventory.manager;

import kor.toxicity.inventory.api.manager.FontManager;
import kor.toxicity.inventory.api.registry.Registry;
import kor.toxicity.inventory.registry.FontRegistryImpl;
import kor.toxicity.inventory.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class FontManagerImpl implements FontManager {
    private final FontRegistryImpl fontRegistry = new FontRegistryImpl();
    @Override
    public void reload() {
        fontRegistry.clear();
        PluginUtil.loadFolder("fonts", f -> {
            var name = PluginUtil.getFileName(f);
            switch (name.extension().toLowerCase()) {
                case "ttf", "oft" -> {
                    try (var stream = new BufferedInputStream(new FileInputStream(f))) {
                        fontRegistry.register(name.name(), Font.createFont(Font.TRUETYPE_FONT, stream));
                    } catch (Exception e) {
                        PluginUtil.warn("Unable to read this font: " + f.getName());
                        PluginUtil.warn("Reason: " + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public @NotNull Registry<Font> getRegistry() {
        return fontRegistry;
    }
}
