package net.Zrips.CMILib.RawMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Items.CMIItemStack;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Shadow.ShadowCommand;
import net.Zrips.CMILib.Shadow.ShadowCommandType;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.commands.CommandsHandler;

public class RawMessage {

    List<String> parts = new ArrayList<String>();
    List<String> onlyText = new ArrayList<String>();

    private LinkedHashMap<RawMessagePartType, String> temp = new LinkedHashMap<RawMessagePartType, String>();

    RawMessageFragment fragment = new RawMessageFragment();
    RawMessageFragment hoverFragment = new RawMessageFragment();

    private RawMessageFragment frozenFragment = new RawMessageFragment();
    private boolean freezeFormat = false;

    private String combined = "";
    String combinedClean = "";

    private boolean dontBreakLine = false;

    public void clear() {
        parts = new ArrayList<String>();
        onlyText = new ArrayList<String>();
        combined = "";
        combinedClean = "";
    }

    private String textIntoJson(String text, boolean hover) {
        if (text.isEmpty()) {
            return "";
        }
        if (text.equalsIgnoreCase(" ")) {
            return " ";
        }
        text = CMIChatColor.deColorize(text);

        Matcher match = CMIChatColor.fullPattern.matcher(text);
        String matcher = null;

        List<RawMessageFragment> fragments = new ArrayList<RawMessageFragment>();

        RawMessageFragment f = hover ? hoverFragment : fragment;

//	String lastText = "";
        while (match.find()) {
            matcher = match.group();

            String[] split = text.split(matcher.replace("#", "\\#").replace("{", "\\{").replace("}", "\\}"), 2);
            text = "";
            for (int i = 1; i < split.length; i++) {
                text += split[i];
            }

            if (split[0] != null && !split[0].isEmpty()) {
                String t = split[0];

                // Disabled do to issue with missing spaces for text like "test &2 test"
//		String t2 = lastText;
//		lastText = t;
//		if (t2.endsWith(" ") && t.startsWith(" ")) {
//		    t = t.substring(1);
//		}

                f.setText(t);
                fragments.add(f);
                f = new RawMessageFragment(f);
            }

            if (matcher.startsWith(CMIChatColor.colorFontPrefix)) {
                f.setFont(matcher);
                continue;
            }

            CMIChatColor color = CMIChatColor.getColor(matcher);
            if (color == null)
                continue;

            if (color.isColor()) {
                f.setLastColor(color);
            } else {
                f.addFormat(color);
            }
        }

        if (!text.isEmpty()) {

//	    if (lastText.endsWith(" ") && text.startsWith(" "))
//		text = text.substring(1);

            RawMessageFragment t = new RawMessageFragment(f);

            t.setText(text);
            fragments.add(t);
        }

        if (hover)
            hoverFragment = f;
        else
            fragment = f;

        StringBuilder finalText = new StringBuilder();

        for (RawMessageFragment one : fragments) {
            if (!finalText.toString().isEmpty())
                finalText.append("},{");
            StringBuilder options = new StringBuilder();
            for (CMIChatColor format : one.getFormats()) {
                if (!options.toString().isEmpty())
                    options.append(",");
                if (format.equals(CMIChatColor.UNDERLINE))
                    options.append("\"underlined\":true");
                else if (format.equals(CMIChatColor.BOLD))
                    options.append("\"bold\":true");
                else if (format.equals(CMIChatColor.ITALIC))
                    options.append("\"italic\":true");
                else if (format.equals(CMIChatColor.STRIKETHROUGH))
                    options.append("\"strikethrough\":true");
                else if (format.equals(CMIChatColor.OBFUSCATED))
                    options.append("\"obfuscated\":true");
            }
            if (!options.toString().isEmpty()) {
                finalText.append(options.toString());
                finalText.append(",");
            }

            if (one.getFont() != null) {
                finalText.append("\"font\":\"" + one.getFont() + "\",");

            }

            if (one.getLastColor() != null) {
                if (one.getLastColor().getHex() != null)
                    finalText.append("\"color\":\"#" + one.getLastColor().getHex() + "\",");
                else if (one.getLastColor().getName() != null) {
                    finalText.append("\"color\":\"" + one.getLastColor().getName().toLowerCase() + "\",");
                }
            }

            String t = one.getText();

            // Old server support, we need to add colors and formats to the text directly
            if (Version.isCurrentLower(Version.v1_16_R1)) {
                StringBuilder oldColors = new StringBuilder();
                if (one.getLastColor() != null && one.getLastColor().getName() != null) {
                    oldColors.append(one.getLastColor().getColorCode());
                }
                for (CMIChatColor format : one.getFormats()) {
                    if (format.equals(CMIChatColor.UNDERLINE))
                        oldColors.append("&n");
                    else if (format.equals(CMIChatColor.BOLD))
                        oldColors.append("&l");
                    else if (format.equals(CMIChatColor.ITALIC))
                        oldColors.append("&o");
                    else if (format.equals(CMIChatColor.STRIKETHROUGH))
                        oldColors.append("&m");
                    else if (format.equals(CMIChatColor.OBFUSCATED))
                        oldColors.append("&k");
                }
                t = oldColors.toString() + t;
            }

            finalText.append("\"text\":\"" + escape(t, hover ? false : this.isDontBreakLine()) + "\"");
        }

        if (finalText.toString().isEmpty())
            return "";
        return "{" + finalText.toString() + "}";
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, String command, String suggestion, String url) {
        add(text, hoverText, command, suggestion, url, null);
        return this;
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, String command, String suggestion, String url, String insertion) {
        this.addText(text);
        this.addHover(hoverText);
        this.addCommand(command);
        this.addSuggestion(suggestion);
        this.addUrl(url);
        this.addInsertion(insertion);
        return this;
    }

