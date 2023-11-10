package net.Zrips.CMILib.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Attributes.Attribute;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Colors.CMIColors;
import net.Zrips.CMILib.Enchants.CMIEnchantment;
import net.Zrips.CMILib.Entities.CMIEntity;
import net.Zrips.CMILib.Entities.CMIEntityType;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Recipes.CMIRecipe;
import net.Zrips.CMILib.Recipes.CMIRecipeIngredient;
import net.Zrips.CMILib.Version.Version;

public class CMIItemStack {

    @Deprecated
    private int id = 0;
    @Deprecated
    private short data = 0;
    private short durability = 0;
    private int amount = 0;

    private String bukkitName = null;
    private String mojangName = null;
    private CMIMaterial cmiMaterial = null;
    private Material material = null;
    private CMIEntityType entityType = null;
    private ItemStack item;

    public CMIItemStack(Material material) {
        this.material = material;
        this.cmiMaterial = CMIMaterial.get(material);
    }

    public CMIItemStack(CMIMaterial cmiMaterial) {
        this.cmiMaterial = cmiMaterial;
        if (cmiMaterial != null)
            this.material = cmiMaterial.getMaterial();
    }

    public CMIItemStack(ItemStack item) {
        this.setItemStack(item);
    }

    @Override
    public CMIItemStack clone() {
        CMIItemStack cm = new CMIItemStack(material);
        cm.entityType = this.entityType;
        cm.setId(id);
        cm.setData(data);
        cm.setAmount(amount);
        cm.setDurability(durability);
        cm.setBukkitName(bukkitName);
        cm.setMojangName(mojangName);
        cm.setCMIMaterial(cmiMaterial);
        cm.setMaterial(material);
        cm.setItemStack(this.item != null ? this.item.clone() : null);
        return cm;
    }

    @Deprecated
    public int getId() {
        return id;
    }

    @Deprecated
    public void setId(Integer id) {
        this.id = id;
    }

    @Deprecated
    public short getData() {
        return data;
    }

    public boolean isTool() {
        return getMaxDurability() > 0;
    }

    public boolean isArmor() {
        if (this.getCMIType() != null && this.getCMIType().isArmor())
            return true;
        return CMIMaterial.isArmor(this.getType());
    }

    public short getDurability() {
        return this.getItemStack().getDurability();
    }

    public short getMaxDurability() {
        return this.material.getMaxDurability();
    }

    public void setData(short data) {
        this.data = data;
        if (this.getCMIType() != null) {
            ItemMeta meta = null;
            if (item != null && item.hasItemMeta()) {
                meta = item.getItemMeta();
            }
            this.item = null;
            if (meta != null && this.getItemStack() != null) {
                this.getItemStack().setItemMeta(meta);
            }
        }
    }

    public CMIItemStack setUnbreakable(Boolean state) {
        if (state == null)
            return this;
        this.item = (ItemStack) new CMINBT(this.getItemStack()).setByte("Unbreakable", state ? (byte) 1 : (byte) 0);
        return this;
    }

    public CMIItemStack addAttributes(List<Attribute> attList) {
        if (attList == null || attList.isEmpty())
            return this;
        try {
//	    for (Attribute attribute : attList) {
//		org.bukkit.attribute.AttributeModifier atmod =
//		    new org.bukkit.attribute.AttributeModifier(UUID.randomUUID(), attribute.getType().getFullName(), attribute.getMod(), Operation.ADD_NUMBER, null);
//		attribute.getType().Armor
//		this.getItemStack().getItemMeta().addAttributeModifier(org.bukkit.attribute.Attribute., atmod);
//	    }
        } catch (Throwable e) {

        }

        this.item = CMILib.getInstance().getReflectionManager().addAttributes(attList, this.getItemStack());
        return this;
    }

    public CMIItemStack setTag(String tag) {
        if (tag == null || tag.isEmpty())
            return this;
        this.item = CMINBT.modifyItemStack(this.getItemStack(), tag);
        return this;
    }

