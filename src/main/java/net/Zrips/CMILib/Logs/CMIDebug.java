package net.Zrips.CMILib.Logs;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.commands.CommandsHandler;

public class CMIDebug {
//    private CMILogType type;
    private Object[] message;

    private static long fixedTime = 0L;

    public static void it() {
        fixedTime = System.nanoTime();
    }

    public static boolean isTestServer() {
        return Version.isTestServer();
    }

    public static double getIT() {
        return ((System.nanoTime() - fixedTime) / 100) / 10000D;
    }

    public static void d(Object... message) {

        if (!Version.isTestServer())
            return;

        Player player = Bukkit.getPlayer("Zrips");
        if (player == null || !player.isOnline())
            return;

        StringBuilder fullMessage = new StringBuilder();
        int i = 1;
        CMIChatColor cl = CMIChatColor.GRAY;

        if (message == null)
            fullMessage.append("null");
        else
            for (Object one : message) {
                i++;
                if (i >= 2) {
                    i = 0;
                    if (cl == CMIChatColor.GRAY)
                        cl = CMIChatColor.WHITE;
                    else
                        cl = CMIChatColor.GRAY;
                    fullMessage.append(cl);
                }
                if (one instanceof String[]) {
                    fullMessage.append(String.valueOf(Arrays.asList((String[]) one).toString()) + " ");
                } else
                    fullMessage.append(String.valueOf(one) + " ");
            }

        player.sendMessage(CMIChatColor.DARK_GRAY + "[CMID] " + CMIChatColor.DARK_AQUA + fullMessage.toString());
    }

    public static void cd(Object... message) {
        Player player = Bukkit.getPlayer("Zrips");
        if (player == null || !player.isOnline())
            return;

        StringBuilder fullMessage = new StringBuilder();

        if (message == null)
            fullMessage.append("null");
        else
            for (Object one : message) {
                if (one instanceof String[]) {
                    fullMessage.append(Arrays.asList((String[]) one).toString()).append(" ");
                } else
                    fullMessage.append(String.valueOf(one)).append(" ");
            }

        player.sendMessage(CMIChatColor.DARK_GRAY + "[CMID] " + CMIChatColor.DARK_AQUA + fullMessage.toString().replace("§", "&"));
    }

    public static void c(Object... message) {

        if (!Version.isTestServer())
            return;

        if (!CommandsHandler.enabledDebug)
            return;
        if (!Version.isTestServer())
            return;
        new CMIDebug(message);
    }

    public CMIDebug(String msg) {
        this((Object) msg);
    }

    public CMIDebug(
//	CMILogType type, 
            Object... message) {
        if (!CommandsHandler.enabledDebug)
            return;
//	this.type = type;

        this.message = message;

        CMILib.getInstance().getLogManager().print(this);
    }

//    public CMILogType getType() {
//	return type;
//    }
//
//    public void setType(CMILogType type) {
//	this.type = type;
//    }

    public String getMsg() {
        StringBuilder fullMessage = new StringBuilder();
        for (Object one : message) {
            if (!fullMessage.toString().isEmpty())
                fullMessage.append(" ");
            if (one instanceof String[]) {
                fullMessage.append(String.valueOf(Arrays.asList((String[]) one).toString()));
            } else
                fullMessage.append(String.valueOf(one));
        }
        return fullMessage.toString();
    }

    public void setMsg(Object... message) {
        this.message = message;
    }
}
