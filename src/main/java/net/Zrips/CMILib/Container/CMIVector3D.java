package net.Zrips.CMILib.Container;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class CMIVector3D extends CMIVector2D {
    private double z = 0D;

    public CMIVector3D() {
        super(0, 0);
    }

    public CMIVector3D(Vector v) {
        super(v.getX(), v.getY());
        this.z = v.getZ();
    }

    public CMIVector3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public CMIVector3D add(double x, double y, double z) {
        return this.plus(x, y, z);
    }

    public CMIVector3D plus(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public CMIVector3D add(CMIVector3D v) {
        return this.plus(v.x, v.y, v.z);
    }

    public CMIVector3D plus(CMIVector3D v) {
        return this.plus(v.x, v.y, v.z);
    }

    public CMIVector3D minus(CMIVector3D v) {
        return this.minus(v.getX(), v.getY(), v.getZ());
    }

    @Override
    public CMIVector3D minus(Vector v) {
        return this.minus(v.getX(), v.getY(), v.getZ());
    }

    public CMIVector3D minus(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public double dot(CMIVector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public CMIVector3D cross(CMIVector3D other) {
        
        double nx = this.y * other.z - this.z * other.y;
        double ny = this.z * other.x - this.x * other.z;
        double nz = this.x * other.y - this.y * other.x;

        this.x = nx;
        this.y = ny;
        this.z = nz;
        
        return this;
    }

    public CMIVector3D multiply(double scalar) {
        return this.multiply(scalar, scalar, scalar);
    }

    public CMIVector3D multiply(CMIVector3D scale) {
        return this.multiply(scale.getX(), scale.getY(), scale.getZ());
    }

    public CMIVector3D multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public CMIVector3D normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    @Override
    public String toString() {
        return String.format("%.2f;%.2f;%.2f", x, y, z);
    }

    public static CMIVector3D fromString(String value) {
        String[] parts = value.split(";");
        try {
            return new CMIVector3D(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
        } catch (Exception e) {
            e.printStackTrace();
            return new CMIVector3D(0, 0, 0);
        }
    }

    public double getZ() {
        return z;
    }

    public CMIVector3D setZ(double z) {
        this.z = z;
        return this;
    }

    public Location toLocation(World world) {
        return new Location(world, this.getX(), this.getY(), this.getZ());
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    @Override
    public CMIVector3D clone() {
        return new CMIVector3D(x, y, z);
    }

}
