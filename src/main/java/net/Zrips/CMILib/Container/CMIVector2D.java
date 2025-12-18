package net.Zrips.CMILib.Container;

import org.bukkit.util.Vector;

public class CMIVector2D {
    protected double x;
    protected double y;

    public CMIVector2D(Vector v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public CMIVector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public CMIVector2D plus(CMIVector2D v) {
        return new CMIVector2D(x + v.x, y + v.y);
    }

    public CMIVector2D minus(CMIVector2D v) {
        return new CMIVector2D(x - v.x, y - v.y);
    }

    public CMIVector2D minus(Vector v) {
        return new CMIVector2D(x - v.getX(), y - v.getY());
    }

    public CMIVector2D multiply(double s) {
        return new CMIVector2D(s * x, s * y);
    }

    public double dot(CMIVector2D v) {
        return x * v.x + y * v.y;
    }

    @Override
    public String toString() {
        return String.format("%.2f;%.2f", x, y);
    }

    public static CMIVector2D fromString(String value) {
        String[] parts = value.split(";");
        try {
            return new CMIVector2D(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
        } catch (Exception e) {
            e.printStackTrace();
            return new CMIVector2D(0, 0);
        }
    }

    public double getX() {
        return x;
    }

    public CMIVector2D setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public CMIVector2D setY(double y) {
        this.y = y;
        return this;
    }
}
