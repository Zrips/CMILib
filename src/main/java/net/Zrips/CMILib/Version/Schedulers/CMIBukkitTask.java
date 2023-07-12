package net.Zrips.CMILib.Version.Schedulers;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CMIBukkitTask implements CMITask {
    private final BukkitTask task;

    public CMIBukkitTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    public Plugin getPlugin() {
        return this.task.getOwner();
    }
}
