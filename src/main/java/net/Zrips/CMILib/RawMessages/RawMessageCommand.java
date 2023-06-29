package net.Zrips.CMILib.RawMessages;

import java.util.UUID;

import org.bukkit.command.CommandSender;

public class RawMessageCommand {
    private Long id = null;
    private Long time = 0L;
    private UUID uuid = null;
    private boolean keep = false;
    private String command = null;

    public RawMessageCommand() {
	this(null);
    }

    public RawMessageCommand(UUID uuid) {
	this.id = RawMessageManager.add(this);
	this.time = System.currentTimeMillis();
	this.uuid = uuid;
    }

    public void run(CommandSender sender) {
    }

    public Long getId() {
	return id;
    }

    @Deprecated
    public String getCommandToPerform() {
	return getCommand();
    }

    public String getCommand() {
	return RawMessageManager.rmccmd + id;
    }

    public void delete() {
	RawMessageManager.delete(this);
    }

    public boolean isKeep() {
	return keep;
    }

    public void setKeep(boolean keep) {
	this.keep = keep;
    }

    public Long getCreationTime() {
	return time;
    }

    public UUID getUUID() {
	return uuid;
    }

    public void setOriginalCommand(String command) {        
	this.command = command.replace("  ", " ");
    }

    public String getOriginalCommand() {
	return this.command;
    }
}
