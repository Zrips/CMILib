package net.Zrips.CMILib.GUI;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.PageInfo;
import net.Zrips.CMILib.GUI.GUIManager.CmiInventoryType;
import net.Zrips.CMILib.GUI.GUIManager.GUIButtonLocation;
import net.Zrips.CMILib.GUI.GUIManager.GUIClickType;
import net.Zrips.CMILib.GUI.GUIManager.GUIFieldType;
import net.Zrips.CMILib.GUI.GUIManager.GUIRows;
import net.Zrips.CMILib.GUI.GUIManager.InvType;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Sounds.CMISound;
import net.Zrips.CMILib.Version.Version;

public class CMIGui {

    private InventoryType invType;
    private GUIRows gUIRows;
    private Player player;
    private Inventory inv;
    private String title;
    private HashMap<Integer, CMIGuiButton> buttons = new HashMap<Integer, CMIGuiButton>();
    private LinkedHashSet<CMIGuiButton> noSlotButtons = new LinkedHashSet<CMIGuiButton>();

    private EnumMap<InvType, GUIFieldType> lock = new EnumMap<>(InvType.class);
    private EnumMap<InvType, String> permLock = new EnumMap<>(InvType.class);

    private Set<Integer> prevButtons = new HashSet<Integer>();
    private Set<Integer> nextButtons = new HashSet<Integer>();
    private Set<Integer> middleButtons = new HashSet<Integer>();

    private CmiInventoryType type = CmiInventoryType.regular;
    private Object whatShows;
    private Object tempData;

    private boolean allowShift = false;
    private boolean allowPickUpAll = false;
    private boolean allowItemPickup = true;
    private boolean allowMoveAll = false;

    private CMISound openSound = null;
    private CMISound closeSound = null;

    private CMIMaterial filler = null;

    private boolean logging = false;
    private LinkedHashSet<GUIClickActionLog> clickLog = new LinkedHashSet<GUIClickActionLog>();

    public CMIGui(Player player) {
        this.player = player;
    }

    @Override
    public CMIGui clone() {
        CMIGui g = new CMIGui(player);
        g.setInvSize(gUIRows);
        g.setButtons(buttons);
        g.setInv(inv);
        g.setInvType(invType);
        g.setTitle(title);
        g.setCmiInventoryType(type);
        g.setWhatShows(whatShows);
        return g;
    }

    public void playOpenSound() {
        if (openSound != null) {
            openSound.play(player);
        }
    }

    public void playCloseSound() {
        if (closeSound != null)
            closeSound.play(player);
    }

    public boolean isOpened() {
        return CMILib.getInstance().getGUIManager().isOpenedGui(getPlayer());
    }

    public boolean isSimilar(CMIGui gui) {

        if (this.getInvSize() != gui.getInvSize())
            return false;

        if (this.getInvType() != gui.getInvType())
            return false;

        return true;
    }

    public CMIGui open() {
        CMILib.getInstance().getGUIManager().openGui(this);
        return this;
    }

    public CMIGui update() {
        CMILib.getInstance().getGUIManager().softUpdateContent(this);
        return this;
    }

    public void outsideClick(GUIClickType type) {

    }

    public boolean click(int slot) {
        return click(slot, null, null);
    }

    public boolean click(int slot, GUIClickType type, ItemStack currentItem) {
        return true;
    }

    public InventoryType getInvType() {
        if (invType == null)
            invType = InventoryType.CHEST;
        return invType;
    }

    public void setInvType(InventoryType invType) {
        this.invType = invType;
    }

    public GUIRows getInvSize() {
        if (gUIRows == null)
            autoResize();
        return gUIRows;
    }

    public void setInvSize(GUIRows GUIRows) {
        this.gUIRows = GUIRows;
    }

    public void setInvSize(int rows) {
        this.gUIRows = GUIRows.getByRows(rows);
    }

