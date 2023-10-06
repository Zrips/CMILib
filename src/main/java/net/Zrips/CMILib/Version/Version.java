package net.Zrips.CMILib.Version;

import org.bukkit.Bukkit;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Messages.CMIMessages;

public enum Version {

    v1_7_R1,
    v1_7_R2,
    v1_7_R3,
    v1_7_R4,
    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_13_R3,
    v1_14_R1,
    v1_14_R2,
    v1_15_R1,
    v1_15_R2,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1,
    v1_18_R1,
    v1_18_R2,
    v1_19_R1,
    v1_19_R2,
    v1_19_R3,
    v1_20_R1,
    v1_20_R2,
    v1_20_R3,
    v1_21_R1,
    v1_21_R2,
    v1_21_R3,
    v1_22_R1,
    v1_22_R2,
    v1_22_R3,
    v1_23_R1,
    v1_23_R2,
    v1_23_R3;

    private Integer value;
    private String shortVersion;
    private static int subVersion = 0;
    private static Version current = null;
    private static MinecraftPlatform platform = null;

    private static boolean testServer = false;

    static {
        getCurrent();
        CMIMessages.consoleMessage("&3Server version: " + current.toString() + " - " + current.getFormated() + " - " + getPlatform());

        // Enables extra commands for test servers
        if (CMILib.getInstance().getReflectionManager().getServerName().equals("LT_Craft") && Bukkit.getWorlds().get(0).getSeed() == 1782374759)
            testServer = true;
    }

    Version() {
        try {
            this.value = Integer.valueOf(this.name().replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
        }
        shortVersion = this.name().substring(0, this.name().length() - 3);
    }

    public Integer getValue() {
        return value;
    }

    public String getShortVersion() {
        return shortVersion;
    }

    public String getShortFormated() {
        return shortVersion.replace("v", "").replace("_", ".") + ".x";
    }

    public String getFormated() {
        return shortVersion.replace("v", "").replace("_", ".") + "." + subVersion;
    }

    public static boolean isPaper() {
        return getPlatform().equals(MinecraftPlatform.paper) || getPlatform().equals(MinecraftPlatform.folia);
    }

    public static boolean isSpigot() {
        return !getPlatform().equals(MinecraftPlatform.craftbukkit);
    }

    public static boolean isFolia() {
        return getPlatform().equals(MinecraftPlatform.folia);
    }

    public static MinecraftPlatform getPlatform() {
        if (platform != null)
            return platform;

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            platform = MinecraftPlatform.folia;
            return platform;
        } catch (ClassNotFoundException e) {
        }

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            platform = MinecraftPlatform.paper;
            return platform;
        } catch (ClassNotFoundException e) {
        }

        try {
            Class.forName("org.spigotmc.SpigotConfig");
            platform = MinecraftPlatform.spigot;
        } catch (ClassNotFoundException e1) {
            platform = MinecraftPlatform.craftbukkit;
        }

        return platform;
    }

    public static Version getCurrent() {
        if (current != null)
            return current;
        String[] v = Bukkit.getServer().getClass().getPackage().getName().split("\\.");

        try {
            String vr = Bukkit.getBukkitVersion().split("-", 2)[0];
            String[] split = vr.split("\\.");
            if (split.length <= 2)
                subVersion = 0;
            else {
                subVersion = Integer.parseInt(split[2]);
            }
        } catch (Throwable e) {
        }

        String vv = v[v.length - 1];
        for (Version one : values()) {
            if (one.name().equalsIgnoreCase(vv)) {
                current = one;
                break;
            }
        }
        if (current == null)
            return Version.v1_13_R2;
        return current;
    }

    public boolean isLower(Version version) {
        return getValue() < version.getValue();
    }

    public boolean isHigher(Version version) {
        return getValue() > version.getValue();
    }

    public boolean isEqualOrLower(Version version) {
        return getValue() <= version.getValue();
    }

    public boolean isEqualOrHigher(Version version) {
        return getValue() >= version.getValue();
    }

    public static boolean isCurrentEqualOrHigher(Version v) {
        return current.getValue() >= v.getValue();
    }

    public static boolean isCurrentHigher(Version v) {
        return current.getValue() > v.getValue();
    }

    public static boolean isCurrentLower(Version v) {
        return current.getValue() < v.getValue();
    }

    public static boolean isCurrentEqualOrLower(Version v) {
        return current.getValue() <= v.getValue();
    }

    public static boolean isCurrentEqual(Version v) {
        return current.getValue() == v.getValue();
    }

    public static boolean isCurrentSubEqualOrHigher(int subVersion) {
        return Version.subVersion >= subVersion;
    }

    public static boolean isCurrentSubHigher(int subVersion) {
        return Version.subVersion > subVersion;
    }

    public static boolean isCurrentSubLower(int subVersion) {
        return Version.subVersion < subVersion;
    }

    public static boolean isCurrentSubEqualOrLower(int subVersion) {
        return Version.subVersion <= subVersion;
    }

    public static boolean isCurrentSubEqual(int subVersion) {
        return Version.subVersion == subVersion;
    }

    public static Integer convertVersion(String v) {
        v = v.replaceAll("[^\\d.]", "");
        Integer version = 0;
        if (v.contains(".")) {
            String lVersion = "";
            for (String one : v.split("\\.")) {
                String s = one;
                if (s.length() == 1)
                    s = "0" + s;
                lVersion += s;
            }

            try {
                version = Integer.parseInt(lVersion);
            } catch (Exception e) {
            }
        } else {
            try {
                version = Integer.parseInt(v);
            } catch (Exception e) {
            }
        }
        return version;
    }

    public static String deconvertVersion(Integer v) {

        StringBuilder version = new StringBuilder();

        String vs = String.valueOf(v);

        while (vs.length() > 0) {
            int subv = 0;
            try {
                if (vs.length() > 2) {
                    subv = Integer.parseInt(vs.substring(vs.length() - 2));
                    version.insert(0, "." + subv);
                } else {
                    subv = Integer.parseInt(vs);
                    version.insert(0, subv);
                }
            } catch (Throwable e) {

            }
            if (vs.length() > 2)
                vs = vs.substring(0, vs.length() - 2);
            else
                break;
        }

        return version.toString();
    }

    public static boolean isTestServer() {
        return testServer;
    }
}
