package net.Zrips.CMILib.RawMessages;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Container.CMIKyori;
import net.Zrips.CMILib.Items.CMIItemStack;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Schedulers.CMITask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

public class PaperRawMessage implements RawMessageBackend {

    private Component message = Component.empty();
    private Component pending = Component.empty();

    private HoverEvent<?> hover = null;
    private ClickEvent<?> clickEvent = null;
    private String insertion = null;

    private final List<String> plainText = new ArrayList<>();

    private boolean dontBreakLine = false;
    private boolean freezeFormat = false;
    private Component frozenFormat = Component.empty();

    @Override
    public RawMessageBackend RawMessage() {
        return this;
    }

    @Override
    public RawMessageBackend RawMessage(String text) {
        this.addText(text);
        return this;
    }

    private Component parse(String text) {
        if (text == null || text.isEmpty())
            return Component.empty();

        return CMIKyori.deserializeLegacy(CMIChatColor.translate(text));
    }

    private Component applyEvents(Component component) {
        if (hover != null)
            component = component.hoverEvent(hover);

        if (clickEvent != null)
            component = component.clickEvent(clickEvent);

        if (insertion != null)
            component = component.insertion(insertion);

        return component;
    }

    private PaperRawMessage commit() {
        if (!pending.equals(Component.empty())) {
            Component component = pending;

            if (freezeFormat && frozenFormat != null)
                component = component.style(component.style().merge(frozenFormat.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));

            message = message.append(applyEvents(component));
            pending = Component.empty();
        }

        hover = null;
        clickEvent = null;
        insertion = null;

        return this;
    }

    private static Component getLastComponent(Component component) {
        List<Component> children = component.children();

        if (children.isEmpty()) {
            return component;
        }

        return getLastComponent(children.get(children.size() - 1));
    }

    @Override
    public void clear() {
        message = Component.empty();
        pending = Component.empty();
        hover = null;
        clickEvent = null;
        insertion = null;
        plainText.clear();
    }

    @Override
    public PaperRawMessage addText(String text) {
        return addText(text, true);
    }

    @Deprecated
    @Override
    public RawMessageBackend add(String text, String hoverText, RawMessageCommand rmc) {
        addText(text);
        addHover(hoverText);
        addCommand(rmc.getCommand());
        return this;
    }

    @Override
    public PaperRawMessage addText(String text, boolean colorize) {
        if (text == null || text.isEmpty())
            return this;

        if (!pending.equals(Component.empty()))
            commit();

        pending = colorize ? parse(text) : CMIKyori.deserializeLegacy(text);
        plainText.add(colorize ? CMIChatColor.translate(text) : text);

        return this;
    }

    @Deprecated
    @Override
    public PaperRawMessage add(String text, String hoverText, String command, String suggestion, String url, String insertion) {
        addText(text);
        addHover(hoverText);
        addCommand(command);
        addSuggestion(suggestion);
        addUrl(url);
        addInsertion(insertion);
        return null;
    }

    @Deprecated
    @Override
    public PaperRawMessage addItem(String text, ItemStack item, String command, String suggestion, String insertion) {
        addText(text);
        addItem(item);
        addCommand(command);
        addSuggestion(suggestion);
        addInsertion(insertion);
        return this;
    }

    @Override
    public PaperRawMessage addText(LC locale) {
        return addText(locale.getLocale(), true);
    }

    @Override
    public PaperRawMessage addText(LC locale, boolean colorize) {
        return addText(locale.getLocale(), colorize);
    }

    @Deprecated
    @Override
    public PaperRawMessage add(String text, List<String> hoverText) {
        addText(text);
        addHoverText(hoverText);
        return this;
    }

    @Override
    public PaperRawMessage addHoverText(List<String> hoverText) {
        String hover = hoverText != null ? String.join("\n", hoverText) : null;
        addHover(hover);
        return this;
    }

