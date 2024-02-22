package net.Zrips.CMILib.Placeholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Chat.ChatFilterRule;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Messages.CMIMessages;

public class Placeholder {

    private CMILib plugin;
    Pattern placeholderKeepPatern = Pattern.compile("(\\%\\%)([^\\\"^\\%^ ]+)(\\%\\%)(\\B)");
    Pattern placeholderOthersKeepPatern = Pattern.compile("(?i)(\\%parseother_)((\\{)([^\\\"^\\%^ ^\\{^\\}]+)(\\}))([^(\\}\\%).]+)(\\}\\%)");
    Pattern placeholderPatern = Pattern.compile("(%)([^\"^%^ ]+)(%)");
    Pattern placeholderPatern2 = Pattern.compile("(\\{)([^\"^%^ ^{^}]+)(\\})");

    private static ChatFilterRule numericalRule = new ChatFilterRule().setPattern("(\\$)(\\d)");

    public Placeholder(CMILib plugin) {
        this.plugin = plugin;
    }

    Random random = new Random(System.nanoTime());

    public enum CMIPlaceHolders {
        ;

        static LinkedHashMap<String, CMIPlaceHolders> byNameStatic = new LinkedHashMap<String, CMIPlaceHolders>();
        static LinkedHashMap<String, LinkedHashSet<CMIPlaceHolders>> byNameComplex = new LinkedHashMap<String, LinkedHashSet<CMIPlaceHolders>>();

        static {
            for (CMIPlaceHolders one : CMIPlaceHolders.values()) {
                String fullName = one.toString();
                if (!one.isComplex()) {
                    byNameStatic.put(fullName.toLowerCase(), one);
                    continue;
                }
                String[] split = fullName.split("_");
                String first = split[0] + "_" + split[1];
                LinkedHashSet<CMIPlaceHolders> old = byNameComplex.getOrDefault(first, new LinkedHashSet<CMIPlaceHolders>());
                old.add(one);
                byNameComplex.put(first, old);
            }
        }

        private String[] vars;
        private List<Integer> groups = new ArrayList<Integer>();
        private ChatFilterRule rule = null;
        private boolean hidden = false;
        private String desc = null;

        CMIPlaceHolders() {
        }

        CMIPlaceHolders(boolean hideen) {
            this(null, hideen);
        }

        CMIPlaceHolders(String desc, String... vars) {
            this(desc, false, vars);
        }

