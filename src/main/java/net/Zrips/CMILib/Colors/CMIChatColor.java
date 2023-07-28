package net.Zrips.CMILib.Colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Version;

public class CMIChatColor {

    private static final Map<Character, CMIChatColor> BY_CHAR = new HashMap<>();
    private static final Map<String, CMIChatColor> BY_NAME = new HashMap<>();
    private static final LinkedHashMap<String, CMIChatColor> CUSTOM_BY_NAME = new LinkedHashMap<>();
    private static final Map<String, CMIChatColor> CUSTOM_BY_HEX = new HashMap<>();
    private static final TreeMap<String, CMIChatColor> CUSTOM_BY_RGB = new TreeMap<>();

    static {
        for (CMICustomColors one : CMICustomColors.values()) {
            CUSTOM_BY_NAME.put(one.name().toLowerCase().replace("_", ""), new CMIChatColor(one.toString(), one.getHex()));
            CUSTOM_BY_HEX.put(one.getHex().toLowerCase(), new CMIChatColor(one.toString(), one.getHex()));
//	    if (one.getExtra() != null) {
//		for (String extra : one.getExtra()) {
//		    CUSTOM_BY_NAME.put(extra.toLowerCase().replace("_", ""), new CMIChatColor(extra.replace(" ", "_"), one.getHex()));
//		}
//	    }
        }

        for (float x = 0.0F; x <= 1; x += 0.1) {
            for (float z = 0.1F; z <= 1; z += 0.1) {
                for (float y = 0; y <= 1; y += 0.03) {
                    java.awt.Color color = java.awt.Color.getHSBColor(y, x, z);
                    StringBuilder hex = new StringBuilder().append(Integer.toHexString((color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue() & 0xffffff));
                    while (hex.length() < 6) {
                        hex.append("0" + hex);
                    }
                    CMIChatColor.getClosest(hex.toString());
                }
            }
        }
    }

    public static final String colorReplacerPlaceholder = "\uFF06";

    public static final String hexSymbol = "#";

    public static final String colorHexReplacerPlaceholder = "{" + colorReplacerPlaceholder + hexSymbol;

    public static final String colorFontPrefix = "{@";
    public static final String colorCodePrefix = "{" + hexSymbol;
    public static final String colorCodeSuffix = "}";

    private static String charEscape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("/");
                break;
            default:
                if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F') || (ch >= '\u2000' && ch <= '\u20FF')) {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    private static String escape(String text) {
        return text.replace("#", "\\#").replace("{", "\\{").replace("}", "\\}");
    }

    public static final String hexColorRegex = "(\\" + colorCodePrefix + ")([0-9A-Fa-f]{6}|[0-9A-Fa-f]{3})(\\" + colorCodeSuffix + ")";

    public static final Pattern cleanOfficialColorRegexPattern = Pattern.compile("(?<!\\{|:\"|" + colorReplacerPlaceholder + ")#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");

    public static final Pattern cleanQuirkyHexColorRegexPattern = Pattern.compile("&#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");

    public static final Pattern hexColorRegexPattern = Pattern.compile(hexColorRegex);
    public static final Pattern hexColorRegexPatternLast = Pattern.compile(hexColorRegex + "(?!.*\\{#)");
    public static final Pattern hexDeColorNamePattern = Pattern.compile("((&|§)x)(((&|§)[0-9A-Fa-f]){6})");
    public static final String ColorNameRegex = "(\\" + colorCodePrefix + ")([a-zA-Z_]{3,})(\\" + colorCodeSuffix + ")";
    public static final Pattern hexColorNamePattern = Pattern.compile(ColorNameRegex);
    public static final Pattern hexColorNamePatternLast = Pattern.compile(ColorNameRegex + "(?!.*\\{#)");

    public static final String ColorFontRegex = "(\\" + colorFontPrefix + ")([a-zA-Z_]{3,})(\\" + colorCodeSuffix + ")";

    public static final Pattern gradientPattern = Pattern.compile("(\\{(#[^\\{]*?)>\\})(.*?)(\\{(#.*?)<(>?)\\})");

    public static final String hexColorDecolRegex = "(&x)(&[0-9A-Fa-f]){6}";

    public static final Pattern postGradientPattern = Pattern.compile("(" + hexColorRegex + "|" + ColorNameRegex + ")" + "(.)" + "(" + hexColorRegex + "|" + ColorNameRegex + ")");
    public static final Pattern post2GradientPattern = Pattern.compile("(" + hexColorRegex + "|" + ColorNameRegex + ")" + "(.)" + "((" + hexColorRegex + "|" + ColorNameRegex + ")" + "(.))+");
    public static final Pattern fullPattern = Pattern.compile("(&[0123456789abcdefklmnorABCDEFKLMNOR])|" + hexColorRegex + "|" + ColorNameRegex + "|" + ColorFontRegex);

