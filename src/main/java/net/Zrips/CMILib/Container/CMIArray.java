package net.Zrips.CMILib.Container;

import java.util.Arrays;

public class CMIArray {

    public static String toString(String[] args, String seperator) {
	return toString(args, seperator, null, null);
    }

    public static String toString(String[] args) {
	return toString(args, null, null, null);
    }

    public static String toString(String[] args, Integer from) {
	return toString(args, null, from, null);
    }

    public static String toString(String[] args, Integer from, Integer to) {
	return toString(args, null, from, to);
    }

    public static String toString(String[] args, String seperator, Integer from, Integer to) {

	from = CMIValidate.validate(from, 0);
	to = CMIValidate.validate(to, args.length);

	StringBuilder msg = new StringBuilder();
	seperator = seperator == null ? " " : seperator;

	for (int i = from; i < to; i++) {
	    if (args.length < i)
		break;
	    if (!msg.toString().isEmpty())
		msg.append(seperator);
	    msg.append(args[i]);
	}

	return msg.toString();
    }

    public static String[] addFirst(String[] args, String value) {
	String[] newArray = new String[args.length + 1];
	newArray[0] = value;
	System.arraycopy(args, 0, newArray, 1, args.length);
	return newArray;
    }

    public static String[] addLast(String[] args, String value) {
	args = Arrays.copyOf(args, args.length + 1);
	args[args.length - 1] = value;
	return args;
    }

    public static String[] removeFirst(String[] args) {
	if (args.length == 0)
	    return args;
	if (args.length == 1)
	    return new String[0];
	return Arrays.copyOfRange(args, 1, args.length);
    }

    public static String[] copyOfRange(String[] args, int from, int to) {
	return Arrays.copyOfRange(args, from, to);
    }

}
