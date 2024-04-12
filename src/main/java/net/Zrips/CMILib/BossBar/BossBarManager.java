
package net.Zrips.CMILib.BossBar;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CommandType;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.commands.CMICommand;

public class BossBarManager {

    CMILib plugin;

    public BossBarManager(CMILib plugin) {
        this.plugin = plugin;
    }

    HashMap<String, BossBarInfo> globalBars = new HashMap();

    ConcurrentHashMap<UUID, ConcurrentHashMap<String, BossBarInfo>> barMap = new ConcurrentHashMap<UUID, ConcurrentHashMap<String, BossBarInfo>>();

    public void addGlobalBar(BossBarInfo binfo) {
        if (binfo.isGlobal()) {
            BossBarInfo old = globalBars.get(binfo.getNameOfBar().replaceAll("[^a-zA-Z0-9]", ""));
            if (old != null) {
                old.cancelAutoScheduler();
                old.cancelHideScheduler();
            }
            globalBars.put(binfo.getNameOfBar().replaceAll("[^a-zA-Z0-9]", ""), binfo);
        }
        Show(binfo);
    }

    public void removeGlobalBossbar(BossBarInfo bar) {
        removeGlobalBossbar(bar.getNameOfBar());
    }

    public void removeGlobalBossbar(String barName) {
        BossBarInfo ret = globalBars.remove(barName.replaceAll("[^a-zA-Z0-9]", ""));
        if (ret != null) {
            ret.cancelAutoScheduler();
            ret.cancelHideScheduler();
        }
    }

    public void updateGlobalBars(Player player) {

        for (Entry<String, BossBarInfo> iter : new HashMap<String, BossBarInfo>(globalBars).entrySet()) {
            BossBarInfo binfo = iter.getValue();

            if (!binfo.stillRunning()) {
                globalBars.remove(iter.getKey());
                continue;
            }

            BossBarInfo clone = binfo.clone(player);
//	    clone.setNameOfBar(null);
            clone.setBar(null);

            getBossBarInfo(player).remove(clone.getNameOfBar().toLowerCase());

            addBossBar(player, clone);

        }

    }

    public synchronized void updateBossBars(Player player) {
        if (Version.isCurrentLower(Version.v1_9_R1))
            return;
        if (player == null)
            return;

        HashMap<String, BossBarInfo> temp = new HashMap<String, BossBarInfo>(getBossBarInfo(player));
        for (Entry<String, BossBarInfo> one : temp.entrySet()) {
            Show(one.getValue());
        }
    }

