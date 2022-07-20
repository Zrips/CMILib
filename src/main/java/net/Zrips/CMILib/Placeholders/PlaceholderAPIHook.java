package net.Zrips.CMILib.Placeholders;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Placeholders.Placeholder.CMIPlaceHolders;
import net.Zrips.CMILib.commands.CommandsHandler;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private CMILib plugin;

    public PlaceholderAPIHook(CMILib plugin) {
	this.plugin = plugin;
    }

    @Override
    public boolean persist() {
	return true;
    }

    @Override
    public boolean canRegister() {
	return true;
    }

    @Override
    public String getAuthor() {
	return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
	return CommandsHandler.getLabel();
    }

    @Override
    public String getVersion() {
	return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

	if (player == null) {
	    return "";
	}

	CMIPlaceHolders placeHolder = CMIPlaceHolders.getByName("%" + CommandsHandler.getLabel() + "_" + identifier + "%");
	if (placeHolder == null) {
	    return null;
	}
	return plugin.getPlaceholderAPIManager().translateOwnPlaceHolder(player, "%cmi_" + identifier + "%");
    }
}