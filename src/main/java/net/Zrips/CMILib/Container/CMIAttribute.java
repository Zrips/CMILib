package net.Zrips.CMILib.Container;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;

import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Version;

public enum CMIAttribute {
    SCALE("GENERIC_SCALE"),
    MOVEMENT_SPEED("GENERIC_MOVEMENT_SPEED"),
    MAX_HEALTH("GENERIC_MAX_HEALTH"),
    ;

    Object attribute;

    CMIAttribute(org.bukkit.attribute.Attribute attribute) {
        this.attribute = attribute;
    }

    CMIAttribute(String attribute) {
        try {
            Class<?> c = Class.forName("org.bukkit.attribute.Attribute");
            Object[] attributes = (Object[]) c.getMethod("values").invoke(c);

            if (Version.isPaperBranch() && Version.isCurrentEqualOrHigher(Version.v1_21_R6)) {
                Class<?> cc = Class.forName("org.bukkit.craftbukkit.attribute.CraftAttribute");
                Method method = cc.getMethod("getKey");
                for (Object one : attributes) {
                    NamespacedKey key = (NamespacedKey) method.invoke(one);
                    if (key.getKey().equalsIgnoreCase(this.toString())) {
                        this.attribute = one;
                        break;
                    }
                }
            }

            // Outdated method
            if (this.attribute == null) {
                for (Object one : attributes) {
                    if (one.toString().equalsIgnoreCase(attribute) || this.toString().equalsIgnoreCase(one.toString())) {
                        this.attribute = one;
                        break;
                    }
                }
            }

            // Last fail safe approach
            if (this.attribute == null) {
                for (Object one : attributes) {
                    if (one.toString().contains(attribute.toLowerCase()) || one.toString().contains(this.toString().toLowerCase())) {
                        this.attribute = one;
                        break;
                    }
                }
            }

            if (this.attribute == null)
                CMIMessages.consoleMessage("&c[CMIL] Cant find attribute: " + this.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getAttribute() {
        return attribute;
    }

    private static Method method = null;

    public @Nullable AttributeInstance get(Entity ent) {
        if (Version.isCurrentEqualOrLower(Version.v1_9_R1))
            return null;
        if (this.getAttribute() == null)
            return null;

        if (!(ent instanceof org.bukkit.attribute.Attributable))
            return null;

        try {
            if (method == null)
                method = Class.forName("org.bukkit.attribute.Attributable").getMethod("getAttribute", Class.forName("org.bukkit.attribute.Attribute"));
            return (AttributeInstance) method.invoke((org.bukkit.attribute.Attributable) ent, this.getAttribute());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setBaseValue(Entity ent, double value) {
        if (Version.isCurrentEqualOrLower(Version.v1_9_R1))
            return;
        AttributeInstance attrib = get(ent);
        if (attrib == null)
            return;
        attrib.setBaseValue(value);
    }

    public double getValue(Entity ent) {
        if (Version.isCurrentEqualOrLower(Version.v1_9_R1))
            return 0D;
        AttributeInstance attrib = get(ent);
        if (attrib == null)
            return 0;
        return attrib.getValue();
    }

    public double getBaseValue(Entity ent) {
        if (Version.isCurrentEqualOrLower(Version.v1_9_R1))
            return 0D;
        AttributeInstance attrib = get(ent);
        if (attrib == null)
            return 0;
        return attrib.getBaseValue();
    }
}
