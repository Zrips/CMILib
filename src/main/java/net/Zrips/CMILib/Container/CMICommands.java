package net.Zrips.CMILib.Container;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.commands.CommandsHandler;

public class CMICommands {

    public static void performCommand(CommandSender sender, Object cls) {
	performCommand(sender, cls, "");
    }

    public static void performCommand(CommandSender sender, Object cls, String cmd) {
	cmd = CommandsHandler.getLabel() + " " + cls.getClass().getSimpleName() + (cmd.isEmpty() ? "" : " " + cmd);
	performCommand(sender, cmd);
    }

    public static void performCommand(CommandSender sender, String cmd) {
	if (sender instanceof Player) {
	    if (((Player) sender).isOnline()) {
		performCommand(sender, cmd, CommandType.silent);
	    }
	} else {
	    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}
    }

    public static void performCommand(CommandSender sender, String command, CommandType type) {
	if (sender instanceof Player) {
	    performCommand((Player) sender, command, type);
	} else {
	    ServerCommandEvent event = new ServerCommandEvent(sender, command.startsWith("/") ? command : "/" + command);
	    Bukkit.getServer().getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand().startsWith("/") ? event.getCommand().substring(1, event.getCommand().length()) : event.getCommand());
	    }
	    if (!type.equals(CommandType.silent))
		Bukkit.getLogger().log(Level.INFO, String.format("%s issued %s  command: /%s", sender.getName(), type.name(), command));
	}
    }

    public static void performCommand(Player player, String command, CommandType type) {
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
