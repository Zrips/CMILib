package net.Zrips.CMILib.Locale;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIList;

public class Language {
    public FileConfiguration enlocale;
    public FileConfiguration Customlocale;

    private CMILib plugin;
    private String lang;

    public Language(CMILib plugin, String lang) {
	this.plugin = plugin;
	this.lang = lang;
    }

    public void setLang(String lang) {
	this.lang = lang;
    }

    /**
     * Reloads the config
     */
    public void reload() {
	try {
	    Customlocale = new YmlMaker(plugin, "Translations" + File.separator + "Locale_" + lang + ".yml").getConfig();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private FileConfiguration getEN() {
	if (enlocale == null)
	    try {
		enlocale = new YmlMaker(plugin, "Translations" + File.separator + "Locale_EN.yml").getConfig();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	return enlocale;
    }

    /**
     * Get the message with the correct key
     * @param key - the key of the message
     * @return the message
     */

    public String getMessage(String key, Object... variables) {
	String missing = "Missing locale for " + key + " ";
	String msg = "";
	if (Customlocale.isString(key))
	    msg = Customlocale.getString(key);
	else if (Customlocale.isList(key))
	    msg = CMIList.listToString(Customlocale.getStringList(key));
	else {
	    msg = getEN().isString(key) ? getEN().getString(key) : missing;
	}

	Snd snd = null;
	Location customLoc = null;

	List<Object> remove = new ArrayList<Object>();

	for (Object one : variables) {

	    if (one instanceof Snd && snd == null) {
		snd = (Snd) one;
		remove.add(one);
		continue;
	    }

	    // Fix
//	    if (CMILib.getInstance().isCmiPresent() && one instanceof com.Zrips.CMI.Containers.Snd) {
//		com.Zrips.CMI.Containers.Snd cmisnd = (com.Zrips.CMI.Containers.Snd) one;
//		msg = updateCmiSnd(cmisnd, msg);
//
//		remove.add(one);
//		continue;
//	    }

	    if (one instanceof Location && customLoc == null) {
		customLoc = (Location) one;
		remove.add(one);
		continue;
	    }
	    if (one instanceof Player && snd == null) {
		snd = new Snd().setTarget((Player) one).setTarget((Player) one);
		continue;
	    }
	}

	if (customLoc != null) {
	    msg = replacePlayer(customLoc, msg);
	}

	if (snd != null) {
	    msg = updateSnd(snd, msg);
	}
	if (variables.length > 0) {
	    for (int i = 0; i < variables.length; i++) {
		if (remove.contains(variables[i]))
		    continue;
		if (variables.length >= i + 2) {
		    Object var = variables[i + 1];
		    if (var instanceof Boolean)
			var = (Boolean) var ? LC.info_variables_True.getLocale() : LC.info_variables_False.getLocale();
		    if (var instanceof LC)
			var = ((LC) var).getText();
		    msg = outReplace(msg, variables[i], var);
		}
		i++;
	    }
	}
	msg = msg.replace("!prefix!", getM(LC.info_prefix));
	msg = filterNewLine(msg);
	return CMIChatColor.translate(msg);
    }

    public String filterNewLine(String msg) {
	Pattern patern = Pattern.compile("([ ]?[\\/][n][$|\\s])|([ ]?\\\\n)");
	Matcher match = patern.matcher(msg);
	while (match.find()) {
	    if (match.group(1) != null && !match.group(1).isEmpty())
		msg = msg.replace(match.group(1), "\n");
	    if (match.group(2) != null && !match.group(2).isEmpty())
		msg = msg.replace(match.group(2), "\n");
	}
	return msg;
    }

    private String getM(LC lc) {

	String key = lc.getPt();
	String missing = "Missing locale for &7" + key + " ";
	String msg = "";
	if (Customlocale.isString(key))
	    msg = Customlocale.getString(key);
	else if (Customlocale.isList(key))
	    msg = CMIList.listToString(Customlocale.getStringList(key));
	else
	    msg = getEN().isString(key) ? getEN().getString(key) : missing;

	return msg;
    }

    public List<String> updateSnd(Snd snd, List<String> msg) {
	for (int i = 0, l = msg.size(); i < l; ++i) {
	    msg.set(i, updateSnd(snd, msg.get(i)));
	}
	return msg;
    }

    public String updateSnd(Snd snd, String msg) {
	if (msg == null)
	    return null;

	if (!msg.contains("[")) {
	    msg = this.filterNewLine(msg);
	    return msg;
	}

	if (snd.getConsoleSender() != null) {
	    String name = snd.getConsoleSender().getName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(CMILib.getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "senderName", name);
	    msg = replace(msg, "senderDisplayName", name);
	    msg = replace(msg, "senderPrefix", "");
	    msg = replace(msg, "senderSuffix", "");
	}

	if (snd.getPlayerSender() != null) {
	    msg = replacePlayer("sender", snd.getPlayerSender(), snd.getPlayerTarget(), msg);
	    if (snd.getPlayerSender().getLocation() != null)
		msg = replacePlayer(snd.getPlayerSender().getLocation(), msg);
	}

	if (snd.getConsoleTarget() != null) {
	    String name = snd.getConsoleTarget().getName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(CMILib.getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "targetName", name);
	    msg = replace(msg, "targetDisplayName", name);
	}

	if (snd.getPlayerTarget() != null) {
	    msg = replacePlayer("", snd.getPlayerTarget(), snd.getPlayerSender(), msg);
	    msg = replacePlayer("player", snd.getPlayerTarget(), snd.getPlayerSender(), msg);
	    if (snd.getPlayerTarget().getLocation() != null)
		msg = replacePlayer(snd.getPlayerTarget().getLocation(), msg);

	    msg = plugin.getPlaceholderAPIManager().updatePlaceHolders(snd.getPlayerTarget(), msg);
	}

	if (snd.getConsoleSource() != null) {
	    String name = snd.getConsoleSource().getName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(CMILib.getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "sourceName", name);
	    msg = replace(msg, "sourceDisplayName", name);
	}
	if (snd.getSenderName() != null) {
	    String name = snd.getSenderName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(CMILib.getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "senderName", name);
	    msg = replace(msg, "senderDisplayName", name);
	}

	if (snd.getTargetName() != null) {
	    String name = snd.getTargetName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(CMILib.getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "Name", name);
	    msg = replace(msg, "DisplayName", name);
	    msg = replace(msg, "playerName", name);
	    msg = replace(msg, "playerDisplayName", name);
	}

	if (snd.getPlayerSource() != null) {
	    msg = replacePlayer("source", snd.getPlayerSource(), snd.getPlayerTarget(), msg);
	    if (snd.getPlayerSource().getLocation() != null)
		msg = replacePlayer(snd.getPlayerSource().getLocation(), msg);
	}

	msg = this.filterNewLine(msg);

	return msg;
    }

    public String updateCmiSnd(com.Zrips.CMI.Containers.Snd snd, String msg) {
	if (msg == null)
	    return null;
	if (!msg.contains("[")) {
	    msg = this.filterNewLine(msg);
	    return msg;
	}

	msg = replace(msg, "serverName", com.Zrips.CMI.CMI.getInstance().getBungeeCordManager().isBungeeCord() ? com.Zrips.CMI.CMI.getInstance().getBungeeCordManager().getThisServerName() : plugin
	    .getReflectionManager()
	    .getServerName());

	if (snd.getConsoleSender() != null) {
	    String name = snd.getConsoleSender().getName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(com.Zrips.CMI.CMI.getInstance().getPlayerManager().getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "senderName", name);
	    msg = replace(msg, "senderDisplayName", name);
	    msg = replace(msg, "senderPrefix", "");
	    msg = replace(msg, "senderSuffix", "");
	}

	if (snd.getPlayerSender() != null) {
	    msg = replacePlayer("sender", snd.getPlayerSender(), snd.getPlayerTarget(), msg);
	    if (snd.getPlayerSender().getLocation() != null)
		msg = replacePlayer(snd.getPlayerSender().getLocation(), msg);
	}

	if (snd.getSenderUser() != null) {
	    msg = replaceCmiUser("sender", snd.getSenderUser(), msg);
	}

	if (snd.getConsoleTarget() != null) {
	    String name = snd.getConsoleTarget().getName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(com.Zrips.CMI.CMI.getInstance().getPlayerManager().getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "targetName", name);
	    msg = replace(msg, "targetDisplayName", name);
	}

	if (snd.getPlayerTarget() != null) {

	    msg = replacePlayer("", snd.getPlayerTarget(), snd.getPlayerSender(), msg);
	    msg = replacePlayer("player", snd.getPlayerTarget(), snd.getPlayerSender(), msg);
	    if (snd.getPlayerTarget().isOnline() && snd.getPlayerTarget().getLocation() != null)
		msg = replacePlayer(snd.getPlayerTarget().getLocation(), msg);
	}

	if (snd.getTargetUser() != null) {
	    msg = replaceCmiUser("", snd.getTargetUser(), msg);
	}

	if (snd.getConsoleSource() != null) {
	    String name = snd.getConsoleSource().getName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(com.Zrips.CMI.CMI.getInstance().getPlayerManager().getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "sourceName", name);
	    msg = replace(msg, "sourceDisplayName", name);
	}
	if (snd.getSenderName() != null) {
	    String name = snd.getSenderName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(com.Zrips.CMI.CMI.getInstance().getPlayerManager().getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "senderName", name);
	    msg = replace(msg, "senderDisplayName", name);
	}

	if (snd.getTargetName() != null) {
	    String name = snd.getTargetName();
	    if (name.equalsIgnoreCase("Console") || name.equalsIgnoreCase(com.Zrips.CMI.CMI.getInstance().getPlayerManager().getFakeUserName()))
		name = this.getM(LC.info_Console);
	    msg = replace(msg, "Name", name);
	    msg = replace(msg, "DisplayName", name);
	    msg = replace(msg, "playerName", name);
	    msg = replace(msg, "playerDisplayName", name);
	}

	if (snd.getPlayerSource() != null) {
	    msg = replacePlayer("source", snd.getPlayerSource(), snd.getPlayerTarget(), msg);
	    if (snd.getPlayerSource().getLocation() != null)
		msg = replacePlayer(snd.getPlayerSource().getLocation(), msg);
	}

	if (snd.getSourceUser() != null) {
	    msg = replaceCmiUser("source", snd.getSourceUser(), msg);
	}

	msg = this.filterNewLine(msg);

	return msg;
    }

    public String replaceCmiUser(String type, com.Zrips.CMI.Containers.CMIUser user, String msg) {
	msg = replace(msg, "serverName", com.Zrips.CMI.CMI.getInstance().getBungeeCordManager().isBungeeCord() ? com.Zrips.CMI.CMI.getInstance().getBungeeCordManager().getThisServerName() : plugin
	    .getReflectionManager()
	    .getServerName());
	if (msg == null || user == null)
	    return msg;
	if (user.isOnline())
	    msg = replacePlayer(type, user.getPlayer(), null, msg);
	msg = replace(msg, type + "offon", getOffOn(user.isOnline()));
	msg = replace(msg, type + "Name", user.getName(false));
	msg = replace(msg, type + "Nick", user.getNickName());
	msg = replace(msg, type + "NickName", user.getNickName());
	msg = replace(msg, type + "DisplayName", user.getDisplayName(true));
	msg = replace(msg, type + "Prefix", CMIChatColor.translate(user.getPrefix()));
	msg = replace(msg, type + "Suffix", CMIChatColor.translate(user.getSuffix()));

	if (type.isEmpty()) {
	    msg = replace(msg, "playerName", user.getName(false));
	    msg = replace(msg, "playerNick", user.getNickName());
	    msg = replace(msg, "playerNickName", user.getNickName());
	    msg = replace(msg, "playerDisplayName", user.getDisplayName(true));
	    msg = replace(msg, "Prefix", CMIChatColor.translate(user.getPrefix()));
	    msg = replace(msg, "Suffix", CMIChatColor.translate(user.getSuffix()));
	}

	if (user.getLogOutLocation() != null)
	    msg = replacePlayer(type, user.getLogOutLocation(), msg);
	return msg;
    }

    @SuppressWarnings("deprecation")
    public String replacePlayer(String type, Player player, Player whoGets, String msg) {

	if (msg == null || player == null)
	    return msg;
	msg = replace(msg, type + "Name", player.getName());
	msg = replace(msg, type + "DisplayName", player.getDisplayName());
	msg = replace(msg, type + "offon", getOffOn(player, whoGets));
	msg = replace(msg, type + "Hp", (int) ((Damageable) player).getHealth());
	msg = replace(msg, type + "MaxHp", (int) ((Damageable) player).getMaxHealth());

	msg = replace(msg, type + "Hunger", player.getFoodLevel());
	if (player.getGameMode() != null) {
	    msg = replace(msg, type + "GameMode", getMessage("info.variables." + player.getGameMode().name().toLowerCase()));
	}

	if (player.isOnline() && player.getLocation() != null)
	    msg = replacePlayer(type, player.getLocation(), msg);

	return msg;
    }

    public String replacePlayer(String type, Location loc, String msg) {
	if (msg == null || loc == null)
	    return msg;
	msg = replace(msg, type + "X", loc.getBlockX());
	msg = replace(msg, type + "Y", loc.getBlockY());
	msg = replace(msg, type + "Z", loc.getBlockZ());
	msg = replace(msg, type + "Yaw", (int) loc.getYaw());
	msg = replace(msg, type + "Pitch", (int) loc.getPitch());
	if (loc.getWorld() != null) {
	    msg = replace(msg, type + "WorldName", loc.getWorld().getName());
	    msg = replace(msg, type + "World", loc.getWorld().getName());
	}
	return msg;
    }

    public String replacePlayer(Location loc, String msg) {
	if (msg == null || loc == null)
	    return msg;
	msg = replace(msg, "x", loc.getBlockX());
	msg = replace(msg, "y", loc.getBlockY());
	msg = replace(msg, "z", loc.getBlockZ());
	msg = replace(msg, "yaw", (int) loc.getYaw());
	msg = replace(msg, "pitch", (int) loc.getPitch());
	if (loc.getWorld() != null) {
	    msg = replace(msg, "worldName", loc.getWorld().getName());
	    msg = replace(msg, "world", loc.getWorld().getName());
	}
	return msg;
    }

    private static String outReplace(String msg, Object what, Object with) {
	if (what == null)
	    return msg;
	if (with == null)
	    with = "";
	return msg.replace(String.valueOf(what), String.valueOf(with));
    }

    private static String replace(String msg, Object what, Object with) {
	if (what == null)
	    return msg;
	if (with == null)
	    with = "";
	return msg.replaceAll(String.valueOf("(?i)(\\[" + what + "\\])"), Matcher.quoteReplacement(String.valueOf(with)));
    }

    public String getDefaultMessage(String key) {
	String missing = "Missing locale for " + key + " ";
	String msg = "";
	msg = getEN().isString(key) ? getEN().getString(key) : missing;

	StringBuilder s = new StringBuilder();
	if (msg.contains("/n")) {
	    int i = 0;
	    for (String one : msg.split("/n")) {
		i++;
		s.append(one);
		if (i < msg.split("/n").length)
		    s.append("\n  ");
	    }
	    msg = s.toString();
	}

	return CMIChatColor.translate(msg);
    }

    /**
     * Get the message with the correct key
     * @param key - the key of the message
     * @return the message
     */
    public List<String> getMessageList(String key, Object... variables) {
	String missing = "Missing locale for " + key + " ";

	List<String> ls;
	if (Customlocale.isList(key))
	    ls = Customlocale.getStringList(key);
	else
	    ls = !getEN().getStringList(key).isEmpty() ? getEN().getStringList(key) : Arrays.asList(missing);

	if (variables != null && variables.length > 0)
	    for (int i = 0; i < ls.size(); i++) {
		String msg = ls.get(i);
		for (int y = 0; y < variables.length; y += 2) {
		    msg = msg.replace(String.valueOf(variables[y]), String.valueOf(variables[y + 1]));
		}
		msg = filterNewLine(msg);
		ls.set(i, CMIChatColor.translate(msg));
	    }

//	    for (int i = 0; i < variables.length; i++) {
//		int ii = 0;
//		for (String one : ls) {
//		    ii++;
//		    if (variables.length >= i + 1) {
//			ls.set(ii, one.replace(String.valueOf(variables[i]), String.valueOf(variables[i + 1])));
//		    }
//		}
//	    }

	int i = 0;
	for (String one : ls) {
	    ls.set(i, CMIChatColor.translate(one.replace("!prefix!", getM(LC.info_prefix))));
	    i++;
	}

	return ls;
    }

    public boolean isList(String key) {
	if (Customlocale != null && Customlocale.contains(key))
	    return Customlocale.isList(key);
	if (getEN().contains(key))
	    return getEN().isList(key);
	return false;
    }

    /**
     * Check if key exists
     * @param key - the key of the message
     * @return true/false
     */
    public boolean containsKey(String key) {
	if (Customlocale != null && Customlocale.contains(key))
	    return true;
	return getEN().contains(key);
    }

    public boolean isString(String key) {
	return getEN().isString(key);
    }

    public Set<String> getKeys(String path) {
	if (!lang.equalsIgnoreCase("EN") && Customlocale != null && Customlocale.isConfigurationSection(path)) {
	    return Customlocale.getConfigurationSection(path).getKeys(false);
	}
	if (getEN() != null && getEN().isConfigurationSection(path)) {
	    return getEN().getConfigurationSection(path).getKeys(false);
	}

	return new HashSet<String>();
    }

    public String getOffOn(Player player, Player whoGets) {
	return getOffOn(whoGets == null ? player.isOnline() : whoGets.canSee(player) && player.isOnline());
    }

    public String getOffOn(Player player) {
	return getOffOn(player.isOnline());
    }

    public String getOffOn(boolean state) {
	return state ? LC.info_variables_Online.getLocale() : LC.info_variables_Offline.getLocale();
    }
}
