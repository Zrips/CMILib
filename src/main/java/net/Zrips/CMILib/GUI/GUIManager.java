package net.Zrips.CMILib.GUI;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Permissions.CMILPerm;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.commands.CMICommand;

public class GUIManager {

    private HashMap<UUID, CMIGui> map = new HashMap<UUID, CMIGui>();
    private CMILib plugin;

    private static int MAX_ENTRIES = 50;
    public static LinkedHashMap<UUID, Long> limit = new LinkedHashMap<UUID, Long>(MAX_ENTRIES + 1, .75F, false) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<UUID, Long> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    public final static String CMIGUIIcon = "CMIGUIIcon";
    public final static String LIProtection = "LIProtection";

    public static boolean usePackets = false;

    public GUIManager(CMILib plugin) {
        this.plugin = plugin;
    }

    public enum GUIButtonLocation {
        topLeft(0, 0), topRight(0, 1), bottomLeft(1, 0), bottomRight(1, 1);

        private Integer row;
        private Integer collumn;

        GUIButtonLocation(Integer row, Integer collumn) {
            this.collumn = collumn;
            this.row = row;
        }

        public Integer getRow() {
            return row;
        }

        public Integer getCollumn() {
            return collumn;
        }

    }

    public enum GUIRows {
        r1(1), r2(2), r3(3), r4(4), r5(5), r6(6);

        private int rows;

        GUIRows(int rows) {
            this.rows = rows;
        }

        public Integer getFields() {
//	    if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) { 
//		return rows * 9 < 27 ? 27 : rows * 9;
//	    }
            return rows * 9;
        }

        public Integer getRows() {
            return rows;
        }

        public static GUIRows getByRows(Integer rows) {
            if (rows > 9)
                rows = rows / 9;
            for (GUIRows one : GUIRows.values()) {
                if (one.getRows().equals(rows))
                    return one;
            }
            return GUIRows.r6;
        }
    }

    public enum GUIFieldType {
        Free, Locked, noPlacing
    }

    public enum InvType {
        Gui, Main, Quickbar
    }

    public enum CmiInventoryType {
        regular, SavedInv, EditableInv, RecipeCreator, ArmorStandEditor, ArmorStandCopy, EntityInventoryEditor, Recipes, SellHand
    }

    public enum GUIClickType {
        Unknown, Left, LeftShift, Right, RightShift, MiddleMouse, QControl, Q;

        public boolean isShiftClick() {
            switch (this) {
            case RightShift:
            case LeftShift:
                return true;
            default:
                break;
            }
            return false;
        }

        public boolean isLeftClick() {
            switch (this) {
            case Left:
            case LeftShift:
                return true;
            default:
                break;
            }
            return false;
        }

        public boolean isRightClick() {
            switch (this) {
            case Right:
            case RightShift:
                return true;
            default:
                break;
            }
            return false;
        }

        public boolean isMiddleClick() {
            switch (this) {
            case MiddleMouse:
                return true;
            default:
                break;
            }
            return false;
        }

        public boolean isQClick() {
            switch (this) {
            case QControl:
            case Q:
                return true;
            default:
                break;
            }
            return false;
        }
    }

    public void closeAll() {
        for (Entry<UUID, CMIGui> one : map.entrySet()) {
            Player player = Bukkit.getPlayer(one.getKey());
            if (player == null)
                continue;
            player.closeInventory();
        }
    }

    public GUIClickType getClickType(boolean left, boolean shift, InventoryAction action, ClickType clickType) {

        if (action != null && action.equals(InventoryAction.CLONE_STACK) || clickType != null && clickType.equals(ClickType.MIDDLE))
            return GUIClickType.MiddleMouse;

        if (action != null) {
            if (action.equals(InventoryAction.DROP_ONE_SLOT))
                return GUIClickType.Q;

            if (action.equals(InventoryAction.DROP_ALL_SLOT))
                return GUIClickType.QControl;
        }

        if (clickType != null) {
            if (clickType.equals(ClickType.LEFT))
                return GUIClickType.Left;

            if (clickType.equals(ClickType.SHIFT_LEFT))
                return GUIClickType.LeftShift;

            if (clickType.equals(ClickType.RIGHT))
                return GUIClickType.Right;

            if (clickType.equals(ClickType.SHIFT_RIGHT))
                return GUIClickType.RightShift;
        }

        if (left && !shift) {
            return GUIClickType.Left;
        }

        if (left && shift) {
            return GUIClickType.LeftShift;
        }

        if (!left && !shift) {
            return GUIClickType.Right;
        }

        return GUIClickType.RightShift;
    }

