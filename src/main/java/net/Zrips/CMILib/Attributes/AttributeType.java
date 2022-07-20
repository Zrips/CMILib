package net.Zrips.CMILib.Attributes;

public enum AttributeType {
    Armor("generic", "armor", 0),
    ArmorToughness("generic", "armor_Toughness", 0),
    AttackDamage("generic", "attack_Damage", 0),
    AttackKnockback("generic", "attack_Knockback", 0),
    AttackSpeed("generic", "attack_Speed", 2),
    FlyingSpeed("generic", "flying_Speed", 0),
    FollowRange("generic", "follow_Range", 0),
    KnockbackResistance("generic", "knockback_Resistance", 0),
    Luck("generic", "luck", 0),
    MaxHealth("generic", "max_Health", 0),
    MovementSpeed("generic", "movement_Speed", 1),
    JumpHeight("horse", "jump_Height", 0),
    SpawnReinforcements("zombie", "spawn_Reinforcements", 0);

    // Attributes introduced in 1.9

    private String name;
    private String preName;
    private int action;

    private AttributeType(String preName, String name, int action) {
	this.preName = preName;
	this.name = name;
	this.action = action;
    }

    public String getPreName() {
	return preName;
    }

    public String getFullName() {
	return preName + "." + name.replace("_", "");
    }

    public String getName() {
	return name.replace("_", "");
    }

    public String getIdentificator() {
	return preName.toUpperCase() + "_" + name.toUpperCase();
    }

    public static AttributeType get(String code) {
	for (AttributeType e : AttributeType.values()) {
	    if (code.equalsIgnoreCase(e.getName()))
		return e;
	}
	return null;
    }

    public int getAction() {
	return action;
    }

}