    @Override
    public PaperRawMessage addHoverText(String hover) {
        addHover(hover);
        return this;
    }

    @Override
    public RawMessageBackend addHover(LC lc) {
        addHover(lc.getLocale());
        return this;
    }

    @Override
    public PaperRawMessage addHover(String text) {
        hover = parse(text).asHoverEvent();
        return this;
    }

    @Override
    public PaperRawMessage addHover(List<String> lines) {
        TextComponent temp = Component.empty();

        if (lines == null)
            return this;

        for (int i = 0; i < lines.size(); i++) {
            if (i > 0)
                temp = temp.append(Component.newline());
            temp = temp.append(parse(lines.get(i)));
        }

        hover = temp.asHoverEvent();

        return this;
    }

    @Override
    public RawMessageBackend addCommand(RawMessageCommand rmc) {
        addCommand(rmc.getCommand());
        return this;
    }

    @Override
    public PaperRawMessage addCommand(String command) {
        if (command == null || command.isEmpty()) {
            return this;
        }

        if (!command.startsWith("/")) {
            command = "/" + command;
        }

        clickEvent = ClickEvent.runCommand(command);
        return this;
    }

    @Override
    public PaperRawMessage addSuggestion(String suggestion) {
        if (suggestion == null || suggestion.isEmpty()) {
            return this;
        }

        clickEvent = ClickEvent.suggestCommand(suggestion);
        return this;
    }

    @Override
    public PaperRawMessage addInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    @Override
    public PaperRawMessage addUrl(String text, String url, String hoverText) {
        addText(text);
        addUrl(url);
        addHover(hoverText);
        return this;
    }

    @Override
    public PaperRawMessage addUrl(String text, String url) {
        addText(text);
        addUrl(url);
        return this;
    }

    @Override
    public PaperRawMessage addUrl(String url) {
        if (url == null || url.isEmpty()) {
            return this;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "https://" + url;

        clickEvent = ClickEvent.openUrl(toUrl(url));

        return this;
    }

    @Override
    public PaperRawMessage addItem(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return this;
        }

        hover = item.asHoverEvent();

        return this;
    }

    @Override
    public PaperRawMessage append(RawMessage other, boolean sameLine) {
        commit();

        if (!sameLine) {
            message = message.append(Component.newline());
        }

        PaperRawMessage paper = (PaperRawMessage) other.backend;
        paper.commit();
        message = message.append(paper.message);

        return this;
    }

    @Override
    public PaperRawMessage build() {
        return commit();
    }

    @Override
    public PaperRawMessage combineClean() {
        commit();
        return this;
    }

    @Override
    public List<String> softCombine() {
        commit();
        List<String> result = new ArrayList<>();
        result.add(getCombinedClean());
        return result;
    }

    @Override
    public int getFinalLength() {
        commit();
        return getCombinedClean().length();
    }

    @Override
    public PaperRawMessage show(Player player) {
        return show(player, true);
    }

    @Override
    public PaperRawMessage show(Player player, boolean softCombined) {
        if (player != null) {
            commit();
            player.sendMessage(message);
        }

        return this;
    }

    @Override
    public PaperRawMessage show(Set<Player> players) {
        return show(players, true);
    }

    @Override
    public PaperRawMessage show(Set<Player> players, boolean softCombined) {
        if (players == null || players.isEmpty()) {
            return this;
        }

        commit();

        for (Player player : players) {
            if (player != null) {
                player.sendMessage(message);
            }
        }

        return this;
    }

    @Override
    public PaperRawMessage show(CommandSender sender) {
        if (sender == null) {
            return this;
        }

        commit();

        if (sender instanceof Player) {
            ((Player) sender).sendMessage(message);
        } else {
            sender.sendMessage(getCombinedClean());
        }

        return this;
    }

    @Override
    public PaperRawMessage show(CMICommandSender sender) {
        if (sender == null) {
            return this;
        }

        return show(sender.getSender());
    }

