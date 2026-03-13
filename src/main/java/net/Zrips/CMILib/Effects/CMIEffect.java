package net.Zrips.CMILib.Effects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMILocation;
import net.Zrips.CMILib.Container.CMIVector3D;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticle;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Version.Version;

public class CMIEffect {

    private CMIParticle particle;
    private int amount = 1;
    private float speed = 0;
    private Vector offset = new Vector();

    private CMIParticleOptions options = null;

    private Object particleParameters = null;

    public CMIEffect(CMIParticle particle) {
        this.particle = particle;
        rebuildOptions();
    }

    private void rebuildOptions() {
        switch (getParticle().getDataType()) {
        case BlockData:
            options = CMIParticleOptions.buildBlockData(null);
            break;
        case Color:
            options = CMIParticleOptions.buildColor(null);
            break;
        case DustOptions:
            options = CMIParticleOptions.buildDustOptions(null, 1);
            break;
        case DustTransition:
            options = CMIParticleOptions.buildDustTransition(null, null, 1);
            break;
        case Float:
            options = CMIParticleOptions.buildFloat(0);
            break;
        case Int:
            options = CMIParticleOptions.buildInteger(0);
            break;
        case ItemStack:
            options = CMIParticleOptions.buildItemStack(null);
            break;
        case Spell:
            options = CMIParticleOptions.buildSpell(null, 1);
            break;
        case Trail:
            options = CMIParticleOptions.buildTrail(null, null, 1);
            break;
        case Vibration:
            options = CMIParticleOptions.buildVibration(null, null, 1);
            break;
        default:
        case Void:
            options = CMIParticleOptions.buildEmpty();
            break;
        }
    }

    public CMIParticle getParticle() {
        if (particle == null) {
            particle = CMIParticle.DUST;
            rebuildOptions();
        }
        return particle;
    }

    public @NotNull CMIParticleOptions getOptions() {
        return options;
    }

    public void setParticle(CMIParticle particle) {
        this.particle = particle;
        this.particleParameters = null;
        rebuildOptions();
    }

    @Deprecated
    public @Nullable Color getColor() {
        return getColorFrom();
    }

    @Deprecated
    public void setColor(Color color) {
        setColorFrom(color);
    }

    @Deprecated
    public @Nullable Color getColorFrom() {
        if (getOptions() instanceof CMIParticleColor)
            return ((CMIParticleColor) getOptions()).getColor();
        return null;
    }

    @Deprecated
    public void setColorFrom(Color color) {
        if (getOptions() instanceof CMIParticleColor)
            ((CMIParticleColor) getOptions()).setColor(color);
    }

    @Deprecated
    public @Nullable Color getColorTo() {
        if (getOptions() instanceof CMIParticleDustTransition)
            return ((CMIParticleDustTransition) getOptions()).getColorTo();
        return null;
    }

