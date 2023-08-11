package net.Zrips.CMILib.Version.Teleporters;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jetbrains.annotations.NotNull;

public interface CMIBaseTeleportImpl {

    @NotNull
    boolean teleport(Entity ent, Location loc);

    @NotNull
    boolean teleport(Entity ent, Location loc, TeleportCause cause);

    @NotNull
    CompletableFuture<Boolean> teleportAsync(Entity ent, Location loc);

    @NotNull
    CompletableFuture<Boolean> teleportAsync(Entity ent, Location loc, TeleportCause cause);

}
