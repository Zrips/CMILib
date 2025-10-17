package net.Zrips.CMILib.Container;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CMIVectorInt2D {

    protected int x = 0;
    protected int z = 0;

    public CMIVectorInt2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public CMIVectorInt2D(Vector vector) {
        this(vector.getBlockX(), vector.getBlockZ());
    }

    public CMIVectorInt2D(Location loc) {
        this(loc.toVector());
    }

    public CMIVectorInt2D(CMILocation loc) {
        this(loc.toVector());
    }

    public CMIVectorInt2D plus(CMIVectorInt2D v) {
        return new CMIVectorInt2D(x + v.x, z + v.z);
    }

    public CMIVectorInt2D minus(CMIVectorInt2D v) {
        return new CMIVectorInt2D(x - v.x, z - v.z);
    }

    public CMIVectorInt2D times(int s) {
        return new CMIVectorInt2D(s * x, s * z);
    }

    public double dot(CMIVectorInt2D v) {
        return x * v.x + z * v.z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return getX() + ";" + getZ();
    }
}
