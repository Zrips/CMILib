package net.Zrips.CMILib.Version.PaperMethods;

import org.bukkit.block.Block;

public class BlockStateSnapshotNoOption implements BlockStateSnapshot {
    @Override
    public BlockStateSnapshotResult getBlockState(Block block, boolean useSnapshot) {
	return new BlockStateSnapshotResult(true, block.getState());
    }
}