package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.Zrips.CMILib.Version.Version;

public abstract class Environment {

    protected AsyncChunks asyncChunksHandler = new AsyncChunksSync();
    protected AsyncTeleport asyncTeleportHandler = new AsyncTeleportSync();
    protected ChunkIsGenerated isGeneratedHandler = new ChunkIsGeneratedUnknown();
    protected BlockStateSnapshot blockStateSnapshotHandler;

    public Environment() {
	if (Version.isCurrentEqualOrHigher(Version.v1_13_R1))
	    this.isGeneratedHandler = new ChunkIsGeneratedApiExists();
	if (Version.isCurrentLower(Version.v1_12_R1)) {
	    this.blockStateSnapshotHandler = new BlockStateSnapshotBeforeSnapshots();
	} else {
	    this.blockStateSnapshotHandler = new BlockStateSnapshotNoOption();
	}
    }

    public abstract String getName();

    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
	return this.asyncChunksHandler.getChunkAtAsync(world, x, z, gen);
    }

    public CompletableFuture<Boolean> teleport(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
	return this.asyncTeleportHandler.teleportAsync(entity, location, cause);
    }

    public boolean isChunkGenerated(World world, int x, int z) {
	return this.isGeneratedHandler.isChunkGenerated(world, x, z);
    }

    public BlockStateSnapshotResult getBlockState(Block block, boolean useSnapshot) {
	return this.blockStateSnapshotHandler.getBlockState(block, useSnapshot);
    }

    public boolean isSpigot() {
	return false;
    }

    public boolean isPaper() {
	return false;
    }
}
