package net.Zrips.CMILib.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public enum CMIPotionEffectType {

    ABSORPTION,
    BAD_OMEN,
    BLINDNESS,
    CONDUIT_POWER,
    NAUSEA("CONFUSION"),
    RESISTANCE("DAMAGE_RESISTANCE"),
    DARKNESS,
    DOLPHINS_GRACE,
    HASTE("FAST_DIGGING"),
    FIRE_RESISTANCE,
    GLOWING,
    INSTANT_DAMAGE,
    INSTANT_HEALTH,
    HEALTH_BOOST,
    HERO_OF_THE_VILLAGE,
    HUNGER,
    STRENGTH("INCREASE_DAMAGE"),
    INVISIBILITY,
    JUMP_BOOST("JUMP"),
    LEVITATION,
    LUCK,
    NIGHT_VISION,
    POISON,
    REGENERATION,
    SATURATION,
    SLOWNESS("SLOW"),
    MINING_FATIGUE("SLOW_DIGGING"),
    SLOW_FALLING,
    SPEED,
    UNLUCK,
    WATER_BREATHING,
    WEAKNESS,
    WITHER,
    INFESTED,
    OOZING,
    RAID_OMEN,
    TRIAL_OMEN,
    WEAVING,
    WIND_CHARGED;

    private String name = null;
    private String translatedName = null;
    private List<String> alternativeNames = new ArrayList<String>();
    private PotionEffectType type = null;

    CMIPotionEffectType(String string) {
        if (string != null)
            alternativeNames.add(string);
        name = CMIText.everyFirstToUpperCase(this.toString());

        for (PotionEffectType one : PotionEffectType.values()) {
            if (one == null)
                continue;
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                if (!one.getKey().getKey().replace("_", "").replace(" ", "").equalsIgnoreCase(this.toString().replace("_", "")))
                    continue;
            } else {
                if (!one.getName().replace("_", "").replace(" ", "").equalsIgnoreCase(this.toString().replace("_", "")))
                    continue;
            }
            type = one;
        }
        if (type == null) {
            for (String alternative : this.getAlternativeNames()) {
                for (PotionEffectType one : PotionEffectType.values()) {
                    if (one == null)
                        continue;
                    if (!one.getName().replace("_", "").replace(" ", "").equalsIgnoreCase(alternative.replace("_", "")))
                        continue;
                    type = one;
                    break;
                }
            }
        }
    }

    CMIPotionEffectType() {
        this(null);
    }

    public PotionEffectType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private static final Map<String, PotionEffectType> byName = new HashMap<String, PotionEffectType>();
    private static final Map<PotionEffectType, CMIPotionEffectType> byType = new HashMap<PotionEffectType, CMIPotionEffectType>();

    private static HashMap<String, CMIPotionEffectType> localizedPotionEffectTypeList = new HashMap<String, CMIPotionEffectType>();

    static {
        for (CMIPotionEffectType one : CMIPotionEffectType.values()) {
            if (one == null || one.getType() == null)
                continue;
            byName.put(one.toString().replace(" ", "").replace("_", "").toLowerCase(), one.getType());
            byType.put(one.getType(), one);
            for (String alternative : one.getAlternativeNames()) {
                byName.put(alternative.replace(" ", "").replace("_", "").toLowerCase(), one.getType());
            }
        }
        CMIScheduler.runTask(CMILib.getInstance(), () -> {
            for (PotionEffectType one : PotionEffectType.values()) {
                if (one == null)
                    continue;
                byName.putIfAbsent(one.toString().replace(" ", "").replace("_", "").toLowerCase(), one);
                byName.putIfAbsent(one.getName().replace(" ", "").replace("_", "").toLowerCase(), one);
            }
        });
    }

    public static void loadLocalization() {
        ConfigReader locale = CMILib.getInstance().getConfigManager().getLocaleConfig();

        for (CMIPotionEffectType one : byType.values()) {
            if (one == null)
                continue;

            String name = one.getName().replace(" ", "").toLowerCase();

            for (String oneL : locale.get("info.PotionEffectAliases." + name, Arrays.asList(CMIText.firstToUpperCase(one.getName())))) {
                one.translatedName = oneL;
                localizedPotionEffectTypeList.put(CMIChatColor.stripColor(oneL).replace(" ", "").replace("_", "").toLowerCase(), one);
            }
        }
    }

    public static PotionEffectType[] effectValues() {
        return byName.values().toArray(new PotionEffectType[byName.size()]);
    }

    public static PotionEffectType get(String name) {
        name = name.replace(" ", "").replace("_", "").toLowerCase();
        PotionEffectType effect = byName.get(name);
        if (effect != null)
            return effect;

        CMIPotionEffectType cmiEffect = localizedPotionEffectTypeList.get(name);

        if (cmiEffect == null) {
            PotionType potionType = CMIPotionType.get(name);
            if (potionType != null)
                return potionType.getEffectType();
            return null;
        }

        return cmiEffect.getType();
    }

    public static CMIPotionEffectType get(PotionEffectType type) {
        return type == null ? null : byType.get(type);
    }

    public String getTranslatedName() {
        return translatedName == null ? name : translatedName;
    }

    public List<String> getAlternativeNames() {
        return alternativeNames;
    }
}
