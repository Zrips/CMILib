package net.Zrips.CMILib.Container;

import java.util.UUID;

import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Recipes.CMIRecipe;
import net.Zrips.CMILib.Recipes.CMIRecipeType;
import net.Zrips.CMILib.Version.Version;

public class CMINamespacedKey {

    private String namespace;
    private String key;

    public CMINamespacedKey(org.bukkit.NamespacedKey key) {
        this.namespace = key.getNamespace();
        this.key = key.getKey();
    }

    public CMINamespacedKey(Plugin plugin, String key) {
        this.namespace = plugin.getName().toLowerCase();
        this.key = key.toLowerCase();
    }

    public CMINamespacedKey(String plugin, String key) {
        this.namespace = plugin.toLowerCase();
        this.key = key.toLowerCase();
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.key;
    }

    public static CMINamespacedKey getKey(Recipe recipe) {

        if (Version.isCurrentEqualOrLower(Version.v1_11_R1)) {
            return new CMINamespacedKey("bukkit", CMIRecipe.getRecipeIdentificator(CMIRecipeType.getByName(recipe.getClass().getSimpleName()), recipe));
        }

        CMIRecipeType type = CMIRecipeType.getByName(recipe.getClass().getSimpleName());

        switch (type) {
        case Shapeless:
            return new CMINamespacedKey(((org.bukkit.inventory.ShapelessRecipe) recipe).getKey());
        case Shaped:
            return new CMINamespacedKey(((org.bukkit.inventory.ShapedRecipe) recipe).getKey());
        case Blasting:
            return new CMINamespacedKey(((org.bukkit.inventory.BlastingRecipe) recipe).getKey());
        case Campfire:
            return new CMINamespacedKey(((org.bukkit.inventory.CampfireRecipe) recipe).getKey());
        case Cooking:
            return new CMINamespacedKey(((org.bukkit.inventory.CookingRecipe) recipe).getKey());
        case Smoking:
            return new CMINamespacedKey(((org.bukkit.inventory.SmokingRecipe) recipe).getKey());
        case Stonecutting:
            return new CMINamespacedKey(((org.bukkit.inventory.StonecuttingRecipe) recipe).getKey());
        case Smithing:
            return new CMINamespacedKey(((org.bukkit.inventory.SmithingRecipe) recipe).getKey());
        case Furnace:            
            if (Version.isCurrentEqualOrLower(Version.v1_12_R1)) {
                return new CMINamespacedKey(new org.bukkit.NamespacedKey(CMILib.getInstance(), CMIRecipe.getRecipeIdentificator(CMIRecipeType.Furnace, recipe)));
            }
            return new CMINamespacedKey(((org.bukkit.inventory.FurnaceRecipe) recipe).getKey());
        case Transmute:            
            if (Version.isCurrentEqualOrHigher(Version.v1_21_R1)) 
                return new CMINamespacedKey(new org.bukkit.NamespacedKey(CMILib.getInstance(), CMIRecipe.getRecipeIdentificator(CMIRecipeType.Transmute, recipe)));            
            return null;
        case Complex:
            return new CMINamespacedKey(((org.bukkit.inventory.ComplexRecipe) recipe).getKey());
        }

        return null;
    }

    public static CMINamespacedKey randomKey() {
        return new CMINamespacedKey("bukkit", UUID.randomUUID().toString());
    }
}
