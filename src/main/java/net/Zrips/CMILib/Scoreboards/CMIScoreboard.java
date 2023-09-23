package net.Zrips.CMILib.Scoreboards;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;

public class CMIScoreboard {

    private static java.util.concurrent.ConcurrentMap<UUID, ScoreboardInfo> timerMap = new ConcurrentHashMap<>();

    private static void runScheduler() {
        Iterator<Entry<UUID, ScoreboardInfo>> meinMapIter = timerMap.entrySet().iterator();
        while (meinMapIter.hasNext()) {
            Entry<UUID, ScoreboardInfo> map = meinMapIter.next();

            if (System.currentTimeMillis() > map.getValue().getTime() + (map.getValue().getKeepSeconds() * 1000)) {
                Player player = Bukkit.getPlayer(map.getKey());
                if (player != null) {
                    removeScoreBoard(player);
                    player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

                    if (map.getValue().getObj() != null) {
                        try {
                            Objective obj = player.getScoreboard().getObjective(map.getValue().getObj().getName());
                            if (obj != null)
                                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                        } catch (IllegalStateException e) {
                        }
                    }
                }

                timerMap.remove(map.getKey());
            }
        }

        if (timerMap.size() > 0) {
            CMIScheduler.get().runTaskLater(() -> {
                runScheduler();
            }, 20L);
        }
    }

    private static void addNew(Player player, int keepSeconds) {
        timerMap.put(player.getUniqueId(), new ScoreboardInfo(player.getScoreboard(), DisplaySlot.SIDEBAR, keepSeconds));
        runScheduler();
    }

    private final static String objName = "CMIScoreboard";
    private static Class<?> nmsChatSerializer;
    private static Class<?> nmsIChatBaseComponent;

