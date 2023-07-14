package net.Zrips.CMILib.Items;

public enum CMITrimMaterial {

    AMETHYST,
    COPPER,
    DIAMOND,
    EMERALD,
    GOLD,
    IRON,
    LAPIS,
    NETHERITE,
    QUARTZ,
    REDSTONE;

    private CMITrimMaterial() {

    }

    public org.bukkit.inventory.meta.trim.TrimMaterial get() {
        return getByName(this.toString());
    }

    public static org.bukkit.inventory.meta.trim.TrimMaterial getByName(String name) {
        switch (name.toUpperCase()) {
        case "AMETHYST":
            return org.bukkit.inventory.meta.trim.TrimMaterial.AMETHYST;
        case "COPPER":
            return org.bukkit.inventory.meta.trim.TrimMaterial.COPPER;
        case "DIAMOND":
            return org.bukkit.inventory.meta.trim.TrimMaterial.DIAMOND;
        case "EMERALD":
            return org.bukkit.inventory.meta.trim.TrimMaterial.EMERALD;
        case "GOLD":
            return org.bukkit.inventory.meta.trim.TrimMaterial.GOLD;
        case "IRON":
            return org.bukkit.inventory.meta.trim.TrimMaterial.IRON;
        case "LAPIS":
            return org.bukkit.inventory.meta.trim.TrimMaterial.LAPIS;
        case "NETHERITE":
            return org.bukkit.inventory.meta.trim.TrimMaterial.NETHERITE;
        case "QUARTZ":
            return org.bukkit.inventory.meta.trim.TrimMaterial.QUARTZ;
        case "REDSTONE":
            return org.bukkit.inventory.meta.trim.TrimMaterial.REDSTONE;
        }
        return null;
    }

}
