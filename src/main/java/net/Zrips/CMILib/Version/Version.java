package net.Zrips.CMILib.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    v1_20_R3(4),
    v1_20_R4(5, 6),
    v1_21_R1(0, 1),
    v1_21_R2(2, 3),
    v1_21_R3(4),
    v1_21_R4(5),
    v1_21_R5(6, 7, 8),
    v1_21_R6(9, 10),
    v1_21_R7(11),
    v1_22_R1(0),
    v1_22_R2(1),
    v1_22_R3(2),
    v1_23_R1(0),
    v1_23_R2,
    v1_23_R3,
    v1_24_R1(0),
    v1_24_R2,
    v1_24_R3;

    //Future change to be based on specific versions and not NMS mappings
//    v1_7(7, 0, 1),
//    v1_7_5(7, 5, 2),
//    v1_7_8(7, 8, 3),
//    v1_7_10(7, 10, 4),
//    v1_8(8, 0, 1),
//    v1_8_1(8, 1, 1),
//    v1_8_2(8, 2, 1),
//    v1_8_3(8, 3, 2),
//    v1_8_4(8, 4, 2),
//    v1_8_8(8, 8, 3),
//    v1_9(9, 0, 1),
//    v1_9_4(9, 4, 2),
//    v1_10(10, 0, 1),
//    v1_11(11, 0, 1),
//    v1_11_2(11, 2, 1),
//    v1_12(12, 0, 1),
//    v1_12_1(12, 1, 1),
//    v1_12_2(12, 2, 1),
//    v1_13(13, 0, 1),
//    v1_13_1(13, 1, 2),
//    v1_13_2(13, 2, 2),
//    v1_14(14, 0, 1),
//    v1_14_2(14, 2, 1),
//    v1_14_3(14, 3, 1),
//    v1_14_4(14, 4, 1),
//    v1_15(15, 0, 1),
//    v1_15_1(15, 1, 1),
//    v1_15_2(15, 2, 1),
//    v1_16(16, 0, 1),
//    v1_16_1(16, 1, 1),
//    v1_16_2(16, 2, 2),
//    v1_16_3(16, 3, 2),
//    v1_16_4(16, 4, 3),
//    v1_16_5(16, 5, 3),
//    v1_17(17, 0, 1),
//    v1_17_1(17, 1, 1),
//    v1_18(18, 0, 1),
//    v1_18_1(18, 1, 1),
//    v1_18_2(18, 2, 2),
//    v1_19(19, 0, 1),
//    v1_19_1(19, 1, 1),
//    v1_19_2(19, 2, 1),
//    v1_19_3(19, 3, 2),
//    v1_19_4(19, 4, 3),
//    v1_20(20, 0, 1),
//    v1_20_1(20, 1, 1),
//    v1_20_2(20, 2, 2),
//    v1_20_3(20, 3, 3),
//    v1_20_4(20, 4, 3),
//    v1_20_5(20, 5, 4),
//    v1_20_6(20, 6, 4),
//    v1_21(21, 0, 1),
//    v1_21_1(21, 1, 1),
//    v1_21_2(21, 2, 1),
//    v1_21_3(21, 3, 2),
//    v1_21_4(21, 4, 3),
//    v1_21_5(21, 5, 4),
//    v1_21_6(21, 6, 5),
//    v1_21_7(21, 7, 5),
//    v1_21_8(21, 8, 5),
//    v1_21_9(21, 9, 6),
//    v1_21_10(21, 10, 6),
//    v1_21_11(21, 11, 7);

    private Integer value;
    private int[] minorVersions = null;

    private int majorVersion = 0;
    private int minorVersion = 0;
    private int nmsVersion = 1;
    
    private String shortVersion;
    private static int subVersion = 0;
    private static Version current = null;
    private static MinecraftPlatform platform = null;

    private static boolean testServer = false;

    static {
        getCurrent();
        CMIMessages.consoleMessage("&3Server version: " + getCurrent().toString() + " - " + getCurrent().getFormated() + " - " + getPlatform() + "  " + Bukkit.getVersion());

        // Enables extra commands for test servers
        if (CMILib.getInstance().getReflectionManager().getServerName().equals("LT_Craft") && Bukkit.getWorlds().get(0).getSeed() == 1782374759)
            testServer = true;
    }


    Version(int majorVersion, int minorVersion, int nmsVersion) {
        this();
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
		this.nmsVersion = nmsVersion;
    }

    Version(int... versions) {
        this();
        minorVersions = versions;
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

    public static boolean isAsyncProcessing() {
        return getPlatform().isAsync();
    }

    public static boolean isPaperBranch() {
        return getPlatform().isAsync();
    }

    public static boolean isMojangMappings() {
        return Version.isCurrentEqualOrHigher(Version.v1_21_R7) && Version.isPaperBranch();
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

    @Deprecated
    public static boolean isPaper() {
        return isPaperBranch();
    }

    public static boolean isSpigot() {
        return !getPlatform().equals(MinecraftPlatform.craftbukkit);
    }

    public static boolean isFolia() {
        return getPlatform().equals(MinecraftPlatform.folia);
    }

    public static boolean isPurpur() {
        return getPlatform().equals(MinecraftPlatform.purpur);
    }

    public static MinecraftPlatform getPlatform() {
        if (platform != null)
            return platform;

        String version = Bukkit.getVersion().toLowerCase();

        if (version.contains("mohist")) {
            platform = MinecraftPlatform.mohist;
            return platform;
        }

        if (version.contains("arclight")) {
            platform = MinecraftPlatform.arclight;
            return platform;
        }

        if (version.contains("purpur")) {
            platform = MinecraftPlatform.purpur;
            return platform;
        }

        if (version.contains("tuinity")) {
            platform = MinecraftPlatform.tuinity;
            return platform;
        }

        if (version.contains("yatopia")) {
            platform = MinecraftPlatform.yatopia;
            return platform;
        }

        if (version.contains("tacospigot")) {
            platform = MinecraftPlatform.tacospigot;
            return platform;
        }

        if (version.contains("glowstone")) {
            platform = MinecraftPlatform.glowstone;
            return platform;
        }

        if (version.contains("pufferfish")) {
            platform = MinecraftPlatform.pufferfish;
            return platform;
        }
        if (version.contains("airplane")) {
            platform = MinecraftPlatform.airplane;
            return platform;
        }

        if (version.contains("fabric")) {
            platform = MinecraftPlatform.fabric;
            return platform;
        }

        if (version.contains("magma")) {
            platform = MinecraftPlatform.magma;
            return platform;
        }

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            platform = MinecraftPlatform.folia;
            return platform;
        } catch (ClassNotFoundException e) {
        }

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");

            try {
                String versionText = (String) Bukkit.class.getMethod("getVersionMessage").invoke(Bukkit.class);

                for (MinecraftPlatform one : MinecraftPlatform.values()) {
                    if (!one.isAsync()) {
                        continue;
                    }

                    if (!versionText.toLowerCase().contains(one.toString().toLowerCase()))
                        continue;
                    platform = one;
                }

            } catch (Throwable e) {
            }

            if (platform == null)
                platform = MinecraftPlatform.paper;
            return platform;
        } catch (

        ClassNotFoundException e) {
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
// Paper returns examples as of 1.20
//        Bukkit.getServer().getClass().getPackage().getName();   org.bukkit.craftbukkit
//        Bukkit.getBukkitVersion();                              1.21-R0.1-SNAPSHOT
//        Bukkit.getMinecraftVersion();                           1.21
//        Bukkit.getVersion();                                    1.21-4-090775e (MC: 1.21)
//        Bukkit.getVersionMessage();                             This server is running Paper version 1.21-4-master@090775e (2024-06-18T13:42:35Z) (Implementing API version 1.21-R0.1-SNAPSHOT)

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

        if (current != null)
            return current;

        String ve = Bukkit.getBukkitVersion().split("-", 2)[0];
        main: for (Version one : values()) {
            if (one.name().equalsIgnoreCase(ve)) {
                current = one;
                break;
            }
            List<String> cleanVersion = one.getMinorVersions();
            for (String cv : cleanVersion) {
                if (ve.equalsIgnoreCase(cv)) {
                    current = one;
                    break main;
                }
            }
        }

        if (current != null)
            return current;

        ve = Bukkit.getBukkitVersion().replaceAll("^(\\d+\\.\\d+).*", "$1");
        main: for (int i = 1; i < 10; i++) {
            try {
                Class.forName("org.bukkit.craftbukkit.v" + ve.replace(".", "_") + "_R" + i + ".entity.CraftPlayer");
                for (Version one : Version.values()) {
                    if (one.name().equalsIgnoreCase("v" + ve.replace(".", "_") + "_R" + i)) {
                        current = one;
                        break main;
                    }
                }
                break;
            } catch (ClassNotFoundException e) {
            }
        }

        if (current != null)
            return current;

        for (Version one : values()) {
            if (ve.startsWith(one.getSimplifiedVersion()) || ve.startsWith(one.getSimplifiedVersion().substring(0, one.getSimplifiedVersion().length() - 1))) {
                current = one;
                CMIMessages.consoleMessage("&c[CMILib] &eServer version detection needs aditional update");
                break;
            }
        }

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
        return getCurrent().getValue() >= v.getValue();
    }

    public static boolean isCurrentHigher(Version v) {
        return getCurrent().getValue() > v.getValue();
    }

    public static boolean isCurrentLower(Version v) {
        return getCurrent().getValue() < v.getValue();
    }

    public static boolean isCurrentEqualOrLower(Version v) {
        return getCurrent().getValue() <= v.getValue();
    }

    public static boolean isCurrentEqual(Version v) {
        return getCurrent().getValue() == v.getValue();
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

    private String getSimplifiedVersion() {
        return this.name().substring(1).replace("_", ".").split("R", 2)[0];
    }

    public List<String> getMinorVersions() {

        if (minorVersions == null)
            return new ArrayList<String>();

        return Arrays.stream(minorVersions)
                .mapToObj(version -> getSimplifiedVersion() + version)
                .collect(Collectors.toList());
    }
}
