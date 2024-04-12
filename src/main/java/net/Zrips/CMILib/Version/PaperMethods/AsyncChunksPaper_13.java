package net.Zrips.CMILib.Version.PaperMethods;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class AsyncChunksPaper_13 implements AsyncChunks {

    private static Method meth = null;

    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {

        if (world == null)
            return CompletableFuture.completedFuture(null);
//        CompletableFuture<Chunk> future = new CompletableFuture<>();
//        CMIScheduler.get().runAtLocation(world, x, z, () -> {
//            CompletableFuture<Chunk> chunkFuture = world.getChunkAtAsync(x, z, gen);
//            chunkFuture.thenAccept(future::complete); // Complete the future when chunk retrieval is done
//        });
//        return future;

        return world.getChunkAtAsync(x, z, gen);
    }
}
