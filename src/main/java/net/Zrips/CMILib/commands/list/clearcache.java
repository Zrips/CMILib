package net.Zrips.CMILib.commands.list;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Entities.CMIEntityType;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.commands.CAnnotation;
import net.Zrips.CMILib.commands.Cmd;

public class clearcache implements Cmd {
    @Override
    public void getExtra(ConfigReader c) {
    }

    @Override
    @CAnnotation(info = "&eClear cache", regVar = { 0 }, consoleVar = { 0 }, others = false)
    public Boolean perform(CMILib plugin, CMICommandSender sender, String[] args) {
        plugin.getItemManager().clearHeadCache();
        CMIEntityType.cache.clear();
        return true;
    }
}
