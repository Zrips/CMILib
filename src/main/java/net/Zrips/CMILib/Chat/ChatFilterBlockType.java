package net.Zrips.CMILib.Chat;

public enum ChatFilterBlockType {
    All(0), Others(1), None(2);

    private int id;

    ChatFilterBlockType(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }
}
