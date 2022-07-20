package net.Zrips.CMILib.Scoreboards;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardInfo {

    private Scoreboard scoreBoard;
    private Objective obj;
    private Long time;
    private long keepSeconds;
    

    public ScoreboardInfo(Scoreboard scoreBoard, DisplaySlot slot, long keepSeconds) {
	this.scoreBoard = scoreBoard;
	this.keepSeconds = keepSeconds;

	for (Objective one : this.scoreBoard.getObjectives()) {
	    if (one.getDisplaySlot() == slot)
		obj = one;
	}

	time = System.currentTimeMillis();
    }

    public Scoreboard getScoreBoard() {
	return scoreBoard;
    }

    public void setScoreBoard(Scoreboard scoreBoard) {
	this.scoreBoard = scoreBoard;
    }

    public Long getTime() {
	return time;
    }

    public void setTime(Long time) {
	this.time = time;
    }

    public Objective getObj() {
	return obj;
    }

    public void setObj(Objective obj) {
	this.obj = obj;
    }

    public long getKeepSeconds() {
	return keepSeconds;
    }

    public void setKeepSeconds(long keepSeconds) {
	this.keepSeconds = keepSeconds;
    }

}
