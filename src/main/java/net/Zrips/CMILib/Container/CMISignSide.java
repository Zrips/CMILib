package net.Zrips.CMILib.Container;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public enum CMISignSide {

    FRONT,
    BACK;

    public static CMISignSide getSignSide(Block sign, Location fromLoc) {
        float playerAngle = (float) Math.toDegrees(Math.atan2(fromLoc.getX() - sign.getX() - 0.5, fromLoc.getZ() - sign.getZ() - 0.5));
        playerAngle = playerAngle < 0 ? playerAngle + 360 : playerAngle;
        BlockFace facing = (new CMIBlock(sign)).getFacing();
        double blockAngle = Math.toDegrees(Math.atan2(facing.getModX(), facing.getModZ()));
        double phi = Math.abs(blockAngle - playerAngle) % 360;
        return (phi > 180 ? 360 - phi : phi) < 90 ? CMISignSide.FRONT : CMISignSide.BACK;
    }
}
