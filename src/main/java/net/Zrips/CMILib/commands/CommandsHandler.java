package net.Zrips.CMILib.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Chat.ChatEditorManager;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIArray;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Container.PageInfo;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Locale.Snd;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Permissions.CMILPerm;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.RawMessages.RawMessageManager;
import net.Zrips.CMILib.Version.Version;

public class CommandsHandler implements CommandExecutor {
    public static final String label = "cmil";
    private static String packagePath = "";
    private static Map<String, CMICommand> commands = new TreeMap<>();
    public static boolean enabledDebug = true;


    protected CMILib plugin;

    public CommandsHandler(CMILib plugin) {
        this.plugin = plugin;
        packagePath = this.getClass().getPackage().getName() + ".list";
    }

    public Boolean performCMICommand(CommandSender sender, Class<?> command, String... args) {
        CMICommand cmd = getCommands().get(command.getSimpleName().toLowerCase());
        if (cmd == null)
            return false;
        
        return performCMICommand(sender, cmd, args);
    }

    public Boolean performCMICommand(CommandSender sender, CMICommand cmd, String[] args) {
        if (cmd == null)
            return false;
        return cmd.getCmdClass().perform(plugin, new CMICommandSender(sender), args);
    }

    public Boolean performCMICommand(CommandSender sender, Class<?> command, String args) {
        CMICommand cmd = getCommands().get(command.getSimpleName().toLowerCase());
        if (cmd == null)
            return false;
        return performCMICommand(sender, cmd, args);
    }

    public Boolean performCMICommand(CommandSender sender, CMICommand cmd, String args) {
        if (cmd == null)
            return false;
        String[] ar = args.split(" ");
        return cmd.getCmdClass().perform(plugin, new CMICommandSender(sender), ar);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String l, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase(RawMessageManager.rmc))
            return false;

        CMICommandSender cmiSender = new CMICommandSender(sender);

        if (cmiSender.isCommandBlock()) {
            try {
                for (int i = 0; i < args.length; i++) {
                    String one = args[i];
                    List<Entity> ent = Bukkit.selectEntities(sender, one.toLowerCase());
                    if (ent.isEmpty())
                        continue;
                    if (one.equalsIgnoreCase("@p") || one.equalsIgnoreCase("@r")) {
                        args[i] = ent.get(0).getName();
                    }
                }
            } catch (Throwable ex) {
            }
        }

        if (!CMILPerm.command.hasPermission(cmiSender, false, 5000L)) {

            if (cmiSender.isPlayer()) {
                boolean showPerm = CMILibConfig.permisionOnError || CMILPerm.permisiononerror.hasPermission(sender, false, 5000L);
                RawMessage rm = new RawMessage();
                rm.addText(CMIMessages.getMsg(LC.info_NoPermission)).addHover(showPerm ? "&2" + CMILPerm.command.getPermission() : null);
                rm.show(sender);
            }

            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Snd snd = new Snd().setSender(console).setTarget(sender);
            CMIMessages.sendMessage(console, CMIMessages.getMsg(LC.info_NoPlayerPermission, snd, "[permission]", CMILPerm.command.getPermission()), false);
            return false;
        }

        if (args.length == 0) {
            return help(cmiSender, 1);
        }

        if ((args.length == 1 || args.length == 2) && (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help"))) {

            if (!CMILibConfig.showMainHelpPage) {
                CMIMessages.sendMessage(cmiSender, LC.info_NoCommand);
                return false;
            }

            int page = 1;
            if (args.length == 2)
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    CMIMessages.sendMessage(cmiSender, CMIMessages.getMsg(LC.info_UseInteger), false);
                    return true;
                }
            if (page < 1)
                page = 1;
            return help(cmiSender, page);
        }

        String cmd = args[0].toLowerCase();

        Cmd cmdClass = getCmdClass(cmd);

        if (cmdClass == null) {
            cmdClass = getBestCmdmatch(cmd);
            if (cmdClass != null) {
                String suffix = CMIArray.toString(args, 1);
                if (!suffix.isEmpty())
                    suffix = " " + suffix;
            }
        }

        if (cmdClass == null) {
            CMIMessages.sendMessage(cmiSender, LC.info_NoCommand);
            return false;
        }

        cmd = cmdClass.getClass().getSimpleName();

        if (!cmd.equalsIgnoreCase(args[0])) {
            RawMessage rm = new RawMessage();
            StringBuilder s = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (!s.toString().isEmpty())
                    s.append(" ");
                s.append(args[i]);
            }
            rm.addText(CMIMessages.getMsg(LC.info_cantFindCommand, "[%1]", args[0], "[%2]", cmd)).addHover(CMIMessages.getMsg(LC.info_Click)).addCommand(getLabel() + " " + cmd + " " + s);
            rm.show(sender);

