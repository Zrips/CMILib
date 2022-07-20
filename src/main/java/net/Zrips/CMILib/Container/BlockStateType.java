package net.Zrips.CMILib.Container;

public enum BlockStateType {
    disarmed(boolean.class),
    triggered(boolean.class),
    half(String.class),
    powered(boolean.class),
    conditional(boolean.class),
    axis(String.class),
    type(String.class),
    down(boolean.class),
    mode(String.class),
    lit(boolean.class),
    west(boolean.class),
    layers(int.class),
    up(boolean.class),
    drag(boolean.class),
    persistent(boolean.class),
    locked(boolean.class),
    inverted(boolean.class),
    eggs(int.class),
    shape(String.class),
    level(int.class),
    hatch(int.class),
    moisture(int.class),
    unstable(boolean.class),
    eye(boolean.class),
    in_wall(boolean.class),
    delay(int.class),
    has_record(boolean.class),
    has_bottle_0(boolean.class),
    attached(boolean.class),
    has_bottle_1(boolean.class),
    has_bottle_2(boolean.class),
    note(int.class),
    hinge(String.class),
    waterlogged(boolean.class),
    distance(int.class),
    south(boolean.class),
    part(String.class),
    north(boolean.class),
    facing(String.class),
    instrument(String.class),
    extended(boolean.class),
    enabled(boolean.class),
    east(boolean.class),
    pickles(int.class),
    power(int.class),
    snowy(boolean.class),
    occupied(boolean.class),
    rotation(int.class),
    bites(int.class),
    face(String.class),
    stage(int.class),
    Short(boolean.class),
    age(int.class),
    open(boolean.class);

    private Class<?> c;

    BlockStateType(Class<?> c) {
	this.c = c;
    }

    public static BlockStateType getByName(String name) {
	for (BlockStateType one : BlockStateType.values()) {
	    if (one.toString().equalsIgnoreCase(name))
		return one;

	}
	return null;
    }

    public Class<?> getClassType() {
	return c;
    }
}
