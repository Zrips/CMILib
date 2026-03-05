package net.Zrips.CMILib.Effects;

import org.bukkit.Color;

public class CMIParticleSpell extends CMIParticleColor {

    private float value = 0f;

    public CMIParticleSpell(Color color, float power) {
        super(color);
        this.value = power;
    }

    public float getPower() {
        return value;
    }

    public void setPower(float power) {
        this.value = power;
    }
}
