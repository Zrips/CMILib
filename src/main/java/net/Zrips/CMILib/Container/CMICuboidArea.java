package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.Zrips.CMILib.Items.CMIMaterial;

public class CMICuboidArea {
    private Vector p1;
    private Vector p2;
    private Vector highPoints;
    private Vector lowPoints;
    private World world;

    private static final int MIN_HEIGHT = 0;

    public CMICuboidArea(World world, Vector startLoc, Vector endLoc) {
        highPoints = startLoc.clone();
        p1 = startLoc.clone();
        lowPoints = endLoc.clone();
        p2 = endLoc.clone();
        this.world = world;
        recheck();
    }

    public CMICuboidArea(Location startLoc, Location endLoc) {
        highPoints = startLoc.toVector().clone();
        p1 = startLoc.toVector().clone();
        lowPoints = endLoc.toVector().clone();
        p2 = endLoc.toVector().clone();
        world = startLoc.getWorld();
        recheck();
    }

    @Override
    public CMICuboidArea clone() {
        CMICuboidArea clone = new CMICuboidArea(world);
        clone.highPoints = highPoints == null ? null : highPoints.clone();
        clone.lowPoints = lowPoints == null ? null : lowPoints.clone();
        clone.p1 = p1 == null ? null : p1.clone();
        clone.p2 = p2 == null ? null : p2.clone();
        return clone;
    }

    private void recheck() {

        if (p1 == null || p2 == null)
            return;

        highPoints = new Vector(
                Math.max(p1.getBlockX(), p2.getBlockX()),
                Math.max(p1.getBlockY(), p2.getBlockY()),
                Math.max(p1.getBlockZ(), p2.getBlockZ()));
        lowPoints = new Vector(
                Math.min(p1.getBlockX(), p2.getBlockX()),
                Math.min(p1.getBlockY(), p2.getBlockY()),
                Math.min(p1.getBlockZ(), p2.getBlockZ()));
    }

    public boolean valid() {
        return p1 != null && p2 != null;
    }

//    public CuboidArea() {
//    }

    public CMICuboidArea(World world) {
        this.world = world;
    }

    public boolean isAreaWithinArea(CMICuboidArea area) {
        return (this.containsLoc(area.highPoints) && this.containsLoc(area.lowPoints));
    }

    public Location getOutsideFreeLoc() {

        if (!this.valid())
            return null;

        List<RandomLoc> randomLocList = new ArrayList<RandomLoc>();

        for (int z = -1; z < getZSize() + 1; z++) {
            randomLocList.add(new RandomLoc(getLowPoint().getX(), 0, getLowPoint().getZ() + z));
            randomLocList.add(new RandomLoc(getLowPoint().getX() + getXSize(), 0, getLowPoint().getZ() + z));
        }

        for (int x = -1; x < getXSize() + 1; x++) {
            randomLocList.add(new RandomLoc(getLowPoint().getX() + x, 0, getLowPoint().getZ()));
            randomLocList.add(new RandomLoc(getLowPoint().getX() + x, 0, getLowPoint().getZ() + getZSize()));
        }

        Location loc = this.getMiddlePoint().toLocation(this.getWorld()).clone();

        boolean found = false;
        int it = 0;
        int maxIt = 30;
        while (!found && it < maxIt) {
            it++;

            Random ran = new Random(System.currentTimeMillis());
            if (randomLocList.isEmpty())
                break;
            int check = ran.nextInt(randomLocList.size());
            RandomLoc place = randomLocList.get(check);
            randomLocList.remove(check);
            double x = place.getX();
            double z = place.getZ();

            loc.setX(x);
            loc.setZ(z);
            loc.setY(getHighPoint().getBlockY());

            int max = getHighPoint().getBlockY();
            max = this.getWorld().getEnvironment() == Environment.NETHER ? 100 : max;

            for (int i = max; i > getLowPoint().getY(); i--) {
                loc.setY(i);
                Block block = loc.getBlock();
                Block block2 = loc.clone().add(0, 1, 0).getBlock();
                Block block3 = loc.clone().add(0, -1, 0).getBlock();
                if (block3.getType() != Material.AIR && block.getType() == Material.AIR && block2.getType() == Material.AIR) {
                    break;
                }
            }

            if (loc.getBlock().getType() != Material.AIR)
                continue;

            if (loc.clone().add(0, -1, 0).getBlock().getState().getType() == Material.LAVA || loc.clone().add(0, -1, 0).getBlock().getState()
                    .getType().equals(CMIMaterial.LAVA.getMaterial()))
                continue;

            if (loc.clone().add(0, -1, 0).getBlock().getState().getType() == Material.WATER || loc.clone().add(0, -1, 0).getBlock().getState()
                    .getType().equals(CMIMaterial.WATER.getMaterial()))
                continue;

            found = true;
            loc.setY(loc.getY() + 2);
            loc.add(0.5, 0, 0.5);
            break;
        }
        return loc;
    }

