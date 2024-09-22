package net.Zrips.CMILib.Version.Schedulers;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Version.Version;

public class CMIScheduler {

    private static final CMIBaseImpl implementation;

    static {
        switch (Version.getPlatform()) {
        case folia:
            implementation = new CMIFoliaImpl(CMILib.getInstance());
            break;
        case craftbukkit:
        case paper:
        case pufferfish:
        case spigot:
        default:
            implementation = new CMIBukkitImpl(CMILib.getInstance());
            break;
        }
    }

    public static CMIBaseImpl get() {
        return implementation;
    }

    public static CompletableFuture<Void> runTask(Runnable runnable) {
        return get().runTask(runnable);
    }

    public static CompletableFuture<Void> runTaskAsynchronously(Runnable runnable) {
        return get().runTaskAsynchronously(runnable);
    }

    public static CMITask runTaskLater(Runnable runnable, long delay) {
        return get().runTaskLater(runnable, delay);
    }

    public static CMITask runLaterAsync(Runnable runnable, long delay) {
        return get().runLaterAsync(runnable, delay);
    }

    public static CMITask scheduleSyncRepeatingTask(Runnable runnable, long delay, long period) {
        return get().scheduleSyncRepeatingTask(runnable, delay, period);
    }

    public static CMITask runTimerAsync(Runnable runnable, long delay, long period) {
        return get().runTimerAsync(runnable, delay, period);
    }

    public static CompletableFuture<Void> runAtLocation(Location location, Runnable runnable) {
        return get().runAtLocation(location, runnable);
    }

    public static CompletableFuture<Void> runAtLocation(Chunk chunk, Runnable runnable) {
        return get().runAtLocation(chunk, runnable);
    }

    public static CMITask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return get().runAtLocationLater(location, runnable, delay);
    }

    public static CMITask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
        return get().runAtLocationTimer(location, runnable, delay, period);
    }

    public static CompletableFuture<CMITaskResult> runAtEntity(Entity entity, Runnable runnable) {
        return get().runAtEntity(entity, runnable);
    }

    public static CompletableFuture<CMITaskResult> runAtEntityWithFallback(Entity entity, Runnable runnable, Runnable fallback) {
        return get().runAtEntityWithFallback(entity, runnable, fallback);
    }

    public static CMITask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return get().runAtEntityLater(entity, runnable, delay);
    }

    public static CMITask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
        return get().runAtEntityTimer(entity, runnable, delay, period);
    }

    public static CompletableFuture<Void> runTask(JavaPlugin plugin, Runnable runnable) {
        return get().runTask(plugin, runnable);
    }

    public static CompletableFuture<Void> runTaskAsynchronously(JavaPlugin plugin, Runnable runnable) {
        return get().runTaskAsynchronously(plugin, runnable);
    }

    public static CMITask runTaskLater(JavaPlugin plugin, Runnable runnable, long delay) {
        return get().runTaskLater(plugin, runnable, delay);
    }

    public static CMITask runLaterAsync(JavaPlugin plugin, Runnable runnable, long delay) {
        return get().runLaterAsync(plugin, runnable, delay);
    }

    public static CMITask scheduleSyncRepeatingTask(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        return get().scheduleSyncRepeatingTask(plugin, runnable, delay, period);
    }

    public static CMITask runTimerAsync(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        return get().runTimerAsync(plugin, runnable, delay, period);
    }

    public static CompletableFuture<Void> runAtLocation(JavaPlugin plugin, Location location, Runnable runnable) {
        return get().runAtLocation(plugin, location, runnable);
    }

    public static CompletableFuture<Void> runAtLocation(JavaPlugin plugin, Chunk chunk, Runnable runnable) {
        return get().runAtLocation(plugin, chunk, runnable);
    }

    public static CMITask runAtLocationLater(JavaPlugin plugin, Location location, Runnable runnable, long delay) {
        return get().runAtLocationLater(plugin, location, runnable, delay);
    }

    public static CMITask runAtLocationTimer(JavaPlugin plugin, Location location, Runnable runnable, long delay, long period) {
        return get().runAtLocationTimer(plugin, location, runnable, delay, period);
    }

    public static CompletableFuture<CMITaskResult> runAtEntity(JavaPlugin plugin, Entity entity, Runnable runnable) {
        return get().runAtEntity(plugin, entity, runnable);
    }

    public static CompletableFuture<CMITaskResult> runAtEntityWithFallback(JavaPlugin plugin, Entity entity, Runnable runnable, Runnable fallback) {
        return get().runAtEntityWithFallback(plugin, entity, runnable, fallback);
    }

    public static CMITask runAtEntityLater(JavaPlugin plugin, Entity entity, Runnable runnable, long delay) {
        return get().runAtEntityLater(entity, runnable, delay);
    }

    public static CMITask runAtEntityTimer(JavaPlugin plugin, Entity entity, Runnable runnable, long delay, long period) {
        return get().runAtEntityTimer(entity, runnable, delay, period);
    }
}
