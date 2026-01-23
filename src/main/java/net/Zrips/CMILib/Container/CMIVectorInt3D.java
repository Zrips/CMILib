package net.Zrips.CMILib.Container;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CMIVectorInt3D extends CMIVectorInt2D {

    private int y = 0;

    public CMIVectorInt3D(int x, int y, int z) {
        super(x, z);
        this.y = y;
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

    public CMIVectorInt3D add(CMIVectorInt3D v) {
        return plus(v);
    }

    public CMIVectorInt3D plus(CMIVectorInt3D v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public CMIVectorInt3D minus(CMIVectorInt3D v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    @Override
    public CMIVectorInt3D times(int s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        return this;
    }

    public double dot(CMIVectorInt3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return getX() + ";" + getY() + ";" + getZ();
    }

    @Override
    public CMIVectorInt3D clone() {
        return new CMIVectorInt3D(x, y, z);
    }
}