    public synchronized void Show(final BossBarInfo barInfo) {
        if (Version.isCurrentLower(Version.v1_9_R1))
            return;

        barInfo.cancelAutoScheduler();
        barInfo.cancelHideScheduler();

        barInfo.setAutoId(CMIScheduler.scheduleSyncRepeatingTask(new Runnable() {
            @Override
            public void run() {

                final Player player = barInfo.getPlayer();
//		barInfo.cancelHideScheduler();

                if (!barInfo.isGlobal() && (player == null || !player.isOnline())) {
                    barInfo.setMakeVisible(true);
                }

                if (barInfo.isGlobal() && player != null && !player.isOnline()) {
                    barInfo.remove();
                    return;
                }

                barInfo.updateCycle();

                BossBar bar = barInfo.getBar();

                String name = player == null ? barInfo.getTitleOfBar() : barInfo.getTitleOfBar(player);

                boolean isNew = true;
                if (bar == null) {
                    BarColor color = barInfo.getColor();
                    if (color == null)
                        if (player == null)
                            color = BarColor.GREEN;
                        else {
                            ConcurrentHashMap<String, BossBarInfo> info = getBossBarInfo(player);
                            int size = 1;
                            if (info != null)
                                size = info.size();
                            switch (size) {
                            case 1:
                                color = BarColor.GREEN;
                                break;
                            case 2:
                                color = BarColor.RED;
                                break;
                            case 3:
                                color = BarColor.WHITE;
                                break;
                            case 4:
                                color = BarColor.YELLOW;
                                break;
                            case 5:
                                color = BarColor.PINK;
                                break;
                            case 6:
                                color = BarColor.PURPLE;
                                break;
                            default:
                                color = BarColor.BLUE;
                                break;
                            }
                        }
                    bar = Bukkit.createBossBar(name, color, barInfo.getStyle() != null ? barInfo.getStyle() : BarStyle.SEGMENTED_10);

                    barInfo.setBar(bar);
                } else {

                    bar.setTitle(name);
                    if (barInfo.getStyle() != null)
                        bar.setStyle(barInfo.getStyle());
                    if (barInfo.getColor() != null)
                        bar.setColor(barInfo.getColor());
                    bar.setVisible(true);
                    isNew = false;
                }

                Double percentage = barInfo.getPercentage();
                if (percentage == null)
                    percentage = 1D;

                if (barInfo.getAdjustPerc() != null) {
                    Double curP = barInfo.getPercentage();

                    if (curP != null && curP + barInfo.getAdjustPerc() <= 0 && barInfo.getAdjustPerc() < 0 || curP != null && curP + barInfo.getAdjustPerc() >= 1 && barInfo.getAdjustPerc() > 0) {

                        if (barInfo.getCommands() != null && barInfo.getHideScheduler() == null) {
                            try {
                                if (barInfo.getAdjustPerc() < 0)
                                    curP = 0D;
                                else
                                    curP = 1D;
                                barInfo.setPercentage(curP);
                                bar.setProgress(curP);
                                bar.setTitle(name);
                            } catch (Throwable e) {
                            }
                            CMIScheduler.get().runTask(() -> {
                                if (!barInfo.timerRunOut())
                                    return;

                                if (CMILib.getInstance().isCmiPresent()) {
                                    if (player != null) {
                                        com.Zrips.CMI.CMI.getInstance().getSpecializedCommandManager().processCmds(barInfo.getCommands(player), player);
                                    } else {
                                        com.Zrips.CMI.CMI.getInstance().getSpecializedCommandManager().processCmds(barInfo.getCommands(null), Bukkit.getConsoleSender());
                                    }
                                } else {
                                    if (player != null) {
                                        CMICommand.performCommand(player, barInfo.getCommands(player), CommandType.bossbar);
                                    } else {
                                        CMICommand.performCommand(Bukkit.getConsoleSender(), barInfo.getCommands(null), CommandType.bossbar);
                                    }
                                }
                            });
                        }

                        if (barInfo.getHideScheduler() == null)
                            barInfo.setHideScheduler(CMIScheduler.get().runTaskLater(() -> {
                                CMIBossBarHideEvent ev = new CMIBossBarHideEvent(barInfo);
                                Bukkit.getServer().getPluginManager().callEvent(ev);
                                if (!ev.isCancelled()) {
                                    barInfo.getBar().setVisible(false);
                                    barInfo.cancelAutoScheduler();
                                    removeGlobalBossbar(barInfo);
                                    barInfo.remove();
                                    barInfo.setHideScheduler(null);
                                }
                            }, barInfo.getKeepFor()));
//			barInfo.cancelAutoScheduler();
//			return;

                    }
                    if (curP == null)
                        if (barInfo.getAdjustPerc() > 0)
                            curP = 0D;
                        else
                            curP = 1D;
                    curP += barInfo.getAdjustPerc();
                    barInfo.setPercentage(curP);
                } else
                    barInfo.setPercentage(percentage);

                try {
                    bar.setProgress(barInfo.getPercentage());
                    bar.setTitle(name);
                    if (player != null && player.isOnline()) {

                        getBossBarInfo(player).put(barInfo.getNameOfBar().toLowerCase(), barInfo);
                        if (isNew || barInfo.isMakeVisible() && getBossBar(player, barInfo.getNameOfBar()) == null) {
                            bar.addPlayer(player);
                            bar.setVisible(true);
                            barInfo.setMakeVisible(false);

                        } else {
                            if (barInfo.isMakeVisible() && getBossBar(player, barInfo.getNameOfBar()) != null) {
                                bar.removePlayer(player);
                                removeGlobalBossbar(barInfo);
                                barInfo.remove();
                            }
                        }
                    }
                } catch (NoSuchMethodError e) {
                    e.printStackTrace();
                }

                if (!barInfo.stillRunning() && barInfo.getHideScheduler() == null) {
                    barInfo.setHideScheduler(CMIScheduler.get().runTaskLater(() -> {
                        CMIBossBarHideEvent ev = new CMIBossBarHideEvent(barInfo);
                        Bukkit.getServer().getPluginManager().callEvent(ev);
                        if (!ev.isCancelled()) {
                            barInfo.getBar().setVisible(false);
                            removeGlobalBossbar(barInfo);
                            barInfo.remove();
                            barInfo.setHideScheduler(null);
                        }
                    }, barInfo.getKeepFor()));

//		    barInfo.cancelAutoScheduler();
//		    return;
                }
            }
        }, 0L, barInfo.getAuto()));
    }

