package kor.toxicity.inventory.api;

import kor.toxicity.inventory.api.event.PluginReloadEndEvent;
import kor.toxicity.inventory.api.event.PluginReloadStartEvent;
import kor.toxicity.inventory.api.gui.*;
import kor.toxicity.inventory.api.manager.FontManager;
import kor.toxicity.inventory.api.manager.ImageManager;
import kor.toxicity.inventory.api.manager.ResourcePackManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class InventoryAPI extends JavaPlugin {
    private static InventoryAPI api;

    private ImageManager imageManager;
    private FontManager fontManager;
    private ResourcePackManager resourcePackManager;

    @Override
    public final void onLoad() {
        if (api != null) throw new SecurityException();
        api = this;
    }

    public static @NotNull InventoryAPI getInstance() {
        return Objects.requireNonNull(api);
    }

    public final void setFontManager(@NotNull FontManager fontManager) {
        this.fontManager = Objects.requireNonNull(fontManager);
    }

    public final @NotNull FontManager getFontManager() {
        return Objects.requireNonNull(fontManager);
    }

    public final @NotNull ImageManager getImageManager() {
        return Objects.requireNonNull(imageManager);
    }

    public final void setImageManager(@NotNull ImageManager imageManager) {
        this.imageManager = Objects.requireNonNull(imageManager);
    }

    public final void setResourcePackManager(@NotNull ResourcePackManager resourcePackManager) {
        this.resourcePackManager = Objects.requireNonNull(resourcePackManager);
    }

    public @NotNull ResourcePackManager getResourcePackManager() {
        return Objects.requireNonNull(resourcePackManager);
    }

    public abstract void reload();
    public void reload(@NotNull Consumer<@NotNull Long> longConsumer) {
        var pluginManager = Bukkit.getPluginManager();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            pluginManager.callEvent(new PluginReloadStartEvent());
            var time = System.currentTimeMillis();
            reload();
            pluginManager.callEvent(new PluginReloadEndEvent());
            longConsumer.accept(System.currentTimeMillis() - time);
        });
    }

    public abstract @NotNull MiniMessage miniMessage();
    public abstract @NotNull GuiFont defaultFont();
    public abstract void openGui(@NotNull Player player, @NotNull Gui gui, @NotNull GuiType type, @NotNull GuiExecutor executor);
}
