package net.Zrips.CMILib.Shadow;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.Zrips.CMILib.commands.CommandsHandler;

public class ShadowCommand {
    
    public static String prefix = CommandsHandler.getLabel();

    private static HashMap<UUID, ShadowCommand> shadowCommand = new HashMap<UUID, ShadowCommand>();

    public static String addShadowCmd(Player player, String cmd, Boolean infinite, ShadowCommandType type) {
	String id = String.valueOf((new Random()).nextInt(Integer.MAX_VALUE)) + (infinite ? "i" : "");
	ShadowCommand shadow = shadowCommand.getOrDefault(player.getUniqueId(), new ShadowCommand());

	id = shadow.add(id, cmd, type);
	shadowCommand.put(player.getUniqueId(), shadow);
	return id;
    }

    public static ShadowCommandInfo getShadowCommand(Player player, String id) {
	ShadowCommand shadow = shadowCommand.get(player.getUniqueId());
	if (shadow == null)
	    return null;
	return shadow.get(id);
    }
    
    HashMap<String, ShadowCommandInfo> map = new HashMap<String, ShadowCommandInfo>();

    public ShadowCommand() {
    }

    public String add(String id, String consoleCommand, ShadowCommandType type) {
	ShadowCommandInfo cmd = new ShadowCommandInfo(type, consoleCommand);
	if (!id.contains("!"))
	    for (Entry<String, ShadowCommandInfo> one : map.entrySet()) {
		if (!one.getValue().getCmd().equals(consoleCommand))
		    continue;
		id = one.getKey();
	    }
	map.put(id, cmd);
	return id;
    }

    public ShadowCommandInfo get(String id) {
	ShadowCommandInfo cmd = map.get(id);
	if (cmd != null && !id.endsWith("i"))
	    return map.remove(id);
	return cmd;
    }
}
