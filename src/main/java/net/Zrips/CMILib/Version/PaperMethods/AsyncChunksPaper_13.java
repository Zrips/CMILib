package net.Zrips.CMILib.Version.PaperMethods;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.World;

import net.Zrips.CMILib.Logs.CMIDebug;

public class AsyncChunksPaper_13 implements AsyncChunks {

    private static Method meth = null;

    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
	try {
	    if (meth == null)
		meth = world.getClass().getMethod("getChunkAtAsync", int.class, int.class, boolean.class);
	    return (CompletableFuture<Chunk>) meth.invoke(world, x, z, gen);
	} catch (Throwable e) {
	    e.printStackTrace();
	}
	return null;
    }
}
