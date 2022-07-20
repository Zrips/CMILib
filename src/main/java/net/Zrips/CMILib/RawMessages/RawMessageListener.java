package net.Zrips.CMILib.RawMessages;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Messages.CMIMessages;

public class RawMessageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
	RawMessageManager.clearCache(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShadowCommand(PlayerCommandPreprocessEvent event) {

	if (event.isCancelled())
	    return;

	String message = event.getMessage();

	if (!message.startsWith(RawMessageManager.rmccmd)) {
	    if (message.startsWith(RawMessageManager.rmccmd.substring(0, RawMessageManager.rmccmd.length() - 1))) {
		CMIMessages.sendMessage(event.getPlayer(), "&7This is internal command used for specific actions like clicking on a chat message");
	    }
	    return;
	}

	event.setCancelled(true);

	String ids = message.substring(RawMessageManager.rmccmd.length(), message.length());
	if (ids.contains(" "))
	    ids = ids.split(" ", 2)[0];
	Long id = null;
	try {
	    id = Long.parseLong(ids);
	} catch (Throwable e) {
	    return;
	}
	Player player = event.getPlayer();

	if (CMILibConfig.rmcConsoleLog) {
	    RawMessageCommand rmc = RawMessageManager.get(id);
	    if (rmc != null && rmc.getOriginalCommand() != null) {
		CMIMessages.consoleMessage("                       --^-- /" + rmc.getOriginalCommand());
	    }
	}
	
	RawMessageManager.perform(player, id);
    }
}
