
package net.Zrips.CMILib.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMILocation;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.RawMessages.RawMessageCommand;

public class ChatMessageListEdit {
    private UUID uuid = null;
    Long time = 0L;

    public static enum ChatEditType {
	String, Location;
    }

    private boolean delRequiresConfirmation = true;
    private boolean flatenLines = false;
    private boolean identifyEmptylines = false;
    private boolean fillWithEmpty = false;
    private List<Object> lines = new ArrayList<Object>();
    private List<String> modifyInfo = new ArrayList<String>();
    private RawMessage topLine;
    private RawMessage bottomLine;
    private ChatEditType type = ChatEditType.String;
    private int maxSize = 0;
    private boolean addNew = true;

    public ChatMessageListEdit(CommandSender sender, Object lines, ChatEditType type, int maxSize) {
	this(sender instanceof Player ? ((Player) sender).getUniqueId() : CMILib.getInstance().getServerUUID(), lines, type, maxSize);
    }

    public ChatMessageListEdit(CommandSender sender, Object lines, ChatEditType type) {
	this(sender instanceof Player ? ((Player) sender).getUniqueId() : CMILib.getInstance().getServerUUID(), lines, type, 0);
    }

    public ChatMessageListEdit(UUID uuid, Object lines, ChatEditType type) {
	this(uuid, lines, type, 0);
    }

    public ChatMessageListEdit(UUID uuid, Object lines, ChatEditType type, int maxSize) {
	this.uuid = uuid;
	this.lines = (List<Object>) lines;
	this.type = type;
	this.maxSize = maxSize;
	if (type == null && !this.lines.isEmpty()) {
	    type = this.lines.get(0) instanceof String ? ChatEditType.String : ChatEditType.Location;
	}
//	ChatEditorManager.add(this);
	time = System.currentTimeMillis();
    }

    public void onUpdate() {
    }

    public void beforePrint() {
    }

    public void clicked(int place) {
    }
//    public void delete() {
//	ChatEditorManager.delete(this);
//    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public void print() {
	beforePrint();
	CommandSender sender = Bukkit.getPlayer(uuid);
	if (uuid == CMILib.getInstance().getServerUUID())
	    sender = Bukkit.getConsoleSender();

	if (topLine != null)
	    topLine.show(sender);

	for (int i = 0; i < lines.size(); i++) {
	    RawMessage rm = new RawMessage();
	    int fxedI = i;

	    RawMessageCommand rmc = new RawMessageCommand() {
		@Override
		public void run(CommandSender sender) {
		    if (delRequiresConfirmation) {

			RawMessage confirmationRm = new RawMessage();
			RawMessageCommand confirmationRmc = new RawMessageCommand() {
			    @Override
			    public void run(CommandSender sender) {
				remove(fxedI);
				print();
			    }
			};
			confirmationRm.addText(LC.info_ClickToConfirmDelete.getLocale("[name]", fxedI + 1));
			confirmationRm.addHover(LC.info_Click.getLocale());
			confirmationRm.addCommand(confirmationRmc);
			confirmationRm.show(sender);
			return;
		    }
		    remove(fxedI);
		    print();
		}
	    };
	    rm.addText(LC.modify_deleteSymbol.getLocale() + " ").addHover(LC.modify_deleteSymbolHover.getLocale("[text]", i + 1)).addCommand(rmc.getCommand());

	    
	    
	    rm.addText(LC.modify_listNumbering.getLocale("[number]", (i < 9 ? LC.modify_listAlign.getLocale()+CMIChatColor.getLastColors(LC.modify_listNumbering.getLocale()) : "") + (i + 1)));
	    rmc = new RawMessageCommand() {
		@Override
		public void run(CommandSender sender) {
		    move(fxedI, dir.up);
		    print();
		}
	    };

	    rm.addText(LC.modify_listUpSymbol.getLocale()).addHover(LC.modify_listUpSymbolHover.getLocale()).addCommand(rmc.getCommand());
	    rmc = new RawMessageCommand() {
		@Override
		public void run(CommandSender sender) {
		    move(fxedI, dir.down);
		    print();
		}
	    };
	    rm.addText(LC.modify_listDownSymbol.getLocale()).addHover(LC.modify_listDownSymbolHover.getLocale()).addCommand(rmc.getCommand());
	    rm.addText(" ");

	    int ii = i;
	    if (type.equals(ChatEditType.String)) {
		rmc = new RawMessageCommand() {
		    @Override
		    public void run(CommandSender sender) {

			ChatMessageEdit chatEdit = new ChatMessageEdit(sender) {
			    @Override
			    public void run(String message) {

				if (!CMIText.isValidString(CMIChatColor.deColorize(message)))
				    return;

				if (maxSize > 0 && lines.size() >= maxSize && ii + 1 > maxSize) {
				    LC.modify_listLimit.sendMessage(sender, "[amount]", maxSize);
				    return;
				}

				while (lines.size() <= ii) {
				    lines.add("");
				}
				lines.set(ii, message);
				onUpdate();
			    }

			    @Override
			    public void onDisable() {
				print();
			    }
			};
			chatEdit.setCheckForCancel(true);

			RawMessage rm = new RawMessage();
			String hover = LC.info_ClickToPaste.getLocale();
			for (String one : modifyInfo) {
			    hover += "\n" + one;
			}
			rm.addText(LC.modify_commandEditInfo.getLocale()).addHover(hover);

			if (lines.size() > ii)
			    rm.addSuggestion(CMIChatColor.deColorize((String) lines.get(ii), false));
			rm.show(sender);
		    }
		};

		String text = (String) lines.get(i);
		if (this.isFlatenLines())
		    text = CMIChatColor.flaten(text);

		if (this.isIdentifyEmptylines() && (text.isEmpty() || text.equalsIgnoreCase(" "))) {
		    rm.addText(LC.modify_emptyLine.getLocale());
		} else
		    rm.addText(LC.modify_editLineColor.getLocale() + text);

		rm.addHover(LC.modify_editSymbolHover.getLocale("[text]", i + 1)).addCommand(rmc.getCommand());
	    } else if (type.equals(ChatEditType.Location)) {

		rm.addText(LC.modify_editLineColor.getLocale() + CMILocation.toString((CMILocation) lines.get(i)));
		rm.addHover(LC.info_ClickToPaste.getLocale());
		rm.addSuggestion(CMILocation.toString((CMILocation) lines.get(i)));
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

			switch (type) {
			case Location:

			    Player player = Bukkit.getPlayer(uuid);
			    lines.add(new CMILocation(player.getLocation()));
			    onUpdate();
			    print();

			    break;
			case String:
			    ChatMessageEdit chatEdit = new ChatMessageEdit(sender) {
				@Override
				public void run(String message) {
				    if (!CMIText.isValidString(CMIChatColor.deColorize(message)))
					return;
				    lines.add(message);
				    onUpdate();
				}

				@Override
				public void onDisable() {
				    print();
				}
			    };
			    chatEdit.setCheckForCancel(true);
			    LC.modify_lineAddInfo.sendMessage(sender);
			    break;
			default:
			    break;

			}
		    }
		};

