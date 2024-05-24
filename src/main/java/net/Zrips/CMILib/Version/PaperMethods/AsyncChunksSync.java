package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.World;

public class AsyncChunksSync implements AsyncChunks {
    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
        if (!gen && !PaperLib.isChunkGenerated(world, x, z))
            return CompletableFuture.completedFuture(null);
        return CompletableFuture.completedFuture(world.getChunkAt(x, z));
    }
}
