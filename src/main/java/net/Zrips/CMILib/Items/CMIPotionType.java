package net.Zrips.CMILib.Items;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CMIPotionType {

    public static PotionType get(PotionEffectType type) {
	if (type == null)
	    return null;
	String name = type.getName().replace(" ", "").replace("_", "");
	PotionType by = PotionType.getByEffect(type);
	if (by != null)
	    return by;
	for (PotionType one : PotionType.values()) {
	    if (one == null)
		continue;
	    if (name.equalsIgnoreCase(one.toString().replace(" ", "").replace("_", "")))
		return one;
	}
	return null;
    }

}
