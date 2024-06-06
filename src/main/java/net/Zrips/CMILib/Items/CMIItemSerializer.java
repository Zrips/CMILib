package net.Zrips.CMILib.Items;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Attributes.AttSlot;
import net.Zrips.CMILib.Attributes.Attribute;
import net.Zrips.CMILib.Attributes.AttributeType;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMINumber;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.Container.LeatherAnimationType;
import net.Zrips.CMILib.Enchants.CMIEnchantment;
import net.Zrips.CMILib.Entities.CMIEntityType;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Skins.CMISkin;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class CMIItemSerializer {

    static String prefix = "{";
    static String suffix = "}";

    static Pattern pname = Pattern.compile("^(?i)(name|n)\\" + prefix);
    static Pattern plore = Pattern.compile("^(?i)(lore|l)\\" + prefix);
    static Pattern pcolor = Pattern.compile("^(?i)(c)\\" + prefix);
    static Pattern penchant = Pattern.compile("^(?i)(e)\\" + prefix);
    static Pattern pmodel = Pattern.compile("^(?i)(custommodeldata|custommodel|cm|cmd)\\" + prefix);

    private static int getOperation(String value) {

        String simplified = value.replace("_", "").toLowerCase();
        switch (simplified) {
        case "add":
            return 0;
        case "multiplybase":
            return 1;
        case "multiply":
            return 2;
        default:
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    private static ConcurrentHashMap<String, ItemStack> headCache = new ConcurrentHashMap<String, ItemStack>();

    private final static Pattern ownExpresion = Pattern.compile("(;.+\\{)");
    private final static Pattern ptr = Pattern.compile("(\\{).+(\\})");

    private static void set(String id, ItemStack lskull, String playerName, CMIAsyncHead ahead) {
        CMINBT nbt = new CMINBT(lskull);
        lskull = (ItemStack) nbt.setString("SkullOwner.Name", playerName);

        // Forcing server to load skin information
        Bukkit.createInventory(null, InventoryType.CHEST, "").addItem(lskull);

        SkullMeta smeta = (SkullMeta) lskull.getItemMeta();
        lskull.setItemMeta(smeta);
        headCache.put(id, lskull);
        if (ahead != null)
            ahead.afterAsyncUpdate(lskull);
    }

    private static ItemStack applySkin(CMISkin skin, ItemStack lskull) {
        if (skin == null)
            return lskull;
        return CMILib.getInstance().getReflectionManager().setSkullTexture(lskull, skin.getName(), skin.getSkin());
    }

    private static CMIItemStack getItem(String name, CMIAsyncHead ahead) {

        if (name == null)
            return null;

        CMIItemStack cm = null;

        if (name.toLowerCase().startsWith("minecraft:"))
            name = name.substring("minecraft:".length(), name.length());

        String original = name;
        int amount = 0;
        CMIEntityType entityType = null;

        Matcher match = ownExpresion.matcher(name);

        String tag = null;

        if (!match.find() && name.contains("{") && name.contains("}")) {
            Matcher matcher = ptr.matcher(name);
            if (matcher.find()) {
                tag = matcher.group();
                name = name.replace(matcher.group(), "");
            }
            name = name.replace("  ", " ");
        }

        name = name.toLowerCase();

        if (name.contains("-")) {
            String[] split = name.split("-");
            if (split.length > 1) {
                String a = split[split.length - 1];
                try {
                    amount = Integer.parseInt(a);
                    name = name.substring(0, name.length() - (split[split.length - 1].length() + 1));
                } catch (Exception e) {
                }
            }
        }

        String subdata = null;
        if (name.contains(":") || name.contains("-")) {
            CMIMaterial mat = CMILib.getInstance().getItemManager().NameMap().get(name);
            if (mat != null)
                return new CMIItemStack(mat);
            try {
                if (name.contains(":"))
                    subdata = name.split(":")[1];
                else
                    subdata = name.split("-")[1];
            } catch (Throwable e) {
            }
        }

        if (subdata != null) {
            try {
                entityType = CMIEntityType.get((short) Integer.parseInt(subdata));
            } catch (Exception e) {
            }
            if (subdata.equalsIgnoreCase("random") && !CMILibConfig.mysterySpawners.isEmpty()) {
                Collections.shuffle(CMILibConfig.mysterySpawners);
                entityType = CMIEntityType.get(CMILibConfig.mysterySpawners.get(0));
            } else {
                entityType = CMIEntityType.get(subdata);
            }
            name = name.split(":")[0];
            name = name.split("-")[0];
        }

        switch (name.toLowerCase()) {
        case "skull":
            cm = CMIMaterial.SKELETON_SKULL.newCMIItemStack();
            break;
        case "door":
            cm = CMIMaterial.SPRUCE_DOOR.newCMIItemStack();
            break;
        case "head":
        case "playerhead":
        case "player_head":
            cm = CMIMaterial.PLAYER_HEAD.newCMIItemStack();

            if (!original.contains(":"))
                break;

            ItemStack old = headCache.get(original);

            if (old != null && (ahead == null || !ahead.isIgnoreCached())) {
                cm.setItemStack(old);
                break;
            }

            String[] split = original.split(":");

            if (split.length <= 1)
                break;

            String d = split[1];

            if (d.length() > 36 || d.startsWith("eyJ0ZXh0dXJlcy")) {
                String texture = d;
                if (texture.contains("-")) {
                    try {
                        String[] sp = texture.split("-");
                        Double.parseDouble(sp[sp.length - 1]);
                        texture = texture.substring(0, texture.length() - 1 - sp[sp.length - 1].length());
                    } catch (Throwable e) {
                    }
                }
                ItemStack skull = CMIItemStack.getHead(texture);
                if (ahead != null && skull != null)
                    headCache.put(original, skull);
                cm.setItemStack(skull);
                break;
            }

            ItemStack skull = CMIMaterial.PLAYER_HEAD.newItemStack();
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                CMIEntityType type = CMIEntityType.get(d);

                if (type != null) {
                    skullMeta = (SkullMeta) type.getHead().getItemMeta();
                    skull.setItemMeta(skullMeta);
                    cm.setItemStack(skull);
                    subdata = null;
                    break;
                }

                if (ahead != null)
                    ahead.setAsyncHead(true);

                CMIScheduler.runTaskAsynchronously(() -> {
                    CMISkin skin = CMILib.getInstance().getSkinManager().getSkin(d);
                    ItemStack s = applySkin(skin, skull);
                    CMINBT nbt = new CMINBT(s);
                    if (skin != null)
                        nbt.setString("SkullOwner.Name", skin.getName());

                    if (ahead != null)
                        ahead.afterAsyncUpdate(s);
                    headCache.put(original, s);
                });

            } else {

                if (d.length() == 36) {
                    try {
                        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(UUID.fromString(d));

                        if (offPlayer == null || offPlayer.getName() == null) {

                            if (ahead != null) {
                                ahead.setAsyncHead(true);
                            }

                            CMIScheduler.runTaskAsynchronously(() -> {
                                OfflinePlayer offlineP = Bukkit.getOfflinePlayer(d);
                                if (offlineP != null) {
                                    ItemStack lskull = skull;
                                    SkullMeta lskullMeta = (SkullMeta) lskull.getItemMeta();
                                    lskullMeta.setOwningPlayer(offlineP);
                                    lskull.setItemMeta(lskullMeta);
                                    if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                                        applySkin(CMILib.getInstance().getSkinManager().getSkin(d), lskull);
                                    }

                                    set(original, lskull, offlineP.getName(), ahead);
                                }
                            });

                        } else {
                            skullMeta.setOwningPlayer(offPlayer);
                            skull.setItemMeta(skullMeta);
                            cm.setItemStack(skull);
                            if (ahead != null)
                                headCache.put(original, skull);
                        }
                    } catch (Exception e) {
                    }
                    break;
                }

                if (Version.isCurrentEqualOrHigher(Version.v1_16_R3)) {
                    if ((ahead != null && !ahead.isForce() || ahead == null) && Bukkit.getPlayer(d) != null) {
                        Player player = Bukkit.getPlayer(d);
                        skullMeta.setOwningPlayer(player);
                        skull.setItemMeta(skullMeta);
                        if (ahead != null) {
                            ahead.setAsyncHead(true);
                            CMIScheduler.runTaskAsynchronously(() -> {
                                ahead.afterAsyncUpdate(skull);
                            });
                            headCache.put(original, skull);
                        }
                    } else {
                        CMIEntityType type = CMIEntityType.getByName(d);

                        if (type != null) {
                            skullMeta = (SkullMeta) type.getHead().getItemMeta();
                            skull.setItemMeta(skullMeta);
                            cm.setItemStack(skull);
                            subdata = null;
                        } else {
                            if (ahead != null) {
                                ahead.setAsyncHead(true);
                            }
                            CMIScheduler.runTaskAsynchronously(() -> {
                                OfflinePlayer offlineP = Bukkit.getOfflinePlayer(d);
                                if (offlineP == null)
                                    return;
                                ItemStack lskull = skull;
                                SkullMeta lskullMeta = (SkullMeta) lskull.getItemMeta();
                                lskullMeta.setOwningPlayer(offlineP);
                                lskull.setItemMeta(lskullMeta);

                                if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                                    applySkin(CMILib.getInstance().getSkinManager().getSkin(d), lskull);
                                }
                                set(original, lskull, offlineP.getName(), ahead);
                            });
                        }
                    }

                } else {
                    skullMeta.setOwner(d);
                    skull.setItemMeta(skullMeta);
                    cm.setItemStack(skull);
                    if (ahead != null)
                        headCache.put(original, skull);
                }

                if (ahead == null || !ahead.isAsyncHead()) {
                    skull.setItemMeta(skullMeta);
                    if (ahead != null)
                        headCache.put(original, skull);
                }
            }

            break;
        }

        CMIMaterial cmat = CMIMaterial.get(subdata == null ? name : name + ":" + subdata);

        if (cmat == null || cmat.isNone()) {
            cmat = CMIMaterial.get(name);
        }

        if (cmat != null && !cmat.isNone() && (cm == null || cm.getCMIType().isNone())) {
            cm = cmat.newCMIItemStack();
        } else
            cmat = CMIMaterial.get(subdata == null ? original : original + ":" + subdata);

        if (cmat != null && !cmat.equals(CMIMaterial.NONE) && (cm == null || cm.getCMIType().isNone()))
            cm = cmat.newCMIItemStack();

        if (cm == null) {
            Material mat = ItemManager.getMaterialByName(original);
            if (mat != null && new CMIItemStack(mat).getItemStack() != null) {
                cm = new CMIItemStack(mat);
            }
        }

        if (cm != null && entityType != null) {
            cm.setEntityType(entityType);
        }
        CMIItemStack ncm = null;
        if (cm != null)
            ncm = cm.clone();

        if (ncm != null && tag != null) {
            ncm.setTag(CMIChatColor.translate(tag));
        }

        if (ncm != null && amount != 0)
            ncm.setAmount(amount);

        if (ncm == null || subdata == null)
            return ncm;

        // Outdated way
        if (ncm.getCMIType().isPotion() || ncm.getCMIType().equals(CMIMaterial.SPLASH_POTION) || ncm.getCMIType().equals(CMIMaterial.TIPPED_ARROW)) {
            applyPotionEffect(ncm, subdata.split("-"));
            return ncm;
        }

        // Outdated way
        if (ncm.getItemStack() != null && ncm.getItemStack().getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) ncm.getItemStack().getItemMeta();

            List<String> split = new ArrayList<String>();
            if (subdata.contains(";"))
                split.addAll(Arrays.asList(subdata.split(";")));
            else
                split.add(subdata);

            for (String one : split) {

                Enchantment type = null;
                Integer level = 1;
                if (one.contains("x")) {
                    try {
                        level = Integer.parseInt(one.split("x")[one.split("x").length - 1]);
                        type = CMIEnchantment.get(one.substring(0, one.length() - ("x" + level).length()));
                    } catch (Exception e) {
                    }
                } else
                    type = CMIEnchantment.get(one);

                if (type == null)
                    continue;
                meta.addStoredEnchant(type, level, true);
            }
            ncm.getItemStack().setItemMeta(meta);
        }

        return ncm;
    }

    private static final Pattern datePattern = Pattern.compile("\\%date/(.*?)\\%");
    private static final Pattern randomPattern = Pattern.compile("\\%rand/(.*?)\\%");
    private static final NumberFormat nummberFormatter = new DecimalFormat("#0.0");

    private static String updateCustomVariables(String input) {

        Matcher m = datePattern.matcher(input);
        while (m.find()) {
            try {
                input = input.replace("%date/" + m.group(1) + "%", new SimpleDateFormat(m.group(1)).format(Calendar.getInstance().getTime()));
            } catch (Exception e) {
                continue;
            }
        }

        m = randomPattern.matcher(input);
        while (m.find()) {
            if (!m.group(1).contains("-"))
                continue;
            Random rand = new Random(System.nanoTime());
            try {
                String fromString = m.group(1).split("-")[0];
                String toString = m.group(1).split("-")[1];
                double multy = 1;
                if (fromString.contains(".")) {
                    multy = 10 ^ fromString.split("\\.")[1].length();
                }
                int from = (int) ((Double.parseDouble(fromString)) * multy);
                int to = (int) ((Double.parseDouble(toString)) * multy) + 1;
                if (from >= to)
                    continue;
                double got = (rand.nextInt(to - from) + from) / multy;
                if (got - (int) got > 0)
                    input = input.replace("%rand/" + m.group(1) + "%", nummberFormatter.format(got) + "");
                else
                    input = input.replace("%rand/" + m.group(1) + "%", (int) got + "");
            } catch (Exception e) {
                continue;
            }
        }

        return input;
    }

    public static CMIItemStack deserialize(String input) {
        return deserialize(null, input, null);
    }

    public static CMIItemStack deserialize(String input, CMIAsyncHead ahead) {
        return deserialize(null, input, ahead);
    }

    public static CMIItemStack deserialize(CommandSender sender, String input) {
        return deserialize(sender, input, null);
    }

    private static Random random = new Random();
    private static String tempReplacer = null;

    public static CMIItemStack deserialize(CommandSender sender, String input, CMIAsyncHead ahead) {

        if (input == null)
            return null;

        input = input.replace(" ", "_");

        input = updateCustomVariables(input);

        String itemName = input.contains(";") ? input.split(";", 2)[0] : input;

        String tag = null;

        if (Version.isCurrentEqualOrLower(Version.v1_20_R3)) {
            if (itemName.endsWith("}") && itemName.contains("{")) {
                tag = "{" + itemName.split("\\{", 2)[1];
                itemName = itemName.split("\\{", 2)[0];
            }
        } else {
            if (itemName.endsWith("]") && itemName.contains("[")) {
                tag = "{" + itemName.split("\\[", 2)[1];
                tag = tag.substring(0, tag.length() - 1) + "}";
                itemName = itemName.split("\\[", 2)[0];
            }
        }

        String itemNameUpdated = itemName.contains("-") || itemName.toLowerCase().startsWith("head:") || itemName.toLowerCase().startsWith("player_head:") ? itemName : itemName.replace(":", "-");

        CMIItemStack cim = getItem(itemNameUpdated, ahead);

        if (cim == null)
            return cim;

        if (input.contains(";")) {

            String extra = input.split(";", 2)[1];

            List<String> temp = Arrays.asList(extra.split(suffix + ";"));

            tempReplacer = "|" + random.nextInt() + "|";

            List<String> s = new ArrayList<String>();

            for (String one : temp) {
                one = one.replace(";;", tempReplacer);
                s.addAll(Arrays.asList(one.split(";")));
            }

            for (int i = 0; i < s.size(); i++) {
                String one = s.get(i);

                if (i == s.size() - 1 && one.endsWith(suffix))
                    one = one.substring(0, one.length() - 1);

                if (applyAmount(sender, cim, one))
                    continue;

                if (applySpecials(cim, one))
                    continue;

                if (applyEnchants(cim, one))
                    continue;

                if (applyColor(cim, one))
                    continue;

                if (Version.isCurrentEqualOrHigher(Version.v1_8_R1) && applyFlags(cim, one))
                    continue;

                if (Version.isCurrentEqualOrHigher(Version.v1_9_R1) && applyAttributes(cim, one))
                    continue;

                if (Version.isCurrentEqualOrHigher(Version.v1_16_R1) && applyCustomModel(cim, one))
                    continue;

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                    if (applyTrim(cim, one))
                        continue;
                    if (applySherd(cim, one))
                        continue;
                }

                if (applyPainting(cim, one))
                    continue;

                if (applyHorn(cim, one))
                    continue;

                if (applyPotionEffect(cim, one.split(":")))
                    continue;

                if (applyEntityType(cim, one))
                    continue;

                // Should be last ones to check due to option of them not having identificators and having random text
                if (applyName(sender, cim, one))
                    continue;

                if (applyLore(sender, cim, one))
                    continue;

            }
        }

        if (tag != null)
            cim.setTag(CMIChatColor.translate(tag));

        tempReplacer = null;

        return cim;
    }

    private static boolean applyEntityType(CMIItemStack cim, String value) {

        if (!cim.getCMIType().equals(CMIMaterial.SPAWNER))
            return false;

        if (cim.getEntityType() != null)
            return false;

        CMIEntityType type = CMIEntityType.getByName(value);

        if (type == null)
            return false;

        cim.setEntityType(type);

        return true;
    }

    private static boolean applyAmount(CommandSender sender, CMIItemStack cim, String value) {

        if (cim.getAmount() != 1)
            return false;

        if (value.startsWith("%")) {
            value = CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(sender instanceof Player ? (Player) sender : null, value);
        }

        try {
            cim.setAmount(CMINumber.clamp(Integer.parseInt(value), 1, 999));
            return true;
        } catch (Throwable e) {

        }
        return false;
    }

    private static boolean applySpecials(CMIItemStack cim, String value) {
        switch (value.toLowerCase()) {
        case "unbreakable":
            cim.setItemStack((ItemStack) new CMINBT(cim.getItemStack()).setByte("Unbreakable", (byte) 1));
            return true;
        }
        return false;
    }

    private static boolean applyName(CommandSender sender, CMIItemStack cim, String value) {
        Matcher nameMatch = pname.matcher(value);
        if (!nameMatch.find()) {
            if (cim.getItemStack().hasItemMeta() && cim.getItemStack().getItemMeta().hasDisplayName())
                return false;

            if (value.contains("\\\\n") || value.contains("\\n"))
                return false;

            return subApplyName(sender, cim, value);
        }
        return subApplyName(sender, cim, value.substring(nameMatch.group().length()));
    }

    private static boolean subApplyName(CommandSender sender, CMIItemStack cim, String name) {
        name = CMIText.replaceUnderScoreSpace(name);
        name = CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(sender instanceof Player ? (Player) sender : null, name);
        if (tempReplacer != null)
            name = name.replace(tempReplacer, ";");
        if (name == null)
            return false;
        cim.setDisplayName(name);

        return true;
    }

    private static boolean applyLore(CommandSender sender, CMIItemStack cim, String value) {
        if (!cim.getLore().isEmpty())
            return false;

        Matcher loreMatch = plore.matcher(value);

        if (!loreMatch.find()) {
            if (cim.getItemStack().hasItemMeta() && !cim.getItemStack().getItemMeta().hasDisplayName())
                return false;
            return subApplyLore(sender, cim, value);
        }

        return subApplyLore(sender, cim, value.substring(loreMatch.group().length()));
    }

    private static boolean subApplyLore(CommandSender sender, CMIItemStack cim, String lore) {

        lore = CMIText.replaceUnderScoreSpace(lore);
        lore = CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(sender instanceof Player ? (Player) sender : null, lore);
        if (tempReplacer != null)
            lore = lore.replace(tempReplacer, ";");
        if (lore != null) {
            lore = lore.replace("\\n", "\n");
            cim.setLore(Arrays.asList(lore.split("\\n")));
            return true;
        }
        return false;
    }

    private static boolean applyPotionEffect(CMIItemStack cim, String[] split) {

        if (!cim.getCMIType().isPotion() && !cim.getCMIType().equals(CMIMaterial.TIPPED_ARROW))
            return false;

        PotionType potionType = null;
        Boolean upgraded = null;
        Boolean extended = null;

        for (String one : split) {
            if (potionType == null) {
                PotionEffectType type = CMIPotionEffectType.get(one);
                if (type != null) {
                    potionType = CMIPotionType.get(type);
                    if (potionType != null)
                        continue;
                }
                potionType = CMIPotionType.get(one);
                if (potionType != null)
                    continue;
            }

            if (one.equalsIgnoreCase("true")) {
                if (upgraded == null)
                    upgraded = true;
                else if (extended == null)
                    extended = true;
            } else if (one.equalsIgnoreCase("false")) {
                if (upgraded == null)
                    upgraded = false;
                else if (extended == null)
                    extended = false;
            }
        }

        if (potionType == null)
            return false;

        upgraded = upgraded == null ? false : upgraded;
        extended = extended == null ? false : extended;

        if (extended && upgraded)
            extended = false;

        ItemStack item = cim.getItemStack();

        potionType = CMIPotionType.get(potionType, upgraded, extended);

        try {
            PotionMeta meta = (PotionMeta) item.getItemMeta();

            if (CMIMaterial.TIPPED_ARROW.equals(CMIMaterial.get(item))) {
                if (!potionType.isExtendable())
                    extended = false;
                if (!potionType.isUpgradeable())
                    upgraded = false;
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4))
                meta.setBasePotionType(potionType);
            else
                meta.setBasePotionData(new PotionData(potionType, extended, upgraded));

            item.setItemMeta(meta);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean applyEnchants(CMIItemStack cim, String value) {

        if (!value.contains(":"))
            return false;

        Matcher mMatch = penchant.matcher(value);
        if (mMatch.find()) {
            value = value.substring(mMatch.group().length());
        }

        boolean added = false;
        for (String OE : value.split(",")) {
            if (!OE.contains(":"))
                continue;
            int level = 0;
            String[] split = OE.split(":", 2);
            try {
                level = Integer.parseInt(split[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            Enchantment enc = CMIEnchantment.get(split[0]);
            if (enc == null)
                continue;
            cim.addEnchant(enc, level);
            added = true;
        }
        return added;
    }

    private static boolean applyAttributes(CMIItemStack cim, String value) {

        if (!value.contains(":"))
            return false;

        List<Attribute> attList = new ArrayList<Attribute>();

        for (String OE : value.split(",")) {

            String[] split = OE.split(":");

            AttributeType attribute = AttributeType.get(split[0]);
            AttSlot slot = null;
            double attributeMod = -1D;
            int operation = -1;

            for (int ai = 1; ai < split.length; ai++) {

                if (slot == null) {
                    slot = AttSlot.get(split[ai]);
                    if (slot != null)
                        continue;
                }

                if (attributeMod == -1) {
                    try {
                        attributeMod = Double.parseDouble(split[ai]);
                        continue;
                    } catch (Throwable e) {
                    }
                }
                if (operation == -1) {
                    operation = getOperation(split[ai]);
                }
            }

            try {
                attributeMod = Double.parseDouble(split[1]);
            } catch (NumberFormatException e) {
                continue;
            }

            if (attributeMod <= 0)
                continue;

            if (attribute != null && attributeMod != -1D) {
                attList.add(new Attribute(attribute, slot, attributeMod, CMINumber.clamp(operation, 0, 2)));
            }
        }
        if (attList.isEmpty())
            return false;

        cim.addAttributes(attList);
        return true;
    }

    private static boolean applyCustomModel(CMIItemStack cim, String value) {
        Matcher mMatch = pmodel.matcher(value);
        if (!mMatch.find())
            return false;

        String f = value.substring(mMatch.group().length());

        try {
            int data = Integer.parseInt(f);
            cim.setNbt("CustomModelData", data);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean applyPainting(CMIItemStack cim, String value) {

        if (!cim.getCMIType().equals(CMIMaterial.PAINTING))
            return false;

        value = value.replace("_", "");

        try {

            Art art = null;

            for (Art one : Art.values()) {
                String artName = one.toString().replace("_", "");
                if (!artName.equalsIgnoreCase(value))
                    continue;
                art = one;
                break;
            }
            if (art == null)
                return false;

            try {
                CMINBT nbt = new CMINBT(cim.getItemStack());
                CMINBT stag = new CMINBT(nbt.getCompound("EntityTag"));
                stag.setString("variant", "minecraft:" + art.toString().toLowerCase());
                cim.setItemStack((ItemStack) nbt.set("EntityTag", stag.getNbt()));
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } catch (Throwable e) {

        }

        return false;
    }

    private static boolean applyHorn(CMIItemStack cim, String value) {

        if (!cim.getCMIType().equals(CMIMaterial.GOAT_HORN))
            return false;

        value = value.replace("_", "");

        try {
            CMIMusicInstrument instrument = CMIMusicInstrument.get(value);
            if (instrument == null)
                return false;
            try {
                cim.setNbt("instrument", "minecraft:" + instrument.toString().toLowerCase() + "_goat_horn");
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } catch (Throwable e) {

        }

        return false;
    }

    private static boolean applyColor(CMIItemStack cim, String value) {
        if (!cim.getCMIType().containsCriteria(CMIMC.COLORED))
            return false;

        Matcher mMatch = pcolor.matcher(value);
        if (mMatch.find()) {
            value = value.substring(mMatch.group().length());
        }

        String[] split = value.split(",");
        CMIChatColor cmic = null;
        int[] colors = new int[3];
        int ii = 0;

        for (String c : split) {
            if (split.length == 3) {
                try {
                    colors[ii] = Integer.parseInt(c);
                } catch (Throwable e) {
                    continue;
                }
                ii++;
                continue;
            }
            if (c.startsWith("#"))
                c = c.substring(1);
            cmic = CMIChatColor.getByHex(c);
            if (cmic == null) {
                cmic = CMIChatColor.getByCustomName(c);
            }

            if (cmic == null && CMILib.getInstance().isCmiPresent()) {
                LeatherAnimationType custom = LeatherAnimationType.getByName(c);
                if (custom != null) {
                    continue;
                }
            }

            if (cmic == null) {
                cmic = new CMIChatColor(c);
            }
        }

        if (split.length == 3) {
            cmic = new CMIChatColor("temp", "z".charAt(0), colors[0], colors[1], colors[2]);
        }

        if (cmic == null || !cmic.isValid())
            return false;

//        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
//            org.bukkit.inventory.meta.ColorableArmorMeta meta = (org.bukkit.inventory.meta.ColorableArmorMeta) cim.getItemStack().getItemMeta();
//            meta.setColor(cmic.getRGBColor());
//            cim.getItemStack().setItemMeta(meta);
//            return true;
//        }

        LeatherArmorMeta meta = (LeatherArmorMeta) cim.getItemStack().getItemMeta();
        meta.setColor(cmic.getRGBColor());
        cim.getItemStack().setItemMeta(meta);
        return true;
    }

    private static boolean applyFlags(CMIItemStack cim, String value) {
        List<ItemFlag> flags = new ArrayList<ItemFlag>();

        for (String OE : value.split(",")) {
            ItemFlag itemFlag = getitemFlag(OE);
            if (itemFlag == null)
                continue;
            flags.add(itemFlag);
        }

        if (flags.isEmpty())
            return false;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            ItemMeta meta = cim.getItemStack().getItemMeta();
            meta.addItemFlags(flags.toArray(new ItemFlag[0]));
            cim.getItemStack().setItemMeta(meta);
            return true;
        }

        int tagg = 0;
        for (ItemFlag oneF : flags) {
            tagg |= getBitModifier(oneF);
        }
        for (ItemFlag oneF : cim.getItemStack().getItemMeta().getItemFlags()) {
            tagg |= getBitModifier(oneF);
        }

        cim.getItemStack().setItemMeta(CMINBT.HideFlag(cim.getItemStack(), tagg).getItemMeta());
        return true;
    }

    private static boolean applySherd(CMIItemStack cim, String value) {
        if (!cim.getCMIType().equals(CMIMaterial.DECORATED_POT))
            return false;

        List<String> sherds = new ArrayList<String>();
        for (String oneSherd : value.split(",")) {
            if (!oneSherd.toLowerCase().endsWith("_pottery_sherd"))
                oneSherd += "_pottery_sherd";
            CMIMaterial sherd = CMIMaterial.get(oneSherd);
            if (sherd.toString().endsWith("_SHERD")) {
                sherds.add("minecraft:" + sherd.toString().toLowerCase());
            }
        }

        sherds.subList(4, sherds.size()).clear();

        if (sherds.isEmpty())
            return false;

        try {
            CMINBT nbt = new CMINBT(cim.getItemStack());
            CMINBT stag = new CMINBT(nbt.getCompound("BlockEntityTag"));
            stag.setStringList("sherds", sherds);
            cim.setItemStack((ItemStack) nbt.set("BlockEntityTag", stag.getNbt()));
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean applyTrim(CMIItemStack cim, String value) {

        if (!cim.getCMIType().isArmor())
            return false;

        String[] split = value.split(":");
        if (split.length != 2)
            return false;

        try {
            ItemMeta meta = cim.getItemStack().getItemMeta();
            org.bukkit.inventory.meta.ArmorMeta ameta = (ArmorMeta) meta;
            org.bukkit.inventory.meta.trim.TrimMaterial trim = CMITrimMaterial.getByName(split[0]);
            org.bukkit.inventory.meta.trim.TrimPattern pattern = CMITrimPattern.getByName(split[1]);

            if (trim != null && pattern != null) {
                org.bukkit.inventory.meta.trim.ArmorTrim teim = new org.bukkit.inventory.meta.trim.ArmorTrim(trim, pattern);
                ameta.setTrim(teim);
                cim.getItemStack().setItemMeta(meta);
                return true;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String serialize(ItemStack item) {
        StringBuilder str = new StringBuilder();

        if (item == null)
            return str.toString();

        CMIMaterial cmim = CMIMaterial.get(item);
        String material = cmim.toString();

        if (cmim.isPlayerHead()) {
            String base = null;
            // Trying to extract head data and its owner name
            try {
                CMINBT nbt = new CMINBT(item);
                List<String> ls = nbt.getList("SkullOwner.Properties.textures");
                if (ls != null) {
                    base = ls.get(0).split("Value:\"", 2)[1].split("\"", 2)[0];
                    CMIEntityType entType = CMIEntityType.getByTexture(base);
                    if (entType != null) {
                        material += ":" + entType.toString();
                    } else {
                        material += ":" + base;
                    }
                } else {
                    int[] idArray = nbt.getIntArray("SkullOwner.Id");
                    if (idArray != null && idArray.length == 4) {
                        material += ":" + new UUID(((long) idArray[0] << 32) | idArray[1], ((long) idArray[2] << 32) | idArray[3]).toString();
                    } else {
                        String name = nbt.getString("SkullOwner.Name");
                        if (name != null)
                            material += ":" + name;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (cmim.equals(CMIMaterial.SPAWNER)) {
            EntityType t = new CMIItemStack(item).getEntityType();
            if (t != null)
                material += ":" + t.toString();
        } else if (cmim.isPotion() || item.getType().name().contains("TIPPED_ARROW")) {
            PotionMeta potion = (PotionMeta) item.getItemMeta();
            try {
                if (potion != null && potion.getBasePotionData() != null && potion.getBasePotionData().getType() != null && potion.getBasePotionData().getType().getEffectType() != null) {
                    material += ":" + potion.getBasePotionData().getType().getEffectType().getName() + "-" + potion.getBasePotionData().isUpgraded() + "-" + potion.getBasePotionData().isExtended();
                }
            } catch (NoSuchMethodError e) {
            }
        }

        str.append(material);

        if (item.getAmount() > 1)
            str.append("-" + item.getAmount());

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (meta.hasDisplayName()) {
                str.append(";n" + prefix);
                str.append(meta.getDisplayName().replace(" ", "_"));
                str.append(suffix);
            }
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                str.append(";l" + prefix);
                StringBuilder l = new StringBuilder();
                for (String one : lore) {
                    if (!l.toString().isEmpty())
                        l.append("\\n");
                    l.append(one.replace(" ", "_"));
                }
                str.append(l);
                str.append(suffix);
            }

            if (cmim.isLeatherArmor()) {
                try {
                    LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
                    Color color = leatherMeta.getColor();
                    str.append(";");
                    str.append(color.getRed() + "," + color.getGreen() + "," + color.getBlue());
//                    str.append(suffix);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
            if (meta instanceof org.bukkit.inventory.meta.ArmorMeta) {
                org.bukkit.inventory.meta.ArmorMeta ameta = (ArmorMeta) meta;
                if (ameta.hasTrim()) {
                    org.bukkit.inventory.meta.trim.ArmorTrim trim = ameta.getTrim();
                    str.append(";");
                    str.append(trim.getMaterial().getKey().getKey().toLowerCase() + ":" + trim.getPattern().getKey().getKey().toLowerCase());
//                    str.append(suffix);
                }
            }
        }

        Map<Enchantment, Integer> enchants = item.getEnchantments();

        if (enchants != null && !enchants.isEmpty()) {
            str.append(";");
            StringBuilder enchantS = new StringBuilder();
            for (Entry<Enchantment, Integer> e : enchants.entrySet()) {
                if (!enchantS.toString().isEmpty())
                    enchantS.append(",");
                enchantS.append(CMIEnchantment.getName(e.getKey()) + ":" + e.getValue());
            }
            str.append(enchantS.toString());
//            str.append(suffix);
        }

        return str.toString();
    }

    private static byte getBitModifier(ItemFlag hideFlag) {
        return (byte) (1 << hideFlag.ordinal());
    }

    private static ItemFlag getitemFlag(String name) {
        name = name.replace("_", "");
        for (ItemFlag one : ItemFlag.values()) {
            if (one.name().replace("_", "").equalsIgnoreCase(name))
                return one;
        }
        return null;
    }

    public static String toOneLiner(CMIItemStack item) {

        String liner = item.getType().toString();
        if (item.getCMIType().equals(CMIMaterial.SPAWNER) || item.getCMIType().isMonsterEgg()) {
            EntityType t = item.getEntityType();
            if (t != null)
                liner += ":" + t.toString();
        } else if (item.getCMIType().isPotion() || item.getType().name().contains("TIPPED_ARROW")) {
            PotionMeta potion = (PotionMeta) item.getItemStack().getItemMeta();
            try {
                if (potion != null && potion.getBasePotionData() != null && potion.getBasePotionData().getType() != null && potion.getBasePotionData().getType().getEffectType() != null) {
                    liner += ":" + potion.getBasePotionData().getType().getEffectType().getName() + "-" + potion.getBasePotionData().isUpgraded() + "-" + potion.getBasePotionData().isExtended();
                }
            } catch (NoSuchMethodError e) {
            }
        } else {
            if (Version.isCurrentLower(Version.v1_13_R1))
                liner += ":" + item.getData();
        }
        if (item.getItemStack().getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemStack().getItemMeta();
            String s = "";
            for (Entry<Enchantment, Integer> one : meta.getStoredEnchants().entrySet()) {
                if (!s.isEmpty())
                    s += ";";
                s += one.getKey().getName() + "x" + one.getValue();
            }

            for (Entry<Enchantment, Integer> one : meta.getEnchants().entrySet()) {
                if (!s.isEmpty())
                    s += ";";
                s += one.getKey().getName() + "x" + one.getValue();
            }
            if (!s.isEmpty()) {
                liner += ":" + s;
            }
        }

        if (item.getCMIType().isPlayerHead()) {

            String base = null;
            // Trying to extract head data and its owner name
            try {
                CMINBT nbt = new CMINBT(item.getItemStack());
                List<String> ls = nbt.getList("SkullOwner.Properties.textures");
                if (ls != null) {
                    base = ls.get(0).split("Value:\"", 2)[1].split("\"", 2)[0];
                    if (base != null && !base.isEmpty())
                        liner += ":" + base;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return liner;
    }
}
