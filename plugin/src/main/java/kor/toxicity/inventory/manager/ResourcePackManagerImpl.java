package kor.toxicity.inventory.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonWriter;
import kor.toxicity.inventory.api.InventoryAPI;
import kor.toxicity.inventory.api.gui.GuiFont;
import kor.toxicity.inventory.api.gui.GuiImage;
import kor.toxicity.inventory.api.gui.GuiObjectGenerator;
import kor.toxicity.inventory.api.gui.GuiResource;
import kor.toxicity.inventory.api.manager.ResourcePackManager;
import kor.toxicity.inventory.builder.JsonArrayBuilder;
import kor.toxicity.inventory.builder.JsonObjectBuilder;
import kor.toxicity.inventory.generator.FontObjectGeneratorImpl;
import kor.toxicity.inventory.generator.ImageObjectGeneratorImpl;
import kor.toxicity.inventory.util.PluginUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ResourcePackManagerImpl implements ResourcePackManager {
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private final List<FontObjectGeneratorImpl> fonts = new ArrayList<>();
    private final List<ImageObjectGeneratorImpl> images = new ArrayList<>();
    @Getter
    private Material emptyMaterial = Material.DIAMOND_HORSE_ARMOR;
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void reload() {
        var topFolder = getFile(getFile(InventoryAPI.getInstance().getDataFolder(), ".generated"), "assets");
        var assets = getFile(topFolder, "inventory");
        var font = getFile(assets, "font");
        var fontFont = getFile(font, "font");
        var textures = getFile(assets, "textures");
        var texturesFont = getFile(textures, "font");
        var texturesFontGui = getFile(texturesFont, "gui");
        var texturesFontFont = getFile(texturesFont, "font");
        var models = getFile(getFile(getFile(topFolder, "minecraft"), "models"),"item");

        var config = getFile(InventoryAPI.getInstance().getDataFolder(), "config.yml");
        if (!config.exists()) InventoryAPI.getInstance().saveResource("config.yml", false);
        try {
            var yaml = YamlConfiguration.loadConfiguration(config).getString("default-empty-material");
            if (yaml != null) emptyMaterial = Material.valueOf(yaml.toUpperCase());
        } catch (Exception e) {
            PluginUtil.warn("Unable to load config.yml");
            PluginUtil.warn("Reason: " + e.getMessage());
        }
        var emptyMaterialLowerCase = emptyMaterial.name().toLowerCase();
        var modelsFile = new File(models, emptyMaterialLowerCase + ".json");
        var modelsJson = new JsonObjectBuilder()
                .add("parent", "minecraft:item/generated")
                .add("textures", new JsonObjectBuilder()
                        .add("0", "minecraft:item/" + emptyMaterialLowerCase)
                        .build()
                )
                .add("overrides", new JsonArrayBuilder()
                        .add(new JsonObjectBuilder()
                                .add("predicate", new JsonObjectBuilder()
                                        .add("custom_model_data", 1)
                                        .build())
                                .add("model", "inventory:item/empty")
                                .build()
                        )
                        .build()
                )
                .build();
        try (var writer = new JsonWriter(new BufferedWriter(new FileWriter(modelsFile)))) {
            gson.toJson(modelsJson, writer);
        } catch (Exception e) {
            PluginUtil.warn("Unable to make a empty material file.");
        }

        fonts.forEach(f -> {
            var targetFolder = new File(texturesFontFont, f.getFont().getName());
            var jsonFolder = new File(fontFont, f.getFontTitle() + ".json");
            var guiFont = f.getFont();
            var available = guiFont.getAvailableChar();
            if (!targetFolder.exists()) {
                targetFolder.mkdir();
                var i = 0;
                var n = 1;
                var yAxis = (float) GuiFont.VERTICAL_SIZE * guiFont.getMetrics().getHeight();
                while (i < available.size()) {
                    var image = new BufferedImage(16 * guiFont.getSize(), (int) (yAxis * 16), BufferedImage.TYPE_INT_ARGB);
                    var graphics = image.createGraphics();
                    graphics.setFont(guiFont.getFont());
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                    find: for (int i1 = 0; i1 < 16; i1++) {
                        for (int i2 = 0; i2 < 16; i2++) {
                            var getIndex = i + i1 * 16 + i2;
                            if (getIndex >= available.size()) break find;
                            graphics.drawString(Character.toString(available.get(getIndex)), (float) (i2 * guiFont.getSize()), (i1 + 0.75F) * yAxis);
                        }
                    }
                    graphics.dispose();
                    var pngName = f.getFont().getName() + "_" + (n++) + ".png";
                    try {
                        ImageIO.write(image, "png", new File(targetFolder, pngName));
                    } catch (Exception e) {
                        PluginUtil.warn("Unable to save this file: " + pngName);
                    }
                    i += 256;
                }
            }
            if (!jsonFolder.exists()) {
                var i = 0;
                var n = 1;
                var json = new JsonArrayBuilder()
                        .add(new JsonObjectBuilder()
                                .add("type", "space")
                                .add("advances", new JsonObjectBuilder()
                                        .add(" ", 4)
                                        .build())
                                .build())
                        .build();
                while (i < available.size()) {
                    var charArray = new JsonArray();
                    find: for (int i1 = 0; i1 < 16; i1++) {
                        var sb = new StringBuilder();
                        for (int i2 = 0; i2 < 16; i2++) {
                            var getIndex = i + i1 * 16 + i2;
                            if (getIndex >= available.size()) break find;
                            sb.append(available.get(getIndex));
                        }
                        charArray.add(sb.toString());
                    }
                    var pngName = f.getFont().getName() + "_" + (n++) + ".png";
                    json.add(new JsonObjectBuilder()
                            .add("type", "bitmap")
                            .add("file", "inventory:font/font/" + f.getFont().getName() + "/" + pngName)
                            .add("ascent", f.getAscent())
                            .add("height", f.getHeight())
                            .add("chars", charArray)
                            .build());
                    i += 256;
                }
                try (var writer = new JsonWriter(new BufferedWriter(new FileWriter(jsonFolder)))) {
                    gson.toJson(new JsonObjectBuilder()
                            .add("providers", json)
                            .build(), writer);
                } catch (Exception e) {
                    PluginUtil.warn("Unable to save gui.json.");
                }
            }
        });

        var array = new JsonArray();
        images.stream().sorted().forEach(i -> {
            var name = i.getImage().name() + ".png";
            try {
                ImageIO.write(i.getImage().image(), "png", new File(texturesFontGui, name));
                array.add(new JsonObjectBuilder()
                        .add("type", "bitmap")
                        .add("file", "inventory:font/gui/" + name)
                        .add("ascent", i.getAscent())
                        .add("height", i.getHeight())
                        .add("chars", new JsonArrayBuilder()
                                .add(i.getSerialChar())
                                .build())
                        .build());
            } catch (Exception e) {
                PluginUtil.warn("Unable to save this image: " + name);
            }
        });
        try (var writer = new JsonWriter(new BufferedWriter(new FileWriter(new File(font, "gui.json"))))) {
            gson.toJson(new JsonObjectBuilder()
                    .add("providers", array)
                    .build(), writer);
        } catch (Exception e) {
            PluginUtil.warn("Unable to save gui.json.");
        }

        ImageObjectGeneratorImpl.initialize();
    }

    @Override
    public @NotNull GuiObjectGenerator registerTask(@NotNull GuiResource resource, int height, int ascent) {
        if (resource instanceof GuiFont guiFont) {
            var generator = new FontObjectGeneratorImpl(guiFont, height, ascent);
            fonts.add(generator);
            return generator;
        } else if (resource instanceof GuiImage image) {
            var generator = new ImageObjectGeneratorImpl(image, height, ascent);
            images.add(generator);
            return generator;
        } else throw new UnsupportedOperationException("unsupported type found.");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getFile(File mother, String dir) {
        var file = new File(mother, dir);
        if (!file.exists()) file.mkdir();
        return file;
    }
}
