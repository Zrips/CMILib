package net.Zrips.CMILib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Container.CMITabComplete;
import net.Zrips.CMILib.Container.TabAction;
import net.Zrips.CMILib.commands.CommandsHandler;

public class TabComplete implements TabCompleter {

    private CMILib plugin;

    public TabComplete(CMILib plugin) {
	this.plugin = plugin;

	for (TabAction one : TabAction.values()) {
	    if (one.equals(TabAction.na))
		continue;
	    map.put("[" + one.name().toLowerCase() + "]", one);
	}

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
	List<String> completionList = get(new CMICommandSender(sender), command.getName(), label, args);
	Collections.sort(completionList);	
	return completionList;
    }

    public static HashMap<String, TabAction> map = new HashMap<String, TabAction>();

    public static CMITabComplete tabs = new CMITabComplete();

    public List<Object> getTabCompleteList(String[] args) {
	return tabs.getTabCompleteList(args);
    }

    public String getTabComplete(String tab) {
	return tabs.getTabComplete(tab);
    }

    public void addTabComplete(String tab) {
	tabs.addTabComplete(tab);
    }

    @SuppressWarnings("deprecation")
    public List<String> get(CMICommandSender sender, String command, String label, String[] args) {
	List<String> completionList = new ArrayList<>();

	if (args.length > 1 && args[args.length - 2].isEmpty()) {
	    return completionList;
	}

	String orCommand = command;
	String or2Command = command;

	if (args.length == 1) {
	    String PartOfCommand = args[0];
	    List<String> temp = new ArrayList<String>();
	    for (Entry<String, Integer> BCmd : CommandsHandler.getCommands(sender).entrySet()) {
		temp.add(BCmd.getKey());
	    }
	    StringUtil.copyPartialMatches(PartOfCommand, temp, completionList);
	}

	if (args.length == 0)
	    return completionList;

	int i = args.length - 1;

	String PartOfCommand = args[i];
	List<Object> ArgsList = new ArrayList<Object>();

	ArgsList = this.getTabCompleteList(args);

	if (ArgsList.size() < i)
	    return completionList;

	if (ArgsList.isEmpty() || i < 1)
	    return completionList;

	String SAction = null;
	List<TabAction> actions = new ArrayList<TabAction>();

	if (i - 1 < ArgsList.size()) {
	    Object arg = ArgsList.get(i - 1);
	    if (arg instanceof TabAction)
		actions.add((TabAction) arg);
	    else
		SAction = (String) arg;
	}

	List<String> temp = new ArrayList<String>();

	if (SAction != null) {

	    if (SAction.contains("%%")) {
		String[] split = SAction.split("%%");
		for (String oneS : split) {
		    if (TabAction.getAction(oneS) != TabAction.na) {

			actions.add(TabAction.getAction(oneS));
			continue;
		    }

		    if (oneS.equalsIgnoreCase("colors")) {
			temp.addAll(getColorNames());
		    } else {
			if (oneS.startsWith("_"))
			    oneS = oneS.substring(1);
			temp.add(oneS);
		    }
		}
	    } else {
		TabAction action = TabAction.getAction(SAction);
		if (action != TabAction.na) {
		    actions.add(action);
		} else {
		    temp.add(SAction);
		}
	    }
	}

	for (TabAction action : actions) {
	    temp.addAll(getByAction(sender, action, args, i));
	}

	StringUtil.copyPartialMatches(PartOfCommand, temp, completionList);

	return completionList;

    }

    public List<String> getByAction(CMICommandSender sender, TabAction action, String[] args, int i) {
	List<String> temp = new ArrayList<String>();
	switch (action) {
	default:
	    break;
	}
	return temp;
    }

    private List<String> getColorNames() {
	List<String> list = new ArrayList<String>();
	for (ChatColor one : ChatColor.values()) {
	    if (one == null ||
		one.name() == null ||
		one == ChatColor.ITALIC ||
		one == ChatColor.BOLD ||
		one == ChatColor.MAGIC ||
		one == ChatColor.STRIKETHROUGH ||
		one == ChatColor.UNDERLINE ||
		one == ChatColor.RESET)
		continue;
	    list.add(one.name().toLowerCase());
	}
	return list;
    }
}
