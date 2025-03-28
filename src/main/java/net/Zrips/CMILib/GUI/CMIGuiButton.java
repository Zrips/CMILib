package net.Zrips.CMILib.GUI;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIList;
import net.Zrips.CMILib.Container.CommandType;
import net.Zrips.CMILib.Enchants.CMIEnchantment;
import net.Zrips.CMILib.GUI.GUIManager.GUIClickType;
import net.Zrips.CMILib.GUI.GUIManager.GUIFieldType;
import net.Zrips.CMILib.Items.CMIItemStack;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Schedulers.CMITask;
import net.kyori.adventure.text.Component;

public class CMIGuiButton {

    private Integer slot = null;
    private GUIFieldType fieldType = GUIFieldType.Locked;
    private boolean closeInv = false;

    private EnumMap<GUIClickType, List<GUIButtonCommand>> commandMap = new EnumMap<>(GUIClickType.class);

    private List<String> permissions = new ArrayList<String>();
    private ItemStack item = null;

    @Override
    public CMIGuiButton clone() {
        CMIGuiButton b = new CMIGuiButton(slot, fieldType, item);
        b.setPermissions(new ArrayList<String>(permissions));
        b.setCommandMap(new HashMap<GUIClickType, List<GUIButtonCommand>>(commandMap));
        return b;
    }

    public CMIGuiButton(Integer slot, GUIFieldType fieldType, ItemStack item) {
        this.slot = slot;
        this.fieldType = fieldType;
        this.item = item == null ? null : item.clone();
    }

    public CMIGuiButton(Integer slot) {
        this.slot = slot;
    }

