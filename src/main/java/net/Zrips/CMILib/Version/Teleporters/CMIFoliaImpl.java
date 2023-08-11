package net.Zrips.CMILib.Version.Teleporters;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.CMILib;

public class CMIFoliaImpl implements CMIBaseTeleportImpl {
    private final JavaPlugin plugin;

    public CMIFoliaImpl(CMILib plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull boolean teleport(Entity ent, Location loc) {
        return ent.teleport(loc);
    }

    @Override
    public @NotNull boolean teleport(Entity ent, Location loc, TeleportCause cause) {

        return ent.teleport(loc, cause);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> teleportAsync(Entity ent, Location loc) {
        return ent.teleportAsync(loc);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> teleportAsync(Entity ent, Location loc, TeleportCause cause) {
        return ent.teleportAsync(loc, cause);
    }
}
