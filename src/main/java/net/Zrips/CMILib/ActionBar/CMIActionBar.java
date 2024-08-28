package net.Zrips.CMILib.ActionBar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Class<?> nmsChatSerializer;
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
        if (Version.isCurrentEqualOrHigher(Version.v1_21_R1))
            return;

        if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                packetType = net.minecraft.network.protocol.game.ClientboundSystemChatPacket.class;
                nmsIChatBaseComponent = net.minecraft.network.chat.IChatBaseComponent.class;
                nmsChatSerializer = net.minecraft.network.chat.IChatBaseComponent.ChatSerializer.class;
                getHandle = Class.forName("org.bukkit.craftbukkit." + Version.getCurrent() + ".entity.CraftPlayer").getMethod("getHandle");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
            try {
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("c");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("b", net.minecraft.network.protocol.Packet.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
            try {
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("c");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("a", net.minecraft.network.protocol.Packet.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("b");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "sendPacket",
                    net.minecraft.network.protocol.Packet.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                packetType = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                nmsIChatBaseComponent = net.minecraft.network.chat.IChatBaseComponent.class;
                getHandle = Class.forName("org.bukkit.craftbukkit." + Version.getCurrent() + ".entity.CraftPlayer").getMethod("getHandle");
                playerConnection = net.minecraft.server.level.EntityPlayer.class.getField("b");
                sendPacket = net.minecraft.server.network.PlayerConnection.class.getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "sendPacket",
                    net.minecraft.network.protocol.Packet.class);
                ChatMessageclz = net.minecraft.network.chat.ChatMessageType.class;

                consts = ChatMessageclz.getEnumConstants();
                sub = consts[2].getClass();

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentHigher(Version.v1_7_R4)) {
            Version version = Version.getCurrent();
            try {
                packetType = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
                Class<?> typeCraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                Class<?> typeNMSPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
                Class<?> typePlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
                nmsChatSerializer = Class.forName(getChatSerializerClasspath());
                nmsIChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
                getHandle = typeCraftPlayer.getMethod("getHandle");
                playerConnection = typeNMSPlayer.getField("playerConnection");
                sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
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
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
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
            send((Player) receivingPacket, msg);
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
        send(receivingPacket, msg, 0);
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

        if (Version.isCurrentEqualOrHigher(Version.v1_21_R1)) {
            simplifiedSend(receivingPacket, msg, keepFor);
            return;
        }

        try {
            if (!Version.getCurrent().isHigher(Version.v1_7_R4) || Version.isCurrentLower(Version.v1_21_R1) && nmsChatSerializer == null) {
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

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
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

            Object packet = p;

            if (keepFor < 1) {
                for (Player player : receivingPacket) {
                    if (player == null)
                        continue;

                    actionbarMap.remove(player.getUniqueId());

                    try {
                        Object cplayer = getHandle.invoke(player);
                        Object connection = playerConnection.get(cplayer);
                        sendPacket.invoke(connection, packet);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                return;
            }

            for (Player player : receivingPacket) {
                if (player == null)
                    continue;

                Object cplayer = getHandle.invoke(player);
                Object connection = playerConnection.get(cplayer);

                repeatingActionBar old = actionbarMap.computeIfAbsent(player.getUniqueId(), k -> new repeatingActionBar(player));

                old.setUntil(System.currentTimeMillis() + (keepFor * 1000L));
                old.setConnection(connection);
                old.setPacket(packet);

                try {
                    sendPacket.invoke(connection, packet);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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

        timer = CMIScheduler.runTimerAsync(() -> {

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
                    sendPacket.invoke(data.getConnection(), data.getPacket());
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

    private static synchronized void simplifiedSend(List<Player> receivingPacket, String msg, int keepFor) {

        if (keepFor < 1) {
            for (Player player : receivingPacket) {
                if (player == null)
                    continue;

                actionbarMap.remove(player.getUniqueId());
                spigotSend(player, msg);
            }
            return;
        }

        for (Player player : receivingPacket) {
            if (player == null)
                continue;

            repeatingActionBar old = actionbarMap.computeIfAbsent(player.getUniqueId(), k -> new repeatingActionBar(player, msg));
            old.setUntil(System.currentTimeMillis() + (keepFor * 1000L));
            spigotSend(player, msg);
        }

        if (keepFor > 0)
            runTimer();
    }

    private static void spigotSend(Player player, String msg) {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, player.getUniqueId(), new TextComponent(CMIChatColor.translate(msg)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static String getChatSerializerClasspath() {
        if (!Version.isCurrentHigher(Version.v1_8_R2))
            return "net.minecraft.server." + Version.getCurrent() + ".ChatSerializer";
        return "net.minecraft.server." + Version.getCurrent() + ".IChatBaseComponent$ChatSerializer";// 1_8_R2 moved to IChatBaseComponent
    }
}