    public void autoResize() {
        this.combineButtons();
        int max = 0;
        for (Entry<Integer, CMIGuiButton> one : this.buttons.entrySet()) {
            if (one.getKey() > max)
                max = one.getKey();
        }

        if (max < 9) {
            this.gUIRows = GUIRows.r1;
        } else if (max < 18) {
            this.gUIRows = GUIRows.r2;
        } else if (max < 27) {
            this.gUIRows = GUIRows.r3;
        } else if (max < 36) {
            this.gUIRows = GUIRows.r4;
        } else if (max < 45) {
            this.gUIRows = GUIRows.r5;
        } else {
            this.gUIRows = GUIRows.r6;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Inventory getInv() {
        if (inv == null)
            CMILib.getInstance().getGUIManager().generateInventory(this);
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public String getTitle() {
        if (title == null)
            title = "";
        return CMIChatColor.translate(title);
    }

    public void updateTitle(String title) {
        setTitle(title);
        CMILib.getInstance().getReflectionManager().updateInventoryTitle(player, this.title);
    }

    public void setTitle(String title) {
        if (Version.isCurrentEqualOrHigher(Version.v1_16_R1)) {
            if (CMIChatColor.stripColor(title).length() > 64) {
                title = title.substring(0, 63) + "~";
            }
        } else {
            if (title.length() > 32) {
                title = title.substring(0, 31) + "~";
            }
        }
        this.title = title;
    }

    public HashMap<Integer, CMIGuiButton> getButtons() {
        combineButtons();
        return buttons;
    }

    public CMIGuiButton getButton(int slot) {

        CMIGuiButton b = getButtons().get(slot);
        if (b != null)
            return b;

        // Missing code to check for slot limits and creation of new button

        return null;
    }

    public CMIGui replaceButton(CMIGuiButton button) {
        button.updateLooks();
        if (button.getSlot() != null)
            this.buttons.remove(button.getSlot());
        if (this.getInv() != null) {
            this.getInv().setItem(button.getSlot(), button.getItem(this.getPlayer()));
        }
        return addButton(button, 54);
    }

    public CMIGui addButton(CMIGuiButton button) {
        button.updateLooks();
        return addButton(button, 54);
    }

    public CMIGui addButton(CMIGuiButton button, int maxSlot) {
        button.setGui(this);
        if (button.getSlot() != null && buttons.get(button.getSlot()) != null) {
            for (int ii = button.getSlot(); ii < maxSlot; ii++) {
                CMIGuiButton b = buttons.get(ii);
                if (b == null) {
                    buttons.put(ii, button);
                    break;
                }
            }
            return this;
        }

        if (button.getSlot() == null) {
            noSlotButtons.add(button);
            return this;
        }
        buttons.put(button.getSlot(), button);
        return this;
    }

    public CMIGui removeButton(int slot) {
        buttons.remove(slot);
        return this;
    }

    private void combineButtons() {
        for (CMIGuiButton button : noSlotButtons) {
            for (int ii = 0; ii < 54; ii++) {
                CMIGuiButton b = buttons.get(ii);
                if (b == null) {
                    button.setSlot(ii);
                    buttons.put(ii, button);
                    break;
                }
            }
        }
        noSlotButtons.clear();
    }

    public void fillEmptyButtons() {
        fillEmptyButtons(null);
    }

    public void fillEmptyButtons(ItemStack item) {
        combineButtons();
        for (int i = 0; i < this.getInvSize().getFields(); i++) {
            if (this.buttons.containsKey(i))
                continue;
            addEmptyButton(item, i);
        }
    }

    public void updateButton(CMIGuiButton button) {
        if (inv == null || button.getSlot() == null || inv.getSize() <= button.getSlot())
            return;

        if (GUIManager.usePackets) {
            CMILib.getInstance().getReflectionManager().updateItemWithPacket(getPlayer(), button.getItem(this.getPlayer()), button.getSlot());
        } else {
            this.inv.setItem(button.getSlot(), button.getItem(this.getPlayer()));
        }

        buttons.put(button.getSlot(), button);
    }

    public void fakeUpdate(int slot, ItemStack item) {
        CMILib.getInstance().getReflectionManager().updateItemWithPacket(getPlayer(), item, slot);
    }

    public void addEmptyButton(int slot) {
        addEmptyButton(null, slot);
    }

    public void addEmptyButton(ItemStack item, int slot) {
        ItemStack MiscInfo = item == null ? filler == null ? CMILib.getInstance().getConfigManager().getGUIEmptyField().getItemStack().clone() : filler.newItemStack() : item.clone();

        ItemMeta MiscInfoMeta = MiscInfo.getItemMeta();
        if (MiscInfoMeta != null) {
            MiscInfoMeta.setDisplayName(" ");
            MiscInfo.setItemMeta(MiscInfoMeta);
        }
        CMIGuiButton button = new CMIGuiButton(slot, GUIFieldType.Locked, MiscInfo);
        addButton(button);
        updateButton(button);
    }

    public void setButtons(HashMap<Integer, CMIGuiButton> buttons) {
//	for (Entry<Integer, CMIGuiButton> one : buttons.entrySet()) {
//	    CMIGuiButton old = this.buttons.get(one.getKey());
//	    if (old == null)
//		old = one.getValue();
//	    buttons.put(one.getKey(), old);
//	}
        this.buttons = buttons;
    }

    public boolean isLocked(InvType type) {
        return lock.containsKey(type) ? (lock.get(type) == GUIFieldType.Locked) : false;
    }

    public boolean isNoItemPlacement(InvType type) {
        return lock.containsKey(type) ? (lock.get(type) == GUIFieldType.noPlacing) : false;
    }

    public void addLock(InvType type) {
        addLock(type, GUIFieldType.Locked);
    }

    public void setNoItemPlacement(InvType type) {
        addLock(type, GUIFieldType.noPlacing);
    }

    public void addLock(InvType type, GUIFieldType lock) {
        this.lock.put(type, lock);
    }

    public boolean isPermLocked(InvType type) {
        return permLock.containsKey(type) ? (!this.player.hasPermission(permLock.get(type))) : true;
    }

    public void addPermLock(InvType type, String perm) {
        this.permLock.put(type, perm);
    }

    public CmiInventoryType getType() {
        return type;
    }

    public void setCmiInventoryType(CmiInventoryType type) {
        this.type = type;
    }

    public Object getWhatShows() {
        return whatShows;
    }

    public void setWhatShows(Object whatShows) {
        this.whatShows = whatShows;
    }

    public Integer getSlot(GUIButtonLocation place) {
        GUIRows size = this.getInvSize();
        int v = place.getCollumn() * 9;
        v = place.getCollumn() > 0 ? v - 1 : v;
        Integer value = (((place.getRow() * (size.getRows())) * 9) - 8) + v;
        value = place.getRow() > 0 ? value : value + 9;
        return value - 1;
    }

    public void onClose() {

    }

    public void onOpen() {

    }

    public void pageChange(int page) {

    }

    public void addPagination(PageInfo pi) {

        int CurrentPage = pi.getCurrentPage();
        int pageCount = pi.getTotalPages();
        int totalEntries = pi.getTotalEntries();

        if (pageCount == 1)
            return;
        if (this.getInvSize().getRows() < 6)
            this.setInvSize(GUIRows.r6);

        int NextPage = CurrentPage + 1;
//	NextPage = CurrentPage < pageCount ? NextPage : CurrentPage;
        int Prevpage = CurrentPage - 1;
//	Prevpage = CurrentPage > 1 ? Prevpage : CurrentPage;

        if (Prevpage == 0)
            Prevpage = pageCount;

        if (NextPage > pageCount)
            NextPage = 1;

        int prevP = Prevpage;
        int nextP = NextPage;
        if (pageCount != 0) {

//	    for (int i = GUIRows.r5.getFields(); i < GUIRows.r6.getFields(); i++) {
//		this.getButtons().remove(i);
//	    }

            Integer mid = this.getSlot(GUIButtonLocation.bottomRight) - 4;
            Set<Integer> midButttons = getPageMiddleButtons();
            if (midButttons.isEmpty())
                midButttons.add(mid);

            for (Integer midSlot : midButttons) {
                CMIGuiButton button = new CMIGuiButton(midSlot, CMILib.getInstance().getConfigManager().getGUIMiddlePage()) {
                    @Override
                    public void click(GUIClickType type) {
                        if (CurrentPage != 1)
                            pageChange(1);
                    }
                };
                button.setName(LC.info_pageCountHover.getLocale("[totalEntries]", totalEntries));
                button.addLore(LC.info_pageCount.getLocale("[current]", CurrentPage, "[total]", pageCount));
                this.removeButton(midSlot);
                this.addButton(button);
            }

            Integer prev = this.getSlot(GUIButtonLocation.bottomLeft);
            Set<Integer> prevButttons = getPagePrevButtons();
            if (prevButttons.isEmpty())
                prevButttons.add(prev);

            for (Integer prevSlot : prevButttons) {
                if (this.getButtons().get(prevSlot) == null
//		    && CurrentPage > 1
                ) {

                    CMIGuiButton button = new CMIGuiButton(prevSlot, CMILib.getInstance().getConfigManager().getGUIPreviousPage()) {
                        @Override
                        public void click(GUIClickType type) {
                            pageChange(prevP);
                        }
                    };
                    button.setName(LC.info_prevPageGui.getLocale());
                    button.addLore(LC.info_pageCount.getLocale("[current]", CurrentPage, "[total]", pageCount) + (prevP == pageCount ? " " + LC.info_lastPageHover.getLocale() : ""));
                    this.removeButton(prevSlot);
                    this.addButton(button);
                }
            }

            Integer next = this.getSlot(GUIButtonLocation.bottomRight);
            Set<Integer> nextButttons = getPageNextButtons();
            if (nextButttons.isEmpty())
                nextButttons.add(next);

            for (Integer nextSlot : nextButttons) {
                if (this.getButtons().get(nextSlot) == null
//		    && pageCount > CurrentPage
                ) {
                    CMIGuiButton button = new CMIGuiButton(nextSlot, CMILib.getInstance().getConfigManager().getGUINextPage()) {
                        @Override
                        public void click(GUIClickType type) {
                            pageChange(nextP);
                        }
                    };
                    button.setName(LC.info_nextPageGui.getLocale());
                    button.addLore(LC.info_pageCount.getLocale("[current]", CurrentPage, "[total]", pageCount) + (nextP == 1 ? " " + LC.info_firstPageHover.getLocale() : ""));
                    this.removeButton(nextSlot);
                    this.addButton(button);
                }
            }
        }
    }

    public boolean isAllowShift() {
        return allowShift;
    }

    public void setAllowShift(boolean allowShift) {
        this.allowShift = allowShift;
    }

    public CMISound getOpenSound() {
        return openSound;
    }

    public void setOpenSound(CMISound openSound) {
        this.openSound = openSound;
    }

    public CMISound getCloseSound() {
        return closeSound;
    }

    public void setCloseSound(CMISound closeSound) {
        this.closeSound = closeSound;
    }

    public Object getTempData() {
        return tempData;
    }

    public void setTempData(Object tempData) {
        this.tempData = tempData;
    }

    public boolean isAllowPickUpAll() {
        return allowPickUpAll;
    }

    public void setAllowPickUpAll(boolean allowPickUpAll) {
        this.allowPickUpAll = allowPickUpAll;
    }

    public void updateButtons() {
        if (GUIManager.usePackets) {

            for (Entry<Integer, CMIGuiButton> one : getButtons().entrySet()) {
                if (one.getKey() > this.getInv().getSize())
                    continue;
                try {
                    ItemStack item = one.getValue().getItem(getPlayer());
                    item = item == null ? null : item.clone();
                    CMILib.getInstance().getReflectionManager().updateItemWithPacket(getPlayer(), item, one.getKey());

                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }

    public boolean isAllowItemPickup() {
        return allowItemPickup;
    }

    public void setAllowItemPickup(boolean allowItemPickup) {
        this.allowItemPickup = allowItemPickup;
    }

    public boolean isAllowMoveAll() {
        return allowMoveAll;
    }

    public void setAllowMoveAll(boolean allowMoveAll) {
        this.allowMoveAll = allowMoveAll;
    }

    public CMIMaterial getFiller() {
        return filler;
    }

    public void setFiller(CMIMaterial filler) {
        this.filler = filler;
    }

    public LinkedHashSet<GUIClickActionLog> getLog() {
        return clickLog;
    }

    @Deprecated
    public void addClickLog(boolean canceled, ClickType clickType, InventoryAction action, ItemStack currentItem, ItemStack holdingItem, int slot) {
        addClickLog(null, canceled, clickType, action, currentItem, holdingItem, slot);
    }

    public void addClickLog(InventoryType inv, boolean canceled, ClickType clickType, InventoryAction action, ItemStack currentItem, ItemStack holdingItem, int slot) {
        if (!isClickLogging())
            return;
        GUIClickActionLog log = new GUIClickActionLog(canceled);
        this.clickLog.add(log);
        log.setInv(inv);
        log.setBukkitClickType(clickType);
        log.setAction(action);
        log.setHoldingItem(holdingItem);
        log.addSlot(slot);
        log.setCurrentItem(currentItem);
    }

    public void addClickLog(boolean canceled, ItemStack holdingItem, Map<Integer, ItemStack> map, Set<Integer> slots, DragType dragType) {
        addClickLog(null, canceled, holdingItem, map, slots, dragType);
    }

    public void addClickLog(InventoryType inv, boolean canceled, ItemStack holdingItem, Map<Integer, ItemStack> map, Set<Integer> slots, DragType dragType) {
        if (!isClickLogging())
            return;
        GUIClickActionLog log = new GUIClickActionLog(canceled);
        this.clickLog.add(log);
        log.setHoldingItem(holdingItem);
        log.setDragtype(dragType);
        log.addSlots(slots);
        log.setNewItems(map);
        log.setInv(inv);
    }

    public boolean isClickLogging() {
        return logging;
    }

    public void logClicks(boolean log) {
        this.logging = log;
    }

    public Set<Integer> getPagePrevButtons() {
        return prevButtons;
    }

    public void setPagePrevButtons(Set<Integer> prevButtons) {
        this.prevButtons = prevButtons;
    }

    public void setPagePrevButtons(Integer... prevButtons) {
        this.prevButtons.clear();
        this.prevButtons.addAll(Arrays.asList(prevButtons));
    }

    public Set<Integer> getPageNextButtons() {
        return nextButtons;
    }

    public void setPageNextButtons(Set<Integer> nextButtons) {
        this.nextButtons = nextButtons;
    }

    public void setPageNextButtons(Integer... nextButtons) {
        this.nextButtons.clear();
        this.nextButtons.addAll(Arrays.asList(nextButtons));
    }

    public Set<Integer> getPageMiddleButtons() {
        return middleButtons;
    }

    public void setPageMiddleButtons(Set<Integer> middleButtons) {
        this.middleButtons = middleButtons;
    }

    public void setPageMiddleButtons(Integer... middleButtons) {
        this.middleButtons.clear();
        this.middleButtons.addAll(Arrays.asList(middleButtons));
    }

    public void fillInEdges() {
        for (int i = 1; i <= getInvSize().getRows(); i++) {
            if (i == 1 || i == getInvSize().getRows()) {
                for (int x = 0; x < 9; x++) {
                    CMIGuiButton b1 = getButtons().get(x + ((i - 1) * 9));
                    if (b1 == null)
                        addButton(new CMIGuiButton(x + ((i - 1) * 9), CMILib.getInstance().getConfigManager().getGUIEmptyField()));
                }
            } else {
                CMIGuiButton b1 = getButtons().get(0 + ((i - 1) * 9));
                if (b1 == null)
                    addButton(new CMIGuiButton(0 + ((i - 1) * 9), CMILib.getInstance().getConfigManager().getGUIEmptyField()));
                b1 = getButtons().get(8 + ((i - 1) * 9));
                if (b1 == null)
                    addButton(new CMIGuiButton(8 + ((i - 1) * 9), CMILib.getInstance().getConfigManager().getGUIEmptyField()));
            }
        }
    }
}
