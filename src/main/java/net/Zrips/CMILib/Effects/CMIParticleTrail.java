package net.Zrips.CMILib.Effects;

import org.bukkit.Color;

import net.Zrips.CMILib.Container.CMIVector3D;

public class CMIParticleTrail extends CMIParticleLocation {

    private int value = 20;
    private Color color = Color.fromRGB(200, 0, 50);

    public CMIParticleTrail(CMIVector3D target, Color color, int duration) {
        super(target);
        this.value = duration;
        setColor(color);
    }

    public int getDuration() {
        return value;
    }

    public void setDuration(int duration) {
        this.value = duration;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color == null ? this.color : color;
    }
}
