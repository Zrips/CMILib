package net.Zrips.CMILib.Items;

import org.bukkit.inventory.ItemStack;

public class CMIAsyncHead {
    private boolean head = false;
    private boolean force = false;
    
    private boolean ignoreCached = false;

    public void afterAsyncUpdate(ItemStack item) {

    }

    public boolean isAsyncHead() {
	return head;
    }

    public void setAsyncHead(boolean head) {
	this.head = head;
    }

    public boolean isForce() {
	return force;
    }

    public void setForce(boolean force) {
	this.force = force;
    }

    public boolean isIgnoreCached() {
	return ignoreCached;
    }

    public void setIgnoreCached(boolean ignoreCached) {
	this.ignoreCached = ignoreCached;
    }
}
