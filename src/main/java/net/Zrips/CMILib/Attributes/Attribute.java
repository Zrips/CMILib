package net.Zrips.CMILib.Attributes;

public class Attribute {

    private AttributeType type;
    private AttSlot slot = null;
    private double mod;
    private int operation;

    @Deprecated
    public Attribute(AttributeType type, AttSlot slot, double mod) {
	this(type, slot, mod, 0);
    }

    public Attribute(AttributeType type, AttSlot slot, double mod, int operation) {
	this.type = type;
	this.slot = slot;
	this.mod = mod;
	this.operation = operation;
    }

    public AttributeType getType() {
	return type;
    }

    public void setType(AttributeType type) {
	this.type = type;
    }

    public AttSlot getSlot() {
	return slot;
    }

    public void setSlot(AttSlot slot) {
	this.slot = slot;
    }

    public double getMod() {
	return mod;
    }

    public void setMod(double mod) {
	this.mod = mod;
    }

    public int getOperation() {
	return operation;
    }

    public void setOperation(int operation) {
	this.operation = operation;
    }

}
