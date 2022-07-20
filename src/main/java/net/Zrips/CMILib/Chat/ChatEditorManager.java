package net.Zrips.CMILib.Chat;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;

public class ChatEditorManager {
    static HashMap<UUID, ChatMessageEdit> map = new HashMap<UUID, ChatMessageEdit>();

    public static final String questionMarkReplacer = "<>-<>";
    
    public static void add(ChatMessageEdit rmc) {
	map.put(rmc.getUuid(), rmc);
    }

    public static void delete(ChatMessageEdit rmc) {
	map.remove(rmc.getUuid());
    }


    public static boolean perform(CommandSender sender, String message) {

	UUID uuid = sender instanceof Player ? ((Player) sender).getUniqueId() : CMILib.getInstance().getServerUUID();

	ChatMessageEdit rmc = map.get(uuid);
	if (rmc == null)
	    return false;

	if (rmc.isCheckForCancel() && CMIChatColor.stripColor(message).equalsIgnoreCase(rmc.getCancelVariable())) {
	    remove(uuid);
	    rmc.onCancel();
	    return true;
	}
	rmc.run(message);
	if (!rmc.isKeep())
	    remove(uuid);
	return true;
    }

    public static void remove(UUID uuid) {
	ChatMessageEdit rmc = map.remove(uuid);
	if (rmc != null)
	    rmc.onDisable();
    }
}
