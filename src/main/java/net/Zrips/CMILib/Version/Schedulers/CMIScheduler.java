package net.Zrips.CMILib.Version.Schedulers;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Version.Version;

public class CMIScheduler {

    private static final CMIBaseImpl implementation;

    static {
        switch (Version.getPlatform()) {
        case folia:
            implementation = new CMIFoliaImpl(CMILib.getInstance());
            break;
        case craftbukkit:
        case paper:
        case pufferfish:
        case spigot:
        default:
            implementation = new CMIBukkitImpl(CMILib.getInstance());
            break;
        }
    }

    public static CMIBaseImpl get() {
        return implementation;
    }

    public static void cancelTask(int id) {
        CMIScheduler.cancelTask(id);        
    }
}
