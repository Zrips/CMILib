package net.Zrips.CMILib.Container;

import org.bukkit.potion.PotionEffectType;

import net.Zrips.CMILib.Items.CMIPotionEffectType;

@Deprecated
public class CMIPotionEffect {

    @Deprecated
    public static PotionEffectType getById(int id) {
        return null;
    }

    @Deprecated
    public static PotionEffectType get(String nameId) {
        return CMIPotionEffectType.get(nameId);
    }

    @Deprecated
    public static PotionEffectType getByName(String name) {
        return CMIPotionEffectType.get(name);
    }

    @Deprecated
    public static PotionEffectType[] values() {
        return CMIPotionEffectType.effectValues();
    }

    @Deprecated
    public static String getName(PotionEffectType enchant) {
        CMIPotionEffectType cmiEffect = CMIPotionEffectType.get(enchant);
        if (cmiEffect == null)
            return null;
        return cmiEffect.getTranslatedName();
    }

    @Deprecated
    public static void initialize() {
        CMIPotionEffectType.loadLocalization();
    }
}
