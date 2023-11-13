package kor.toxicity.inventory.manager;

import kor.toxicity.inventory.api.manager.ImageManager;
import kor.toxicity.inventory.api.registry.Registry;
import kor.toxicity.inventory.registry.ImageRegistryImpl;
import kor.toxicity.inventory.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageManagerImpl implements ImageManager {
    private final ImageRegistryImpl imageRegistry = new ImageRegistryImpl();
    @Override
    public void reload() {
        imageRegistry.clear();
        PluginUtil.loadFolder("images", f -> {
            var name = PluginUtil.getFileName(f);
            if (name.extension().equalsIgnoreCase("png")) {
                try {
                    imageRegistry.register(name.name(), ImageIO.read(f));
                } catch (Exception e) {
                    PluginUtil.warn("Unable to read this image: " + f.getName());
                    PluginUtil.warn("Reason: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public @NotNull Registry<BufferedImage> getRegistry() {
        return imageRegistry;
    }
}
