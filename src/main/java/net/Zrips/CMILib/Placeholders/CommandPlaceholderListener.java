package net.Zrips.CMILib.Placeholders;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Keeps track of the last command each player performed so it can be
 * exposed through the {command} placeholder (returns the command without
 * the leading /).
 */
public class CommandPlaceholderListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Placeholder.setLastCommand(player.getUniqueId(), event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Placeholder.removeLastCommand(event.getPlayer().getUniqueId());
    }
}
