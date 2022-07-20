package net.Zrips.CMILib.Container;

public enum LeatherAnimationType {
    Rainbow(1), Health(2), Biome(3), Day(4);

    private Integer id;

    LeatherAnimationType(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public static LeatherAnimationType getById(int id) {
	for (LeatherAnimationType one : values()) {
	    if (one.getId() == id)
		return one;
	}
	return null;
    }

    public static LeatherAnimationType getByName(String name) {
	for (LeatherAnimationType one : values()) {
	    if (one.name().equalsIgnoreCase(name))
		return one;
	}
	return null;
    }

}
