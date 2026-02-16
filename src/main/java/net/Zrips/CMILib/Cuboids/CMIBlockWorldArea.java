package net.Zrips.CMILib.Cuboids;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.Zrips.CMILib.Container.CMILocation;
import net.Zrips.CMILib.Container.CMIVectorInt3D;
import net.Zrips.CMILib.Container.CMIWorld;

public class CMIBlockWorldArea extends CMIBlockArea {

    private CMIWorld world = new CMIWorld();

    public CMIBlockWorldArea() {
    }

    public CMIBlockWorldArea(Location startLoc, Location endLoc) {
        this(startLoc.getWorld(), startLoc.toVector(), endLoc.toVector());
    }

    public CMIBlockWorldArea(CMILocation startLoc, CMILocation endLoc) {
        this(startLoc.getWorld(), startLoc.toVector(), endLoc.toVector());
        this.world = new CMIWorld(startLoc);
    }

    public CMIBlockWorldArea(World world, Vector startLoc, Vector endLoc) {
        super(startLoc, endLoc);
        this.world = new CMIWorld(world);
    }

    public void setWorld(@NotNull World world) {
        this.world = world == null ? new CMIWorld() : new CMIWorld(world);
    }

    public @NotNull CMIWorld getWorld() {
        return this.world;
    }

    @Override
    public CMIBlockWorldArea clone() {
        CMIBlockWorldArea clone = new CMIBlockWorldArea();
        clone.p1 = p1 == null ? null : p1.clone();
        clone.p2 = p2 == null ? null : p2.clone();
        clone.highPoint = highPoint == null ? null : highPoint.clone();
        clone.lowPoint = lowPoint == null ? null : lowPoint.clone();
        clone.world = world;
        return clone;
    }

    public boolean contract(CMIActionDirection d, int amount) {
        return super.contract(d, getWorld(), amount);
    }

    public boolean expand(CMIActionDirection d, int amount) {
        return super.expand(d, getWorld(), amount);
    }

    public boolean shift(CMIActionDirection d, int amount) {
        return super.shift(d, getWorld(), amount);
    }

    @Override
    public boolean containsLoc(Location loc) {
        return containsLoc(loc, 0);
    }

    @Override
    public boolean containsLoc(Location loc, int extraRange) {
        if (loc == null)
            return false;

        if (loc.getWorld() == null)
            return false;

        if (!loc.getWorld().equals(getWorld()))
            return false;

        return super.containsLoc(loc, extraRange);
    }

    public boolean checkCollision(CMIBlockWorldArea area) {
        if (area.getWorld() == null || this.getWorld() == null)
            return false;

        if (!area.getWorld().equals(this.getWorld())) {
            return false;
        }

        return super.checkCollision(area);
    }

    @Override
    public void setLowLocation(Location lowLocation) {
        if (lowLocation == null)
            return;
        setWorld(lowLocation.getWorld());
        super.setLowLocation(lowLocation);
    }

    @Override
    public void setHighLocation(Location highLocation) {
        if (highLocation == null)
            return;
        setWorld(highLocation.getWorld());
        super.setHighLocation(highLocation);
    }

    @Override
    public @Nullable String toString() {
        if (!this.valid())
            return null;

        StringBuilder sb = new StringBuilder(32);
        sb.append(getWorld().getWorldName()).append(';')
                .append(this.lowPoint.getX()).append(';')
                .append(this.lowPoint.getY()).append(';')
                .append(this.lowPoint.getZ()).append(';')
                .append(this.highPoint.getX()).append(';')
                .append(this.highPoint.getY()).append(';')
                .append(this.highPoint.getZ());
        return sb.toString();
    }

    public static @Nullable CMIBlockWorldArea fromString(String value) {
        String[] parts = value.split(";");
        try {

            String worldName = parts[0];

            CMIVectorInt3D lowPoint = new CMIVectorInt3D(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            CMIVectorInt3D highPoint = new CMIVectorInt3D(Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
            CMIBlockWorldArea cuboid = new CMIBlockWorldArea();
            cuboid.setLowPoint(lowPoint);
            cuboid.setHighPoint(highPoint);
            cuboid.world = new CMIWorld(worldName);

            return cuboid;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