    public boolean processClick(final Player player, List<Integer> buttons, final GUIClickType clickType) {
        return processClick(player, null, buttons, clickType);
    }

    public boolean processClick(final Player player, ItemStack currentItem, List<Integer> buttons, final GUIClickType clickType) {

        Long time = limit.get(player.getUniqueId());
        if (time != null && time > System.currentTimeMillis())
            return false;

        CMIGui gui = map.get(player.getUniqueId());
        if (gui == null)
            return false;

        if (!gui.isAllowMoveAll())
            limit.put(player.getUniqueId(), System.currentTimeMillis() + 150L);

        int clicks = 0;

        try {
            if (!gui.getInv().equals(player.getOpenInventory().getTopInventory()) || gui.getInv().getHolder() != null) {
                player.closeInventory();
                map.remove(player.getUniqueId());
                return false;
            }
        } catch (Throwable e) {
        }

        for (Integer one : buttons) {

            final CMIGuiButton button = gui.getButtons().get(one);

            if (!gui.click(one, clickType, currentItem))
                return false;

            if (button == null)
                continue;
            clicks++;
            boolean canClick = true;
            for (String oneC : button.getPermissions()) {
                if (!CMILPerm.hasPermission(player, oneC, true, false))
                    canClick = false;
            }

            if (canClick) {
                CMIScheduler.get().runTaskLater(() -> {
                    for (GUIButtonCommand oneC : button.getCommands(clickType)) {
                        CMICommand.performCommand(player, oneC.getCommand(), oneC.getVis());
                    }
                }, 1L);
            }

            button.click();
            button.click(clickType);
            if (button.isCloseInv())
                player.closeInventory();

            if (!button.getCommands(clickType).isEmpty())
                break;
        }
        if (clicks == 0)
            gui.outsideClick(clickType);

        return true;
    }

    public boolean isLockedPart(Player player, List<Integer> buttons) {
        return isLockedPart(player, buttons, null);
    }

    public boolean isLockedPart(Player player, List<Integer> buttons, ItemStack item) {
        CMIGui gui = map.get(player.getUniqueId());
        if (gui == null)
            return false;

        int size = gui.getInv().getSize();
        int mainInvMax = size + 36 - 9;
        int quickbar = size + 36;

        boolean itemInHand = item != null && !item.getType().equals(Material.AIR);

        for (Integer one : buttons) {
            if (one > quickbar || quickbar < 0)
                continue;
            if (one < size && (gui.isLocked(InvType.Gui) && gui.isPermLocked(InvType.Gui))) {
                return true;
            } else if (one < size && gui.isNoItemPlacement(InvType.Gui) && itemInHand) {
                return true;
            } else if (one >= size && one < mainInvMax && (gui.isLocked(InvType.Main) && gui.isPermLocked(InvType.Main))) {
                return true;
            } else if (one >= mainInvMax && one < quickbar && ((gui.isLocked(InvType.Quickbar) && gui.isPermLocked(InvType.Quickbar)) || (gui.isLocked(InvType.Main) && gui.isPermLocked(InvType.Main)))) {
                return true;
            }
        }

        return false;
    }

    public boolean canClick(Player player, List<Integer> buttons) {
        return canClick(player, buttons, null);
    }

