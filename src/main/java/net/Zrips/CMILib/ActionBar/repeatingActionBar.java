package net.Zrips.CMILib.ActionBar;

import org.bukkit.Bukkit;

public class repeatingActionBar {

    private int id = -1;
    private Long until = 0L;

    public int getId() {
	return id;
    }

    public void cancel() {
	if (id > 0)
	    Bukkit.getScheduler().cancelTask(id);
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