    public void hideItemFlags() {
        if (item == null)
            return;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_PLACED_ON);
            try {
                meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            } catch (Throwable e) {
            }
            try {
                meta.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS);
            } catch (Throwable e) {
            }
            item.setItemMeta(meta);
            return;
        }

        item = CMINBT.HideFlag(item, 63);
    }

    public CMIGuiButton hideToltip() {
        if (!Version.isCurrentEqualOrHigher(Version.v1_21_R2))
            return this;

        ItemMeta meta = item.getItemMeta();
        meta.setHideTooltip(true);
        item.setItemMeta(meta);
        return this;
    }

    public CMIGuiButton(ItemStack item) {
        this.item = item == null ? null : item.clone();
    }

    public CMIGuiButton(CMIMaterial mat) {
        this.item = mat == null ? null : mat.newItemStack();
    }

    public CMIGuiButton(Integer slot, CMIItemStack item) {
        this(slot, item == null ? null : item.getItemStack());
    }

    public CMIGuiButton(Integer slot, Material material) {
        this(slot, CMIMaterial.get(material), null);
    }

    public CMIGuiButton(Integer slot, CMIMaterial material) {
        this(slot, material, null);
    }

    @Deprecated
    public CMIGuiButton(Integer slot, Material material, int data) {
        this(slot, material, data, null);
    }

    public CMIGuiButton(Integer slot, Material material, String name) {
        this(slot, CMIMaterial.get(material), name);
    }

    public CMIGuiButton(Integer slot, CMIMaterial material, String name) {
        this.slot = slot;
        this.item = material == null ? null : material.newItemStack();
        if (item != null && name != null) {
            ItemMeta meta = this.item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(CMIChatColor.translate(name));
                this.item.setItemMeta(meta);
            }
        }
    }

    @Deprecated
    public CMIGuiButton(Integer slot, Material material, int data, String name) {
        this.slot = slot;
        if (Version.isCurrentEqualOrHigher(Version.v1_13_R1))
            this.item = new ItemStack(material, 1);
        else
            this.item = new ItemStack(material, 1, (short) data);
        if (name != null) {
            ItemMeta meta = this.item.getItemMeta();
            meta.setDisplayName(CMIChatColor.translate(name));
            this.item.setItemMeta(meta);
        }
    }

    public CMIGuiButton(Integer slot, ItemStack item) {
        this(slot, item, false);
    }

    public CMIGuiButton(Integer slot, ItemStack item, boolean hideFlags) {
        this.slot = slot;
        this.item = item == null ? null : item.clone();
        if (this.item != null && this.item.getDurability() == 32767) {
            CMIMaterial d = CMIMaterial.getRandom(CMIMaterial.get(this.item));
            if (d != null && d.getLegacyData() != -1)
                this.item.setDurability((short) d.getLegacyData());
        }
        if (hideFlags)
            this.hideItemFlags();
    }

    private CMITask sched = null;
    private int updateInterval = 20;
    private int ticks = 0;
    private CMIGui sgui;

    public void setGui(CMIGui sgui) {
        this.sgui = sgui;
    }

    public void startAutoUpdate(int intervalTicks) {
        updateInterval = intervalTicks;
        tasker();
    }

    @Deprecated
    public void startAutoUpdate(CMIGui sgui, int intervalTicks) {
        updateInterval = intervalTicks;
        this.sgui = sgui;
        tasker();
    }

    private void tasker() {
        if (sched != null) {
            sched.cancel();
            sched = null;
        }
        CMIGuiButton b = this;
        sched = CMIScheduler.get().scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {
                ticks++;
                if (sgui != null && CMILib.getInstance().getGUIManager().getGui(sgui.getPlayer()) != sgui) {
                    if (sched != null) {
                        sched.cancel();
                        sched = null;
                        return;
                    }
                }
                updateLooks();
                update(sgui);
                if (sgui != null)
                    sgui.updateButton(b);
            }
        }, 20L, updateInterval);
    }

    public void updateLooks() {

    }

    public void update() {
        if (this.sgui != null)
            sgui.updateButton(this);
    }

    public void update(CMIGui gui) {
        if (gui != null)
            gui.updateButton(this);
    }

    public Integer getSlot() {
        return slot;
    }

    public CMIGuiButton setSlot(Integer slot) {
        this.slot = slot;
        return this;
    }

    public GUIFieldType getFieldType() {
        return fieldType;
    }

    public CMIGuiButton setFieldType(GUIFieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public CMIGuiButton lockField() {
        this.fieldType = GUIFieldType.Locked;
        return this;
    }

    public CMIGuiButton unlockField() {
        this.fieldType = GUIFieldType.Free;
        return this;
    }

    public boolean isLocked() {
        return this.fieldType.equals(GUIFieldType.Locked);
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public CMIGuiButton addPermission(String perm) {
        this.permissions.add(perm);
        return this;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

//    public List<GUIButtonMethod> getMethods(GUIClickType type) {
//	List<GUIButtonMethod> list = methodMap.get(type);
//	if (list == null)
//	    list = new ArrayList<GUIButtonMethod>();
//	return list;
//    }

    public List<GUIButtonCommand> getCommands(GUIClickType type) {
        List<GUIButtonCommand> list = commandMap.get(type);
        if (list == null)
            list = new ArrayList<GUIButtonCommand>();
        return list;
    }

    public CMIGuiButton setName(String name) {
        if (this.item == null)
            return this;
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null)
            return this;

        if (name.contains("\n")) {
            String[] split = name.split("\\n");
            meta.setDisplayName(CMIChatColor.translate(split[0]));
            this.item.setItemMeta(meta);
            for (int i = 1; i < split.length; i++) {
                addLore(split[i]);
            }
        } else {
            meta.setDisplayName(CMIChatColor.translate(name));
            this.item.setItemMeta(meta);
        }

        return this;
    }

    public CMIGuiButton addLore(List<String> l) {
        l = CMIList.spreadList(l);
        for (String one : l) {
            addLore(one);
        }
        return this;
    }

    public CMIGuiButton addLore(String l) {
        if (this.item == null)
            return this;
        ItemMeta meta = this.item.getItemMeta();

        if (meta == null)
            return this;

        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<String>();

        if (l.contains("\\n")) {
            String[] split = l.split("\\\\n");
            for (String one : split) {
                lore.add(CMIChatColor.translate(one));
            }
        } else if (l.contains("\n")) {
            String[] split = l.split("\\n");
            for (String one : split) {
                lore.add(CMIChatColor.translate(one));
            }
        } else
            lore.add(CMIChatColor.translate(l));

        meta.setLore(lore);
        this.item.setItemMeta(meta);

        return this;
    }

    public CMIGuiButton clearLore() {
        if (this.item == null)
            return this;
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setLore(new ArrayList<String>());
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public CMIGuiButton addItemName(String name) {
        if (this.item == null)
            return this;
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(CMIChatColor.translate(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public CMIGuiButton addCommand(String command) {
        return addCommand(null, command);
    }

    public CMIGuiButton addCommand(String command, CommandType vis) {
        return addCommand(null, command, vis);
    }

    public CMIGuiButton addCommand(GUIClickType type, String command) {
        return addCommand(type, command, CommandType.gui);
    }

    public CMIGuiButton addCommand(GUIClickType type, String command, CommandType vis) {
        if (type == null) {
            for (GUIClickType one : GUIClickType.values()) {
                List<GUIButtonCommand> list = commandMap.get(one);
                if (list == null)
                    list = new ArrayList<GUIButtonCommand>();
                list.add(new GUIButtonCommand(command, vis));
                commandMap.put(one, list);
            }
        } else {
            List<GUIButtonCommand> list = commandMap.get(type);
            if (list == null)
                list = new ArrayList<GUIButtonCommand>();
            list.add(new GUIButtonCommand(command, vis));
            commandMap.put(type, list);
        }
        return this;
    }

    public void click() {

    }

    public void click(GUIClickType type) {

    }

    public CMIGuiButton addCommand(Location loc) {
        if (loc == null)
            return this;
        addCommand("cmi tppos " + loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getBlockZ() + " " + loc.getPitch() + " " + loc.getYaw());
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getItem(Player player) {

        if (item == null)
            return null;

        ItemStack i = item.clone();

        if (this.isLocked() && !CMIMaterial.isAir(i.getType()))
            i = (ItemStack) new CMINBT(i).setString(GUIManager.CMIGUIIcon, GUIManager.LIProtection);

        ItemMeta meta = i.hasItemMeta() ? i.getItemMeta() : null;

        if (meta == null) {
            try {
                meta = i.getItemMeta();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (meta != null && meta.hasDisplayName()) {
            meta.setDisplayName(CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(player, meta.getDisplayName()));
        }

        if (meta != null && meta.hasLore()) {
            meta.setLore(CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(player, meta.getLore()));
        }

        if (meta != null) {
            i.setItemMeta(meta);
        }

        return i;
    }

    public CMIGuiButton setItem(CMIMaterial material) {
        return setItem(material == null ? null : material.newItemStack().clone());
    }

    public CMIGuiButton setItem(ItemStack item) {
        this.item = item == null ? null : item.clone();
        return this;
    }

    public void setCommandMap(HashMap<GUIClickType, List<GUIButtonCommand>> commandMap) {
        this.commandMap.clear();
        for (Entry<GUIClickType, List<GUIButtonCommand>> one : commandMap.entrySet()) {
            this.commandMap.put(one.getKey(), one.getValue());
        }
    }

    public boolean isCloseInv() {
        return closeInv;
    }

    public void setCloseInv(boolean closeInv) {
        this.closeInv = closeInv;
    }

    public CMIGui getGui() {
        return sgui;
    }

    public int getTicks() {
        return ticks;
    }

    public void setGlowing() {
        this.item.addUnsafeEnchantment(CMIEnchantment.get("LUCK"), 1);
        this.hideItemFlags();
    }

    public void removeGlowing() {
        this.item.removeEnchantment(CMIEnchantment.get("LUCK"));
        this.hideItemFlags();
    }
}
