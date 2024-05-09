package net.Zrips.CMILib.Items;

import org.bukkit.potion.PotionEffectType;

@Deprecated
public class CMIPotionEffect {

    @Deprecated
    public static PotionEffectType getById2(int id) {
        return null;
    }

    @Deprecated
    public static PotionEffectType get2(String nameId) {
        return CMIPotionEffectType.get(nameId);
    }

    @Deprecated
    public static PotionEffectType getByName2(String name) {
        return CMIPotionEffectType.get(name);
    }

    @Deprecated
    public static PotionEffectType[] values2() {
        return CMIPotionEffectType.effectValues();
    }

    @Deprecated
    public static String getName2(PotionEffectType enchant) {
        CMIPotionEffectType cmiEffect = CMIPotionEffectType.get(enchant);
        if (cmiEffect == null)
            return null;
        return cmiEffect.getTranslatedName();
    }

    @Deprecated
    public static void initialize2() {
        CMIPotionEffectType.loadLocalization();
    }
}