    public static final Pattern formatPattern = Pattern.compile("(&[klmnorKLMNOR])");

    public static final CMIChatColor BLACK = new CMIChatColor("Black", '0', 0, 0, 0);
    public static final CMIChatColor DARK_BLUE = new CMIChatColor("Dark_Blue", '1', 0, 0, 170);
    public static final CMIChatColor DARK_GREEN = new CMIChatColor("Dark_Green", '2', 0, 170, 0);
    public static final CMIChatColor DARK_AQUA = new CMIChatColor("Dark_Aqua", '3', 0, 170, 170);
    public static final CMIChatColor DARK_RED = new CMIChatColor("Dark_Red", '4', 170, 0, 0);
    public static final CMIChatColor DARK_PURPLE = new CMIChatColor("Dark_Purple", '5', 170, 0, 170);
    public static final CMIChatColor GOLD = new CMIChatColor("Gold", '6', 255, 170, 0);
    public static final CMIChatColor GRAY = new CMIChatColor("Gray", '7', 170, 170, 170);
    public static final CMIChatColor DARK_GRAY = new CMIChatColor("Dark_Gray", '8', 85, 85, 85);
    public static final CMIChatColor BLUE = new CMIChatColor("Blue", '9', 85, 85, 255);
    public static final CMIChatColor GREEN = new CMIChatColor("Green", 'a', 85, 255, 85);
    public static final CMIChatColor AQUA = new CMIChatColor("Aqua", 'b', 85, 255, 255);
    public static final CMIChatColor RED = new CMIChatColor("Red", 'c', 255, 85, 85);
    public static final CMIChatColor LIGHT_PURPLE = new CMIChatColor("Light_Purple", 'd', 255, 85, 255);
    public static final CMIChatColor YELLOW = new CMIChatColor("Yellow", 'e', 255, 255, 85);
    public static final CMIChatColor WHITE = new CMIChatColor("White", 'f', 255, 255, 255);
    public static final CMIChatColor OBFUSCATED = new CMIChatColor("Obfuscated", 'k', false);
    public static final CMIChatColor BOLD = new CMIChatColor("Bold", 'l', false);
    public static final CMIChatColor STRIKETHROUGH = new CMIChatColor("Strikethrough", 'm', false);
    public static final CMIChatColor UNDERLINE = new CMIChatColor("Underline", 'n', false);
    public static final CMIChatColor ITALIC = new CMIChatColor("Italic", 'o', false);
    public static final CMIChatColor RESET = new CMIChatColor("Reset", 'r', false, true);
    public static final CMIChatColor HEX = new CMIChatColor("Hex", 'x', false, false);

    private char c = 10;
    private boolean color = true;
    private boolean isReset = false;
    private Pattern pattern = null;
    private int redChannel;
    private int greenChannel;
    private int blueChannel;
    private String hexCode = null;
    private String name;

    public CMIChatColor(String name, char c, int red, int green, int blue) {
        this(name, c, true, false, red, green, blue);
    }

    public CMIChatColor(String hex) {
        this(null, hex);
    }

    public CMIChatColor(String name, String hex) {
        if (hex == null)
            return;
        
        if (hex.startsWith(colorCodePrefix))
            hex = hex.substring(colorCodePrefix.length());
        if (hex.endsWith(colorCodeSuffix))
            hex = hex.substring(0, hex.length() - colorCodeSuffix.length());
        if (hex.startsWith("#"))
            hex = hex.substring(1);

        this.hexCode = hex;
        this.name = name;

        try {
            redChannel = Integer.valueOf(this.hexCode.substring(0, 2), 16);
            greenChannel = Integer.valueOf(this.hexCode.substring(2, 4), 16);
            blueChannel = Integer.parseInt(this.hexCode.substring(4, 6), 16);
        } catch (Throwable e) {
            this.hexCode = null;
        }
    }

    public CMIChatColor(String name, char c, Boolean color) {
        this(name, c, color, false);
    }

    public CMIChatColor(String name, char c, Boolean color, Boolean reset) {
        this(name, c, color, reset, -1, -1, -1);
    }

    public CMIChatColor(String name, char c, Boolean color, Boolean reset, int red, int green, int blue) {
        this.name = name;
        this.c = c;
        this.color = color;
        this.isReset = reset;
        this.pattern = Pattern.compile("(?i)(&[" + c + "])");
        this.redChannel = red;
        this.greenChannel = green;
        this.blueChannel = blue;

        if (Version.isCurrentLower(Version.v1_16_R1) && name.equalsIgnoreCase("Hex"))
            return;
        BY_CHAR.put(Character.valueOf(c), this);
        BY_NAME.put(this.getName().toLowerCase().replace("_", ""), this);
    }

