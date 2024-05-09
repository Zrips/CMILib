package net.Zrips.CMILib.Items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public enum CMIPotionType {

    AWKWARD,
    FIRE_RESISTANCE,
    HARMING,
    HEALING,
    INFESTED,
    INVISIBILITY,
    LEAPING,
    LONG_FIRE_RESISTANCE,
    LONG_INVISIBILITY,
    LONG_LEAPING,
    LONG_NIGHT_VISION,
    LONG_POISON,
    LONG_REGENERATION,
    LONG_SLOWNESS,
    LONG_SLOW_FALLING,
    LONG_STRENGTH,
    LONG_SWIFTNESS,
    LONG_TURTLE_MASTER,
    LONG_WATER_BREATHING,
    LONG_WEAKNESS,
    LUCK,
    MUNDANE,
    NIGHT_VISION,
    OOZING,
    POISON,
    REGENERATION,
    SLOWNESS,
    SLOW_FALLING,
    STRENGTH,
    STRONG_HARMING,
    STRONG_HEALING,
    STRONG_LEAPING,
    STRONG_POISON,
    STRONG_REGENERATION,
    STRONG_SLOWNESS,
    STRONG_STRENGTH,
    STRONG_SWIFTNESS,
    STRONG_TURTLE_MASTER,
    SWIFTNESS,
    THICK,
    TURTLE_MASTER,
    WATER,
    WATER_BREATHING,
    WEAKNESS,
    WEAVING,
    WIND_CHARGED;

    private boolean extended = false;
    private boolean upgraded = false;
    private String name = null;
    private PotionType type = null;

    CMIPotionType() {
        name = CMIText.everyFirstToUpperCase(this.toString().replace(extendedPrefix, "").replace(upgradedPrefix, ""));
        extended = this.toString().startsWith(extendedPrefix);
        upgraded = this.toString().startsWith(upgradedPrefix);

        for (PotionType one : PotionType.values()) {
            if (one == null)
                continue;
            if (!one.toString().equalsIgnoreCase(this.toString()))
                continue;
            type = one;
        }

    }

    public PotionType getType() {
        return type;
    }

    private static final String extendedPrefix = "LONG_";
    private static final String upgradedPrefix = "STRONG_";

    private static final Map<String, PotionType> byName = new HashMap<String, PotionType>();
    private static final Map<PotionType, CMIPotionType> byType = new HashMap<PotionType, CMIPotionType>();

    static {
        for (CMIPotionType one : CMIPotionType.values()) {
            if (one == null || one.getType() == null)
                continue;
            byName.put(one.toString().replace(" ", "").replace("_", "").toLowerCase(), one.getType());
            byType.put(one.getType(), one);
        }
        CMIScheduler.runTask(() -> {
            for (PotionType one : PotionType.values()) {
                if (one == null)
                    continue;
                byName.putIfAbsent(one.toString().replace(" ", "").replace("_", "").toLowerCase(), one);
            }
        });
    }

    public String getName() {
        return name;
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public boolean isExtended() {
        return extended;
    }

    @Deprecated
    public static PotionType get(PotionType potionType, boolean upgraded, boolean extended) {

        if (potionType == null)
            return null;
        CMIPotionType cmiType = get(potionType);

        String name = cmiType == null ? potionType.toString() : cmiType.toString();
        if (upgraded && !name.startsWith(upgradedPrefix))
            name = upgradedPrefix + name;
        if (extended && !name.startsWith(extendedPrefix))
            name = extendedPrefix + name;

        return get(name);
    }

    public static PotionType get(PotionEffectType type) {

        PotionType by = PotionType.getByEffect(type);
        if (by != null)
            return by;

        return get(type.getName());

    }

    public static PotionType get(String name) {
        if (name == null)
            return null;
        return byName.get(name.replace(" ", "").replace("_", "").toLowerCase());
    }

    public static CMIPotionType get(PotionType type) {
        if (type == null)
            return null;
        return byType.get(type);
    }
}
