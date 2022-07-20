package net.Zrips.CMILib.commands;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CommandType;
import net.Zrips.CMILib.Messages.CMIMessages;

public class CMICommand {

    private Cmd cmdClass;
    private String name;
    private CAnnotation anottation;

    private Boolean enabled = null;

    public CMICommand(Cmd cmdClass, String name, CAnnotation anottation) {
	this.cmdClass = cmdClass;
	this.name = name;
	this.anottation = anottation;
    }

    public Cmd getCmdClass() {
	return cmdClass;
    }

    public CMICommand setCmdClass(Cmd cmdClass) {
	this.cmdClass = cmdClass;
	return this;
    }

    public String getName() {
	return name;
    }

    public CMICommand setName(String name) {
	this.name = name;
	return this;
    }

    public CAnnotation getAnottation() {
	return anottation;
    }

    public String getTranslatedArgs() {
	String cmdString = "";
	String key = "command." + cmdClass.getClass().getSimpleName() + ".help.args";
	if (CMILib.getInstance().getLM().containsKey(key) && !CMILib.getInstance().getLM().getMessage(key).isEmpty()) {
	    cmdString = CMILib.getInstance().getLM().getMessage(key);
	}
	return cmdString;
    }

    public void setAnottation(CAnnotation anottation) {
	this.anottation = anottation;
    }

    public Boolean getEnabled() {
	return enabled;
    }

    public void setEnabled(Boolean enabled) {
	this.enabled = enabled;
    }

    public static void performCommand(CommandSender sender, String command, CommandType type) {
	performCommand(sender, Arrays.asList(command), type);
    }

    public static void performCommand(CommandSender sender, List<String> commands, CommandType type) {
	for (String command : commands) {
	    if (sender instanceof Player) {
		performCommand((Player) sender, command, type);
	    } else {
		ServerCommandEvent event = new ServerCommandEvent(sender, command.startsWith("/") ? command : "/" + command);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
		    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand().startsWith("/") ? event.getCommand().substring(1, event.getCommand().length()) : event.getCommand());
		}
		if (!type.equals(CommandType.silent))
		    Bukkit.getLogger().log(Level.INFO, sender.getName() + " issued " + type.name() + " command: /" + command);
	    }
	}
    }

    public static void performCommand(Player player, String command, CommandType type) {
	performCommand(player, Arrays.asList(command), type);
    }

    public static void performCommand(Player player, List<String> commands, CommandType type) {
	for (String command : commands) {
	    if (player == null) {
		CMIMessages.consoleMessage("&cCant perform command (" + command + "). Player is NULL");
		return;
	    }
	    if (command == null) {
		CMIMessages.consoleMessage("&cCant perform command (" + command + "). Command is NULL");
		return;
	    }
	    PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, command.startsWith("/") ? command : "/" + command);
	    Bukkit.getServer().getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {
		player.performCommand(event.getMessage().startsWith("/") ? event.getMessage().substring(1, event.getMessage().length()) : event.getMessage());
	    }
	    if (!type.equals(CommandType.silent))
		Bukkit.getLogger().log(Level.INFO, player.getName() + " issued " + type.name() + " command: /" + command);
	}
    }

}
