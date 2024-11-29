package net.Zrips.CMILib.Time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import net.Zrips.CMILib.Locale.LC;

public class CMITimeManager {

    static double tPHour = 1000d;
    static double tPMin = 1000d / 60d;
    static double tPSec = 1000d / 60d / 60d;

    static int dayTime = 600;
    static int sunriseTime = 90;
    static int sunsetTime = 90;
    static int nightTime = 420;

    private List<String> worlds = new ArrayList<String>();

    public List<String> getWorlds() {
        return worlds;
    }

    public static int timeInInt() {
        return timeInInt(System.currentTimeMillis());
    }

    public static int timeInInt(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return Integer.valueOf(new SimpleDateFormat("YYMMdd").format(calendar.getTime()));
    }

    public static String to24hour(Long ticks) {
        long hours = ticks / 1000 + 6;
        long minutes = (ticks % 1000) * 60 / 1000;
        if (hours >= 24)
            hours = hours - 24;
        return hours + ":" + (minutes < 10 ? "0" + minutes : minutes);
    }

    public static String to24hourShort(Long ticks) {
        return to24hourShort(ticks, true);
    }

    public static String to24hourShort(Long ticks, boolean trim) {

        long years = ticks / 1000L / 60L / 60L / 24L / 365L;
        ticks = ticks - (years * 1000L * 60L * 60L * 24L * 365L);

        long days = ticks / 1000L / 60L / 60L / 24L;
        ticks = ticks - (days * 1000L * 60L * 60L * 24L);

        long hours = ticks / 1000L / 60L / 60L;
        ticks = ticks - (hours * 1000L * 60L * 60L);

        long minutes = ticks / 1000L / 60L;
        ticks = ticks - (minutes * 1000L * 60L);

        long sec = ticks / 1000L;
        ticks = ticks - (sec * 1000L);

        String time = "";

        if (years > 0)
            time += years == 1 ? LC.info_oneYear.getLocale("[years]", years) : LC.info_years.getLocale("[years]", years);

        if (days > 0 || (!trim && years > 0))
            time += days == 1 ? LC.info_oneDay.getLocale("[days]", days) : LC.info_day.getLocale("[days]", days);

        if (hours > 0 || (!trim && (days > 0 || years > 0)))
            time += hours == 1 ? LC.info_oneHour.getLocale("[hours]", hours) : LC.info_hour.getLocale("[hours]", hours);

        if (minutes > 0 || (!trim && (hours > 0 || days > 0 || years > 0)))
            time += LC.info_min.getLocale("[mins]", minutes);

        if (sec > 0 || (!trim && (minutes > 0 || hours > 0 || days > 0 || years > 0)))
            time += LC.info_sec.getLocale("[secs]", sec);

        if (time.isEmpty())
            time += LC.info_sec.getLocale("[secs]", 0);

        return time;
    }

    public static String to24hourAproximateShort(Long ticks, boolean trim) {

        long years = ticks / 1000L / 60L / 60L / 24L / 365L;

        // rounding to show only days as minimum
        if (years > 0)
            ticks = (ticks / 1000L / 60L / 60L / 24L) * 1000L * 60L * 60L * 24L;

        ticks = ticks - (years * 1000L * 60L * 60L * 24L * 365L);

        int months = 0;
        int weeks = 0;

        long days = ticks / 1000L / 60L / 60L / 24L;

        if (days > 30) {
            months = (int) (days / 30);
            days = days - (months * 30);
        }

        if (days > 7) {
            weeks = (int) (days / 7);
            days = days - (weeks * 7);
        }

        // rounding to show only hours as minimum
        if (months > 0)
            ticks = (ticks / 1000L / 60L / 60L) * 1000L * 60L * 60L;

        // rounding to show only minutes as minimum
        if (weeks > 0)
            ticks = (ticks / 1000L / 60L) * 1000L * 60L;

        ticks = ticks - (months * 1000L * 60L * 60L * 24L * 30);
        ticks = ticks - (weeks * 1000L * 60L * 60L * 24L * 7);

        ticks = ticks - (days * 1000L * 60L * 60L * 24L);

        long hours = ticks / 1000L / 60L / 60L;
        ticks = ticks - (hours * 1000L * 60L * 60L);

        long minutes = ticks / 1000L / 60L;
        ticks = ticks - (minutes * 1000L * 60L);

        long sec = ticks / 1000L;
        ticks = ticks - (sec * 1000L);

        String time = "";

        if (years > 0)
            time += years == 1 ? LC.info_oneYear.getLocale("[years]", years) : LC.info_years.getLocale("[years]", years);

        if (months > 0 || (!trim && years > 0))
            time += months == 1 ? LC.info_oneMonth.getLocale("[months]", months) : LC.info_months.getLocale("[months]", months);

        if (weeks > 0 || (!trim && (months > 0 || years > 0)))
            time += weeks == 1 ? LC.info_oneWeek.getLocale("[weeks]", weeks) : LC.info_weeks.getLocale("[weeks]", weeks);

        if (days > 0 || (!trim && (weeks > 0 || months > 0) && years == 0))
            time += days == 1 ? LC.info_oneDay.getLocale("[days]", days) : LC.info_day.getLocale("[days]", days);

        if (hours > 0 || (!trim && (days > 0 || weeks > 0 || months > 0 || years > 0)))
            time += hours == 1 ? LC.info_oneHour.getLocale("[hours]", hours) : LC.info_hour.getLocale("[hours]", hours);

        if (minutes > 0 || (!trim && (hours > 0 || days > 0 || weeks > 0 || months > 0 || years > 0)))
            time += LC.info_min.getLocale("[mins]", minutes);

        if (sec > 0 || (!trim && (minutes > 0 || hours > 0 || days > 0 || weeks > 0 || months > 0 || years > 0)))
            time += LC.info_sec.getLocale("[secs]", sec);

        if (time.isEmpty())
            time += LC.info_sec.getLocale("[secs]", 0);

        return time;
    }