    @Deprecated
    public RawMessage addUrl(String text, String url) {
        addUrl(text, url, null);
        return this;
    }

    @Deprecated
    public RawMessage addUrl(String text, String url, String hoverText) {
        this.addText(text);
        this.addHover(hoverText);
        this.addUrl(url);
        return this;
    }

    @Deprecated
    public RawMessage add(String text) {
        return add(text, null, null, null, null);
    }

    @Deprecated
    public RawMessage add(String text, String hoverText) {
        return add(text, hoverText, null, null, null);
    }

    @Deprecated
    public RawMessage add(String text, List<String> hoverText) {

        String hover = "";
        if (hoverText != null)
            for (String one : hoverText) {
                if (!hover.isEmpty())
                    hover += "\n";
                hover += one;
            }
        return add(text, hover.isEmpty() ? null : hover, null, null, null);
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, RawMessageCommand rmc) {
        return add(text, hoverText, rmc.getCommand(), null, null);
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, String command) {
        return add(text, hoverText, command, null, null);
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, String command, String suggestion) {
        return add(text, hoverText, command, suggestion, null);
    }

    @Deprecated
    public RawMessage addHoverText(List<String> hoverText) {
        return addHover(hoverText);
    }

    @Deprecated
    public RawMessage addHoverText(String hover) {
        return addHover(hover);
    }

    public RawMessage addRM(RawMessage rm) {
        return addRM(rm, false);
    }

    public RawMessage addRM(RawMessage rm, boolean sameLine) {
        rm.build();

        if (!sameLine && !parts.isEmpty()) {
            addText("\n");
        }
        build();

        parts.addAll(rm.parts);
        onlyText.addAll(rm.onlyText);
        return this;
    }

    public RawMessage addItem(String text, ItemStack item, String command, String suggestion, String insertion) {
        this.addText(text);
        this.addCommand(command);
        this.addSuggestion(suggestion);
        this.addInsertion(insertion);
        this.addItem(item);
        return this;
    }

    public RawMessage addText(LC lc) {
        return addText(lc.getLocale());
    }

