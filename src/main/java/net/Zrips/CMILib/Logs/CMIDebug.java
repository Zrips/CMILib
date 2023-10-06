package net.Zrips.CMILib.Logs;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Version.Version;


public class CMIDebug {
//    private CMILogType type;
    private Object[] message;

    private static long fixedTime = 0L;

    public static void it() {
	fixedTime = System.nanoTime();
    }
    
    public static boolean isTestServer() {
	return Version.isTestServer();
    }

    public static double getIT() {
	return ((System.nanoTime() - fixedTime) / 100) / 10000D;
    }

    public static void d(Object... message) {
	Player player = Bukkit.getPlayer("Zrips");
	if (player == null || !player.isOnline())
	    return;

	StringBuilder FullMessage = new StringBuilder();
	int i = 1;
	ChatColor cl = ChatColor.GRAY;
	for (Object one : message) {
	    i++;
	    if (i >= 2) {
		i = 0;
		if (cl == ChatColor.GRAY)
		    cl = ChatColor.WHITE;
		else
		    cl = ChatColor.GRAY;
		FullMessage.append(cl);
	    }
	    if (one instanceof String[]) {
		FullMessage.append(String.valueOf(Arrays.asList((String[]) one).toString()) + " ");
	    } else
		FullMessage.append(String.valueOf(one) + " ");
	}

	player.sendMessage(ChatColor.DARK_GRAY + "[CMID] " + ChatColor.DARK_AQUA + FullMessage.toString());
    }

    public static void cd(Object... message) {
	Player player = Bukkit.getPlayer("Zrips");
	if (player == null || !player.isOnline())
	    return;

	StringBuilder FullMessage = new StringBuilder();
	for (Object one : message) {
	    if (one instanceof String[]) {
		FullMessage.append(String.valueOf(Arrays.asList((String[]) one).toString()) + " ");
	    } else
		FullMessage.append(String.valueOf(one) + " ");
	}

	player.sendMessage(ChatColor.DARK_GRAY + "[CMID] " + ChatColor.DARK_AQUA + FullMessage.toString());
    }

    @SuppressWarnings("unused")
    public static void c(Object... message) {

	if (!CMILib.getInstance().getCommandManager().enabledDebug)
	    return;
	if (!Version.isTestServer())
	    return;
	new CMIDebug(message);
    }

    public CMIDebug(String msg) {
	this((Object) msg);
    }

    public CMIDebug(
//	CMILogType type, 
	Object... message) {
	if (!CMILib.getInstance().getCommandManager().enabledDebug)
	    return;
//	this.type = type;

	this.message = message;
	CMILib.getInstance().getLogManager().print(this);
    }

//    public CMILogType getType() {
//	return type;
//    }
//
//    public void setType(CMILogType type) {
//	this.type = type;
//    }

    public String getMsg() {
	StringBuilder fullMessage = new StringBuilder();
	for (Object one : message) {
	    if (!fullMessage.toString().isEmpty())
		fullMessage.append(" ");
	    if (one instanceof String[]) {
		fullMessage.append(String.valueOf(Arrays.asList((String[]) one).toString()));
	    } else
		fullMessage.append(String.valueOf(one));
	}
	return fullMessage.toString();
    }

    public void setMsg(Object... message) {
	this.message = message;
    }
}
