package net.Zrips.CMILib.Container;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Utility;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Version.Version;

public class CMILocation extends Location {

    private static final String separator = ";";

    public CMILocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.worldName = world.getName();
    }

    public CMILocation(World world, double x, double y, double z) {
        super(world, x, y, z);
        if (world != null)
            this.worldName = world.getName();
    }

    private String worldName;

    public CMILocation(Location loc) {
        super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        if (loc.getWorld() != null)
            this.worldName = loc.getWorld().getName();
    }

    public CMILocation(String world, double x, double y, double z, float yaw, float pitch) {
        super(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        this.worldName = world;
    }

    public CMILocation(String world, double x, double y, double z) {
        super(Bukkit.getWorld(world), x, y, z);
        this.worldName = world;
    }

    public void recheck() {
        updateWorld();
    }

    private void updateWorld() {

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_16_R1) && super.getWorld() != null && !super.isWorldLoaded())
                return;
        } catch (Throwable e) {
        }

        try {
            if (super.getWorld() == null && worldName != null) {
                World w = null;
                if (worldName.length() == 36) {
                    w = Bukkit.getWorld(UUID.fromString(worldName));
                } else {
                    w = Bukkit.getWorld(worldName);
                }
                if (w != null) {
                    super.setWorld(w);
                    this.worldName = super.getWorld().getName();
                }
            }
        } catch (Exception e) {
//	    e.printStackTrace();
        }
    }

    public String getWorldName() {
        return this.worldName != null ? this.worldName : this.getWorld() == null ? null : this.getWorld().getName();
    }

    public Location getBukkitLoc() {
        updateWorld();
        return this;
    }

    public boolean isValid() {
        updateWorld();
        return this.getWorld() != null && this.getWorld().getName() != null && Bukkit.getWorld(this.getWorld().getUID()) != null;
    }

    public boolean isWorldNull() {
        updateWorld();
        return this.getWorld() != null && this.getWorld().getName() != null && Bukkit.getWorld(this.getWorld().getUID()) != null;
    }

    @Override
    public World getWorld() {
        updateWorld();
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_16_R1) && !super.isWorldLoaded())
                return null;
        } catch (Throwable e) {
        }
        return super.getWorld();
    }

    @Override
    public Chunk getChunk() {
        updateWorld();
        return super.getChunk();
    }

    @Override
    public Block getBlock() {
        updateWorld();
        if (super.getWorld() == null)
            return null;
        return super.getBlock();
    }

    @Override
    public Location add(Location vec) {
        updateWorld();
        return super.add(vec);
    }

    @Override
    public Location subtract(Location vec) {
        updateWorld();
        return super.subtract(vec);
    }

    @Override
    public double distanceSquared(Location o) {
        if (o == null)
            return Integer.MAX_VALUE;
        if (!this.isValid())
            return Integer.MAX_VALUE;
        return super.distanceSquared(o);
    }

    @Override
    public double distance(Location o) {
        if (o == null)
            return Integer.MAX_VALUE;
        if (!this.isValid())
            return Integer.MAX_VALUE;
        return super.distance(o);
    }

    @Override
    public boolean equals(Object obj) {
        updateWorld();
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        updateWorld();
        return super.hashCode();
    }

    @Override
    public String toString() {
        updateWorld();
        return super.toString();
    }

    @Override
    public CMILocation clone() {
        updateWorld();
        return new CMILocation(super.clone());
    }

    @Override
    @Utility
    public Map<String, Object> serialize() {
        updateWorld();
        return super.serialize();
    }

    public int getHighestBlockYAt() {
//	Location loc = this.getBukkitLoc();
//	if (loc == null)
//	    return 0;
//	return this.getWorld().getHighestBlockYAt(this);

        if (this.getWorld() == null)
            return 63;

        ChunkSnapshot chunk = this.getWorld().getEmptyChunkSnapshot(this.getBlockX() >> 4, this.getBlockZ() >> 4, true, true);

        int x = this.getBlockX() % 16;
        x = x < 0 ? 16 + x : x;
        int z = this.getBlockZ() % 16;
        z = z < 0 ? 16 + z : z;

        return chunk.getHighestBlockYAt(x, z);

//	ChunkSnapshot snap = loc.getChunk().getChunkSnapshot(true, false, false);
//
//	int x = this.getBlockX() - (snap.getX() * 16);
//	int z = this.getBlockZ() - (snap.getZ() * 16);
//	
//	x = x < 0 ? 0 : x > 15 ? 15 : x;
//	z = z < 0 ? 0 : z > 15 ? 15 : z;
//	
//	return snap.getHighestBlockYAt(x, z);
    }

    public Material getBlockType() {
        Location loc = this.getBukkitLoc();
        if (loc == null)
            return Material.AIR;

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_16_R1) && !super.isWorldLoaded())
                return Material.AIR;
        } catch (Throwable e) {
        }

