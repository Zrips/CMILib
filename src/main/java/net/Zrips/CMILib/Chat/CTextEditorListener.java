package net.Zrips.CMILib.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Locale.Snd;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.commands.CommandsHandler;

public class CTextEditorListener implements Listener {
    private CMILib plugin;

    public CTextEditorListener(CMILib plugin) {
	this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void AsyncPlayerChatEvent(final AsyncPlayerChatEvent event) {
	final Player player = event.getPlayer();

	if (!ListEditor.isChatEditing(player))
	    return;
	final String cmd = ListEditor.getChatEditorCmd(player);
	final String message = event.getMessage().endsWith("?") ? event.getMessage() + ListEditor.questionMarkReplacer : event.getMessage();

	if (cmd != null) {
	    CMIScheduler.get().runTask(new Runnable() {
		@Override
		public void run() {
		    player.performCommand(cmd + message);
		}
	    });
	}
	ListEditor.removeChatEditor(player);
	event.setCancelled(true);
	event.getRecipients().clear();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShadowCommand(PlayerCommandPreprocessEvent event) {
	if (event.isCancelled())
	    return;

	Player player = event.getPlayer();

	String message = event.getMessage();

	if (!message.startsWith("/" + CommandsHandler.getLabel() + " shadowcmd "))
	    return;

	event.setCancelled(true);
	String id = message.substring(("/" + CommandsHandler.getLabel() + " shadowcmd ").length(), message.length()).split(" ")[0];

	ShadowCommandInfo cmd = ListEditor.getShadowCommand(player, id);

	if (cmd != null) {
	    String command = cmd.getCmd();
	    Snd snd = new Snd(player, player);
	    command = plugin.getLM().updateSnd(snd, command);

	    switch (cmd.getType()) {
	    case Console:
		command = plugin.getPlaceholderAPIManager().translateOwnPlaceHolder(player, command);
		ServerCommandEvent e = new ServerCommandEvent(plugin.getServer().getConsoleSender(), command);
		plugin.getServer().getPluginManager().callEvent(e);
		if (e.isCancelled())
		    return;
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), e.getCommand());
		break;
	    case Player:
		command = plugin.getPlaceholderAPIManager().translateOwnPlaceHolder(player, command);
		PlayerCommandPreprocessEvent ev = new PlayerCommandPreprocessEvent(player, "/" + command);
		plugin.getServer().getPluginManager().callEvent(ev);
		if (ev.isCancelled())
		    return;
		player.performCommand(cmd.getCmd());
		break;
	    default:
		break;
	    }
	}
    }

}