        CMIPlaceHolders(String desc, boolean hidden, String... vars) {
            this.desc = desc;
            this.vars = vars;
            this.hidden = hidden;

            try {
                Matcher matcher = numericalRule.getMatcher(this.toString());
                if (matcher != null) {
                    rule = new ChatFilterRule();
                    List<String> ls = new ArrayList<>();
                    ls.add("(%)" + this.toString().replaceAll("\\$\\d", "([^\"^%]*)") + "(%)");
                    ls.add("(\\{)" + this.toString().replaceAll("\\$\\d", "([^\"^%]*)") + "(\\})");
                    rule.setPattern(ls);
                    while (matcher.find()) {
                        try {
                            int id = Integer.parseInt(matcher.group(2));

                            groups.add(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        public static CMIPlaceHolders getByName(String name) {

            if (name.startsWith("%") || name.startsWith("{"))
                name = name.replace("%", "").replace("{", "").replace("}", "");

            String original = name;

            CMIPlaceHolders got = byNameStatic.get(name);
            if (got != null)
                return got;
            String[] split = name.split("_");

            if (split.length < 3) {
                return null;
            }

            String prefix = split[0] + "_" + split[1];

            Set<CMIPlaceHolders> main = byNameComplex.get(prefix);

            if (main == null) {
                return null;
            }

            for (CMIPlaceHolders mainOne : main) {
                if (!mainOne.getComplexRegexMatchers(original).isEmpty()) {
                    return mainOne;
                }
            }
            return null;
        }

        public static CMIPlaceHolders getByNameExact(String name) {
            return getByName(name);
        }

        public String getFull() {
            if (this.isComplex()) {
                String name = this.name();
                int i = 0;
                for (String one : this.name().split("_")) {
                    if (!one.startsWith("$"))
                        continue;
                    if (vars.length >= i - 1)
                        name = name.replace(one, "[" + vars[i] + "]");
                    i++;
                }

                return "%" + name + "%";
            }
            return "%" + this.name() + "%";
        }

        public String getMVdW() {
            if (this.isComplex()) {
                String name = this.name();
                int i = 0;
                for (String one : this.name().split("_")) {
                    if (!one.startsWith("$"))
                        continue;
                    if (vars.length >= i - 1)
                        name = name.replace(one, "*");
                    i++;
                }

                return name;
            }
            return this.name();
        }

        public List<String> getComplexRegexMatchers(String text) {
            List<String> lsInLs = new ArrayList<String>();
            if (!this.isComplex())
                return lsInLs;

            if (!text.startsWith("%") && !text.endsWith("%"))
                text = "%" + text + "%";

            Matcher matcher = this.getRule().getMatcher(text);
            if (matcher == null)
                return lsInLs;
            while (matcher.find()) {
                lsInLs.add(matcher.group());
            }
            return lsInLs;
        }

        public List<String> getComplexValues(String text) {

            List<String> lsInLs = new ArrayList<String>();
            if (!this.isComplex() || text == null)
                return lsInLs;

            if (!text.startsWith("%") && !text.endsWith("%"))
                text = "%" + text + "%";

            Matcher matcher = this.getRule().getMatcher(text);
            if (matcher == null)
                return lsInLs;
            while (matcher.find()) {
                try {
                    for (Integer oneG : groups) {
                        lsInLs.add(matcher.group(oneG + 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            return lsInLs;
        }

        public boolean isComplex() {
            return rule != null;
        }

        public ChatFilterRule getRule() {
            return rule;
        }

        public void setRule(ChatFilterRule rule) {
            this.rule = rule;
        }

        public boolean isHidden() {
            return hidden;
        }

        public String getDescription() {
            return desc;
        }
    }

    public List<String> updatePlaceHolders(Player player, List<String> messages) {
        List<String> ms = new ArrayList<String>(messages);
        for (int i = 0, l = messages.size(); i < l; ++i) {
            ms.set(i, updatePlaceHolders(player, messages.get(i)));
        }
        return ms;
    }

    public enum CMIPlaceholderType {
        CMI, PAPI, MVdW;
    }

    private static final String checkItem = "%checkitem_";

    private void reportIssue() {
        CMIMessages.consoleMessage("&cPlaceholder got blocked due to security concerns (" + checkItem + "...%)");
    }

    public CMIPlaceholderType getPlaceHolderType(Player player, String placeholder) {
        if (placeholder == null)
            return null;
        if (placeholder.contains("%")) {
            if (!placeholder.equals(translateOwnPlaceHolder(player, placeholder)))
                return CMIPlaceholderType.CMI;
        }
        if (placeholder.contains("{")) {
            if (!placeholder.equals(translateOwnPlaceHolder(player, placeholder)))
                return CMIPlaceholderType.CMI;
        }
        if (plugin.isPlaceholderAPIEnabled()) {
            try {
                if (placeholder.contains("%"))
                    if (!placeholder.toLowerCase().contains(checkItem) || !CMILibConfig.ExploitPatcherCheckItem) {
                        if (!placeholder.equals(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, placeholder)))
                            return CMIPlaceholderType.PAPI;
                    } else {
                        reportIssue();
                    }
            } catch (Throwable e) {

            }
        }
        return null;
    }

    public String updatePlaceHolders(UUID uuid, String message) {

        if (message == null)
            return null;
        if (message.contains("{")) {
            message = translateOwnPlaceHolder(uuid, message);
        }
        if (message.contains("%"))
            message = translateOwnPlaceHolder(uuid, message);

        if (plugin.isPlaceholderAPIEnabled()) {
            try {
                if (message.contains("%")) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (!message.toLowerCase().contains(checkItem) || !CMILibConfig.ExploitPatcherCheckItem) {
                        message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
                    } else {
                        reportIssue();
                    }
                }
            } catch (Exception e) {

            }
        }

        return message;
    }

    public String updatePlaceHolders(String message) {
        UUID uuid = null;
        return updatePlaceHolders(uuid, message);
    }

    public String updatePlaceHolders(Player player, String message) {

        if (message == null)
            return null;

        HashMap<String, String> temp = new HashMap<String, String>();

        if (message.contains("{")) {
            // Duplicated code
            Matcher match = placeholderOthersKeepPatern.matcher(message);
            while (match.find()) {
                try {
                    if (!message.contains("%"))
                        break;
                    String group = match.group();
                    int id = (new Random()).nextInt(Integer.MAX_VALUE);
                    String with = "|" + id + "|";
                    temp.put(with, group);
                    message = message.replaceFirst(Matcher.quoteReplacement(group), Matcher.quoteReplacement(with));
                } catch (Throwable e) {
                }
            }

            message = translateOwnPlaceHolder(player, message);

            if (message.contains("%")) {
                message = translateOwnPlaceHolder(player, message);
            }

            for (Entry<String, String> one : temp.entrySet()) {
                message = message.replace(one.getKey(), one.getValue());
            }
        } else {
            if (message.contains("%")) {
                message = translateOwnPlaceHolder(player, message);
            }
        }

        if (plugin.isPlaceholderAPIEnabled()) {
            try {
                if (message.contains("%")) {
                    if (!message.toLowerCase().contains(checkItem) || !CMILibConfig.ExploitPatcherCheckItem) {
                        message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
                    } else {
                        reportIssue();
                    }
                }
            } catch (Throwable e) {
            }
        }

        return message;
    }

    public boolean containsPlaceHolder(String msg) {

        Matcher match = placeholderPatern.matcher(msg);
        while (match.find()) {
            return true;
        }

        match = placeholderPatern2.matcher(msg);
        while (match.find()) {
            return true;
        }

        return false;
    }

    public String translateOwnPlaceHolder(Player player, String message) {
        return translateOwnPlaceHolder(player == null ? null : player.getUniqueId(), message);
    }

    private String matchInception(UUID uuid, String message, int depth) {

        if (message.contains("{")) {
            Matcher match = placeholderPatern2.matcher(message);
            int i = 0;
            try {
                while (match.find()) {
                    i++;
                    if (i > 10)
                        break;
                    String cmd = match.group(2);
                    if (!message.contains("{"))
                        break;

                    CMIPlaceHolders place = CMIPlaceHolders.getByNameExact(cmd);
                    if (place == null) {
                        if (plugin.isPlaceholderAPIEnabled()) {
                            try {
                                Player player = Bukkit.getPlayer(uuid);
                                String group = match.group();

                                if (!group.startsWith(CMIChatColor.colorCodePrefix)) {
                                    if (!cmd.toLowerCase().contains(checkItem) || !CMILibConfig.ExploitPatcherCheckItem) {
                                        String with = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, "%" + cmd + "%");
                                        if (with == null)
                                            with = "";
                                        if (!with.equalsIgnoreCase("%" + cmd + "%")) {
                                            message = message.replaceFirst(Pattern.quote(group), Matcher.quoteReplacement(with));
                                        }
                                    } else {
                                        reportIssue();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                        continue;
                    }

                    String group = match.group();
                    String with = this.getValue(uuid, place, group);
                    if (with == null)
                        with = "";
                    message = message.replaceFirst(Matcher.quoteReplacement(group), Matcher.quoteReplacement(with));
                }
            } catch (Throwable e) {
            }
        }

        return message;
    }

    public String translateOwnPlaceHolder(UUID uuid, String message) {
        if (message == null)
            return null;

        if (CMILib.getInstance().isCmiPresent()) {
            try {
                message = com.Zrips.CMI.CMI.getInstance().getPlaceholderAPIManager().translateOwnPlaceHolder(uuid, message);
            } catch (Throwable e) {
            }
        }

        if (message.contains("{")) {

            // Duplicated code
            HashMap<String, String> temp = new HashMap<String, String>();
            Matcher match = placeholderOthersKeepPatern.matcher(message);
            while (match.find()) {
                try {
                    if (!message.contains("%"))
                        break;
                    String group = match.group();
                    int id = (new Random()).nextInt(Integer.MAX_VALUE);
                    String with = "|" + id + "|";
                    temp.put(with, group);
                    message = message.replaceFirst(Matcher.quoteReplacement(group), Matcher.quoteReplacement(with));
                } catch (Throwable e) {
                }
            }

            message = matchInception(uuid, message, 0);

            for (Entry<String, String> one : temp.entrySet()) {
                message = message.replace(one.getKey(), one.getValue());
            }
        }

        if (message.contains("%")) {

            HashMap<String, String> temp = new HashMap<String, String>();
            Matcher matchKeep = placeholderKeepPatern.matcher(message);
            while (matchKeep.find()) {
                try {
                    if (!message.contains("%"))
                        break;
                    String group = matchKeep.group();

                    int id = (new Random()).nextInt(Integer.MAX_VALUE);

                    String with = "|" + id + "|";

                    temp.put(with, group.substring(1, group.length() - 1));

                    message = message.replaceFirst(Matcher.quoteReplacement(group), Matcher.quoteReplacement(with));
                } catch (Throwable e) {
                }
            }

            Matcher match = placeholderPatern.matcher(message);
            while (match.find()) {
                try {
                    String cmd = match.group(2);
                    if (!message.contains("%"))
                        break;
                    CMIPlaceHolders place = CMIPlaceHolders.getByNameExact(cmd);
                    if (place == null)
                        continue;

                    String group = match.group();

                    String with = this.getValue(uuid, place, group);

                    if (with == null)
                        with = "";

                    message = message.replaceFirst(Pattern.quote(group), Matcher.quoteReplacement(with));
                } catch (Throwable e) {
//		    e.printStackTrace();
                }
            }

            for (Entry<String, String> one : temp.entrySet()) {
                message = message.replace(one.getKey(), one.getValue());
            }
        }
        return message;
    }

    private HashMap<String, String> randomCache = new HashMap<String, String>();

    public String getValue(Player player, CMIPlaceHolders placeHolder) {
        return getValue(player, placeHolder, null);
    }

    public String getValue(Player player, CMIPlaceHolders placeHolder, String value) {
        return getValue(player != null ? player.getUniqueId() : null, placeHolder, value);
    }

    public String getValue(UUID uuid, CMIPlaceHolders placeHolder, String value) {
        return null;
    }

    private void informFailed(String value) {
        CMIMessages.consoleMessage("&cInccorrect placeholder format for " + value);
    }

    private String variable(Boolean state) {
        return state ? CMIMessages.getMsg(LC.info_variables_True) : CMIMessages.getMsg(LC.info_variables_False);
    }
}