//	if (Version.isCurrentEqualOrLower(Version.v1_13_R2))
        return this.getBlock().getType();

//	ChunkSnapshot snap = loc.getChunk().getChunkSnapshot(false, false, false);
//
//	int x = this.getBlockX() - (snap.getX() * 16);
//	int z = this.getBlockZ() - (snap.getZ() * 16);
//
//	x = x < 0 ? 0 : x > 15 ? 15 : x;
//	z = z < 0 ? 0 : z > 15 ? 15 : z;
//	
//	return snap.getBlockType(x, loc.getBlockY(), z);
    }

    public CMIMaterial getBlockCMIType() {
        Location loc = this.getBukkitLoc();
        if (loc == null)
            return CMIMaterial.AIR;

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_16_R1) && !super.isWorldLoaded())
                return CMIMaterial.AIR;
        } catch (Throwable e) {
        }

        CMIMaterial mat = CMIMaterial.get(this.getBlock());
        return mat == CMIMaterial.NONE ? CMIMaterial.AIR : mat;
    }

    private static MethodHandle getBlockTypeId = null;
    private static MethodHandle getBlockData = null;

    public static Material getBlockTypeSafe(Location loc) {
        if (loc == null)
            return Material.AIR;

        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int y = loc.getBlockY();

        int cx = Math.abs(loc.getBlockX() % 16);
        int cz = Math.abs(loc.getBlockZ() % 16);
        World world = loc.getWorld();

        ChunkSnapshot chunkSnapshot = null;

        if (!world.getBlockAt(x, 0, z).getChunk().isLoaded()) {
            world.getBlockAt(x, 0, z).getChunk().load();
            chunkSnapshot = world.getBlockAt(x, 0, z).getChunk().getChunkSnapshot(false, false, false);
            world.getBlockAt(x, 0, z).getChunk().unload();
        } else {
            chunkSnapshot = world.getBlockAt(x, 0, z).getChunk().getChunkSnapshot();
        }

        if (chunkSnapshot == null)
            return Material.AIR;

        if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
            BlockData type = chunkSnapshot.getBlockData(cx, y, cz);
            return type.getMaterial();
        }

        if (getBlockTypeId == null) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            try {
                getBlockTypeId = lookup.findVirtual(ChunkSnapshot.class, "getBlockTypeId", MethodType.methodType(int.class, int.class, int.class)).asType(MethodType.methodType(int.class,
                    ChunkSnapshot.class));
                getBlockData = lookup.findVirtual(ChunkSnapshot.class, "getBlockData", MethodType.methodType(int.class, int.class, int.class)).asType(MethodType.methodType(int.class,
                    ChunkSnapshot.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (getBlockTypeId == null || getBlockData == null) {
            return Material.AIR;
        }
        try {
            int type = (int) getBlockTypeId.invokeExact(chunkSnapshot);
            if (type == 0) {
                return Material.AIR;
            }
            int data = (int) getBlockData.invokeExact(chunkSnapshot);
            return CMIMaterial.get(type, data).getMaterial();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Material.AIR;
    }

    public static CMILocation fromString(String map) {
        return fromString(map, map.contains(separator) ? separator : ":");
    }

    public static CMILocation fromString(String map, String separator) {
        CMILocation loc = null;
        if (map == null)
            return null;
        if (!map.contains(separator))
            return null;

        // replace , with . for the systems using different numerical format
        String[] split = map.replace(",", ".").split(separator);

        Double x = null;
        Double y = null;
        Double z = null;
        Float yaw = null;
        Float pitch = null;
        World world = null;
        String worldName = null;

        for (String one : split) {

            try {
                if (x == null) {
                    x = Double.parseDouble(one);
                    continue;
                }

                if (y == null) {
                    y = Double.parseDouble(one);
                    continue;
                }

                if (z == null) {
                    z = Double.parseDouble(one);
                    continue;
                }

                if (yaw == null) {
                    yaw = Float.parseFloat(one);
                    continue;
                }

                if (pitch == null) {
                    pitch = Float.parseFloat(one);
                    continue;
                }
            } catch (Exception e) {
            }

            worldName = one;

            if (world == null) {
                world = CMIWorld.getWorld(one);
            }
        }

        worldName = world == null ? worldName : world.getName();
        if (worldName == null)
            return null;

        loc = new CMILocation(worldName,
            x == null ? 0 : x,
            y == null ? 0 : y,
            z == null ? 0 : z);
        loc.setYaw(yaw == null ? 0 : yaw);
        loc.setPitch(pitch == null ? 0 : pitch);

        return loc;
    }

    public static List<CMILocation> fromString(List<String> map) {
        return fromString(map, separator);
    }

    public static List<CMILocation> fromString(List<String> maps, String separator) {
        List<CMILocation> ls = new ArrayList<CMILocation>();
        for (String map : maps) {
            ls.add(fromString(map, separator));
        }
        return ls;
    }

    public static String toString(Location loc) {
        return toString(loc, separator, false, false);
    }

    public static String toString(Location loc, boolean noPitchYaw) {
        return toString(loc, separator, noPitchYaw, false);
    }

    public static String toString(Location loc, String separator) {
        return toString(loc, separator, false, false);
    }

    public static String toString(Location loc, String separator, boolean noPitchYaw) {
        return toString(loc, separator, noPitchYaw, false);
    }

    public static String toString(Location loc, String separator, boolean noPitchYaw, boolean integers) {
        return new CMILocation(loc).toString(separator, noPitchYaw, integers);
    }

    public static String toString(CMILocation loc) {
        return toString(loc, separator, false, false);
    }

    public static String toString(CMILocation loc, boolean noPitchYaw) {
        return toString(loc, separator, noPitchYaw, false);
    }

    public static String toString(CMILocation loc, String separator) {
        return toString(loc, separator, false, false);
    }

    public static String toString(CMILocation loc, String separator, boolean noPitchYaw) {
        return toString(loc, separator, noPitchYaw, false);
    }

    public static String toString(CMILocation loc, String separator, boolean noPitchYaw, boolean integers) {
        return loc.toString(separator, noPitchYaw, integers);
    }

    public String toString(String separator) {
        return toString(separator, false, false);
    }

    public String toString(boolean noPitchYaw) {
        return toString(separator, noPitchYaw, false);
    }

    public String toString(String separator, boolean noPitchYaw) {
        return toString(separator, noPitchYaw, false);
    }

    public String toString(String separator, boolean noPitchYaw, boolean integers) {
        StringBuilder map = new StringBuilder();
        try {
            if (this.getWorldName() != null) {
                map.append(this.getWorldName());
                if (integers) {
                    map.append(separator + getBlockX());
                    map.append(separator + getBlockY());
                    map.append(separator + getBlockZ());
                } else {
                    map.append(separator + fNumber(getX()));
                    map.append(separator + (long) (getY() * 100) / 100D);
                    map.append(separator + fNumber(getZ()));
                }
                if (!noPitchYaw) {
                    if (integers) {
                        map.append(separator + (long) getYaw());
                        map.append(separator + (long) getPitch());
                    } else {
                        map.append(separator + (long) (getYaw() * 100) / 100D);
                        map.append(separator + (long) (getPitch() * 100) / 100D);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // replace , with . for the systems using different numerical format
        return map.toString().replace(",", ".");
    }

    public static List<String> toString(List<CMILocation> loc) {
        return toString(loc, separator);
    }

    public static List<String> toString(List<CMILocation> locations, String separator) {
        List<String> ls = new ArrayList<String>();
        for (Location loc : locations) {
            ls.add(toString(loc, separator));
        }
        return ls;
    }

    private static Double fNumber(Double amount) {
        return (long) (amount * 100) / 100D;
    }

    public static World getWorld(String name) {
        return CMIWorld.getWorld(name);
    }

    public static int getMinHeight(World world) {
        if (Version.isCurrentEqualOrLower(Version.v1_16_R3))
            return 0;
        return world.getMinHeight();
    }

    public static double getDistance(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null || loc1.getWorld() != loc2.getWorld())
            return Integer.MAX_VALUE;

        try {
            return loc1.distance(loc2);
        } catch (Throwable e) {
            return Integer.MAX_VALUE;
        }
    }
}
