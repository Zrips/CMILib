package net.Zrips.CMILib.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Messages.CMIMessages;

public enum Version {

    /** @deprecated Use {@link #v1_7_10} */
    v1_7_R1(1),
    /** @deprecated Use {@link #v1_7_10} */
    v1_7_R2(2),
    /** @deprecated Use {@link #v1_7_10} */
    v1_7_R3(3),
    /** @deprecated Use {@link #v1_7_10} */
    v1_7_R4(10),
    /** @deprecated Use {@link #v1_8_0} {@link #v1_8_1} {@link #v1_8_2} */
    v1_8_R1(0, 1, 2),
    /** @deprecated Use {@link #v1_8_3} */
    v1_8_R2(3),
    /**
     * @deprecated Use {@link #v1_8_4} {@link #v1_8_5} {@link #v1_8_6}
     *             {@link #v1_8_7} {@link #v1_8_8}
     */
    v1_8_R3(4, 5, 6, 7, 8),
    /**
     * @deprecated Use {@link #v1_9_0} {@link #v1_9_1} {@link #v1_9_2}
     *             {@link #v1_9_3}
     */
    v1_9_R1(0, 1, 2, 3),
    /** @deprecated Use {@link #v1_9_4} */
    v1_9_R2(4),
    /** @deprecated Use {@link #v1_10_0} {@link #v1_10_1} {@link #v1_10_2} */
    v1_10_R1(0, 1, 2),
    /** @deprecated Use {@link #v1_11_0} {@link #v1_11_1} {@link #v1_11_2} */
    v1_11_R1(0, 1, 2),
    /** @deprecated Use {@link #v1_12_0} {@link #v1_12_1} {@link #v1_12_2} */
    v1_12_R1(0, 1, 2),
    /** @deprecated Use {@link #v1_13_0} */
    v1_13_R1(0),
    /** @deprecated Use {@link #v1_13_1} {@link #v1_13_2} */
    v1_13_R2(1, 2),
    /** @deprecated Use {@link #v1_13_3} */
    v1_13_R3(3),
    /**
     * @deprecated Use {@link #v1_14_0} {@link #v1_14_1} {@link #v1_14_2}
     *             {@link #v1_14_3} {@link #v1_14_4}
     */
    v1_14_R1(0, 1, 2, 3, 4),
    /** @deprecated Use {@link #v1_14_5} */
    v1_14_R2(5),
    /** @deprecated Use {@link #v1_15_0} {@link #v1_15_1} {@link #v1_15_2} */
    v1_15_R1(0, 1, 2),
    /** @deprecated Use {@link #v1_15_3} */
    v1_15_R2(3),
    /** @deprecated Use {@link #v1_16_0} {@link #v1_16_1} */
    v1_16_R1(0, 1),
    /** @deprecated Use {@link #v1_16_2} {@link #v1_16_3} */
    v1_16_R2(2, 3),
    /** @deprecated Use {@link #v1_16_4} {@link #v1_16_5} */
    v1_16_R3(4, 5),
    /** @deprecated Use {@link #v1_17_0} {@link #v1_17_1} */
    v1_17_R1(0, 1),
    /** @deprecated Use {@link #v1_18_0} {@link #v1_18_1} */
    v1_18_R1(0, 1),
    /** @deprecated Use {@link #v1_18_2} */
    v1_18_R2(2),
    /** @deprecated Use {@link #v1_19_0} {@link #v1_19_1} {@link #v1_19_2} */
    v1_19_R1(0, 1, 2),
    /** @deprecated Use {@link #v1_19_3} */
    v1_19_R2(3),
    /** @deprecated Use {@link #v1_19_4} */
    v1_19_R3(4),
    /** @deprecated Use {@link #v1_20_0} {@link #v1_20_1} */
    v1_20_R1(0, 1),
    /** @deprecated Use {@link #v1_20_2} */
    v1_20_R2(2),
    /** @deprecated Use {@link #v1_20_3} {@link #v1_20_4} */
    v1_20_R3(3, 4),
    /** @deprecated Use {@link #v1_20_5} {@link #v1_20_6} */
    v1_20_R4(5, 6),
    /** @deprecated Use {@link #v1_21_0} {@link #v1_21_1} */
    v1_21_R1(0, 1),
    /** @deprecated Use {@link #v1_21_2} {@link #v1_21_3} */
    v1_21_R2(2, 3),
    /** @deprecated Use {@link #v1_21_4} */
    v1_21_R3(4),
    /** @deprecated Use {@link #v1_21_5} */
    v1_21_R4(5),
    /** @deprecated Use {@link #v1_21_6} {@link #v1_21_7} {@link #v1_21_8} */
    v1_21_R5(6, 7, 8),
    /** @deprecated Use {@link #v1_21_9} {@link #v1_21_10} */
    v1_21_R6(9, 10),
    /** @deprecated Use {@link #v1_21_11} */
    v1_21_R7(11),

