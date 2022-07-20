package net.Zrips.CMILib.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.Container.PageInfo;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.RawMessages.RawMessageCommand;

public class ChatMessageObjectEdit {
    private CommandSender sender = null;
    Long time = 0L;

    private List<ChatEditorObject> lines = new ArrayList<ChatEditorObject>();
    private List<String> modifyInfo = new ArrayList<String>();
    private boolean delRequiresConfirmation = true;
    private RawMessage topLine;
    private boolean basicNew = false;
    private boolean addNew = true;
    private int maxSize = 0;
    private PageInfo pi;

    public ChatMessageObjectEdit(CommandSender sender, PageInfo pi) {
	this(sender, 0, pi);
    }

    public ChatMessageObjectEdit(CommandSender sender, int maxSize, PageInfo pi) {
	this.sender = sender;
	this.maxSize = maxSize;
	this.pi = pi;
	time = System.currentTimeMillis();
    }

    public void addline(ChatEditorObject CEO) {
	lines.add(CEO);
    }

    public void onUpdate() {
    }

    public void beforePrint() {
    }

    public void clicked(int place) {
    }

    public void newAdd(String message) {
    }

    public UUID getUuid() {
	return sender instanceof Player ? ((Player) sender).getUniqueId() : CMILib.getInstance().getServerUUID();
    }

    public void print() {
	beforePrint();

	if (topLine != null)
	    topLine.show(sender);

	for (int i = 0; i < lines.size(); i++) {
	    RawMessage rm = new RawMessage();

	    ChatEditorObject line = lines.get(i);

	    RawMessageCommand rmc = new RawMessageCommand() {
		@Override
		public void run(CommandSender sender) {

		    if (delRequiresConfirmation) {

			RawMessage confirmationRm = new RawMessage();
			RawMessageCommand confirmationRmc = new RawMessageCommand() {
			    @Override
			    public void run(CommandSender sender) {
				line.onDelete();
			    }
			};
			confirmationRm.addText(LC.info_ClickToConfirmDelete.getLocale("[name]", line.getText()));
			confirmationRm.addHover(LC.info_Click.getLocale());
			confirmationRm.addCommand(confirmationRmc);
			confirmationRm.show(sender);
			return;
		    }

		    line.onDelete();
//		    print();
		}
	    };

	    rmc.setKeep(true);
	    rm.addText(LC.modify_deleteSymbol.getLocale() + " ").addHover(LC.modify_deleteSymbolHover.getLocale("[text]", i + pi.getStart() + 1)).addCommand(rmc.getCommand());

	    String number = String.valueOf(i + pi.getStart() + 1);

	    if (pi.getStart() < 99 && pi.getEnd() > 98 && i + pi.getStart() + 1 < 100 || i + pi.getStart() < 9)
		number = LC.modify_listAlign.getLocale() + CMIChatColor.getLastColors(LC.modify_listNumbering.getLocale()) + number;

	    rm.addText(LC.modify_listNumbering.getLocale("[number]", number));

	    rmc = new RawMessageCommand() {
		@Override
		public void run(CommandSender sender) {
		    line.onClick();
		}
	    };

	    rmc.setKeep(true);

	    if (line.getLine() != null) {
		rm.addRM(line.getLine(), true);
	    } else {
		rm.addText(LC.modify_editLineColor.getLocale() + line.getText()).addHover(line.getHover());
		if (line.getSuggestion() == null)
		    rm.addCommand(rmc.getCommand());
		else
		    rm.addSuggestion(line.getSuggestion());
	    }

	    rm.show(sender);
	}

	if (this.isAddNew()) {
	    RawMessage rm = new RawMessage();
	    if (maxSize <= 0 || lines.size() < maxSize) {
		RawMessageCommand rmc = new RawMessageCommand() {
		    @Override
		    public void run(CommandSender sender) {

			if (maxSize > 0 && lines.size() >= maxSize) {
			    LC.modify_listLimit.sendMessage(sender, "[amount]", maxSize);
			    return;
			}

			if (isBasicNew()) {			    
			    newAdd(null);
			} else {
			    ChatMessageEdit chatEdit = new ChatMessageEdit(sender) {
				@Override
				public void run(String message) {
				    if (!CMIText.isValidString(CMIChatColor.deColorize(message)))
					return;
				    newAdd(message);
//			    onUpdate();
				}

				@Override
				public void onDisable() {
//				onCancel();
				}
			    };
			    chatEdit.setCheckForCancel(true);
			    LC.modify_lineAddInfo.sendMessage(sender);
			}
		    }
		};
		rmc.setKeep(true);
		rm.addText(" " + LC.modify_addSymbol.getLocale() + " ").addHover(LC.modify_addSymbolHover.getLocale()).addCommand(rmc.getCommand());

		rm.addText(LC.modify_listNumbering.getLocale("[number]", (pi.getTotalEntries() < 9 ? LC.modify_listAlign.getLocale() + CMIChatColor.getLastColors(LC.modify_listNumbering
		    .getLocale()) : "") + (pi.getTotalEntries() + 1)));
	    }
	    rm.show(sender);
	}
    }

    public List<ChatEditorObject> getLines() {
	return lines;
    }

    public void setLines(List<ChatEditorObject> lines) {
	this.lines = lines;
    }

    public RawMessage getTopLine() {
	return topLine;
    }

    public void setTopLine(RawMessage topLine) {
	this.topLine = topLine;
    }

    public List<String> getModifyInfo() {
	return modifyInfo;
    }

    public void setModifyInfo(List<String> modifyInfo) {
	this.modifyInfo = modifyInfo;
    }

    public PageInfo getPageInfo() {
	return pi;
    }

    public boolean isDelRequiresConfirmation() {
	return delRequiresConfirmation;
    }

    public void setDelRequiresConfirmation(boolean delRequiresConfirmation) {
	this.delRequiresConfirmation = delRequiresConfirmation;
    }

    public boolean isAddNew() {
	return addNew;
    }

    public void setAddNew(boolean addNew) {
	this.addNew = addNew;
    }

    public boolean isBasicNew() {
	return basicNew;
    }

    public void setBasicNew(boolean basicNew) {
	this.basicNew = basicNew;
    }
}
