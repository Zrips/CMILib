package net.Zrips.CMILib.Version.PaperMethods;

import org.bukkit.block.Block;

public interface BlockStateSnapshot {
    BlockStateSnapshotResult getBlockState(Block paramBlock, boolean paramBoolean);
}
