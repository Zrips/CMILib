package net.Zrips.CMILib.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradientColor {

    private String startingColor = null;
    private String startingTextFormat = null;

    private String endingColor = null;
    private String nextChar = null;

    private String prevHexColor = null;
    private double prevDistance = Double.MAX_VALUE;
    private double prevDelta = Double.MAX_VALUE;

    private StringBuilder modifiedText = new StringBuilder();
    private StringBuilder toBeReplacedText = new StringBuilder();

    private String replaceWith = null;

    public GradientColor(String startingColor, String startingTextFormat) {
        this.startingColor = startingColor;
        this.startingTextFormat = startingTextFormat;
    }

    public String getStartingColor() {
        return startingColor;
    }

    public GradientColor setStartingColor(String startingColor) {
        this.startingColor = startingColor;
        return this;
    }

    public String getStartingTextFormat() {
        return startingTextFormat;
    }

    public GradientColor setStartingTextFormat(String startingTextFormat) {
        this.startingTextFormat = startingTextFormat;
        return this;
    }

    public String getPrevHexColor() {
        return prevHexColor;
    }

    public GradientColor setPrevHexColor(String prevHexColor) {
        this.prevHexColor = prevHexColor;
        return this;
    }

    public double getPrevDistance() {
        return prevDistance;
    }

    public GradientColor setPrevDistance(double prevDistance) {
        this.prevDistance = prevDistance;
        return this;
    }

    public String getModifiedText() {
        return modifiedText.toString();
    }

    public GradientColor addToModifiedText(String modifiedText) {
        this.modifiedText.append(modifiedText);
        return this;
    }

    public StringBuilder getReplaceWhat() {
        return toBeReplacedText;
    }

    public GradientColor addReplaceWhat(String toBeReplacedText) {
        this.toBeReplacedText.append(toBeReplacedText);
        return this;
    }

    public String getEndingColor() {
        return endingColor;
    }

    public GradientColor setEndingColor(String endingColor) {
        this.endingColor = endingColor;
        return this;
    }

    public String getNextChar() {
        return nextChar;
    }

    public GradientColor setNextChar(String nextChar) {
        this.nextChar = nextChar;
        return this;
    }

    public String getReplaceWith() {
        return replaceWith;
    }

    public GradientColor setReplaceWith(String replaceWith) {
        this.replaceWith = replaceWith;
        return this;
    }

    public void calculateFinalString() {

        CMIChatColor startingColor = CMIChatColor.getColor(getStartingColor());
        CMIChatColor endColor = CMIChatColor.getColor(getEndingColor());

        addReplaceWhat(CMIChatColor.colorCodePrefix + getEndingColor() + CMIChatColor.colorCodeSuffix + (getStartingTextFormat() == null ? "" : getStartingTextFormat()) + getNextChar());

        addToModifiedText(getNextChar());

        StringBuilder with = new StringBuilder();

        StringBuilder formatedStarting = new StringBuilder();
        if (getModifiedText().length() > 1) {
            formatedStarting.append(CMIChatColor.colorCodePrefix);
            formatedStarting.append(startingColor.getCleanName());
            formatedStarting.append(">");
            formatedStarting.append(CMIChatColor.colorCodeSuffix);
            if (getStartingTextFormat() != null)
                formatedStarting.append(getStartingTextFormat());
            formatedStarting.append(getModifiedText());

            formatedStarting.append(CMIChatColor.colorCodePrefix);
            formatedStarting.append(endColor.getCleanName());
            formatedStarting.append("<");
            formatedStarting.append(CMIChatColor.colorCodeSuffix);
        }
        with.append(formatedStarting);

        setReplaceWith(with.toString());
    }

    public double getPreviousDelta() {
        return prevDelta;
    }

    public GradientColor setPreviousDelta(double prevDelta) {
        this.prevDelta = prevDelta;
        return this;
    }

    private static Pattern gradientDetectionPattern = Pattern.compile("(?=(\\{#([0-9A-Fa-f]{6}|[a-zA-Z_]{3,})\\}((?:&[0-9a-fk-or])*)(.)\\{#([0-9A-Fa-f]{6}|[a-zA-Z_]{3,})\\}((?:&[0-9a-fk-or])*)(.)))");
    private static Pattern cleanupPattern = Pattern.compile("\\{#([0-9A-Za-z_]{3,})<\\}\\{#\\1>\\}");

    private static final Pattern gradientPattern = Pattern.compile("(\\{(#([^\\{\\}]*)?)>\\})(.*?)(\\{(#([^\\{\\}]*)?)<(>?)\\})");
//    private static final Pattern gradientPattern = Pattern.compile("(\\{(#[^\\{\\}]*?)>\\})(.*?)(\\{(#[^\\{\\}]*?)<(>?)\\})");

    public static String deconvert(String text) {

        text = convertLegacyToHex(text);

        String original = text;

        text = cycleOver(text);

        if (text.contains("<" + CMIChatColor.colorCodeSuffix)) {
            Matcher match = cleanupPattern.matcher(text);
            while (match.find()) {
                String fullMatch = match.group();
                String color = match.group(1);
                text = text.replace(fullMatch, CMIChatColor.colorCodePrefix + color + "<>" + CMIChatColor.colorCodeSuffix);
            }
        }

        if (!CMIChatColor.translate(text).equals(CMIChatColor.translate(original))) {
            text = original;
        }

        return text;
    }

    private static String cycleOver(String text) {

        if (!text.contains(CMIChatColor.colorCodePrefix))
            return text;

        String original = text;

        Matcher match = gradientDetectionPattern.matcher(text);

        List<GradientColor> gradients = new ArrayList<>();

        GradientColor gc = null;

        while (match.find()) {

            String firstHex = match.group(2);
            String firstFormats = match.group(3);
            String firstChar = match.group(4);

            String secondHex = match.group(5);
            String secondChar = match.group(7);

            CMIChatColor fromColor = CMIChatColor.getColor(firstHex);
            CMIChatColor toColor = CMIChatColor.getColor(secondHex);

            if (gc == null)
                gc = new GradientColor(firstHex, firstFormats);

            int rDiff = fromColor.getRed() - toColor.getRed();
            int gDiff = fromColor.getGreen() - toColor.getGreen();
            int bDiff = fromColor.getBlue() - toColor.getBlue();

            double dist = Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);

            if (gc.getPrevDistance() == Double.MAX_VALUE)
                gc.setPrevDistance(dist);

            double delta = Math.abs(gc.getPrevDistance() - dist);

            if (gc.getPreviousDelta() == Double.MAX_VALUE)
                gc.setPreviousDelta(delta);

            if (delta > 2) {
                gc.calculateFinalString();
                gradients.add(gc);
                gc = null;
                break;
            }

            gc.addReplaceWhat(CMIChatColor.colorCodePrefix + firstHex + CMIChatColor.colorCodeSuffix + (gc.getStartingTextFormat() == null ? "" : gc.getStartingTextFormat()) + firstChar);
            gc.addToModifiedText(firstChar);
            gc.setEndingColor(secondHex);
            gc.setNextChar(secondChar);
            gc.setPreviousDelta(delta);
            gc.setPrevDistance(dist);
        }

        if (gc != null) {
            gc.calculateFinalString();
            gradients.add(gc);
        }

        for (GradientColor gradient : gradients) {
            text = text.replace(gradient.getReplaceWhat(), gradient.getReplaceWith());
        }

        if (!gradients.isEmpty() && !original.equals(text)) {
            text = deconvert(text);
        }

        return text;
    }

    private static String legacyCodeToHex(char code) {
        switch (Character.toLowerCase(code)) {
        case '0':
            return CMICustomColors.Black.getName();
        case '1':
            return CMICustomColors.Dark_Blue.getName();
        case '2':
            return CMICustomColors.Dark_Green.getName();
        case '3':
            return CMICustomColors.Dark_Aqua.getName();
        case '4':
            return CMICustomColors.Dark_Red.getName();
        case '5':
            return CMICustomColors.Dark_Purple.getName();
        case '6':
            return CMICustomColors.Gold.getName();
        case '7':
            return CMICustomColors.Gray.getName();
        case '8':
            return CMICustomColors.Dark_Gray.getName();
        case '9':
            return CMICustomColors.Blue.getName();
        case 'a':
            return CMICustomColors.Green.getName();
        case 'b':
            return CMICustomColors.Aqua.getName();
        case 'c':
            return CMICustomColors.Red.getName();
        case 'd':
            return CMICustomColors.Light_Purple.getName();
        case 'e':
            return CMICustomColors.Yellow.getName();
        case 'f':
            return CMICustomColors.White.getName();
        default:
            return null;
        }
    }
