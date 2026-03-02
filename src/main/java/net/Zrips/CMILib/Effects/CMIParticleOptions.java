package net.Zrips.CMILib.Effects;

import org.bukkit.Color;

import net.Zrips.CMILib.Container.CMILocation;
import net.Zrips.CMILib.Container.CMIVector3D;
import net.Zrips.CMILib.Items.CMIMaterial;

public class CMIParticleOptions {

    public static CMIParticleOptions buildEmpty() {
        return new CMIParticleOptions();
    }

    public static CMIParticleOptions buildColor(Color color) {
        return new CMIParticleColor(color);
    }

    public static CMIParticleOptions buildDustOptions(Color color, float value) {
        return new CMIParticleDustOptions(color, value);
    }

    public static CMIParticleOptions buildDustTransition(Color colorFrom, Color colorTo, float value) {
        return new CMIParticleDustTransition(colorFrom, colorTo, value);
    }

    public static CMIParticleOptions buildFloat(float value) {
        return new CMIParticleFloat(value);
    }

    public static CMIParticleOptions buildInteger(int value) {
        return new CMIParticleInteger(value);
    }

    public static CMIParticleOptions buildBlockData(CMIMaterial material) {
        return new CMIParticleBlockData(material);
    }

    public static CMIParticleOptions buildItemStack(CMIMaterial material) {
        return new CMIParticleItemStack(material);
    }

    public static CMIParticleOptions buildTrail(CMIVector3D location, Color color, int value) {
        return new CMIParticleTrail(location, color, value);
    }

    public static CMIParticleOptions buildSpell(Color color, float value) {
        return new CMIParticleSpell(color, value);
    }

    public static CMIParticleOptions buildVibration(CMIVector3D origin, CMIVector3D destination, int value) {
        return new CMIParticleVibration(origin, destination, value);
    }

}
