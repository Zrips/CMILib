package net.Zrips.CMILib.NBT;

public enum CMINBTListType {
    END(0), BYTE(1), SHORT(2), INT(3), LONG(4), FLOAT(5), DOUBLE(6), BYTE_(7), STRING(8), LIST(9), COMPOUND(10), INT_(11);

    private int id;

    CMINBTListType(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public static String getType(int id) {
	for (CMINBTListType one : CMINBTListType.values()) {
	    if (one.getId() == id) {
		String name = one.name();
		if (name.endsWith("_"))
		    name = name.substring(0, name.length() - 1) + "[]";
		return name.substring(0, 1) + name.substring(1, name.length()).toLowerCase();
	    }
	}
	return null;
    }
}
