package net.Zrips.CMILib.Recipes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public enum CMIRecipeChoice {
    byMaterial, byItemStack;

    public static List<ItemStack> getChoices(RecipeChoice inputChoice) {

        List<ItemStack> items = new ArrayList<ItemStack>();
        if (inputChoice instanceof RecipeChoice.ExactChoice) {
            items = ((RecipeChoice.ExactChoice) inputChoice).getChoices();
        } else if (inputChoice instanceof RecipeChoice.MaterialChoice) {
            List<Material> materials = ((RecipeChoice.MaterialChoice) inputChoice).getChoices();

            for (Material material : materials) {
                if (material.equals(Material.AIR))
                    continue;
                items.add(new ItemStack(material));
            }
        } 

        return items;
    }

}
