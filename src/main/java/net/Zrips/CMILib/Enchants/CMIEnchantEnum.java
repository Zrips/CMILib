package net.Zrips.CMILib.Enchants;

import org.bukkit.enchantments.Enchantment;

public enum CMIEnchantEnum {
    AQUA_AFFINITY("Water Worker"),
    BANE_OF_ARTHROPODS("Damage Arthropods"),
    BINDING_CURSE,
    BLAST_PROTECTION("Protection Explosions"),
    CHANNELING,
    DEPTH_STRIDER,
    EFFICIENCY("Dig Speed"),
    FEATHER_FALLING("Protection Fall"),
    FIRE_ASPECT,
    FIRE_PROTECTION("Protection Fire"),
    FLAME("Arrow Fire"),
    FORTUNE("Loot Bonus Blocks"),
    FROST_WALKER,
    IMPALING,
    INFINITY("Arrow Infinite"),
    KNOCKBACK,
    LOOTING("Loot Bonus Mobs"),
    LOYALTY,
    LUCK_OF_THE_SEA("Luck"),
    LURE,
    MENDING,
    MULTISHOT,
    PIERCING,
    POWER("Arrow Damage"),
    PROJECTILE_PROTECTION("Protection Projectile"),
    PROTECTION("Protection Environmental"),
    PUNCH("Arrow Knockback"),
    QUICK_CHARGE,
    RESPIRATION("Oxygen"),
    RIPTIDE,
    SHARPNESS("Damage All"),
    SILK_TOUCH,
    SMITE("Damage Undead"),
    SOUL_SPEED,
    SWEEPING("Sweeping Edge"),
    SWIFT_SNEAK,
    THORNS,
    UNBREAKING("Durability"),
    VANISHING_CURSE;

    private String alternative = null;

    CMIEnchantEnum() {
        this.alternative = this.toString();
    }

    CMIEnchantEnum(String alternative) {
        this.alternative = alternative.replace(" ", "_").toUpperCase();
    }

    public String getName() {
        return this.toString();
    }

    public String getAlternativeName() {
        return alternative;
    }

    public Enchantment getEnchantment() {
        Enchantment enchant = CMIEnchantment.getByName(this.toString());
        if (enchant != null)
            return enchant;
        return CMIEnchantment.getByName(getAlternativeName());
    }
}
