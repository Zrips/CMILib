package net.Zrips.CMILib.Container;

import org.bukkit.util.Vector;

public class CMIPlane {
    private CMIVector3D center;
    private CMIVector2D sizeMin;
    private CMIVector2D sizeMax;

    @Deprecated
    public CMIPlane(Vector center, CMIVector2D sizeMin, CMIVector2D sizeMax) {
        this.center = new CMIVector3D(center);
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
    }

    @Deprecated
    public Vector getCenter() {
        return new Vector(center.getX(), center.getY(), center.getZ());
    }

    @Deprecated
    public void setCenter(Vector center) {
        this.center = new CMIVector3D(center);
    }

    public CMIVector3D getCenterPoint() {
        return center;
    }

    public void setCenterPoint(CMIVector3D center) {
        this.center = center;
    }

    public CMIVector2D getSizeMin() {
        return sizeMin;
    }

    public void setSizeMin(CMIVector2D sizeMin) {
        this.sizeMin = sizeMin;
    }

    public CMIVector2D getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(CMIVector2D sizeMax) {
        this.sizeMax = sizeMax;
    }

}
