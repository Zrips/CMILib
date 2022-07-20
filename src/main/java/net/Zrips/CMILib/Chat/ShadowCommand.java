package net.Zrips.CMILib.Chat;

import java.util.HashMap;
import java.util.Map.Entry;

public class ShadowCommand {

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
