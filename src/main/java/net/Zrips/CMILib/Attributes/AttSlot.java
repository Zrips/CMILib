package net.Zrips.CMILib.Attributes;

public enum AttSlot {
    Main_hand("mainhand"),
    Off_hand("offhand"),
    Head("head"),
    Chest("chest"),
    Legs("legs"),
    Feet("feet");
    
    private String name;
    private AttSlot(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static AttSlot get(String code) {
	for (AttSlot e : AttSlot.values()) {
	    if (code.equalsIgnoreCase(e.name))
		return e;
	}
	return null;
    }
}