    public boolean canClick(Player player, List<Integer> buttons, ItemStack item) {
        try {

            CMIGui gui = map.get(player.getUniqueId());
            if (gui == null)
                return true;

            boolean itemInHand = item != null && !item.getType().equals(Material.AIR);

            for (Integer one : buttons) {
                CMIGuiButton button = gui.getButtons().get(one);
                if (button == null)
                    continue;
                if (button.getFieldType() == GUIFieldType.Locked)
                    return false;

                if (itemInHand && button.getFieldType() == GUIFieldType.noPlacing) {
                    return false;
                }
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public CMIGui getGui(Player player) {
        return map.get(player.getUniqueId());
    }

    public boolean isOpenedGui(Player player) {
        CMIGui gui = map.get(player.getUniqueId());
        if (gui == null)
            return false;
        if (player.getOpenInventory() == null)
            return false;
        return true;
    }

    public boolean removePlayer(Player player) {
        if (player == null)
            return false;
        CMIGui removed = map.remove(player.getUniqueId());
        if (removed == null)
            return false;
        if (player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().equals(removed.getInv()))
            player.closeInventory();

//	removed.processClose();
        removed.onClose();
        removed.playCloseSound();

        CMIGUICloseEvent event = new CMIGUICloseEvent(player, removed);
        Bukkit.getServer().getPluginManager().callEvent(event);

        return true;
    }

    public void generateInventory(CMIGui gui) {
        Inventory GuiInv = null;

        if (gui.getInvSize() != null) {
            GuiInv = Bukkit.createInventory(null, gui.getInvSize().getFields(), gui.getTitle());
        } else {
            GuiInv = Bukkit.createInventory(null, gui.getInvType(), gui.getTitle());
        }

        if (GuiInv == null)
            return;

        if (!usePackets) {
            for (Entry<Integer, CMIGuiButton> one : gui.getButtons().entrySet()) {
                if (one.getKey() > GuiInv.getSize())
                    continue;
                try {
                    ItemStack item = one.getValue().getItem(gui.getPlayer());
                    item = item == null ? null : item.clone();
                    if (item != null && one.getValue().isLocked()) {
                        item = (ItemStack) new CMINBT(item).setString(CMIGUIIcon, LIProtection);
                    }

                    if (one.getKey() < GuiInv.getSize())
                        GuiInv.setItem(one.getKey(), item);
                } catch (Throwable e) {
                    e.printStackTrace();
                    break;
                }
            }
        } else {
//	    for (Entry<Integer, CMIGuiButton> one : gui.getButtons().entrySet()) {
//		if (one.getKey() > GuiInv.getSize())
//		    continue;
//		try {
//		    ItemStack item = one.getValue().isLocked() ? CMIMaterial.STONE.newItemStack() : one.getValue().getItem(gui.getPlayer());
//		    item = item == null ? null : item.clone();
//		    if (item != null && one.getValue().isLocked()) {
//			item = plugin.getReflectionManager().setNbt(item, CMIGUIIcon, LIProtection);
//		    }
//		    GuiInv.setItem(one.getKey(), item);
//		} catch (ArrayIndexOutOfBoundsException e) {
//		    break;
//		}
//	    }
        }
        gui.setInv(GuiInv);
    }

    public void openGui(CMIGui gui) {

        Player player = gui.getPlayer();
        if (player.isSleeping())
            return;
        CMIGui oldGui = null;
        if (isOpenedGui(player)) {
            oldGui = plugin.getGUIManager().getGui(player);
            if (!gui.isSimilar(oldGui)) {
                oldGui = null;
            }
        }

        if (oldGui == null) {
            generateInventory(gui);
            player.closeInventory();

            if (gui.getInv().getHolder() != null) {
                return;
            }

            player.openInventory(gui.getInv());

            if (gui.getInv().getViewers().isEmpty()) {
                return;
            }

            gui.updateButtons();

            gui.playOpenSound();
            gui.onOpen();
            map.put(player.getUniqueId(), gui);
        } else {
            updateContent(gui);
        }

    }

    private static boolean validInventory(CMIGui gui) {

        Player player = gui.getPlayer();

        if (player.getOpenInventory() == null ||
            player.getOpenInventory().getTopInventory() == null ||
            !player.getOpenInventory().getTopInventory().getType().equals(gui.getInv().getType()) ||
            player.getOpenInventory().getTopInventory().getSize() != gui.getInv().getSize() ||
            Version.isCurrentEqualOrHigher(Version.v1_9_R1) && player.getOpenInventory().getTopInventory().getLocation() != null) {
            return false;
        }

        return true;
    }

    public void updateContent(CMIGui gui) {

        Player player = gui.getPlayer();

        if (!validInventory(gui)) {
            player.closeInventory();
            map.remove(player.getUniqueId());
            return;
        }

        player.getOpenInventory().getTopInventory().setContents(gui.getInv().getContents());
        player.updateInventory();
        gui.setInv(player.getOpenInventory().getTopInventory());

        if (gui.getInv().getHolder() != null) {

            player.closeInventory();
            return;
        }

        gui.updateButtons();

        CMILib.getInstance().getReflectionManager().updateInventoryTitle(player, gui.getTitle());

        map.put(player.getUniqueId(), gui);
    }

    public void softUpdateContent(CMIGui gui) {

        Player player = gui.getPlayer();

        if (!validInventory(gui)) {
            player.closeInventory();
            return;
        }

//	plugin.getNMS().updateInventoryTitle(player, gui.getTitle());

        for (int i = 0; i < player.getOpenInventory().getTopInventory().getSize(); i++) {
            CMIGuiButton button = gui.getButtons().get(i);
            if (button == null)
                continue;
            if (!button.isLocked())
                continue;

            player.getOpenInventory().getTopInventory().setItem(i, button.getItem(gui.getPlayer()));
        }
        gui.setInv(player.getOpenInventory().getTopInventory());
        map.put(player.getUniqueId(), gui);
        player.updateInventory();
    }
}
