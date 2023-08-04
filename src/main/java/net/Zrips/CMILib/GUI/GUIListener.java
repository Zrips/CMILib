package net.Zrips.CMILib.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.GUI.GUIManager.InvType;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class GUIListener implements Listener {
    CMILib plugin;

    public GUIListener(CMILib plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onNormalInventoryClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        if (plugin.getGUIManager().isOpenedGui(player) && plugin.getGUIManager().removePlayer(player)) {
            player.updateInventory();
            clearIconItems(player);
        }
    }

    private static void clearIconItems(Player player) {
        for (ItemStack one : player.getInventory().getContents()) {
            CMINBT nbt = new CMINBT(one);
            String res = nbt.getString(GUIManager.CMIGUIIcon);
            if (res == null || !res.equalsIgnoreCase(GUIManager.LIProtection))
                continue;
            player.getInventory().remove(one);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (event.getEntity() == null)
            return;
        clearIconItems(event.getEntity());
    }

    private HashMap<UUID, Long> LastClick = new HashMap<UUID, Long>();

    private boolean canClickByTimer(UUID uuid) {
        Long time = LastClick.get(uuid);
        if (time == null) {
            LastClick.put(uuid, System.currentTimeMillis());
            return true;
        }

        if (time + 51 > System.currentTimeMillis()) {
            LastClick.put(uuid, System.currentTimeMillis());
            return false;
        }

        LastClick.put(uuid, System.currentTimeMillis());
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickLog(final InventoryClickEvent event) {

        final Player player = (Player) event.getWhoClicked();

        if (!plugin.getGUIManager().isOpenedGui(player))
            return;

        CMIGui gui = plugin.getGUIManager().getGui(player);

        if (!gui.isClickLogging())
            return;

        if (gui.isAllowShift() && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && (event.getClickedInventory() == null || event.getClickedInventory().getType().equals(
            InventoryType.PLAYER)))
            return;

        try {
            gui.addClickLog(event.getClickedInventory() == null ? null : event.getClickedInventory().getType(), event.isCancelled(), event.getClick(), event.getAction(), event.getCurrentItem(), event
                .getCursor(), event.getSlot());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryOpenEvent(InventoryOpenEvent event) {

        if (!(event.getPlayer() instanceof Player))
            return;
        clearIconItems((Player) event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getGUIManager().isOpenedGui(player))
            event.setCancelled(true);

        ItemStack one = event.getItemDrop().getItemStack();

        CMINBT nbt = new CMINBT(one);
        String res = nbt.getString(GUIManager.CMIGUIIcon);

        if (res == null || !res.equalsIgnoreCase(GUIManager.LIProtection))
            return;
        event.setCancelled(true);
        clearIconItems(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(final InventoryClickEvent event) {

        if (event.isCancelled() && !event.getWhoClicked().getGameMode().toString().equalsIgnoreCase("Spectator"))
            return;

        final Player player = (Player) event.getWhoClicked();

        if (!plugin.getGUIManager().isOpenedGui(player))
            return;
        CMIGui gui = plugin.getGUIManager().getGui(player);

        if (player.isSleeping()) {
            player.closeInventory();
            event.setCancelled(true);
            return;
        }

        if (event.getAction().toString().equalsIgnoreCase("HOTBAR_SWAP") || event.getAction().toString().equalsIgnoreCase("HOTBAR_MOVE_AND_READD")) {
            ItemStack iioh = CMILib.getInstance().getReflectionManager().getItemInOffHand(player);
            event.setCancelled(true);
            CMIScheduler.get().runTaskLater(() -> {

                try {
                    player.getInventory().setItemInOffHand(iioh);
                    player.updateInventory();
                } catch (Throwable e) {
                }
            }, 1L);
        }

//	if (gui != null)
//	    return; 

        if (event.getClick() == ClickType.DOUBLE_CLICK || event.getHotbarButton() != -1)

        {
            event.setCancelled(true);
            return;
        }

        if (!gui.isAllowShift() && event.isShiftClick())
            event.setCancelled(true);

        if (event.isShiftClick() && gui.isNoItemPlacement(InvType.Gui) && !event.getClickedInventory().equals(gui.getInv())) {
            event.setCancelled(true);
        }

        if (!event.getAction().equals(InventoryAction.PICKUP_ALL) && !event.getAction().equals(InventoryAction.PICKUP_ONE) && !event.getAction().equals(InventoryAction.PICKUP_HALF) && !event.getAction()
            .equals(InventoryAction.PICKUP_SOME) && !event.getAction().equals(InventoryAction.PLACE_ALL) && !event.getAction().equals(InventoryAction.PLACE_ONE) && !event.getAction().equals(
                InventoryAction.PLACE_SOME) && !gui.isAllowShift() && !event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
            event.setCancelled(true);

        if (!gui.isAllowPickUpAll() && !canClickByTimer(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        final List<Integer> buttons = new ArrayList<Integer>();
        buttons.add(event.getRawSlot());
        if (!plugin.getGUIManager().canClick(player, buttons, event.getCursor())) {
            event.setCancelled(true);
            if (GUIManager.usePackets) {
                CMIScheduler.get().runTaskLater(() -> {
                    player.setItemOnCursor(player.getItemOnCursor());
                }, 1L);
            }

            gui.updateButtons();
        }

        if (plugin.getGUIManager().isLockedPart(player, buttons)) {
            event.setCancelled(true);
            if (GUIManager.usePackets) {
                CMIScheduler.get().runTaskLater(() -> {
                    player.setItemOnCursor(player.getItemOnCursor());
                }, 1L);
            }
        }

        if (plugin.getGUIManager().isLockedPart(player, buttons, event.getCursor())) {
            event.setCancelled(true);
            if (GUIManager.usePackets) {
                CMIScheduler.get().runTaskLater(() -> {
                    player.setItemOnCursor(player.getItemOnCursor());
                }, 1L);
            }
        }

        InventoryAction action = event.getAction();

        // removing click limit in case its move to another inventory event which can happen after double clicking on item while holding same one on cursor
        if (!gui.isAllowMoveAll() && action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
            GUIManager.limit.remove(player.getUniqueId());

        boolean click = plugin.getGUIManager().processClick(player, event.getCurrentItem(), buttons, plugin.getGUIManager().getClickType(event.isLeftClick(), event.isShiftClick(), action, event
            .getClick()));

        if (!click) {
            event.setCancelled(true);
            return;
        }

        // From TradeMe plugin
        if (!event.isCancelled() &&
            gui.isAllowShift() && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {

            event.setCancelled(true);
            ItemStack item = event.getCurrentItem().clone();
            for (int i = 0; i < gui.getInvSize().getFields(); i++) {
                CMIGuiButton b = gui.getButtons().get(i);
                if ((b == null || !b.isLocked() && b.getItem() == null) && gui.getInv().getItem(i) == null && i < gui.getInv().getSize()) {

                    if (gui.isClickLogging()) {
                        try {
                            gui.addClickLog(event.getClickedInventory() == null ? null : event.getClickedInventory().getType(), event.isCancelled(), event.getClick(), event.getAction(), event
                                .getCurrentItem(), event.getCursor(), event.getSlot());
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }

                    gui.getInv().setItem(i, item);
                    player.getInventory().setItem(event.getSlot(), null);
                    event.setCurrentItem(null);
                    break;
                }
            }
        }

        clearIconItems(player);

    }

//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onInventoryClicks(final InventoryClickEvent event) {
//	final Player player = (Player) event.getWhoClicked();
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onInventoryClicks(final InventoryDragEvent event) {
//	final Player player = (Player) event.getWhoClicked();
//    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryDragEventLog(final InventoryDragEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (!plugin.getGUIManager().isOpenedGui(player))
            return;

        if (player.isSleeping()) {
            player.closeInventory();
            event.setCancelled(true);
            return;
        }

        CMIGui gui = plugin.getGUIManager().getGui(player);
        if (!gui.isClickLogging())
            return;
        try {
            gui.addClickLog(event.getInventory().getType(), event.isCancelled(), event.getCursor(), event.getNewItems(), event.getInventorySlots(), event.getType());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryMove(final InventoryDragEvent event) {

        if (event.isCancelled() && !event.getWhoClicked().getGameMode().toString().equalsIgnoreCase("Spectator"))
            return;

        final Player player = (Player) event.getWhoClicked();

        if (!plugin.getGUIManager().isOpenedGui(player))
            return;

        CMIGui gui = plugin.getGUIManager().getGui(player);

        if (!gui.isAllowPickUpAll() && !canClickByTimer(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        final List<Integer> buttons = new ArrayList<Integer>();
        buttons.addAll(event.getRawSlots());
        if (!plugin.getGUIManager().canClick(player, buttons, event.getCursor()))
            event.setCancelled(true);

        if (plugin.getGUIManager().isLockedPart(player, buttons, event.getCursor()))
            event.setCancelled(true);

        plugin.getGUIManager().processClick(player, buttons, plugin.getGUIManager().getClickType(true, false, null, null));
        clearIconItems(player);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void PlayerPickupItemEvent(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();

        if (!plugin.getGUIManager().isOpenedGui(player))
            return;

        CMIGui gui = plugin.getGUIManager().getGui(player);

        if (!gui.isAllowItemPickup()) {
            event.setCancelled(true);
            event.getItem().setPickupDelay(event.getItem().getPickupDelay() < 20 ? 20 : event.getItem().getPickupDelay());
        }
    }
}
