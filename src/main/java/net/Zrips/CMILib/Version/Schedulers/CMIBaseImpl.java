package net.Zrips.CMILib.Version.Schedulers;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public interface CMIBaseImpl {
    CompletableFuture<Void> runTask(Runnable paramRunnable);

    CompletableFuture<Void> runTaskAsynchronously(Runnable paramRunnable);

    CMITask runTaskLater(Runnable paramRunnable, long ticksDelay);

    CMITask runLaterAsync(Runnable paramRunnable, long ticksDelay);

    CMITask scheduleSyncRepeatingTask(Runnable paramRunnable, long ticksStart, long ticksDelay);

    CMITask runTimerAsync(Runnable paramRunnable, long ticksStart, long ticksDelay);

    CompletableFuture<Void> runAtLocation(Location paramLocation, Runnable paramRunnable);

    CMITask runAtLocationLater(Location paramLocation, Runnable paramRunnable, long ticksDelay);

    CMITask runAtLocationTimer(Location paramLocation, Runnable paramRunnable, long ticksStart, long ticksDelay);

    CompletableFuture<CMITaskResult> runAtEntity(Entity paramEntity, Runnable paramRunnable);

    CompletableFuture<CMITaskResult> runAtEntityWithFallback(Entity paramEntity, Runnable paramRunnable1, Runnable paramRunnable2);

    CMITask runAtEntityLater(Entity paramEntity, Runnable paramRunnable, long ticksDelay);

    CMITask runAtEntityTimer(Entity paramEntity, Runnable paramRunnable, long ticksStart, long ticksDelay);

    CompletableFuture<Void> runAtLocation(Chunk chunk, Runnable runnable);

    CompletableFuture<Void> runAtLocation(World world, int x, int z, Runnable runnable);
}
