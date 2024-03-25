package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.World;

import net.Zrips.CMILib.Version.Version;

public class AsyncChunksPaper_9_12 implements AsyncChunks {
    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
        CompletableFuture<Chunk> future = new CompletableFuture<>();
        if (!gen && Version.isCurrentEqualOrHigher(Version.v1_12_R1) && !world.isChunkGenerated(x, z)) {
            future.complete(null);
        } else {
//	    future.complete(null);
            World.ChunkLoadCallback chunkLoadCallback = future::complete;
            world.getChunkAtAsync(x, z, chunkLoadCallback);
        }
        return future;
    }
}