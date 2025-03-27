package net.Zrips.CMILib.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import net.Zrips.CMILib.CMILib;

public class GUIListener1_9 implements Listener {
    CMILib plugin;

    public GUIListener1_9(CMILib plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void playerInteractEvent(PlayerSwapHandItemsEvent event) {

        if (event.isCancelled())
            return;
        Player player = event.getPlayer();

        if (!plugin.getGUIManager().isOpenedGui(player))
            return;

        event.setCancelled(true);
    }
}
