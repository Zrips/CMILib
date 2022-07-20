package net.Zrips.CMILib.Container;

public class CMIValidate {

    public static int validate(Integer value, int defaultValue) {
	return value == null ? defaultValue : value;
    }

}
