package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Version.Version;

public class CMIWorld {

    public static void onDisable() {
        worldNames.clear();
    }

    static HashMap<String, String> worldNames = new HashMap<String, String>();

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

    public static World getWorld(String name) {
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
}
