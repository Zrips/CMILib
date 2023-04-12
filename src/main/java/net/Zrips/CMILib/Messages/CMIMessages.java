package net.Zrips.CMILib.Messages;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Permissions.CMILPerm;

public class CMIMessages {

    public static void info(Class<?> c, Player player, String path, Object... variables) {
        info(c.getSimpleName(), player, path, variables);
    }

    public static void info(Object c, Player player, String path, Object... variables) {
        info(c.getClass().getSimpleName(), player, path, variables);
    }

    public static void info(Object thi, CMICommandSender sender, String path, Object... variables) {
        info(thi.getClass().getSimpleName(), sender, path, variables);
    }

    public static void info(String c, CMICommandSender sender, String path, Object... variables) {
        if (sender != null) {
            String msg = CMILib.getInstance().getLM().getMessage("command." + c + ".info." + path, variables);
            if (!msg.isEmpty())
                sendMessage(sender, msg, false);
        }
    }

    public static void info(String c, Player player, String path, Object... variables) {
        if (player != null) {
            String msg = CMILib.getInstance().getLM().getMessage("command." + c + ".info." + path, variables);
            if (!msg.isEmpty())
                sendMessage(player, msg, false);
        }
    }

    public static String getIM(Class<?> c, String path, Object... variables) {
        return getIM(c.getSimpleName(), path, variables);
    }

    public static String getIM(Object c, String path, Object... variables) {
        return getIM(c.getClass().getSimpleName(), path, variables);
    }

    public static String getIM(String cmd, String path, Object... variables) {
        return CMILib.getInstance().getLM().getMessage("command." + cmd + ".info." + path, variables);
    }

    public static List<String> getIML(String cmd, String path, Object... variables) {
        return CMILib.getInstance().getLM().getMessageList("command." + cmd + ".info." + path, variables);
    }

    public static List<String> getIML(Object c, String path, Object... variables) {
        return getIML(c.getClass().getSimpleName(), path, variables);
    }

    public static void sendMessage(Object sender, LC lc, Object... variables) {
        if (sender == null)
            return;
        String msg = getMsg(lc, variables);
        if (msg == null || msg.isEmpty())
            return;
        msg = CMILib.getInstance().getLM().filterNewLine(msg);

        CMIMultiMessage multi = new CMIMultiMessage(msg);
        multi.setUpdateSnd(true);

        if (sender instanceof CMICommandSender) {
            sender = ((CMICommandSender) sender).getSender();
        }

        if (sender instanceof Player) {
            multi.show((Player) sender);
            return;
        }
        if (msg == null)
            return;
        ((CommandSender) sender).sendMessage(msg);
    }

    public static void sendMessage(Object sender, String msg) {
        sendMessage(sender, msg, true);
    }

    public static void sendMessage(Object sender, String msg, boolean updateSnd) {
        sendMessage(sender, msg, updateSnd, true);
    }

    public static void sendMessage(Object sender, String msg, boolean updateSnd, boolean translateColors) {
        sendMessage(sender, msg, updateSnd, translateColors, true);
    }

    public static void sendMessage(Object sender, String msg, boolean updateSnd, boolean translateColors, boolean translatePlaceholders) {
        
        if (sender == null)
            return;
        if (msg == null || msg.isEmpty())
            return;
                
        msg = CMILib.getInstance().getLM().filterNewLine(msg);

        CMIMultiMessage multi = new CMIMultiMessage(msg);
        multi.setTranslateColors(translateColors);
        multi.setUpdateSnd(updateSnd);
        multi.setTranslatePlaceholders(translatePlaceholders);

        if (sender instanceof CMICommandSender)
            sender = ((CMICommandSender) sender).getSender();

        if (sender instanceof Player) {
            multi.show((Player) sender);
        } else {
            if (CMILibConfig.monochromeConsole)
                msg = CMIChatColor.stripColor(msg);
            multi = new CMIMultiMessage(msg);
            multi.setTranslateColors(translateColors);
            multi.setUpdateSnd(updateSnd);
            multi.setTranslatePlaceholders(translatePlaceholders);
            multi.show((CommandSender) sender);
        }
    }

    public static int broadcastMessage(String msg) {
        return broadcastMessage(null, msg, false, null, null);
    }

    public static int broadcastMessage(CommandSender sender, String msg) {
        return broadcastMessage(sender, msg, true, null, null);
    }

    public static int broadcastMessage(CommandSender sender, CMILPerm perm, String msg) {
        return broadcastMessage(sender, msg, true, perm, null);
    }

    public static int broadcastMessage(CommandSender sender, String msg, boolean showForsender) {
        return broadcastMessage(sender, msg, showForsender, null, null);
    }

    public static int broadcastMessage(CommandSender sender, String msg, boolean showForsender, Set<Player> ignorePlayers) {
        return broadcastMessage(sender, msg, showForsender, null, ignorePlayers);
    }

    public static int broadcastMessage(CommandSender sender, String msg, boolean showForsender, CMILPerm perm, Set<Player> ignorePlayers) {
        if (msg == null || msg.isEmpty())
            return 0;
        msg = CMIChatColor.translate(msg);
        int i = 0;
        msg = CMILib.getInstance().getLM().filterNewLine(msg);

        for (Player one : Bukkit.getOnlinePlayers()) {
            if (sender != null && sender.getName().equals(one.getName()) && !showForsender)
                continue;
            if (ignorePlayers != null && ignorePlayers.contains(one))
                continue;
            if (perm != null && perm.hasPermission(one) || perm == null) {
                i++;
                CMIMultiMessage multi = new CMIMultiMessage(msg);
                multi.show(one);
            }
        }

        i++;
        return i;
    }

    public static void consoleMessage(String message) {    
        if (message == null || message.isEmpty())
            return;
        Bukkit.getConsoleSender().sendMessage(CMIChatColor.translate(message));
    }

    public static void sendMessage(CommandSender sender, String message) {           
        sendMessage(sender, message, true, true, true);
    }

    public static String getMsg(LC lc, Object... variables) {

        LC f = null;
        if (variables.length > 0 && variables[0] instanceof LC) {
            f = lc;
            lc = (LC) variables[0];
            variables = Arrays.copyOfRange(variables, 1, variables.length);
        }

        boolean list = false;

        if (lc == null)
            return "";

        if (f == null) {
            list = CMILib.getInstance().getLM().isList(lc.getPt());
            if (!list) {
                return CMILib.getInstance().getLM().getMessage(lc.getPt(), variables);
            }
            List<String> ls = CMILib.getInstance().getLM().getMessageList(lc.getPt(), variables);
            StringBuilder msg = new StringBuilder();
            for (int i = 0; i < ls.size(); i++) {
                msg.append(ls.get(i));
                if (i < ls.size())
                    msg.append("\n");
            }
            return msg.toString();
        }

        list = CMILib.getInstance().getLM().isList(f.getPt());
        if (!list) {
            return CMILib.getInstance().getLM().getMessage(f.getPt()) + CMILib.getInstance().getLM().getMessage(lc.getPt(), variables);
        }
        List<String> ls = CMILib.getInstance().getLM().getMessageList(f.getPt(), variables);
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < ls.size(); i++) {
            msg.append(ls.get(i));
            if (i < ls.size())
                msg.append("\n");
        }

        return CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(msg.toString());
    }
}
