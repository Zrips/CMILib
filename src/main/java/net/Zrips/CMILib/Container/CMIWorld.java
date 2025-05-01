package net.Zrips.CMILib.Container;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Schedulers.CMITask;

public class CMIWorld {

    public static void onDisable() {
        worldNames.clear();
    }

    static HashMap<String, String> worldNames = new HashMap<String, String>();
    static HashMap<UUID, String> worldNamesByUUID = new HashMap<UUID, String>();

    public static String getWorldNameFormatted(World world) {
        if (world == null || world.getName() == null)
            return "Unknown";

        String name = worldNames.get(world.getName());
        return name != null ? name : world.getName();
    }

    public static void initialize() {
        List<String> worlds = new ArrayList<String>();
        for (World one : Bukkit.getWorlds()) {
            worlds.add(one.getName() + "-&2" + CMIText.everyFirstToUpperCase(one.getName().replace("_", " ")));
        }
        ConfigReader locale = CMILib.getInstance().getConfigManager().getLocaleConfig();

        worlds = locale.get("Location.WorldNames", worlds);
        worldNames.clear();

        for (String one : worlds) {
            if (one.endsWith(" "))
                one = one.substring(0, one.length() - 1);

            String[] wsplit = one.split("-", 2);

            if (wsplit.length == 1)
                continue;
            worldNames.put(wsplit[0], wsplit[1]);
        }
        if (worldNames.isEmpty()) {
            worldNames.put("--NothingFound--", "--NothingFound--");
        }
        initWorldNames();

    }

    private static void initWorldNames() {
        if (!Version.isCurrentEqualOrHigher(Version.v1_16_R1))
            return;

        // Loading should happen before checking actual existing worlds
        loadWorldNames();

        try {
            for (World one : Bukkit.getWorlds()) {
                addWorldName(one.getUID(), one.getName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private static void loadWorldNames() {
        ConfigReader cfg = null;

        try {
            cfg = new ConfigReader(CMILib.getInstance(), CMILib.savesFolderName + File.separator + "worldNames.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cfg == null)
            return;
        @NotNull
        Set<String> keys = cfg.getC().getKeys(false);

        for (String one : keys) {

            UUID uuid = null;
            try {
                uuid = UUID.fromString(one);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if (uuid == null)
                continue;

            String name = cfg.getC().getString(one);
            if (name == null)
                continue;

            worldNamesByUUID.put(uuid, name);
        }
    }

    public static void cacheWorld(World world) {
        if (world == null)
            return;

        if (!Version.isCurrentEqualOrHigher(Version.v1_16_R1))
            return;

        addWorldName(world.getUID(), world.getName());
    }

    private static void addWorldName(UUID uuid, String name) {

        String old = worldNamesByUUID.get(uuid);
        if (old == null || !old.equals(name)) {
            saveWorldNames();
        }

        worldNamesByUUID.put(uuid, name);
    }

    static CMITask saveTask = null;

    private static void saveWorldNames() {

        if (saveTask != null)
            return;

        saveTask = CMIScheduler.runLaterAsync(CMILib.getInstance(), () -> {

            ConfigReader cfg = null;

            try {
                cfg = new ConfigReader(CMILib.getInstance(), CMILib.savesFolderName + File.separator + "worldNames.yml");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (cfg == null)
                return;

            for (Entry<UUID, String> one : worldNamesByUUID.entrySet()) {
                UUID uuid = one.getKey();
                String name = one.getValue();
                cfg.set(uuid.toString(), name);
            }

            cfg.save();

            saveTask = null;
        }, 5 * 20L);
    }

    public static boolean insideWorldBorder(Location loc) {
        try {
            Location center = loc.getWorld().getWorldBorder().getCenter();
            double size = loc.getWorld().getWorldBorder().getSize() / 2D;
            double minX = center.getX() - size;
            double maxX = center.getX() + size;
            double minZ = center.getZ() - size;
            double maxZ = center.getZ() + size;
            return loc.getX() >= minX && loc.getX() < maxX && loc.getZ() >= minZ && loc.getZ() < maxZ;
        } catch (Throwable e) {
            e.printStackTrace();
            return true;
        }
    }

    public static int getMinHeight(World world) {
        if (Version.isCurrentEqualOrLower(Version.v1_16_R3))
            return 0;
        return world == null ? 0 : world.getMinHeight();
    }

    public static int getMaxHeight(World world) {
        if (world == null)
            return 256;

        if (world.getEnvironment() == null)
            return 256;

        try {
            switch (world.getEnvironment()) {
            case NETHER:
                return 128;
            case NORMAL:
            case THE_END:
                if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
                    return world.getMaxHeight();
                return 256;
            default:
                break;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 256;
    }

    public static @Nullable World getWorld(String name) {
        World w = Bukkit.getWorld(name);

        if (w != null)
            return w;
        String originalName = name;
        name = name.replaceAll("[_|.|-]", "");

        for (World one : Bukkit.getWorlds()) {
            String n = one.getName().replaceAll("[_|.|-]", "");
            if (n.equalsIgnoreCase(name))
                return one;
        }

        if (originalName.length() == 36) {
            try {
                return Bukkit.getWorld(UUID.fromString(originalName));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static @Nullable World getWorld(UUID uuid) {

        if (!Version.isCurrentEqualOrHigher(Version.v1_16_R1))
            return null;

        return Bukkit.getWorld(uuid);
    }

    public static @Nullable String getWorldName(UUID uuid) {

        if (!Version.isCurrentEqualOrHigher(Version.v1_16_R1))
            return null;

        String name = worldNamesByUUID.get(uuid);

        if (name != null)
            return name;

        World w = getWorld(uuid);

        if (w != null)
            return w.getName();

        return null;
    }
}
