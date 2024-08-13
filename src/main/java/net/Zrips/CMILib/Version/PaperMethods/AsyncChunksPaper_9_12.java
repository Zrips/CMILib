package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class AsyncChunksPaper_9_12 implements AsyncChunks {
    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
        CompletableFuture<Chunk> future = new CompletableFuture<>();
        if (gen && Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
            world.getChunkAtAsync(x, z).thenAccept(future::complete).exceptionally(ex -> {
                future.completeExceptionally(ex);
                return null;
            });
        } else {
            CMIScheduler.runTask(() -> {
                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    try {
                        if (world.isChunkLoaded(x, z)) {
                            future.complete(world.getChunkAt(x, z));
                        } else {
                            future.complete(null);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else {
                    future.complete(world.getChunkAt(x, z));
                }
            });
        }
        return future;
    }
}