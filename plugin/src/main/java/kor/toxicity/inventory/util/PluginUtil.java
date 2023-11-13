package kor.toxicity.inventory.util;

import kor.toxicity.inventory.api.InventoryAPI;

import java.io.File;
import java.util.function.Consumer;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PluginUtil {
    private PluginUtil() {
        throw new SecurityException();
    }

    public static void loadFolder(String dir, Consumer<File> fileConsumer) {
        var dataFolder = InventoryAPI.getInstance().getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        var folder = new File(dataFolder, dir);
        if (!folder.exists()) folder.mkdir();
        var listFiles = folder.listFiles();
        if (listFiles != null) for (File listFile : listFiles) {
            fileConsumer.accept(listFile);
        }
    }

    public static FileName getFileName(File file) {
        var name = file.getName().split("\\.");
        return new FileName(name[0], name.length > 1 ? name[1] : "");
    }

    public static void warn(String message) {
        InventoryAPI.getInstance().getLogger().warning(message);
    }

    public record FileName(String name, String extension) {
    }
}