//    private static String legacyCodeToHex(char code) {
//        switch (Character.toLowerCase(code)) {
//        case '0':
//            return "000000"; // black
//        case '1':
//            return "0000aa"; // dark blue
//        case '2':
//            return "00aa00"; // dark green
//        case '3':
//            return "00aaaa"; // dark aqua
//        case '4':
//            return "aa0000"; // dark red
//        case '5':
//            return "aa00aa"; // dark purple
//        case '6':
//            return "ffaa00"; // gold
//        case '7':
//            return "aaaaaa"; // gray
//        case '8':
//            return "555555"; // dark gray
//        case '9':
//            return "5555ff"; // blue
//        case 'a':
//            return "55ff55"; // green
//        case 'b':
//            return "55ffff"; // aqua
//        case 'c':
//            return "ff5555"; // red
//        case 'd':
//            return "ff55ff"; // light purple
//        case 'e':
//            return "ffff55"; // yellow
//        case 'f':
//            return "ffffff"; // white
//        default:
//            return null;
//        }
//    }

    private static boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9')
            || (c >= 'a' && c <= 'f')
            || (c >= 'A' && c <= 'F');
    }

    public static String convertLegacyToHex(String input) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '&') {
                if (i + 13 < input.length() && (input.charAt(i + 1) == 'x' || input.charAt(i + 1) == 'X')) {
                    StringBuilder hex = new StringBuilder();
                    boolean valid = true;

                    for (int j = 0; j < 6; j++) {
                        int pos = i + 2 + j * 2;
                        if (input.charAt(pos) != '&') {
                            valid = false;
                            break;
                        }
                        char hexChar = input.charAt(pos + 1);
                        if (!isHexDigit(hexChar)) {
                            valid = false;
                            break;
                        }
                        hex.append(hexChar);
                    }

                    if (valid) {
                        CMIChatColor color = CMIChatColor.getByHex(hex.toString());
                        if (color != null)
                            output.append("{#").append(color.getCleanName()).append("}");
                        else
                            output.append("{#").append(hex).append("}");
                        i += 13;
                        continue;
                    }
                }

                if (i + 1 < input.length()) {
                    char code = input.charAt(i + 1);
                    String hex = legacyCodeToHex(code);
                    if (hex != null) {
                        output.append("{#").append(hex).append("}");
                        i++;
                        continue;
                    }
                }
            }
            output.append(c);
        }

        return output.toString();
    }

    public static CMIChatColor mixColors(CMIChatColor color1, CMIChatColor color2, double percent) {
        percent = percent / 100D;
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color2.getRed() * percent + color1.getRed() * inverse_percent);
        int greenPart = (int) (color2.getGreen() * percent + color1.getGreen() * inverse_percent);
        int bluePart = (int) (color2.getBlue() * percent + color1.getBlue() * inverse_percent);
