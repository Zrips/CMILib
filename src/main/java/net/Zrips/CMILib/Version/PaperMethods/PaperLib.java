package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class PaperLib {

    private static Environment ENVIRONMENT = initialize();

    private static Environment initialize() {

        if (Version.isPaperBranch()) {
            return new PaperEnvironment();
        }

        switch (Version.getPlatform()) {
        case spigot:
            return new SpigotEnvironment();
        default:
        case craftbukkit:
            return new CraftBukkitEnvironment();
        }
    }

    public static CompletableFuture<Material> getBlockType(Location loc, boolean generate) {
        return getSnapshot(loc, generate, false).thenApplyAsync(cmiChunkSnapShot -> cmiChunkSnapShot.getSnapshot().getBlockType(loc.getBlockX() & 0xF, loc.getBlockY(), loc.getBlockZ() & 0xF));
    }

    public static CompletableFuture<CMIChunkSnapShot> getSnapshot(Location loc, boolean generate, boolean biomeData) {
        return getSnapshot(loc.getWorld(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4, generate, biomeData);
    }

    public static CompletableFuture<CMIChunkSnapShot> getSnapshot(World world, int chunkX, int chunkZ, boolean generate, boolean biomeData) {

        if (world == null)
            return CompletableFuture.completedFuture(null);

        CompletableFuture<Chunk> future = null;
        try {
            if (Version.isSpigot())
                return CompletableFuture.supplyAsync(() -> {
                    CMIChunkSnapShot cmiChunkSnapshot = new CMIChunkSnapShot(world);
                    try {
                        CMIScheduler.runAtLocation(new Location(world, chunkX * 16, 0, chunkZ * 16), () -> cmiChunkSnapshot.setSnapshot(world.getChunkAt(chunkX, chunkZ).getChunkSnapshot(true,
                            biomeData, false))).get();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }

                    return cmiChunkSnapshot;
                });

            future = PaperLib.getChunkAtAsync(world, chunkX, chunkZ, generate);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (future == null)
            return CompletableFuture.completedFuture(null);

        return future.thenComposeAsync(chunk -> CompletableFuture.supplyAsync(() -> {
            CMIChunkSnapShot cmiChunkSnapshot = new CMIChunkSnapShot(world);

            if (chunk == null)
                return cmiChunkSnapshot;

            try {
                CompletableFuture<Void> f = CMIScheduler.runAtLocation(new Location(world, chunkX * 16, 0, chunkZ * 16), () -> cmiChunkSnapshot.setSnapshot(chunk.getChunkSnapshot(true, biomeData, false)));
                f.get();
            } catch (Throwable e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            return cmiChunkSnapshot;
        }));
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