    public boolean containsLoc(Location loc) {
        if (loc == null)
            return false;

        if (loc.getWorld() == null)
            return false;

        if (!loc.getWorld().equals(world))
            return false;

        return containsLoc(loc.toVector(), 0);
    }

    public boolean containsLoc(Vector vector) {
        return containsLoc(vector, 0);
    }

    public boolean containsLoc(Location loc, int extraRange) {
        if (loc == null)
            return false;

        if (!loc.getWorld().equals(world))
            return false;
        return containsLoc(loc.toVector(), extraRange);
    }

    public boolean containsLoc(Vector vector, int extraRange) {
        if (vector == null)
            return false;

        if (lowPoints.getBlockX() - extraRange > vector.getBlockX())
            return false;

        if (highPoints.getBlockX() + extraRange < vector.getBlockX())
            return false;

        if (lowPoints.getBlockZ() - extraRange > vector.getBlockZ())
            return false;

        if (highPoints.getBlockZ() + extraRange < vector.getBlockZ())
            return false;

        if (lowPoints.getBlockY() - extraRange > vector.getBlockY())
            return false;

        if (highPoints.getBlockY() + extraRange < vector.getBlockY())
            return false;

        return true;
    }

    public boolean checkCollision(CMICuboidArea area) {
        if (area.getWorld() == null || this.getWorld() == null)
            return false;
        if (!area.getWorld().equals(this.getWorld())) {
            return false;
        }

        if (!this.valid())
            return false;

        if (area.containsLoc(lowPoints) || area.containsLoc(highPoints) || this.containsLoc(area.highPoints) || this.containsLoc(area.lowPoints)) {
            return true;
        }
        return advCuboidCheckCollision(highPoints, lowPoints, area.highPoints, area.lowPoints);
    }

    private static boolean advCuboidCheckCollision(Vector A1High, Vector A1Low, Vector A2High, Vector A2Low) {
        int A1HX = A1High.getBlockX();
        int A1LX = A1Low.getBlockX();
        int A1HY = A1High.getBlockY();
        int A1LY = A1Low.getBlockY();
        int A1HZ = A1High.getBlockZ();
        int A1LZ = A1Low.getBlockZ();
        int A2HX = A2High.getBlockX();
        int A2LX = A2Low.getBlockX();
        int A2HY = A2High.getBlockY();
        int A2LY = A2Low.getBlockY();
        int A2HZ = A2High.getBlockZ();
        int A2LZ = A2Low.getBlockZ();
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
        int xsize = (highPoints.getBlockX() - lowPoints.getBlockX()) + 1;
        int zsize = (highPoints.getBlockZ() - lowPoints.getBlockZ()) + 1;
        int ysize = (highPoints.getBlockY() - lowPoints.getBlockY()) + 1;
        return xsize * ysize * zsize;
    }

    public int getXSize() {
        if (!this.valid())
            return 0;
        return (highPoints.getBlockX() - lowPoints.getBlockX()) + 1;
    }

    public int getYSize() {
        if (!this.valid())
            return 0;
        return (highPoints.getBlockY() - lowPoints.getBlockY()) + 1;
    }

    public int getZSize() {
        if (!this.valid())
            return 0;
        return (highPoints.getBlockZ() - lowPoints.getBlockZ()) + 1;
    }

    public Location getHighLocation() {
        if (!this.valid())
            return null;
        return highPoints.toLocation(this.getWorld());
    }

    public Vector getHighPoint() {
        return highPoints;
    }

    public Location getLowLocation() {
        if (!this.valid())
            return null;
        return lowPoints.toLocation(this.getWorld());
    }

    public Vector getLowPoint() {
        return lowPoints;
    }

    public World getWorld() {
        return this.world;
    }

