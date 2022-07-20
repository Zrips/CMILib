package net.Zrips.CMILib.Time;

public class CMITimeRate {

    private int everyTicks = 1;
    private int addAmount = 1;
    private timeState state = timeState.day;
    private boolean turnCycleOff = false;

    public CMITimeRate(timeState state, int everyTicks, int addAmount) {
	this.state = state;
	this.everyTicks = everyTicks;
	this.addAmount = addAmount;
    }

    public int getAddAmount() {
	return addAmount;
    }

    public void setAddAmount(int addAmount) {
	this.addAmount = addAmount;
    }

    public int getEveryTicks() {
	return everyTicks;
    }

    public void setEveryTicks(int everyTicks) {
	this.everyTicks = everyTicks;
    }

    public timeState getState() {
	return state;
    }

    public void setState(timeState state) {
	this.state = state;
    }

    public boolean isTurnCycleOff() {
	return turnCycleOff;
    }

    public void setTurnCycleOff(boolean turnCycleOff) {
	this.turnCycleOff = turnCycleOff;
    }

}
