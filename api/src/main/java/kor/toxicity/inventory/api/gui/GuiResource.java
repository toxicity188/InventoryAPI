package kor.toxicity.inventory.api.gui;

import kor.toxicity.inventory.api.InventoryAPI;
import org.jetbrains.annotations.NotNull;

public interface GuiResource {
    default @NotNull GuiObjectGenerator generator(int height, int ascent) {
        return InventoryAPI.getInstance().getResourcePackManager().registerTask(this, height, ascent);
    }
}
