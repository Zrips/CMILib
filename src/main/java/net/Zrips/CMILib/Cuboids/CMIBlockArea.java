package net.Zrips.CMILib.Cuboids;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import net.Zrips.CMILib.Container.CMIChunkReference;
import net.Zrips.CMILib.Container.CMIVectorInt3D;
import net.Zrips.CMILib.Container.CMIWorld;

public class CMIBlockArea extends CMIArea {

    protected CMIVectorInt3D p1;
    protected CMIVectorInt3D p2;

    protected CMIVectorInt3D highPoint;
    protected CMIVectorInt3D lowPoint;

    public CMIBlockArea() {

    }

    public CMIBlockArea(Vector startLoc, Vector endLoc) {
        this(new CMIVectorInt3D(startLoc), new CMIVectorInt3D(endLoc));
    }

    public CMIBlockArea(Location startLoc, Location endLoc) {
        this(new CMIVectorInt3D(startLoc.toVector()), new CMIVectorInt3D(endLoc.toVector()));
    }

    public CMIBlockArea(CMIVectorInt3D startLoc, CMIVectorInt3D endLoc) {
        p1 = startLoc;
        p2 = endLoc;
        highPoint = startLoc.clone();
        lowPoint = endLoc.clone();
        recheck();
    }

    @Override
    public CMIBlockArea clone() {
        CMIBlockArea clone = new CMIBlockArea();
        clone.p1 = p1 == null ? null : p1.clone();
        clone.p2 = p2 == null ? null : p2.clone();
        clone.highPoint = highPoint == null ? null : highPoint.clone();
        clone.lowPoint = lowPoint == null ? null : lowPoint.clone();
        return clone;
    }

    private void recheck() {

        if (p1 == null || p2 == null)
            return;

        highPoint = new CMIVectorInt3D(
                Math.max(p1.getX(), p2.getX()),
                Math.max(p1.getY(), p2.getY()),
                Math.max(p1.getZ(), p2.getZ()));
        lowPoint = new CMIVectorInt3D(
                Math.min(p1.getX(), p2.getX()),
                Math.min(p1.getY(), p2.getY()),
                Math.min(p1.getZ(), p2.getZ()));
    }

    public boolean valid() {
        return highPoint != null && lowPoint != null;
    }

    public boolean isAreaWithinArea(CMIBlockArea area) {
        return (this.containsLoc(area.getHighPoint()) && this.containsLoc(area.getLowPoint()));
    }

    public boolean containsLoc(Location loc) {
        if (loc == null)
            return false;

        if (loc.getWorld() == null)
            return false;

        return containsLoc(new CMIVectorInt3D(loc.toVector()), 0);
    }

    public boolean containsLoc(CMIVectorInt3D vector) {
        return containsLoc(vector, 0);
    }

    public boolean containsLoc(Vector vector) {
        return containsLoc(new CMIVectorInt3D(vector), 0);
    }

    public boolean containsLoc(Location loc, int extraRange) {
        if (loc == null)
            return false;

        return containsLoc(new CMIVectorInt3D(loc.toVector()), extraRange);
    }

    public boolean containsLoc(CMIVectorInt3D vector, int extraRange) {
        if (vector == null)
            return false;

        if (lowPoint.getX() - extraRange > vector.getX())
            return false;

        if (highPoint.getX() + extraRange < vector.getX())
            return false;

        if (lowPoint.getZ() - extraRange > vector.getZ())
            return false;

        if (highPoint.getZ() + extraRange < vector.getZ())
            return false;

        if (lowPoint.getY() - extraRange > vector.getY())
            return false;

        if (highPoint.getY() + extraRange < vector.getY())
            return false;

        return true;
    }

    public boolean checkCollision(CMIBlockArea area) {

        if (!this.valid())
            return false;

        if (area.containsLoc(lowPoint) || area.containsLoc(highPoint) || this.containsLoc(area.highPoint) || this.containsLoc(area.lowPoint)) {
            return true;
        }
        return advCuboidCheckCollision(highPoint, lowPoint, area.highPoint, area.lowPoint);
    }

