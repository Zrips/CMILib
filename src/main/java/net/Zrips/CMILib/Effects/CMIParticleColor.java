package net.Zrips.CMILib.Effects;

import org.bukkit.Color;

public class CMIParticleColor extends CMIParticleOptions {

    private Color color = Color.fromRGB(200, 0, 50);

    public CMIParticleColor(Color color) {
        setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color == null ? this.color : color;
    }
}
