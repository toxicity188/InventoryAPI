package kor.toxicity.inventory.generator;

import kor.toxicity.inventory.api.gui.GuiObject;
import kor.toxicity.inventory.api.gui.GuiObjectGenerator;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class JoinObject implements GuiObject {

    private final GuiObjectGenerator generator;
    private final Component first;
    private final Component second;


    @Override
    public @NotNull GuiObjectGenerator getGenerator() {
        return generator;
    }

    @Override
    public @NotNull Component asComponent() {
        return first.append(second);
    }

    @Override
    public @NotNull GuiObject append(@NotNull GuiObject object) {
        return new JoinObject(generator, asComponent(), object.asComponent());
    }
}
