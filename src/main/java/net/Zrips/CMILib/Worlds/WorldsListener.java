package net.Zrips.CMILib.Worlds;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import net.Zrips.CMILib.Container.CMIWorld;

public class WorldsListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoadEvent(WorldLoadEvent event) {
        CMIWorld.cacheWorld(event.getWorld());
    }
}
