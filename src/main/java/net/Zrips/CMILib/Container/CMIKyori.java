package net.Zrips.CMILib.Container;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class CMIKyori {

    public static String serialize(Component comp) {
        return PlainTextComponentSerializer.plainText().serialize(comp);
    }

    public static Component deserialize(String text) {
        return PlainTextComponentSerializer.plainText().deserialize(text);
    }
}
