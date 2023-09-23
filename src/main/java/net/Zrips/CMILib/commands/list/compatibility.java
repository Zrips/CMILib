package net.Zrips.CMILib.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Zrips.CMI.CMI;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Reflections;
import net.Zrips.CMILib.ActionBar.CMIActionBar;
import net.Zrips.CMILib.BossBar.BossBarInfo;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Container.CMIServerProperties;
import net.Zrips.CMILib.Effects.CMIEffect;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticle;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import net.Zrips.CMILib.commands.CAnnotation;
import net.Zrips.CMILib.commands.Cmd;

public class compatibility implements Cmd {
    @Override
    public void getExtra(ConfigReader c) {
    }

    @Override
    @CAnnotation(info = "&eTest compatibility", regVar = { 0 }, consoleVar = { -666 }, hidden = true)
    public Boolean perform(CMILib plugin, CMICommandSender sender, String[] args) {

        Reflections ref = plugin.getReflectionManager();

        RawMessage raw = new RawMessage();
        raw.addText("test");
        ref.textToIChatBaseComponent(raw.getRaw());

        ref.getCurrentTick();

        // Not working on 1.18
//	ref.getItemInfo(1, "strength");

        ref.setServerProperties(CMIServerProperties.motd, Bukkit.getServer().getMotd(), true);

        CMINBT.isNBTSimilar(CMIMaterial.STONE.newItemStack(), CMIMaterial.STONE.newItemStack());

        CMINBT.HideFlag(CMIMaterial.STONE.newItemStack(), 2);

        CMINBT.toJson(CMIMaterial.STONE.newItemStack());

        CMINBT nbt = new CMINBT(CMIMaterial.DIAMOND_SWORD.newItemStack());

        nbt.setBoolean("boolTest", true);
        if (!nbt.getBoolean("boolTest"))
            CMIMessages.consoleMessage("Error boolTest");

        nbt.setByte("byteTest", (byte) 1);
        if (nbt.getByte("byteTest") != 1)
            CMIMessages.consoleMessage("Error byteTest");

        nbt.setInt("intTest", 1);
        if (nbt.getInt("intTest") != 1)
            CMIMessages.consoleMessage("Error intTest");

        nbt.setLong("longTest", 1L);
        if (nbt.getLong("longTest") != 1)
            CMIMessages.consoleMessage("Error longTest");

        nbt.setShort("shortTest", (short) 1);
        if (nbt.getShort("shortTest") != 1)
            CMIMessages.consoleMessage("Error shortTest");

        ItemStack item = (ItemStack) nbt.setString("stringTest", "test");
        if (!nbt.getString("stringTest").equals("test"))
            CMIMessages.consoleMessage("Error stringTest");

        Player player = Bukkit.getOnlinePlayers().iterator().next();
        ref.getProfile(player);
        ref.getCraftPlayer(player);
        ref.getPlayerHandle(player);
        ref.getPlayerConnection(player);
        ref.getTileEntityAt(player.getLocation().clone().add(0, -1, 0));

        CMINBT blockNBT = new CMINBT(player.getLocation().clone().add(0, -1, 0).getBlock());

        CMINBT entityNBT = new CMINBT(player);

        nbt.getKeys();

//        AdvancementManager.sendToast(player, "Test", CMIMaterial.ACACIA_BOAT.newCMIItemStack(), FrameType.CHALLENGE);

        CMITitleMessage.send(player, "Title", "SubTitle");
        CMIActionBar.send(player, "Action bar Test");

        plugin.getReflectionManager().playEffect(player, player.getEyeLocation(), new CMIEffect(CMIParticle.COLOURED_DUST));

        RawMessage rm = new RawMessage();
        rm.addText("Active container ID: " + plugin.getReflectionManager().getActiveContainerId(player));
        rm.addHover("Hover text");
        rm.show(player);

        try {
            CMI.getInstance().getReflectionManager().changePlayerLimit(Bukkit.getMaxPlayers());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            CMI.getInstance().getNMS().showResurection(player);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        BossBarInfo bossbar = new BossBarInfo(player, "Compatibility");
        bossbar.setTitleOfBar("Test");
        bossbar.setPercentage(0.33);
        plugin.getBossBarManager().Show(bossbar);

        // Check this
        // ref.setSkullTexture(item, customProfileName, texture)	
        // ref.updateTileEntity(loadValue, tag);

        return true;
    }
}
