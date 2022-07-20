package net.Zrips.CMILib.Logs;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Messages.CMIMessages;

public class CMILogManager {

//    Set<CMILogType> enabledLogs = new HashSet<CMILogType>();

    boolean enabledDebug = false;

    public CMILogManager() {
	enabledDebug = CMILib.getInstance().getCommandManager().isTestServer();
    }

    public void print(CMIDebug log) {
//	if (log.getType() != null && !enabledLogs.contains(log.getType()))
//	    return;
	
	CMIMessages.consoleMessage(log.getMsg());
    }

}
