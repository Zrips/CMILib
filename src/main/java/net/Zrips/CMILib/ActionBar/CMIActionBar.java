package net.Zrips.CMILib.ActionBar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

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
        if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
            try {
                packetType = net.minecraft.network.protocol.game.ClientboundSystemChatPacket.class;
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("c");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("b", net.minecraft.network.protocol.Packet.class);
                getHandle = Class.forName("org.bukkit.craftbukkit." + Version.getCurrent() + ".entity.CraftPlayer").getMethod("getHandle");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
            try {
                packetType = net.minecraft.network.protocol.game.ClientboundSystemChatPacket.class;
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("c");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("a", net.minecraft.network.protocol.Packet.class);
                getHandle = Class.forName("org.bukkit.craftbukkit." + Version.getCurrent() + ".entity.CraftPlayer").getMethod("getHandle");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                packetType = net.minecraft.network.protocol.game.ClientboundSystemChatPacket.class;
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("b");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "sendPacket",
                    net.minecraft.network.protocol.Packet.class);
                getHandle = Class.forName("org.bukkit.craftbukkit." + Version.getCurrent() + ".entity.CraftPlayer").getMethod("getHandle");
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

    static HashMap<UUID, repeatingActionBar> actionbarMap = new HashMap<UUID, repeatingActionBar>();

    public static void send(Player receivingPacket, String msg) {
        send(receivingPacket, msg, 0);
    }

    public static void send(Player receivingPacket, String msg, int keepFor) {
        send(Arrays.asList(receivingPacket), msg, keepFor);
    }

    public static void send(List<Player> receivingPacket, String msg) {
        send(receivingPacket, msg, 0);
    }

    public static void send(List<Player> receivingPacket, String msg, int keepFor) {

        if (receivingPacket == null)
            return;
        if (msg == null)
            return;

        keepFor--;
        keepFor = keepFor < 0 ? 0 : keepFor;
        try {
            if (!Version.getCurrent().isHigher(Version.v1_7_R4) || nmsChatSerializer == null) {
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
            Object serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, CMIChatColor.translate(rm.getRaw()));

            Object p = null;

            for (Player player : receivingPacket) {
                if (player == null)
                    continue;

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
                Object cplayer = getHandle.invoke(player);
                Object connection = playerConnection.get(cplayer);

                Object packet = p;
                if (keepFor < 1) {
                    CMIScheduler.get().runTaskAsynchronously(() -> {
                        try {
                            sendPacket.invoke(connection, packet);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    });
                    continue;
                }

                repeatingActionBar old = actionbarMap.remove(player.getUniqueId());
                if (old != null)
                    old.cancel();
                old = new repeatingActionBar();
                old.setUntil(System.currentTimeMillis() + (keepFor * 1000L));

                actionbarMap.put(player.getUniqueId(), old);

                old.setScheduler(CMIScheduler.get().runTimerAsync(new Runnable() {
                    @Override
                    public void run() {
                        repeatingActionBar old = actionbarMap.get(player.getUniqueId());
                        if (old == null)
                            return;

                        if (old.getUntil() < System.currentTimeMillis()) {
                            old.cancel();
                            return;
                        }

                        try {
                            sendPacket.invoke(connection, packet);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }, 0L, 20L));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getChatSerializerClasspath() {
        if (!Version.isCurrentHigher(Version.v1_8_R2))
            return "net.minecraft.server." + Version.getCurrent() + ".ChatSerializer";
        return "net.minecraft.server." + Version.getCurrent() + ".IChatBaseComponent$ChatSerializer";// 1_8_R2 moved to IChatBaseComponent
    }
}
