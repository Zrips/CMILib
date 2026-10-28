package net.Zrips.CMILib.RawMessages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Version.Version;

public class RawMessage {

    final RawMessageBackend backend;

    public static final String newLine = "\u00A5n";
    private static final Pattern QUOTED_NUMBER_WITH_D = Pattern.compile("(?<!text:)\"(-?\\d+(?:\\.\\d+)?)(?:[dD])?\"(?=[,\\}])");

    static Pattern patern = Pattern.compile("(?<!&)((http|https|ftp|ftps)\\:\\/\\/)?[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,3}(\\/\\S*)?([^\\s|^\\)]+)");

    public static String fixQuotedAmountNumbers(String input) {
        if (!input.contains("d\",") && !input.contains("d\"}"))
            return input;
        return QUOTED_NUMBER_WITH_D.matcher(input).replaceAll("$1");
    }

    static String extractTag(String message, String tag) {
        int start = message.indexOf("<" + tag + ">");
        if (start == -1) {
            return "";
        }

        start += tag.length() + 2;

        int end = message.indexOf("</" + tag + ">", start);
        if (end == -1) {
            return "";
        }

        return message.substring(start, end);
    }

    public static RawMessageBackend createBackend() {
        return supportsAdventureComponents() ? new PaperRawMessage() : new LegacyRawMessage();
    }

    static boolean supportsAdventureComponents() {
        return !CMILibConfig.legacyRawMessage && Version.isPaperBranch() && Version.isCurrentEqualOrHigher(Version.v1_16_0);
    }

    public static RawMessage translateRawMessage(CommandSender sender, String textLine) {
        return translateRawMessage(sender, textLine, false);
    }

    public static RawMessage translateRawMessage(CommandSender sender, String textLine, boolean book) {
        return RawMessageParser.translateRawMessage(sender, textLine, book);
    }

    public static RawMessage translateTextOnlyRawMessage(String textLine) {
        return RawMessageParser.translateTextOnlyRawMessage(textLine);
    }

    public RawMessage() {
        this.backend = createBackend();
    }

    public RawMessage(String text) {
        this.backend = createBackend();
        this.addText(text);
    }

    public void clear() {
        backend.clear();
    }

    // ---------------------------------------------------------
    // Deprecated compatibility API
    // ---------------------------------------------------------

    @Deprecated
    public RawMessage addItem(String text, ItemStack item, String command, String suggestion, String insertion) {
        addText(text);
        addCommand(command);
        addSuggestion(suggestion);
        addInsertion(insertion);
        addItem(item);
        return this;
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, String command, String suggestion, String url) {
        add(text, hoverText, command, suggestion, url, null);
        return this;
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, String command, String suggestion, String url, String insertion) {
        addText(text);
        addHover(hoverText);
        addCommand(command);
        addSuggestion(suggestion);
        addUrl(url);
        addInsertion(insertion);
        return this;
    }

    @Deprecated
    public RawMessage addUrl(String text, String url) {
        return addUrl(text, url, null);
    }

    @Deprecated
    public RawMessage addUrl(String text, String url, String hoverText) {
        addText(text);
        addHover(hoverText);
        addUrl(url);
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
            hover = String.join("\n", hoverText);
        return add(text, hover.isEmpty() ? null : hover, null, null, null);
    }