    private static boolean advCuboidCheckCollision(CMIVectorInt3D A1High, CMIVectorInt3D A1Low, CMIVectorInt3D A2High, CMIVectorInt3D A2Low) {
        int A1HX = A1High.getX();
        int A1LX = A1Low.getX();
        int A1HY = A1High.getY();
        int A1LY = A1Low.getY();
        int A1HZ = A1High.getZ();
        int A1LZ = A1Low.getZ();
        int A2HX = A2High.getX();
        int A2LX = A2Low.getX();
        int A2HY = A2High.getY();
        int A2LY = A2Low.getY();
        int A2HZ = A2High.getZ();
        int A2LZ = A2Low.getZ();
        if ((A1HX >= A2LX && A1HX <= A2HX) || (A1LX >= A2LX && A1LX <= A2HX) || (A2HX >= A1LX && A2HX <= A1HX) || (A2LX >= A1LX && A2LX <= A1HX)) {
            if ((A1HY >= A2LY && A1HY <= A2HY) || (A1LY >= A2LY && A1LY <= A2HY) || (A2HY >= A1LY && A2HY <= A1HY) || (A2LY >= A1LY && A2LY <= A1HY)) {
                if ((A1HZ >= A2LZ && A1HZ <= A2HZ) || (A1LZ >= A2LZ && A1LZ <= A2HZ) || (A2HZ >= A1LZ && A2HZ <= A1HZ) || (A2LZ >= A1LZ && A2LZ <= A1HZ)) {
                    return true;
                }
            }
        }
        return false;
    }

    public long getSize() {
        if (!this.valid())
            return 0;
        int xsize = (highPoint.getX() - lowPoint.getX()) + 1;
        int zsize = (highPoint.getZ() - lowPoint.getZ()) + 1;
        int ysize = (highPoint.getY() - lowPoint.getY()) + 1;
        return xsize * ysize * zsize;
    }

    public int getXSize() {
        if (!this.valid())
            return 0;
        return (highPoint.getX() - lowPoint.getX()) + 1;
    }

    public int getYSize() {
        if (!this.valid())
            return 0;
        return (highPoint.getY() - lowPoint.getY()) + 1;
    }

    public int getZSize() {
        if (!this.valid())
            return 0;
        return (highPoint.getZ() - lowPoint.getZ()) + 1;
    }

    public CMIVectorInt3D getHighPoint() {
        return highPoint;
    }

    public CMIVectorInt3D getLowPoint() {
        return lowPoint;
    }

    public List<CMIChunkReference> getChunks() {
        return getChunks(0);
    }

    public List<CMIChunkReference> getChunks(int range) {
        List<CMIChunkReference> chunks = new ArrayList<>();
        CMIVectorInt3D high = this.highPoint;
        CMIVectorInt3D low = this.lowPoint;
        int lowX = CMIChunkReference.getChunkCoord(low.getX() - range);
        int lowZ = CMIChunkReference.getChunkCoord(low.getZ() - range);
        int highX = CMIChunkReference.getChunkCoord(high.getX() + range);
        int highZ = CMIChunkReference.getChunkCoord(high.getZ() + range);

        for (int x = lowX; x <= highX; x++) {
            for (int z = lowZ; z <= highZ; z++) {
                chunks.add(new CMIChunkReference(x, z));
            }
        }
        return chunks;
    }

    public void setArea(CMIBlockArea area) {
        this.highPoint = area.getHighPoint().clone();
        this.lowPoint = area.getLowPoint().clone();
        recheck();
    }

    public void setHighLocation(Location highLocation) {
        if (highLocation == null)
            return;
        setHighPoint(new CMIVectorInt3D(highLocation.toVector()));
    }

    public void setHighPoint(Vector highLocation) {
        setHighPoint(new CMIVectorInt3D(highLocation));
    }

    public void setHighPoint(CMIVectorInt3D highPoint) {
        this.p1 = highPoint;
        recheck();
    }

