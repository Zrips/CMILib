package net.Zrips.CMILib.Version.PaperMethods;

import net.Zrips.CMILib.Version.Version;

public class PaperEnvironment extends SpigotEnvironment {
    public PaperEnvironment() {
	if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
	    this.asyncChunksHandler = new AsyncChunksPaper_13();
	    this.asyncTeleportHandler = new AsyncTeleportPaper();
	} else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1) && Version.isCurrentLower(Version.v1_13_R1)) {
	    this.asyncChunksHandler = new AsyncChunksPaper_9_12();
	    this.asyncTeleportHandler = new AsyncTeleportPaper();
	}
	if (Version.isCurrentEqualOrHigher(Version.v1_12_R1)) {
	    this.isGeneratedHandler = new ChunkIsGeneratedApiExists();
	    this.blockStateSnapshotHandler = new BlockStateSnapshotOptionalSnapshots();
	}
    }

    @Override
    public String getName() {
	return "Paper";
    }

    @Override
    public boolean isPaper() {
	return true;
    }
}