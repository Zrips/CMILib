package net.Zrips.CMILib.Container;

import java.util.Arrays;
import java.util.LinkedHashMap;

import org.bukkit.block.Biome;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Messages.CMIMessages;

public class CMIBiome {

    private static LinkedHashMap<Biome, CMIBiome> biomeNames = new LinkedHashMap<Biome, CMIBiome>();

    private Biome biome;
    private String name = null;

    public static void initialize() {
	Arrays.stream(Biome.values()).map(Biome::toString).sorted().filter(name -> name != null).forEach(name -> {
	    try {
		Biome biome = null;
		for (Biome bio : Biome.values()) {
		    if (bio.toString().equalsIgnoreCase(name)) {
			biome = bio;
			break;
		    }
		}
		if (biome == null)
		    return;
		biomeNames.put(biome, new CMIBiome(biome, CMILib.getInstance().getConfigManager().getLocaleConfig().get("info.Biomes." + biome.toString(), CMIText.firstToUpperCase(biome.toString()))));
	    } catch (Exception e) {
		CMIMessages.consoleMessage("&4Failed to recognize biome by (" + name + ") name. Skipping.");
	    }
	});
    }

    public CMIBiome(Biome biome) {
	this.biome = biome;
	CMIBiome n = biomeNames.get(biome);
	if (n != null)
	    name = n.getBiomeName();
    }

    public CMIBiome(Biome biome, String name) {
	this.biome = biome;
	this.name = name;
    }

    public CMIBiome getBiome(Biome biome) {
	return biomeNames.get(biome);
    }

    public Biome getBiome() {
	return biome;
    }

    public String getBiomeName() {
	if (name == null) {
	    name = CMIText.firstToUpperCase(biome.toString());
	}
	return name;
    }
}
