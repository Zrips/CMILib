package net.Zrips.CMILib.Version.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMINumber;

public class CMIFoliaImpl implements CMIBaseImpl {
    private final JavaPlugin plugin;

    private final RegionScheduler regionScheduler;
    private final GlobalRegionScheduler globalRegionScheduler;
    private final AsyncScheduler asyncScheduler;

    public CMIFoliaImpl(CMILib plugin) {
        this.plugin = plugin;
        this.globalRegionScheduler = this.plugin.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = this.plugin.getServer().getAsyncScheduler();
        this.regionScheduler = this.plugin.getServer().getRegionScheduler();
    }

    @Override
    public CompletableFuture<Void> runTask(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.globalRegionScheduler.execute(this.plugin, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> runTaskAsynchronously(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.asyncScheduler.runNow(this.plugin, task -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    private static long clamp(long delay) {
        return CMINumber.clamp(delay, 1L, Math.abs(delay));
    }

    @Override
    public CMITask runTaskLater(Runnable runnable, long delay) {
        return new CMIFoliaTask(this.globalRegionScheduler.runDelayed(this.plugin, task -> runnable.run(), clamp(delay)));
    }

    @Override
    public CMITask runLaterAsync(Runnable runnable, long delay) {
        return new CMIFoliaTask(this.asyncScheduler.runDelayed(this.plugin, task -> runnable.run(), clamp(delay) * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public CMITask scheduleSyncRepeatingTask(Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(this.globalRegionScheduler.runAtFixedRate(this.plugin, task -> runnable.run(), clamp(delay), clamp(period)));
    }

    @Override
    public CMITask runTimerAsync(Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(this.asyncScheduler.runAtFixedRate(this.plugin, task -> runnable.run(), clamp(delay) * 50L, clamp(period) * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Location location, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.regionScheduler.execute(this.plugin, location, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Chunk chunk, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.regionScheduler.execute(this.plugin, chunk.getWorld(), chunk.getX(), chunk.getZ(), () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> runAtLocation(World world, int x, int z, Runnable runnable) {
                
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.regionScheduler.execute(this.plugin, world, x, z, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CMITask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return new CMIFoliaTask(regionScheduler.runDelayed(this.plugin, location, task -> runnable.run(), clamp(delay)));
    }

    @Override
    public CMITask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(regionScheduler.runAtFixedRate(this.plugin, location, task -> runnable.run(), clamp(delay), clamp(period)));
    }

    @Override
    public CompletableFuture<CMITaskResult> runAtEntity(Entity entity, Runnable runnable) {
        CompletableFuture<CMITaskResult> future = new CompletableFuture<>();
        boolean success = entity.getScheduler().execute(this.plugin, () -> {
            runnable.run();
            future.complete(CMITaskResult.SUCCESS);
        }, null, 0L);
        if (!success)
            future.complete(CMITaskResult.SCHEDULER_RETIRED);
        return future;
    }

    @Override
    public CompletableFuture<CMITaskResult> runAtEntityWithFallback(Entity entity, Runnable runnable, Runnable fallback) {
        CompletableFuture<CMITaskResult> future = new CompletableFuture<>();
        boolean success = entity.getScheduler().execute(this.plugin, () -> {
            runnable.run();
            future.complete(CMITaskResult.SUCCESS);
        }, () -> {
            fallback.run();
            future.complete(CMITaskResult.ENTITY_RETIRED);
        }, 0L);
        if (!success)
            future.complete(CMITaskResult.SCHEDULER_RETIRED);
        return future;
    }

    @Override
    public CMITask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return new CMIFoliaTask(entity.getScheduler().runDelayed(this.plugin, task -> runnable.run(), null, clamp(delay)));
    }

    @Override
    public CMITask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(entity.getScheduler().runAtFixedRate(this.plugin, task -> runnable.run(), null, clamp(delay), clamp(period)));
    }
}
