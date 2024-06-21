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
        return runTask(this.plugin, runnable);
    }

    @Override
    public CompletableFuture<Void> runTask(JavaPlugin plugin, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.globalRegionScheduler.execute(plugin, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> runTaskAsynchronously(Runnable runnable) {
        return runTaskAsynchronously(this.plugin, runnable);
    }

    @Override
    public CompletableFuture<Void> runTaskAsynchronously(JavaPlugin plugin, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.asyncScheduler.runNow(plugin, task -> {
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
        return runTaskLater(this.plugin, runnable, delay);
    }

    @Override
    public CMITask runTaskLater(JavaPlugin plugin, Runnable runnable, long delay) {
        return new CMIFoliaTask(this.globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), clamp(delay)));
    }

    @Override
    public CMITask runLaterAsync(Runnable runnable, long delay) {
        return runLaterAsync(this.plugin, runnable, delay);
    }

    @Override
    public CMITask runLaterAsync(JavaPlugin plugin, Runnable runnable, long delay) {
        return new CMIFoliaTask(this.asyncScheduler.runDelayed(plugin, task -> runnable.run(), clamp(delay) * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public CMITask scheduleSyncRepeatingTask(Runnable runnable, long delay, long period) {
        return scheduleSyncRepeatingTask(this.plugin, runnable, delay, period);
    }

    @Override
    public CMITask scheduleSyncRepeatingTask(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(this.globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), clamp(delay), clamp(period)));
    }

    @Override
    public CMITask runTimerAsync(Runnable runnable, long delay, long period) {
        return runTimerAsync(this.plugin, runnable, delay, period);
    }

    @Override
    public CMITask runTimerAsync(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(this.asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), clamp(delay) * 50L, clamp(period) * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Location location, Runnable runnable) {
        return runAtLocation(this.plugin, location, runnable);
    }

    @Override
    public CompletableFuture<Void> runAtLocation(JavaPlugin plugin, Location location, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.regionScheduler.execute(plugin, location, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Chunk chunk, Runnable runnable) {
        return runAtLocation(this.plugin, chunk, runnable);
    }

    @Override
    public CompletableFuture<Void> runAtLocation(JavaPlugin plugin, Chunk chunk, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.regionScheduler.execute(plugin, chunk.getWorld(), chunk.getX(), chunk.getZ(), () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> runAtLocation(World world, int x, int z, Runnable runnable) {
        return runAtLocation(this.plugin, world, x, z, runnable);
    }

    @Override
    public CompletableFuture<Void> runAtLocation(JavaPlugin plugin, World world, int x, int z, Runnable runnable) {

        CompletableFuture<Void> future = new CompletableFuture<>();
        this.regionScheduler.execute(plugin, world, x, z, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CMITask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return runAtLocationLater(this.plugin, location, runnable, delay);
    }

    @Override
    public CMITask runAtLocationLater(JavaPlugin plugin, Location location, Runnable runnable, long delay) {
        return new CMIFoliaTask(regionScheduler.runDelayed(plugin, location, task -> runnable.run(), clamp(delay)));
    }

    @Override
    public CMITask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
        return runAtLocationTimer(this.plugin, location, runnable, delay, period);
    }

    @Override
    public CMITask runAtLocationTimer(JavaPlugin plugin, Location location, Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(regionScheduler.runAtFixedRate(plugin, location, task -> runnable.run(), clamp(delay), clamp(period)));
    }

    @Override
    public CompletableFuture<CMITaskResult> runAtEntity(Entity entity, Runnable runnable) {
        return runAtEntity(this.plugin, entity, runnable);
    }

    @Override
    public CompletableFuture<CMITaskResult> runAtEntity(JavaPlugin plugin, Entity entity, Runnable runnable) {
        CompletableFuture<CMITaskResult> future = new CompletableFuture<>();
        boolean success = entity.getScheduler().execute(plugin, () -> {
            runnable.run();
            future.complete(CMITaskResult.SUCCESS);
        }, null, 0L);
        if (!success)
            future.complete(CMITaskResult.SCHEDULER_RETIRED);
        return future;
    }

    @Override
    public CompletableFuture<CMITaskResult> runAtEntityWithFallback(Entity entity, Runnable runnable, Runnable fallback) {
        return runAtEntityWithFallback(this.plugin, entity, runnable, fallback);
    }

    public CompletableFuture<CMITaskResult> runAtEntityWithFallback(JavaPlugin plugin, Entity entity, Runnable runnable, Runnable fallback) {
        CompletableFuture<CMITaskResult> future = new CompletableFuture<>();
        boolean success = entity.getScheduler().execute(plugin, () -> {
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
        return runAtEntityLater(this.plugin, entity, runnable, delay);
    }

    @Override
    public CMITask runAtEntityLater(JavaPlugin plugin, Entity entity, Runnable runnable, long delay) {
        return new CMIFoliaTask(entity.getScheduler().runDelayed(plugin, task -> runnable.run(), null, clamp(delay)));
    }

    @Override
    public CMITask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
        return runAtEntityTimer(this.plugin, entity, runnable, delay, period);
    }

    @Override
    public CMITask runAtEntityTimer(JavaPlugin plugin, Entity entity, Runnable runnable, long delay, long period) {
        return new CMIFoliaTask(entity.getScheduler().runAtFixedRate(plugin, task -> runnable.run(), null, clamp(delay), clamp(period)));
    }
}
