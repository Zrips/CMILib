package net.Zrips.CMILib.BossBar;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.Zrips.CMILib.events.EventAnnotation;

public final class CMIBossBarHideEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private BossBarInfo bossBar = null;

    public CMIBossBarHideEvent(BossBarInfo bossBar) {
	this.bossBar = bossBar;
    }

    @Override
    @EventAnnotation(info = "Fired when CMI boss bar should be hidden. Can be canceled.")
    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }
    @Override
    public final void setCancelled(final boolean cancel) {
	this.cancel = cancel;
    }

    @Override
    public final boolean isCancelled() {
	return cancel;
    }

    public BossBarInfo getBossBar() {
	return bossBar;
    }

    public void setBossBar(BossBarInfo bossBar) {
	this.bossBar = bossBar;
    }
}