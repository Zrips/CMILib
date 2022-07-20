package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

public class AsyncTeleportSync implements AsyncTeleport {
    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
	return CompletableFuture.completedFuture(Boolean.valueOf(entity.teleport(location, cause)));
    }
}
