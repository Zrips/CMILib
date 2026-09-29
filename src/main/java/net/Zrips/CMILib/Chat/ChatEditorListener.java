package net.Zrips.CMILib.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class ChatEditorListener implements Listener {
    private CMILib plugin;

    public ChatEditorListener(CMILib plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(final AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        if (ChatEditorManager.map.isEmpty())
            return;

        Player player = event.getPlayer();

        if (!ChatEditorManager.map.containsKey(player.getUniqueId()))
            return;

        CMIScheduler.runTask(plugin, () -> ChatEditorManager.perform(player, event.getMessage()));
        event.setCancelled(true);
        event.getRecipients().clear();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {

        if (ChatEditorManager.map.isEmpty())
            return;

        Player player = event.getPlayer();

        if (!ChatEditorManager.map.containsKey(player.getUniqueId()))
            return;
        ChatEditorManager.remove(player.getUniqueId());
    }
}
