package net.Zrips.CMILib.Version.PaperMethods;

import org.bukkit.ChunkSnapshot;
import org.bukkit.World;

import net.Zrips.CMILib.Container.CMIWorld;

public class CMIChunkSnapShot {
    private ChunkSnapshot snap;
    private int maxY = 128;
    private int minY = 0;
    private World world;

    public CMIChunkSnapShot(World world) {
        this.world = world;
        maxY = CMIWorld.getMaxHeight(world);
        minY = CMIWorld.getMinHeight(world);
    }

    public CMIChunkSnapShot setSnapshot(ChunkSnapshot snap) {
        this.snap = snap;
        return this;
    }

    public ChunkSnapshot getSnapshot() {
        return snap;
    }

    public int getMaxY() {
        return maxY;
    }

    public CMIChunkSnapShot setMaxY(int maxY) {
        this.maxY = maxY;
        return this;

    }

    public int getMinY() {
        return minY;
    }

    public CMIChunkSnapShot setMinY(int minY) {
        this.minY = minY;
        return this;
    }

    public World getWorld() {
        return world;
    }
}