    static {
        if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeScoreBoard(Player player) {
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {

                Object serialized = null;
                try {
                    serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "[\"\"]");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

//		if (serialized == null)
//		    return;

                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, "", null, (IChatBaseComponent) serialized, null);

                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 1);
                setField(pp1, "d", player.getName());
                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
                sendPacket(player, pp1);

            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {

                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, "", null,
                    (IChatBaseComponent) Class.forName("net.minecraft.network.chat.ChatComponentText").getConstructor(String.class).newInstance(CMIChatColor.translate("")),
                    null);

                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 1);
                setField(pp1, "d", player.getName());
                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
                sendPacket(player, pp1);

            } else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                Object pp1 = getNMSClass("PacketPlayOutScoreboardObjective").getConstructor().newInstance();
                setField(pp1, "a", player.getName());
                setField(pp1, "d", 1);
                sendPacket(player, pp1);
            } else {
                Object boards = getNMSClass("Scoreboard").getConstructor().newInstance();

                Object obj = boards.getClass().getMethod("registerObjective", String.class,
                    getNMSClass("IScoreboardCriteria")).invoke(boards, objName,
                        getNMSClass("ScoreboardBaseCriteria").getConstructor(String.class).newInstance("JobsDummy"));
                sendPacket(player, getNMSClass("PacketPlayOutScoreboardObjective").getConstructor(obj.getClass(), int.class).newInstance(obj, 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Player player, String displayName, List<String> lines, int keepSeconds) {
        CMIScheduler.get().runTaskAsynchronously(() -> {
            setScoreBoard(player, displayName, lines);
            addNew(player, keepSeconds);
        });
    }

    private static void setScoreBoard(Player player, String displayName, List<String> lines) {
        removeScoreBoard(player);

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {

                Object serialized = null;
                try {
                    serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "[\"" + CMIChatColor.translate(displayName) + "\"]");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, player.getName(), null, (IChatBaseComponent) serialized, null);

                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
                setField(pp1, "d", player.getName());
                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
                sendPacket(player, pp1);

                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = new net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective(
                    net.minecraft.world.scores.DisplaySlot.a, sobj);
                Field field = obj.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(obj, player.getName());
                sendPacket(player, obj);

                for (int i = 0; i < 15; i++) {
                    if (i >= lines.size())
                        break;
                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore.class.getConstructor(
                        net.minecraft.server.ScoreboardServer.Action.class, String.class, String.class, int.class)
                        .newInstance(net.minecraft.server.ScoreboardServer.Action.a, player.getName(), null, 0);
                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
                    sendPacket(player, PacketPlayOutScoreboardScore);
                }
            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {

                Object serialized = null;
                try {
                    serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "[\"" + CMIChatColor.translate(displayName) + "\"]");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, player.getName(), null, (IChatBaseComponent) serialized, null);

                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
                setField(pp1, "d", player.getName());
                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
                sendPacket(player, pp1);

                Constructor<PacketPlayOutScoreboardDisplayObjective> constructor = net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective.class.getConstructor(int.class,
                    net.minecraft.world.scores.ScoreboardObjective.class);

                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = constructor.newInstance(1, sobj);
//                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = new net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective(1, sobj);
                Field field = obj.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(obj, player.getName());
                sendPacket(player, obj);

                for (int i = 0; i < 15; i++) {
                    if (i >= lines.size())
                        break;
                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore.class.getConstructor(
                        net.minecraft.server.ScoreboardServer.Action.class, String.class, String.class, int.class)
                        .newInstance(net.minecraft.server.ScoreboardServer.Action.a, player.getName(), null, 0);
                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
                    sendPacket(player, PacketPlayOutScoreboardScore);
                }
            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {

                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, player.getName(), null,
                    (IChatBaseComponent) Class.forName("net.minecraft.network.chat.ChatComponentText").getConstructor(String.class).newInstance(CMIChatColor.translate(displayName)),
                    null);

                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
                setField(pp1, "d", player.getName());
                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
                sendPacket(player, pp1);
                
                Constructor<PacketPlayOutScoreboardDisplayObjective> constructor = net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective.class.getConstructor(int.class,
                    net.minecraft.world.scores.ScoreboardObjective.class);

                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = constructor.newInstance(1, sobj);
                
//                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = new net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective(1, sobj);
                Field field = obj.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(obj, player.getName());
                sendPacket(player, obj);

                for (int i = 0; i < 15; i++) {
                    if (i >= lines.size())
                        break;
                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore.class.getConstructor(
                        net.minecraft.server.ScoreboardServer.Action.class, String.class, String.class, int.class)
                        .newInstance(net.minecraft.server.ScoreboardServer.Action.a, player.getName(), null, 0);
                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
                    sendPacket(player, PacketPlayOutScoreboardScore);
                }
            } else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                Object pp1 = getNMSClass("PacketPlayOutScoreboardObjective").getConstructor().newInstance();
                setField(pp1, "a", player.getName());
                setField(pp1, "d", 0);
                setField(pp1, "b", getNMSClass("ChatComponentText").getConstructor(String.class).newInstance(CMIChatColor.translate(displayName)));
                setField(pp1, "c", getNMSClass("IScoreboardCriteria$EnumScoreboardHealthDisplay").getEnumConstants()[1]);
                sendPacket(player, pp1);

                Object d0 = getNMSClass("PacketPlayOutScoreboardDisplayObjective").getConstructor().newInstance();
                setField(d0, "a", 1);
                setField(d0, "b", player.getName());
                sendPacket(player, d0);

                for (int i = 0; i < 15; i++) {
                    if (i >= lines.size())
                        break;
                    Object PacketPlayOutScoreboardScore = getNMSClass("PacketPlayOutScoreboardScore").getConstructor().newInstance();

                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
                    setField(PacketPlayOutScoreboardScore, "b", player.getName());
                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
                    setField(PacketPlayOutScoreboardScore, "d", getNMSClass("ScoreboardServer$Action").getEnumConstants()[0]);
                    sendPacket(player, PacketPlayOutScoreboardScore);
                }
            } else {
                Object boards = getNMSClass("Scoreboard").getConstructor().newInstance();
                Object obj = boards.getClass().getMethod("registerObjective", String.class, getNMSClass("IScoreboardCriteria"))
                    .invoke(boards, objName, getNMSClass("ScoreboardBaseCriteria").getConstructor(String.class).newInstance("JobsDummy"));

                obj.getClass().getMethod("setDisplayName", String.class).invoke(obj, CMIChatColor.translate(displayName));

                sendPacket(player, getNMSClass("PacketPlayOutScoreboardObjective").getConstructor(obj.getClass(), int.class).newInstance(obj, 1));
                sendPacket(player, getNMSClass("PacketPlayOutScoreboardObjective").getConstructor(obj.getClass(), int.class).newInstance(obj, 0));

                sendPacket(player, getNMSClass("PacketPlayOutScoreboardDisplayObjective").getConstructor(int.class,
                    getNMSClass("ScoreboardObjective")).newInstance(1, obj));

                for (int i = 0; i < 15; i++) {
                    if (i >= lines.size())
                        break;

                    Object packet2 = getNMSClass("ScoreboardScore").getConstructor(getNMSClass("Scoreboard"),
                        getNMSClass("ScoreboardObjective"), String.class).newInstance(boards, obj, CMIChatColor.translate(lines.get(i)));
                    packet2.getClass().getMethod("setScore", int.class).invoke(packet2, 15 - i);

                    sendPacket(player, getNMSClass("PacketPlayOutScoreboardScore").getConstructor(getNMSClass("ScoreboardScore")).newInstance(packet2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setField(Object edit, String fieldName, Object value) {
        try {
            Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            CMILib.getInstance().getReflectionManager().sendPlayerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
//	try {
//	    getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet")).invoke(getConnection(player), packet);
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
    }

    private static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        return CMILib.getInstance().getReflectionManager().getMinecraftClass(nmsClassString);
    }

//    private static Object getConnection(Player player) throws Exception {
//	Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
//	return nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
//    }
}
