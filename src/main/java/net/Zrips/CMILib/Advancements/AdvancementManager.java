package net.Zrips.CMILib.Advancements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Advancements.CMIAdvancement.AdvancementBuilder;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Items.CMIItemStack;

public class AdvancementManager {

    static HashMap<String, CMIAdvancement> map = new HashMap<String, CMIAdvancement>();

    public static CMIAdvancement getOld(String... key) {
	String oneKey = "";
	for (String one : key) {
	    oneKey += one;
	}
	return map.get(oneKey);
    }

    public static void add(CMIAdvancement adv) {
	map.put(adv.getId().getKey(), adv);
    }

    public enum FrameType {
	TASK("task"),
	GOAL("goal"),
	CHALLENGE("challenge");

	private String name;

	FrameType(String name) {
	    this.name = name;
	}

	public static FrameType getFromString(String frameType) {
	    try {
		for (FrameType one : FrameType.values()) {
		    if (one.name.equalsIgnoreCase(frameType))
			return one;
		}
	    } catch (EnumConstantNotPresentException e) {
	    }
	    return FrameType.TASK;
	}

	@Override
	public String toString() {
	    return name;
	}
    }

    public class Condition {
	protected String name;
	protected JsonObject set;

	public Condition(String name, JsonObject set) {
	    this.name = name;
	    this.set = set;
	}
    }

    public class ConditionBuilder {
	private String name;
	private JsonObject set;

	ConditionBuilder() {
	}

	public Condition build() {
	    return new Condition(name, set);
	}
    }

    public static void sendToast(Player player, String message, CMIItemStack icon, FrameType task) {
	Set<Player> players = new HashSet<Player>();
	players.add(player);
	sendToast(players, message, icon, task);
    }

    public static void sendToast(Set<Player> players, String message, CMIItemStack icon, FrameType task) {

	if (players.size() == 1) {
	    Player player = players.iterator().next();
	    message = CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(player, message);
	}

	AdvancementBuilder builder = CMIAdvancement.builder(new org.bukkit.NamespacedKey(CMILib.getInstance(), "cmi/commandToast"));
	builder.description("_")
	    .icon("minecraft:" + icon.getMojangName().toLowerCase().replace(" ", "_"))
	    .data(icon.getData())
	    .announce(false)
	    .hidden(false)
	    .toast(true)
	    .background("minecraft:textures/gui/advancements/backgrounds/adventure.png")
	    .frame(task);

	CMIAdvancement advancementAPI = builder
	    .title(CMIChatColor.translate(message))
	    .build();
	try {
	    advancementAPI.show(CMILib.getInstance(), players.toArray(new Player[players.size()]));
	} catch (Exception | Error e) {

	    String iconString = icon.getBukkitName() == null ? "stone" : icon.getBukkitName().toLowerCase().replace(" ", "_");

	    builder = CMIAdvancement.builder(new org.bukkit.NamespacedKey(CMILib.getInstance(), "cmi/commandToast"));
	    builder.description("_")
		.icon("minecraft:" + iconString)
		.data(icon.getData())
		.announce(false)
		.hidden(false)
		.toast(true)
		.background("minecraft:textures/gui/advancements/backgrounds/end.png")
		.frame(task);
	    advancementAPI = builder
		.title(CMIChatColor.translate(message))
		.build();

	    advancementAPI.show(CMILib.getInstance(), players.toArray(new Player[players.size()]));
	}

    }
}
