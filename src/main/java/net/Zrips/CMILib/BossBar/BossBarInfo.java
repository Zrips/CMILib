package net.Zrips.CMILib.BossBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Locale.Snd;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Time.CMITimeManager;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class BossBarInfo {
    private Player player;
    private Double percentage = null;
    private Double adjustPerc = null;
    private Integer keepFor = 60;
    private Integer auto = null;
    private BossBar bar;
    private BarColor startingColor = null;
    private BarStyle style = null;
    private Integer autoId = null;
    private Integer id = null;
    private String nameOfBar;
    private String titleOfBar = "";
    private boolean withPlaceholder = false;
    private List<String> cmds = null;
    private boolean global = false;
    private boolean makeVisible = false;
    private long started = 0L;
    private boolean translateColors = true;

    public BossBarInfo clone(Player player) {
        BossBarInfo barInfo = new BossBarInfo(player, nameOfBar);
        barInfo.percentage = percentage;
        barInfo.adjustPerc = adjustPerc;
        barInfo.keepFor = keepFor;
        barInfo.auto = auto;
        barInfo.bar = bar;
        barInfo.startingColor = startingColor;
        barInfo.style = style;
//	barInfo.autoId = autoId;
//	barInfo.id = id;
        barInfo.nameOfBar = nameOfBar;
        barInfo.translateColors = translateColors;

        barInfo.titleOfBar = titleOfBar;
        barInfo.withPlaceholder = CMILib.getInstance().getPlaceholderAPIManager().containsPlaceHolder(titleOfBar);
        barInfo.cmds = cmds;
        barInfo.global = global;
        return barInfo;
    }

    public BossBarInfo(String nameOfBar) {
        this(null, nameOfBar, null);
    }

    public BossBarInfo(Player player, String nameOfBar) {
        this(player, nameOfBar, null);
    }

    public BossBarInfo(Player player, String nameOfBar, BossBar bar) {
        this.player = player;
        this.nameOfBar = nameOfBar;
        this.bar = bar;
        started = System.currentTimeMillis();
    }

    public void setHideId(Integer id) {
        cancelHideScheduler();
        this.id = id;
    }

    public synchronized void cancelAutoScheduler() {
        if (autoId != null) {
            CMIScheduler.cancelTask(autoId);
            autoId = null;
        }
    }

    public synchronized void cancelHideScheduler() {
        if (id != null) {
            CMIScheduler.cancelTask(id);
            id = null;
        }
    }

    public void remove() {
        cancelAutoScheduler();
        cancelHideScheduler();
        if (bar != null) {
            bar.setVisible(false);
        }
        if (this.player != null)
            CMILib.getInstance().getBossBarManager().removeBossBar(this.player, this);
        else {
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public BossBar getBar() {
        return this.bar;
    }

    public Double getPercentage() {
        if (percentage == null)
            percentage = 0D;
        return percentage < 0D ? 0D : percentage > 1D ? 1D : percentage;
    }

    public void setPercentage(double max, double current) {
        if (max == 0)
            max = 1D;
        current = current * 100 / max / 100D;
        setPercentage(current);
    }

    public void setPercentage(Double percentage) {

        if (percentage != null) {
            if (percentage < 0)
                percentage = 0D;
            if (percentage > 1)
                percentage = 1D;
            if (Double.isNaN(percentage) || Double.isInfinite(percentage)) {
                if (adjustPerc != null && adjustPerc > 0)
                    percentage = 0D;
                else
                    percentage = 1D;
            }
        }

        this.percentage = percentage;
    }

    public String getNameOfBar() {
        if (nameOfBar == null)
            nameOfBar = "CmiBossbar" + (new Random().nextInt(Integer.MAX_VALUE));
        return nameOfBar;
    }

    public void setNameOfBar(String nameOfBar) {
        this.nameOfBar = nameOfBar;
    }

    public Integer getKeepFor() {
        return keepFor == null ? 30 : keepFor;
    }

    public void setKeepForTicks(Integer keepFor) {
        if (keepFor != null)
            this.keepFor = keepFor;
    }

    public String getTitleOfBarClean() {
        return titleOfBar == null ? "" : titleOfBar;
    }

    public String getTitleOfBar() {
        if (titleOfBar != null && titleOfBar.contains("[autoTimeLeft]")) {
            if (this.percentage != null && this.adjustPerc != null && this.auto != null) {
                return titleOfBar.replace("[autoTimeLeft]", CMITimeManager.to24hourShort(getLeftDuration(), false));
            }
            return titleOfBar.replace("[autoTimeLeft]", CMITimeManager.to24hourShort(0L, false));
        }
        return titleOfBar == null ? "" : titleOfBar;
    }

    public String getTitleOfBar(Player player) {
        String t = getTitleOfBar();

        if (this.isWithPlaceholder())
            t = CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders(player, t);

        return t == null ? "" : isTranslateColors() ? CMIChatColor.colorize(t) : t;
    }

    public long getLeftDuration() {
        Long mili = 0L;
        if (this.percentage != null && this.adjustPerc != null && this.auto != null) {
            double leftTicks = this.percentage / (this.adjustPerc < 0 ? -this.adjustPerc : this.adjustPerc);
            Long totalTicks = (long) (leftTicks * (this.auto < 0 ? -this.auto : this.auto));
            mili = totalTicks * 50;
            if (this.getAdjustPerc() < 0)
                mili += 1000;
        }
        return mili;
    }

    public void setTitleOfBar(String titleOfBar) {
        if (titleOfBar == null || titleOfBar.isEmpty())
            this.titleOfBar = null;
        else
            this.titleOfBar = titleOfBar;

        withPlaceholder = CMILib.getInstance().getPlaceholderAPIManager().containsPlaceHolder(titleOfBar);
    }

    public void setBar(BossBar bar) {
        this.bar = bar;
    }

    public BarColor getColor() {
        return startingColor;
    }

    public void setColor(BarColor startingColor) {
//	if (startingColor == null)
//	    startingColor = BarColor.GREEN;
        this.startingColor = startingColor;
    }

    public Double getAdjustPerc() {
        return adjustPerc;
    }

    public void setAdjustPerc(Double adjustPerc) {
        this.adjustPerc = adjustPerc;
    }

    public BarStyle getStyle() {
        return style;
    }

    public void setStyle(BarStyle style) {
//	if (style == null)
//	    style = BarStyle.SEGMENTED_10;
        this.style = style;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getHideId() {
        return id;
    }

    public Integer getAuto() {
        return auto == null ? 20 : auto;
    }

    public void setAuto(Integer auto) {
        cancelAutoScheduler();
        this.auto = auto;
    }

    public Integer getAutoId() {
        return autoId;
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public List<String> getCommands() {
        return cmds;
    }

    public List<String> getCommands(Player player) {
        Snd snd = new Snd();
        snd.setSender(player);
        snd.setTarget(player);
        return CMILib.getInstance().getLM().updateSnd(snd, new ArrayList<String>(cmds));
    }

    public void setCmds(List<String> cmds) {
        this.cmds = cmds;
    }

    public boolean stillRunning() {

        if (getPercentage() < 1 && getAdjustPerc() != null && getAdjustPerc() > 0)
            return true;

        if (getPercentage() > 0 && getAdjustPerc() != null && getAdjustPerc() < 0)
            return true;

        if (getPercentage() <= 0 && getAdjustPerc() != null && getAdjustPerc() < 0)
            return false;

        if (getPercentage() >= 0 && getAdjustPerc() != null && getAdjustPerc() > 0)
            return false;

        if (getAdjustPerc() == null && this.getKeepFor() > 0 && getStarted() > 0 && System.currentTimeMillis() < getStarted() + (this.getKeepFor() * 50L)) {
            return true;
        }

        return getAdjustPerc() == null && this.getKeepFor() < 0 || (getPercentage() != null && getPercentage() <= 0 && getAdjustPerc() != null && getAdjustPerc() < 0) || (getPercentage() != null
            && getPercentage() >= 1 && getAdjustPerc() != null && getAdjustPerc() > 0);
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isMakeVisible() {
        return makeVisible;
    }

    public void setMakeVisible(boolean makeVisible) {
        this.makeVisible = makeVisible;
    }

    public long getStarted() {
        return started;
    }

    public void setStarted(long started) {
        this.started = started;
    }

    public void setSeconds(int time) {
        double change = (100D / (time * 20D)) / 100D;
        setAdjustPerc(change);
        if (time < 0)
            setPercentage(1D);
        else
            setPercentage(0D);
        setAuto(1);
    }

    public boolean isWithPlaceholder() {
        return withPlaceholder;
    }

    public boolean isTranslateColors() {
        return translateColors;
    }

    public void setTranslateColors(boolean translateColors) {
        this.translateColors = translateColors;
    }

    public void updateCycle() {
    }
}
