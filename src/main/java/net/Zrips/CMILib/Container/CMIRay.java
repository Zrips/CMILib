package net.Zrips.CMILib.Container;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CMIRay {

    private Vector origin, direction;

    public CMIRay(Vector origin, Vector direction) {
	this.origin = origin;
	this.direction = direction;
    }

    public CMIRay(Player player) {
	this(player.getEyeLocation().toVector(), player.getLocation().getDirection());
    }

    public Vector getOrigin() {
	return origin;
    }

    public Vector getDirection() {
	return direction;
    }

    public double origin(int i) {
	switch (i) {
	case 0:
	    return origin.getX();
	case 1:
	    return origin.getY();
	case 2:
	    return origin.getZ();
	default:
	    return 0;
	}
    }

    public double direction(int i) {
	switch (i) {
	case 0:
	    return direction.getX();
	case 1:
	    return direction.getY();
	case 2:
	    return direction.getZ();
	default:
	    return 0;
	}
    }

    public Vector getPoint(double distance) {
	return direction.clone().normalize().multiply(distance).add(origin);
    }
}
