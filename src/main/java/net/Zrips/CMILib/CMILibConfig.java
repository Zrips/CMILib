package net.Zrips.CMILib;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIBiome;
import net.Zrips.CMILib.Container.CMIWorld;
import net.Zrips.CMILib.Enchants.CMIEnchantment;
import net.Zrips.CMILib.Entities.CMIEntity;
import net.Zrips.CMILib.Entities.CMIEntityType;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Images.CMIImage;
import net.Zrips.CMILib.Items.CMIItemStack;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Items.CMIPotionEffectType;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.commands.CAnnotation;
import net.Zrips.CMILib.commands.CMICommand;
import net.Zrips.CMILib.commands.Cmd;
import net.Zrips.CMILib.commands.CommandsHandler;

public class CMILibConfig {

    private CMILib plugin;

    public static String lang = "EN";
    public static boolean LanguageDownload = true;
    public static boolean autoUpdate = true;
    public static boolean permisionOnError = true;
    public static boolean isPermisionInConsole = true;
    public static boolean monochromeConsole = true;
    public static boolean showMainHelpPage = true;
    public static int SimilarCommandChecker = 75;
    public static boolean CommandSorting = true;
    public static boolean isSimilarCommandPrevention = false;
    public static boolean rmcConsoleLog = true;
//    public static boolean playerNameForItemStack = true;
//    public static boolean mobNameForItemStack = true;
    public static boolean OfficialHex = true;
    public static boolean QuirkyHex = true;
    public static List<String> mysterySpawners;

    public static boolean ExploitPatcherCheckItem = true;

    private CMIItemStack GUIEmptyField = null;
    private CMIItemStack GUIPreviousPage = null;
    private CMIItemStack GUINextPage = null;
    private CMIItemStack GUIMiddlePage = null;
    private CMIItemStack GUIClose = null;
    private CMIItemStack GUIInfo = null;

    private ConfigReader localeFile = null;
    private ConfigReader cfg = null;

    private LinkedHashMap<Biome, CMIBiome> biomeNames = new LinkedHashMap<Biome, CMIBiome>();

    public CMILibConfig(CMILib plugin) {
        this.plugin = plugin;
    }

    private static StringBuilder formStringBuilder(List<String> list) {
        StringBuilder header = new StringBuilder();
        for (String one : list) {
            header.append(System.lineSeparator());
            header.append(one);
        }
        return header;
    }

    public boolean load() {
        return load(false);
    }

