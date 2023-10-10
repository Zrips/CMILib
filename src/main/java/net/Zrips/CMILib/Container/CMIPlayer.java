package net.Zrips.CMILib.Container;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Version.Version;

public class CMIPlayer {

    public static Player getByName(String name) {

        if (name.length() == 36) {
            try {
                UUID uuid = UUID.fromString(name);
                return Bukkit.getPlayer(uuid);
            } catch (Throwable e) {
            }
        }

        name = CMIChatColor.stripColor(name);

        Player player = Bukkit.getPlayerExact(name);

        if (player != null)
            return player;

        player = Bukkit.getPlayer(name);

        if (player != null)
            return player;

        for (Player one : Bukkit.getOnlinePlayers()) {
            if (one.getDisplayName() == null || one.getDisplayName().isEmpty())
                continue;
            if (CMIChatColor.stripColor(one.getDisplayName()).equalsIgnoreCase(name))
                return one;
        }

        return player;
    }

    public static double getMaxHealth(Player player) {
        if (Version.isCurrentEqualOrHigher(Version.v1_9_R1)) {
            return player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        }
        return player.getMaxHealth();
    }
}
