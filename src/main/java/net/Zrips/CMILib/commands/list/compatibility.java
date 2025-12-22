package net.Zrips.CMILib.commands.list;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Reflections;
import net.Zrips.CMILib.ActionBar.CMIActionBar;
import net.Zrips.CMILib.Advancements.AdvancementBackground;
import net.Zrips.CMILib.Advancements.AdvancementFrameType;
import net.Zrips.CMILib.Advancements.CMIAdvancement;
import net.Zrips.CMILib.BossBar.BossBarInfo;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Container.CMINumber;
import net.Zrips.CMILib.Container.CMIServerProperties;
import net.Zrips.CMILib.Effects.CMIEffect;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticle;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Items.CMIItemStack;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.Scoreboards.CMIScoreboard;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import net.Zrips.CMILib.Version.Version;
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
        try {
            ref.textToIChatBaseComponent(raw.getRaw());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            ref.getCurrentTick();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // Not working on 1.18
//	ref.getItemInfo(1, "strength");

        try {
            ref.setServerProperties(CMIServerProperties.motd, Bukkit.getServer().getMotd(), true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Player player = Bukkit.getOnlinePlayers().iterator().next();
        ref.getProfile(player);
        ref.getCraftPlayer(player);
        ref.getPlayerHandle(player);
        ref.getPlayerConnection(player);
        ref.getTileEntityAt(player.getLocation().clone().add(0, -1, 0));

        player.getInventory().addItem(CMINBT.HideFlag(CMIMaterial.STONE.newItemStack(), 2));

        if (Version.isCurrentLower(Version.v1_21_R5)) {
            CMIMessages.sendMessage(sender, CMINBT.toJson(CMIMaterial.STONE.newItemStack()));
            CMINBT.isNBTSimilar(CMIMaterial.STONE.newItemStack(), CMIMaterial.STONE.newItemStack());

            try {
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

                CMINBT blockNBT = new CMINBT(player.getLocation().clone().add(0, -1, 0).getBlock());

                CMINBT entityNBT = new CMINBT(player);

                nbt.getKeys();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        CMITitleMessage.send(player, "Title", "SubTitle");
        CMIActionBar.send(player, "Action bar Test");
        CMIScoreboard.show(player, "Top line", Arrays.asList("first", "second"), 2);

        new CMIAdvancement().setItem(CMIMaterial.TOTEM_OF_UNDYING.newItemStack()).setTitle("Test Title").show(player);

        new CMIEffect(CMIParticle.DUST).show(player, player.getEyeLocation());

        try {
            RawMessage rm = new RawMessage();
            rm.addText("Active container ID: " + plugin.getReflectionManager().getActiveContainerId(player));
            rm.addHover("Hover text");
            rm.addCommand("cmi heal");
            rm.show(player);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            CMILib.getInstance().getReflectionManager().setServerProperties(CMIServerProperties.max_players, Bukkit.getMaxPlayers(), true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        BossBarInfo bossbar = plugin.getBossBarManager().getBossBar(player, "Compatibility");
        if (bossbar == null)
            bossbar = new BossBarInfo(player, "Compatibility");
        bossbar.setTitleOfBar("Test");
        bossbar.setPercentage(new Random().nextDouble());
        plugin.getBossBarManager().Show(bossbar);

        if (Version.isCurrentLower(Version.v1_12_R1)) {

            CMIAdvancement advancement = new CMIAdvancement()
                    .setId(new org.bukkit.NamespacedKey(plugin, "cmi/commandToast"))
                    .setDescription("_")
                    .setAnnounce(false)
                    .setHidden(false)
                    .setToast(true)
                    .setBackground(AdvancementBackground.ADVENTURE)
                    .setFrame(AdvancementFrameType.TASK)
                    .setTitle("Toast Test " + CMINumber.random(0, 1000));
            try {
                advancement.setItem(CMIItemStack.deserialize("leatherhelmet;purple").getItemStack());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            advancement.show(player);
        }

        // Check this
        // ref.setSkullTexture(item, customProfileName, texture)
        // ref.updateTileEntity(loadValue, tag);

        return true;
    }
}