    public static String processGradient(String text) {

        Matcher gradientMatch = gradientPattern.matcher(text);

        while (gradientMatch.find()) {
            String fullmatch = gradientMatch.group();
            CMIChatColor c1 = CMIChatColor.getColor(CMIChatColor.colorCodePrefix + gradientMatch.group(2).replace("#", "") + CMIChatColor.colorCodeSuffix);
            CMIChatColor c2 = CMIChatColor.getColor(CMIChatColor.colorCodePrefix + gradientMatch.group(5).replace("#", "") + CMIChatColor.colorCodeSuffix);

            if (c1 == null || c2 == null) {
                continue;
            }

            String gtext = gradientMatch.group(3);
            boolean continuous = !gradientMatch.group(6).isEmpty();
            StringBuilder updated = new StringBuilder();

            Set<CMIChatColor> formats = getFormats(gtext);

            gtext = stripColor(gtext);

            for (int i = 0; i < gtext.length(); i++) {
                char ch = gtext.charAt(i);
                int length = gtext.length();
                length = length < 2 ? 2 : length;
                double percent = (i * 100D) / (length - 1);
                CMIChatColor mix = CMIChatColor.mixColors(c1, c2, percent);
                updated.append(CMIChatColor.colorCodePrefix + mix.getHex() + CMIChatColor.colorCodeSuffix);
                if (!formats.isEmpty()) {
                    for (CMIChatColor one : formats) {
                        updated.append("&" + one.getChar());
                    }
                }
                updated.append(String.valueOf(ch));
            }

            if (continuous) {
                updated.append(CMIChatColor.colorCodePrefix + gradientMatch.group(5).replace("#", "") + ">" + CMIChatColor.colorCodeSuffix);
            }

            text = text.replace(fullmatch, updated.toString());

            if (continuous) {
                text = processGradient(text);
            }
        }

        return text;
    }

