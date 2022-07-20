package net.Zrips.CMILib.Version.PaperMethods;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Chunk;
import org.bukkit.World;

public interface AsyncChunks {
    CompletableFuture<Chunk> getChunkAtAsync(World paramWorld, int paramInt1, int paramInt2, boolean paramBoolean);
}