//        This slow? Cache results?
        String hexCode = '#' + Integer.toHexString((redPart << 16) | (greenPart << 8) | bluePart).toUpperCase(Locale.ROOT);
//        String hexCode = String.format("#%02x%02x%02x", redPart, greenPart, bluePart);
        return new CMIChatColor(hexCode);
    }

    public static String processGradient(String text) {
        Matcher gradientMatch = gradientPattern.matcher(text);

        while (gradientMatch.find()) {
            String fullmatch = gradientMatch.group();
            CMIChatColor c1 = CMIChatColor.getColor(CMIChatColor.colorCodePrefix + gradientMatch.group(3) + CMIChatColor.colorCodeSuffix);
            CMIChatColor c2 = CMIChatColor.getColor(CMIChatColor.colorCodePrefix + gradientMatch.group(7) + CMIChatColor.colorCodeSuffix);

            if (c1 == null || c2 == null) {
                continue;
            }

            String gtext = gradientMatch.group(4);
            boolean continuous = !gradientMatch.group(8).isEmpty();
            StringBuilder updated = new StringBuilder();

            Set<CMIChatColor> formats = CMIChatColor.getFormats(gtext);

            gtext = CMIChatColor.stripColor(gtext);

            for (int i = 0; i < gtext.length(); i++) {
                char ch = gtext.charAt(i);
                int length = gtext.length();
                length = length < 2 ? 2 : length;
                double percent = (i * 100D) / (length - 1);
                CMIChatColor mix = mixColors(c1, c2, percent);
                updated.append(CMIChatColor.colorCodePrefix).append(mix.getHex()).append(CMIChatColor.colorCodeSuffix);
                if (!formats.isEmpty()) {
                    for (CMIChatColor one : formats) {
                        updated.append("&").append(one.getChar());
                    }
                }

                int codePoint = Character.codePointAt(gtext, i);

                if (codePoint > 127) {
                    if (Character.isSurrogate(ch)) {
                        // Should be "" instead of StringOf to get correct results
                        updated.append("" + ch + gtext.charAt(i + 1));
                        i++;
                    } else {
                        updated.append("" + ch);
                    }
                } else {
                    updated.append(String.valueOf(ch));
                }
            }

            if (continuous) {
                updated.append(CMIChatColor.colorCodePrefix).append(gradientMatch.group(7)).append(CMIChatColor.gradientStart).append(CMIChatColor.colorCodeSuffix);
            }

            text = text.replace(fullmatch, updated.toString());

            if (continuous) {
                text = processGradient(text);
            }
        }

        return text;
    }
}
