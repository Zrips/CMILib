package net.Zrips.CMILib.Chat;

public enum listMoveDirection {
    Up(-1), Down(1);

    private int dir;

    listMoveDirection(int dir) {
	this.dir = dir;
    }

    public int getDir() {
	return dir;
    }

    public void setDir(int dir) {
	this.dir = dir;
    }
}