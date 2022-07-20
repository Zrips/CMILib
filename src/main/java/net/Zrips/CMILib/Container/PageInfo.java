package net.Zrips.CMILib.Container;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.RawMessages.RawMessageCommand;
import net.Zrips.CMILib.commands.CommandsHandler;

public class PageInfo {

    private int totalEntries = 0;
    private int totalPages = 0;
    private int start = 0;
    private int end = 0;
    private int currentPage = 0;

    private int currentEntry = 0;

    private int perPage = 6;

    public PageInfo(int perPage, int totalEntries, int currentPage) {
	this.perPage = perPage;
	this.totalEntries = totalEntries;
	this.currentPage = currentPage < 1 ? 1 : currentPage;
	calculate();
    }

    public int getPositionForOutput() {
	return currentEntry;
    }

    public int getPositionForOutput(int place) {
	return this.start + place + 1;
    }

    private void calculate() {
	currentEntry = 0;
	this.start = (this.currentPage - 1) * this.perPage;
	this.end = this.start + this.perPage - 1;
	if (this.end + 1 > this.totalEntries)
	    this.end = this.totalEntries - 1;
	this.totalPages = (int) Math.ceil((double) this.totalEntries / (double) this.perPage);
    }

    public boolean isInRange(int place) {
	if (place >= start && place <= end)
	    return true;
	return false;
    }

    public boolean isEntryOk() {
	currentEntry++;
	return currentEntry - 1 >= start && currentEntry - 1 <= end;
    }

    public boolean isContinue() {
	return !isEntryOk();
    }

    public boolean isContinueNoAdd() {
	return currentEntry - 1 >= start && currentEntry - 1 <= end;
    }

    public boolean isBreak() {
	return currentEntry - 1 > end;
    }

    public boolean isPageOk() {
	return isPageOk(this.currentPage);
    }

    public boolean isPageOk(int page) {
	if (this.totalPages < page)
	    return false;
	if (page < 1)
	    return false;
	return true;
    }

    public int getStart() {
	return start;
    }

    public int getEnd() {
	return end;
    }

    public int getTotalPages() {
	return totalPages;
    }

    public int getCurrentPage() {
	return currentPage;
    }

    public int getTotalEntries() {
	return totalEntries;
    }

    public int getNextPageNumber() {
	return this.getCurrentPage() + 1 > this.getTotalPages() ? this.getTotalPages() : this.getCurrentPage() + 1;
    }

    public int getPrevPageNumber() {
	return this.getCurrentPage() - 1 < 1 ? 1 : this.getCurrentPage() - 1;
    }

    public Boolean pageChange(int page) {
	return true;
    }

    public PageInfo setCurrentPage(int currentPage) {
	this.currentPage = currentPage;
	calculate();
	return this;
    }

    @Deprecated
    public void ShowPagination(CommandSender sender, String cmd) {
	ShowPagination(sender, cmd, null);
    }

    @Deprecated
    public void ShowPagination(CMICommandSender sender, String cmd) {
	ShowPagination(sender.getSender(), cmd, null);
    }

    @Deprecated
    public void ShowPagination(CommandSender sender, Object clas, String pagePref) {
	ShowPagination(sender, CommandsHandler.getLabel() + " " + clas.getClass().getSimpleName(), pagePref);
    }

