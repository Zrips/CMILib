package net.Zrips.CMILib.Logs;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Version;

public class CMILogManager {

//    Set<CMILogType> enabledLogs = new HashSet<CMILogType>();

    boolean enabledDebug = false;

    public CMILogManager() {
	enabledDebug = Version.isTestServer();
    }

    public void print(CMIDebug log) {
//	if (log.getType() != null && !enabledLogs.contains(log.getType()))
//	    return;
	
	CMIMessages.consoleMessage(log.getMsg());
    }

}
