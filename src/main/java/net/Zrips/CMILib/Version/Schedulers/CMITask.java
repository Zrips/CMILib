package net.Zrips.CMILib.Version.Schedulers;

import org.bukkit.plugin.Plugin;

public interface CMITask {
    boolean isCancelled();

    void cancel();

    Plugin getPlugin();
}
