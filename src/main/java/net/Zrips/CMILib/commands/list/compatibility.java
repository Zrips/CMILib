package net.Zrips.CMILib.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Reflections;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Container.CMIServerProperties;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.RawMessages.RawMessage;
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
	Player player = Bukkit.getOnlinePlayers().iterator().next();
	ref.getProfile(player);
	ref.getCraftPlayer(player);
	ref.getPlayerHandle(player);
	ref.getPlayerConnection(player);
	ref.getTileEntityAt(player.getLocation().clone().add(0, -1, 0));

	CMINBT.toJson(CMIMaterial.STONE.newItemStack());

	CMINBT nbt = new CMINBT(CMIMaterial.DIAMOND_SWORD.newItemStack());

	nbt.setBoolean("boolTest", true);
	nbt.getBoolean("boolTest");

	nbt.setByte("byteTest", (byte) 1);
	nbt.getByte("byteTest");

	nbt.setInt("intTest", 1);
	nbt.getInt("intTest");

	nbt.setLong("longTest", 1L);
	nbt.getLong("longTest");

	nbt.setShort("shortTest", (short) 1);
	nbt.getShort("shortTest");

	ItemStack item = (ItemStack) nbt.setString("stringTest", "test");
	nbt.getString("stringTest");
	
	nbt.getKeys();
	
	// Check this
	// ref.setSkullTexture(item, customProfileName, texture)	
	// ref.updateTileEntity(loadValue, tag);

	return true;
    }
}