            if (CMILibConfig.isSimilarCommandPrevention) {
                return false;
            }
        }

        if (!hasCommandPermission(cmiSender, cmd, 50L)) {
            if (cmiSender.isPlayer()) {

                CMICommand cm = getCommands().get(cmdClass.getClass().getSimpleName());
                if (cm != null && !cm.getAnottation().redirectClass().equals(Void.class)) {
                    Class<? extends Cmd> redirectClass = cm.getAnottation().redirectClass();
                    if (hasCommandPermission(cmiSender, redirectClass.getSimpleName(), 50L)) {
                        cmiSender.performCommand(getLabel() + " " + redirectClass.getSimpleName() + " " + CMIArray.toString(CMIArray.removeFirst(args)));
                        return true;
                    }
                }

                boolean showPerm = CMILibConfig.permisionOnError || CMILPerm.permisiononerror.hasPermission(sender, false, 5000L);

                RawMessage rm = new RawMessage();
                rm.addText(CMIMessages.getMsg(LC.info_NoPermission)).addHover(showPerm ? "&2" + label + ".command." + cmd : null);
                rm.show(sender);

                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Snd snd = new Snd().setSender(console).setTarget(sender);
                CMIMessages.sendMessage(console, CMIMessages.getMsg(LC.info_NoPlayerPermission, snd, "[permission]", label + ".command." + cmd), false);
            } else
                CMIMessages.sendMessage(sender, CMIMessages.getMsg(LC.info_NoPermission), false);

            return false;
        }

        String[] myArgs = CMIArray.removeFirst(args);
        if (myArgs.length > 0 && myArgs[myArgs.length - 1].equals("?")) {
            CMICommand cm = getCommands().get(cmdClass.getClass().getSimpleName());
            if (cm == null || !cm.getAnottation().ignoreHelpPage()) {
                sendUsage(cmiSender, cmd);
                return false;
            }
        }

        for (Method met : cmdClass.getClass().getMethods()) {
            if (!met.isAnnotationPresent(CAnnotation.class))
                continue;
            CAnnotation cs = met.getAnnotation(CAnnotation.class);

            if (cmiSender.isPlayer()) {
                int[] regVar = cs.regVar();
                List<Integer> list = new ArrayList<>();
                boolean more = true;
                for (int one : regVar) {
                    if (one < 0)
                        more = false;
                    list.add(one);
                }

                int size = myArgs.length;

                boolean good = true;

                if (list.contains(666)) {
                    CMIMessages.sendMessage(sender, LC.info_FromConsole, false);
                    return false;
                }

                if (list.contains(-size))
                    good = false;

                if (list.contains(size))
                    good = true;

                if (list.contains(-100) && size == 0)
                    good = false;

                if (more && !list.contains(size))
                    good = false;

                if (!good) {
                    sendUsage(cmiSender, cmdClass.getClass().getSimpleName(), false);
                    return false;
                }
            } else {

                int[] consoleVar = cs.consoleVar();
                List<Integer> list = new ArrayList<>();
                boolean more = true;
                for (int one : consoleVar) {
                    if (one < 0)
                        more = false;
                    list.add(one);
                }
                int size = myArgs.length;
                boolean good = true;

                if (list.contains(666)) {
                    CMIMessages.sendMessage(sender, LC.info_Ingame, false);
                    return false;
                }

                if (list.contains(-size))
                    good = false;

                if (list.contains(size))
                    good = true;

                if (list.contains(-100) && size == 0)
                    good = false;

                if (more && !list.contains(size))
                    good = false;
                if (!good) {
                    sendUsage(cmiSender, cmdClass.getClass().getSimpleName(), false);
                    return false;
                }

            }
        }

        if (myArgs.length > 0)
            myArgs[myArgs.length - 1] = myArgs[myArgs.length - 1].replace(ChatEditorManager.questionMarkReplacer, "");

        String ar = CMIArray.toString(myArgs, " ");
        ar = ar.isEmpty() ? "" : " " + ar;

        Boolean back = cmdClass.perform(plugin, cmiSender, myArgs);
        if (back != null && !back)
            sendUsage(cmiSender, cmd, false);

        return back == null || !back ? false : true;
    }

    public static boolean hasCommandPermission(CMICommandSender sender, String cmd, Long delay) {
        if (!sender.isPlayer())
            return true;
        if (CMILPerm.command_$1.hasPermission(sender, false, true, delay, "*"))
            return true;
//	if (sender.hasPermission(label + ".command.*"))
//	    return true;
        return CMILPerm.command_$1.hasPermission(sender, false, true, delay, cmd);
//	    return true;
//	return sender.hasPermission(label + ".command." + cmd);
    }

    private String getUsage(String cmd) {
        String baseCmd = label + " " + cmd;

        String cmdString = plugin.getLM().getMessage("command.help.output.cmdFormat", "[command]", baseCmd);
        String key = "command." + cmd + ".help.args";
        if (plugin.getLM().containsKey(key) && !plugin.getLM().getMessage(key).isEmpty()) {
            cmdString = cmdString.replace("[arguments]", " " + plugin.getLM().getMessage(key));
        } else
            cmdString = cmdString.replace("[arguments]", "");

        return cmdString;
    }

    public void sendUsage(CMICommandSender sender, String cmd) {
        sendUsage(sender, cmd, true);
    }

    public void sendUsage(CMICommandSender sender, String cmd, boolean explanation) {
        String message = CMIChatColor.YELLOW + plugin.getLM().getMessage("command.help.output.usage");
        message = message.replace("%usage%", getUsage(cmd));
        CMIMessages.sendMessage(sender, message, false);

        CMIMessages.sendMessage(sender, plugin.getLM().getMessage("command.help.output.helpPageDescription", "[description]", plugin.getLM().getMessage("command." + cmd + ".help.info")), false);
        if (explanation && plugin.getLM().containsKey("command." + cmd + ".help.explanation")) {
            if (plugin.getLM().isString("command." + cmd + ".help.explanation"))
                CMIMessages.sendMessage(sender, plugin.getLM().getMessage("command.help.output.explanation", "[explanation]", plugin.getLM().getMessage("command." + cmd + ".help.explanation")), false);
            else
                for (String one : plugin.getLM().getMessageList("command." + cmd + ".help.explanation")) {
                    CMIMessages.sendMessage(sender, plugin.getLM().getMessage("command.help.output.explanation", "[explanation]", one), false);
                }
        }
    }

    protected boolean help(CMICommandSender sender, int page) {

        Map<String, Integer> commands = getCommands(sender);
        if (commands.size() == 0) {
            CMIMessages.sendMessage(sender, LC.info_NoPermission.getLocale(), false);
            return true;
        }
//	plugin.sendMessage(sender, plugin.getLM().getMessage("command.help.output.title"), false);

        if (!CMILibConfig.CommandSorting)
            commands = sort(commands);

        PageInfo pi = new PageInfo(7, commands.size(), page);

        CMIMessages.sendMessage(sender, plugin.getLM().getMessage("command.help.output.title"), false);

        int i = -1;
        for (Entry<String, Integer> one : commands.entrySet()) {
            i++;
            if (i > pi.getEnd())
                break;
            if (!pi.isInRange(i))
                continue;

            String message = plugin.getLM().getMessage("command.help.output.cmdInfoFormat");

            message = message.replace("[command]", getUsage(one.getKey()));
            message = message.replace("[description]", plugin.getLM().getMessage("command." + one.getKey() + ".help.info"));

            CMIMessages.sendMessage(sender, message, false);
        }
        pi.ShowPagination(sender, "/" + label + " ?");
        return true;
    }

    public static List<String> getClassesFromPackage(String pckgname) throws ClassNotFoundException {
        List<String> result = new ArrayList<String>();
        try {
            for (URL jarURL : ((URLClassLoader) CMILib.class.getClassLoader()).getURLs()) {
                try {
                    result.addAll(getClassesInSamePackageFromJar(pckgname, jarURL.toURI().getPath()));
                } catch (URISyntaxException e) {
                }
            }
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)");
        }

        return result;
    }

    private static List<String> getClassesInSamePackageFromJar(String packageName, String jarPath) {
        JarFile jarFile = null;
        List<String> listOfCommands = new ArrayList<String>();
        try {
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                JarEntry entry = en.nextElement();
                String entryName = entry.getName();
                packageName = packageName.replace(".", "/");
                if (entryName != null && entryName.endsWith(".class") && entryName.startsWith(packageName)) {
                    String name = entryName.replace(packageName, "").replace(".class", "").replace("/", "");
                    if (name.contains("$"))
                        name = name.split("\\$")[0];
                    listOfCommands.add(name);
                }
            }
        } catch (Exception e) {
        } finally {
            if (jarFile != null)
                try {
                    jarFile.close();
                } catch (Exception e) {
                }
        }
        return listOfCommands;
    }

    public static Map<String, Integer> getCommands(CMICommandSender sender) {
        Map<String, Integer> temp = new TreeMap<String, Integer>();
        main: for (Entry<String, CMICommand> cmd : commands.entrySet()) {
            if (cmd.getValue().getAnottation().hidden())
                continue;
            if (cmd.getValue().getAnottation().test())
                continue;

            if (cmd.getValue().getEnabled() != null && !cmd.getValue().getEnabled())
                continue;
            if (cmd.getValue().getEnabled() == null) {
//		if (cmd.getValue().getAnottation().modules().length > 0) {
//		    for (String one : cmd.getValue().getAnottation().modules()) {
//			if (!CMIModule.isEnabled(one)) {
//			    cmd.getValue().setEnabled(false);
//			    continue main;
//			}
//		    }
//		}
                cmd.getValue().setEnabled(true);
            }

            if (sender != null && !hasCommandPermission(sender, cmd.getKey(), 5000L))
                continue;
            temp.put(cmd.getKey(), cmd.getValue().getAnottation().priority());
        }
        return temp;
    }

    public void fillCommands() {
        List<String> lm = new ArrayList<String>();
        HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
        try {
            lm = getClassesFromPackage(packagePath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (String one : lm) {
            Class<?> newclass = getClass(one);
            if (newclass != null)
                classes.put(one, newclass);
        }

        for (Entry<String, Class<?>> OneClass : classes.entrySet()) {
            for (Method met : OneClass.getValue().getMethods()) {
                if (!met.isAnnotationPresent(CAnnotation.class))
                    continue;

                String cmd = OneClass.getKey();
                try {
                    if (Cmd.class.isAssignableFrom(OneClass.getValue())) {
                        Cmd cmdClass;
                        cmdClass = (Cmd) OneClass.getValue().getConstructor().newInstance();
                        CMICommand cmiCommand = new CMICommand(cmdClass, cmd, met.getAnnotation(CAnnotation.class));

                        if (cmiCommand.getAnottation().test() && !Version.isTestServer())
                            continue;

                        commands.put(cmd.toLowerCase(), cmiCommand);
//			cmiCommand.setHidden(cmiCommand.getAnottation().hidden());
                    }
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    Bukkit.getConsoleSender().sendMessage("!!! " + cmd);
                    e.printStackTrace();
                }
                break;
            }
        }
        return;
    }

    private static Class<?> getClass(String cmd) {
        Class<?> nmsClass = null;
        if (cmd.toLowerCase().contains("notfinished"))
            return null;
        try {
            nmsClass = Class.forName(packagePath + "." + cmd.toLowerCase());
        } catch (Exception e) {
            CMIMessages.consoleMessage("&cFailed to load " + cmd.toLowerCase() + " command!");
            e.printStackTrace();
        }
        return nmsClass;
    }

    private Cmd getCmdClass(String cmdName) {
        CMICommand cmd = commands.get(cmdName.toLowerCase());
        Cmd cmdClass = null;
        if (cmd != null) {
            if (cmd.getAnottation().test() && !Version.isTestServer())
                return null;
            if (cmd.getEnabled() != null && !cmd.getEnabled()) {
                CMIMessages.consoleMessage("&5Someone tried to use command for disabled module (" + cmd.getName() + ")");
                return null;
            }
            if (cmd.getEnabled() == null) {
//		if (cmd.getAnottation().modules().length > 0) {
//		    for (String one : cmd.getAnottation().modules()) {
//			if (CMIModule.isEnabled(one))
//			    continue;
//			cmd.setEnabled(false);
//			plugin.consoleMessage("&5Someone tried to use command for disabled module (" + cmd.getName() + ")");
//			return null;
//		    }
//		}
                cmd.setEnabled(true);
            }
            cmdClass = cmd.getCmdClass();
        }

        return cmdClass;
    }

    private Cmd getBestCmdmatch(String cmdName) {
        if (CMILibConfig.SimilarCommandChecker > 0) {
            Cmd bestMatch = null;
            int value = cmdName.length() * 2;
            for (Entry<String, CMICommand> one : commands.entrySet()) {
                int d = distance(one.getKey(), cmdName);
                if (d > value)
                    continue;
                value = d;
                bestMatch = one.getValue().getCmdClass();
                if (value == 1)
                    break;
            }

            if (bestMatch != null) {
                double matchPercentage = (100 - ((value * 100D) / bestMatch.getClass().getSimpleName().length()));
                if (CMILibConfig.SimilarCommandChecker <= matchPercentage) {
                    return bestMatch;
                }
            }
        }

        return null;
    }

    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    private static Map<String, Integer> sort(Map<String, Integer> unsortMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static Map<String, CMICommand> getCommands() {
        return commands;
    }

    public static String getLabel() {
        return label;
    }

    public static String getCommandPrefix(String command) {

        if (command.startsWith("/"))
            command = command.substring(1);

        if (command.startsWith(getLabel() + " "))
            command = command.substring(getLabel().length() + 1);

        return getLabel() + " " + command;
    }
}