    @Deprecated
    public void ShowPagination(CommandSender sender, String cmd, String pagePref) {

	if (!cmd.startsWith("/"))
	    cmd = "/" + cmd;

	if (getTotalPages() == 1)
	    return;

	String pagePrefix = pagePref == null ? "" : pagePref;

	int nextPage = getCurrentPage() + 1;
	nextPage = getCurrentPage() < getTotalPages() ? nextPage : getCurrentPage();
	int prevpage = getCurrentPage() - 1;
	prevpage = getCurrentPage() > 1 ? prevpage : getCurrentPage();

	if (!(sender instanceof Player)) {
	    CMIMessages.sendMessage(sender, LC.info_nextPageConsole, "[command]", (cmd.replace("/", "") + " " + pagePrefix + nextPage));
	    return;
	}

	RawMessage rm = new RawMessage();
	rm.addText((getCurrentPage() > 1 ? CMIMessages.getMsg(LC.info_prevPage) : CMIMessages.getMsg(LC.info_prevPageOff)))
	    .addHover(getCurrentPage() > 1 ? CMIMessages.getMsg(LC.info_prevPageHover) : CMIMessages.getMsg(LC.info_lastPageHover))
	    .addCommand(getCurrentPage() > 1 ? cmd + " " + pagePrefix + prevpage : cmd + " " + pagePrefix + getTotalPages());
	rm.addText(CMIMessages.getMsg(LC.info_pageCount, "[current]", getCurrentPage(), "[total]", getTotalPages())).addHover(CMIMessages.getMsg(LC.info_pageCountHover, "[totalEntries]",
	    getTotalEntries()));
	rm.addText(CMIMessages.getMsg(getTotalPages() > getCurrentPage() ? LC.info_nextPage : LC.info_nextPageOff))
	    .addHover(getTotalPages() > getCurrentPage() ? CMIMessages.getMsg(LC.info_nextPageHover) : CMIMessages.getMsg(LC.info_firstPageHover))
	    .addCommand(getTotalPages() > getCurrentPage() ? cmd + " " + pagePrefix + nextPage : cmd + " " + pagePrefix + 1);
	if (getTotalPages() != 0)
	    rm.show(sender);
    }

    public void autoPagination(CMICommandSender sender, String cmd) {
	autoPagination(sender.getSender(), cmd, null);
    }

    public void autoPagination(CMICommandSender sender, String cmd, String pagePref) {
	autoPagination(sender.getSender(), cmd, pagePref);
    }

    public void autoPagination(CommandSender sender, String cmd) {
	autoPagination(sender, cmd, null);
    }

    public void autoPagination(CommandSender sender, String cmd, String pagePref) {

	if (getTotalPages() == 1)
	    return;

	String pagePrefix = pagePref == null ? "" : pagePref;

	final int nextPage = getCurrentPage() < getTotalPages() ? getCurrentPage() + 1 : 1;
	final int prevpage = getCurrentPage() > 1 ? getCurrentPage() - 1 : getTotalPages();

	if (!(sender instanceof Player)) {
	    CMIMessages.sendMessage(sender, LC.info_nextPageConsole, "[command]", (cmd.replace("/", "") + " " + pagePrefix + nextPage));
	    return;
	}

	RawMessage rm = new RawMessage();
	RawMessageCommand rmcb = new RawMessageCommand() {
	    @Override
	    public void run(CommandSender sender) {
		pageChange(prevpage);
	    }
	};

	rmcb.setOriginalCommand((cmd.replace("/", "") + " " + pagePrefix + prevpage));

	rm.addText((getCurrentPage() > 1 ? LC.info_prevPage.getLocale() : LC.info_prevPageOff.getLocale()))
	    .addHover(getCurrentPage() > 1 ? LC.info_prevPageHover.getLocale() : LC.info_lastPageHover.getLocale())
	    .addCommand(rmcb.getCommand());

	rm.addText(LC.info_pageCount.getLocale("[current]", getCurrentPage(), "[total]", getTotalPages())).addHover(LC.info_pageCountHover.getLocale("[totalEntries]", getTotalEntries()));

	RawMessageCommand rmcf = new RawMessageCommand() {
	    @Override
	    public void run(CommandSender sender) {
		pageChange(nextPage);
	    }
	};
	rmcf.setOriginalCommand((cmd.replace("/", "") + " " + pagePrefix + nextPage));
	rm.addText(CMIMessages.getMsg(getTotalPages() > getCurrentPage() ? LC.info_nextPage : LC.info_nextPageOff))
	    .addHover(getTotalPages() > getCurrentPage() ? LC.info_nextPageHover.getLocale() : LC.info_firstPageHover.getLocale())
	    .addCommand(rmcf.getCommand());
	if (getTotalPages() != 0)
	    rm.show(sender);
    }
}
