package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.Zrips.CMILib.Version.Version;

public class PaperLib {

    private static Environment ENVIRONMENT = initialize();

    private static Environment initialize() {
	switch (Version.getPlatform()) {
	case paper:
	    return new PaperEnvironment();
	case spigot:
	    return new SpigotEnvironment();
	default:
	case craftbukkit:
	    return new CraftBukkitEnvironment();
	}
    }

    @Nonnull
    public static CompletableFuture<Boolean> teleportAsync(@Nonnull Entity entity, @Nonnull Location location) {
	return ENVIRONMENT.teleport(entity, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Nonnull
    public static CompletableFuture<Boolean> teleportAsync(@Nonnull Entity entity, @Nonnull Location location, PlayerTeleportEvent.TeleportCause cause) {
	return ENVIRONMENT.teleport(entity, location, cause);
    }

    @Nonnull
    public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull Location loc) {
	return getChunkAtAsync(loc.getWorld(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4, true);
    }

    @Nonnull
    public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull Location loc, boolean gen) {
	return getChunkAtAsync(loc.getWorld(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4, gen);
    }

    @Nonnull
    public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull World world, int x, int z) {
	return getChunkAtAsync(world, x, z, true);
    }

    @Nonnull
    public static CompletableFuture<Chunk> getChunkAtAsync(@Nonnull World world, int x, int z, boolean gen) {
	return ENVIRONMENT.getChunkAtAsync(world, x, z, gen);
    }

    public static boolean isChunkGenerated(@Nonnull Location loc) {
	return isChunkGenerated(loc.getWorld(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }

    public static boolean isChunkGenerated(@Nonnull World world, int x, int z) {
	return ENVIRONMENT.isChunkGenerated(world, x, z);
    }

    @Nonnull
    public static BlockStateSnapshotResult getBlockState(@Nonnull Block block, boolean useSnapshot) {
	return ENVIRONMENT.getBlockState(block, useSnapshot);
    }
}
