package kor.toxicity.inventory.api.manager;

import kor.toxicity.inventory.api.gui.GuiObjectGenerator;
import kor.toxicity.inventory.api.gui.GuiResource;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface ResourcePackManager extends InventoryManager {
    @NotNull GuiObjectGenerator registerTask(@NotNull GuiResource resource, int height, int ascent);
    @NotNull Material getEmptyMaterial();
}
