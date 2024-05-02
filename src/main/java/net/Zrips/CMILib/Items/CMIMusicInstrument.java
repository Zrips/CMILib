package net.Zrips.CMILib.Items;

import java.util.HashMap;

import org.bukkit.MusicInstrument;

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

    static HashMap<String, org.bukkit.MusicInstrument> instruments = new HashMap<String, org.bukkit.MusicInstrument>();

    static {
        try {
            for (MusicInstrument one : org.bukkit.MusicInstrument.values()) {
                instruments.put(one.toString().replace("_GOAT_HORN", "").replace("_", "").toLowerCase(), one);
                instruments.put(one.toString().replace("_", "").toLowerCase(), one);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static org.bukkit.MusicInstrument getByName(String name) {
        return instruments.get(name.toLowerCase().replace("_GOAT_HORN", ""));
    }
}
