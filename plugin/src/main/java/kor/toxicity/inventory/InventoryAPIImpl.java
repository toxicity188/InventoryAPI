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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
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
            .postProcessor(c -> {
                var style = c.style();
                if (style.color() == null) style = style.color(NamedTextColor.WHITE);
                var deco = style.decorations();
                var newDeco = new EnumMap<TextDecoration, TextDecoration.State>(TextDecoration.class);
                for (TextDecoration value : TextDecoration.values()) {
                    var get = deco.get(value);
                    if (get == null || get == TextDecoration.State.NOT_SET) {
                        newDeco.put(value, TextDecoration.State.FALSE);
                    } else newDeco.put(value, get);
                }
                style = style.decorations(newDeco);
                return c.style(style);
            })
        .build();

    private GuiFont font;
    private final List<InventoryManager> managers = new ArrayList<>();
    private static final ItemStack AIR = new ItemStack(Material.AIR);

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
            private final Map<UUID, BukkitTask> delayMap = new HashMap<>();
            @EventHandler
            public void click(InventoryClickEvent event) {
                if (event.getView().getTopInventory().getHolder() instanceof GuiHolder holder) {
                    var player = event.getWhoClicked();
                    if (delayMap.containsKey(player.getUniqueId())) {
                        event.setCancelled(true);
                        return;
                    }
                    delayMap.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLaterAsynchronously(InventoryAPIImpl.this, () -> delayMap.remove(player.getUniqueId()), holder.getDelay()));
                    var executor = holder.getExecutor();
                    MouseButton button;
                    if (event.isLeftClick()) {
                        button = event.isShiftClick() ? MouseButton.SHIFT_LEFT : MouseButton.LEFT;
                    } else if (event.isRightClick()) {
                        button = event.isShiftClick() ? MouseButton.SHIFT_RIGHT : MouseButton.RIGHT;
                    } else {
                        button = MouseButton.OTHER;
                    }
                    event.setCancelled(executor.onClick(
                            holder,
                            Objects.equals(event.getClickedInventory(), player.getInventory()),
                            event.getSlot(),
                            event.getCurrentItem() != null ? event.getCurrentItem() : AIR,
                            button
                    ));
                }
            }
            @EventHandler
            public void end(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player player && event.getView().getTopInventory().getHolder() instanceof GuiHolder holder) {
                    if (holder.isLocked()) {
                        holder.setLocked(false);
                        return;
                    }
                    var executor = holder.getExecutor();
                    executor.onEnd(holder);
                    if (holder.getType() == GuiType.SUB) {
                        var parent = holder.getParent();
                        if (parent == null) return;
                        Bukkit.getScheduler().runTaskLater(InventoryAPIImpl.this, () -> parent.open(player), 1);
                    }
                }
            }
        }, this);

        try (var stream = getResource("unifont.ttf")) {
            font = new GuiFont(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(stream)), "unifont");
        } catch (Exception e) {
            PluginUtil.warn("Unable to find unifont.ttf.");
            pluginManager.disablePlugin(this);
            return;
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
    public void openGui(@NotNull Player player, @NotNull Gui gui, @NotNull GuiType type, long delay, @NotNull GuiExecutor executor) {
        var holder = new GuiHolder(Component.empty().append(AdventureUtil.getSpaceFont(-8)).append(gui.object().apply(player).asComponent()).append(gui.name()), gui.size(), type);
        holder.setDelay(delay);
        holder.setExecutor(executor);
        var inventory = holder.getInventory();
        gui.contents().forEach(inventory::setItem);

        var oldInventory = player.getOpenInventory().getTopInventory().getHolder();
        if (oldInventory instanceof GuiHolder oldHolder) {
            holder.setParent(oldHolder);
            oldHolder.setLocked(true);
        }
        holder.open(player);
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
