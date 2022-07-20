package net.Zrips.CMILib.commands.list;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.commands.CAnnotation;
import net.Zrips.CMILib.commands.Cmd;

public class reload implements Cmd {
    @Override
    public void getExtra(ConfigReader c) {
	c.get("feedback", "&6CMIL Configs and locale files reloaded! Took [ms]ms");
	c.get("failedConfig", "&4Failed to load config file! Check spelling!");
	c.get("failedLocale", "&4Failed to load locale file! Check spelling!");
    }

    @Override
    @CAnnotation(info = "&eReloads plugins config and locale files", regVar = { 0 }, consoleVar = { 0 }, others = false)
    public Boolean perform(CMILib plugin, CMICommandSender sender, String[] args) {
	plugin.getConfigManager().reload(sender.getSender());
	return true;
    }
}
