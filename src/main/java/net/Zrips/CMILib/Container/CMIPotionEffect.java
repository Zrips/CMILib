package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.potion.PotionEffectType;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.FileHandler.ConfigReader;

public class CMIPotionEffect {

    private static HashMap<String, List<String>> potyionEffectList = new HashMap<String, List<String>>();
    private static final Map<Integer, PotionEffectType> byId = new HashMap<Integer, PotionEffectType>();
    private static final Map<String, PotionEffectType> byName = new HashMap<String, PotionEffectType>();

    @SuppressWarnings("deprecation")
    public static void saveEnchants() {
	for (PotionEffectType one : PotionEffectType.values()) {
	    if (one == null)
		continue;
	    byId.put(one.getId(), one);
	    byName.put(one.getName(), one);
	}
    }

    public static PotionEffectType getById(int id) {
	return byId.get(id);
    }

    public static PotionEffectType get(String nameId) {
	PotionEffectType enchant = getByName(nameId);
	if (enchant == null)
	    try {
		enchant = getById(Integer.parseInt(nameId));
	    } catch (Exception e) {

	    }
	return enchant;
    }

    public static PotionEffectType getByName(String name) {
	name = name.replace("_", "");

	main: for (Entry<String, List<String>> oneEntry : potyionEffectList.entrySet()) {
	    for (String oneVar : oneEntry.getValue()) {
		if (oneVar.replace("_", "").equalsIgnoreCase(name)) {
		    name = oneEntry.getKey();
		    break main;
		}
	    }
	}
	name = name.replace("_", "");

	for (PotionEffectType one : PotionEffectType.values()) {
	    if (one == null || one.getName() == null)
		continue;
	    if (one.getName().replace("_", "").equalsIgnoreCase(name)) {
		return one;
	    }
	    if (one.toString().replace("_", "").equalsIgnoreCase(name)) {
		return one;
	    }
	}
	return null;
    }

    public static PotionEffectType[] values() {
	return byId.values().toArray(new PotionEffectType[byId.size()]);
    }

    public static String getName(PotionEffectType enchant) {
	if (enchant == null || enchant.getName() == null)
	    return null;
	for (Entry<String, List<String>> oneEntry : potyionEffectList.entrySet()) {
	    if (oneEntry.getKey().replace("_", "").equalsIgnoreCase(enchant.getName().replace("_", "")))
		if (!oneEntry.getValue().isEmpty())
		    return oneEntry.getValue().get(0);
	}
	return enchant.getName();
    }

    public static void initialize() {
	ConfigReader locale = CMILib.getInstance().getConfigManager().getLocaleConfig();

	PotionEffectType[] enchants = PotionEffectType.values();
	for (PotionEffectType one : enchants) {
	    if (one == null)
		continue;
	    String name = one.getName();
	    if (name == null)
		continue;
	    List<String> ls = potyionEffectList.get(name.toLowerCase());
	    if (ls == null) {
		ls = new ArrayList<String>();
	    }
	    List<String> l = locale.get("info.PotionEffectAliases." + name.toLowerCase(), Arrays.asList(CMIText.firstToUpperCase(name)));
	    for (String oneL : l) {
		if (!ls.contains(oneL.replace(" ", "_"))) {
		    ls.add(oneL.replace(" ", "_"));
		}
	    }
	    potyionEffectList.put(name.toLowerCase(), ls);
	}
	saveEnchants();
    }
}
