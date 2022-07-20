package net.Zrips.CMILib.Version.PaperMethods;

import java.lang.reflect.Method;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class BlockStateSnapshotOptionalSnapshots implements BlockStateSnapshot {

    private static Method met = null;

    @Override
    public BlockStateSnapshotResult getBlockState(Block block, boolean useSnapshot) {

	try {
	    if (met == null)
		met = block.getClass().getMethod("getState", boolean.class);
	    return new BlockStateSnapshotResult(useSnapshot, (BlockState) met.invoke(block, useSnapshot));
	} catch (Throwable e) {
	    e.printStackTrace();
	}

	return new BlockStateSnapshotResult(useSnapshot, null);
    }
}