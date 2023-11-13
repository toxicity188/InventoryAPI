package kor.toxicity.inventory;

import kor.toxicity.inventory.api.InventoryAPI;
import kor.toxicity.inventory.api.event.PluginReloadEndEvent;
import kor.toxicity.inventory.api.event.PluginReloadStartEvent;
import kor.toxicity.inventory.api.gui.*;
import kor.toxicity.inventory.api.manager.InventoryManager;
import kor.toxicity.inventory.manager.FontManagerImpl;
import kor.toxicity.inventory.manager.ImageManagerImpl;
import kor.toxicity.inventory.manager.ResourcePackManagerImpl;
import kor.toxicity.inventory.util.AdventureUtil;
import kor.toxicity.inventory.util.PluginUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public final class InventoryAPIImpl extends InventoryAPI {

    private static final Tag NONE_TAG = Tag.selfClosingInserting(Component.text(""));
    private static final Tag ERROR_TAG = Tag.selfClosingInserting(Component.text("error!"));

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolvers(
                            TagResolver.standard(),
                            TagResolver.resolver("space", (ArgumentQueue argumentQueue, Context context) -> {
                                if (argumentQueue.hasNext()) {
                                    var next = argumentQueue.pop().value();
                                    try {
                                        return Tag.selfClosingInserting(AdventureUtil.getSpaceFont(Integer.parseInt(next)));
                                    } catch (Exception e) {
                                        return ERROR_TAG;
                                    }
                                } else return NONE_TAG;
                            })
                    )
                    .build()
            )
        .build();

    private GuiFont font;
    private final List<InventoryManager> managers = new ArrayList<>();

    @Override
    public void onEnable() {
        var pluginManager = Bukkit.getPluginManager();

        var fontManager = new FontManagerImpl();
        setFontManager(fontManager);
        var imageManager = new ImageManagerImpl();
        setImageManager(imageManager);
        var resourcePackManager = new ResourcePackManagerImpl();
        setResourcePackManager(resourcePackManager);

        managers.addAll(List.of(
                fontManager,
                imageManager,
                resourcePackManager
        ));

        var command = getCommand("inventoryapi");
        if (command != null) command.setExecutor((sender, command1, label, args) -> {
            if (sender.isOp()) {
                reload(l -> sender.sendMessage("Reload completed. (" + l + " ms)"));
            } else {
                sender.sendMessage("You are not OP!");
            }
            return true;
        });
        pluginManager.registerEvents(new Listener() {
            @EventHandler
            public void click(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player player) {

                }
            }
        }, this);

        try (var stream = getResource("unifont.ttf")) {
            font = new GuiFont(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(stream)), "unifont");
        } catch (Exception e) {
            PluginUtil.warn("Unable to find unifont.ttf.");
            pluginManager.disablePlugin(this);
        }
        Bukkit.getScheduler().runTask(this, () -> {
            pluginManager.callEvent(new PluginReloadStartEvent());
            reload();
            pluginManager.callEvent(new PluginReloadEndEvent());
            getLogger().info("Plugin enabled.");
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    @Override
    public void reload() {
        delete(new File(getDataFolder(), ".generated"));
        unzipAssets(".generated", "assets");
        managers.forEach(InventoryManager::reload);
    }

    @Override
    public @NotNull MiniMessage miniMessage() {
        return MINI_MESSAGE;
    }

    @Override
    public @NotNull GuiFont defaultFont() {
        return Objects.requireNonNull(font);
    }


    @Override
    public void openGui(@NotNull Player player, @NotNull Gui gui, @NotNull GuiType type, @NotNull GuiExecutor executor) {
        var holder = new GuiHolder(Component.empty().append(AdventureUtil.getSpaceFont(-8)).append(gui.object().apply(player).asComponent()).append(gui.name()), gui.size(), type);
        var inventory = holder.getInventory();
        gui.contents().forEach(inventory::setItem);

        var oldInventory = player.getOpenInventory().getTopInventory().getHolder();
        if (oldInventory instanceof GuiHolder oldHolder) holder.setParent(oldHolder);
        player.openInventory(inventory);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void unzipAssets(String dir, String target) {
        try(var jar = new JarFile(getFile())) {

            var dataFolder = getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdir();
            var targetFolder = new File(dataFolder, dir);
            if (!targetFolder.exists()) targetFolder.mkdir();

            var iterator = jar.entries().asIterator();
            while (iterator.hasNext()) {
                var next = iterator.next();
                var name = next.getName();
                if (!name.isEmpty() && name.startsWith(target)) {
                    var mkdir = new File(targetFolder, name);
                    if (next.isDirectory()) {
                        if (!mkdir.exists()) mkdir.mkdir();
                    } else {
                        try (var resource = getResource(name)) {
                            if (resource != null) try (var output = new BufferedOutputStream(new FileOutputStream(mkdir))) {
                                output.write(resource.readAllBytes());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            getLogger().warning("Unable to unzip assets.");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delete(File file) {
        if (file.isDirectory()) {
            var listFile = file.listFiles();
            if (listFile != null) for (File file1 : listFile) {
                delete(file1);
            }
        }
        file.delete();
    }
}
