package net.Zrips.CMILib.Version.Schedulers;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.Zrips.CMILib.CMILib;

public class CMIBukkitImpl implements CMIBaseImpl {
    private final JavaPlugin plugin;

    private final BukkitScheduler scheduler;

    public CMIBukkitImpl(CMILib plugin) {
        this.plugin = plugin;
        this.scheduler = this.plugin.getServer().getScheduler();
    }

    @Override
    public CompletableFuture<Void> runTask(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.scheduler.runTask(this.plugin, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> runTaskAsynchronously(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.scheduler.runTaskAsynchronously(this.plugin, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CMITask runTaskLater(Runnable runnable, long delay) {
        return new CMIBukkitTask(this.scheduler.runTaskLater(this.plugin, runnable, delay));
    }

    @Override
    public CMITask runLaterAsync(Runnable runnable, long delay) {
        return new CMIBukkitTask(this.scheduler.runTaskLaterAsynchronously(this.plugin, runnable, delay));
    }

    @Override
    public CMITask scheduleSyncRepeatingTask(Runnable runnable, long delay, long period) {
        return new CMIBukkitTask(this.scheduler.runTaskTimer(this.plugin, runnable, delay, period));
    }

    @Override
    public CMITask runTimerAsync(Runnable runnable, long delay, long period) {
        return new CMIBukkitTask(this.scheduler.runTaskTimerAsynchronously(this.plugin, runnable, delay, period));
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Location location, Runnable runnable) {
        return runAtLocation(null, 0, 0, runnable);
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Chunk chunk, Runnable runnable) {
        return runAtLocation(null, 0, 0, runnable);
    }

    @Override
    public CompletableFuture<Void> runAtLocation(World world, int x, int z, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        this.scheduler.runTask(this.plugin, () -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

    @Override
    public CMITask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return new CMIBukkitTask(this.scheduler.runTaskLater(this.plugin, runnable, delay));
    }

    @Override
    public CMITask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
        return new CMIBukkitTask(this.scheduler.runTaskTimer(this.plugin, runnable, delay, period));
    }

    @Override
    public CompletableFuture<CMITaskResult> runAtEntity(Entity entity, Runnable runnable) {
        CompletableFuture<CMITaskResult> future = new CompletableFuture<>();
        this.scheduler.runTask(this.plugin, () -> {
            runnable.run();
            future.complete(CMITaskResult.SUCCESS);
        });
        return future;
    }

    @Override
    public CompletableFuture<CMITaskResult> runAtEntityWithFallback(Entity entity, Runnable runnable, Runnable fallback) {
        CompletableFuture<CMITaskResult> future = new CompletableFuture<>();
        this.scheduler.runTask(this.plugin, () -> {
            if (entity.isValid()) {
                runnable.run();
                future.complete(CMITaskResult.SUCCESS);
            } else {
                fallback.run();
                future.complete(CMITaskResult.ENTITY_RETIRED);
            }
        });
        return future;
    }

    @Override
    public CMITask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return new CMIBukkitTask(this.scheduler.runTaskLater(this.plugin, runnable, delay));
    }

    @Override
    public CMITask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
        return new CMIBukkitTask(this.scheduler.runTaskTimer(this.plugin, runnable, delay, period));
    }

}