		rm.addText(" " + LC.modify_addSymbol.getLocale() + " ").addHover(LC.modify_addSymbolHover.getLocale()).addCommand(rmc.getCommand());
		rm.addText(LC.modify_listNumbering.getLocale("[number]", (lines.size() < 9 ? LC.modify_listAlign.getLocale()+CMIChatColor.getLastColors(LC.modify_listNumbering.getLocale()) : "") + (lines.size() + 1)));
	    }
	    rm.show(sender);
	}

	if (bottomLine != null)
	    bottomLine.show(sender);
    }

    private enum dir {
	up(-1), down(1);

	private int val;

	dir(int val) {
	    this.val = val;
	}

	public int getValue() {
	    return val;
	}
    }

    private void remove(int place) {
	if (lines.size() >= place) {
	    if (this.isFillWithEmpty())
		lines.set(place, "");
	    else
		lines.remove(place);
	    onUpdate();
	}
    }

    private void move(int from, dir direction) {
	int to = from + direction.getValue();
	Object tocmd = null;
	Object fromcmd = null;
	if (lines.size() >= to + 1 && to >= 0)
	    tocmd = lines.get(to);
	if (lines.size() >= from + 1 && from >= 0)
	    fromcmd = lines.get(from);
	if (tocmd != null && fromcmd != null) {
	    lines.set(to, fromcmd);
	    lines.set(from, tocmd);
	}
	onUpdate();
    }

    public List<Object> getLines() {
	return lines;
    }

    public void setLines(List<Object> lines) {
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

    public boolean isFlatenLines() {
	return flatenLines;
    }

    public void setFlatenLines(boolean flatenLines) {
	this.flatenLines = flatenLines;
    }

    public boolean isAddNew() {
	return addNew;
    }

    public void setAddNew(boolean addNew) {
	this.addNew = addNew;
    }

    public RawMessage getBottomLine() {
	return bottomLine;
    }

    public void setBottomLine(RawMessage bottomLine) {
	this.bottomLine = bottomLine;
    }

    public boolean isIdentifyEmptylines() {
	return identifyEmptylines;
    }

    public void setIdentifyEmptylines(boolean identifyEmptylines) {
	this.identifyEmptylines = identifyEmptylines;
    }

    public boolean isFillWithEmpty() {
	return fillWithEmpty;
    }

    public void setFillWithEmpty(boolean fillWithEmpty) {
	this.fillWithEmpty = fillWithEmpty;
    }
}