    public void setLowLocation(Location lowLocation) {
        if (lowLocation == null)
            return;
        setLowPoint(new CMIVectorInt3D(lowLocation.toVector()));
    }

    public void setLowPoint(Vector lowPoint) {
        setLowPoint(new CMIVectorInt3D(lowPoint));
    }

    public void setLowPoint(CMIVectorInt3D lowPoint) {
        this.p2 = lowPoint;
        recheck();
    }

    public void setLocation(Location location) {
        if (location == null)
            return;
        setPoint(location.toVector());
    }

    public void setPoint(Vector vector) {
        if (vector == null)
            return;
        if (this.p1 == null)
            this.p1 = new CMIVectorInt3D(vector);
        else
            this.p2 = new CMIVectorInt3D(vector);
        recheck();
    }

    public Vector getMiddlePoint() {
        if (!this.valid())
            return null;
        return new Vector(
                (this.getLowPoint().getX() + this.getHighPoint().getX()) / 2,
                (this.getLowPoint().getY() + this.getHighPoint().getY()) / 2,
                (this.getLowPoint().getZ() + this.getHighPoint().getZ()) / 2);
    }

    @Override
    public @Nullable String toString() {
        if (!this.valid())
            return null;

        StringBuilder sb = new StringBuilder(32);
        sb.append(this.lowPoint.getX()).append(';')
                .append(this.lowPoint.getY()).append(';')
                .append(this.lowPoint.getZ()).append(';')
                .append(this.highPoint.getX()).append(';')
                .append(this.highPoint.getY()).append(';')
                .append(this.highPoint.getZ());
        return sb.toString();
    }

