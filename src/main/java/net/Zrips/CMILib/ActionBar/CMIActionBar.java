package net.Zrips.CMILib.ActionBar;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Container.CMINumber;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Schedulers.CMITask;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CMIActionBar {

    private static Class<?> nmsIChatBaseComponent;
    private static Class<?> packetType;

    private static Class<?> ChatMessageclz;
    private static Class<?> sub;
    private static Object[] consts;
    private static Constructor<?> constructor;

    static {
        init();
    }

    private static void init() {

        if (Version.isMojangMappings()) {
            try {
                constructor = Class.forName("net.minecraft.network.protocol.game.ClientboundSystemChatPacket").getConstructor(Class.forName("net.minecraft.network.chat.Component"),
                        boolean.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                packetType = Class.forName("net.minecraft.network.protocol.game.ClientboundSystemChatPacket");
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                packetType = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat");
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                ChatMessageclz = Class.forName("net.minecraft.network.chat.ChatMessageType");

                consts = ChatMessageclz.getEnumConstants();
                sub = consts[2].getClass();

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentHigher(Version.v1_7_R4)) {
            Version version = Version.getCurrent();
            try {
                packetType = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
                nmsIChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
                if (Version.isCurrentHigher(Version.v1_11_R1)) {
                    ChatMessageclz = Class.forName("net.minecraft.server." + version + ".ChatMessageType");
                    consts = ChatMessageclz.getEnumConstants();
                    sub = consts[2].getClass();
                }
            } catch (Throwable ex) {
                CMIMessages.consoleMessage(ex.toString());
            }
        }

        try {
            if (Version.isMojangMappings()) {

            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                constructor = packetType.getConstructor(nmsIChatBaseComponent, boolean.class);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                if (Version.isCurrentSubEqual(0))
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, int.class);
                else
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, boolean.class);
            } else if (Version.isCurrentHigher(Version.v1_15_R1)) {
                constructor = packetType.getConstructor(nmsIChatBaseComponent, sub, UUID.class);
            } else if (Version.isCurrentHigher(Version.v1_11_R1)) {
                constructor = packetType.getConstructor(nmsIChatBaseComponent, sub);
            } else if (Version.isCurrentHigher(Version.v1_7_R4)) {
                constructor = packetType.getConstructor(nmsIChatBaseComponent, byte.class);
            } else {
                constructor = packetType.getConstructor(nmsIChatBaseComponent, int.class);
            }
        } catch (Throwable ex) {
            CMIMessages.consoleMessage(ex.toString());
        }
    }

    public static void send(CommandSender receivingPacket, String msg) {
        if (receivingPacket instanceof Player)
            send(Arrays.asList((Player) receivingPacket), msg, 0);
        else
            receivingPacket.sendMessage(msg);
    }

    public static void send(CMICommandSender receivingPacket, String msg) {
        send(receivingPacket, msg, 0);
    }

    public static void send(CMICommandSender receivingPacket, String msg, int keepFor) {
        if (receivingPacket.isPlayer())
            send(receivingPacket.getPlayer(), msg, keepFor);
        else
            receivingPacket.sendMessage(msg);
    }

    static ConcurrentHashMap<UUID, repeatingActionBar> actionbarMap = new ConcurrentHashMap<UUID, repeatingActionBar>();

    public static void send(Player receivingPacket, String msg) {
        send(Arrays.asList(receivingPacket), msg, 0);
    }

    public static void send(Player receivingPacket, String msg, int keepFor) {
        send(Arrays.asList(receivingPacket), msg, keepFor);
    }

    public static void send(List<Player> receivingPacket, String msg) {
        send(receivingPacket, msg, 0);
    }

    public static synchronized void send(List<Player> receivingPacket, String msg, int keepFor) {

        if (receivingPacket == null)
            return;

        if (msg == null)
            return;

        keepFor--;
        keepFor = CMINumber.clamp(keepFor, 0, 60 * 60);

        try {
            if (!Version.getCurrent().isHigher(Version.v1_7_R4)) {
                msg = CMIChatColor.translate(msg);
                for (Player player : receivingPacket) {
                    if (player == null)
                        continue;
                    if (!player.isOnline())
                        continue;
                    player.sendMessage(msg);
                }
                return;
            }

            RawMessage rm = new RawMessage();
            rm.addText(msg);

            Object serialized = CMILib.getInstance().getReflectionManager().textToIChatBaseComponent(CMIChatColor.translate(rm.getRaw()));

            Object p = null;

            if (Version.isMojangMappings()) {
                p = constructor.newInstance(serialized, true);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                p = constructor.newInstance(serialized, true);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                if (Version.isCurrentSubEqual(0))
                    p = constructor.newInstance(serialized, 2);
                else
                    p = constructor.newInstance(serialized, true);
            } else if (Version.isCurrentHigher(Version.v1_15_R1)) {
                p = constructor.newInstance(serialized, consts[2], new UUID(0, 0));
            } else if (Version.isCurrentHigher(Version.v1_11_R1)) {
                p = constructor.newInstance(serialized, consts[2]);
            } else if (Version.isCurrentHigher(Version.v1_7_R4)) {
                p = constructor.newInstance(serialized, (byte) 2);
            } else {
                p = constructor.newInstance(serialized, 2);
            }

            if (keepFor < 1) {
                for (Player player : receivingPacket) {
                    if (player == null)
                        continue;

                    actionbarMap.remove(player.getUniqueId());

                    CMILib.getInstance().getReflectionManager().sendPlayerPacket(player, p);
                }
                return;
            }

            for (Player player : receivingPacket) {
                if (player == null)
                    continue;

                repeatingActionBar old = actionbarMap.computeIfAbsent(player.getUniqueId(), k -> new repeatingActionBar(player));

                old.setUntil(System.currentTimeMillis() + (keepFor * 1000L));
                old.setPacket(p);

                CMILib.getInstance().getReflectionManager().sendPlayerPacket(player, p);

            }

            runTimer();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static CMITask timer = null;

    private static void runTimer() {

        if (timer != null) {
            return;
        }

        if (actionbarMap.isEmpty())
            return;

        timer = CMIScheduler.runTimerAsync(CMILib.getInstance(), () -> {

            Iterator<Entry<UUID, repeatingActionBar>> iter = actionbarMap.entrySet().iterator();
            while (iter.hasNext()) {

                Entry<UUID, repeatingActionBar> next = iter.next();
                repeatingActionBar data = next.getValue();

                if (data.getUntil() < System.currentTimeMillis()) {
                    iter.remove();
                    continue;
                }

                if (data.getMessage() != null) {
                    spigotSend(data.getPlayer(), data.getMessage());
                    continue;
                }

                try {
                    CMILib.getInstance().getReflectionManager().sendPlayerPacket(data.getPlayer(), data.getPacket());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            if (!actionbarMap.isEmpty())
                return;

            timer.cancel();
            timer = null;
        }, 0L, 20L);

    }

    private static void spigotSend(Player player, String msg) {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, player.getUniqueId(), new TextComponent(CMIChatColor.translate(msg)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
