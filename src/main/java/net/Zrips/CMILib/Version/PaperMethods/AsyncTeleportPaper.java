package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class AsyncTeleportPaper implements AsyncTeleport {
    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (Version.isCurrentLower(Version.v1_19_R1))
            return PaperLib.getChunkAtAsync(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4, true)
                .thenCompose(chunk -> {
                    // Shift to main thread since it needs to be synchronous
                    CompletableFuture<Boolean> result = new CompletableFuture<>();
                    CMIScheduler.runTask(() -> result.complete(entity.teleport(location, cause)));
                    return result;
                });

        return entity.teleportAsync(location, cause);
    }
}