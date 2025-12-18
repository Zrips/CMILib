package net.Zrips.CMILib.Container;

import org.bukkit.Location;

public class CMIChunkReference {

    public static int getChunkCoord(final int val) {
        return val >> 4;
    }

    private final int z;
    private final int x;

    public CMIChunkReference(Location loc) {
        this.x = getChunkCoord(loc.getBlockX());
        this.z = getChunkCoord(loc.getBlockZ());
    }

    public CMIChunkReference(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        CMIChunkReference other = (CMIChunkReference) obj;
        return this.x == other.x && this.z == other.z;
    }

    @Override
    public int hashCode() {
        return x ^ z;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(x).append(";").append(z);
        return sb.toString();
    }

}
