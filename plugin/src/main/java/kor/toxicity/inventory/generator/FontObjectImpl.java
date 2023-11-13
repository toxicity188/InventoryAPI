package kor.toxicity.inventory.generator;

import kor.toxicity.inventory.api.gui.GuiObject;
import kor.toxicity.inventory.api.gui.GuiObjectGenerator;
import kor.toxicity.inventory.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public class FontObjectImpl implements GuiObject {
    private static final Component SPACE_COMPONENT = Component.text(' ');
    private final FontObjectGeneratorImpl objectGenerator;
    private final Component component;
    public FontObjectImpl(FontObjectGeneratorImpl objectGenerator, String text, Style style, int space, int xOffset) {
        this.objectGenerator = objectGenerator;
        var comp = Component.empty().append(AdventureUtil.getSpaceFont(xOffset));
        var spaceComp = AdventureUtil.getSpaceFont(space);
        var charArray = text.toCharArray();
        var i = 0;
        for (char c : charArray) {
            if (c == ' ') {
                comp = comp.append(SPACE_COMPONENT.font(objectGenerator.getFontKey()));
                i += 4;
            } else {
                var charWidth = objectGenerator.getFont().getFontWidth().get(c);
                var t = charWidth != null ? charWidth.apply(objectGenerator.getHeight()) : 0;
                if (style.hasDecoration(TextDecoration.BOLD)) t++;
                if (style.hasDecoration(TextDecoration.ITALIC)) t++;
                comp = comp.append(Component.text(c).style(style.font(objectGenerator.getFontKey()))
                        .append(AdventureUtil.getSpaceFont(-t))
                        .append(spaceComp));
                i += space;
            }
        }
        component = comp.append(AdventureUtil.getSpaceFont(-i));
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
        return new JoinObject(objectGenerator, component, object.asComponent());
    }
}
