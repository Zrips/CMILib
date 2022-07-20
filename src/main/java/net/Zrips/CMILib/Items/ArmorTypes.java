package net.Zrips.CMILib.Items;

import org.bukkit.inventory.ItemStack;

public enum ArmorTypes {
    HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8), ELYTRA(6), SHIELD(9);

    private final int slot;

    ArmorTypes(int slot) {
	this.slot = slot;
    }

    public final static ArmorTypes matchType(final ItemStack itemStack) {
	if (itemStack == null)
	    return null;
	CMIMaterial cmat = CMIMaterial.get(itemStack);
	
	if (cmat.isHelmet() || cmat.isSkull())
	    return HELMET;
	if (cmat.isChestplate())
	    return CHESTPLATE;
	if (cmat.isLeggings())
	    return LEGGINGS;
	if (cmat.isBoots())
	    return BOOTS;
	if (cmat.isShield())
	    return SHIELD;
	
	switch (cmat) {
	case ELYTRA:
	    return ELYTRA;
	default:
	    return null;
	}
    }

    public int getSlot() {
	return slot;
    }
}