    public static @Nullable CMIBlockArea fromString(String value) {
        String[] parts = value.split(";");
        try {
            CMIVectorInt3D lowPoint = new CMIVectorInt3D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            CMIVectorInt3D highPoint = new CMIVectorInt3D(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));

            return new CMIBlockArea(lowPoint, highPoint);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean shift(Player player, int amount) {
        return shift(getDirection(player), player.getWorld(), amount);
    }

    public boolean shift(CMIActionDirection d, World world, int amount) {
        return shift(d, new CMIWorld(world), amount);
    }

    public boolean shift(CMIActionDirection d, CMIWorld world, int amount) {
        if (!valid()) {
            return false;
        }
        if (d == null) {
            return false;
        }
        switch (d) {
        case MINUS_Y:
            int oldy = getLowPoint().getY() - amount;
            if (oldy < CMIWorld.getMinHeight(world)) {
                oldy = CMIWorld.getMinHeight(world);
            }
            getLowPoint().setY(oldy);
            getHighPoint().setY(getHighPoint().getY() - amount);
            break;
        case PLUS_Y:
            oldy = getHighPoint().getY() + amount;
            if (oldy > CMIWorld.getMaxHeight(world)) {
                oldy = CMIWorld.getMaxHeight(world);
            }
            getHighPoint().setY(oldy);
            getLowPoint().setY(getLowPoint().getY() + amount);
            break;
        case MINUS_X:
            getLowPoint().setX(getLowPoint().getX() - amount);
            getHighPoint().setX(getHighPoint().getX() - amount);
            break;
        case MINUS_Z:
            getLowPoint().setZ(getLowPoint().getZ() - amount);
            getHighPoint().setZ(getHighPoint().getZ() - amount);
            break;
        case PLUS_X:
            getHighPoint().setX(getHighPoint().getX() + amount);
            getLowPoint().setX(getLowPoint().getX() + amount);
            break;
        case PLUS_Z:
            getHighPoint().setZ(getHighPoint().getZ() + amount);
            getLowPoint().setZ(getLowPoint().getZ() + amount);
            break;
        default:
            break;
        }
        return true;
    }

    public boolean expand(Player player, int amount) {
        return expand(getDirection(player), player.getWorld(), amount);
    }

    public boolean expand(CMIActionDirection d, World world, int amount) {
        return expand(d, new CMIWorld(world), amount);
    }

    public boolean expand(CMIActionDirection d, CMIWorld world, int amount) {
        if (!valid()) {
            return false;
        }

        if (d == null) {
            return false;
        }

        switch (d) {
        case MINUS_Y:
            int oldy = getLowPoint().getY() - amount;
            if (oldy < CMIWorld.getMinHeight(world)) {
                oldy = CMIWorld.getMinHeight(world);
            }
            getLowPoint().setY(oldy);
            break;
        case PLUS_Y:
            oldy = getHighPoint().getY() + amount;
            if (oldy > CMIWorld.getMaxHeight(world)) {
                oldy = CMIWorld.getMaxHeight(world);
            }
            getHighPoint().setY(oldy);
            break;
        case MINUS_X:
            getLowPoint().setX(getLowPoint().getX() - amount);
            break;
        case MINUS_Z:
            getLowPoint().setZ(getLowPoint().getZ() - amount);
            break;
        case PLUS_X:
            getHighPoint().setX(getHighPoint().getX() + amount);
            break;
        case PLUS_Z:
            getHighPoint().setZ(getHighPoint().getZ() + amount);
            break;
        default:
            break;
        }
        return true;
    }

    public boolean contract(Player player, int amount) {
        if (!this.valid()) {
            return false;
        }
        return contract(getDirection(player), player.getWorld(), amount);
    }

    public boolean contract(CMIActionDirection d, World world, int amount) {
        return contract(d, new CMIWorld(world), amount);
    }

    public boolean contract(CMIActionDirection d, CMIWorld world, int amount) {
        if (!this.valid()) {
            return false;
        }
        if (d == null) {
            return false;
        }

        switch (d) {
        case MINUS_Y:
            int oldy = getHighPoint().getY() - amount;
            if (oldy > CMIWorld.getMaxHeight(world)) {
                oldy = CMIWorld.getMaxHeight(world);
            }

            oldy = getHighPoint().getY() - amount;
            oldy = getLowPoint().getY() > oldy ? getLowPoint().getY() : oldy;

            getHighPoint().setY(oldy);
            break;
        case PLUS_Y:
            oldy = getLowPoint().getY() + amount;
            if (oldy < CMIWorld.getMinHeight(world)) {
                oldy = CMIWorld.getMinHeight(world);
            }
            oldy = getLowPoint().getY() + amount;
            oldy = getHighPoint().getY() < oldy ? getHighPoint().getY() : oldy;
            getLowPoint().setY(oldy);
            break;
        case MINUS_X:
            int res = getHighPoint().getX() - amount;
            res = getLowPoint().getX() > res ? getLowPoint().getX() : res;
            getHighPoint().setX(res);
            break;
        case MINUS_Z:
            res = getHighPoint().getZ() - amount;
            res = getLowPoint().getZ() > res ? getLowPoint().getZ() : res;
            getHighPoint().setZ(res);
            break;
        case PLUS_X:
            res = getLowPoint().getX() + amount;
            res = getHighPoint().getX() < res ? getHighPoint().getX() : res;
            getLowPoint().setX(res);
            break;
        case PLUS_Z:
            res = getLowPoint().getZ() + amount;
            res = getHighPoint().getZ() < res ? getHighPoint().getZ() : res;
            getLowPoint().setZ(res);
            break;
        default:
            break;
        }

        return true;
    }

    private static CMIActionDirection getDirection(Player player) {

        int yaw = (int) player.getLocation().getYaw();

        if (yaw < 0)
            yaw += 360;

        yaw += 45;
        yaw %= 360;

        int facing = yaw / 90;

        float pitch = player.getLocation().getPitch();
        if (pitch < -50)
            return CMIActionDirection.PLUS_Y;
        if (pitch > 50)
            return CMIActionDirection.MINUS_Y;
        if (facing == 1) // east
            return CMIActionDirection.MINUS_X;
        if (facing == 3) // west
            return CMIActionDirection.PLUS_X;
        if (facing == 2) // north
            return CMIActionDirection.MINUS_Z;
        if (facing == 0) // south
            return CMIActionDirection.PLUS_Z;
        return null;
    }
}
