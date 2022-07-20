package net.Zrips.CMILib.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class Snd {
    private Player sender;
    private String senderName;
    private ConsoleCommandSender console;
    private Player target;
    private String targetName;
    private ConsoleCommandSender targetConsole;
    private Player source;
    private ConsoleCommandSender sourceConsole;

    public Snd() {

    }

    public Snd(Player sender, Player target) {
	this.sender = sender;
	if (sender != null)
	    senderName = sender.getName();
	this.target = target;
	if (target != null)
	    targetName = target.getName();
    }

    public Player getPlayerSender() {
	return sender;
    }

    public ConsoleCommandSender getConsoleSender() {
	return console;
    }

    public Snd setSender(Player sender) {
	this.sender = sender;
	if (sender != null)
	    senderName = sender.getName();
	return this;
    }

    public Snd setSender(CommandSender sender) {
	if (sender instanceof Player) {
	    this.sender = (Player) sender;
	    senderName = sender.getName();
	} else if (sender instanceof ConsoleCommandSender)
	    this.console = (ConsoleCommandSender) sender;
	else if (sender instanceof BlockCommandSender)
	    this.console = Bukkit.getConsoleSender();
	else if (sender instanceof RemoteConsoleCommandSender)
	    this.console = Bukkit.getConsoleSender();
	return this;
    }

    public Snd setSender(ConsoleCommandSender console) {
	this.console = console;
	return this;
    }

    public Player getPlayerTarget() {
	return target;
    }

    public ConsoleCommandSender getConsoleTarget() {
	return targetConsole;
    }

    public Snd setTarget(Player sender) {
	this.target = sender;
	return this;
    }

    public Snd setTarget(CommandSender sender) {
	if (sender instanceof Player) {
	    this.target = (Player) sender;
	} else if (sender instanceof ConsoleCommandSender) {
	    this.targetConsole = (ConsoleCommandSender) sender;
	} else if (sender instanceof BlockCommandSender) {
	    this.targetConsole = Bukkit.getConsoleSender();
	} else if (sender instanceof RemoteConsoleCommandSender) {
	    this.targetConsole = Bukkit.getConsoleSender();
	}
	return this;
    }

    public Snd setTarget(ConsoleCommandSender console) {
	this.targetConsole = console;
	return this;
    }

    public Player getPlayerSource() {
	return source;
    }

    public ConsoleCommandSender getConsoleSource() {
	return sourceConsole;
    }

    public Snd setSource(Player sender) {
	this.source = sender;
	return this;
    }

    public Snd setSource(CommandSender sender) {
	if (sender instanceof Player)
	    this.source = (Player) sender;
	else if (sender instanceof ConsoleCommandSender)
	    this.sourceConsole = (ConsoleCommandSender) sender;
	else if (sender instanceof BlockCommandSender)
	    this.sourceConsole = Bukkit.getConsoleSender();
	else if (sender instanceof RemoteConsoleCommandSender)
	    this.sourceConsole = Bukkit.getConsoleSender();
	return this;
    }

    public Snd setSource(ConsoleCommandSender console) {
	this.sourceConsole = console;
	return this;
    }

    public String getSenderName() {
	return senderName;
    }

    public Snd setSenderName(String senderName) {
	this.senderName = senderName;
	return this;
    }

    public String getTargetName() {
	return targetName;
    }

    public Snd setTargetName(String targetName) {
	this.targetName = targetName;
	return this;
    }
}
