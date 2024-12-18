package net.Zrips.CMILib.Recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CMIRecipeType {
    Shaped(Arrays.asList(11, 12, 13, 20, 21, 22, 29, 30, 31)),
    Shapeless(Arrays.asList(11, 12, 13, 20, 21, 22, 29, 30, 31)),
    Transmute(Arrays.asList(11, 12, 13, 20, 21, 22, 29, 30, 31)),
    Furnace(20, true),
    Blasting(20, true),
    Campfire(20, true),
    Cooking(20, true),
    Merchant(20),
    Smoking(20, true),
    Stonecutting(20),
    Smithing(2, Arrays.asList(20, 21)),
    Complex(20),
    Unknown(20);

    private List<Integer> ingredientSlots = new ArrayList<Integer>();
    private int minSlots = 1;
    private boolean timeAndExp = false;

    CMIRecipeType(int minSlots, List<Integer> ingredientSlots) {
        this.ingredientSlots = ingredientSlots;
        this.minSlots = minSlots;
    }

    CMIRecipeType(int ingredientSlot, boolean timeAndExp) {
        this.ingredientSlots.clear();
        this.ingredientSlots.add(ingredientSlot);
        this.timeAndExp = timeAndExp;
    }

    CMIRecipeType(int ingredientSlot) {
        this.ingredientSlots.clear();
        this.ingredientSlots.add(ingredientSlot);
    }

    CMIRecipeType(List<Integer> ingredientSlots) {
        this.ingredientSlots = ingredientSlots;
    }

    public static CMIRecipeType getByName(String name) {
        name = name.toLowerCase();
        name = name.endsWith("recipe") ? name.substring(0, name.length() - "recipe".length()) : name;
        name = name.startsWith("craft") ? name.substring("craft".length(), name.length()) : name;
        name = name.endsWith("transform") ? name.substring(0, name.length() - "transform".length()) : name;
        for (CMIRecipeType one : CMIRecipeType.values()) {
            if (one.name().equalsIgnoreCase(name))
                return one;
        }
        return CMIRecipeType.Unknown;
    }

    public List<Integer> getIngredientSlots() {
        return ingredientSlots;
    }

    public int getMinSlots() {
        return minSlots;
    }

    public boolean isTimeAndExp() {
        return timeAndExp;
    }
}
