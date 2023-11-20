package net.Zrips.CMILib.Items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CMIPotionType {

    private static final Map<String, PotionType> byName = new HashMap<String, PotionType>();

    static {
        for (PotionType one : PotionType.values()) {
            if (one == null)
                continue;
            byName.put(one.toString().replace(" ", "").replace("_", "").toLowerCase(), one);
        }
    }

    public static PotionType get(PotionEffectType type) {
        PotionType by = PotionType.getByEffect(type);
        if (by != null)
            return by;

        return get(type.getName().replace(" ", "").replace("_", ""));

    }

    public static PotionType get(String name) {
        if (name == null)
            return null;
        return byName.get(name);
    }

}
