package net.Zrips.CMILib.Time;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.Zrips.CMILib.Logs.CMIDebug;

public enum timeModifier {
    s(1), m(60), h(60 * 60), d(60 * 60 * 24), w(60 * 60 * 24 * 7), M(60 * 60 * 24 * 30), Y(60 * 60 * 24 * 365);

    static Pattern patern = Pattern.compile("(\\d+[mshdwMY])");

    private int modifier = 0;

    timeModifier(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public static Long getTimeRangeFromString(String time) {
        try {
            return Long.parseLong(time);
        } catch (Exception e) {
        }
        Matcher match = patern.matcher(time);
        Long total = null;

        while (match.find()) {
            String t = match.group(1);
            for (timeModifier one : timeModifier.values()) {
                if (!t.endsWith(one.name()))
                    continue;
                try {
                    Long amount = Long.parseLong(t.substring(0, t.length() - one.name().length()));
                    time = time.replace(t, "");
                    if (total == null)
                        total = 0L;
                    total += amount * one.getModifier();
                } catch (Exception e) {
                    break;
                }
            }
        }

        if (!time.isEmpty())
            return null;

        return total;
    }

    public static Double getDoubleTimeRangeFromString(String time) {
        try {
            return Double.parseDouble(time);
        } catch (Exception e) {
        }
        Matcher match = patern.matcher(time);
        Double total = null;
        while (match.find()) {
            String t = match.group(1);
            for (timeModifier one : timeModifier.values()) {
                if (t.endsWith(one.name())) {
                    try {
                        Double amount = Double.parseDouble(t.substring(0, t.length() - one.name().length()));
                        time = time.replace(t, "");
                        if (total == null)
                            total = 0D;
                        total += amount * Double.valueOf(one.getModifier());
                    } catch (Exception e) {
                        break;
                    }
                }
            }
        }
        if (!time.isEmpty())
            return null;
        return total;
    }
}
