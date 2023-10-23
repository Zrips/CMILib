package net.Zrips.CMILib.Messages;

import java.util.HashMap;
import java.util.regex.Pattern;

public enum CMIMultiMessageType {

    plain(""),
    actionBar("^(?i)(!actionbar!)"),
    timedActionBar("^(?i)(!actionbar:)(\\d+)(!)"),
    title("^(?i)(!title!)((.*?(?=!subtitle!))(!subtitle!))?(.*)"),
    toast("^(?i)(!toast!)( -t:([^\\s]+))?( -icon:([^\\s]+))?"),
    broadcast("^(?i)(!broadcast!)"),
    json("^(<T>)(.*)(<\\/T>)"),
    customText("^(?i)(!customtext:)(.+[^\\s])(!)"),
    bossBar("^(?i)(!bossBar:)(\\w+[^\\s])(-((\\d+.)?\\d+))?(!)");

    private Pattern patern;

    private static HashMap<String, CMIMultiMessageType> map = new HashMap<String, CMIMultiMessageType>();

    static {
        for (CMIMultiMessageType one : CMIMultiMessageType.values()) {
            map.put(one.toString().toLowerCase(), one);
        }
    }

    CMIMultiMessageType(String regex) {
        patern = Pattern.compile(regex);
    }

    public Pattern getRegex() {
        return patern;
    }

    public static CMIMultiMessageType getByName(String name) {
        return map.get(name.toLowerCase());
    }
}
