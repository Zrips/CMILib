package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class AsyncTeleportSync implements AsyncTeleport {
    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        CMIScheduler.runTask(() -> future.complete(entity.teleport(location, cause)));

        return future;

//	return CompletableFuture.completedFuture(Boolean.valueOf(entity.teleport(location, cause)));
    }
}
