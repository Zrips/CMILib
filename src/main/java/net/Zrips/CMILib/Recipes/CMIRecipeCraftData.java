package net.Zrips.CMILib.Recipes;

import net.Zrips.CMILib.Version.Version;

public class CMIRecipeCraftData {

    private float exp = 0;
    private int cookingTime = 20;

    public CMIRecipeCraftData(float exp, int cookingTime) {
	this.exp = exp;
	this.cookingTime = cookingTime;
    }

    public float getExp() {
	return exp;
    }

    public void setExp(float exp) {
	if (Version.isCurrentEqualOrHigher(Version.v1_17_R1) && exp > 2)
	    exp = 2;
	else if (!Version.isCurrentEqualOrHigher(Version.v1_17_R1) && exp > 1)
	    exp = 1;
	if (exp < 0)
	    exp = 0;
	this.exp = exp;
    }

    public int getCookingTime() {
	return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
	this.cookingTime = cookingTime;
    }

}
