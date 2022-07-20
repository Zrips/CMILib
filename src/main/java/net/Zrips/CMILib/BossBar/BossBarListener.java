package net.Zrips.CMILib.BossBar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.Zrips.CMILib.CMILib;

public class BossBarListener implements Listener {
    private CMILib plugin;

    public BossBarListener(CMILib plugin) {
	this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinBossBar(PlayerJoinEvent event) {
	Player player = event.getPlayer();
	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	    @Override
	    public void run() {
		plugin.getBossBarManager().updateGlobalBars(player);
		return;
	    }
	}, 5L);
    }
}