    public RawMessage addText(String text) {
        if (text == null || text.isEmpty())
            return this;
        if (temp.containsKey(RawMessagePartType.Text))
            build();

//	if (this.isDontBreakLine()) {
        onlyText.add(CMIChatColor.translate(text));

//	text = escape(text, this.isDontBreakLine());
//	}
        text = textIntoJson(text, false);

        String f = "";
        if (text.isEmpty())
            f = "\"text\":\"\"";
        else if (text.equalsIgnoreCase(" "))
            f = "\"text\":\" \"";
        else
            f = "\"text\":\"\",\"extra\":[" + CMIChatColor.translate(text).replace(CMIChatColor.colorHexReplacerPlaceholder, CMIChatColor.colorCodePrefix).replace(CMIChatColor.colorReplacerPlaceholder,
                "&")
                + "]";
        temp.put(RawMessagePartType.Text, f);
        return this;
    }

    public RawMessage addHover(List<String> hoverText) {
        StringBuilder hover = new StringBuilder();
        if (hoverText != null) {
            for (String one : hoverText) {
                if (!hover.toString().isEmpty())
                    hover.append("\n");
                hover.append(one);
            }
        }
        return addHover(hover.toString());
    }

    public RawMessage addHover(LC lc) {
        return addHover(lc.getLocale());
    }

    public RawMessage addHover(String hover) {
        hoverFragment = new RawMessageFragment();
        if (hover == null || hover.isEmpty())
            return this;

        hover = textIntoJson(hover, true);
//	hover = escape(hover, false);
        String f = "";
        if (hover.isEmpty())
            f = "\"text\":\"\"";
        else if (hover.equalsIgnoreCase(" "))
            f = "\"text\":\" \"";
        else
            f = "\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[" + CMIChatColor.translate(hover).replace(CMIChatColor.colorReplacerPlaceholder, "&") + "]}}";
        temp.put(RawMessagePartType.HoverText, f);
        return this;
    }

    public RawMessage addCommand(RawMessageCommand rmc) {
        if (rmc == null || rmc.getCommand().isEmpty())
            return this;
        addCommand(rmc.getCommand());
        return this;
    }

    public RawMessage addCommand(String command) {
        if (command == null || command.isEmpty())
            return this;
        if (!command.startsWith("/"))
            command = "/" + command;
        command = escape(command, true);
        String f = "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + CMIChatColor.deColorize(command).replace(CMIChatColor.colorReplacerPlaceholder, "&") + "\"}";
        temp.put(RawMessagePartType.ClickCommand, f);
        return this;
    }

    public RawMessage addSuggestion(String suggestion) {
        if (suggestion == null || suggestion.isEmpty())
            return this;
        suggestion = escape(suggestion, true);
        String f = "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + CMIChatColor.deColorize(suggestion, false).replace(CMIChatColor.colorReplacerPlaceholder, "&") + "\"}";
        temp.put(RawMessagePartType.ClickSuggestion, f);
        return this;
    }

    public RawMessage addInsertion(String insertion) {
        if (insertion == null || insertion.isEmpty())
            return this;
        insertion = escape(insertion, true);
        String f = "\"insertion\":\"" + CMIChatColor.deColorize(insertion).replace(CMIChatColor.colorReplacerPlaceholder, "&") + "\"";
        temp.put(RawMessagePartType.ClickInsertion, f);
        return this;
    }

