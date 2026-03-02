package net.Zrips.CMILib.Effects;

import org.bukkit.Color;

import net.Zrips.CMILib.Container.CMINumber;

public class CMIParticleDustOptions extends CMIParticleColor {

    private float size = 1f;

    public CMIParticleDustOptions(Color color, float size) {
        super(color);
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float value) {
        this.size = CMINumber.clampValue(value, 0.1f, 4f);
    }
}