    public static List<String> translate(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, translate(lines.get(i)));
        }
        return lines;
    }

    public static String translate(String text) {

        if (text == null)
            return null;

        text = processGradient(text);

        if (text.contains(colorCodePrefix)) {
            Matcher match = hexColorRegexPattern.matcher(text);
            while (match.find()) {
                String string = match.group();
                if (Version.isCurrentLower(Version.v1_16_R1)) {

                    String copy = string;
                    copy = copy.substring(2, copy.length() - 1);
                    if (copy.length() == 3) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < copy.length(); i++)
                            sb.append(copy.charAt(i) + "" + copy.charAt(i));
                        copy = sb.toString();
                    }
                    copy = getClosestVanilla(copy);
                    text = text.replace(string, copy);

                } else {
                    StringBuilder magic = new StringBuilder("§x");
                    for (char c : string.substring(2, string.length() - 1).toCharArray()) {
                        magic.append('§').append(c);
                        if (string.substring(2, string.length() - 1).length() == 3)
                            magic.append('§').append(c);
                    }
                    text = text.replace(string, magic.toString());
                }
            }

            Matcher nameMatch = hexColorNamePattern.matcher(text);
            while (nameMatch.find()) {
                String string = nameMatch.group(2);
                CMIChatColor cn = getByCustomName(string.toLowerCase().replace("_", ""));
                if (cn == null)
                    continue;
                String gex = cn.getHex();
                StringBuilder magic = new StringBuilder("§x");
                for (char c : gex.toCharArray()) {
                    magic.append('§').append(c);
                }
                text = text.replace(nameMatch.group(), magic.toString());
            }
        }

        if (CMILibConfig.QuirkyHex && text.contains("&" + hexSymbol)) {
            Matcher match = cleanQuirkyHexColorRegexPattern.matcher(text);
            while (match.find()) {
                String string = match.group();
                if (Version.isCurrentLower(Version.v1_16_R1)) {
                    String copy = string;
                    copy = copy.substring(2, copy.length());
                    if (copy.length() == 3) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < copy.length(); i++)
                            sb.append(copy.charAt(i) + "" + copy.charAt(i));
                        copy = sb.toString();
                    }
                    copy = getClosestVanilla(copy);
                    text = text.replace(string, copy);
                } else {
                    StringBuilder magic = new StringBuilder("§x");
                    String shorten = string.substring(2, string.length());
                    for (char c : shorten.toCharArray()) {
                        magic.append('§').append(c);
                        if (shorten.length() == 3)
                            magic.append('§').append(c);
                    }
                    text = text.replace(string, magic.toString());
                }
            }
        }

        if (CMILibConfig.OfficialHex && text.contains(hexSymbol)) {
            Matcher match = cleanOfficialColorRegexPattern.matcher(text);
            while (match.find()) {
                String string = match.group();
                if (Version.isCurrentLower(Version.v1_16_R1)) {
                    String copy = string;
                    copy = copy.substring(1, copy.length());
                    if (copy.length() == 3) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < copy.length(); i++)
                            sb.append(copy.charAt(i) + "" + copy.charAt(i));
                        copy = sb.toString();
                    }
                    copy = getClosestVanilla(copy);
                    text = text.replace(string, copy);
                } else {
                    StringBuilder magic = new StringBuilder("§x");
                    String shorten = string.substring(1, string.length());
                    for (char c : shorten.toCharArray()) {
                        magic.append('§').append(c);
                        if (shorten.length() == 3)
                            magic.append('§').append(c);
                    }
                    text = text.replace(string, magic.toString());
                }
            }
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String convertNamedHex(String text) {

        if (text == null)
            return null;

        text = processGradient(text);

        if (text.contains(colorCodePrefix)) {
            Matcher nameMatch = hexColorNamePattern.matcher(text);
            while (nameMatch.find()) {
                String string = nameMatch.group(2);
                CMIChatColor cn = getByCustomName(string.toLowerCase().replace("_", ""));
                if (cn == null)
                    continue;
                String gex = cn.getHex();
                StringBuilder magic = new StringBuilder(colorCodePrefix);
                for (char c : gex.toCharArray()) {
                    magic.append('&').append(c);
                }
                magic.append(colorCodeSuffix);
                text = text.replace(nameMatch.group(), magic.toString());
            }
        }

        return text;
    }

    public static String applyEqualGradient(String text, List<CMIChatColor> gradients) {
        if (gradients == null || gradients.isEmpty())
            return text;
        int size = text.length() / gradients.size();
        StringBuilder messageWithGradient = new StringBuilder();
        messageWithGradient.append(gradients.get(0).getFormatedHex(">"));
        for (int y = 0; y <= gradients.size() - 1; y++) {
            if (y > 0 && size > 0)
                messageWithGradient.append(gradients.get(y).getFormatedHex("<>"));
            for (int i = 0; i < size; i++) {
                messageWithGradient.append(text.charAt(0));
                text = text.substring(1);
            }
        }
        messageWithGradient.append(text + gradients.get(gradients.size() - 1).getFormatedHex("<"));
        return messageWithGradient.toString();
    }

    @Deprecated
    public static String translateAlternateColorCodes(String text) {
        return translate(text);
    }

    public static String colorize(String text) {
        if (text == null)
            return null;
        return translate(text);
    }

    public static String simpleFlaten(String text) {
        return text.replace("&", colorReplacerPlaceholder).replace(colorCodePrefix, colorHexReplacerPlaceholder);
    }

    public static String flaten(String text) {
        return deColorize(text, true).replace("&", colorReplacerPlaceholder).replace(colorCodePrefix, colorHexReplacerPlaceholder);
    }

    public static String deColorize(String text) {
        return deColorize(text, true);
    }

    public static String deColorize(String text, boolean colorizeBeforeDe) {
        if (text == null)
            return null;
        if (colorizeBeforeDe)
            text = CMIChatColor.translate(text);
        text = text.replace("§", "&");

        if (text.contains("&x")) {
            Matcher match = hexDeColorNamePattern.matcher(text);
            while (match.find()) {
                String reg = match.group(3).replace("&", "");
                CMIChatColor custom = CUSTOM_BY_HEX.get(reg.toLowerCase());
                if (custom != null) {
                    text = text.replace(match.group(), colorCodePrefix + custom.getName().toLowerCase().replace("_", "") + colorCodeSuffix);
                } else {
                    text = text.replace(match.group(), colorCodePrefix + reg + colorCodeSuffix);
                }
            }
        }

//	Bukkit.getConsoleSender().sendMessage(text);
//
//	if (text.contains(colorCodePrefix)) {
//	    String t = text;
//
//	    Matcher match = post2GradientPattern.matcher(t);
//	    StringBuilder endTest = new StringBuilder();
//
//	    Pattern pat = Pattern.compile("((\\{#)([0-9A-Fa-f]{6}|[0-9A-Fa-f]{3})(\\})|(\\{#)([a-zA-Z_]{3,})(\\}))(.)");
//
//	    while (match.find()) {
//		String fullMatch = match.group();
//
//		CMIChatColor prevColor = null;
//		String prevColorS = null;
//		String prevChar = "";
//		double prevDist = Double.MAX_VALUE;
//		double prevDeltaDist = Double.MAX_VALUE;
//
//		Matcher subMatch = pat.matcher(fullMatch);
//
//		StringBuilder subEndTest = new StringBuilder();
//
//		while (subMatch.find()) {
//
//		    String color = subMatch.group(1);
//		    CMIChatColor h1 = getColor(color);
//		    String group8 = subMatch.group(8);
//
//		    if (prevColorS != null && prevColor != null) {
//
//			java.awt.Color c1 = null;
//			try {
//			    c1 = new java.awt.Color(
//				Integer.valueOf(h1.getHex().substring(0, 2), 16),
//				Integer.valueOf(h1.getHex().substring(2, 4), 16),
//				Integer.valueOf(h1.getHex().substring(4, 6), 16));
//			} catch (Throwable e) {
//			    break;
//			}
//
//			java.awt.Color c2 = null;
//			try {
//			    c2 = new java.awt.Color(
//				Integer.valueOf(prevColor.getHex().substring(0, 2), 16),
//				Integer.valueOf(prevColor.getHex().substring(2, 4), 16),
//				Integer.valueOf(prevColor.getHex().substring(4, 6), 16));
//			} catch (Throwable e) {
//			    break;
//			}
//
//			int red1 = c1.getRed();
//			int red2 = c2.getRed();
//			int rmean = (red1 + red2) >> 1;
//			int r = red1 - red2;
//			int g = c1.getGreen() - c2.getGreen();
//			int b = c1.getBlue() - c2.getBlue();
//			double dist = Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
//
//
//			double cdist = prevDist - dist;
//			cdist = cdist < 0 ? -cdist : cdist;
//
//			if (prevDist == 0) {
//
//			    subEndTest.append(prevChar);
//			} else if (dist == 0) {
//			    String converted = prevColorS.substring(0, prevColorS.length() - 1) + "<>}";
//			    subEndTest.append(prevChar + converted);
//			} else if (prevDist == Double.MAX_VALUE) {
//			    String converted = prevColorS.substring(0, prevColorS.length() - 1) + ">}";
//			    subEndTest.append(converted + prevChar);
//			} else {
//			    if (prevDist - dist <= 3 && prevDist - dist >= -3) {
//				subEndTest.append(prevChar);
//			    } else {
//				subEndTest.append(prevColorS + prevChar);
//			    }
//			}
//			prevDeltaDist = prevDist - dist;
//			prevDist = dist;
//		    }
//		    prevColor = h1;
//		    prevColorS = color;
//		    prevChar = group8;
//		}
//		if (prevColorS != null) {
//		    if (prevDeltaDist <= 3 && prevDeltaDist >= -3)
//			subEndTest.append(prevChar + prevColorS.substring(0, prevColorS.length() - 1) + "<}");
//		    else
//			subEndTest.append(prevColorS.substring(0, prevColorS.length() - 1) + "}" + prevChar);
//		}
//
//		Bukkit.getConsoleSender().sendMessage("Into:" + fullMatch + "  " + subEndTest.toString());
//
//		t = t.replace(fullMatch, subEndTest.toString());
//
//	    }
//	    if (!endTest.toString().isEmpty())
//		CMIDebug.d("endString: " + endTest);
//	}

//	if (text.contains(colorCodePrefix)) {
//	    String t = text;
//	    Matcher match = postGradientPattern.matcher(t);
//	    double prevDist = Double.MAX_VALUE;
//	    StringBuilder endTest = new StringBuilder();
//	    String prevG9 = "";
//	    while (match.find()) {
//		String g1 = match.group(1);
//
//		if (!t.startsWith(g1)) {
//
//		    endTest.append(t.split(escape(g1), 2)[0]);
//		    String oneC = "";
//		    if (!prevG9.isEmpty()) {
//			CMIDebug.d("S----------------------");
//			oneC = String.valueOf(t.split(escape(g1), 2)[1].charAt(0));
//			t = t.substring(1);
//		    }
//
////		    if (!prevG9.isEmpty()) {
////			String colorHex = g1.substring(0, g1.length() - 1) + ">}";
////			t = colorHex + oneC + t.split(escape(g1), 2)[1];
////		    } else
//			t = g1 + oneC + t.split(escape(g1), 2)[1];
//		    match = postGradientPattern.matcher(t);
////		    prevDist = Double.MAX_VALUE;
//		    prevG9 = "";
////		    prevDist = Double.MAX_VALUE;
//		    continue;
//		}
//
//		String g9 = match.group(9);
//		String g8 = match.group(8);
//		CMIChatColor h1 = getColor(g1);
//		CMIChatColor h2 = getColor(g9);
//
//		if (h1.getHex() == null || h2.getHex() == null)
//		    continue;
//
//		java.awt.Color c1 = null;
//		try {
//		    c1 = new java.awt.Color(
//			Integer.valueOf(h1.getHex().substring(0, 2), 16),
//			Integer.valueOf(h1.getHex().substring(2, 4), 16),
//			Integer.valueOf(h1.getHex().substring(4, 6), 16));
//		} catch (Throwable e) {
//		    break;
//		}
//
//		java.awt.Color c2 = null;
//		try {
//		    c2 = new java.awt.Color(
//			Integer.valueOf(h2.getHex().substring(0, 2), 16),
//			Integer.valueOf(h2.getHex().substring(2, 4), 16),
//			Integer.valueOf(h2.getHex().substring(4, 6), 16));
//		} catch (Throwable e) {
//		    break;
//		}
//
//		int red1 = c1.getRed();
//		int red2 = c2.getRed();
//		int rmean = (red1 + red2) >> 1;
//		int r = red1 - red2;
//		int g = c1.getGreen() - c2.getGreen();
//		int b = c1.getBlue() - c2.getBlue();
//		double dist = Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
//
//		String[] split = t.split(escape(g1) + match.group(8).replace("|", "\\|"), 2);
//		t = split[1];
//
//		endTest.append(split[0]);
//		if (prevDist == Double.MAX_VALUE) {
//		    String converted = g1.substring(0, g1.length() - 1) + ">}";
//		    endTest.append(converted + match.group(8));
//		} else {
//		    if (prevDist - dist <= 3) {
//			endTest.append(match.group(8));
//		    } else {
//			endTest.append(g1 + match.group(8));
//		    }
//		}
//		prevG9 = g9;
//
//		prevDist = dist;
//
//		match = postGradientPattern.matcher(t);
//
//		CMIDebug.d("Distance ", g8, g1, g9, (int) (dist * 100) / 100D, prevDist - dist <= 2 ? "OK" : "FAIL");
//	    }
//
//	    if (!prevG9.isEmpty()) {
//		prevG9 = prevG9.substring(0, prevG9.length() - 1) + "<}";
//	    }
//	    endTest.append(prevG9);
//	    endTest.append(t);
//	    if (!endTest.toString().isEmpty())
//		CMIDebug.d("endString: " + endTest);
//	}
//
        return text;
    }

    public static List<String> deColorize(List<String> lore) {
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, deColorize(lore.get(i)));
        }
        return lore;
    }

    public static String stripColor(String text) {
        if (text == null)
            return null;
        text = CMIChatColor.translate(text);
        return ChatColor.stripColor(text);
    }

    public static String stripHexColor(String message) {

        message = translate(message);

        Matcher match = hexColorRegexPattern.matcher(message);

        while (match.find()) {
            String string = match.group();
            message = message.replace(string, "");
        }

        if (message.contains("&x") || message.contains("§x")) {
            match = hexDeColorNamePattern.matcher(message);
            while (match.find()) {
                String string = match.group();
                message = message.replace(string, "");
            }
        }

        return message;
    }

    public static String getLastColors(String text) {
        if (text == null)
            return null;

        text = deColorize(text);
        Matcher match = hexColorRegexPatternLast.matcher(text);
        if (match.find()) {
            String colorByHex = match.group(0);
            if (text.endsWith(colorByHex))
                return colorByHex;
            String[] split = text.split(escape(colorByHex), 2);
            if (split == null)
                return colorByHex;
            String last = getLastColors(split[1]);
            return last == null || last.isEmpty() ? colorByHex : last;

        }

        match = hexColorNamePatternLast.matcher(text);
        if (match.find()) {
            String colorByName = match.group();
            if (text.endsWith(colorByName))
                return colorByName;
            String[] split = text.split(escape(colorByName), 2);
            if (split == null)
                return colorByName;
            String last = getLastColors(split[1]);
            return last == null || last.isEmpty() ? colorByName : last;
        }

        return ChatColor.getLastColors(translate(text));
    }

    public String getColorCode() {
        if (hexCode != null)
            return colorCodePrefix + hexCode + colorCodeSuffix;
        return "&" + c;
    }

    public String getBukkitColorCode() {
        if (hexCode != null)
            return translate(colorCodePrefix + hexCode + colorCodeSuffix);
        return "§" + c;
    }

    @Override
    public String toString() {
        return getBukkitColorCode();
    }

    public char getChar() {
        return c;
    }

    public void setChar(char c) {
        this.c = c;
    }

    public boolean isColor() {
        return color;
    }

    public boolean isFormat() {
        return !color && !isReset;
    }

    public boolean isReset() {
        return isReset;
    }

    public ChatColor getColor() {
        return ChatColor.getByChar(this.getChar());
    }

    public static Set<CMIChatColor> getFormats(String text) {
        text = text.replace("§", "&");
        Set<CMIChatColor> formats = new HashSet<CMIChatColor>();
        Matcher match = formatPattern.matcher(text);
        while (match.find()) {
            String string = match.group();
            CMIChatColor format = CMIChatColor.getFormat(string);
            if (format != null && format.isFormat()) {
                formats.add(format);
            }
        }
        return formats;
    }

    public static CMIChatColor getFormat(String text) {

        if (text == null)
            return null;

        String or = deColorize(text);
        text = text.replace("§", "&");

        if (text.length() > 1) {
            String formated = text.toLowerCase().replace("_", "");
            CMIChatColor got = BY_NAME.get(formated);
            if (got != null)
                return got;
            got = CUSTOM_BY_NAME.get(formated);
            if (got != null)
                return got;
        }

        if (or.length() > 1 && String.valueOf(or.charAt(or.length() - 2)).equalsIgnoreCase("&")) {
            text = text.substring(text.length() - 1, text.length());
            for (Entry<Character, CMIChatColor> one : BY_CHAR.entrySet()) {
                if (String.valueOf(one.getKey()).equalsIgnoreCase(text)) {
                    return one.getValue().isFormat() ? one.getValue() : null;
                }
            }
        }

        return null;
    }

    public static CMIChatColor getColor(String text) {

        if (text == null)
            return null;

        String or = deColorize(text);

        if (or.contains(colorCodePrefix)) {
            Matcher match = hexColorRegexPatternLast.matcher(or);
            if (match.find()) {

                return new CMIChatColor(match.group(2));
            }
            match = hexColorNamePatternLast.matcher(or);
            if (match.find()) {
                return CMIChatColor.getByCustomName(match.group(2));
            }
        }

        text = deColorize(text).replace("&", "");

        if (text.length() > 1) {
            String formated = text.toLowerCase().replace("_", "");
            CMIChatColor got = BY_NAME.get(formated);
            if (got != null)
                return got;

            got = CUSTOM_BY_NAME.get(formated);
            if (got != null)
                return got;
        }

        if (or.length() > 1 && String.valueOf(or.charAt(or.length() - 2)).equalsIgnoreCase("&")) {
            text = text.substring(text.length() - 1, text.length());

            for (Entry<Character, CMIChatColor> one : BY_CHAR.entrySet()) {
                if (String.valueOf(one.getKey()).equalsIgnoreCase(text)) {
                    return one.getValue();
                }
            }
        }

        CMIChatColor hexColor = new CMIChatColor(text);
        if (hexColor.getHex() != null)
            return hexColor;

        return null;
    }

    public static CMIChatColor getRandomColor() {
        List<CMIChatColor> ls = new ArrayList<CMIChatColor>();
        for (Entry<String, CMIChatColor> one : BY_NAME.entrySet()) {
            if (!one.getValue().isColor())
                continue;
            ls.add(one.getValue());
        }
        Collections.shuffle(ls);
        return ls.get(0);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Color getRGBColor() {
        if (blueChannel < 0)
            return null;
        return Color.fromRGB(redChannel, greenChannel, blueChannel);
    }

    public java.awt.Color getJavaColor() {
        if (blueChannel < 0)
            return null;
        return new java.awt.Color(redChannel, greenChannel, blueChannel);
    }

    public String getHex() {
        return hexCode;
    }

    public String getFormatedHex() {
        return getFormatedHex(null);
    }

    public String getFormatedHex(String subSuffix) {
        return colorCodePrefix + hexCode + (subSuffix == null ? "" : subSuffix) + colorCodeSuffix;
    }

    public String getName() {
        return name;
    }

    public String getCleanName() {
        return name == null ? getHex() : name.replace("_", "");
    }

    public static CMIChatColor getByCustomName(String name) {

        if (name.startsWith(colorCodePrefix))
            name = name.substring(colorCodePrefix.length());
        if (name.endsWith(colorCodeSuffix))
            name = name.substring(0, name.length() - colorCodeSuffix.length());

        if (name.equalsIgnoreCase("random")) {
            List<CMIChatColor> valuesList = new ArrayList<CMIChatColor>(CUSTOM_BY_NAME.values());
            int randomIndex = new Random().nextInt(valuesList.size());
            return valuesList.get(randomIndex);
        }

        return CUSTOM_BY_NAME.get(name.toLowerCase().replace("_", ""));
    }

    public static CMIChatColor getByHex(String hex) {
        if (hex.startsWith(colorCodePrefix))
            hex = hex.substring(colorCodePrefix.length());
        if (hex.endsWith(colorCodeSuffix))
            hex = hex.substring(0, hex.length() - colorCodeSuffix.length());
        return CUSTOM_BY_HEX.get(hex.toLowerCase().replace("_", ""));
    }

    public static Map<String, CMIChatColor> getByName() {
        return BY_NAME;
    }

    public static Map<String, CMIChatColor> getByCustomName() {
        return CUSTOM_BY_NAME;
    }

    public static String getHexFromCoord(int x, int y) {
        x = x < 0 ? 0 : x > 255 ? 255 : x;
        y = y < 0 ? 0 : y > 255 ? 255 : y;

        int blue = (int) (255 - y * 255 * (1.0 + Math.sin(6.3 * x)) / 2);
        int green = (int) (255 - y * 255 * (1.0 + Math.cos(6.3 * x)) / 2);
        int red = (int) (255 - y * 255 * (1.0 - Math.sin(6.3 * x)) / 2);
        StringBuilder hex = new StringBuilder().append(Integer.toHexString((red << 16) + (green << 8) + blue & 0xffffff));
        while (hex.length() < 6) {
            hex.append("0" + hex);
        }
        return "#" + hex.toString();
    }

    public static String getHexRedGreenByPercent(int percentage, int parts) {
        float percent = (percentage * 33F / 100F) / 100F;

        java.awt.Color color = java.awt.Color.getHSBColor(percent, 1, 1);
        StringBuilder hex = new StringBuilder().append(Integer.toHexString((color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue() & 0xffffff));
        while (hex.length() < 6) {
            hex.append("0" + hex);
        }
        return "#" + hex.toString();
    }

    public int getRed() {
        return redChannel;
    }

    public int getGreen() {
        return greenChannel;
    }

    public int getBlue() {
        return blueChannel;
    }

    public static CMIChatColor getClosest(String hex) {
        if (hex.startsWith("#"))
            hex = hex.substring(1);

        CMIChatColor closest = CUSTOM_BY_RGB.get(hex);
        if (closest != null)
            return closest;

        java.awt.Color c2 = null;
        try {
            c2 = new java.awt.Color(
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16));
        } catch (Throwable e) {
            return null;
        }
        double distance = Double.MAX_VALUE;
        for (Entry<String, CMIChatColor> one : CUSTOM_BY_HEX.entrySet()) {

            java.awt.Color c1 = new java.awt.Color(
                Integer.valueOf(one.getValue().hexCode.substring(0, 2), 16),
                Integer.valueOf(one.getValue().hexCode.substring(2, 4), 16),
                Integer.valueOf(one.getValue().hexCode.substring(4, 6), 16));

            int red1 = c1.getRed();
            int red2 = c2.getRed();
            int rmean = (red1 + red2) >> 1;
            int r = red1 - red2;
            int g = c1.getGreen() - c2.getGreen();
            int b = c1.getBlue() - c2.getBlue();
            double dist = Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
            if (dist < distance) {
                closest = one.getValue();
                distance = dist;
            }
        }

        if (closest != null) {
            CUSTOM_BY_RGB.put(hex, closest);
            return closest;
        }
        CUSTOM_BY_RGB.put(hex, null);

        return null;
    }

    public static String getClosestVanilla(String hex) {
        try {
            if (hex.startsWith("#"))
                hex = hex.substring(1);

            CMIChatColor old = CUSTOM_BY_HEX.get(hex);
            if (old != null && old.getChar() != 10) {
                return "&" + old.getChar();
            }

            java.awt.Color c2 = null;
            try {
                c2 = new java.awt.Color(
                    Integer.valueOf(hex.substring(0, 2), 16),
                    Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16));
            } catch (Throwable e) {
                return null;
            }

            double distance = Double.MAX_VALUE;
            CMIChatColor closest = null;
            for (Entry<Character, CMIChatColor> one : BY_CHAR.entrySet()) {

                if (!one.getValue().isColor())
                    continue;
                java.awt.Color c1 = new java.awt.Color(
                    one.getValue().getRed(),
                    one.getValue().getGreen(),
                    one.getValue().getBlue());

                int red1 = c1.getRed();
                int red2 = c2.getRed();
                int rmean = (red1 + red2) >> 1;
                int r = red1 - red2;
                int g = c1.getGreen() - c2.getGreen();
                int b = c1.getBlue() - c2.getBlue();
                double dist = Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
                if (dist < distance) {
                    closest = one.getValue();
                    distance = dist;
                }
            }

            if (closest != null) {
                if (old != null) {
                    old.setChar(closest.getChar());
                } else {
                    CUSTOM_BY_HEX.put(hex, closest);
                }
                return "&" + closest.getChar();
            }

        } catch (Throwable ee) {
            ee.printStackTrace();
        }
        return null;
    }

    public CMIChatColor mixColors(CMIChatColor color, double percent) {
        return mixColors(this, color, percent);
    }

    public static CMIChatColor mixColors(CMIChatColor color1, CMIChatColor color2, double percent) {
        percent = percent / 100D;
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color2.getRed() * percent + color1.getRed() * inverse_percent);
        int greenPart = (int) (color2.getGreen() * percent + color1.getGreen() * inverse_percent);
        int bluePart = (int) (color2.getBlue() * percent + color1.getBlue() * inverse_percent);
        String hexCode = String.format("#%02x%02x%02x", redPart, greenPart, bluePart);
        return new CMIChatColor(hexCode);
    }

//    public static CMIChatColor getClosest(Long rgb) {
//
//	Entry<Long, customColors> low = CUSTOM_BY_RGB.floorEntry(rgb);
//	Entry<Long, customColors> high = CUSTOM_BY_RGB.ceilingEntry(rgb);
//	customColors res = null;
//	if (low != null && high != null) {
//	    res = Math.abs(rgb - low.getKey()) < Math.abs(rgb - high.getKey()) ? low.getValue() : high.getValue();
//	} else if (low != null || high != null) {
//	    res = low != null ? low.getValue() : high == null ? null : high.getValue();
//	}
//	if (res == null) {
//	    return null;
//	}
//	return new CMIChatColor(res.name(), res.hex);
//    }

}