    public RawMessage addItem(ItemStack item) {
        if (item == null)
            return this;

        item = item.clone();

        String res = CMINBT.toJson(item);

        // Cleaning up useless information. Italic not included due to some weird behavior which defaults to italic look if not specifically set to not be one
        res = res.replaceAll("\\\"bold\\\":false,|\\\"underlined\\\":false,|\\\"strikethrough\\\":false,|\\\"obfuscated\\\":false,", "");

        try {
//            if (res.length() > 32766) {
//		if (CMIMaterial.get(item.getType()).isShulkerBox()) {
//		    Inventory inv = CMILib.getInstance().getNMS().getShulkerInv(item);
//		    ItemStack[] contents = inv.getContents();
//		    int c = 0;
//		    for (int i = 0; i < contents.length; i++) {
//			ItemStack one = contents[i];
//			if (one == null)
//			    continue;
//			c++;
//			if (c <= 5)
//			    continue;
//			ItemMeta meta = one.getItemMeta();
//			meta.setDisplayName(CMIChatColor.stripColor(meta.getDisplayName()));
//			meta.setLore(new ArrayList<String>());
//			one.setItemMeta(meta);
//			contents[i] = one;
//		    }
//		    CMI.getInstance().getShulkerBoxManager().setShulkerInv(item, contents);
//		}
//
//                if (res.length() > 32766) {
//                    res = truncate(res);
//                }
//                res = CMINBT.toJson(item);
//            }

            // Lets not even try to add item if its near a limit which still gives 200 bytes to play around
            if (res.length() > 32566) {
                return this;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        String f = "\"hoverEvent\":{\"action\":\"show_item\",\"value\":\"" + escape(res, true) + "\"}";

        temp.put(RawMessagePartType.HoverItem, f);
        return this;
    }

    public RawMessage addUrl(String url) {
        if (url == null || url.isEmpty())
            return this;

        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://"))
            url = "http://" + url;

        url = url.replace("\"", "");
        url = url.replace("\'", "");

        url = Matcher.quoteReplacement(url);

        String f = "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + CMIChatColor.deColorize(url).replace(CMIChatColor.colorReplacerPlaceholder, "&") + "\"}";

        temp.put(RawMessagePartType.ClickLink, f);
        return this;
    }

    public RawMessage build() {
        if (temp.isEmpty())
            return this;

        if (!temp.containsKey(RawMessagePartType.Text))
            return this;
        String part = "";
        for (RawMessagePartType one : RawMessagePartType.values()) {
            String t = temp.get(one);
            if (t == null)
                continue;
            if (!part.isEmpty())
                part += ",";
            part += t;
        }
        part = "{" + part + "}";
        temp.clear();
        parts.add(part);
        return this;
    }

    private static String escape(String s, boolean escapeNewLn) {

        if (s == null)
            return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        if (escapeNewLn)
            return sb.toString().replace(nl, "\\\\n");
        return sb.toString().replace(nl, "\\n");
    }

    private static final String nl = "\u00A5n";

    private static void escape(String s, StringBuffer sb) {
        s = s.replace("\n", nl);
        s = s.replace("\\n", nl);
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
                sb.append("\\/");
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

//	for (int i = 0; i < s.length(); i++) {
//	    char ch = s.charAt(i);
//	    if (String.valueOf(ch).equals(nl)) {
//		if (escapeNewLn) {
//		    sb.append("\\n");
//		} else {
//		    sb.append("\n");
//		}
//	    } else
//		sb.append(ch);
//
//	}
    }

    public List<String> softCombine() {
        List<String> ls = new ArrayList<String>();
        String f = "";
        for (String part : parts) {
            if (f.isEmpty())
                f = "[\"\",";
            else {
                if (f.length() > 30000) {
                    ls.add(f + "]");
                    f = "[\"\"," + part;
                    continue;
                }
                f += ",";
            }
            f += part;
        }
        if (!f.isEmpty())
            f += "]";
        ls.add(f);
        return ls;
    }

    private RawMessage combine() {
        String f = "";
        for (String part : parts) {
            if (f.isEmpty())
                f = "[\"\",";
            else
                f += ",";
            f += part;
        }
        if (!f.isEmpty())
            f += "]";

        if (f.isEmpty())
            f = "{\"text\":\" \"}";

        combined = f;
        return this;
    }

    public RawMessage combineClean() {
        String f = "";
        for (String part : onlyText) {
            f += part.replace("\\\"", "\"");
        }
        combinedClean = f;
        return this;
    }

    public RawMessage show(Set<Player> player) {
        return show(player, true);
    }

    public RawMessage show(Set<Player> players, boolean softCombined) {
        if (players == null)
            return this;
        if (combined.isEmpty()) {
            this.build();
            combine();
        }

        if (players.isEmpty())
            return this;
        if (softCombined) {
            for (String one : softCombine()) {
                if (one.isEmpty())
                    continue;
                RawMessageManager.send(players, truncate(one));
            }
        } else {
            RawMessageManager.send(players, truncate(combined));
        }

        return this;
    }

    private String truncate(String value) {
        if (value.length() < 5000)
            return value;
        value = value.replace("\"bold\":false,", "");
        value = value.replace("\\\"bold\\\":false,", "");
        value = value.replace("\"italic\":false,", "");
        value = value.replace("\\\"italic\\\":false,", "");
        value = value.replace("\"obfuscated\":false,", "");
        value = value.replace("\\\"obfuscated\\\":false,", "");
        value = value.replace("\"strikethrough\":false,", "");
        value = value.replace("\\\"strikethrough\\\":false,", "");
        value = value.replace("\"underlined\":false,", "");
        value = value.replace("\\\"underlined\\\":false,", "");
        return value;
    }

    public RawMessage show(Player player) {
        return show(player, true);
    }

    public RawMessage show(Player player, boolean softCombined) {
        HashSet<Player> set = new HashSet<Player>();
        set.add(player);
        return show(set, softCombined);
    }

    public int getFinalLength() {
        String f = "";
        for (String part : parts) {
            if (f.isEmpty())
                f = "[\"\",";
            else
                f += ",";
            f += part;
        }
        for (String part : temp.values()) {
            if (f.isEmpty())
                f = "[\"\",";
            else
                f += ",";
            f += part;
        }
        if (!f.isEmpty())
            f += "]";
        return f.length();
    }

    public RawMessage show(CMICommandSender sender) {
        if (sender.isPlayer())
            return show(sender.getPlayer(), true);
        CMIMessages.sendMessage(sender.getSender(), this.combineClean().combinedClean);
        return this;
    }

    public int getFinalLenght() {
        String f = "";
        for (String part : parts) {
            if (f.isEmpty())
                f = "[\"\",";
            else
                f += ",";
            f += part;
        }
        if (!f.isEmpty())
            f += "]";
        return f.length();
    }

    public RawMessage show(CommandSender sender) {
        if (combined.isEmpty()) {
            this.build();
            combine();
        }
        if (sender instanceof Player) {
            show((Player) sender);
        } else {
            CMIMessages.sendMessage(sender, this.combineClean().combinedClean);
        }
        return this;
    }

    public String getTextOnly() {
        return this.combineClean().combinedClean;
    }

    public String getRaw() {
        if (combined.isEmpty()) {
            build();
            combine();
        }

        return combined;
    }

    public void setCombined(String combined) {
        this.combined = combined;
    }

    public String getShortRaw() {
        build();
        String f = "";
        for (String part : parts) {
            if (!f.isEmpty())
                f += ",";
            f += part;
        }
        f = truncate(f);

        return f;
    }

    public boolean isDontBreakLine() {
        return dontBreakLine;
    }

    public void setDontBreakLine(boolean dontBreakLine) {
        this.dontBreakLine = dontBreakLine;
    }

    public boolean isFormatFrozen() {
        return freezeFormat;
    }

    public void freezeFormat() {
        frozenFragment = new RawMessageFragment(fragment);
        this.freezeFormat = true;
    }

    public void unFreezeFormat() {
        fragment = new RawMessageFragment(frozenFragment);
        this.freezeFormat = false;
    }

    static Pattern patern = Pattern.compile("((http|https|ftp|ftps)\\:\\/\\/)?[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,3}(\\/\\S*)?([^\\s|^\\)]+)");

    public static RawMessage translateRawMessage(CommandSender sender, String textLine) {
        return translateRawMessage(sender, textLine, false);
    }

    public static RawMessage translateRawMessage(CommandSender sender, String textLine, boolean book) {

        RawMessage rm = new RawMessage();

        textLine = textLine.replace("\n", "\\n");

        List<String> split = new ArrayList<String>();
        if (textLine.contains("<Next>"))
            split.addAll(Arrays.asList(textLine.split("<Next>")));
        else
            split.add(textLine);

        ArrayList<String> temp = new ArrayList<String>(split);
        split.clear();
        for (String one : temp) {
            if (one.split("<T>").length > 2) {
                for (String spone : one.split("<T>")) {
                    if (spone == null || spone.isEmpty())
                        continue;
                    split.add("<T>" + spone);
                }
            } else
                split.add(one);
        }

        for (String message : split) {
            if (message.contains("<T>")) {
                String text = message.replaceAll(".*\\<T>|\\</T>.*", "");

                Matcher match = patern.matcher(text);
                if (match.find()) {
                    String url = match.group();

                    String[] sp = text.split(url);
                    if (sp.length > 0) {
                        if (sp[0].endsWith("&")) {
                            rm.addText(CMIChatColor.translate(sp[0]) + url.substring(0, 1));
                            url = url.substring(1);
                        } else {
                            String t = CMIChatColor.translate(sp[0]);
                            if (!t.equals(url))
                                rm.addText(t);
                        }
                    }

                    rm.addText(url);
                    rm.addUrl(url);
                    if (sp.length > 1) {
                        rm.addText(sp[1]);
                    }
                } else {
                    rm.addText(text);
                }
            }

            if (message.contains("<H>")) {
                rm.addHover(message.replaceAll(".*\\<H>|\\</H>.*", ""));
            }

            if (message.contains("<ITEM>")) {
                String st = message.replaceAll(".*\\<ITEM>|\\</ITEM>.*", "");
                CMIItemStack stack = null;
                if (st.equalsIgnoreCase("%itemInHand%")) {
                    if (sender instanceof Player)
                        stack = new CMIItemStack(CMILib.getInstance().getReflectionManager().getItemInMainHand((Player) sender));
                } else if (st.equalsIgnoreCase("%itemInOffHand%")) {
                    if (sender instanceof Player)
                        stack = new CMIItemStack(CMILib.getInstance().getReflectionManager().getItemInOffHand((Player) sender));
                } else
                    stack = CMILib.getInstance().getItemManager().getItem(st);

                if (stack != null)
                    rm.addItem(stack.getItemStack());
            }

            String command = null;
            if (message.contains("<C>")) {
                command = message.replaceAll(".*\\<C>|\\</C>.*", "");
            }

            if (message.contains("<URL>")) {
                rm.addUrl(message.replaceAll(".*\\<URL>|\\</URL>.*", ""));
            }

            String consoleCommand = null;
            if (sender instanceof Player) {
                if (message.contains("<CC>")) {
                    Player player = (Player) sender;
                    consoleCommand = message.replaceAll(".*\\<CC>|\\</CC>.*", "");
                    String id = ShadowCommand.addShadowCmd(player, consoleCommand, false, ShadowCommandType.Console);
                    command = CommandsHandler.getLabel() + " shadowcmd " + id + " (" + consoleCommand + ")";
                    if (book)
                        command = consoleCommand;
                }
                if (message.contains("<CCI>")) {
                    Player player = (Player) sender;
                    consoleCommand = message.replaceAll(".*\\<CCI>|\\</CCI>.*", "");
                    String id = ShadowCommand.addShadowCmd(player, consoleCommand, true, ShadowCommandType.Console);
//		command = "cmi shadowcmd " + id;
                    command = CommandsHandler.getLabel() + " shadowcmd " + id + " (" + consoleCommand + ")";
                    if (book)
                        command = consoleCommand;
                }
            }

            if (message.contains("<SC>")) {
                rm.addSuggestion(message.replaceAll(".*\\<SC>|\\</SC>.*", ""));
            }

            if (message.contains("<SI>")) {
                rm.addInsertion(message.replaceAll(".*\\<SI>|\\</SI>.*", ""));
            }

            rm.addCommand(command);
        }

        return rm;
    }

    public LinkedHashMap<RawMessagePartType, String> getTemporyCache() {
        return temp;
    }
}
