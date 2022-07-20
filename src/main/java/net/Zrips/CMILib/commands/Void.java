package net.Zrips.CMILib.commands;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.FileHandler.ConfigReader;

public class Void implements Cmd {

    @Override
    public void getExtra(ConfigReader c) {
    }

    @Override
    @CAnnotation(others = false)
    public Boolean perform(CMILib plugin, CMICommandSender sender, String[] args) {
	return true;
    }

}