    public List<CMIChunkReference> getChunks() {
        return getChunks(0);
    }

    public List<CMIChunkReference> getChunks(int range) {
        List<CMIChunkReference> chunks = new ArrayList<>();
        Vector high = this.highPoints;
        Vector low = this.lowPoints;
        int lowX = CMIChunkReference.getChunkCoord(low.getBlockX() - range);
        int lowZ = CMIChunkReference.getChunkCoord(low.getBlockZ() - range);
        int highX = CMIChunkReference.getChunkCoord(high.getBlockX() + range);
        int highZ = CMIChunkReference.getChunkCoord(high.getBlockZ() + range);

        for (int x = lowX; x <= highX; x++) {
            for (int z = lowZ; z <= highZ; z++) {
                chunks.add(new CMIChunkReference(x, z));
            }
        }
        return chunks;
    }

    public void setArea(CMICuboidArea area) {
        this.highPoints = area.getHighPoint().clone();
        this.lowPoints = area.getLowPoint().clone();
        this.p1 = area.p1.clone();
        this.p2 = area.p2.clone();
        this.world = area.getWorld();
        recheck();
    }

    public void setHighLocation(Location highLocation) {
        if (highLocation == null)
            return;
        world = highLocation.getWorld();
        setHighPoint(highLocation.toVector());
    }

    public void setHighPoint(Vector highLocation) {
        this.p1 = highLocation;
        recheck();
    }

    public void setLowLocation(Location lowLocation) {
        if (lowLocation == null)
            return;
        world = lowLocation.getWorld();
        setLowPoint(lowLocation.toVector());
    }

