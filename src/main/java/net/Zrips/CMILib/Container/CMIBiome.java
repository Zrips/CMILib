package net.Zrips.CMILib.Container;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Version;

public class CMIBiome {

	private CMIBiomeType type;
	private String name;
	private String localized;

	static HashMap<String, CMIBiome> cmiBiomeMap = new HashMap<>();

	private static Method kmet = null;
	private static Method met = null;

	public static String getBiomeName(Object biome) {
		if (!Version.isPaperBranch()) {
			return biome.toString();
		}

		try {
			if (met == null) {
				kmet = biome.getClass().getMethod("getKey");
				Object key = kmet.invoke(biome);
				met = key.getClass().getMethod("getKey");
			}
			return (String) met.invoke(kmet.invoke(biome));
		} catch (Exception e) {
		}
		return biome.toString();
	}

	static {

		try {
			Class<?> c = Class.forName("org.bukkit.block.Biome");

			Object[] biomes = (Object[]) c.getMethod("values").invoke(c);

			for (Object biome : biomes) {
				if (biome == null)
					continue;

				String name = getBiomeName(biome);

				try {
					CMIBiome cbiome = new CMIBiome(name, CMILib.getInstance().getConfigManager().getLocaleConfig().get("info.Biomes." + name, CMIText.firstToUpperCase(name)));
					cbiome.type = CMIBiomeType.get(name);
					cmiBiomeMap.put(name.toLowerCase().replace("_", "").replace(" ", ""), cbiome);
				} catch (Exception e) {
					CMIMessages.consoleMessage("&4Failed to recognize biome by (" + name + ") name. Skipping.");
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	CMIBiome(String name, String localized) {
		this.name = name;
		this.localized = localized;
	}

	public static CMIBiome get(String name) {
		return cmiBiomeMap.get(name.toLowerCase().replace("_", ""));
	}

	public static String getName(Object biome) {
		String name = getBiomeName(biome);
		CMIBiome b = cmiBiomeMap.get(name.replace("_", ""));
		return b == null ? CMIText.firstToUpperCase(name) : b.getName();
	}

	public Color getColor() {
		if (type == null)
			return new Color(0, 0, 0);
		return type.getColor();
	}

	public int getId() {
		if (type == null)
			return 0;
		return type.getId();
	}

	public String getLocalized() {
		return localized;
	}

	public String getName() {
		return name;
	}

	public static Set<String> values() {
		return cmiBiomeMap.keySet();
	}

}
