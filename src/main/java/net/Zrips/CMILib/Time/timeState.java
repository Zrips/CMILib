package net.Zrips.CMILib.Time;

import org.bukkit.World;

public enum timeState {

    day(0, 12000, 600), sunset(12000, 13800, 90), night(13800, 22200, 420), sunrise(22200, 24000, 90);

    private int from;
    private int until;
    private int defaultDuration;

    timeState(int from, int until, int defaultDuration) {
	this.from = from;
	this.until = until;
	this.defaultDuration = defaultDuration;
    }

    public int getFrom() {
	return from;
    }

    public int getUntil() {
	return until;
    }

    public static timeState getTimeState(World world) {
	return getTimeState((int) world.getTime());
    }

    public static timeState getTimeState(int ticks) {
	for (timeState one : timeState.values()) {
	    if (one.getFrom() <= ticks && one.getUntil() >= ticks)
		return one;
	}
	return timeState.day;
    }

    public int getDefaultDuration() {
	return defaultDuration;
    }

}
