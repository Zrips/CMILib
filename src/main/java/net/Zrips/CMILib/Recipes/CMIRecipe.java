package net.Zrips.CMILib.Recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMIList;
import net.Zrips.CMILib.Container.CMINamespacedKey;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Version.Version;

public class CMIRecipe {

    private Recipe recipe = null;
    private String identificator = null;
    private String customKey = null;
    private CMIRecipeType type = null;

    private CMIRecipeCraftData data = null;

    public CMIRecipe(String identificator, CMIRecipeType type) {
        this.identificator = identificator;
        this.type = type;
    }

    public String getIdentificator() {
        return identificator;
    }

    public CMIRecipe setIdentificator(String identificator) {
        this.identificator = identificator;
        return this;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public CMIRecipe setRecipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public CMIRecipeType getType() {
        return type;
    }

    public CMIRecipe setType(CMIRecipeType type) {
        this.type = type;
        return this;
    }

    public ItemStack getResult() {
        return this.recipe.getResult();
    }

    public HashMap<Integer, ItemStack> getIngridients() {
        HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
        switch (type) {
        case Shaped:
            ShapedRecipe src = (ShapedRecipe) this.recipe;
            String[] shape = src.getShape();
            List<String> chars = new ArrayList<String>();
            for (String one : shape) {
                chars.addAll(Arrays.asList(one.split("(?<=\\G.)")));
            }
            for (int i = 0; i < 9; i++) {
                if (chars.size() <= i)
                    break;
                String ch = chars.get(i);
                ItemStack item = src.getIngredientMap().get(ch.charAt(0));
                if (item == null)
                    continue;
                map.put(i + 1, item);
            }
            return map;
        case Shapeless:
            ShapelessRecipe rc = (ShapelessRecipe) this.recipe;
            int i = 0;
            for (ItemStack one : rc.getIngredientList()) {
                i++;
                if (one == null)
                    continue;
                map.put(i, one);
            }
            return map;
        case Furnace:
            FurnaceRecipe fc = (FurnaceRecipe) this.recipe;
            map.put(1, fc.getInput());
            return map;
        }
        return null;
    }

    public static Recipe makeShapedRecipe(ItemStack result, HashMap<Integer, CMIRecipeIngredient> Recipe) {
        return makeShapedRecipe(result, Recipe, null);
    }

    @SuppressWarnings("deprecation")
    public static Recipe makeShapedRecipe(ItemStack result, HashMap<Integer, CMIRecipeIngredient> Recipe, String customKey) {

        if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
            ShapedRecipe NewShapedRecipe = new ShapedRecipe(new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Shaped, result, Recipe) : customKey),
                result);

            LinkedHashMap<String, CMIRecipeIngredient> itemsused = new LinkedHashMap<String, CMIRecipeIngredient>();

            StringBuilder ss = new StringBuilder();

            int y = 0;
            for (int i = 0; i < 9; i++) {
                CMIRecipeIngredient ritem = Recipe.get(i);
                if (ritem == null) {
                    ss.append(" ");
                    continue;
                }
                int e = 1;
                ItemStack item = ritem.getItem().clone();

                String ident = item.toString();

                if (!itemsused.containsKey(ident)) {
                    itemsused.put(ident, ritem.setItem(item));
                    y++;
                    e = y;
                } else {
                    e = getIndex(itemsused, ident) + 1;
                }
                ss.append(String.valueOf(e));
            }
            NewShapedRecipe.shape(ss.toString().split("(?<=\\G...)"));

