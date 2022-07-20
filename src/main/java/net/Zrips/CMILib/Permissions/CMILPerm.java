package net.Zrips.CMILib.Permissions;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Locale.Snd;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.commands.CommandsHandler;

public enum CMILPerm {
    command_$1_others("Allows to perform command on another player", "commandName"),
    command_$1_others_$2("Allows to perform command on another player", "commandName", "extra"),
    command("Gives access to base usage of commands"),
    permisiononerror("Allows to see missing permission on error message"),
    command_$1("", false);

    private Boolean show = true;
    private String desc;
    private String[] wars;

    CMILPerm(String desc, Boolean show) {
	this.desc = desc;
	this.show = show;
    }

    CMILPerm(String desc) {
	this.desc = desc;
    }

    CMILPerm(String desc, String... wars) {
	this.desc = desc;
	this.wars = wars;
    }

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public String getPermissionForShow() {
	return getPermissionForShow(false);
    }

    public String getPermissionForShow(boolean cmd) {
	if (this.getWars() == null)
	    return getPermission("");

	String[] w = new String[this.getWars().length];

	for (int i = 0; i < this.getWars().length; i++) {
	    w[i] = CMIMessages.getIM("checkperm", "variableColor") + "[" + this.getWars()[i] + "]" + (!cmd ? CMIMessages.getIM("checkperm", "permissionColor") : CMIMessages.getIM(
		"checkperm", "cmdPermissionColor"));
	}

	return getPermission(w);
    }

    public String getPermission() {
	return getPermission("");
    }

    public String getPermission(String... extra) {
	String perm = this.name().replace("_", ".");
	int i = 0;
	for (String one : extra) {
	    i++;
	    if (one == null)
		continue;
	    perm = perm.replace("$" + i, one.toLowerCase());
	}
	perm = perm.replace("$star", "*");
	return CommandsHandler.getLabel() + "." + perm;
    }

    public boolean hasPermission(CommandSender sender) {
	return hasPermission(sender, false);
    }

    public boolean hasPermission(CommandSender sender, Integer... extra) {
	String[] ex = new String[extra.length];
	for (int i = 0; i < extra.length; i++) {
	    ex[i] = String.valueOf(extra[i]);
	}
	return hasPermission(sender, false, ex);
    }

    public boolean hasPermission(CommandSender sender, String... extra) {
	return hasPermission(sender, false, true, null, extra);
    }

    public boolean hasPermission(CMICommandSender cmisender, boolean b, long l) {
	return hasPermission(cmisender.getSender(), b, true, l);
    }
    
    public boolean hasPermission(CommandSender sender, Long delay, String... extra) {
	return hasPermission(sender, false, true, delay, extra);
    }

    public boolean hasPermission(CommandSender sender, boolean inform, String... extra) {
	return hasPermission(sender, inform, true, null, extra);
    }

    public boolean hasPermission(CommandSender sender, boolean inform, boolean informConsole, String... extra) {
	return hasPermission(sender, inform, informConsole, null, extra);
    }

    public boolean hasPermission(CommandSender sender, boolean inform, Long delayInMiliSeconds) {
	return hasPermission(sender, inform, true, delayInMiliSeconds);
    }

    public boolean hasPermission(CMICommandSender sender, boolean inform, boolean informConsole, Long delay, String... extra) {
	if (!sender.isPlayer())
	    return true;
	return hasPermission(sender.getPlayer(), inform, informConsole, delay, extra);
    }

    public boolean hasPermission(CommandSender sender, boolean inform, boolean informConsole, Long delay, String... extra) {
	if (sender == null)
	    return false;

	if (!(sender instanceof Player)) {
	    return true;
	}

	String perm = this.getPermission(extra);

	Player player = (Player) sender;

	PermissionInfo info = getFromCache(player, perm);
	boolean has = false;
	if (info != null && info.getDelay() + info.getLastChecked() > System.currentTimeMillis()) {
	    has = info.isEnabled();
	} else {
	    has = sender.hasPermission(perm);
	    addToCache(player, perm, has, delay == null ? 200L : delay);
	}

	if (!has && inform) {
	    boolean showPerm = CMILibConfig.permisionOnError || CMILPerm.permisiononerror.hasPermission(sender);
	    RawMessage rm = new RawMessage();
	    rm.add(CMIMessages.getMsg(LC.info_NoPermission), showPerm ? perm : null);
	    rm.show(sender);

	    informConsole(sender, perm, informConsole);
	}
	return has;
    }

    private static void informConsole(CommandSender sender, String permission, boolean informConsole) {
	if (informConsole && CMILibConfig.isPermisionInConsole) {
	    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	    Snd snd = new Snd().setSender(console).setTarget(sender);
	    CMIMessages.sendMessage(console, CMIMessages.getMsg(LC.info_NoPlayerPermission, snd, "[permission]", permission), false);
	}
    }

    public boolean hasSetPermission(CommandSender sender, String... extra) {
	return hasSetPermission(sender, false, extra);
    }

    public boolean hasSetPermission(CommandSender sender, boolean inform, String... extra) {
	return hasSetPermission(sender, this.getPermission(extra), inform);
    }

    public static boolean hasSetPermission(CommandSender sender, String perm, boolean inform) {
	boolean has = isSetPermission(sender, perm);
	if (!has && inform) {
	    boolean showPerm = CMILibConfig.permisionOnError || CMILPerm.permisiononerror.hasPermission(sender);
	    RawMessage rm = new RawMessage();
	    rm.add(CMIMessages.getMsg(LC.info_NoPermission), showPerm ? perm : null);
	    rm.show(sender);

	    informConsole(sender, perm, true);
	}
	return has;
    }