    v1_7_10(4),
    v1_8_0,
    v1_8_1,
    v1_8_2,
    v1_8_3(2),
    v1_8_4(3),
    v1_8_5(3),
    v1_8_6(3),
    v1_8_7(3),
    v1_8_8(3),
    v1_9_0,
    v1_9_1,
    v1_9_2,
    v1_9_3,
    v1_9_4(2),
    v1_10_0,
    v1_10_1,
    v1_10_2,
    v1_11_0,
    v1_11_1,
    v1_11_2,
    v1_12_0,
    v1_12_1,
    v1_12_2,
    v1_13_0,
    v1_13_1(2),
    v1_13_2(2),
    v1_14_0,
    v1_14_2,
    v1_14_3,
    v1_14_4,
    v1_15_0,
    v1_15_1,
    v1_15_2,
    v1_16_0,
    v1_16_1,
    v1_16_2(2),
    v1_16_3(2),
    v1_16_4(3),
    v1_16_5(3),
    v1_17_0,
    v1_17_1,
    v1_18_0,
    v1_18_1,
    v1_18_2(2),
    v1_19_0,
    v1_19_1,
    v1_19_2,
    v1_19_3(2),
    v1_19_4(3),
    v1_20_0,
    v1_20_1,
    v1_20_2(2),
    v1_20_3(3),
    v1_20_4(3),
    v1_20_5(4),
    v1_20_6(4),
    v1_21_0,
    v1_21_1,
    v1_21_2,
    v1_21_3(2),
    v1_21_4(3),
    v1_21_5(4),
    v1_21_6(5),
    v1_21_7(5),
    v1_21_8(5),
    v1_21_9(6),
    v1_21_10(6),
    v1_21_11(7),

    // Change in version naming from 2026
    v26_1_0,
    v26_1_1,
    v26_2_0,
    v26_3_0,
    v27_1_0,
    v27_2_0,
    v27_3_0,
    v28_1_0,
    v28_2_0,
    v28_3_0,

    // Fallback version in case current isn't in the list
    v99_99_99;

    private int value = 0;
    @Deprecated
    private int[] minorVersions = new int[0];

    private int majorVersion = 0;
    private int minorVersion = 0;
    private int patchVersion = 0;
    private int nmsVersion = 1;

    boolean legacy = false;

    private static int subVersion = 0;
    private static Version current = null;
    private static MinecraftPlatform platform = null;
    private static boolean testServer = false;

    static {
        getCurrent();
        CMIMessages.consoleMessage("&eServer version: " + getCurrent().toString() + " - " + getCurrent().getCleanVersion() + " - " + getPlatform() + "  " + Bukkit.getVersion());

        // Enables extra commands for test servers
        if (CMILib.getInstance().getReflectionManager().getServerName().equals("LT_Craft") && Bukkit.getWorlds().get(0).getSeed() == 1782374759)
            testServer = true;
    }