    @Deprecated
    public void setColorTo(Color color) {
        if (getOptions() instanceof CMIParticleDustTransition)
            ((CMIParticleDustTransition) getOptions()).setColorTo(color);
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

    @Deprecated
    public int getSize() {
        if (getOptions() instanceof CMIParticleDustOptions)
            return (int) ((CMIParticleDustOptions) getOptions()).getSize();
        return 1;
    }

    @Deprecated
    public void setSize(int size) {
        if (getOptions() instanceof CMIParticleDustOptions)
            ((CMIParticleDustOptions) getOptions()).setSize(size);
    }

    @Deprecated
    public @Nullable CMIMaterial getMaterial() {
        if (getOptions() instanceof CMIParticleMaterial)
            return ((CMIParticleMaterial) getOptions()).getMaterial();
        return null;
    }

    @Deprecated
    public void setMaterial(CMIMaterial material) {
        if (getOptions() instanceof CMIParticleMaterial)
            ((CMIParticleMaterial) getOptions()).setMaterial(material);
    }

    @Deprecated
    public int getDuration() {
        if (getOptions() instanceof CMIParticleTrail)
            return ((CMIParticleTrail) getOptions()).getDuration();
        return 1;
    }

    @Deprecated
    public void setDuration(int duration) {
        if (getOptions() instanceof CMIParticleTrail)
            ((CMIParticleTrail) getOptions()).setDuration(duration);
    }

    public static CMIEffect deserialize(String name) {
        return get(name);
    }

    private static final String VECTOR_REGEX = "[^;{}]*(?:\\{[^}]*\\}[^;{}]*)*";
    private static final Pattern VECTOR_PATTERN = Pattern.compile(VECTOR_REGEX, Pattern.MULTILINE);

    public static CMIEffect get(String name) {

        if (name == null)
            return null;
        name = name.replace("_", "").toLowerCase();
        CMIMaterial mat = CMIMaterial.NONE;
        Color colorFrom = null;
        Color colorTo = null;

        float size = 0;

        String sub = null;

        if (name.contains(";")) {
            sub = name.split(";", 2)[1];
            name = name.split(";", 2)[0];
        }

        CMIParticle cmiParticle = CMIParticle.get(name);

        if (cmiParticle == null)
            return null;

        CMIEffect cmiEffect = new CMIEffect(cmiParticle);

        if (Version.isCurrentEqualOrHigher(Version.v1_9_R1) && cmiEffect.getParticle() == null)
            return null;

        if (Version.isCurrentLower(Version.v1_13_R1) && cmiEffect.getParticle().getEffect() == null)
            return null;

        if (sub == null)
            return cmiEffect;

        Matcher matcher = VECTOR_PATTERN.matcher(sub);
        List<String> parts = new ArrayList<>();

        while (matcher.find()) {
            String part = matcher.group();
            if (!part.isEmpty()) {
                parts.add(part);
            }
        }

        String[] split = parts.toArray(new String[0]);

        for (String one : split) {

            if (one.startsWith("a{") && one.endsWith("}")) {
                try {
                    int amount = Integer.parseInt(one.substring(2, one.length() - 1));
                    cmiEffect.setAmount(amount);
                    continue;
                } catch (Throwable e) {
                }
            }

            if (one.startsWith("s{") && one.endsWith("}")) {
                try {
                    float speed = Float.parseFloat(one.substring(2, one.length() - 1));
                    cmiEffect.setSpeed(speed);
                    continue;
                } catch (Throwable e) {
                }
            }

            if (one.startsWith("o{") && one.endsWith("}")) {
                try {

                    String temp = one.substring(2, one.length() - 1);
                    CMIVector3D loc = CMIVector3D.fromString(temp);
                    if (loc != null)
                        cmiEffect.setOffset(loc.toVector());
                    continue;
                } catch (Throwable e) {
                }
            }

            if (cmiEffect.getOptions() instanceof CMIParticleTrail) {

                if (!one.contains("{")) {
                    try {
                        int duration = Integer.parseInt(one);
                        ((CMIParticleTrail) cmiEffect.getOptions()).setDuration(duration);
                        continue;
                    } catch (Throwable e) {
                    }
                }

                Color color = processColor(one);
                if (color != null) {
                    ((CMIParticleTrail) cmiEffect.getOptions()).setColor(color);
                    continue;
                }
            }

            if (cmiEffect.getOptions() instanceof CMIParticleColor && colorFrom == null) {
                colorFrom = processColor(one);
                if (colorFrom != null) {
                    ((CMIParticleColor) cmiEffect.getOptions()).setColor(colorFrom);
                    continue;
                }
            }

            if (cmiEffect.getOptions() instanceof CMIParticleDustTransition && colorTo == null && colorFrom != null) {
                colorTo = processColor(one);
                if (colorTo != null) {
                    ((CMIParticleDustTransition) cmiEffect.getOptions()).setColorTo(colorTo);
                    continue;
                }
            }

            if (cmiEffect.getOptions() instanceof CMIParticleMaterial && mat.equals(CMIMaterial.NONE)) {
                mat = CMIMaterial.get(one);
                if (!mat.equals(CMIMaterial.NONE)) {
                    if (cmiEffect.getOptions() instanceof CMIParticleBlockData) {
                        ((CMIParticleBlockData) cmiEffect.getOptions()).setMaterial(mat);
                    } else if (cmiEffect.getOptions() instanceof CMIParticleItemStack) {
                        ((CMIParticleItemStack) cmiEffect.getOptions()).setMaterial(mat);
                    }
                    continue;
                }
            }

            if (cmiEffect.getOptions() instanceof CMIParticleLocation && ((CMIParticleLocation) cmiEffect.getOptions()).getOffset().isZero()) {
                try {

                    String temp = one.replace(",", ";").replace("{", "").replace("}", "");

                    CMIVector3D loc = CMIVector3D.fromString(temp);
                    if (loc != null) {
                        ((CMIParticleLocation) cmiEffect.getOptions()).setOffset(loc);
                        continue;
                    }
                } catch (Throwable e) {
                }
            }

            if (cmiEffect.getOptions() instanceof CMIParticleVibration && ((CMIParticleVibration) cmiEffect.getOptions()).getDestination() == null) {
                try {
                    String temp = one.replace(",", ";").replace("{", "").replace("}", "");
                    CMIVector3D loc = CMIVector3D.fromString(temp);
                    if (loc != null) {
                        ((CMIParticleVibration) cmiEffect.getOptions()).setDestination(loc);
                        continue;
                    }
                } catch (Throwable e) {
                }
            }

            if (size == 0 && cmiEffect.getOptions() instanceof CMIParticleDustOptions) {
                try {
                    size = Float.parseFloat(one);
                    ((CMIParticleDustOptions) cmiEffect.getOptions()).setSize(size);
                    continue;
                } catch (Throwable e) {
                }
            } else if (size == 0 && cmiEffect.getOptions() instanceof CMIParticleFloat) {
                try {
                    size = Float.parseFloat(one);
                    ((CMIParticleFloat) cmiEffect.getOptions()).setValue(size);
                    continue;
                } catch (Throwable e) {
                }
            } else if (size == 0 && cmiEffect.getOptions() instanceof CMIParticleInteger) {
                try {
                    size = Integer.parseInt(one);
                    ((CMIParticleInteger) cmiEffect.getOptions()).setValue((int) size);
                    continue;
                } catch (Throwable e) {
                }
            }
        }

        return cmiEffect;
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(getParticle().getName().replace(" ", ""));

        if (getAmount() != 1)
            sb.append(";a{").append(getAmount()).append("}");
        if (getSpeed() != 0)
            sb.append(";s{").append(getSpeed()).append("}");

        if (!getOffset().isZero())
            sb.append(";o{").append((new CMIVector3D(getOffset())).toString()).append("}");

        if (getOptions() instanceof CMIParticleDustOptions) {
            float size = ((CMIParticleDustOptions) getOptions()).getSize();
            if (size != 1)
                sb.append(";").append(size);
        }

        if (getOptions() instanceof CMIParticleLocation) {
            CMIVector3D offset = ((CMIParticleLocation) getOptions()).getOffset();
            if (!offset.isZero())
                sb.append(";{").append(offset.toString()).append("}");
        }

        if (getOptions() instanceof CMIParticleDustTransition) {

            Color colorFrom = ((CMIParticleDustTransition) getOptions()).getColor();
            if (colorFrom.getRed() != 200 || colorFrom.getGreen() != 0 || colorFrom.getBlue() != 50) {
                CMIChatColor cmiColorFrom = new CMIChatColor(colorFrom);
                sb.append(";").append(cmiColorFrom.getFormatedHex());
            }

            Color colorTo = ((CMIParticleDustTransition) getOptions()).getColorTo();
            if (colorTo.getRed() != 50 || colorTo.getGreen() != 0 || colorTo.getBlue() != 200) {
                CMIChatColor cmiColorTo = new CMIChatColor(colorTo);
                sb.append(";").append(cmiColorTo.getFormatedHex());
            }

        } else if (getOptions() instanceof CMIParticleColor) {
            Color colorTo = ((CMIParticleColor) getOptions()).getColor();
            if (colorTo.getRed() != 200 || colorTo.getGreen() != 0 || colorTo.getBlue() != 50) {
                CMIChatColor cmiColorTo = new CMIChatColor(colorTo);
                sb.append(";").append(cmiColorTo.getFormatedHex());
            }
        }

        if (getOptions() instanceof CMIParticleMaterial) {
            CMIMaterial mat = null;
            if (getOptions() instanceof CMIParticleBlockData)
                mat = ((CMIParticleBlockData) getOptions()).getMaterial();
            else if (getOptions() instanceof CMIParticleItemStack)
                mat = ((CMIParticleItemStack) getOptions()).getMaterial();
            if (mat != null && !mat.equals(CMIMaterial.NONE))
                sb.append(";").append(mat.getName());
        }

        if (getOptions() instanceof CMIParticleTrail) {
            int duration = ((CMIParticleTrail) getOptions()).getDuration();
            if (duration != 1)
                sb.append(";").append(duration);

            Color colorFrom = ((CMIParticleTrail) getOptions()).getColor();

            if (colorFrom.getRed() != 200 || colorFrom.getGreen() != 0 || colorFrom.getBlue() != 50) {
                CMIChatColor cmiColorTo = new CMIChatColor(colorFrom);
                sb.append(";").append(cmiColorTo.getFormatedHex());
            }
        }

        if (getOptions() instanceof CMIParticleVibration) {
            CMIVector3D dest = ((CMIParticleVibration) getOptions()).getDestination();
            if (dest != null)
                sb.append(";{").append(dest.toString()).append("}");
        }

        if (getOptions() instanceof CMIParticleFloat) {
            float value = ((CMIParticleFloat) getOptions()).getValue();
            if (value != 0)
                sb.append(";").append(value);
        }

        if (getOptions() instanceof CMIParticleInteger) {
            int value = ((CMIParticleInteger) getOptions()).getValue();
            if (value != 0)
                sb.append(";").append(value);
        }

        return sb.toString().replace(" ", "");
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

    public @Nullable Object getParticleParameter(@Nullable Location location) {
        return CMIEffectManager.getParticleParameter(location, this);
    }

    public @Nullable Object getParticleOptions(@Nullable Location location) {
        if (this.particleParameters == null || this.getOptions() instanceof CMIParticleLocation)
            this.particleParameters = CMIEffectManager.getParticleOptions(location, this);
        return this.particleParameters;
    }

    public @Nullable Object generateParameterObject(@Nullable Location location) {
        return CMIEffectManager.getParticleParameter(location, this);
    }

    public void setParticleParameters(Object particleParameters) {
        this.particleParameters = particleParameters;
    }

    public void resetParticleParameters(@Nullable Location location) {
        this.particleParameters = CMIEffectManager.getParticleOptions(location, this);
    }
}