    public boolean load(boolean isReload) {

        try {
            cfg = new ConfigReader(this.plugin, "config.yml");
        } catch (Exception e) {
            if (isReload) {
                CMIMessages.consoleMessage(
                    "&cSEVERE CONFIG FILE READ ERROR. Check your spelling! Changes will not be applied until you fix it or perform server restart which can result in config file reset");
                return false;
            }
            e.printStackTrace();
        }

        if (cfg == null)
            return false;

        String locale = "EN";
        File ff = new File("plugins" + File.separator + "CMI" + File.separator + "config.yml");
        if (ff.isFile()) {
            try {
                ConfigReader configReader = new ConfigReader(ff);
                locale = configReader.getC().getString("Language", "EN");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        cfg.addComment("Language", "Language file you want to use");
        lang = cfg.get("Language", locale).toUpperCase();

        cfg.addComment("LanguageDownload", "Defines if you want to auto download default locale files from github repository",
            "You can disable this if you are using EN or you already have your locale setup and you don't need to have other languages being downloaded");
        LanguageDownload = cfg.get("LanguageDownload", true);

        cfg.addComment("AutoUpdate", "When enabled plugin will try to keep CMILib up to date automatically");
        autoUpdate = cfg.get("AutoUpdate", false);

        cfg.addComment("ExploitPatcher.Placeholders.blocked.checkItem",
            "By default we are blocking PAPI %checkitem_...% placeholder to avoid potential serious issues with it",
            "Only disable this if you have dedicated protection for it");
        ExploitPatcherCheckItem = cfg.get("ExploitPatcher.Placeholders.blocked.checkItem", true);

        cfg.addComment("GlobalGui.EmptyField", "Defines item type in empty fields in GUI when its needed to be filled up");
        GUIEmptyField = CMILib.getInstance().getItemManager().getItem(cfg.get("GlobalGui.EmptyField", "BLACK_STAINED_GLASS_PANE"));
        if (GUIEmptyField == null)
            GUIEmptyField = new CMIItemStack(CMIMaterial.BLACK_STAINED_GLASS_PANE.newItemStack());
        GUIEmptyField.setDisplayName("&r ");

        cfg.addComment("GlobalGui.Pages.Previous", "Icon for UI previous page button");
        GUIPreviousPage = CMILib.getInstance().getItemManager().getItem(cfg.get("GlobalGui.Pages.Previous",
            "head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0="));
        if (GUIPreviousPage == null)
            GUIPreviousPage = new CMIItemStack(CMIMaterial.WHITE_WOOL.newItemStack());

        cfg.addComment("GlobalGui.Pages.Next", "Icon for UI next page button");
        GUINextPage = CMILib.getInstance().getItemManager().getItem(cfg.get("GlobalGui.Pages.Next",
            "head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19"));
        if (GUINextPage == null)
            GUINextPage = new CMIItemStack(CMIMaterial.GRAY_WOOL.newItemStack());

        cfg.addComment("GlobalGui.Pages.Middle", "Icon for UI information button");
        GUIMiddlePage = CMILib.getInstance().getItemManager().getItem(cfg.get("GlobalGui.Pages.Middle",
            "head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmEyYWZhN2JiMDYzYWMxZmYzYmJlMDhkMmM1NThhN2RmMmUyYmFjZGYxNWRhYzJhNjQ2NjJkYzQwZjhmZGJhZCJ9fX0="));
        if (GUIMiddlePage == null)
            GUIMiddlePage = new CMIItemStack(CMIMaterial.LIGHT_GRAY_WOOL.newItemStack());

        cfg.addComment("GlobalGui.Close", "Icon for UI close button");
        GUIClose = CMILib.getInstance().getItemManager().getItem(cfg.get("GlobalGui.Close",
            "head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzM4YWIxNDU3NDdiNGJkMDljZTAzNTQzNTQ5NDhjZTY5ZmY2ZjQxZDllMDk4YzY4NDhiODBlMTg3ZTkxOSJ9fX0="));
        if (GUIClose == null)
            GUIClose = new CMIItemStack(CMIMaterial.LIGHT_GRAY_WOOL.newItemStack());

        cfg.addComment("GlobalGui.Info", "Icon for UI info button");
        GUIInfo = CMILib.getInstance().getItemManager().getItem(cfg.get("GlobalGui.Info",
            "head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjcwNWZkOTRhMGM0MzE5MjdmYjRlNjM5YjBmY2ZiNDk3MTdlNDEyMjg1YTAyYjQzOWUwMTEyZGEyMmIyZTJlYyJ9fX0="));
        if (GUIInfo == null)
            GUIInfo = new CMIItemStack(CMIMaterial.LIGHT_GRAY_WOOL.newItemStack());

        cfg.addComment("Spawners.mysterySpawners", "List of spawners to pick from while using spawner:random variable");
        mysterySpawners = cfg.get("Spawners.mysterySpawners", Arrays.asList("skeleton", "zombie", "silverfish", "panda", "fox"));

        cfg.addComment("RMCCommands.ConsoleLog", "When enabled we will (where possible) include which command was performed when using rmc commands");
        rmcConsoleLog = cfg.get("RMCCommands.ConsoleLog", true);

//        cfg.addComment("Heads.PlayerName", "When processing head item stack we will apply owners name instead of letting game client to use its own", "This will change 'Zrips's head' to 'Head of Zrips'");
//        playerNameForItemStack = cfg.get("Heads.PlayerName", false);
//
//        cfg.addComment("Heads.MobName", "When processing head item stack we will apply monsters name instead of letting game client to use its own",
//            "This will change 'Zrips's head' to 'Head of Chicken' in case custom skin is applyed to it");
//        mobNameForItemStack = cfg.get("Heads.MobName", false);

        cfg.addComment("Images.Filler", "Symbol to be used to create image fields", "Color codes are NOT supported here",
            "This will take full effect after server restart due to some images being already cached");
        CMIImage.imageFiller = CMIChatColor.stripColor(cfg.get("Images.Filler", "⬛"));
        cfg.addComment("Images.EmptyFiller", "Symbol to be used to fill in empty image fields", "Color codes are supported here");
        CMIImage.imageEmptyFiller = CMIChatColor.translate(cfg.get("Images.EmptyFiller", "&7_|"));

        cfg.addComment("Colors.OfficialHex", "When enabled plugin will try to detect simplified hex color codes like #f6f6f6 or #ff6 in adition to {#f6f6f6} and {#red}",
            "Keep in mind that this adds extra checks and simplified format will not support gradients or named colors so you will still need to use more complex format for those");
        OfficialHex = cfg.get("Colors.OfficialHex", true);

        cfg.addComment("Colors.QuirkyHex", "When enabled plugin will try to detect quirky hex color codes like &#f6f6f6 or &#ff6 in adition to {#f6f6f6} and {#red}",
            "Keep in mind that this adds extra checks and quirky format will not support gradients or named colors so you will still need to use more complex format for those");
        QuirkyHex = cfg.get("Colors.QuirkyHex", true);

        cfg.save();
        return true;

    }

    // Language file
    public boolean LoadLang(String lang) {
        return LoadLang(lang, false);
    }

    List<String> CommentList = new ArrayList<String>(Arrays.asList(

        "The phrases support full color (hex) code, and some variables.",
        "Keep in mind that some variables will not work for certain lines.",
        "Just keep them where there are now and everything will be okay :)",
        "Some lines can have global variables set. For the player who will be affected. For example /heal Zrips then Zrips data will be used as variable",
        "[serverName] to show server name",
        "[playerName] to show target player name",
        "[playerDisplayName] to show target player display name",
        "[lvl] to show target player level",
        "[exp] to show target player total exp",
        "[hp] to show target player health",
        "[maxHp] to show target player max health",
        "[hunger] to show target player hunger level",
        "[gameMode] to show target player gamemode",
        "[prefix] to show target player prefix if possible",
        "[suffix] to show target player suffix if possible",
        "Sender is console or player who performs the command. For example Zrips performs /heal Zhax then Zrips data will be used",
        "[senderName] to show Sender player name",
        "[senderDisplayName] to show Sender player display name",
        "[senderLvl] to show Sender player level",
        "[senderExp] to show Sender player total exp",
        "[senderHp] to show Sender player health",
        "[senderMaxHp] to show Sender player max health",
        "[senderHunger] to show Sender player hunger level",
        "[senderGameMode] to show Sender player gamemode",
        "[senderPrefix] to show Sender player prefix if possible",
        "[senderSuffix] to show Sender player suffix if possible",
        "Source is player which is being used for extra info. For example Zrips performs /tp Zhax Zrips then Zhax data will be used as its location is being taken for new player location",
        "[sourceName] to show source player name",
        "[sourceDisplayName] to show source player display name",
        "[sourceLvl] to show source player level",
        "[sourceExp] to show source player total exp",
        "[sourceHp] to show source player health",
        "[sourceMaxHp] to show source player max health",
        "[sourceHunger] to show source player hunger level",
        "[sourceGameMode] to show source player gamemode",
        "[sourcePrefix] to show source player prefix if possible",
        "[sourceSuffix] to show source player suffix if possible",
        "***********************************************",
        "Some lines support the option to send them to custom places, like action bar, title, sub-title, or even create JSON/clickable messages",
        "If the line starts with !toast! then player will get toast message (advancement popup, only 1.12 and up). Some extra variables can be used to define type and icon. example: !toast! -t:goal -icon:paper Hello world!",
        "If the line starts with !actionbar! then player will get action bar message defined after this variable",
        "If the line starts with !actionbar:[seconds]! then player will get action bar message for a defined amount of time",
        "If the line starts with !broadcast! then everyone will receive message. You can add extra !toast! !actionbar! or !title! to send message for everyone to specific place, in example !broadcast!!title!",
        "If the line starts with !customtext:[cTextName]! then custom text will be taken by name provided and shown for player. In case it is used after !broadcast! then everyone who is online will get this custom text message",
        "If the line starts with !title! then player will get title message defined after this variable, in addition it can contain !subtitle! which will add subtitle message",
        "If the line starts with !bossbar:[name]-[timer]! then player will get bossbar message defined after this variable, in addition you can define how long this message will be visible. You need to define bossbar name which can be anything you want, but lines with same name will override each other to prevent stacking",
        "To include clickable messages: <T>Text</T><H>Hover text</H><C>command</C><SC>Suggested text</SC>",
        "<T> and </T> required, other is optional",
        "Use /n to break line",
        "To have more than one JSON message use <Next>",
        "<C> performs command as a player who clicked",
        "<CC> performs command from console once",
        "<CCI> performs command from console every time player clicks text",
        "<URL> includes url"));

    public boolean reloadLanguage() {
        boolean langLoaded = LoadLang("EN", true);
        if (!lang.equalsIgnoreCase("EN"))
            if (!LoadLang(lang, true))
                langLoaded = false;
        if (langLoaded) {
            plugin.getLM().setLang(lang);
            plugin.getLM().reload();
        }
        return langLoaded;
    }

    public boolean LoadLang(String lang, boolean isReload) {

//	copyOverTranslations();
//	importFromCMI();

        Long time = System.currentTimeMillis();

        File f = new File(plugin.getDataFolder(), "Translations" + File.separator + "Locale_" + lang + ".yml");

        try {
            this.localeFile = new ConfigReader(f);
        } catch (Exception e1) {
            if (isReload) {
                CMIMessages.consoleMessage(
                    "&cSEVERE LOCALE FILE READ ERROR. Check your spelling! Changes will not be applied until you fix it or perform server restart which can result in locale file reset to default");
                return false;
            }
        }

        if (localeFile == null)
            return false;

        if (lang.equalsIgnoreCase(CMILibConfig.lang))
            localeFile.setRecordContents(true);

        localeFile.options().copyDefaults(true);
        localeFile.options().header(formStringBuilder(CommentList).toString());

//	c.addComment("info.prefix", "use !prefix! in any locale line to automatically include this prefix");
        for (LC one : LC.values()) {
            if (one.getComments() != null && !one.getComments().isEmpty()) {
                for (String oneC : one.getComments()) {
                    localeFile.addComment(one.name().replace("_", "."), oneC);
                }
            }
            localeFile.get(one.name().replace("_", "."), one.getText());
        }

        HashMap<String, String> damageCauseList = new HashMap<String, String>();
        damageCauseList.put("BLOCK_EXPLOSION", "Explosion");
        damageCauseList.put("CONTACT", "Block Damage");
        damageCauseList.put("CUSTOM", "Unknown");
        damageCauseList.put("DRAGON_BREATH", "Dragon breath");
        damageCauseList.put("DROWNING", "Drowning");
        damageCauseList.put("ENTITY_ATTACK", "Entity attack");
        damageCauseList.put("ENTITY_EXPLOSION", "Explosion");
        damageCauseList.put("FALL", "Fall");
        damageCauseList.put("FALLING_BLOCK", "Falling block");
        damageCauseList.put("FIRE", "Fire");
        damageCauseList.put("FIRE_TICK", "Fire");
        damageCauseList.put("FLY_INTO_WALL", "Fly into wall");
        damageCauseList.put("HOT_FLOOR", "Magma block");
        damageCauseList.put("LAVA", "Lava");
        damageCauseList.put("LIGHTNING", "Lightning");
        damageCauseList.put("MAGIC", "Magic");
        damageCauseList.put("MELTING", "Melting");
        damageCauseList.put("POISON", "Poison");
        damageCauseList.put("PROJECTILE", "Projectile");
        damageCauseList.put("STARVATION", "Starvation");
        damageCauseList.put("SUFFOCATION", "Suffocation");
        damageCauseList.put("SUICIDE", "Suicide");
        damageCauseList.put("THORNS", "Thorns");
        damageCauseList.put("VOID", "Void");
        damageCauseList.put("WITHER", "Wither");

        Arrays.stream(DamageCause.values()).map(DamageCause::name).sorted().filter(name -> name != null).forEach(name -> {
            String path = "info.DamageCause." + name.toLowerCase();
            if (!damageCauseList.containsKey(name.toUpperCase())) {
                localeFile.get(path, name.toLowerCase().replace("_", " "));
                return;
            }
            localeFile.get(path, damageCauseList.get(name.toUpperCase()));
        });

        // Loading data
        CMIBiome.initialize();
        CMIEntity.initialize();
        CMIEnchantment.updateLocale();
        CMIPotionEffectType.loadLocalization();
        CMIWorld.initialize();

        // Command stuff
        localeFile.get("command.help.output.usage", "&eUsage: &7%usage%");
        localeFile.get("command.help.output.cmdInfoFormat", "[command] &f- &e[description]");
        localeFile.get("command.help.output.cmdFormat", "&6/[command]&f[arguments]");
        localeFile.get("command.help.output.helpPageDescription", "&e* [description]");
        localeFile.get("command.help.output.explanation", "&e * [explanation]");

//	c.get("command.help.output.title", "-");
        localeFile.get("command.help.output.title", "&e------ ======= &6Help&e &e======= ------");

        localeFile.get("command.nocmd.help.info", "&eShows all available commands");
        localeFile.get("command.nocmd.help.args", "");
        for (Entry<String, CMICommand> one : CommandsHandler.getCommands().entrySet()) {

            CMICommand cmd = one.getValue();

            if (cmd.getAnottation().hidden())
                continue;

            if (cmd.getAnottation().test())
                continue;

            CAnnotation cs = cmd.getAnottation();

            String info = cs.info();
            if (info != null)
                localeFile.get("command." + cmd.getName() + ".help.info", info);

            String args = cs.args();
            if (args != null)
                localeFile.get("command." + cmd.getName() + ".help.args", args);

            // new mutli tab
            String[] multiTab = cs.multiTab();
            if (multiTab.length > 0) {
                for (Object oneT : Arrays.asList(multiTab)) {
                    StringBuilder str = new StringBuilder();
                    str.append(cmd.getName());
                    str.append(" ");
                    str.append(oneT);
                    plugin.getTab().addTabComplete(str.toString());
                }
            } else {
                plugin.getTab().addTabComplete(cmd.getName());
            }

            List<String> explanation = Arrays.asList(cs.explanation());
            if (explanation != null && !explanation.isEmpty()) {
                localeFile.get("command." + cmd.getName() + ".help.explanation", explanation);
            }

            Cmd cm = cmd.getCmdClass();
            localeFile.setP(cmd.getName());
            cm.getExtra(localeFile);
            localeFile.resetP();

        }
        localeFile.save();
        CMIMessages.consoleMessage("Updated (" + lang + ") language file. Took &6" + (System.currentTimeMillis() - time) + "ms");
        localeFile = null;
        return true;
    }

    public void reload(CommandSender player) {

//	plugin.defaultLocaleDownloader();

        Long time = System.currentTimeMillis();
        boolean configLoaded = load(true);
        boolean langLoaded = reloadLanguage();
        plugin.getItemManager().loadLocale();
        CMIEntityType.cache.clear();
        CMIWorld.onDisable();

        if (configLoaded && langLoaded)
            plugin.info("reload", player, "feedback", "[ms]", (System.currentTimeMillis() - time));
        if (!configLoaded) {
            plugin.info("reload", player, "failedConfig");
        }
        if (!langLoaded) {
            plugin.info("reload", player, "failedLocale");
        }
    }

    public ConfigReader getLocaleConfig() {
        if (localeFile == null) {
            File f = new File(plugin.getDataFolder(), "Translations" + File.separator + "Locale_" + lang + ".yml");
            try {
                this.localeFile = new ConfigReader(f);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return localeFile;
    }

    public CMIItemStack getGUIEmptyField() {
        return GUIEmptyField;
    }

    public HashMap<Biome, CMIBiome> getBiomeNames() {
        return biomeNames;
    }

    public CMIItemStack getGUIPreviousPage() {
        return GUIPreviousPage;
    }

    public CMIItemStack getGUIMiddlePage() {
        return GUIMiddlePage;
    }

    public CMIItemStack getGUINextPage() {
        return GUINextPage;
    }

    public ConfigReader getConfigFile() {
        return cfg;
    }

    public void setConfigFile(ConfigReader cfg) {
        this.cfg = cfg;
    }

    public CMIItemStack getGUIClose() {
        return GUIClose;
    }

    public CMIItemStack getGUIInfo() {
        return GUIInfo;
    }

}
