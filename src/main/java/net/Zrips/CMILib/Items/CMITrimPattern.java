package net.Zrips.CMILib.Items;

public enum CMITrimPattern {
    COAST,
    DUNE,
    EYE,
    HOST,
    RAISER,
    RIB,
    SENTRY,
    SHAPER,
    SILENCE,
    SNOUT,
    SPIRE,
    TIDE,
    VEX,
    WARD,
    WAYFINDER,
    WILD;

    private CMITrimPattern() {

    }

    public org.bukkit.inventory.meta.trim.TrimPattern get() {
        return getByName(this.toString());
    }

    public static org.bukkit.inventory.meta.trim.TrimPattern getByName(String name) {
        switch (name.toUpperCase()) {
        case "COAST":
            return org.bukkit.inventory.meta.trim.TrimPattern.COAST;
        case "DUNE":
            return org.bukkit.inventory.meta.trim.TrimPattern.DUNE;
        case "EYE":
            return org.bukkit.inventory.meta.trim.TrimPattern.EYE;
        case "HOST":
            return org.bukkit.inventory.meta.trim.TrimPattern.HOST;
        case "RAISER":
            return org.bukkit.inventory.meta.trim.TrimPattern.RAISER;
        case "RIB":
            return org.bukkit.inventory.meta.trim.TrimPattern.RIB;
        case "SENTRY":
            return org.bukkit.inventory.meta.trim.TrimPattern.SENTRY;
        case "SHAPER":
            return org.bukkit.inventory.meta.trim.TrimPattern.SHAPER;
        case "SILENCE":
            return org.bukkit.inventory.meta.trim.TrimPattern.SILENCE;
        case "SNOUT":
            return org.bukkit.inventory.meta.trim.TrimPattern.SNOUT;
        case "SPIRE":
            return org.bukkit.inventory.meta.trim.TrimPattern.SPIRE;
        case "TIDE":
            return org.bukkit.inventory.meta.trim.TrimPattern.TIDE;
        case "VEX":
            return org.bukkit.inventory.meta.trim.TrimPattern.VEX;
        case "WARD":
            return org.bukkit.inventory.meta.trim.TrimPattern.WARD;
        case "WAYFINDER":
            return org.bukkit.inventory.meta.trim.TrimPattern.WAYFINDER;
        case "WILD":
            return org.bukkit.inventory.meta.trim.TrimPattern.WILD;
        }
        return null;
    }
}
