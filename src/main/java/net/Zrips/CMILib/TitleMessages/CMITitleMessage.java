package net.Zrips.CMILib.TitleMessages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.ActionBar.CMIActionBar;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class CMITitleMessage {
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Class<?> nmsIChatBaseComponent;

    private static Constructor<?> nmsPacketPlayOutTitle;
    private static Constructor<?> nmsPacketPlayOutTimes;
    private static Class<?> enumTitleAction;
    private static Method fromString;
    private static boolean simpleTitleMessages = false;

    private static Object timerResetPacket = null;

    static {

        if (Version.isCurrentHigher(Version.v1_7_R4)) {
            Version version = Version.getCurrent();
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                try {
                    getHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
                    nmsIChatBaseComponent = net.minecraft.network.chat.IChatBaseComponent.class;
                    playerConnection = net.minecraft.server.level.EntityPlayer.class.getField("c");
                    sendPacket = net.minecraft.server.network.PlayerConnection.class.getMethod("a", net.minecraft.network.protocol.Packet.class);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                try {
                    getHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                nmsIChatBaseComponent = net.minecraft.network.chat.IChatBaseComponent.class;
                try {
                    playerConnection = net.minecraft.server.level.EntityPlayer.class.getField("c");
                    sendPacket = net.minecraft.server.network.PlayerConnection.class.getMethod("a", net.minecraft.network.protocol.Packet.class);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                try {
                    getHandle = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer").getMethod("getHandle");
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                nmsIChatBaseComponent = net.minecraft.network.chat.IChatBaseComponent.class;
                try {
                    playerConnection = net.minecraft.server.level.EntityPlayer.class.getField("b");
                    sendPacket = net.minecraft.server.network.PlayerConnection.class.getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "sendPacket",
                        net.minecraft.network.protocol.Packet.class);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
// Timer reset packet
//                if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
//                    timerResetPacket = new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(10, 70, 20);
//                }

            } else {
                try {
                    Class<?> typeCraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                    Class<?> typeNMSPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
                    Class<?> typePlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
                    nmsIChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
                    getHandle = typeCraftPlayer.getMethod("getHandle");
                    playerConnection = typeNMSPlayer.getField("playerConnection");
                    sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "Error {0}", ex);
                }

                // Title
                try {
                    Class<?> typePacketPlayOutTitle = Class.forName(getPacketPlayOutTitleClasspath());
                    enumTitleAction = Class.forName(getEnumTitleActionClasspath());
                    nmsPacketPlayOutTitle = typePacketPlayOutTitle.getConstructor(enumTitleAction, nmsIChatBaseComponent);
                    nmsPacketPlayOutTimes = typePacketPlayOutTitle.getConstructor(int.class, int.class, int.class);
                    fromString = Class.forName("org.bukkit.craftbukkit." + version + ".util.CraftChatMessage").getMethod("fromString", String.class);
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
                    simpleTitleMessages = true;
                }
            }
        }
    }

    public static void send(CMICommandSender receivingPacket, final Object title, final Object subtitle) {
        if (!receivingPacket.isPlayer()) {
            receivingPacket.sendMessage(String.valueOf(title) + String.valueOf(subtitle));
            return;
        }
        send(receivingPacket.getPlayer(), title, subtitle, 0, 40, 10);
    }

    public static void send(final Player receivingPacket, final Object title, final Object subtitle) {
        send(receivingPacket, title, subtitle, 0, 40, 10);
    }

    public static void send(final Player receivingPacket, final Object title, final Object subtitle, final int fadeIn, final int keep, final int fadeOut) {
        if (receivingPacket == null)
            return;
        CMIScheduler.get().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {

                String t = title == null ? null : CMIChatColor.translate((String) title);
                String s = subtitle == null ? null : CMIChatColor.translate((String) subtitle);

                if (Version.isCurrentEqualOrLower(Version.v1_7_R4)) {
                    CMIActionBar.send(receivingPacket, (t == null ? "" : t) + (s == null ? "" : s));
                    return;
                }

                if (simpleTitleMessages) {
                    receivingPacket.sendMessage(t);
                    receivingPacket.sendMessage(s);
                    return;
                }
                try {
                    switch (Version.getCurrent()) {
                    case v1_12_R1:
                    case v1_13_R1:
                    case v1_13_R2:
                    case v1_14_R1:
                    case v1_15_R1:
                    case v1_16_R1:
                    default:
                        receivingPacket.sendTitle(t, s, fadeIn, keep, fadeOut);
//                        Timer reset packet, causes some issue
//                        if (Version.isCurrentEqualOrHigher(Version.v1_19_R1) && timerResetPacket != null) {
//                            sendPacket(receivingPacket, timerResetPacket);
//                        }

                        break;
                    case v1_9_R1:
                    case v1_9_R2:
                    case v1_10_R1:
                    case v1_11_R1:
                    case v1_8_R1:
                    case v1_8_R2:
                    case v1_8_R3:
                        Object packetTimes = nmsPacketPlayOutTimes.newInstance(fadeIn, keep, fadeOut);
                        sendPacket(receivingPacket, packetTimes);
                        if (title != null) {
                            Object packetTitle = nmsPacketPlayOutTitle.newInstance(enumTitleAction.getField("TITLE").get(null), ((Object[]) fromString.invoke(null, t))[0]);
                            sendPacket(receivingPacket, packetTitle);
                        }
                        if (subtitle != null) {
                            if (title == null) {
                                Object packetTitle = nmsPacketPlayOutTitle.newInstance(enumTitleAction.getField("TITLE").get(null), ((Object[]) fromString.invoke(null, ""))[0]);
                                sendPacket(receivingPacket, packetTitle);
                            }
                            Object packetSubtitle = nmsPacketPlayOutTitle.newInstance(enumTitleAction.getField("SUBTITLE").get(null), ((Object[]) fromString.invoke(null, s))[0]);
                            sendPacket(receivingPacket, packetSubtitle);
                        }
                        break;
                    }

                } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
                    simpleTitleMessages = true;
                    Bukkit.getLogger().log(Level.SEVERE, "Your server can't fully support title messages. They will be shown in chat instead.");
                }
                return;
            }
        });
    }

    private static void sendPacket(Player player, Object packet) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object handle = getHandle.invoke(player);
        Object connection = playerConnection.get(handle);
        sendPacket.invoke(connection, packet);
    }

    private static String getPacketPlayOutTitleClasspath() {
        return "net.minecraft.server." + Version.getCurrent() + ".PacketPlayOutTitle";
    }

    private static String getEnumTitleActionClasspath() {
        return getPacketPlayOutTitleClasspath() + "$EnumTitleAction";
    }

}
