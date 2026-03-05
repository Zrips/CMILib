package net.Zrips.CMILib.Effects;

import org.bukkit.Color;

public class CMIParticleDustTransition extends CMIParticleDustOptions {

    private Color colorTo = Color.fromRGB(50, 0, 200);

    public CMIParticleDustTransition(Color colorFrom, Color colorTo, float size) {
        super(colorFrom, size);
        setColorTo(colorTo);
    }

    public Color getColorTo() {
        return colorTo;
    }

    public void setColorTo(Color colorTo) {
        this.colorTo = colorTo == null ? this.colorTo : colorTo;
    }
}
