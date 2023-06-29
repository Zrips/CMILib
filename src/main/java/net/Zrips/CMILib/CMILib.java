package net.Zrips.CMILib;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.Zrips.CMILib.BossBar.BossBarListener;
import net.Zrips.CMILib.BossBar.BossBarManager;
import net.Zrips.CMILib.Chat.ChatEditorListener;
import net.Zrips.CMILib.GUI.GUIListener;
import net.Zrips.CMILib.GUI.GUIManager;
import net.Zrips.CMILib.Items.ItemManager;
import net.Zrips.CMILib.Locale.Language;
import net.Zrips.CMILib.Logs.CMILogManager;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Placeholders.Placeholder;
import net.Zrips.CMILib.Placeholders.PlaceholderAPIHook;
import net.Zrips.CMILib.RawMessages.RawMessageListener;
import net.Zrips.CMILib.Shadow.ShadowCommandListener;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.commands.CommandsHandler;

public class CMILib extends JavaPlugin {
    private static final UUID ServerUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID emptyUserUUID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");
    private static final UUID fakeUserUUID = UUID.fromString("ffffffff-ffff-ffff-ffff-fffffffffff0");
    private static final String fakeUserName = "CMIFakeOperator";

    public static final String translationsFolderName = "Translations";
    public static final String itemsFolderName = "Items";

    private static CMILib instance;

    private boolean cmiPresent = false;

    private Reflections ref = null;
    private Language languageManager = null;
    private CMILibConfig config = null;
    private ItemManager itemManager = null;
    private TabComplete tab = null;
    private CommandsHandler cManager = null;
    private Placeholder Placeholder = null;
    private CMILogManager logManager;
    private GUIManager GUIManager;
    private BossBarManager BossBarManager;

    private boolean PlaceholderAPIEnabled = false;

    public static CMILib getInstance() {
        return instance;
    }

    public GUIManager getGUIManager() {
        if (GUIManager == null)
            GUIManager = new GUIManager(this);
        return GUIManager;
    }

    public BossBarManager getBossBarManager() {
        if (BossBarManager == null)
            BossBarManager = new BossBarManager(this);
        return BossBarManager;
    }

    public CMILogManager getLogManager() {
        if (logManager == null)
            logManager = new CMILogManager();
        return logManager;
    }

    public Reflections getReflectionManager() {
        if (ref == null)
            ref = new Reflections(this);
        return ref;
    }

    public Language getLM() {
        if (languageManager == null) {
            languageManager = new Language(this, CMILibConfig.lang);
            languageManager.reload();
        }
        return languageManager;
    }

    public CMILibConfig getConfigManager() {
        if (config == null)
            config = new CMILibConfig(this);
        return config;
    }

    public TabComplete getTab() {
        if (tab == null)
            tab = new TabComplete(this);
        return tab;
    }

    public ItemManager getItemManager() {
        if (itemManager == null)
            itemManager = new ItemManager(getInstance());
        return itemManager;
    }

    public CommandsHandler getCommandManager() {
        if (cManager == null)
            cManager = new CommandsHandler(this);
        return cManager;
    }

    private int cmiLibResourceId = 87610;

