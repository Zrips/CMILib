package net.Zrips.CMILib.commands;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.FileHandler.ConfigReader;

public interface Cmd {
    public Boolean perform(CMILib plugin, CMICommandSender sender, String[] args);

    void getExtra(ConfigReader c);
}
