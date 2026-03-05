package net.Zrips.CMILib.Effects;

public class CMIParticleInteger extends CMIParticleOptions {

    private int value = 0;

    public CMIParticleInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
