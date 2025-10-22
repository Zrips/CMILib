package net.Zrips.CMILib.Container;

import java.util.List;
import java.util.Random;

public class CMIText {
    static Random random = new Random(System.nanoTime());

    public static String getFirstVariable(String message) {
        if (message == null)
            return null;
        if (!message.contains(" "))
            return message;
        return message.split(" ")[0];
    }

    public static String truncate(String message, int length, String suffix) {
        if (message.length() <= length)
            return message;
        return message.substring(0, length) + (suffix == null ? "" : suffix);
    }

    public static String replaceUnderScoreSpace(String message) {
        String underScore = "U" + random.nextInt(Integer.MAX_VALUE) + "U";
        message = message.replace("__", underScore);
        message = message.replace("_", " ");
        message = message.replace(underScore, "_");
        return message;
    }

    public static boolean isValidString(String message) {
        return message != null && !message.isEmpty();
    }

    public static String firstToUpperCase(String name) {
        if (name.isEmpty())
            return name;

        if (name.length() == 1)
            return name.toUpperCase();

        return name.toLowerCase().replace("_", " ").substring(0, 1).toUpperCase() + name.toLowerCase().replace("_", " ").substring(1);
    }

    public static String everyFirstToUpperCase(String name) {
        return everyFirstToUpperCase(name, true);
    }

    public static String everyFirstToUpperCase(String name, boolean removeUnderScore) {
        if (name.isEmpty())
            return name;

        if (name.length() == 1)
            return name.toUpperCase();

        if (removeUnderScore)
            name = name.replace("_", " ");

        String[] split = name.toLowerCase().split(removeUnderScore ? " " : "_");
        StringBuilder str = new StringBuilder();
        for (String one : split) {
            if (!str.toString().isEmpty()) {
                if (removeUnderScore)
                    str.append(" ");
                else
                    str.append("_");
            }

            if (one.length() > 1)
                str.append(one.substring(0, 1).toUpperCase() + one.substring(1));
            else if (one.length() == 1)
                str.append(one.toUpperCase());
            else
                str.append(one);
        }

        return str.toString();
    }

    public static void toLowerCase(List<String> strings) {
        CMIList.toLowerCase(strings);
    }

}
