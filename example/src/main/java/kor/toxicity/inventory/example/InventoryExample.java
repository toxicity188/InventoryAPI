package kor.toxicity.inventory.example;

import kor.toxicity.inventory.api.InventoryAPI;
import kor.toxicity.inventory.api.gui.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.text.DecimalFormat;
import java.util.Collections;

@SuppressWarnings("unused")
public class InventoryExample extends JavaPlugin {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,###");
    @Override
    public void onEnable() {
        try (var imageStream = getResource("layout.png")) {
            assert imageStream != null;

            var guiImage = new GuiImage(ImageIO.read(imageStream), "default_layout").generator(139, 13);
            var playerNameGenerator = InventoryAPI.getInstance().defaultFont().generator(18, -5);

            var firstLine = InventoryAPI.getInstance().defaultFont().generator(12, -27);
            var secondLine = InventoryAPI.getInstance().defaultFont().generator(12, -42);


            var gui = new Gui(Component.empty(), 54, -8, Collections.emptyMap(), player -> guiImage.builder().build()
                    .append(playerNameGenerator.builder()
                            .asFont()
                            .setSpace(8)
                            .setText("Name: " + player.getName())
                            .setStyle(Style.style(NamedTextColor.WHITE))
                            .setXOffset(10)
                            .build()
                    )
                    .append(firstLine.builder()
                            .asFont()
                            .setSpace(6)
                            .setText("HP: " + FORMAT.format(player.getHealth()))
                            .setStyle(Style.style(NamedTextColor.WHITE))
                            .setXOffset(15)
                            .build()
                    )
                    .append(firstLine.builder()
                            .asFont()
                            .setSpace(6)
                            .setText("FOOD: " + FORMAT.format(player.getFoodLevel()))
                            .setStyle(Style.style(NamedTextColor.WHITE))
                            .setXOffset(80)
                            .build()
                    )
                    .append(secondLine.builder()
                            .asFont()
                            .setSpace(6)
                            .setText("LV: " + FORMAT.format(player.getLevel()))
                            .setStyle(Style.style(NamedTextColor.WHITE))
                            .setXOffset(-80)
                            .build()
                    )
                    .append(secondLine.builder()
                            .asFont()
                            .setSpace(6)
                            .setText("EXP: " + FORMAT.format(player.getExp()))
                            .setStyle(Style.style(NamedTextColor.WHITE))
                            .setXOffset(80)
                            .build()
                    )
            );

            var command = getCommand("testmenu");
            if (command != null) command.setExecutor((sender, command1, label, args) -> {
                if (sender instanceof Player player) {
                    gui.openGui(player, GuiType.DEFAULT, 4, new GuiExecutor() {
                        @Override
                        public void onInitialize(@NotNull GuiHolder holder) {
                            player.sendMessage(Component.text("Hello world!"));
                        }

                        @Override
                        public boolean onClick(@NotNull GuiHolder holder, boolean isPlayerInventory, int clickedSlot, @NotNull ItemStack clickedItem, @NotNull MouseButton button) {
                            player.sendMessage(Component.text("The clicked slot is " + clickedSlot + "!"));
                            return true;
                        }

                        @Override
                        public void onEnd(@NotNull GuiHolder holder) {
                            player.sendMessage(Component.text("Gui ended!"));
                        }
                    });
                }
                return true;
            });
        } catch (Exception e) {
            getLogger().warning("Unable to find layout.png");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
