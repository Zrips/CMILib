package net.Zrips.CMILib.Container;

import org.bukkit.entity.Player;

import net.Zrips.CMILib.Container.CMINumber;
import net.Zrips.CMILib.Version.Version;

public class CMIExperience {

    // xp calculation for one current lvl
    public static long getXpToLevelUpFrom(int level) {
        if (Version.isCurrentLower(Version.v1_8_R1)) {
            return level <= 16 ? 17 : level <= 31 ? 3 * level - 31 : 7L * level - 155;
        }
        return level <= 15 ? 2 * level + 7 : level <= 30 ? 5 * level - 38 : 9L * level - 158;
    }

    // total xp calculation based by lvl
    public static long getTotalExpToLevel(int level) {
        if (Version.isCurrentLower(Version.v1_8_R1)) {
            return level <= 16 ? 17 * level : level <= 31 ? ((3 * level * level) / 2) - ((59 * level) / 2) + 360 : ((7 * level * level) / 2) - ((303 * level) / 2) + 2220;
        }
        return (long) (level <= 16 ? (level * level) + (6 * level) : level <= 31 ? ((2.5 * level * level) - (40.5 * level) + 360) : (4.5 * level * level) - (162.5 * level) + 2220);
    }

    public static void setLevel(Player player, int level) {
        if (player == null)
            return;
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
        player.setLevel(level);
    }

    public static int levelFromExp(long exp) {
        return (int) Math.floor((Math.sqrt(72D * exp - 54215) + 325) / 18D);
    }

    public static void setExp(Player player, long exp) {
        if (player == null)
            return;
        exp = (long) CMINumber.clamp(exp, 0, Float.MAX_VALUE);

        if (Version.isCurrentLower(Version.v1_8_R1) || exp < Integer.MAX_VALUE) {
            player.setLevel(0);
            player.setExp(0);
            player.setTotalExperience(0);
            player.giveExp((int) exp);
            return;
        }

        int level = levelFromExp(exp);

        float leftOver = exp - getTotalExpToLevel(level);
        float delta = getXpToLevelUpFrom(level);

        player.setTotalExperience(0);
        player.setLevel(level);
        player.setExp((float) CMINumber.clamp(leftOver / delta, 0, 1));
    }

    public static void addExp(Player player, long exp) {
        if (player == null)
            return;
        setExp(player, getTotalExp(player) + exp);
    }

    public static void takeExp(Player player, long exp) {
        if (player == null)
            return;
        setExp(player, getTotalExp(player) - exp);
    }

    public static long getMissingExp(Player player) {
        if (player == null)
            return 0;
        return getXpToLevelUpFrom(player.getLevel()) - getExpForCurrentLevel(player);
    }

    public static int getLevel(Player player) {
        if (player == null)
            return 0;
        return player.getLevel();
    }

    public static long getExpForCurrentLevel(Player player) {
        if (player == null)
            return 0;
        return getTotalExp(player) - getTotalExpToLevel(player.getLevel());
    }

    public static long getTotalExp(Player player) {
        if (player == null)
            return 0;
        return (getTotalExpToLevel(player.getLevel()) + Math.round(getXpToLevelUpFrom(player.getLevel()) * player.getExp()));
    }
}