    private void cleanOldFiles() {

        File folder = new File("plugins");
        if (!folder.isDirectory())
            return;

        Integer cmilibVersion = Version.convertVersion(getDescription().getVersion());

        for (File one : folder.listFiles()) {
            if (!one.isFile())
                continue;
            if (!one.getName().toLowerCase().startsWith("cmilib"))
                continue;

            if (!one.getName().toLowerCase().endsWith(".jar"))
                continue;

            String version = one.getName().substring("cmilib".length());
            version = version.substring(0, version.length() - 4);

            if (version.length() < 7 || !version.contains(".")) {
                continue;
            }

            Integer oldVersion = Version.convertVersion(version);

            if (oldVersion == 0 || cmilibVersion <= oldVersion) {
                continue;
            }

            try {
                one.delete();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }

    public void defaultLocaleDownloader() {
        try {
            List<String> lang = Arrays.asList("CN", "DE", "ES", "IT", "LT", "RU", "SK", "SL", "FR", "PL", "NO", "ZH", "TR", "CZ");
            String lr = null;

            for (String one : lang) {
                File file = new File(getDataFolder() + File.separator + "Translations", "Locale_" + one + ".yml");
                if (!file.isFile()) {
                    lr = one;
                }
            }

            final String flr = lr;

            for (String one : lang) {
                File file = new File(getDataFolder() + File.separator + "Translations", "Locale_" + one + ".yml");
                if (!file.isFile()) {
                    FileDownloader downloader = new FileDownloader() {
                        @Override
                        public void afterDownload() {
                            CMIMessages.consoleMessage("Downloaded Locale_" + one + ".yml file");
                            if (flr != null && one.equalsIgnoreCase(flr))
                                getConfigManager().reloadLanguage();
                        }
                    };
                    downloader.downloadUsingStream("https://raw.githubusercontent.com/Zrips/CMILib/master/Translations/Locale_" + one + ".yml", file.getPath(), true);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void defaultItemLocaleDownloader() {
        try {
            List<String> lang = Arrays.asList("ES");
            String lr = null;

            for (String one : lang) {
                File file = new File(getDataFolder() + File.separator + "Translations" + File.separator + "Items", "items_" + one + ".yml");
                if (!file.isFile()) {
                    lr = one;
                }
            }

            final String flr = lr;

            for (String one : lang) {
                File file = new File(getDataFolder() + File.separator + "Translations" + File.separator + "Items", "items_" + one + ".yml");
                if (!file.isFile()) {
                    FileDownloader downloader = new FileDownloader() {
                        @Override
                        public void afterDownload() {
                            CMIMessages.consoleMessage("Downloaded items_" + one + ".yml file");
                            if (flr != null && one.equalsIgnoreCase(flr))
                                getConfigManager().reloadLanguage();
                        }
                    };
                    downloader.downloadUsingStream("https://raw.githubusercontent.com/Zrips/CMILib/master/Translations/Items/items_" + one + ".yml", file.getPath(), true);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
      
        instance = this;
        this.getItemManager().load();
        this.getConfigManager().load();
        this.getItemManager().loadLocale();

        PluginManager pm = getServer().getPluginManager();

        defaultLocaleDownloader();
        defaultItemLocaleDownloader();

        if (CMILibConfig.autoUpdate && CMILibConfig.autoFileRemoval)
            cleanOldFiles();

        new updateChecker(this, cmiLibResourceId).getVersion(version -> {

            Integer cmilibVersion = Version.convertVersion(getDescription().getVersion());

            Integer cmilVersion = Version.convertVersion(version);

            if (cmilVersion > cmilibVersion) {

                if (!CMILibConfig.autoUpdate) {
                    CMIMessages.consoleMessage("&2New version of CMILib was detected. Please update it");
                    return;
                }
                File file = new File(getDataFolder().getParent(), "CMILib" + version + ".jar");
                if (!file.isFile()) {
                    FileDownloader downloader = new FileDownloader() {
                        @Override
                        public void afterDownload() {
                            CMIMessages.consoleMessage("&2Downloaded CMILib" + version + " file");

                            File dir = new File(getDataFolder() + File.separator + "Libs");
                            if (dir.isDirectory()) {
                                for (File one : dir.listFiles()) {
                                    if (one.getName().contains("CMILib") && one.getName().endsWith(".jar")) {
                                        File dest = new File(getDataFolder().getParent(), one.getName());
                                        if (!dest.exists())
                                            one.renameTo(dest);
                                        break;
                                    }
                                }
                            }
                            CMIMessages.consoleMessage("&2Please restart server for this to take full effect");
                        }

                        @Override
                        public void failedDownload() {
                            CMIMessages.consoleMessage("&eThe attempt to automatically download CMILib has failed");
                            CMIMessages.consoleMessage("&eSome plugins might not work properly without the newest version");
                            CMIMessages.consoleMessage("&eYou can download it manually from: https://www.zrips.net/CMILib/CMILib" + version + ".jar");
                        }
                    };
                    downloader.downloadUsingStream("https://www.zrips.net/CMILib/CMILib" + version + ".jar", file.getPath(), false);
                }
            }
        });

        setupCMIAPI();
        setupPlaceHolderAPI();

        this.getCommand(CommandsHandler.getLabel()).setExecutor(getCommandManager());
        this.getCommand(CommandsHandler.getLabel()).setTabCompleter(getTab());

        pm.registerEvents(new ChatEditorListener(this), this);
        pm.registerEvents(new BossBarListener(this), this);
        pm.registerEvents(new GUIListener(this), this);
        pm.registerEvents(new RawMessageListener(), this);
        pm.registerEvents(new ShadowCommandListener(), this);
        getCommandManager().fillCommands();

        getConfigManager().LoadLang("EN");
        if (!CMILibConfig.lang.equalsIgnoreCase("EN")) {
            getConfigManager().LoadLang(CMILibConfig.lang);
        }      
    }

    @Override
    public void onDisable() {

        try {
            if (GUIManager != null)
                getGUIManager().closeAll();
        } catch (Throwable e) {
        }
    }

    private boolean setupPlaceHolderAPI() {
        if (!getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            return false;
        if (new PlaceholderAPIHook(this).register())
            CMIMessages.consoleMessage("PlaceholderAPI hooked.");
        PlaceholderAPIEnabled = true;
        return true;
    }

    private boolean setupCMIAPI() {
        if (getServer().getPluginManager().getPlugin("CMI") == null)
            return false;
        CMIMessages.consoleMessage("CMI hooked.");
        cmiPresent = true;
        return true;
    }

    public boolean isPlaceholderAPIEnabled() {
        return PlaceholderAPIEnabled;
    }

    public Placeholder getPlaceholderAPIManager() {
        if (Placeholder == null)
            Placeholder = new Placeholder(this);
        return Placeholder;
    }

    public UUID getServerUUID() {
        return ServerUUID;
    }

    public static String getFakeUserName() {
        return fakeUserName;
    }

    public boolean isCmiPresent() {
        return cmiPresent;
    }

    public void info(Class<?> c, Player player, String path, Object... variables) {
        info(c.getSimpleName(), player, path, variables);
    }

    public void info(Object c, Player player, String path, Object... variables) {
        info(c.getClass().getSimpleName(), player, path, variables);
    }

    public void info(Object thi, CommandSender sender, String path, Object... variables) {
        info(thi.getClass().getSimpleName(), sender, path, variables);
    }

    public void info(String c, CommandSender sender, String path, Object... variables) {
        if (sender != null) {
            String msg = getLM().getMessage("command." + c + ".info." + path, variables);
            if (!msg.isEmpty())
                CMIMessages.sendMessage(sender, msg, false);
        }
    }

    public void info(String c, Player player, String path, Object... variables) {
        if (player != null) {
            String msg = getLM().getMessage("command." + c + ".info." + path, variables);
            if (!msg.isEmpty())
                CMIMessages.sendMessage(player, msg, false);
        }
    }

    public void loadMessage(Integer amount, String type, Long took) {
        if (this.isEnabled())
            CMIMessages.consoleMessage("&3Loaded (&f" + amount + "&3) &7" + type + " &3into memory. &6Took &e" + took + "&6ms");
    }
}
