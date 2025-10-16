package net.Zrips.CMILib.Recipes;

import org.bukkit.inventory.ItemStack;

public class CMIRecipeIngredient {

    private CMIRecipeChoice choice = CMIRecipeChoice.byMaterial;
    private ItemStack item = null;

    public CMIRecipeIngredient(ItemStack item, CMIRecipeChoice choice) {
        this.item = item;
        this.choice = choice;
    }

    public CMIRecipeIngredient(ItemStack item) {
        this.item = item;
    }

    public CMIRecipeChoice getChoice() {
        return choice;
    }

    public CMIRecipeIngredient setChoice(CMIRecipeChoice choice) {
        this.choice = choice;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public CMIRecipeIngredient setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public Object generateChoice() {
        if (choice.equals(CMIRecipeChoice.byMaterial)) {
            return new org.bukkit.inventory.RecipeChoice.MaterialChoice(item.getType());
        }
        return new org.bukkit.inventory.RecipeChoice.ExactChoice(item);
    }
}
