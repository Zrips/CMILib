package net.Zrips.CMILib.Version.Teleporters;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Version.Version;

public class CMITeleporter {

    private static final CMIBaseTeleportImpl implementation;

    static {
        if (Version.isFolia()) {
            implementation = new CMIFoliaImpl(CMILib.getInstance());
        } else {
            implementation = new CMIBukkitTeleportImpl(CMILib.getInstance());
        }
    }

    public static CMIBaseTeleportImpl get() {
        return implementation;
    }

    public static boolean teleport(Entity ent, Location loc) {
        return implementation.teleport(ent, loc);
    }

    public static @NotNull CompletableFuture<Boolean> teleportAsync(Entity ent, Location loc) {
        return implementation.teleportAsync(ent, loc);
    }

    public static boolean teleport(Entity ent, Location loc, TeleportCause cause) {
        return implementation.teleport(ent, loc, cause);
    }

    public static @NotNull CompletableFuture<Boolean> teleportAsync(Entity ent, Location loc, TeleportCause cause) {
        return implementation.teleportAsync(ent, loc, cause);
    }
}
