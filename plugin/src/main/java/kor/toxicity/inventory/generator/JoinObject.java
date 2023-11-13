package kor.toxicity.inventory.generator;

import kor.toxicity.inventory.api.gui.GuiObject;
import kor.toxicity.inventory.api.gui.GuiObjectGenerator;
import kor.toxicity.inventory.util.AdventureUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class JoinObject implements GuiObject {

    private static final Component ONE_BACKSPACE = AdventureUtil.getSpaceFont(-1);

    private final GuiObjectGenerator generator;
    private final Component first;
    private final Component second;


    @Override
    public @NotNull GuiObjectGenerator getGenerator() {
        return generator;
    }

    @Override
    public @NotNull Component asComponent() {
        return first.append(ONE_BACKSPACE).append(second);
    }

    @Override
    public @NotNull GuiObject append(@NotNull GuiObject object) {
        return new JoinObject(generator, asComponent(), object.asComponent());
    }
}