    public CMIItemStack setDisplayName(String name) {
        ItemMeta meta = this.getItemStack().getItemMeta();
        if (meta != null) {
            if (name == null) {
                meta.setDisplayName(null);
            } else {
                meta.setDisplayName(CMIChatColor.translate(name));
            }
        }
        this.getItemStack().setItemMeta(meta);
        return this;
    }

    public String getDisplayName() {
        ItemMeta meta = this.getItemStack().getItemMeta();
        return meta == null || meta.getDisplayName() == null || meta.getDisplayName().isEmpty() ? getRealName() : meta.getDisplayName();
    }

    public CMIItemStack addLore(String string) {
        if (string == null)
            return this;
        ItemMeta meta = this.getItemStack().getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<String>();
        lore.add(CMIChatColor.translate(string));
        meta.setLore(lore);
        this.getItemStack().setItemMeta(meta);
        return this;
    }

    public CMIItemStack clearLore() {
        ItemMeta meta = this.getItemStack().getItemMeta();
        if (meta != null) {
            List<String> t = new ArrayList<String>();
            meta.setLore(t);
            this.getItemStack().setItemMeta(meta);
        }
        return this;
    }

    public CMIItemStack setLore(List<String> lore) {
        if (lore == null || lore.isEmpty())
            return this;
        ItemMeta meta = this.getItemStack().getItemMeta();
        List<String> t = new ArrayList<String>();
        for (String one : lore) {
            t.add(CMIChatColor.translate(one));
        }
        meta.setLore(t);
        this.getItemStack().setItemMeta(meta);
        return this;
    }

    public CMIItemStack addEnchant(Enchantment enchant, Integer level) {
        if (enchant == null)
            return this;

        if (this.getItemStack().getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.getItemStack().getItemMeta();
            meta.addStoredEnchant(enchant, level, true);
            this.getItemStack().setItemMeta(meta);
        } else {
            ItemMeta meta = this.getItemStack().getItemMeta();
            meta.addEnchant(enchant, level, true);
            this.getItemStack().setItemMeta(meta);
        }
        return this;
    }

    public CMIItemStack addEnchant(HashMap<Enchantment, Integer> enchants) {
        if (enchants == null || enchants.isEmpty())
            return this;
        for (Entry<Enchantment, Integer> oneEnch : enchants.entrySet()) {
            this.addEnchant(oneEnch.getKey(), oneEnch.getValue());
        }
        return this;
    }

    public CMIItemStack clearEnchants() {
        ItemMeta meta = this.getItemStack().getItemMeta();
        meta.getEnchants().clear();
        this.getItemStack().setItemMeta(meta);
        return this;
    }

    public List<String> getLore() {
        ItemMeta meta = this.getItemStack().getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<String>();
                meta.setLore(lore);
            }

