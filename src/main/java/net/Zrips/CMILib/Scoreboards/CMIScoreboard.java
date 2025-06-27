package net.Zrips.CMILib.Scoreboards;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

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

    private static Class<?> scoreboardObjectiveClass = null;
    private static Class<?> packetPlayOutScoreboardObjectiveClass = null;
    private static Class<?> enumScoreboardHealthDisplayClass = null;
    private static Method chatSerializerA = null;
    private static Constructor<?> chatComponentTextConstructor = null;

    private static Class<?> packetPlayOutScoreboardDisplayObjective = null;
    private static Class<?> packetPlayOutScoreboardScore = null;
    private static Class<?> scoreboardScoreAction = null;
    private static Object healthDisplayValue = null;
    private static Object scoreActionValue = null;
    private static Class<?> chatComponentText = null;

    static {
        if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                nmsIChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
                scoreboardObjectiveClass = Class.forName("net.minecraft.world.scores.ScoreboardObjective");
                packetPlayOutScoreboardObjectiveClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective");
                enumScoreboardHealthDisplayClass = Class.forName("net.minecraft.world.scores.criteria.IScoreboardCriteria$EnumScoreboardHealthDisplay");
            } catch (Throwable e) {
            }

            try {
                // Only for 1.13 -> 1.19                
                chatComponentText = Class.forName("net.minecraft.network.chat.ChatComponentText");
                chatComponentTextConstructor = chatComponentText.getConstructor(String.class);
            } catch (Throwable e) {
            }

            try {
                // Only for 1.19 -> 1.20
                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
                chatSerializerA = nmsChatSerializer.getMethod("a", String.class);
            } catch (Throwable e) {
            }

            try {
                packetPlayOutScoreboardDisplayObjective = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective");
                packetPlayOutScoreboardScore = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore");
            } catch (Throwable e) {
            }

            try {
                scoreboardScoreAction = Class.forName("net.minecraft.server.ScoreboardServer$Action");
                scoreActionValue = scoreboardScoreAction.getEnumConstants()[0];
            } catch (Throwable e) {
            }

            try {
                healthDisplayValue = enumScoreboardHealthDisplayClass.getEnumConstants()[1];
            } catch (Throwable e) {
            }
        }
    }

    public static void removeScoreBoard(Player player) {
        try {
            Object serialized;

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                serialized = CMILib.getInstance().getReflectionManager().textToIChatBaseComponent("[\"\"]");

                Object sobj = getScoreboardObjectiveConstructor().newInstance(null, "", null, serialized, null, false, null);

                Object packet = packetPlayOutScoreboardObjectiveClass.getConstructor(scoreboardObjectiveClass, int.class).newInstance(sobj, 1);

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                    setField(packet, "e", player.getName());
                    setField(packet, "g", enumScoreboardHealthDisplayClass.getField("b").get(null));
                } else {
                    setField(packet, "d", player.getName());
                    setField(packet, "f", enumScoreboardHealthDisplayClass.getField("b").get(null));
                }

                sendPacket(player, packet);

            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                serialized = chatSerializerA.invoke(null, "[\"\"]");

                Object sobj = getScoreboardObjectiveConstructor().newInstance(null, "", null, serialized, null);

                Object packet = packetPlayOutScoreboardObjectiveClass.getConstructor(scoreboardObjectiveClass, int.class).newInstance(sobj, 1);

                setField(packet, "d", player.getName());
                setField(packet, "f", enumScoreboardHealthDisplayClass.getField("b").get(null));
                sendPacket(player, packet);

            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                Object chatComponent = chatComponentTextConstructor.newInstance(CMIChatColor.translate(""));

                Object sobj = getScoreboardObjectiveConstructor().newInstance(null, "", null, chatComponent, null);
                Object packet = packetPlayOutScoreboardObjectiveClass.getConstructor(scoreboardObjectiveClass, int.class).newInstance(sobj, 1);
                setField(packet, "d", player.getName());
                setField(packet, "f", enumScoreboardHealthDisplayClass.getField("b").get(null));
                sendPacket(player, packet);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                Object packet = packetPlayOutScoreboardObjectiveClass.getConstructor().newInstance();
                setField(packet, "a", player.getName());
                setField(packet, "d", 1);
                sendPacket(player, packet);
            } else {
                Object boards = getNMSClass("Scoreboard").getConstructor().newInstance();
                Object baseCriteria = getNMSClass("ScoreboardBaseCriteria").getConstructor(String.class).newInstance("JobsDummy");
                Object obj = boards.getClass().getMethod("registerObjective", String.class, getNMSClass("IScoreboardCriteria")).invoke(boards, "JobsDummy", baseCriteria);
                Object packet = getNMSClass("PacketPlayOutScoreboardObjective").getConstructor(obj.getClass(), int.class).newInstance(obj, 1);
                sendPacket(player, packet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void removeScoreBoard(Player player) {
//
//        try {
//            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
//
//                Object serialized = CMILib.getInstance().getReflectionManager().textToIChatBaseComponent("[\"\"]");
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, "", null, (IChatBaseComponent) serialized, null, false, null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 1);
//                setField(pp1, "e", player.getName());
//                setField(pp1, "g", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
//
//                Object serialized = CMILib.getInstance().getReflectionManager().textToIChatBaseComponent("[\"\"]");
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, "", null, (IChatBaseComponent) serialized, null, false, null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 1);
//                setField(pp1, "d", player.getName());
//                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
//
//                Object serialized = null;
//                try {
//                    serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "[\"\"]");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
////		if (serialized == null)
////		    return;
//
//                Constructor<ScoreboardObjective> cons = net.minecraft.world.scores.ScoreboardObjective.class.getConstructor(
//                    net.minecraft.world.scores.Scoreboard.class,
//                    String.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.class,
//                    net.minecraft.network.chat.IChatBaseComponent.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.class);
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = cons.newInstance(null, "", null, serialized, null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 1);
//                setField(pp1, "d", player.getName());
//                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
//
//                Constructor<ScoreboardObjective> cons = net.minecraft.world.scores.ScoreboardObjective.class.getConstructor(
//                    net.minecraft.world.scores.Scoreboard.class,
//                    String.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.class,
//                    net.minecraft.network.chat.IChatBaseComponent.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.class);
//                net.minecraft.world.scores.ScoreboardObjective sobj = cons.newInstance(null, "", null,
//                    (IChatBaseComponent) Class.forName("net.minecraft.network.chat.ChatComponentText").getConstructor(String.class).newInstance(CMIChatColor.translate("")),
//                    null);
//
////                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, "", null,
////                    (IChatBaseComponent) Class.forName("net.minecraft.network.chat.ChatComponentText").getConstructor(String.class).newInstance(CMIChatColor.translate("")),
////                    null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 1);
//                setField(pp1, "d", player.getName());
//                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
//                Object pp1 = getNMSClass("PacketPlayOutScoreboardObjective").getConstructor().newInstance();
//                setField(pp1, "a", player.getName());
//                setField(pp1, "d", 1);
//                sendPacket(player, pp1);
//            } else {
//                Object boards = getNMSClass("Scoreboard").getConstructor().newInstance();
//
//                Object obj = boards.getClass().getMethod("registerObjective", String.class,
//                    getNMSClass("IScoreboardCriteria")).invoke(boards, objName,
//                        getNMSClass("ScoreboardBaseCriteria").getConstructor(String.class).newInstance("JobsDummy"));
//                sendPacket(player, getNMSClass("PacketPlayOutScoreboardObjective").getConstructor(obj.getClass(), int.class).newInstance(obj, 1));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void show(Player player, String displayName, List<String> lines, int keepSeconds) {
        CMIScheduler.get().runTaskAsynchronously(() -> {
            setScoreBoard(player, displayName, lines);
            addNew(player, keepSeconds);
        });
    }

    private static Constructor<?> scoreboardObjectiveConstructor = null;

    private static Constructor<?> getScoreboardObjectiveConstructor() {
        if (scoreboardObjectiveConstructor != null)
            return scoreboardObjectiveConstructor;

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                scoreboardObjectiveConstructor = scoreboardObjectiveClass.getConstructor(
                    Class.forName("net.minecraft.world.scores.Scoreboard"),
                    String.class,
                    Class.forName("net.minecraft.world.scores.criteria.IScoreboardCriteria"),
                    nmsIChatBaseComponent,
                    enumScoreboardHealthDisplayClass,
                    boolean.class,
                    Class.forName("net.minecraft.network.chat.numbers.NumberFormat"));
            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                scoreboardObjectiveConstructor = scoreboardObjectiveClass.getConstructor(
                    Class.forName("net.minecraft.world.scores.Scoreboard"),
                    String.class,
                    Class.forName("net.minecraft.world.scores.criteria.IScoreboardCriteria"),
                    nmsIChatBaseComponent,
                    enumScoreboardHealthDisplayClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scoreboardObjectiveConstructor;
    }

    private static Constructor<?> playOutScoreboardDisplayObjectiveConstructor = null;

    private static Constructor<?> getPlayOutScoreboardDisplayObjectiveConstructor() {
        if (playOutScoreboardDisplayObjectiveConstructor != null)
            return playOutScoreboardDisplayObjectiveConstructor;

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                playOutScoreboardDisplayObjectiveConstructor = packetPlayOutScoreboardDisplayObjective.getConstructor(Class.forName("net.minecraft.world.scores.DisplaySlot"), scoreboardObjectiveClass);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                playOutScoreboardDisplayObjectiveConstructor = packetPlayOutScoreboardDisplayObjective.getConstructor(int.class, scoreboardObjectiveClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return playOutScoreboardDisplayObjectiveConstructor;
    }

    private static void setScoreBoard(Player player, String displayName, List<String> lines) {
        removeScoreBoard(player);
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                String playerNameFieldName = "e";
                String healthFieldName = "g";
                String objectiveFieldName = "c";
                Object serialized = null;

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                    playerNameFieldName = "e";
                    healthFieldName = "g";
                    objectiveFieldName = "c";
                } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                    playerNameFieldName = "d";
                    healthFieldName = "f";
                    objectiveFieldName = "b";
                }

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                    serialized = CMILib.getInstance().getReflectionManager().textToIChatBaseComponent("[\"" + CMIChatColor.translate(displayName) + "\"]");
                } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                    serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "[\"" + CMIChatColor.translate(displayName) + "\"]");
                }

                Object sobj = null;

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                    sobj = getScoreboardObjectiveConstructor().newInstance(null, player.getName(), null, serialized, null, false, null);
                } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                    sobj = getScoreboardObjectiveConstructor().newInstance(null, player.getName(), null, serialized, null);
                }

                Object pp1 = packetPlayOutScoreboardObjectiveClass.getConstructor(scoreboardObjectiveClass, int.class).newInstance(sobj, 0);
                setField(pp1, playerNameFieldName, player.getName());

                setField(pp1, healthFieldName, healthDisplayValue);
                sendPacket(player, pp1);

                Object obj = null;

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                    obj = getPlayOutScoreboardDisplayObjectiveConstructor().newInstance(Class.forName("net.minecraft.world.scores.DisplaySlot").getEnumConstants()[1], sobj);
                } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                    obj = getPlayOutScoreboardDisplayObjectiveConstructor().newInstance(1, sobj);
                }

                Field field = packetPlayOutScoreboardDisplayObjective.getDeclaredField(objectiveFieldName);
                field.setAccessible(true);
                field.set(obj, player.getName());
                sendPacket(player, obj);

                Constructor<?> scoreCons = null;
                Object value1 = null;
                Object value2 = null;

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                    scoreCons = packetPlayOutScoreboardScore.getConstructor(
                        String.class,
                        String.class,
                        int.class,
                        java.util.Optional.class,
                        java.util.Optional.class);
                    value1 = Optional.ofNullable(null);
                    value2 = Optional.ofNullable(null);
                } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                    scoreCons = packetPlayOutScoreboardScore.getConstructor(
                        String.class,
                        String.class,
                        int.class,
                        nmsIChatBaseComponent,
                        Class.forName("net.minecraft.network.chat.numbers.NumberFormat"));
                } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                    scoreCons = packetPlayOutScoreboardScore.getConstructor(
                        scoreboardScoreAction,
                        String.class,
                        String.class,
                        int.class);
                }

                if (scoreCons == null)
                    return;

                for (int i = 0; i < 15 && i < lines.size(); i++) {
                    Object scorePacket = null;
                    if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                        scorePacket = scoreCons.newInstance(CMIChatColor.translate(lines.get(i)), player.getName(), 15 - i, value1, value2);
                    } else {
                        scorePacket = scoreCons.newInstance(scoreActionValue, player.getName(), null, 0);
                        setField(scorePacket, "a", CMIChatColor.translate(lines.get(i)));
                        setField(scorePacket, "c", 15 - i);
                    }

                    sendPacket(player, scorePacket);
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
//    private static void setScoreBoard(Player player, String displayName, List<String> lines) {
//        removeScoreBoard(player);
//
//        try {
//            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
//
//                Object serialized = CMILib.getInstance().getReflectionManager().textToIChatBaseComponent("[\"" + CMIChatColor.translate(displayName) + "\"]");
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, player.getName(), null, (IChatBaseComponent) serialized, null, false, null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
//                setField(pp1, "e", player.getName());
//                setField(pp1, "g", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = new net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective(
//                    net.minecraft.world.scores.DisplaySlot.b, sobj);
//                Field field = obj.getClass().getDeclaredField("c");
//                field.setAccessible(true);
//                field.set(obj, player.getName());
//                sendPacket(player, obj);
//
//                for (int i = 0; i < 15; i++) {
//                    if (i >= lines.size())
//                        break;
//                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(CMIChatColor.translate(lines.get(i)), player.getName(),
//                        15 - i, Optional.ofNullable(null), Optional.ofNullable(null));
//                    sendPacket(player, PacketPlayOutScoreboardScore);
//                }
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
//
//                Object serialized = CMILib.getInstance().getReflectionManager().textToIChatBaseComponent("[\"" + CMIChatColor.translate(displayName) + "\"]");
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = new net.minecraft.world.scores.ScoreboardObjective(null, player.getName(), null, (IChatBaseComponent) serialized, null, false, null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
//                setField(pp1, "d", player.getName());
//                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = new net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective(
//                    net.minecraft.world.scores.DisplaySlot.b, sobj);
//                Field field = obj.getClass().getDeclaredField("b");
//                field.setAccessible(true);
//                field.set(obj, player.getName());
//                sendPacket(player, obj);
//
//                Constructor<PacketPlayOutScoreboardScore> PacketPlayOutScoreboardScoreConstructor = net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore.class.getConstructor(
//                    String.class, String.class, int.class, IChatBaseComponent.class, NumberFormat.class);
//
//                for (int i = 0; i < 15; i++) {
//                    if (i >= lines.size())
//                        break;
//
//                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = PacketPlayOutScoreboardScoreConstructor.newInstance(CMIChatColor.translate(lines.get(i)),
//                        player.getName(), 15 - i, null, null);
//
//                    sendPacket(player, PacketPlayOutScoreboardScore);
//                }
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
//
//                Object serialized = null;
//                try {
//                    serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "[\"" + CMIChatColor.translate(displayName) + "\"]");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//                Constructor<ScoreboardObjective> cons = net.minecraft.world.scores.ScoreboardObjective.class.getConstructor(
//                    net.minecraft.world.scores.Scoreboard.class,
//                    String.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.class,
//                    net.minecraft.network.chat.IChatBaseComponent.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.class);
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = cons.newInstance(null, player.getName(), null, (IChatBaseComponent) serialized, null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
//                setField(pp1, "d", player.getName());
//                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = new net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective(
//                    net.minecraft.world.scores.DisplaySlot.b, sobj);
//                Field field = obj.getClass().getDeclaredField("b");
//                field.setAccessible(true);
//                field.set(obj, player.getName());
//                sendPacket(player, obj);
//
//                for (int i = 0; i < 15; i++) {
//                    if (i >= lines.size())
//                        break;
//                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore.class.getConstructor(
//                        net.minecraft.server.ScoreboardServer.Action.class, String.class, String.class, int.class)
//                        .newInstance(net.minecraft.server.ScoreboardServer.Action.a, player.getName(), null, 0);
//                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
//                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
//                    sendPacket(player, PacketPlayOutScoreboardScore);
//                }
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
//
//                Object serialized = null;
//                try {
//                    serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, "[\"" + CMIChatColor.translate(displayName) + "\"]");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//                Constructor<ScoreboardObjective> cons = net.minecraft.world.scores.ScoreboardObjective.class.getConstructor(
//                    net.minecraft.world.scores.Scoreboard.class,
//                    String.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.class,
//                    net.minecraft.network.chat.IChatBaseComponent.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.class);
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = cons.newInstance(null, player.getName(), null, (IChatBaseComponent) serialized, null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
//                setField(pp1, "d", player.getName());
//                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//                Constructor<PacketPlayOutScoreboardDisplayObjective> constructor = net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective.class.getConstructor(int.class,
//                    net.minecraft.world.scores.ScoreboardObjective.class);
//
//                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = constructor.newInstance(1, sobj);
//                Field field = obj.getClass().getDeclaredField("b");
//                field.setAccessible(true);
//                field.set(obj, player.getName());
//                sendPacket(player, obj);
//
//                for (int i = 0; i < 15; i++) {
//                    if (i >= lines.size())
//                        break;
//                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore.class.getConstructor(
//                        net.minecraft.server.ScoreboardServer.Action.class, String.class, String.class, int.class)
//                        .newInstance(net.minecraft.server.ScoreboardServer.Action.a, player.getName(), null, 0);
//                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
//                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
//                    sendPacket(player, PacketPlayOutScoreboardScore);
//                }
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
//
//                Constructor<ScoreboardObjective> cons = net.minecraft.world.scores.ScoreboardObjective.class.getConstructor(
//                    net.minecraft.world.scores.Scoreboard.class,
//                    String.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.class,
//                    net.minecraft.network.chat.IChatBaseComponent.class,
//                    net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.class);
//
//                net.minecraft.world.scores.ScoreboardObjective sobj = cons.newInstance(null, player.getName(), null,
//                    Class.forName("net.minecraft.network.chat.ChatComponentText").getConstructor(String.class).newInstance(CMIChatColor.translate(displayName)), null);
//
//                Object pp1 = net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective.class.getConstructor(sobj.getClass(), int.class).newInstance(sobj, 0);
//                setField(pp1, "d", player.getName());
//                setField(pp1, "f", net.minecraft.world.scores.criteria.IScoreboardCriteria.EnumScoreboardHealthDisplay.b);
//                sendPacket(player, pp1);
//
//                Constructor<PacketPlayOutScoreboardDisplayObjective> constructor = net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective.class.getConstructor(int.class,
//                    net.minecraft.world.scores.ScoreboardObjective.class);
//
//                net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective obj = constructor.newInstance(1, sobj);
//
//                Field field = obj.getClass().getDeclaredField("b");
//                field.setAccessible(true);
//                field.set(obj, player.getName());
//                sendPacket(player, obj);
//
//                for (int i = 0; i < 15; i++) {
//                    if (i >= lines.size())
//                        break;
//                    net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore PacketPlayOutScoreboardScore = net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore.class.getConstructor(
//                        net.minecraft.server.ScoreboardServer.Action.class, String.class, String.class, int.class)
//                        .newInstance(net.minecraft.server.ScoreboardServer.Action.a, player.getName(), null, 0);
//                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
//                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
//                    sendPacket(player, PacketPlayOutScoreboardScore);
//                }
//            } else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
//                Object pp1 = getNMSClass("PacketPlayOutScoreboardObjective").getConstructor().newInstance();
//                setField(pp1, "a", player.getName());
//                setField(pp1, "d", 0);
//                setField(pp1, "b", getNMSClass("ChatComponentText").getConstructor(String.class).newInstance(CMIChatColor.translate(displayName)));
//                setField(pp1, "c", getNMSClass("IScoreboardCriteria$EnumScoreboardHealthDisplay").getEnumConstants()[1]);
//                sendPacket(player, pp1);
//
//                Object d0 = getNMSClass("PacketPlayOutScoreboardDisplayObjective").getConstructor().newInstance();
//                setField(d0, "a", 1);
//                setField(d0, "b", player.getName());
//                sendPacket(player, d0);
//
//                for (int i = 0; i < 15; i++) {
//                    if (i >= lines.size())
//                        break;
//                    Object PacketPlayOutScoreboardScore = getNMSClass("PacketPlayOutScoreboardScore").getConstructor().newInstance();
//
//                    setField(PacketPlayOutScoreboardScore, "a", CMIChatColor.translate(lines.get(i)));
//                    setField(PacketPlayOutScoreboardScore, "b", player.getName());
//                    setField(PacketPlayOutScoreboardScore, "c", 15 - i);
//                    setField(PacketPlayOutScoreboardScore, "d", getNMSClass("ScoreboardServer$Action").getEnumConstants()[0]);
//                    sendPacket(player, PacketPlayOutScoreboardScore);
//                }
//            } else {
//                Object boards = getNMSClass("Scoreboard").getConstructor().newInstance();
//                Object obj = boards.getClass().getMethod("registerObjective", String.class, getNMSClass("IScoreboardCriteria"))
//                    .invoke(boards, objName, getNMSClass("ScoreboardBaseCriteria").getConstructor(String.class).newInstance("JobsDummy"));
//
//                obj.getClass().getMethod("setDisplayName", String.class).invoke(obj, CMIChatColor.translate(displayName));
//
//                sendPacket(player, getNMSClass("PacketPlayOutScoreboardObjective").getConstructor(obj.getClass(), int.class).newInstance(obj, 1));
//                sendPacket(player, getNMSClass("PacketPlayOutScoreboardObjective").getConstructor(obj.getClass(), int.class).newInstance(obj, 0));
//
//                sendPacket(player, getNMSClass("PacketPlayOutScoreboardDisplayObjective").getConstructor(int.class,
//                    getNMSClass("ScoreboardObjective")).newInstance(1, obj));
//
//                for (int i = 0; i < 15; i++) {
//                    if (i >= lines.size())
//                        break;
//
//                    Object packet2 = getNMSClass("ScoreboardScore").getConstructor(getNMSClass("Scoreboard"),
//                        getNMSClass("ScoreboardObjective"), String.class).newInstance(boards, obj, CMIChatColor.translate(lines.get(i)));
//                    packet2.getClass().getMethod("setScore", int.class).invoke(packet2, 15 - i);
//
//                    sendPacket(player, getNMSClass("PacketPlayOutScoreboardScore").getConstructor(getNMSClass("ScoreboardScore")).newInstance(packet2));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
    }

    private static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        return CMILib.getInstance().getReflectionManager().getMinecraftClass(nmsClassString);
    }
}
