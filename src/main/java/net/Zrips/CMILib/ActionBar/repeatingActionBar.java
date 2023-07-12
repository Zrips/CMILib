package net.Zrips.CMILib.ActionBar;

import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class repeatingActionBar {

    private int id = -1;
    private Long until = 0L;

    public int getId() {
	return id;
    }

    public void cancel() {
	if (id > 0)
	    CMIScheduler.cancelTask(id);
    }

    public void setId(int id) {
	this.id = id;
    }

    public Long getUntil() {
	return until;
    }

    public void setUntil(Long until) {
	this.until = until;
    }

}
