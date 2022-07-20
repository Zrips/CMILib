package net.Zrips.CMILib.Sounds;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;

public class CMISound {

    private static HashMap<String, Sound> soundsByname = new HashMap<String, Sound>();

    private String rawName = null;
    private Sound sound = null;
    private float pitch = 1;
    private float volume = 1;
    private boolean enabled = true;

    public CMISound(String name) {
	this(name, 1, 1);
    }

    public CMISound(String name, float volume, float pitch) {
	if (name == null || name.isEmpty())
	    return;
	if (name.contains(":")) {
	    String[] split = name.split(":");
	    name = split[0];
	    if (split.length > 1) {
		try {
		    volume = Float.parseFloat(split[1]);
		} catch (Exception e) {

		}
	    }
	    if (split.length > 2) {
		try {
		    pitch = Float.parseFloat(split[2]);
		} catch (Exception e) {
		}
	    }
	}

	this.volume = volume;
	this.pitch = pitch;
	name = name.toLowerCase().replace("_", "");
	rawName = name;
	if (soundsByname.isEmpty()) {
	    for (Sound one : Sound.values()) {
		soundsByname.put(one.name().toLowerCase().replace("_", ""), one);
	    }
	}
	sound = soundsByname.get(name);

	if (sound == null) {
	    for (Entry<String, Sound> one : soundsByname.entrySet()) {
		if (one.getKey().contains(name))
		    sound = one.getValue();
	    }
	}
	if (sound != null)
	    rawName = sound.toString();
    }

    public Sound getSound() {
	return sound;
    }

    public CMISound setSound(Sound sound) {
	this.sound = sound;
	return this;
    }

    public CMISound play(Location loc) {
	if (!enabled)
	    return this;
	if (sound == null)
	    return this;
	loc.getWorld().playSound(loc, sound, volume, pitch);
	return this;
    }

    public CMISound play(Player player) {
	if (!enabled)
	    return this;
	if (sound == null)
	    return this;
	if (player == null)
	    return this;
	
	CMILib.getInstance().getReflectionManager().playSound(player, player.getLocation(), sound, volume, pitch);
	
	return this;
    }

    public float getPitch() {
	return pitch;
    }

    public CMISound setPitch(float pitch) {
	this.pitch = pitch;
	return this;
    }

    public float getVolume() {
	return volume;
    }

    public CMISound setVolume(float volume) {
	this.volume = volume;
	return this;
    }

    public String getRawName() {
	return rawName;
    }

    @Override
    public String toString() {
	return sound == null ? "Unknown" : sound.toString() + ":" + fmt(volume) + ":" + fmt(pitch);
    }

    private static String fmt(float d) {
	return String.format("%s", d);
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }
}