            return meta.getLore() == null ? new ArrayList<String>() : meta.getLore();
        }
        return new ArrayList<String>();
    }

    public String getRealName() {
        return this.getCMIType() == null || this.getCMIType() == CMIMaterial.NONE ? this.getType().name() : this.getCMIType().getTranslatedName() != null ? this.getCMIType().getTranslatedName() : this
            .getCMIType().getName();
//	if (this.getItemStack() != null) {
//
////	    String translated = CMI.getInstance().getItemManager().getTranslatedName(this.getItemStack());
//	    if (translated != null)
////		return translated;
//	    try {
//		return CMI.getInstance().getRef().getItemMinecraftName(this.getItemStack());
//	    } catch (Exception e) {
//	    }
//	}
//	return CMI.getInstance().getItemManager().getRealName(this, true).getName();
    }

    public String getBukkitName() {
        return bukkitName == null || bukkitName.isEmpty() ? null : bukkitName;
    }

    public void setBukkitName(String bukkitName) {
        this.bukkitName = bukkitName;
    }

    public String getMojangName() {
//	if (getCMIType().isSkull() && !Version.isCurrentEqualOrHigher(Version.v1_13_R1))
//	    mojangName = "skull";
//	try {
//	    mojangName = CMI.getInstance().getRef().getItemMinecraftName(getItemStack()).replace("minecraft:", "");
//	} catch (Exception e) {
//
//	}
        return mojangName == null || mojangName.isEmpty() ? this.getCMIType().getMaterial().name() : mojangName;
    }

    public void setMojangName(String mojangName) {
        if (mojangName != null)
            this.mojangName = mojangName.replace("minecraft:", "");
    }

    public Material getType() {
        if (material == null && cmiMaterial != null)
            return cmiMaterial.getMaterial();
        return material;
    }

    public CMIMaterial getCMIType() {
        return cmiMaterial == null ? CMIMaterial.get(material) : cmiMaterial;
    }

    @Deprecated
    public Material getMaterial() {
        return getType();
    }

    public void setMaterial(Material material) {
        this.cmiMaterial = CMIMaterial.get(material);
        this.material = material;
    }

    public void setCMIMaterial(CMIMaterial material) {
        this.cmiMaterial = material;
        this.material = material == null ? null : material.getMaterial();
    }

    private void setEnt() {
        if (cmiMaterial.isMonsterEgg()) {
            if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                this.item = this.item == null ? new ItemStack(this.getType()) : this.item;
                this.item.setAmount(this.getAmount());
            } else {
                this.item = this.item == null ? new ItemStack(this.getType(), this.amount == 0 ? 1 : this.amount, data == 0 ? (short) 90 : data) : this.item;
            }
            if (this.getEntityType() != null) {
                this.item = CMILib.getInstance().getReflectionManager().setEggType(this.item, this.getEntityType());
            } else {
                CMIEntityType type = CMIEntityType.getById(data);
                if (type != null && !Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    this.item = CMILib.getInstance().getReflectionManager().setEggType(this.item, type.getType());
                }
            }
        } else {
            if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                this.item = this.item == null ? new ItemStack(this.getType()) : this.item;
                this.item.setAmount(this.getAmount());
                if (this.getType().equals(CMIMaterial.SPAWNER.getMaterial())) {
                    if (this.getEntityType() != null) {
                        item = CMIEntity.setEntityType(item, this.getEntityType());
                    } else {
                        // As of 1.19 we should return empty spawner if type not set
                        if (Version.isCurrentEqualOrLower(Version.v1_19_R2)) {
                            item = CMIEntity.setEntityType(item, CMIEntityType.getById(data == 0 ? (short) 90 : data).getType());
                        }
                    }
                }
            } else {
                this.item = new ItemStack(this.getType(), this.amount == 0 ? 1 : this.amount, data);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemStack() {

        if (item == null) {

            try {
                if (!this.getType().isItem()) {
                    return null;
                }
            } catch (Throwable e) {
            }

            setEnt();

            // Should not be null at this point if everything went how it should have, but just in case
            if (this.item == null)
                return null;

            if (this.getCMIType().isPotion() || item.getType().name().contains("SPLASH_POTION") || item.getType().name().contains("TIPPED_ARROW")) {
                PotionMeta potion = (PotionMeta) item.getItemMeta();
                PotionEffectType effect = PotionEffectType.getById(data);
                if (effect != null) {
                    potion.addCustomEffect(new PotionEffect(effect, 60, 0), true);
                }
                item.setItemMeta(potion);
                item.setDurability((short) 0);
                potion = (PotionMeta) item.getItemMeta();
                potion.setDisplayName(this.getRealName());
                item.setItemMeta(potion);
            }

            if (CMIMaterial.SPAWNER.equals(this.item.getType())) {
                if (data == 0)
                    data = 90;
                CMIEntityType type = this.entityType;
                if (type == null && Version.isCurrentEqualOrLower(Version.v1_19_R2)) {
                    type = CMIEntityType.getById(data);
                }
                if (type != null) {
                    this.item = CMIEntity.setEntityType(this.item, type.getType());
                }
            }
            if (this.durability > 0) {
                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    org.bukkit.inventory.meta.Damageable damage = (org.bukkit.inventory.meta.Damageable) item.getItemMeta();
                    damage.setDamage(item.getType().getMaxDurability() - durability);
                } else {
                    this.item.setDurability(durability);
                }
            }
        }
        return item;
    }

    @SuppressWarnings("deprecation")
    public CMIItemStack setItemStack(ItemStack item) {
        this.item = item;
        if (item != null) {
            this.amount = item.getAmount();
            this.material = item.getType();
            this.cmiMaterial = CMIMaterial.get(item);
            if (Version.isCurrentEqualOrLower(Version.v1_13_R2)) {
                this.id = item.getType().getId();
                if ((this.getType().isBlock() || this.getType().isSolid())) {
                    data = item.getData().getData();
                }
                if (item.getType().getMaxDurability() - item.getDurability() < 0) {
                    data = item.getData().getData();
                }
            } else if (cmiMaterial != null) {
                this.id = cmiMaterial.getId();
            }

            if (item.getType().getMaxDurability() > 15) {
                data = (short) 0;
            }

            if (CMIMaterial.isMonsterEgg(item.getType())) {
                entityType = CMIEntityType.getByType(CMILib.getInstance().getReflectionManager().getEggType(item));
            }

            if (item.getType() == Material.POTION || item.getType().name().contains("SPLASH_POTION") || item.getType().name().contains("TIPPED_ARROW")) {
                PotionMeta potion = (PotionMeta) item.getItemMeta();
                try {
                    if (potion != null && potion.getBasePotionData() != null && potion.getBasePotionData().getType() != null && potion.getBasePotionData().getType().getEffectType() != null) {
                        data = (short) potion.getBasePotionData().getType().getEffectType().getId();
                    }
                } catch (NoSuchMethodError e) {
                }
            }
        }

        return this;
    }

    public int getAmount() {
        return amount <= 0 ? 1 : amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        if (item != null)
            this.item.setAmount(this.amount == 0 ? item.getAmount() : this.amount);
    }

    public boolean isSimilar(ItemStack item) {
        return isSimilar(CMILib.getInstance().getItemManager().getItem(item));
    }

    public boolean isSimilar(CMIItemStack item) {

        if (item == null)
            return false;

        try {
            if ((item.getCMIType().isPotion() || item.getCMIType().equals(CMIMaterial.TIPPED_ARROW)) &&
                (this.getCMIType().isPotion() || this.getCMIType().equals(CMIMaterial.TIPPED_ARROW)) &&
                this.getType().equals(item.getType())) {
                PotionMeta potion = (PotionMeta) item.getItemStack().getItemMeta();
                PotionMeta potion2 = (PotionMeta) this.getItemStack().getItemMeta();
                try {
                    if (potion != null && potion.getBasePotionData() != null) {
                        PotionData base1 = potion.getBasePotionData();
                        if (base1.getType() != null) {
                            if (potion2 != null && potion2.getBasePotionData() != null) {
                                PotionData base2 = potion2.getBasePotionData();
                                if (base2.getType() != null) {
                                    if (base1.getType().equals(base2.getType()) && base1.isExtended() == base2.isExtended() && base1.isUpgraded() == base2.isUpgraded())
                                        return true;
                                }
                            }
                        }
                    }
                    return false;
                } catch (NoSuchMethodError e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.getItemStack().getItemMeta() instanceof EnchantmentStorageMeta && item.getItemStack().getItemMeta() instanceof EnchantmentStorageMeta) {

                EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) this.getItemStack().getItemMeta();
                EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) item.getItemStack().getItemMeta();

                for (Entry<Enchantment, Integer> one : meta1.getEnchants().entrySet()) {
                    if (!meta2.getEnchants().containsKey(one.getKey()) || meta2.getEnchants().get(one.getKey()) != one.getValue())
                        return false;
                }

                for (Entry<Enchantment, Integer> one : meta1.getStoredEnchants().entrySet()) {
                    if (!meta2.getStoredEnchants().containsKey(one.getKey()) || meta2.getStoredEnchants().get(one.getKey()) != one.getValue())
                        return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((item.getCMIType() == CMIMaterial.SPAWNER || item.getCMIType().isMonsterEgg()) && (getCMIType() == CMIMaterial.SPAWNER || getCMIType().isMonsterEgg())) {
            if (this.cmiMaterial != item.cmiMaterial)
                return false;
            if (getEntityType() != item.getEntityType())
                return false;
            return true;
        }

        if (item.getCMIType() == CMIMaterial.PLAYER_HEAD && this.getCMIType() == CMIMaterial.PLAYER_HEAD) {
            try {

                String base1 = null;
                // Trying to extract head data and its owner name
                try {
                    CMINBT nbt = new CMINBT(getItemStack());
                    List<String> ls = nbt.getList("SkullOwner.Properties.textures");
                    if (ls != null) {
                        base1 = ls.get(0);
                    }
                } catch (Throwable e) {
//		    e.printStackTrace();
                }

                String base2 = null;
                // Trying to extract head data and its owner name
                try {
                    CMINBT nbt = new CMINBT(item.getItemStack());
                    List<String> ls = nbt.getList("SkullOwner.Properties.textures");
                    if (ls != null) {
                        base2 = ls.get(0);
                    }
                } catch (Throwable e) {
//		    e.printStackTrace();
                }

                if (base1 != null && base2 == null || base1 == null && base2 != null || base1 != null && base2 != null && !base1.equals(base2)) {

                    SkullMeta skullMeta = (SkullMeta) item.getItemStack().getItemMeta();
                    SkullMeta skullMeta2 = (SkullMeta) getItemStack().getItemMeta();

                    if (skullMeta.getOwner() != null && skullMeta2.getOwner() == null || skullMeta.getOwner() == null && skullMeta2.getOwner() != null)
                        return false;

                    if (skullMeta.getOwner() != null && skullMeta2.getOwner() != null && !skullMeta.getOwner().equals(skullMeta2.getOwner()))
                        return false;

                    return false;
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }

        }

        if (Version.isCurrentEqualOrHigher(Version.v1_13_R1))
            return this.cmiMaterial == item.cmiMaterial;
        return this.cmiMaterial == item.cmiMaterial && this.getData() == item.getData();
    }

    public EntityType getEntityType() {

        if (this.getItemStack() == null)
            return null;

        if (this.entityType != null) {
            return this.entityType.getType();
        }

        ItemStack is = this.getItemStack().clone();

        if (Version.isCurrentEqualOrHigher(Version.v1_8_R1) && is.getItemMeta() instanceof org.bukkit.inventory.meta.BlockStateMeta) {

            if (Version.isCurrentEqualOrHigher(Version.v1_19_R3)) {
                CMINBT nbt = new CMINBT(is);
                if (!nbt.hasNBT("BlockEntityTag"))
                    return null;
            }

            org.bukkit.inventory.meta.BlockStateMeta bsm = (org.bukkit.inventory.meta.BlockStateMeta) is.getItemMeta();
            if (bsm.getBlockState() instanceof CreatureSpawner) {
                CreatureSpawner bs = (CreatureSpawner) bsm.getBlockState();
                try {
                    return bs.getSpawnedType();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        if (CMIMaterial.isMonsterEgg(is.getType())) {
            return CMILib.getInstance().getReflectionManager().getEggType(is);
        }
        if (CMIMaterial.get(is) != null && CMIMaterial.get(is).isMonsterEgg()) {
            return CMILib.getInstance().getReflectionManager().getEggType(is);
        }

        if (Version.isCurrentEqualOrLower(Version.v1_12_R1))
            return EntityType.fromId(is.getData().getData());
        return null;
    }

    public boolean hasNbtTag() {
        return new CMINBT(this.getItemStack()).hasNBT();
    }

    public CMIItemStack setNbt(String path, String value) {
        ItemStack it = this.getItemStack();
        it = (ItemStack) new CMINBT(it).setString(path, value);
        this.item = it;
        return this;
    }

    public CMIItemStack setNbt(String path, int value) {
        ItemStack it = this.getItemStack();
        it = (ItemStack) new CMINBT(it).setInt(path, value);
        this.item = it;
        return this;
    }

    public CMIItemStack setNbt(String path, boolean value) {
        ItemStack it = this.getItemStack();
        it = (ItemStack) new CMINBT(it).setBoolean(path, value);
        ItemMeta meta = it.getItemMeta();
        if (meta != null) {
            this.getItemStack().setItemMeta(meta);
        }
        return this;
    }

    public String getNbtString(String path) {
        return new CMINBT(this.getItemStack()).getString(path);
    }

    public Integer getNbtInt(String path) {
        return new CMINBT(this.getItemStack()).getInt(path);
    }

    public Boolean getNbtBoolean(String path) {
        return new CMINBT(this.getItemStack()).getBoolean(path);
    }

    public List<Recipe> getRecipesFor() {
        if (getItemStack() == null)
            return new ArrayList<Recipe>();

        ItemStack i = getItemStack().clone();

        if (Version.isCurrentLower(Version.v1_13_R1))
            i.getData().setData((byte) data);
        if (i.getType().getMaxDurability() > 15)
            i.setDurability((short) 0);

        List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            recipes = Bukkit.getRecipesFor(i);
        } catch (Throwable e) {
        }

        if (this.getCMIType().isShulkerBox()) {
            CMIMaterial colorMat = CMIColors.getColorMaterial(getCMIType());
            if (colorMat != null) {
                HashMap<Integer, CMIRecipeIngredient> Recipe = new HashMap<Integer, CMIRecipeIngredient>();

                Recipe.put(1, new CMIRecipeIngredient(CMIMaterial.SHULKER_BOX.newItemStack()));
                Recipe.put(2, new CMIRecipeIngredient(colorMat.newItemStack()));
                org.bukkit.inventory.Recipe rec = CMIRecipe.makeShaplessRecipe(this.getItemStack(), Recipe);
                if (rec != null)
                    recipes.add(rec);

                for (CMIMaterial one : CMIMaterial.values()) {
                    if (!one.isShulkerBox())
                        continue;

                    CMIMaterial c = CMIColors.getColorMaterial(one);

                    if (c == null || c.equals(colorMat))
                        continue;

                    Recipe.put(1, new CMIRecipeIngredient(one.newItemStack()));
                    Recipe.put(2, new CMIRecipeIngredient(colorMat.newItemStack()));
                    rec = CMIRecipe.makeShaplessRecipe(this.getItemStack(), Recipe);
                    if (rec != null)
                        recipes.add(rec);
                }
            }
        }
        if (this.getCMIType().equals(CMIMaterial.FIREWORK_ROCKET)) {

            HashMap<Integer, CMIRecipeIngredient> Recipe = new HashMap<Integer, CMIRecipeIngredient>();

            Recipe.put(1, new CMIRecipeIngredient(CMIMaterial.PAPER.newItemStack()));
            Recipe.put(2, new CMIRecipeIngredient(CMIMaterial.GUNPOWDER.newItemStack()));
            org.bukkit.inventory.Recipe rec = CMIRecipe.makeShaplessRecipe(this.getItemStack(), Recipe);
            if (rec != null)
                recipes.add(rec);

            Recipe.put(3, new CMIRecipeIngredient(CMIMaterial.GUNPOWDER.newItemStack()));
            rec = CMIRecipe.makeShaplessRecipe(this.getItemStack(), Recipe);
            if (rec != null)
                recipes.add(rec);

            Recipe.put(4, new CMIRecipeIngredient(CMIMaterial.GUNPOWDER.newItemStack()));
            rec = CMIRecipe.makeShaplessRecipe(this.getItemStack(), Recipe);
            if (rec != null)
                recipes.add(rec);

            Recipe.put(5, new CMIRecipeIngredient(CMIMaterial.FIREWORK_STAR.newItemStack()));
            rec = CMIRecipe.makeShaplessRecipe(this.getItemStack(), Recipe);
            if (rec != null)
                recipes.add(rec);
        }
        return recipes;
    }

    public List<Recipe> getRecipesFrom() {
        List<Recipe> recipes = new ArrayList<Recipe>();

        ItemStack i = getItemStack();
        if (i == null)
            return recipes;
        i = i.clone();

        if (Version.isCurrentLower(Version.v1_13_R1))
            i.getData().setData((byte) data);
        if (i.getType().getMaxDurability() > 15)
            i.setDurability((short) 0);
        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe rec = it.next();
            if (rec == null)
                continue;
            for (ItemStack one : CMIRecipe.getIngredientsList(rec)) {

                if (one.getType() != i.getType()) {
                    continue;
                }

                if (one.getDurability() == -1 || one.getDurability() == Short.MAX_VALUE || one.getDurability() == i.getDurability()) {
                    recipes.add(rec);
                    break;
                }
            }

            if (rec instanceof ShapelessRecipe) {
                ShapelessRecipe srec = (ShapelessRecipe) rec;
                for (RecipeChoice one : srec.getChoiceList()) {
                    if (one != null && one.test(i)) {
                        recipes.add(rec);
                        break;
                    }
                }
            } else if (rec instanceof ShapedRecipe) {
                ShapedRecipe srec = (ShapedRecipe) rec;
                for (RecipeChoice one : srec.getChoiceMap().values()) {
                    if (one != null && one.test(i)) {
                        recipes.add(rec);
                        break;
                    }
                }
            }
        }

        return recipes;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public Set<Enchantment> getValidEnchants() {
        Set<Enchantment> enchants = new HashSet<Enchantment>();
        for (Enchantment one : CMIEnchantment.values()) {
            if (!CMIEnchantment.isEnabled(one))
                continue;
            if (one.canEnchantItem(this.getItemStack()))
                enchants.add(one);
        }
        return enchants;
    }

    public String toOneLiner() {
        return CMIItemSerializer.toOneLiner(this);
    }

    public static ItemStack getHead(String texture) {
        if (texture == null || texture.isEmpty())
            return null;
        if (texture.length() < 120)
            texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + texture;
        ItemStack cached = CMIEntityType.cache.get(texture);
        if (cached != null) {
            return cached.clone();
        }
        ItemStack item = CMIMaterial.PLAYER_HEAD.newItemStack();
        item = CMILib.getInstance().getReflectionManager().setSkullTexture(item, null, texture);
        CMIEntityType.cache.put(texture, item);
        return item.clone();
    }

    public void setEntityType(CMIEntityType entityType) {
        this.entityType = entityType;
    }

    public void setEntityType(EntityType entityType) {
        setEntityType(CMIEntityType.getByType(entityType));
    }

    public static ItemStack getItemInMainHand(Player player) {
        return CMILib.getInstance().getReflectionManager().getItemInMainHand(player);
    }

    public static ItemStack getItemInOffHand(Player player) {
        return CMILib.getInstance().getReflectionManager().getItemInOffHand(player);
    }

    public static void setItemInMainHand(Player player, ItemStack item) {
        CMILib.getInstance().getReflectionManager().setItemInMainHand(player, item);
    }

    public static void setItemInOffHand(Player player, ItemStack item) {
        CMILib.getInstance().getReflectionManager().setItemInOffHand(player, item);
    }

    public static CMIItemStack deserialize(String input) {
        return CMIItemSerializer.deserialize(null, input, null);
    }

    public static CMIItemStack deserialize(String input, CMIAsyncHead ahead) {
        return CMIItemSerializer.deserialize(null, input, ahead);
    }

    public static CMIItemStack deserialize(CommandSender sender, String input) {
        return CMIItemSerializer.deserialize(sender, input, null);
    }

    public static CMIItemStack deserialize(CommandSender sender, String input, CMIAsyncHead ahead) {
        return CMIItemSerializer.deserialize(sender, input, ahead);
    }

    public static String serialize(ItemStack item) {
        return CMIItemSerializer.serialize(item);
    }
}