    public String[] getWars() {
	return wars;
    }

    public void setWars(String[] wars) {
	this.wars = wars;
    }

    public Boolean getShow() {
	return show;
    }

    public void setShow(Boolean show) {
	this.show = show;
    }

    public static boolean hasPermission(CommandSender sender, String permision, Boolean output) {
	return hasPermission(sender, permision, output, true);
    }

    public static boolean hasPermission(CommandSender sender, String permision, Boolean output, boolean informConsole) {
	if (!(sender instanceof Player)) {
	    return true;
	}
	Player player = (Player) sender;
	if (player.hasPermission(permision)) {
	    return true;
	}
	if (output) {
	    boolean showPerm = CMILibConfig.permisionOnError || CMILPerm.permisiononerror.hasPermission(sender);
	    RawMessage rm = new RawMessage();
	    rm.addText(CMIMessages.getMsg(LC.info_NoPermission)).addHover(showPerm ? permision : null);
	    rm.show(sender);

	    informConsole(sender, permision, informConsole);
	}
	return false;
    }

    private static HashMap<UUID, HashMap<String, PermissionInfo>> cache = new HashMap<UUID, HashMap<String, PermissionInfo>>();

    public void removeFromCache(Player player) {
	cache.remove(player.getUniqueId());
    }

    public PermissionInfo getFromCache(Player player, String perm) {
	HashMap<String, PermissionInfo> old = cache.get(player.getUniqueId());
	if (old == null) {
	    return null;
	}

	PermissionInfo info = old.get(perm);

	if (info == null) {
	    return null;
	}

	return info;
    }

    public PermissionInfo addToCache(Player player, String perm, boolean has, Long delayInMiliseconds) {
	HashMap<String, PermissionInfo> old = cache.get(player.getUniqueId());
	if (old == null) {
	    old = new HashMap<String, PermissionInfo>();
	}

	PermissionInfo info = new PermissionInfo(perm, delayInMiliseconds);
	info.setLastChecked(System.currentTimeMillis());
	info.setEnabled(has);

	old.put(perm, info);
	cache.put(player.getUniqueId(), old);

	return info;
    }

    public PermissionInfo getPermissionInfo(Player player, String perm, Long delayInMiliseconds) {
	return getPermissionInfo(player, perm, false, delayInMiliseconds);
    }

    public PermissionInfo getPermissionInfo(Player player, CMILPerm perm) {
	return getPermissionInfo(player, perm, 1000L);
    }

    public PermissionInfo getPermissionInfo(Player player, CMILPerm perm, Long delayInMiliseconds) {
	String permission = perm.getPermission(" ");
	if (permission.endsWith(" "))
	    permission = permission.replace(" ", "");
	if (permission.endsWith("."))
	    permission = permission.substring(0, permission.length() - 1);
	return getPermissionInfo(player, permission, false, delayInMiliseconds);
    }

    @Deprecated
    public PermissionInfo getPermissionInfo(Player player, String perm) {
	return getPermissionInfo(player, perm, false, 1000L);
    }

    public PermissionInfo getPermissionInfo(Player player, String perm, boolean force) {
	return getPermissionInfo(player, perm, force, 1000L);
    }

    public PermissionInfo getPermissionInfo(Player player, String perm, boolean force, Long delay) {
	perm = perm.toLowerCase();
	if (!perm.endsWith("."))
	    perm += ".";

	if (player == null)
	    return new PermissionInfo(perm, delay);

	HashMap<String, PermissionInfo> c = cache.get(player.getUniqueId());
	if (c == null)
	    c = new HashMap<String, PermissionInfo>();

	PermissionInfo p = c.get(perm);

	if (p == null)
	    p = new PermissionInfo(perm, delay);

	if (delay != null)
	    p.setDelay(delay);

	String pref = perm.contains(".") ? perm.split("\\.")[0] : perm;

	if (force || p.isTimeToRecalculate()) {
	    HashMap<String, Boolean> all = getAll(player, pref);

	    Double max = null;
	    Double min = null;
	    for (Entry<String, Boolean> uno : all.entrySet()) {
		if (!uno.getValue())
		    continue;

		if (!uno.getKey().startsWith(perm))
		    continue;

		String value = uno.getKey().replace(perm, "");

		p.addValue(value);
		try {

		    double t = Double.parseDouble(value);

		    if (max == null || t > max)
			max = t;

		    if (min == null || t < min)
			min = t;
		} catch (Exception e) {
		}

	    }

	    p.setMaxValue(max);
	    p.setMinValue(min);
	}
	p.setLastChecked(System.currentTimeMillis());
	c.put(perm, p);
	cache.put(player.getUniqueId(), c);
	return p;
    }

    private static HashMap<String, Boolean> getAll(Player player, String pref) {
	pref = pref.endsWith(".") ? pref : pref + ".";
	HashMap<String, Boolean> mine = new HashMap<String, Boolean>();
	for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
	    if (permission.getPermission().startsWith(pref))
		mine.put(permission.getPermission(), permission.getValue());
	}
	return mine;
    }

    public static PermissionAttachmentInfo getSetPermission(CommandSender sender, String perm) {
	if (sender instanceof Player)
	    for (PermissionAttachmentInfo permission : ((Player) sender).getEffectivePermissions()) {
		if (permission.getPermission().equalsIgnoreCase(perm)) {
		    return permission;
		}
	    }
	return null;
    }

    public static boolean isSetPermission(CommandSender sender, String perm) {
	if (sender instanceof Player)
	    return isSetPermission((Player) sender, perm);
	return true;
    }

    public static boolean isSetPermission(Player player, String perm) {
	return player.hasPermission(new Permission(perm, PermissionDefault.FALSE));
    }
}