    public PaperRawMessage addComponent(Component component) {
        if (component != null) {
            this.message = this.message.append(component);
        }
        return this;
    }

    private static String toUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "";
        }

        String normalized = url.trim();

        if (!normalized.matches("(?i)^[a-z][a-z0-9+.-]*://.*")) {
            normalized = "https://" + normalized;
        }

        try {
            URI uri = new URI(normalized);

            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("Invalid URL: " + url);
            }

            String scheme = uri.getScheme().toLowerCase(java.util.Locale.ROOT);

            if (!scheme.equals("http") && !scheme.equals("https")) {
                throw new IllegalArgumentException("Unsupported URL scheme: " + scheme);
            }

            return uri.toASCIIString();
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("Invalid URL: " + url, exception);
        }
    }

    public String getRaw() {
        commit();
        return GsonComponentSerializer.gson().serialize(message);
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
        commit();

        if (message.equals(Component.empty())) {
            frozenFormat = Component.empty();
        } else {
            Component last = getLastComponent(message);
            frozenFormat = Component.empty().style(last.style());
        }

        freezeFormat = true;
    }

    public void unFreezeFormat() {
        freezeFormat = false;
        frozenFormat = Component.empty();
    }

    public PaperRawMessage add(String text) {
        return addText(text);
    }

    public PaperRawMessage add(String text, String hoverText) {
        return add(text, hoverText, null, null, null);
    }

    public PaperRawMessage add(String text, String hoverText, String command, String suggestion, String url) {

        addText(text);

        if (hoverText != null && !hoverText.isEmpty()) {
            addHover(hoverText);
        }

        if (command != null && !command.isEmpty()) {
            addCommand(command);
        } else if (suggestion != null && !suggestion.isEmpty()) {
            addSuggestion(suggestion);
        } else if (url != null && !url.isEmpty()) {
            addUrl(url);
        }

        return this;
    }

    public PaperRawMessage add(String text, String hoverText, String command) {
        return add(text, hoverText, command, null, null);
    }

    public PaperRawMessage add(String text, String hoverText, String command, String suggestion) {
        return add(text, hoverText, command, suggestion, null);
    }

    public PaperRawMessage add(String text, String hoverText, String command, String suggestion, String url, ItemStack item) {
        add(text, hoverText, command, suggestion, url);

        if (item != null) {
            addItem(item);
        }

        return this;
    }

    public PaperRawMessage addText(String text, String hoverText) {
        addText(text);

        if (hoverText != null) {
            addHover(hoverText);
        }

        return this;
    }

    public PaperRawMessage addText(String text, String hoverText, String command) {
        addText(text);

        if (hoverText != null) {
            addHover(hoverText);
        }

        if (command != null) {
            addCommand(command);
        }

        return this;
    }

    public PaperRawMessage addText(String text, String hoverText, String command, String suggestion, String url) {
        return add(text, hoverText, command, suggestion, url);
    }

    public PaperRawMessage addItem(CMIItemStack item) {
        return item == null ? this : addItem(item.getItemStack());
    }

    public PaperRawMessage append(RawMessage other) {
        return append(other, true);
    }

    public PaperRawMessage append(PaperRawMessage other) {
        return append(other, true);
    }

    public PaperRawMessage append(PaperRawMessage other, boolean sameLine) {
        if (other == null) {
            return this;
        }

        commit();
        other.commit();

        if (!sameLine) {
            message = message.append(Component.newline());
        }

        message = message.append(other.message);
        plainText.add(other.getCombinedClean());

        return this;
    }

    public String getCombinedClean() {
        commit();

        StringBuilder result = new StringBuilder();

        for (String text : plainText) {
            result.append(text);
        }

        return result.toString();
    }

    public PaperRawMessage combine() {
        return commit();
    }

    public String getCombined() {
        return getRaw();
    }

    public int getFinalLenght() {
        return getFinalLength();
    }

    public PaperRawMessage show(CMICommandSender sender, boolean softCombined) {
        return sender == null ? this : show(sender.getSender());
    }

    @Override
    public String getTextOnly() {
        commit();

        return net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(message);
    }

    @Override
    public void setCombined(String combined) {
        message = Component.empty();
        pending = Component.empty();
        plainText.clear();

        if (combined == null || combined.isEmpty()) {
            return;
        }

        message = parse(combined);
        plainText.add(CMIChatColor.translate(combined));

        hover = Component.empty().asHoverEvent();
        clickEvent = null;
        insertion = null;
    }

    @Override
    public String getShortRaw() {
        commit();

        return LegacyComponentSerializer.legacyAmpersand().serialize(message);
    }

    @Override
    public LinkedHashMap<RawMessagePartType, String> getTemporyCache() {
        LinkedHashMap<RawMessagePartType, String> cache = new LinkedHashMap<>();

        commit();

        String text = LegacyComponentSerializer.legacyAmpersand().serialize(message);

        if (!text.isEmpty()) {
            cache.put(RawMessagePartType.Text, text);
        }

        return cache;
    }

    private final Map<UUID, CMITask> actionBarTasks = new ConcurrentHashMap<>();

    @Override
    public RawMessageBackend showActionBar(Set<Player> players, int keepFor) {
        commit();

        if (players == null || players.isEmpty())
            return this;

        for (Player player : players) {
            if (player == null || !player.isOnline())
                continue;

            UUID uuid = player.getUniqueId();

            CMITask previous = actionBarTasks.remove(uuid);
            if (previous != null)
                previous.cancel();

            player.sendActionBar(message);

            if (keepFor <= 0)
                continue;

            long endTime = System.currentTimeMillis() + keepFor * 1000L;
            long interval = 40L;

            CMITask task = CMIScheduler.runTimerAsync(CMILib.getInstance(), () -> {
                if (!player.isOnline() || System.currentTimeMillis() >= endTime) {
                    CMITask current = actionBarTasks.get(uuid);
                    if (current != null) {
                        actionBarTasks.remove(uuid);
                        current.cancel();
                    }
                    return;
                }

                player.sendActionBar(message);
            }, interval, interval);

            actionBarTasks.put(uuid, task);
        }

        return this;
    }

    @Override
    public RawMessageBackend showActionBar(Player player, int keepFor) {
        return showActionBar(Collections.singleton(player), keepFor);
    }

    @Override
    public RawMessageBackend showTitle(Player player, int fadeIn, int keep, int fadeOut) {

        if (player == null)
            return this;

        return showTitle(Collections.singleton(player), fadeIn, keep, fadeOut);
    }

    @Override
    public RawMessageBackend showTitle(Set<Player> players, int fadeIn, int keep, int fadeOut) {
        if (players == null || players.isEmpty())
            return this;

        commit();

        String plainText = PlainTextComponentSerializer.plainText().serialize(message);
        int newline = plainText.indexOf('\n');

        Component title = Component.empty();
        Component subtitle = Component.empty();

        if (newline >= 0) {
            String titleText = plainText.substring(0, newline);
            String subtitleText = plainText.substring(newline + 1);

            if (!titleText.isEmpty())
                title = LegacyComponentSerializer.legacyAmpersand().deserialize(titleText);

            if (!subtitleText.isEmpty())
                subtitle = LegacyComponentSerializer.legacyAmpersand().deserialize(subtitleText);
        } else if (!plainText.isEmpty()) {
            title = message;
        }

        Title.Times times = Times.times(Duration.ofMillis(fadeIn * 50L), Duration.ofMillis(keep * 50L), Duration.ofMillis(fadeOut * 50L));

        Title adventureTitle = Title.title(title, subtitle, times);

        for (Player player : players) {
            if (player != null && player.isOnline())
                player.showTitle(adventureTitle);
        }

        return this;
    }
}