    public void setLowPoint(Vector lowPoint) {
        this.p2 = lowPoint;
        recheck();
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setLocation(Location location) {
        if (location == null)
            return;
        world = location.getWorld();
        setPoint(location.toVector());
    }

    public void setPoint(Vector vector) {
        if (vector == null)
            return;
        if (this.p1 == null)
            this.p1 = vector;
        else
            this.p2 = vector;
        recheck();
    }

//    public void setWorld(World world) {
//	this.world = world;
//    }

    @Deprecated
    public Location getMiddleLocation() {
        if (!this.valid())
            return null;
        return new Location(this.world,
                (this.getLowPoint().getX() + this.getHighPoint().getX()) / 2,
                (this.getLowPoint().getY() + this.getHighPoint().getY()) / 2,
                (this.getLowPoint().getZ() + this.getHighPoint().getZ()) / 2);
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
    public String toString() {
        if (!this.valid())
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append(this.lowPoints.getX()).append(":").append(this.lowPoints.getY()).append(":").append(this.lowPoints.getZ()).append(":")
                .append(this.highPoints.getX()).append(":").append(this.highPoints.getY()).append(":").append(this.highPoints.getZ());
        return sb.toString();
    }

    public boolean shift(Player player, double amount) {
        return shift(getDirection(player), amount);
    }

    public boolean shift(Direction d, double amount) {
        if (!valid()) {
            return false;
        }
        if (d == null) {
            return false;
        }
        switch (d) {
        case DOWN:
            double oldy = getLowPoint().getBlockY() - amount;
            if (oldy < MIN_HEIGHT) {
                oldy = MIN_HEIGHT;
            }
            getLowPoint().setY(oldy);
            getHighPoint().setY(getHighPoint().getBlockY() - amount);
            break;
        case MINUSX:
            getLowPoint().setX(getLowPoint().getBlockX() - amount);
            getHighPoint().setX(getHighPoint().getBlockX() - amount);
            break;
        case MINUSZ:
            getLowPoint().setZ(getLowPoint().getBlockZ() - amount);
            getHighPoint().setZ(getHighPoint().getBlockZ() - amount);
            break;
        case PLUSX:
            getHighPoint().setX(getHighPoint().getBlockX() + amount);
            getLowPoint().setX(getLowPoint().getBlockX() + amount);
            break;
        case PLUSZ:
            getHighPoint().setZ(getHighPoint().getBlockZ() + amount);
            getLowPoint().setZ(getLowPoint().getBlockZ() + amount);
            break;
        case UP:
            oldy = getHighPoint().getBlockY() + amount;
            if (oldy > getMaxWorldHeight()) {
                oldy = getMaxWorldHeight();
            }
            getHighPoint().setY(oldy);
            getLowPoint().setY(getLowPoint().getBlockY() + amount);
            break;
        default:
            break;
        }
        return true;
    }

    public boolean expand(Player player, double amount) {
        return expand(getDirection(player), amount);
    }

    public boolean expand(Direction d, double amount) {
        if (!valid()) {
            return false;
        }

        if (d == null) {
            return false;
        }

        switch (d) {
        case DOWN:
            double oldy = getLowPoint().getBlockY() - amount;
            if (oldy < MIN_HEIGHT) {
                oldy = MIN_HEIGHT;
            }
            getLowPoint().setY(oldy);
            break;
        case MINUSX:
            getLowPoint().setX(getLowPoint().getBlockX() - amount);
            break;
        case MINUSZ:
            getLowPoint().setZ(getLowPoint().getBlockZ() - amount);
            break;
        case PLUSX:
            getHighPoint().setX(getHighPoint().getBlockX() + amount);
            break;
        case PLUSZ:
            getHighPoint().setZ(getHighPoint().getBlockZ() + amount);
            break;
        case UP:
            oldy = getHighPoint().getBlockY() + amount;
            if (oldy > getMaxWorldHeight()) {
                oldy = getMaxWorldHeight();
            }
            getHighPoint().setY(oldy);
            break;
        default:
            break;
        }
        return true;
    }

    public boolean contract(Player player, double amount) {
        if (!this.valid()) {
            return false;
        }
        return contract(getDirection(player), amount);
    }

    public boolean contract(Direction d, double amount) {
        if (!this.valid()) {
            return false;
        }
        if (d == null) {
            return false;
        }

        switch (d) {
        case DOWN:
            double oldy = getHighPoint().getBlockY() - amount;
            if (oldy > getMaxWorldHeight()) {
                oldy = getMaxWorldHeight();
            }

            oldy = getHighPoint().getBlockY() - amount;
            oldy = getLowPoint().getY() > oldy ? getLowPoint().getY() : oldy;

            getHighPoint().setY(oldy);
            break;
        case MINUSX:
            double res = getHighPoint().getBlockX() - amount;
            res = getLowPoint().getX() > res ? getLowPoint().getX() : res;
            getHighPoint().setX(res);
            break;
        case MINUSZ:
            res = getHighPoint().getBlockZ() - amount;
            res = getLowPoint().getZ() > res ? getLowPoint().getZ() : res;
            getHighPoint().setZ(res);
            break;
        case PLUSX:
            res = getLowPoint().getBlockX() + amount;
            res = getHighPoint().getX() < res ? getHighPoint().getX() : res;
            getLowPoint().setX(res);
            break;
        case PLUSZ:
            res = getLowPoint().getBlockZ() + amount;
            res = getHighPoint().getZ() < res ? getHighPoint().getZ() : res;
            getLowPoint().setZ(res);
            break;
        case UP:
            oldy = getLowPoint().getBlockY() + amount;
            if (oldy < MIN_HEIGHT) {
                oldy = MIN_HEIGHT;
            }
            oldy = getLowPoint().getBlockY() + amount;
            oldy = getHighPoint().getY() < oldy ? getHighPoint().getY() : oldy;
            getLowPoint().setY(oldy);
            break;
        default:
            break;
        }

        return true;
    }

    private int getMaxWorldHeight() {
        if (this.getWorld() == null)
            return 256;
        switch (this.getWorld().getEnvironment()) {
        case NETHER:
            return 128;
        case NORMAL:
        case THE_END:
            return 256;
        default:
            break;
        }

        return 256;
    }

    private static Direction getDirection(Player player) {

        int yaw = (int) player.getLocation().getYaw();

        if (yaw < 0)
            yaw += 360;

        yaw += 45;
        yaw %= 360;

        int facing = yaw / 90;

        float pitch = player.getLocation().getPitch();
        if (pitch < -50)
            return Direction.UP;
        if (pitch > 50)
            return Direction.DOWN;
        if (facing == 1) // east
            return Direction.MINUSX;
        if (facing == 3) // west
            return Direction.PLUSX;
        if (facing == 2) // north
            return Direction.MINUSZ;
        if (facing == 0) // south
            return Direction.PLUSZ;
        return null;
    }

    public enum Direction {
        UP, DOWN, PLUSX, PLUSZ, MINUSX, MINUSZ
    }
}
