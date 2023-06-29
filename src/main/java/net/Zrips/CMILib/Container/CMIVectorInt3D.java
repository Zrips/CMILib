package net.Zrips.CMILib.Container;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CMIVectorInt3D {

    private int x = 0;
    private int y = 0;
    private int z = 0;

    public CMIVectorInt3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CMIVectorInt3D(Vector vector) {
        this(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public CMIVectorInt3D(Location loc) {
        this(loc.toVector());
    }

    public CMIVectorInt3D(CMILocation loc) {
        this(loc.toVector());
    }

    public CMIVectorInt3D plus(CMIVectorInt3D v) {
        return new CMIVectorInt3D(x + v.x, y + v.y, z + v.z);
    }

    public CMIVectorInt3D minus(CMIVectorInt3D v) {
        return new CMIVectorInt3D(x - v.x, y - v.y, z - v.z);
    }

    public CMIVectorInt3D times(int s) {
        return new CMIVectorInt3D(s * x, s * y, s * z);
    }

    public double dot(CMIVectorInt3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

}
