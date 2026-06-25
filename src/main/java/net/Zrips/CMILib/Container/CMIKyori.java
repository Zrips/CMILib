package net.Zrips.CMILib.Container;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class CMIKyori {

    public static String serializeLegacy(Component comp) {
        return LegacyComponentSerializer.legacySection().serialize(comp);
    }

    public static String serialize(Component comp) {
        return PlainTextComponentSerializer.plainText().serialize(comp);
    }

    public static Component deserialize(String text) {
        return PlainTextComponentSerializer.plainText().deserialize(text);
    }

    public static String parse(ItemStack item) {
        Component component = Component.empty().hoverEvent(item.asHoverEvent());
        @NotNull
        String json = GsonComponentSerializer.gson().serialize(component);
        return json.substring(1, json.length() - 1);
    }
}
