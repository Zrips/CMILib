package net.Zrips.CMILib;

import java.lang.reflect.Method;

import org.bukkit.World;

public enum ServerType {
    Bukkit, Spigot, Paper, Other;

    static ServerType type = ServerType.Bukkit;

    static {
	try {
	    for (Method one : World.class.getMethods()) {
		if (one.getName().equalsIgnoreCase("spigot")) {
		    type = ServerType.Spigot;
		    break;
		}
	    }
	} catch (Throwable e) {
	}
	try {
	    for (Method one : World.class.getMethods()) {
		if (one.getName().equalsIgnoreCase("getChunkAtAsync")) {
		    type = ServerType.Paper;
		    break;
		}
	    }
	} catch (Throwable e) {
	}
    }
}
