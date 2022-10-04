package net.Zrips.CMILib.Enchants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.FileHandler.ConfigReader;

public class CMIEnchantment {

    private static final Map<String, Enchantment> byName = new HashMap<String, Enchantment>();

    private static HashMap<String, String> enchantList = new HashMap<String, String>();
    private static HashMap<String, String> transaltedEnchantList = new HashMap<String, String>();

    public static void initialize() {

        ConfigReader locale = CMILib.getInstance().getConfigManager().getLocaleConfig();

        Enchantment[] enchants = Enchantment.values();
        HashMap<String, List<String>> eList = new HashMap<String, List<String>>();
        eList.put("protection_environmental", Arrays.asList("Protection"));
        eList.put("protection_fire", Arrays.asList("FireProtection"));
        eList.put("protection_fall", Arrays.asList("FallProtection", "FeatherFalling"));
        eList.put("protection_explosions", Arrays.asList("BlastProtection"));
        eList.put("protection_projectile", Arrays.asList("ProjectileProtection"));
        eList.put("oxygen", Arrays.asList("Respiration"));
        eList.put("water_worker", Arrays.asList("AquaAffinity"));
        eList.put("thorns", Arrays.asList("Thorns"));
        eList.put("vanishing_curse", Arrays.asList("VanishingCurse"));
        eList.put("depth_strider", Arrays.asList("DepthStrider"));
        eList.put("damage_all", Arrays.asList("Sharpness"));
        eList.put("damage_undead", Arrays.asList("Smite"));
        eList.put("damage_arthropods", Arrays.asList("BaneOfArthropods"));
        eList.put("knockback", Arrays.asList("Knockback"));
        eList.put("fire_aspect", Arrays.asList("FireAspect"));
        eList.put("frost_walker", Arrays.asList("FrostWalker"));
        eList.put("loot_bonus_mobs", Arrays.asList("Looting"));
        eList.put("dig_speed", Arrays.asList("Efficiency"));
        eList.put("silk_touch", Arrays.asList("SilkTouch"));
        eList.put("durability", Arrays.asList("Unbreaking"));
        eList.put("loot_bonus_blocks", Arrays.asList("Fortune"));
        eList.put("arrow_damage", Arrays.asList("Power"));
        eList.put("arrow_knockback", Arrays.asList("Punch"));
        eList.put("arrow_fire", Arrays.asList("Flame"));
        eList.put("arrow_infinite", Arrays.asList("Infinity"));
        eList.put("luck", Arrays.asList("Luck"));
        eList.put("lure", Arrays.asList("Lure"));
        eList.put("mending", Arrays.asList("Mending"));
        eList.put("sweeping_edge", Arrays.asList("SweepingEdge"));
        eList.put("binding_curse", Arrays.asList("BindingCurse"));
        eList.put("loyalty", Arrays.asList("Loyalty"));
        eList.put("piercing", Arrays.asList("Piercing"));
        eList.put("multishot", Arrays.asList("Multishot"));
        eList.put("channeling", Arrays.asList("Channeling"));
        eList.put("riptide", Arrays.asList("Riptide"));
        eList.put("quickcharge", Arrays.asList("QuickCharge"));
        eList.put("impaling", Arrays.asList("Impaling"));

        transaltedEnchantList.clear();

        for (Enchantment one : enchants) {
            try {
                if (one == null)
                    continue;

                String name = one.getName();
                if (name == null)
                    continue;
                if (name.isEmpty())
                    continue;
                if (name == " ")
                    continue;

                if (!eList.containsKey(name.toLowerCase())) {

                    try {
                        String remade = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                        String r = "";
                        for (String oneS : name.split("_")) {
                            r += oneS.substring(0, 1).toUpperCase() + oneS.substring(1, oneS.length());
                        }
                        if (!r.isEmpty())
                            remade = r;

                        List<String> ls = locale.get("info.EnchantAliases." + name.toLowerCase(), Arrays.asList(remade));
                        for (String oneE : ls) {
                            CMIEnchantment.enchantList.put(oneE.replace("_", "").replace(" ", "").toLowerCase(), name.toLowerCase());
                        }
                        continue;
                    } catch (Exception e) {
                        continue;
                    }
                }

                List<String> ls = locale.get("info.EnchantAliases." + name.toLowerCase(), eList.get(name.toLowerCase()));
                for (String oneE : ls) {
                    if (!transaltedEnchantList.containsKey(name.replace("_", "").toLowerCase()))
                        transaltedEnchantList.put(name.replace("_", "").toLowerCase(), oneE);
                    CMIEnchantment.enchantList.put(oneE.replace("_", "").replace(" ", "").toLowerCase(), name.toLowerCase());
                }
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveEnchants() {
        for (Enchantment one : Enchantment.values()) {
            if (one == null || one.getName() == null)
                continue;
            byName.put(one.getName().replace("_", "").replace(" ", "").toLowerCase(), one);
        }
    }

    public static Enchantment get(String nameId) {
        Enchantment enchant = getByName(nameId);
        return enchant;
    }

    public static Enchantment getByName(String name) {
        name = name.replace("_", "").replace(" ", "").toLowerCase();

        String tp = enchantList.get(name);
        if (tp != null)
            name = tp;
        name = name.replace("_", "").replace(" ", "").toLowerCase();

        Enchantment got = byName.get(name);
        if (got != null)
            return got;

        if (name.contains(":")) {
            String[] split = name.split(":", 2);
            Enchantment enchant = Enchantment.getByKey(new NamespacedKey(split[0], split[1]));
            if (enchant != null)
                return enchant;
        }

        return null;
    }

    public static boolean isEnabled(Enchantment enchant) {
        if (enchant == null || enchant.getName() == null)
            return false;
        for (Enchantment one : Enchantment.values()) {
            if (one == null || one.getName() == null)
                continue;
            if (one.getName().equalsIgnoreCase(enchant.getName()))
                return true;
        }
        return false;
    }

    public static Enchantment[] values() {
        return byName.values().toArray(new Enchantment[byName.size()]);
    }

    public static String getName(Enchantment enchant) {
        if (enchant == null || enchant.getName() == null)
            return null;

        String name = enchant.getName().replace(" ", "").replace("_", "");

        String got = enchantList.get(name.toLowerCase());

        if (got != null) {
            return got;
        }

        for (Entry<String, String> oneEntry : enchantList.entrySet()) {
            if (oneEntry.getValue().replace("_", "").replace(" ", "").equalsIgnoreCase(name))
                if (!oneEntry.getValue().isEmpty())
                    return oneEntry.getValue();
        }
        return CMIText.firstToUpperCase(enchant.getName());
    }

    public static String getTranslatedName(Enchantment enchant) {
        if (enchant == null || enchant.getName() == null)
            return null;

        String name = enchant.getName().replace(" ", "").replace("_", "");

        String got = transaltedEnchantList.get(name.toLowerCase());
        if (got != null) {
            return got;
        }

        return CMIText.firstToUpperCase(enchant.getName());
    }

    public static HashMap<String, String> getEnchantList() {
        return enchantList;
    }
}
