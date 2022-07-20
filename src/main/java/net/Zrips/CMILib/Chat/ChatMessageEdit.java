package net.Zrips.CMILib.Chat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.RawMessages.RawMessage;

public class ChatMessageEdit {
    private UUID uuid = null;
    Long time = 0L;
    private boolean keep = false;
    private String cancelVariable = "cancel";
    private boolean checkForCancel = false;
    private String pasteInString = null;

    public ChatMessageEdit(CommandSender sender) {
	this(sender instanceof Player ? ((Player) sender).getUniqueId() : CMILib.getInstance().getServerUUID());
    }

    public ChatMessageEdit(CommandSender sender, String pasteInText) {
	this(sender instanceof Player ? ((Player) sender).getUniqueId() : CMILib.getInstance().getServerUUID(), pasteInText);
    }

    public ChatMessageEdit(UUID uuid) {
	this(uuid, null);
    }

    public ChatMessageEdit(UUID uuid, String pasteInText) {
	this.uuid = uuid;
	ChatEditorManager.add(this);
	time = System.currentTimeMillis();
	this.pasteInString = pasteInText;
    }

    public void printMessage() {
	printMessage(null);
    }

    public void printMessage(String customText) {
	if (pasteInString != null) {
	    RawMessage rm = new RawMessage();
	    rm.addText(customText == null ? LC.modify_commandEditInfo.getLocale() : CMIChatColor.translate(customText));
	    rm.addHover(LC.info_Click.getLocale());
	    rm.addSuggestion(pasteInString);
	    rm.show(Bukkit.getPlayer(uuid));
	}
    }

    public void run(String message) {
    }

    public void onCancel() {

    }

    public void onDisable() {
    }

    public void delete() {
	ChatEditorManager.delete(this);
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public boolean isKeep() {
	return keep;
    }

    public void setKeep(boolean keep) {
	this.keep = keep;
    }

    public String getCancelVariable() {
	return cancelVariable;
    }

    public void setCancelVariable(String cancelVariable) {
	this.cancelVariable = cancelVariable;
    }

    public boolean isCheckForCancel() {
	return checkForCancel;
    }

    public void setCheckForCancel(boolean checkForCancel) {
	this.checkForCancel = checkForCancel;
    }

    public String getPasteInString() {
	return pasteInString;
    }
}
