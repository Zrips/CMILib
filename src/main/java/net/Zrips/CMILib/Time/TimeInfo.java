package net.Zrips.CMILib.Time;

public class TimeInfo {

    private int hours = -1;
    private int minutes = 0;
    private int seconds = 0;
    private int ticks = -1;
    private boolean byTicks = false;

    public TimeInfo() {
    }

    public boolean isByTicks() {
	return byTicks;
    }

    public void setByTicks() {
	this.byTicks = true;
    }

    public int getHours() {
	return hours;
    }

    public void setHours(int hours) {
	this.hours = hours;
    }

    public int getMinutes() {
	return minutes;
    }

    public void setMinutes(int minutes) {
	this.minutes = minutes;
    }

    public int getSeconds() {
	return seconds;
    }

    public void setSeconds(int seconds) {
	this.seconds = seconds;
    }

    public int getTicks() {
	return ticks;
    }

    public void setTicks(int ticks) {
	this.ticks = ticks;
    }
}