    public static String toOnlyHoursShort(Long ticks, boolean trim, boolean includeMinutes) {

        long years = ticks / 1000L / 60L / 60L / 24L / 365L;
        ticks = ticks - (years * 1000L * 60L * 60L * 24L * 365L);

        long days = ticks / 1000L / 60L / 60L / 24L;
        ticks = ticks - (days * 1000L * 60L * 60L * 24L);

        long hours = ticks / 1000L / 60L / 60L;
        ticks = ticks - (hours * 1000L * 60L * 60L);

        long minutes = ticks / 1000L / 60L;
        ticks = ticks - (minutes * 1000L * 60L);

        long sec = ticks / 1000L;
        ticks = ticks - (sec * 1000L);

        String time = "";

        hours += (years * 365 * 24) + (days * 24);

        String fraction = "";

        if (minutes != 0 && !trim && !includeMinutes) {
            fraction = "." + (minutes * 100 / 60);
        }

        if (hours > 0 || (!trim && (days > 0 || years > 0)))
            time += hours == 1 ? LC.info_oneHour.getLocale("[hours]", hours + fraction) : LC.info_hour.getLocale("[hours]", hours + fraction);

        if (includeMinutes)
            if (minutes > 0 || (!trim && (hours > 0 || days > 0 || years > 0)))
                time += LC.info_min.getLocale("[mins]", minutes);

        return time;
    }

    public static String to12hour(Long ticks) {
        long hours = ticks / 1000 + 6;
        long minutes = (ticks % 1000) * 60 / 1000;
        if (hours >= 24)
            hours = hours - 24;

        String form = LC.info_variables_am.getLocale();

        if (hours >= 12)
            form = LC.info_variables_pm.getLocale();
        if (hours > 12)
            hours -= 12;

        return hours + ":" + (minutes < 10 ? "0" + minutes : minutes) + form;
    }

    public static TimeInfo convertToTicks(TimeInfo tInfo) {
        double minticks = ((tInfo.getMinutes() * tPMin) * 100) % 100;
        double Fmin = tInfo.getMinutes() * tPMin;
        if (minticks > 60)
            Fmin += 2;
        else if (minticks > 30)
            Fmin++;
        tInfo.setTicks((int) ((tInfo.getHours() * tPHour) + Fmin + (tInfo.getSeconds() * tPSec)));
        tInfo.setTicks(tInfo.getTicks() - 6000);
        tInfo.setTicks(tInfo.getTicks() < 0 ? 24000 - tInfo.getTicks() : tInfo.getTicks());
        tInfo.setTicks(tInfo.getTicks() > 24000 ? 24000 - (tInfo.getTicks() % 24000) : tInfo.getTicks());
        return (tInfo);
    }

    public static TimeInfo stringToTimeInfo(String time) {

        TimeInfo tInfo = new TimeInfo();
        time = time.toLowerCase();

        List<String> splited = new ArrayList<String>();

        int additional = 0;
        if (time.contains("pm")) {
            time = time.replace("pm", "");
            additional = 12;
        } else if (time.contains("am")) {
            time = time.replace("am", "");
        } else if (time.contains("ticks") || !time.contains(":")) {
            time = time.replace("ticks", "");
            long ticks = -1;
            try {
                ticks = Long.parseLong(time);
            } catch (NumberFormatException e) {
                return tInfo;
            }
            if (ticks < 0)
                return tInfo;
            tInfo.setTicks((int) ticks);
            return tInfo;
        }

        if (time.contains(":")) {
            splited.addAll(new LinkedList<String>(Arrays.asList(time.split(":"))));
        } else
            splited.add(time);

        try {
            tInfo.setHours(Integer.parseInt(splited.get(0)));
        } catch (NumberFormatException e) {
        }

        if (tInfo.getHours() > 24 || tInfo.getHours() < 0)
            tInfo.setHours(-1);

        if (splited.size() > 1)
            try {
                tInfo.setMinutes(Integer.parseInt(splited.get(1)));
            } catch (NumberFormatException e) {
            }

        if (tInfo.getMinutes() > 60 || tInfo.getMinutes() < 0)
            tInfo.setHours(-1);

        if (splited.size() > 2)
            try {
                tInfo.setSeconds(Integer.parseInt(splited.get(2)));
            } catch (NumberFormatException e) {
            }

        if (tInfo.getSeconds() > 60 || tInfo.getSeconds() < 0)
            tInfo.setHours(-1);

        tInfo.setHours(tInfo.getHours() + additional);

        return tInfo;
    }

    public static String MiliToDate(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        return formatter.format(calendar.getTime());
    }

    public static int[] splitToComponentDate(Long biggys) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(biggys);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int[] ints = { mYear, mMonth, mDay };
        return ints;
    }

    public static int[] splitToComponentTimes(Long biggys) {
        int biggy = (int) (System.currentTimeMillis() - biggys) / 1000;
        int hours = biggy / 3600;
        int mins = (biggy % 3600) / 60;
        int secs = biggy % 60;

        int[] ints = { hours, mins, secs };
        return ints;
    }
}