            char[] charId = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
            int i = 0;
            for (Entry<String, CMIRecipeIngredient> one : itemsused.entrySet()) {
                char ch = charId[i];
                ItemStack item = one.getValue().getItem();
                if (item == null)
                    continue;
                i++;

                NewShapedRecipe.setIngredient(ch, (org.bukkit.inventory.RecipeChoice) one.getValue().generateChoice());
            }
            return NewShapedRecipe;
        }

        ShapedRecipe NewShapedRecipe = null;
        if (Version.isCurrentHigher(Version.v1_11_R1))
            NewShapedRecipe = new ShapedRecipe(new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Shaped, result, Recipe) : customKey), result);
        else
            NewShapedRecipe = new ShapedRecipe(result);

        LinkedHashMap<String, CMIRecipeIngredient> itemsused = new LinkedHashMap<String, CMIRecipeIngredient>();

        String ss = "";

        int y = 0;
        for (int i = 0; i < 9; i++) {
            CMIRecipeIngredient ritem = Recipe.get(i);
            if (ritem == null) {
                ss += " ";
                continue;
            }
            int e = 1;
            ItemStack item = new ItemStack(ritem.getItem().getType(), 1, ritem.getItem().getData().getData());
            if (!itemsused.containsKey(item.getType() + ":" + ritem.getItem().getData().getData())) {
                itemsused.put(item.getType() + ":" + ritem.getItem().getData().getData(), ritem.setItem(item));
                y++;
                e = y;
            } else {
                e = getIndex(itemsused, item.getType() + ":" + ritem.getItem().getData().getData()) + 1;
            }
            ss += String.valueOf(e);
        }
        NewShapedRecipe.shape(ss.split("(?<=\\G...)"));

        char[] charId = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        int i = 0;
        for (Entry<String, CMIRecipeIngredient> one : itemsused.entrySet()) {
            char ch = charId[i];
            ItemStack item = one.getValue().getItem();
            if (item == null)
                continue;
            i++;
            NewShapedRecipe.setIngredient(ch, item.getData());
        }
        return NewShapedRecipe;

    }

    public static int getIndex(LinkedHashMap<String, CMIRecipeIngredient> itemsused, Object value) {
        int result = 0;
        for (Entry<String, CMIRecipeIngredient> entry : itemsused.entrySet()) {
            if (entry.getKey().equals(value))
                return result;
            result++;
        }
        return -1;
    }

    public static Recipe makeShaplessRecipe(ItemStack result, HashMap<Integer, CMIRecipeIngredient> recipe) {
        return makeShaplessRecipe(result, recipe, null, null);
    }

    public static Recipe makeShaplessRecipe(ItemStack result, HashMap<Integer, CMIRecipeIngredient> recipe, String customKey) {
        return makeShaplessRecipe(result, recipe, null, customKey);
    }

    public static Recipe makeShaplessRecipe(ItemStack result, HashMap<Integer, CMIRecipeIngredient> recipe, CMINamespacedKey key) {
        return makeShaplessRecipe(result, recipe, key, null);
    }

    @SuppressWarnings("deprecation")
    public static Recipe makeShaplessRecipe(ItemStack result, HashMap<Integer, CMIRecipeIngredient> recipe, CMINamespacedKey key, String customKey) {

        if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
            ShapelessRecipe NewShapelessRecipe = new ShapelessRecipe(
                key == null ? new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Shapeless, result, recipe) : customKey)
                    : new org.bukkit.NamespacedKey(key.getNamespace(), key
                        .getKey()), result);
            for (CMIRecipeIngredient item : recipe.values()) {
                if (item == null)
                    continue;

                NewShapelessRecipe.addIngredient((org.bukkit.inventory.RecipeChoice) item.generateChoice());
            }
            return NewShapelessRecipe;
        }
        ShapelessRecipe NewShapelessRecipe = null;
        if (Version.isCurrentHigher(Version.v1_11_R1))
            NewShapelessRecipe = new ShapelessRecipe(key == null ? new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Shapeless, result, recipe)
                : customKey)
                : new org.bukkit.NamespacedKey(key.getNamespace(), key.getKey()), result);
        else
            NewShapelessRecipe = new ShapelessRecipe(result);
        for (CMIRecipeIngredient item : recipe.values()) {
            if (item == null)
                continue;
            NewShapelessRecipe.addIngredient(1, item.getItem().getData());
        }

        return NewShapelessRecipe;

    }

    public static Recipe makeFurnaceRecipe(ItemStack result, CMIRecipeIngredient Recipe) {
        return makeFurnaceRecipe(result, Recipe, null, null);
    }

    public static Recipe makeFurnaceRecipe(ItemStack result, CMIRecipeIngredient Recipe, CMIRecipeCraftData temp) {
        return makeFurnaceRecipe(result, Recipe, temp, null);
    }

    public static Recipe makeFurnaceRecipe(ItemStack result, CMIRecipeIngredient Recipe, CMIRecipeCraftData temp, String customKey) {
        if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
            float exp = 0.1F;
            int duration = 60;
            if (temp != null) {
                exp = temp.getExp();
                duration = temp.getCookingTime();
            }

            return new FurnaceRecipe(new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Furnace, result, Recipe) : customKey), result,
                (org.bukkit.inventory.RecipeChoice) Recipe
                    .generateChoice(), exp, duration);
        }
        FurnaceRecipe NewShapelessRecipe = new FurnaceRecipe(result, Recipe.getItem().getData());
        return NewShapelessRecipe;
    }

    public static Recipe makeSmithingRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeIngredient ingredient2) {
        return makeSmithingRecipe(result, ingredient1, ingredient2, null);
    }

    public static Recipe makeSmithingRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeIngredient ingredient2, String customKey) {
        if (Version.isCurrentEqualOrLower(Version.v1_13_R1))
            return null;

        return new org.bukkit.inventory.SmithingRecipe(
            new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Smithing, result, ingredient1, ingredient2) : customKey),
            result,
            (org.bukkit.inventory.RecipeChoice) ingredient1.generateChoice(),
            (org.bukkit.inventory.RecipeChoice) ingredient2.generateChoice());
    }

    public static Recipe makeStonecuttingRecipe(ItemStack result, CMIRecipeIngredient ingredient1) {
        return makeStonecuttingRecipe(result, ingredient1, null);
    }

    public static Recipe makeStonecuttingRecipe(ItemStack result, CMIRecipeIngredient ingredient1, String customKey) {
        if (Version.isCurrentEqualOrLower(Version.v1_13_R1))
            return null;
        return new org.bukkit.inventory.StonecuttingRecipe(
            new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Stonecutting, result, ingredient1) : customKey),
            result,
            (org.bukkit.inventory.RecipeChoice) ingredient1.generateChoice());
    }

    public static Recipe makeCampfireRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeCraftData temp) {
        return makeCampfireRecipe(result, ingredient1, temp, null);
    }

    public static Recipe makeCampfireRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeCraftData temp, String customKey) {
        if (Version.isCurrentEqualOrLower(Version.v1_13_R1))
            return null;

        float exp = 0.1F;
        int duration = 60;
        if (temp != null) {
            exp = temp.getExp();
            duration = temp.getCookingTime();
        }

        return new org.bukkit.inventory.CampfireRecipe(new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Campfire, result, ingredient1)
            : customKey), result,
            (org.bukkit.inventory.RecipeChoice) ingredient1.generateChoice(), exp, duration);
    }

    public static Recipe makeSmokingRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeCraftData temp) {
        return makeSmokingRecipe(result, ingredient1, temp, null);
    }

    public static Recipe makeSmokingRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeCraftData temp, String customKey) {
        if (Version.isCurrentEqualOrLower(Version.v1_13_R1))
            return null;

        float exp = 0.1F;
        int duration = 60;
        if (temp != null) {
            exp = temp.getExp();
            duration = temp.getCookingTime();
        }

        return new org.bukkit.inventory.SmokingRecipe(new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Smoking, result, ingredient1)
            : customKey), result,
            (org.bukkit.inventory.RecipeChoice) ingredient1.generateChoice(), exp,
            duration);
    }

    public static Recipe makeBlastingRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeCraftData temp) {
        return makeBlastingRecipe(result, ingredient1, temp, null);
    }

    public static Recipe makeBlastingRecipe(ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeCraftData temp, String customKey) {
        if (Version.isCurrentEqualOrLower(Version.v1_13_R1))
            return null;

        float exp = 0.1F;
        int duration = 60;
        if (temp != null) {
            exp = temp.getExp();
            duration = temp.getCookingTime();
        }

        return new org.bukkit.inventory.BlastingRecipe(new org.bukkit.NamespacedKey(CMILib.getInstance(), customKey == null ? getRecipeIdentificator(CMIRecipeType.Blasting, result, ingredient1)
            : customKey), result,
            (org.bukkit.inventory.RecipeChoice) ingredient1.generateChoice(), exp,
            duration);
    }

    public static Recipe createRecipe(CMIRecipeType type, ItemStack result, HashMap<Integer, CMIRecipeIngredient> Recipe) {
        return createRecipe(type, result, Recipe, null);
    }

    public static Recipe createRecipe(CMIRecipeType type, ItemStack result, HashMap<Integer, CMIRecipeIngredient> Recipe, CMIRecipeCraftData temp) {
        return createRecipe(type, result, Recipe, temp, null);
    }

    public static Recipe createRecipe(CMIRecipeType type, ItemStack result, HashMap<Integer, CMIRecipeIngredient> Recipe, CMIRecipeCraftData temp, String customKey) {
        Recipe recipe = null;
        switch (type) {
        case Shaped:
            recipe = makeShapedRecipe(result, Recipe, customKey);
            break;
        case Shapeless:
            recipe = makeShaplessRecipe(result, Recipe, customKey);
            break;
        case Furnace:
            recipe = makeFurnaceRecipe(result, Recipe.get(0), temp, customKey);
            break;
        case Smithing:
            recipe = makeSmithingRecipe(result, Recipe.get(0), Recipe.get(1), customKey);
            break;
        case Stonecutting:
            recipe = makeStonecuttingRecipe(result, Recipe.get(0), customKey);
            break;
        case Campfire:
            recipe = makeCampfireRecipe(result, Recipe.get(0), temp, customKey);
            break;
        case Smoking:
            recipe = makeSmokingRecipe(result, Recipe.get(0), temp, customKey);
            break;
        case Blasting:
            recipe = makeBlastingRecipe(result, Recipe.get(0), temp, customKey);
            break;
        }
        return recipe;
    }

    public static List<ItemStack> getIngredientsList(Recipe recipe) {

        List<ItemStack> list = new ArrayList<ItemStack>();

        for (CMIRecipeIngredient one : getIngredientsMap(recipe).values()) {
            if (one.getItem() != null)
                list.add(one.getItem());
        }

        return list;
    }

    public HashMap<Integer, CMIRecipeIngredient> getIngredients() {
        return getIngredientsMap(recipe);
    }

    public static HashMap<Integer, CMIRecipeIngredient> getIngredientsMap(Recipe recipe) {

        HashMap<Integer, CMIRecipeIngredient> map = new HashMap<Integer, CMIRecipeIngredient>();

        if (recipe == null)
            return map;

        if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe rc = (ShapelessRecipe) recipe;

            for (int i = 0; i < rc.getIngredientList().size(); i++) {

                CMIRecipeIngredient CMIri = new CMIRecipeIngredient(rc.getIngredientList().get(i));

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(rc.getChoiceList().get(i), CMIri);
                }
                map.put(i, CMIri);
            }

        } else if (recipe instanceof ShapedRecipe) {
            ShapedRecipe rc = (ShapedRecipe) recipe;
            String[] shape = rc.getShape();
            int i = -1;
            for (int x = 0; x < 3; x++) {

                if (shape.length <= x) {
                    break;
                }
                String line = shape[x];
                for (int z = 0; z < 3; z++) {

                    i++;
                    if (line.length() <= z) {
                        continue;
                    }

                    char oneS = line.charAt(z);

                    ItemStack item = rc.getIngredientMap().get(oneS);

                    if (item == null) {
                        continue;
                    }

                    CMIRecipeIngredient CMIri = new CMIRecipeIngredient(item);

                    if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                        setChoice(rc.getChoiceMap().get(oneS), CMIri);
                    }

                    map.put(i, CMIri);
                }
            }
        } else if (recipe instanceof FurnaceRecipe) {
            FurnaceRecipe rc = (FurnaceRecipe) recipe;

            CMIRecipeIngredient CMIri = new CMIRecipeIngredient(rc.getInput());

            if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                setChoice(rc.getInputChoice(), CMIri);
            }
            map.put(0, CMIri);
        } else {

            CMIRecipeType recipeType = CMIRecipeType.getByName(recipe.getClass().getSimpleName());

            switch (recipeType) {
            case Blasting:
                org.bukkit.inventory.BlastingRecipe brc = (org.bukkit.inventory.BlastingRecipe) recipe;
                CMIRecipeIngredient CMIri = new CMIRecipeIngredient(brc.getInput());

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(brc.getInputChoice(), CMIri);
                }

                map.put(0, CMIri);
                break;
            case Campfire:
                org.bukkit.inventory.CampfireRecipe cfr = (org.bukkit.inventory.CampfireRecipe) recipe;
                CMIri = new CMIRecipeIngredient(cfr.getInput());

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(cfr.getInputChoice(), CMIri);
                }

                map.put(0, CMIri);
                break;
            case Cooking:
                org.bukkit.inventory.CookingRecipe cor = (org.bukkit.inventory.CookingRecipe) recipe;
                CMIri = new CMIRecipeIngredient(cor.getInput());

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(cor.getInputChoice(), CMIri);
                }
                map.put(0, CMIri);
                break;
            case Merchant:
                org.bukkit.inventory.MerchantRecipe mer = (org.bukkit.inventory.MerchantRecipe) recipe;

                for (int i = 0; i < mer.getIngredients().size(); i++) {
                    CMIri = new CMIRecipeIngredient(mer.getIngredients().get(i));
                    map.put(i, CMIri);
                }

                break;
            case Smoking:
                org.bukkit.inventory.SmokingRecipe smr = (org.bukkit.inventory.SmokingRecipe) recipe;

                CMIri = new CMIRecipeIngredient(smr.getInput());

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(smr.getInputChoice(), CMIri);
                }
                map.put(0, CMIri);
                break;
            case Stonecutting:
                org.bukkit.inventory.StonecuttingRecipe str = (org.bukkit.inventory.StonecuttingRecipe) recipe;
                CMIri = new CMIRecipeIngredient(str.getInput());

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(str.getInputChoice(), CMIri);
                }
                map.put(0, CMIri);
                break;
            case Smithing:
                org.bukkit.inventory.SmithingRecipe smtr = (org.bukkit.inventory.SmithingRecipe) recipe;

                CMIri = new CMIRecipeIngredient(smtr.getBase().getItemStack());

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(smtr.getBase(), CMIri);
                }
                map.put(0, CMIri);

                CMIri = new CMIRecipeIngredient(smtr.getAddition().getItemStack());

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    setChoice(smtr.getAddition(), CMIri);
                }
                map.put(1, CMIri);
                break;
            }

        }
        return map;
    }

    private static void setChoice(org.bukkit.inventory.RecipeChoice choice, CMIRecipeIngredient CMIri) {
        try {
            if (choice instanceof org.bukkit.inventory.RecipeChoice.ExactChoice) {
                CMIri.setChoice(CMIRecipeChoice.byItemStack);
            } else {
                CMIri.setChoice(CMIRecipeChoice.byMaterial);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getRecipeIdentificator(CMIRecipeType type, Recipe recipe) {
        return getRecipeIdentificator(type, recipe.getResult(), getIngredientsMap(recipe));
    }

    private static String getRecipeIdentificator(CMIRecipeType type, ItemStack result, CMIRecipeIngredient ingredient1, CMIRecipeIngredient ingredient2) {
        HashMap<Integer, CMIRecipeIngredient> temp = new HashMap<Integer, CMIRecipeIngredient>();
        temp.put(1, ingredient1);
        temp.put(2, ingredient2);

        return getRecipeIdentificator(type, result, temp);
    }

    private static String getRecipeIdentificator(CMIRecipeType type, ItemStack result, CMIRecipeIngredient ingredient1) {
        HashMap<Integer, CMIRecipeIngredient> temp = new HashMap<Integer, CMIRecipeIngredient>();
        temp.put(1, ingredient1);

        return getRecipeIdentificator(type, result, temp);
    }

    private static String getRecipeIdentificator(CMIRecipeType type, ItemStack result, HashMap<Integer, CMIRecipeIngredient> Recipe) {
        StringBuilder str = new StringBuilder();
        str.append(type.toString());
        str.append(result.getType() + ":" + result.getAmount());
        for (Entry<Integer, CMIRecipeIngredient> one : Recipe.entrySet()) {
            if (one.getValue().getItem() == null)
                continue;

            if (one.getValue().getChoice().equals(CMIRecipeChoice.byItemStack)) {
                if (one.getValue().getItem().hasItemMeta()) {
                    ItemMeta meta = one.getValue().getItem().getItemMeta();
                    if (meta.hasDisplayName())
                        str.append(":" + meta.getDisplayName());
                    if (meta.hasLore())
                        str.append(CMIList.listToString(meta.getLore()));
                }
                CMINBT nbt = new CMINBT(one.getValue().getItem());
                Integer value = nbt.getInt("CustomModelData");
                if (value != null)
                    str.append(":" + value);
            }

            str.append(one.getValue().getItem().getType() + ":" + one.getValue().getItem().getAmount() + ":" + (one.getValue().getChoice().equals(CMIRecipeChoice.byItemStack) ? "1:" : "") + one.getKey());
        }
        return UUID.nameUUIDFromBytes(str.toString().getBytes()).toString();
    }

    public static Recipe getRecipe(CMINamespacedKey key) {

        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
            return Bukkit.getServer().getRecipe(new org.bukkit.NamespacedKey(key.getNamespace(), key.getKey()));

        Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            if (CMINamespacedKey.getKey(recipe).toString().equals(key.toString()))
                return recipe;
        }
        return null;
    }

    public CMIRecipeCraftData getData() {
        return data;
    }

    public CMIRecipe setData(CMIRecipeCraftData data) {
        this.data = data;
        return this;
    }

    public String getCustomKey() {
        return customKey;
    }

    public CMIRecipe setCustomKey(String customKey) {
        this.customKey = customKey;
        return this;
    }
}
