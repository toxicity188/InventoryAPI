package kor.toxicity.inventory.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

public class AdventureUtil {
    private static final Key SPACE_KEY = Key.key("inventory:space");
    private AdventureUtil() {
        throw new SecurityException();
    }
    public static String parseChar(int i) {
        if (i <= 0xFFFF) return Character.toString((char) i);
        else {
            var t = i - 0x10000;
            return Character.toString((t >>> 10) + 0xD800) + Character.toString((t & ((1 << 10) - 1)) + 0xDC00);
        }
    }

    public static Component getSpaceFont(int i) {
        return Component.text(parseChar(i + 0xD0000)).font(SPACE_KEY);
    }
}
