package net.Zrips.CMILib.Effects;

public class CMIParticleFloat extends CMIParticleOptions {

    private float value = 0f;

    public CMIParticleFloat(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
