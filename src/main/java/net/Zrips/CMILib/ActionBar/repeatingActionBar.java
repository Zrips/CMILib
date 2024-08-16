package net.Zrips.CMILib.ActionBar;

import net.Zrips.CMILib.Version.Schedulers.CMITask;

public class repeatingActionBar {

    private CMITask scheduler = null;
    private Long until = 0L;

    public CMITask getScheduler() {
        return scheduler;
    }

    public void cancel() {
        if (scheduler != null)
            scheduler.cancel();
    }

    public void setScheduler(CMITask cmiTask) {
        this.scheduler = cmiTask;
    }

    public Long getUntil() {
        return until;
    }

    public void setUntil(Long until) {
        this.until = until;
    }

}
