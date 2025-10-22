package net.Zrips.CMILib.Container;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.TreeMap;

public class CMINumber {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    private final static Random random = new Random();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public static final String toRoman(int number) {
        if (number > 3999)
            return String.valueOf(number);

        Integer l = map.floorKey(number);

        if (l == null)
            return String.valueOf(number);

        if (number == l)
            return map.get(number);

        return map.get(l) + toRoman(number - l);
    }

    private final static DecimalFormat dcf = new DecimalFormat("##.##");

    public static String format2Decimals(double number) {
        return dcf.format(number);
    }

    public static double sum(double number1, double number2) {
        return sum(number1, number2, 2);
    }

    public static double sum(double number1, double number2, int precision) {
        double decimals = (int) Math.pow(10, precision);
        return (Math.round((number1 * decimals) + (int) (number2 * decimals))) / decimals;
    }

    public static double clamp(double number, double min, int max) {
        return number < min ? min : number > max ? max : number;
    }

    public static double clamp(double number, double min, double max) {
        return number < min ? min : number > max ? max : number;
    }

    public static int clamp(int number, int min, int max) {
        return number < min ? min : number > max ? max : number;
    }

    public static long clamp(long number, long min, long max) {
        return number < min ? min : number > max ? max : number;
    }

    public static <T extends Number & Comparable<T>> T clamp(T value, T min) {
        if (value == null)
            return min;
        if (value.compareTo(min) < 0)
            return min;
        return value;
    }

    public static <T extends Number> double normalize(T value) {
        if (value == null)
            return 0.0;
        double v = value.doubleValue();
        return Math.max(0.0, Math.min(1.0, v));
    }

    public static boolean fractional(double number) {
        return number != Math.floor(number);
    }

    public static int abs(int num) {
        return num < 0 ? -num : num;
    }

    public static long abs(long num) {
        return num < 0 ? -num : num;
    }

    public static float abs(float num) {
        return num < 0 ? -num : num;
    }

    public static double abs(double num) {
        return num < 0 ? -num : num;
    }

    public static int random(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static double random() {
        return random.nextDouble();
    }
}
