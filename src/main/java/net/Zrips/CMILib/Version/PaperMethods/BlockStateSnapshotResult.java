package net.Zrips.CMILib.Version.PaperMethods;

import org.bukkit.block.BlockState;

public class BlockStateSnapshotResult {
    private final boolean isSnapshot;

    private final BlockState state;

    public BlockStateSnapshotResult(boolean isSnapshot, BlockState state) {
	this.isSnapshot = isSnapshot;
	this.state = state;
    }

    public boolean isSnapshot() {
	return this.isSnapshot;
    }

    public BlockState getState() {
	return this.state;
    }
}
