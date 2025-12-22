package net.Zrips.CMILib.Container;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Version.Version;

public class CMIPlayerConnection implements Listener {

    private static ConcurrentHashMap<UUID, Object> playersConnections = new ConcurrentHashMap<UUID, Object>();

    private static Method sendPacket;

    static {
        try {
            if (Version.isMojangMappings()) {
                sendPacket = Class.forName("net.minecraft.server.network.ServerGamePacketListenerImpl").getMethod("send", Class.forName("net.minecraft.network.protocol.Packet"));
            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("b", Class.forName("net.minecraft.network.protocol.Packet"));
            } else if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("a", Class.forName("net.minecraft.network.protocol.Packet"));
            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("sendPacket", Class.forName("net.minecraft.network.protocol.Packet"));
            } else {
                sendPacket = Class.forName("net.minecraft.server." + Version.getCurrent() + ".PlayerConnection").getMethod("sendPacket",
                        Class.forName("net.minecraft.server." + Version.getCurrent() + ".Packet"));
            }
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addConnection(Player player) {
        if (player == null)
            return;

        UUID uuid = player.getUniqueId();
        if (!player.isOnline()) {
            playersConnections.remove(uuid);
            return;
        }

        playersConnections.put(uuid, CMILib.getInstance().getReflectionManager().getPlayerConnection(player));
    }

    public static @Nullable Object getConnection(Player player) {
        if (player == null)
            return null;

        UUID uuid = player.getUniqueId();

        if (!player.isOnline()) {
            playersConnections.remove(uuid);
            return null;
        }

        Object connection = getConnection(uuid);
        if (connection == null) {
            addConnection(player);
            return getConnection(uuid);
        }

        return connection;
    }

    public static @Nullable Object getConnection(@Nonnull UUID uuid) {
        if (uuid == null)
            return null;

        return playersConnections.get(uuid);
    }

    public static void removeConnection(Player player) {
        if (player == null)
            return;
        removeConnection(player.getUniqueId());
    }

    public static void removeConnection(UUID uuid) {
        playersConnections.remove(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        addConnection(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        removeConnection(event.getPlayer());
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(CMILib.getInstance()))
            playersConnections.clear();
    }

    public static void sendPacket(Player player, Object packet) {
        sendPacket(player.getUniqueId(), packet);
    }

    public static void sendPacket(UUID uuid, Object packet) {
        Object connection = getConnection(uuid);
        if (connection == null)
            return;
        sendPacket(connection, packet);
    }

    public static void sendPacket(Object connection, Object packet) {
        try {
            sendPacket.invoke(connection, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
