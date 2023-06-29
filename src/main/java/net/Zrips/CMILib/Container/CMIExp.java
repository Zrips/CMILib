package net.Zrips.CMILib.Container;

import org.bukkit.entity.Player;

import net.Zrips.CMILib.Version.Version;

public class CMIExp {

    // total xp calculation based by lvl
    public static int levelToExp(int level) {
        if (Version.isCurrentLower(Version.v1_8_R1)) {
            if (level <= 15) {
                return 17 * level;
            } else if (level <= 30) {
                return (3 * level * level / 2) - (59 * level / 2) + 360;
            } else {
                return (7 * level * level / 2) - (303 * level / 2) + 2220;
            }
        }
        if (level <= 15) {
            return level * level + 6 * level;
        } else if (level <= 30) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }

    }

    // xp calculation for one current lvl
    public static int deltaLevelToExp(int level) {
        if (Version.isCurrentLower(Version.v1_8_R1)) {
            if (level <= 16) {
                return 17;
            } else if (level <= 31) {
                return 3 * level - 31;
            } else {
                return 7 * level - 155;
            }
        }
        if (level <= 15) {
            return 2 * level + 7;
        } else if (level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }

    public static int currentlevelxpdelta(Player player) {
        return deltaLevelToExp(player.getLevel()) - ((levelToExp(player.getLevel()) + Math.round(deltaLevelToExp(player.getLevel()) * player.getExp())) - levelToExp(player.getLevel()));
    }

    public static int getPlayerExperience(Player player) {
        return (int) (levelToExp(player.getLevel()) + Math.floor(deltaLevelToExp(player.getLevel()) * player.getExp()));
    }

    public static void setTotalExperience(Player player, int xp) {

        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);

        
        if (xp < 1)
            return;
        
        player.giveExp(xp);

        int xpForLevel = levelToExp(player.getLevel());
        int delta = deltaLevelToExp(player.getLevel());

        player.setExp((xp - xpForLevel) / Float.valueOf(delta));
    }

    public static int expToLevel(double sourceleftexp, double d) {
        if (d > 21863)
            d = 21863;
        for (int i = 1; i <= d + 1; i++) {
            double levelexp = levelToExp(i);
            if (levelexp > sourceleftexp)
                return i - 1;
        }
        return 0;
    }

}
