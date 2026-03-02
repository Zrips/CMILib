package net.Zrips.CMILib.Effects;

import net.Zrips.CMILib.Container.CMIVector3D;

public class CMIParticleVibration extends CMIParticleLocation {

    private int value = 0;
    private CMIVector3D destination = new CMIVector3D(0, 0, 0);

    public CMIParticleVibration(CMIVector3D origin, CMIVector3D destination, int arrivalTime) {
        super(origin);
        this.value = arrivalTime;
        setDestination(destination);
    }

    public int getArrivalTime() {
        return value;
    }

    public void setArrivalTime(int value) {
        this.value = value;
    }

    public CMIVector3D getDestination() {
        return destination;
    }

    public void setDestination(CMIVector3D destination) {
        this.destination = destination == null ? this.destination : destination;
    }
}
