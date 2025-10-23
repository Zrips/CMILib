package net.Zrips.CMILib.Effects;

import javax.annotation.Nullable;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticle;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticleDataType;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Version.Version;

public class CMIEffect {

    private CMIParticle particle;
    private Color colorFrom = Color.fromBGR(50, 0, 200);
    private Color colorTo = Color.fromBGR(0, 200, 0);
    private CMIMaterial material = null;
    private Vector offset = new Vector();
    private int size = 1;
    private int amount = 1;
    private float speed = 0;
    private int duration = 1;

    private Object particleParameters = null;

    public CMIEffect(CMIParticle particle) {
        this.particle = particle;
    }

    public CMIParticle getParticle() {
        if (particle == null)
            particle = CMIParticle.DUST;
        return particle;
    }

    public void setParticle(CMIParticle particle) {
        this.particle = particle;
        this.particleParameters = null;
    }

    @Deprecated
    public Color getColor() {
        return getColorFrom();
    }

    @Deprecated
    public void setColor(Color color) {
        setColorFrom(color);
    }

    public Color getColorFrom() {
        return colorFrom;
    }

    public void setColorFrom(Color color) {
        this.colorFrom = color;
    }

    public Color getColorTo() {
        return colorTo;
    }

    public void setColorTo(Color color) {
        this.colorTo = color;
    }

    public Vector getOffset() {
        return offset;
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public CMIMaterial getMaterial() {
        return material;
    }

    public void setMaterial(CMIMaterial material) {
        this.material = material;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static CMIEffect get(String name) {
        CMIEffect cmiEffect = null;
        if (name == null)
            return null;
        name = name.replace("_", "").toLowerCase();
        CMIMaterial mat = CMIMaterial.NONE;
        Color colorFrom = null;
        Color colorTo = null;
        int duration = 0;
        int size = 0;

        String sub = null;

        if (name.contains(":")) {
            sub = name.split(":", 2)[1];
            name = name.split(":", 2)[0];
        }

        CMIParticle cmiParticle = CMIParticle.get(name);

        if (cmiParticle == null)
            return null;

        if (sub != null) {
            for (String one : sub.split(":")) {

                if (cmiParticle.isColored() && colorFrom == null) {
                    colorFrom = processColor(one);
                    if (colorFrom != null)
                        continue;
                }

                if (cmiParticle.getDataType().equals(CMIParticleDataType.DustTransition) && colorTo == null && colorFrom != null) {
                    colorTo = processColor(one);
                    if (colorTo != null)
                        continue;
                }

                if (mat.equals(CMIMaterial.NONE) && (cmiParticle.getDataType().equals(CMIParticleDataType.BlockData) || cmiParticle.getDataType().equals(CMIParticleDataType.MaterialData))) {
                    mat = CMIMaterial.get(one);
                    if (!mat.equals(CMIMaterial.NONE))
                        continue;
                }

                if (duration == 0 && (cmiParticle.getDataType().equals(CMIParticleDataType.Trail))) {
                    try {
                        duration = Integer.parseInt(one);
                        continue;
                    } catch (Throwable e) {
                    }
                }
                if (size == 0 && (cmiParticle.getDataType().equals(CMIParticleDataType.DustOptions) || cmiParticle.getDataType().equals(CMIParticleDataType.DustTransition))) {
                    try {
                        size = Integer.parseInt(one);
                        continue;
                    } catch (Throwable e) {
                    }
                }
            }
        }

        cmiEffect = new CMIEffect(cmiParticle);

        if (Version.isCurrentEqualOrHigher(Version.v1_9_R1) && cmiEffect.getParticle() == null)
            return null;

        if (Version.isCurrentLower(Version.v1_13_R1) && cmiEffect.getParticle().getEffect() == null)
            return null;

        if (!mat.equals(CMIMaterial.NONE))
            cmiEffect.setMaterial(mat);

        if (colorFrom != null)
            cmiEffect.setColorFrom(colorFrom);
        if (colorTo != null)
            cmiEffect.setColorTo(colorTo);

        cmiEffect.setDuration(duration);

        if (size > 0)
            cmiEffect.setSize(size);

        return cmiEffect;
    }

    private static Color processColor(String colorString) {
        if (colorString.contains(",")) {
            String[] split = colorString.split(",");
            try {
                return Color.fromRGB(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            } catch (Throwable e) {

            }
        }

        CMIChatColor c = CMIChatColor.getColor(colorString);
        if (c != null)
            return c.getRGBColor();

        CMIChatColor cmicolor = CMIChatColor.getColor(colorString);
        if (cmicolor != null) {
            return Color.fromRGB(cmicolor.getRed(), cmicolor.getGreen(), cmicolor.getBlue());
        }

        return null;
    }

    public void show(Object playerConnection, Location location) {
        CMIEffectManager.playEffect(playerConnection, location, this);
    }

    public void show(Player player, Location location) {
        CMIEffectManager.playEffect(player, location, this);
    }

    public @Nullable Object getParticleParameters(@Nullable Location location) {
        if (this.particleParameters == null)
            this.particleParameters = CMIEffectManager.getParticleParameters(location, this);
        return this.particleParameters;
    }

    public @Nullable Object getParticleParameter(@Nullable Location location) {
        return CMIEffectManager.getParticleParameter(location, this);
    }

    public void setParticleParameters(Object particleParameters) {
        this.particleParameters = particleParameters;
    }
}