    @Deprecated
    public RawMessage add(String text, String hoverText, RawMessageCommand command) {
        return add(text, hoverText, command.getCommand(), null, null);
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

    @Deprecated
    public RawMessage combineClean() {
        backend.combineClean();
        return this;
    }

    @Deprecated
    public List<String> softCombine() {
        return backend.softCombine();
    }

    @Deprecated
    public int getFinalLength() {
        return backend.getFinalLength();
    }

    @Deprecated
    public int getFinalLenght() {
        return getFinalLength();
    }

    @Deprecated
    public RawMessage show(Player player, boolean softCombined) {
        backend.show(player, softCombined);
        return this;
    }

    @Deprecated
    public RawMessage show(Set<Player> players, boolean softCombined) {
        backend.show(players, softCombined);
        return this;
    }

    @Deprecated
    public RawMessage addRM(RawMessage other) {
        return addRM(other, false);
    }

    @Deprecated
    public RawMessage addRM(RawMessage other, boolean sameLine) {
        backend.append(other, sameLine);
        return this;
    }

    // ---------------------------------------------------------
    // Modern public API
    // ---------------------------------------------------------

    public RawMessage addText(String text) {
        backend.addText(text);
        return this;
    }

    public RawMessage addText(String text, boolean colorize) {
        backend.addText(text, colorize);
        return this;
    }

    public RawMessage addText(LC locale) {
        backend.addText(locale);
        return this;
    }

    public RawMessage addText(LC locale, boolean colorize) {
        backend.addText(locale, colorize);
        return this;
    }

    public RawMessage addHover(String hover) {
        backend.addHover(hover);
        return this;
    }

    public RawMessage addHover(List<String> hover) {
        backend.addHover(hover);
        return this;
    }

    public RawMessage addHover(LC locale) {
        return addHover(locale.getLocale());
    }

    public RawMessage addCommand(String command) {
        backend.addCommand(command);
        return this;
    }

    public RawMessage addCommand(RawMessageCommand command) {
        if (command != null)
            addCommand(command.getCommand());
        return this;
    }

    public RawMessage addSuggestion(String suggestion) {
        backend.addSuggestion(suggestion);
        return this;
    }

    public RawMessage addInsertion(String insertion) {
        backend.addInsertion(insertion);
        return this;
    }

    public RawMessage addUrl(String url) {
        backend.addUrl(url);
        return this;
    }

    public RawMessage addItem(ItemStack item) {
        backend.addItem(item);
        return this;
    }

    public RawMessage append(RawMessage other) {
        return append(other, false);
    }

    public RawMessage append(RawMessage other, boolean sameLine) {
        backend.append(other, sameLine);
        return this;
    }

    public RawMessage build() {
        backend.build();
        return this;
    }

    public RawMessage show(Player player) {
        backend.show(player);
        return this;
    }

    public RawMessage show(Set<Player> players) {
        backend.show(players);
        return this;
    }

    public RawMessage show(CommandSender sender) {
        backend.show(sender);
        return this;
    }

    public RawMessage show(CMICommandSender sender) {
        backend.show(sender);
        return this;
    }

    public String getTextOnly() {
        return backend.getTextOnly();
    }

    public String getRaw() {
        return backend.getRaw();
    }

    public void setCombined(String combined) {
        backend.setCombined(combined);
    }

    public String getShortRaw() {
        return backend.getShortRaw();
    }

    public boolean isDontBreakLine() {
        return backend.isDontBreakLine();
    }

    public void setDontBreakLine(boolean dontBreakLine) {
        backend.setDontBreakLine(dontBreakLine);
    }

    public boolean isFormatFrozen() {
        return backend.isFormatFrozen();
    }

    public void freezeFormat() {
        backend.freezeFormat();
    }

    public void unFreezeFormat() {
        backend.unFreezeFormat();
    }

    public LinkedHashMap<RawMessagePartType, String> getTemporyCache() {
        return backend.getTemporyCache();
    }

    public RawMessage showActionBar(Set<Player> players, int keepForSec) {
        backend.showActionBar(players, keepForSec);
        return this;
    }

    public RawMessage showActionBar(Player player, int keepForSec) {
        backend.showActionBar(player, keepForSec);
        return this;
    }

    public RawMessage showTitle(Player player, int fadeIn, int keep, int fadeOut) {
        backend.showTitle(player, fadeIn, keep, fadeOut);
        return this;
    }

    public RawMessage showTitle(Set<Player> players, int fadeIn, int keep, int fadeOut) {
        backend.showTitle(players, fadeIn, keep, fadeOut);
        return this;
    }
}
