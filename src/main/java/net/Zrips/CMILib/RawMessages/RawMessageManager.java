package net.Zrips.CMILib.RawMessages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.commands.CommandsHandler;

public class RawMessageManager {

    private static Object packet;
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

    static HashMap<UUID, Set<Long>> temp = new HashMap<UUID, Set<Long>>();
    //Lets limit cache to specific amount to avoid memory leaks
    private static final int MAX_ENTRIES = 5000;
    static LinkedHashMap<Long, RawMessageCommand> map = new LinkedHashMap<Long, RawMessageCommand>(MAX_ENTRIES + 1, .75F, false) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, RawMessageCommand> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    public static final String rmc = "rmc";
    public static final String rmccmd = "/" + CommandsHandler.getLabel() + " " + rmc + " ";

    public static Long add(RawMessageCommand rmc) {
        Random rnd = new Random();
        long id = rnd.nextLong();
        while (map.containsKey(id)) {
            id = rnd.nextLong();
        }
        map.put(id, rmc);
        if (rmc.getUUID() != null) {
            Set<Long> old = temp.getOrDefault(rmc.getUUID(), new HashSet<Long>());
            old.add(id);
            temp.put(rmc.getUUID(), old);
        }
        return id;
    }

    public static void clearCache(UUID uuid) {
        if (uuid == null)
            return;
        Set<Long> list = temp.get(uuid);
        if (list != null)
            for (Long one : list) {
                map.remove(one);
            }
    }

    public static void delete(RawMessageCommand rmc) {
        remove(rmc.getId());
    }

    public static boolean perform(CommandSender sender, Long id) {
        RawMessageCommand rmc = map.get(id);
        if (rmc == null)
            return false;
        rmc.run(sender);

        if (!rmc.isKeep())
            delete(rmc);
        return true;
    }

    public static RawMessageCommand get(Long id) {
        return map.get(id);
    }

    public static void remove(Long id) {
        map.remove(id);
    }

    static {
        Version version = Version.getCurrent();
        if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
            try {
                getHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
                playerConnection = net.minecraft.server.level.EntityPlayer.class.getField("c");
                sendPacket = net.minecraft.server.network.PlayerConnection.class.getMethod("b", net.minecraft.network.protocol.Packet.class);
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                constructor = net.minecraft.network.protocol.game.ClientboundSystemChatPacket.class.getConstructor(net.minecraft.network.chat.IChatBaseComponent.class, boolean.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
            try {
                getHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
                packetType = net.minecraft.network.protocol.game.ClientboundSystemChatPacket.class;
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("c");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("a", net.minecraft.network.protocol.Packet.class);

                constructor = packetType.getConstructor(nmsIChatBaseComponent, boolean.class);

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                getHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
                packetType = net.minecraft.network.protocol.game.ClientboundSystemChatPacket.class;
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("b");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "sendPacket",
                    net.minecraft.network.protocol.Packet.class);

                if (Version.isCurrentSubEqual(0))
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, int.class);
                else
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, boolean.class);

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                getHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
                packetType = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat");
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                playerConnection = Class.forName("net.minecraft.server.level.EntityPlayer").getField("b");
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "sendPacket",
                    net.minecraft.network.protocol.Packet.class);
                ChatMessageclz = Class.forName("net.minecraft.network.chat.ChatMessageType");
                consts = ChatMessageclz.getEnumConstants();
                sub = consts[2].getClass();
                constructor = packetType.getConstructor(nmsIChatBaseComponent, sub, UUID.class);

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
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

                if (Version.isCurrentHigher(Version.v1_15_R1)) {
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, sub, UUID.class);
                } else if (Version.isCurrentHigher(Version.v1_11_R1)) {
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, sub);
                } else if (Version.isCurrentHigher(Version.v1_7_R4)) {
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, byte.class);
                } else {
                    constructor = packetType.getConstructor(nmsIChatBaseComponent, int.class);
                }

            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException ex) {
                CMIMessages.consoleMessage("Error {0} ");
                CMIMessages.consoleMessage(ex.toString());
            }
        }
    }

    public static void send(Set<Player> players, String ajson) {
        if (players == null || players.isEmpty() || ajson == null)
            return;

//	if (players.size() == 1) {
//	    ajson = CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(players.iterator().next(), ajson);
//	}

        String json = ajson;

        Object serialized = null;
        try {
            serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, json);
        } catch (Exception ex) {
            ex.printStackTrace();

            for (Player receivingPacket : players) {
                if (!receivingPacket.isOnline())
                    return;

                receivingPacket.sendMessage(ajson);
            }
            return;
        }

        for (Player receivingPacket : players) {
            if (!receivingPacket.isOnline())
                return;

            try {

//		if (method != null) {
//		    serialized = method.invoke(null, CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(receivingPacket, json));
//		}

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                    packet = constructor.newInstance(serialized, false);
                } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                    if (Version.isCurrentSubEqual(0))
                        packet = constructor.newInstance(serialized, 1);
                    else
                        packet = constructor.newInstance(serialized, false);
                } else if (Version.isCurrentHigher(Version.v1_15_R1)) {
                    packet = constructor.newInstance(serialized, consts[1], new UUID(0, 0));
                } else if (Version.isCurrentHigher(Version.v1_11_R1)) {
                    packet = constructor.newInstance(serialized, consts[1]);
                } else if (Version.isCurrentHigher(Version.v1_7_R4)) {
                    packet = constructor.newInstance(serialized, (byte) 1);
                } else {
                    packet = constructor.newInstance(serialized, 1);
                }
                Object player = getHandle.invoke(receivingPacket);
                Object connection = playerConnection.get(player);

                sendPacket.invoke(connection, packet);
            } catch (Exception ex) {
                ex.printStackTrace();
                CMIMessages.consoleMessage("Failed to show json message with packets, using command approach");
                CMIScheduler.get().runTask(() -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:tellraw \"" + receivingPacket.getName() + "\" " + json);
                });
            }
        }

    }

    public static void send(CommandSender receivingPacket, String msg) {
        if (receivingPacket instanceof Player)
            send((Player) receivingPacket, msg);
        else
            receivingPacket.sendMessage(msg);
    }

    public static void send(Player receivingPacket, String json) {

        if (receivingPacket == null)
            return;
        if (!receivingPacket.isOnline())
            return;

        HashSet<Player> set = new HashSet<Player>();
        set.add(receivingPacket);

        send(set, json);
    }

    private static String getChatSerializerClasspath() {
        if (!Version.isCurrentHigher(Version.v1_8_R2))
            return "net.minecraft.server." + Version.getCurrent() + ".ChatSerializer";
        return "net.minecraft.server." + Version.getCurrent() + ".IChatBaseComponent$ChatSerializer";// 1_8_R2 moved to IChatBaseComponent
    }
}
