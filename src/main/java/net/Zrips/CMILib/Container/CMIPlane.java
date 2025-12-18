package net.Zrips.CMILib.Container;

import org.bukkit.util.Vector;

public class CMIPlane {
    private Vector center;
    private CMIVector2D sizeMin;
    private CMIVector2D sizeMax;

    public CMIPlane(Vector center, CMIVector2D sizeMin, CMIVector2D sizeMax) {
        this.center = center;
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
    }

    public Vector getCenter() {
        return center;
    }

    public void setCenter(Vector center) {
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