    public void removeBossBar(Player player, String name) {
        BossBarInfo old = this.getBossBar(player, name);
        if (old == null)
            return;
        this.removeBossBar(player, old);
    }

    public void removeBossBar(Player player, BossBarInfo bossBar) {
        if (bossBar == null)
            return;
        if (bossBar.getBar() != null)
            bossBar.getBar().setVisible(false);
        bossBar.cancelAutoScheduler();
        bossBar.cancelHideScheduler();
        getBossBarInfo(player).remove(bossBar.getNameOfBar().toLowerCase());
    }

    public void addBossBar(Player player, BossBarInfo barInfo) {
        if (player == null || barInfo == null)
            return;

        if (barInfo.getPlayer() == null)
            barInfo.setPlayer(player);
        if (!getBossBarInfo(player).containsKey(barInfo.getNameOfBar().toLowerCase())) {
            getBossBarInfo(player).put(barInfo.getNameOfBar().toLowerCase(), barInfo);
            Show(barInfo);
        } else {
            BossBarInfo old = getBossBar(player, barInfo.getNameOfBar().toLowerCase());
            if (old != null) {

                old.cancelAutoScheduler();
                old.cancelHideScheduler();

                if (barInfo.getColor() != null)
                    old.setColor(barInfo.getColor());

                if (barInfo.getKeepFor() != null)
                    old.setKeepForTicks(barInfo.getKeepFor());

                if (barInfo.getPercentage() != null)
                    old.setPercentage(barInfo.getPercentage());

                if (barInfo.getPlayer() != null)
                    old.setPlayer(barInfo.getPlayer());

                if (barInfo.getAdjustPerc() != null)
                    old.setAdjustPerc(barInfo.getAdjustPerc());

                if (barInfo.getStyle() != null)
                    old.setStyle(barInfo.getStyle());

                if (!barInfo.getTitleOfBar().isEmpty()) {
                    old.setTitleOfBar(barInfo.getTitleOfBarClean());
                }

                if (barInfo.getBar() != null)
                    old.setBar(barInfo.getBar());

                if (barInfo.getHideScheduler() != null)
                    old.setHideScheduler(barInfo.getHideScheduler());

                if (barInfo.getAuto() != null)
                    old.setAuto(barInfo.getAuto());

                if (barInfo.getCommands() != null)
                    old.setCmds(barInfo.getCommands());

                old.setStarted(System.currentTimeMillis());

            }
            Show(old);
        }
    }

    public synchronized BossBarInfo getBossBar(Player player, String name) {
        ConcurrentHashMap<String, BossBarInfo> info = getBossBarInfo(player);
        if (info == null) {
            return null;
        }
        return info.get(name.toLowerCase());
    }

    public synchronized ConcurrentHashMap<String, BossBarInfo> getBossBarInfo(Player player) {
        if (player == null) {
            return null;
        }
        ConcurrentHashMap<String, BossBarInfo> got = this.barMap.get(player.getUniqueId());
        if (got == null) {
            got = new ConcurrentHashMap<String, BossBarInfo>();
            this.barMap.put(player.getUniqueId(), got);
        }
        return got;
    }

    public synchronized void hideBossBars(Player player) {
        if (player == null || !player.isOnline()) {
            if (player != null)
                clearBossMaps(player);
            return;
        }

        ConcurrentHashMap<String, BossBarInfo> info = getBossBarInfo(player);
        if (info == null)
            return;
        for (Entry<String, BossBarInfo> one : info.entrySet()) {
            try {
                one.getValue().getBar().setVisible(false);
            } catch (NullPointerException e) {
            }
        }
    }

    public void clearBossMaps(Player player) {
        ConcurrentHashMap<String, BossBarInfo> info = getBossBarInfo(player);
        if (info == null)
            return;
        for (Entry<String, BossBarInfo> one : info.entrySet()) {
            one.getValue().cancelHideScheduler();
        }
        info.clear();
    }

}
