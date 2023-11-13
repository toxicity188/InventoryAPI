package kor.toxicity.inventory.generator;

import kor.toxicity.inventory.api.gui.GuiObject;
import kor.toxicity.inventory.api.gui.GuiObjectGenerator;
import kor.toxicity.inventory.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class ImageObjectImpl implements GuiObject {
    private final ImageObjectGeneratorImpl objectGenerator;
    private final Component component;
    public ImageObjectImpl(ImageObjectGeneratorImpl objectGenerator, int xOffset) {
        this.objectGenerator = objectGenerator;
        var image = objectGenerator.getImage().image();
        var heightValue = (double) objectGenerator.getHeight() / image.getHeight();
        component = AdventureUtil.getSpaceFont(xOffset).append(
                Component.text(objectGenerator.getSerialChar())
                        .font(objectGenerator.getFontKey()))
                .append(AdventureUtil.getSpaceFont((int) -Math.round(objectGenerator.getImage().image().getWidth() * heightValue)));
    }

    @Override
    public @NotNull GuiObjectGenerator getGenerator() {
        return objectGenerator;
    }

    @Override
    public @NotNull Component asComponent() {
        return component;
    }

    @Override
    public @NotNull GuiObject append(@NotNull GuiObject object) {
        return new JoinObject(objectGenerator, asComponent(), object.asComponent());
    }
}
