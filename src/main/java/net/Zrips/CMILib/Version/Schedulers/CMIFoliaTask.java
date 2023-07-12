package net.Zrips.CMILib.Version.Schedulers;

import org.bukkit.plugin.Plugin;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public class CMIFoliaTask implements CMITask {
    private final ScheduledTask task;

    public CMIFoliaTask(ScheduledTask task) {
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
        return this.task.getOwningPlugin();
    }

    @Override
    public int getTaskId() {
        return this.getTaskId();
    }
}