    Version(int... versions) {

        this.legacy = this.name().contains("R");

        if (this.isLegacy()) {
            minorVersions = versions;
        } else if (versions != null && versions.length > 0) {
            this.nmsVersion = versions[0];
        }

        try {

            String cleanName = this.name().substring(1, this.name().length());
            String majorValue = "1";
            String minorValue = "0";
            String patchValue = "0";

            if (isLegacy()) {
                minorValue = cleanName.split("_", 3)[1];
                if (this.minorVersions.length > 0)
                    patchValue = String.valueOf(this.minorVersions[patchValue.length() - 1]);

                this.nmsVersion = Integer.parseInt(cleanName.split("R", 2)[1]);
            } else {
                String[] split = cleanName.split("_");
                majorValue = split[0];
                if (split.length > 1)
                    minorValue = split[1];
                if (split.length > 2)
                    patchValue = split[2];

                if (minorVersions.length > 0) {
                    this.nmsVersion = this.minorVersions[minorValue.length() - 1];
                    this.minorVersions = new int[0];
                }
            }

            this.majorVersion = Integer.parseInt(majorValue);
            this.minorVersion = Integer.parseInt(minorValue);
            this.patchVersion = Integer.parseInt(patchValue);

            this.value = (this.getMajorVersion() * 10000) + (this.getMinorVersion() * 100) + getPatchVersion();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Version() {
        this(new int[0]);
    }

    public String getRevisionVersionName() {
        return this.isLegacy() ? this.name() : "v1_" + getMajorVersion() + "_R" + nmsVersion;
    }

    @Deprecated
    public Integer getValue() {
        return value;
    }

    public int getVersionValue() {
        return value;
    }

    public static boolean isAsyncProcessing() {
        return getPlatform().isAsync();
    }

    public static boolean isPaperBranch() {
        return getPlatform().isAsync();
    }

    public static boolean isMojangMappings() {
        return Version.isCurrentEqualOrHigher(Version.v26_1_0) || Version.isCurrentEqualOrHigher(Version.v1_21_11) && Version.isPaperBranch();
    }

    public String getCleanVersion() {
        return getMajorVersion() + "." + getMinorVersion() + "." + getPatchVersion();
    }

    public String getShortVersion() {
        return getMajorVersion() + "." + getMinorVersion();
    }

    public String getShortFormated() {
        return getShortVersion() + ".x";
    }

    @Deprecated
    public String getFormated() {
        return getShortVersion() + "." + subVersion;
    }

    @Deprecated
    public static boolean isPaper() {
        return isPaperBranch();
    }

    public static boolean isSpigot() {
        return !getPlatform().equals(MinecraftPlatform.craftbukkit);
    }

    public static boolean isFolia() {
        return getPlatform().equals(MinecraftPlatform.folia) || getPlatform().equals(MinecraftPlatform.canvas);
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

        if (version.contains("canvas")) {
            platform = MinecraftPlatform.canvas;
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

    private static Version cycleAll(String looking) {
        for (Version version : values()) {
            if (version.isLegacy())
                continue;

            if (version.getCleanVersion().equalsIgnoreCase(looking)) {
                return version;
            }
        }

        if (!looking.endsWith(".0") && looking.split(".").length < 3) {
            return cycleAll(looking + ".0");
        }

        return null;

    }

    private static Version getLegacyEqual(Version v) {

        for (Version version : values()) {
            if (!version.isLegacy())
                continue;

            if (version.getMajorVersion() != v.getMajorVersion())
                continue;

            if (version.getMinorVersion() != v.getMinorVersion())
                continue;

            if (!version.getPatchVersions().contains(v.getPatchVersion()))
                continue;

            version.patchVersion = v.getPatchVersion();

            return version;
        }

        return null;
    }

    private static Version tryToFindVersion() {
        try {
            @NotNull
            String bukkitVersion = Bukkit.getBukkitVersion();

            if (bukkitVersion.contains("-")) {
                bukkitVersion = bukkitVersion.substring(0, bukkitVersion.indexOf("-"));
            }

            Version found = cycleAll(bukkitVersion);

            if (found != null)
                return found;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            @NotNull
            String bukkitVersion = Bukkit.getVersion();

            if (bukkitVersion.contains("MC: ")) {
                bukkitVersion = bukkitVersion.substring(bukkitVersion.indexOf("MC: "), bukkitVersion.length() - 1);
            }

            Version found = cycleAll(bukkitVersion);

            if (found != null)
                return found;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            @NotNull
            String bukkitVersion = Bukkit.getMinecraftVersion();
            Version found = cycleAll(bukkitVersion);
            if (found != null)
                return found;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            @NotNull
            String bukkitVersion = Bukkit.getVersionMessage();

            if (bukkitVersion.contains("Paper version ")) {
                bukkitVersion = bukkitVersion.substring(bukkitVersion.indexOf("Paper version "), bukkitVersion.indexOf("-"));
            }

            Version found = cycleAll(bukkitVersion);

            if (found != null)
                return found;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void updateFallBack(String bukkitVersion) {
        if (!bukkitVersion.endsWith(".0") && bukkitVersion.split(".").length < 3) {
            bukkitVersion = bukkitVersion + ".0";
        }

        String majorValue = "1";
        String minorValue = "0";
        String patchValue = "0";

        if (bukkitVersion.contains(".")) {
            String[] split = bukkitVersion.split("\\.");
            majorValue = split[0];
            if (split.length > 1)
                minorValue = split[1];
            if (split.length > 2)
                patchValue = split[2];
        }

        v99_99_99.majorVersion = Integer.parseInt(majorValue);
        v99_99_99.minorVersion = Integer.parseInt(minorValue);
        v99_99_99.patchVersion = Integer.parseInt(patchValue);
        v99_99_99.value = (v99_99_99.getMajorVersion() * 10000) + (v99_99_99.getMinorVersion() * 100) + v99_99_99.patchVersion;
    }

    private static Version fallBackVersion() {
        try {
            @NotNull
            String bukkitVersion = Bukkit.getBukkitVersion();
            if (bukkitVersion.contains("-")) {
                bukkitVersion = bukkitVersion.substring(0, bukkitVersion.indexOf("-"));
            }
            updateFallBack(bukkitVersion);
            return v99_99_99;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            @NotNull
            String bukkitVersion = Bukkit.getVersion();

            if (bukkitVersion.contains("MC: ")) {
                bukkitVersion = bukkitVersion.substring(bukkitVersion.indexOf("MC: "), bukkitVersion.length() - 1);
            }
            updateFallBack(bukkitVersion);
            return v99_99_99;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            @NotNull
            String bukkitVersion = Bukkit.getMinecraftVersion();
            updateFallBack(bukkitVersion);
            return v99_99_99;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            @NotNull
            String bukkitVersion = Bukkit.getVersionMessage();
            if (bukkitVersion.contains("Paper version ")) {
                bukkitVersion = bukkitVersion.substring(bukkitVersion.indexOf("Paper version "), bukkitVersion.indexOf("-"));
            }
            updateFallBack(bukkitVersion);
            return v99_99_99;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return v99_99_99;
    }

    public static Version getCurrent() {
        if (current != null)
            return current;
//        Paper returns examples as of 1.20
//        Bukkit.getServer().getClass().getPackage().getName();   org.bukkit.craftbukkit
//        Bukkit.getBukkitVersion();                              1.21-R0.1-SNAPSHOT
//        Bukkit.getMinecraftVersion();                           1.21
//        Bukkit.getVersion();                                    1.21-4-090775e (MC: 1.21)
//        Bukkit.getVersionMessage();                             This server is running Paper version 1.21-4-master@090775e (2024-06-18T13:42:35Z) (Implementing API version 1.21-R0.1-SNAPSHOT)

        Version tryVersion = tryToFindVersion();

        // This can be removed in future updates when all associated plugins are updated
        // to use getRevisionVersionName() method instead of name()
        if (tryVersion != null && !tryVersion.isEqualOrHigher(Version.v26_1_0)) {
            Version legacyVersion = getLegacyEqual(tryVersion);
            if (legacyVersion != null) {
                tryVersion = legacyVersion;
            }
        }

        if (tryVersion != null) {
            current = tryVersion;
            subVersion = tryVersion.getPatchVersion();
            CMIMessages
                    .consoleMessage("&eServer version detection by new method: &6" + tryVersion + " &eLegacy: &6" + tryVersion.isLegacy() + " &eMojang Mappings: &6" + isMojangMappings());
            return current;
        } else {
            fallBackVersion();
            current = v99_99_99;
            subVersion = current.getPatchVersion();
            CMIMessages.consoleMessage("&eCould not determine exact version, using custom: &6" + v99_99_99.getCleanVersion() + " &eLegacy: &6" + v99_99_99.isLegacy());
            return current;
        }
    }

    public boolean isLower(Version version) {
        return getVersionValue() < version.getVersionValue();
    }

    public boolean isHigher(Version version) {
        return getVersionValue() > version.getVersionValue();
    }

    public boolean isEqualOrLower(Version version) {
        return getVersionValue() <= version.getVersionValue();
    }

    public boolean isEqualOrHigher(Version version) {
        return getVersionValue() >= version.getVersionValue();
    }

    public static boolean isCurrentEqualOrHigher(Version v) {
        return getCurrent().getVersionValue() >= v.getVersionValue();
    }

    public static boolean isCurrentHigher(Version v) {
        return getCurrent().getVersionValue() > v.getVersionValue();
    }

    public static boolean isCurrentLower(Version v) {
        return getCurrent().getVersionValue() < v.getVersionValue();
    }

    public static boolean isCurrentEqualOrLower(Version v) {
        return getCurrent().getVersionValue() <= v.getVersionValue();
    }

    public static boolean isCurrentEqual(Version v) {
        return getCurrent().getVersionValue() == v.getVersionValue();
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

    @Deprecated
    public List<String> getMinorVersions() {

        if (minorVersions == null)
            return new ArrayList<String>();

        return Arrays.stream(minorVersions)
                .mapToObj(version -> getSimplifiedVersion() + version)
                .collect(Collectors.toList());
    }

    public List<Integer> getPatchVersions() {

        if (minorVersions == null)
            return new ArrayList<Integer>();

        return Arrays.stream(minorVersions).mapToObj(version -> version).collect(Collectors.toList());
    }

    public boolean isLegacy() {
        return legacy;
    }

    public int getPatchVersion() {
        return patchVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }
}
