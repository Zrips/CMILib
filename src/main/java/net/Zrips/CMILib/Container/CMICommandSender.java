package net.Zrips.CMILib.Container;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;

public class CMICommandSender {

    public enum commandSenderType {
	player, console, commandblock, rcon, invalid;
    }

    private commandSenderType type = commandSenderType.invalid;
    private CommandSender sender;

    public CMICommandSender(CommandSender sender) {
	this.sender = sender;

	if (sender == null)
	    return;

	if (sender instanceof Player) {
	    type = commandSenderType.player;
	} else if (sender instanceof ConsoleCommandSender) {
	    type = commandSenderType.console;
	} else {
	    try {
		if (Class.forName("org.bukkit.command.BlockCommandSender") != null && sender instanceof org.bukkit.command.BlockCommandSender) {
		    type = commandSenderType.commandblock;
		}
	    } catch (Exception e) {
	    }
	    try {
		if (Class.forName("org.bukkit.command.ProxiedCommandSender") != null && sender instanceof org.bukkit.command.ProxiedCommandSender) {
		    type = commandSenderType.rcon;
		}
	    } catch (Exception e) {
	    }
	}
    }

    public boolean isPlayer() {
	return type.equals(commandSenderType.player);
    }

    public boolean isConsole() {
	return type.equals(commandSenderType.console);
    }

    public boolean isCommandBlock() {
	return type.equals(commandSenderType.commandblock);
    }

    public Player getPlayer() {
	if (!isPlayer())
	    return null;
	return (Player) sender;
    }

    public boolean isValid() {
	return sender != null && !type.equals(commandSenderType.invalid);
    }

    public Location getLocation() {
	if (!isValid())
	    return null;
	if (isPlayer()) {
	    return this.getPlayer().getLocation();
	}

	if (isCommandBlock()) {
	    return ((BlockCommandSender) sender).getBlock().getLocation();
	}
	return null;
    }

    public UUID getUUID() {
	if (!isValid())
	    return null;
	if (isPlayer()) {
	    return this.getPlayer().getUniqueId();
	}

	if (isConsole()) {
	    return CMILib.getInstance().getServerUUID();
	}

	return null;
    }

    public commandSenderType getType() {
	return type;
    }

    public CommandSender getSender() {
	return sender;
    }

    public void setSender(CommandSender sender) {
	this.sender = sender;
    }

    public void sendMessage(String message) {
	this.getSender().sendMessage(message);
    }

    public void performCommand(String command) {
	CMICommands.performCommand(this.getSender(), command);
    }
}
