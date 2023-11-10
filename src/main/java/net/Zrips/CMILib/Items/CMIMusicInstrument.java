package net.Zrips.CMILib.Items;

public enum CMIMusicInstrument {
    PONDER, SING, SEEK, FEEL, ADMIRE, CALL, YEARN, DREAM;

    public org.bukkit.MusicInstrument get() {
        return getByName(this.toString());
    }

    public static CMIMusicInstrument get(String name) {
        for (CMIMusicInstrument one : CMIMusicInstrument.values()) {
            if (one.toString().equalsIgnoreCase(name))
                return one;
        }
        return null;
    }

    public static org.bukkit.MusicInstrument getByName(String name) {
        switch (name.toUpperCase()) {
        case "PONDER":
            return org.bukkit.MusicInstrument.PONDER;
        case "SING":
            return org.bukkit.MusicInstrument.SING;
        case "SEEK":
            return org.bukkit.MusicInstrument.SEEK;
        case "FEEL":
            return org.bukkit.MusicInstrument.FEEL;
        case "ADMIRE":
            return org.bukkit.MusicInstrument.ADMIRE;
        case "CALL":
            return org.bukkit.MusicInstrument.CALL;
        case "YEARN":
            return org.bukkit.MusicInstrument.YEARN;
        case "DREAM":
            return org.bukkit.MusicInstrument.DREAM;
        }
        return null;
    }
}
