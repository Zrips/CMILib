package net.Zrips.CMILib.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Zrips.CMI.Modules.Advancements.AdvancementManager;
import com.Zrips.CMI.Modules.Advancements.AdvancementManager.FrameType;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.ActionBar.CMIActionBar;
import net.Zrips.CMILib.Advancements.AdvancementFrameType;
import net.Zrips.CMILib.Advancements.CMIAdvancement;
import net.Zrips.CMILib.BossBar.BossBarInfo;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMICommandSender;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Locale.Snd;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class CMIMultiMessage {

    private boolean updateSnd = false;
    private boolean translateColors = true;
    private boolean translatePlaceholders = true;
    private CMIMultiMessageType type = null;
    private String message;
    private List<Object> extra;

    public CMIMultiMessage(String msg) {

	for (CMIMultiMessageType one : CMIMultiMessageType.values()) {

	    Pattern pattern = one.getRegex();

	    Matcher match = pattern.matcher(msg);

	    if (!match.find())
		continue;
	    type = one;

	    switch (one) {
	    case actionBar:
	    case broadcast:
		msg = msg.substring(match.group().length());
		message = msg;
		break;
	    case toast:
		msg = msg.substring(match.group().length());

		extra = new ArrayList<Object>();

		if (match.group(2) != null) {
		    FrameType frame = FrameType.getFromString(match.group(3));
		    if (frame != null) {
			extra.add(frame);
		    }
		}

		if (match.group(4) != null) {
		    CMIMaterial material = CMIMaterial.get(match.group(5));
		    if (material.isValidItem())
			extra.add(material);
		}

		if (msg.startsWith(" "))
		    msg = msg.substring(1);

		message = msg;
		break;
	    case bossBar:
		msg = msg.substring(match.group().length());
		extra = new ArrayList<Object>();
		extra.add(match.group(2));
		if (match.group(4) != null && !match.group(4).isEmpty())
		    extra.add(match.group(4));
		message = msg;
		break;
	    case customText:
	    case timedActionBar:
		msg = msg.substring(match.group().length());
		extra = new ArrayList<Object>();
		extra.add(match.group(2));
		message = msg;
		break;
	    case json:
	    case plain:
		message = msg;
		break;
	    case title:
		String title = match.group(3);
		if (title != null && !title.isEmpty()) {
		    message = title;
		    extra = new ArrayList<Object>();
		    extra.add(match.group(5));
		} else {
		    message = match.group(5);
		}
		break;
	    default:
		break;
	    }
	}

	if (type == null) {
	    message = msg;
	    type = CMIMultiMessageType.plain;
	}
    }

//    public boolean show(CMIUser user) {
//	if (!user.isOnline())
//	    return false;
//	return show(user.getPlayer(false));
//    }

    public boolean show(CMICommandSender sender) {
	return show(sender.getSender());
    }

    public boolean show(CommandSender sender) {
        if (message.isEmpty())
            return false;
	message = CMILib.getInstance().getLM().filterNewLine(message);
	if (isTranslateColors())
	    message = CMIChatColor.translate(message);
	if (isUpdateSnd())
	    message = CMILib.getInstance().getLM().updateSnd(new Snd().setSender(sender).setTarget(sender), message);
	if (sender instanceof Player) {
	    if (this.isTranslatePlaceholders())
		message = CMILib.getInstance().getPlaceholderAPIManager().updatePlaceHolders((Player) sender, message);
	} else {
//	     Cant use with null player, returns empty field
//	    message = CMI.getInstance().getPlaceholderAPIManager().updatePlaceHolders(message);
	}

	switch (type) {
	case actionBar:
	    CMIActionBar.send(sender, message);
	    break;
	case bossBar:
	    if (!(sender instanceof Player))
		return false;
	    String bName = (String) getExtra().get(0);

	    int keepfor = 30;

	    if (getExtra().size() > 1) {
		try {
		    keepfor = (int) (Double.parseDouble((String) getExtra().get(1)) * 20);
		} catch (Throwable e) {
		}
	    }
	    BossBarInfo barInfo = new BossBarInfo((Player) sender, bName);
	    barInfo.setKeepForTicks(keepfor);
	    barInfo.setTitleOfBar(message);
	    CMILib.getInstance().getBossBarManager().addBossBar((Player) sender, barInfo);
	    break;
	case broadcast:
	    CMIMultiMessage sub = new CMIMultiMessage(message);
	    switch (sub.getType()) {
	    case customText:
		for (Player one : Bukkit.getOnlinePlayers()) {
		    sub.show(one);
		}
		break;
	    default:
		if (!(sender instanceof Player)) {
		    sender.sendMessage(message);
		}
		CMIMessages.broadcastMessage(sender, message);
		break;
	    }
	    break;
	case customText:
	    if (CMILib.getInstance().isCmiPresent()) {
		com.Zrips.CMI.Modules.CustomText.CText cText = com.Zrips.CMI.CMI.getInstance().getCTextManager().getCText((String) getExtra().get(0));
		if (cText == null)
		    sender.sendMessage(message);
		else
		    com.Zrips.CMI.CMI.getInstance().getCTextManager().showCText(sender, cText, 1);
	    }
	    break;
	case json:
	    RawMessage rm = RawMessage.translateRawMessage(sender, message);
	    CMIScheduler.get().runTask(() -> rm.show(sender));
	    break;
	case timedActionBar:
	    if (!(sender instanceof Player))
		return false;

	    int ime = 0;
	    try {
		ime = Integer.parseInt((String) getExtra().get(0));
	    } catch (Throwable e) {
	    }
	    CMIActionBar.send((Player) sender, message, ime);

	    break;
	case title:
	    String title = message;
	    String subtitle = getExtra().isEmpty() ? null : (String) getExtra().get(0);
	    if (sender instanceof Player)
		CMITitleMessage.send((Player) sender, title, subtitle);
	    break;
	case toast:
	    if (!(sender instanceof Player))
		return false;
	    if (Version.isCurrentLower(Version.v1_12_R1))
		sender.sendMessage(message);
	    else {

		CMIMaterial material = CMIMaterial.BLACK_STAINED_GLASS_PANE;
		AdvancementFrameType frametype = AdvancementFrameType.CHALLENGE;

		if (extra != null)
		    for (Object oneS : extra) {
			if (oneS instanceof CMIMaterial)
			    material = (CMIMaterial) oneS;
			else if (oneS instanceof AdvancementFrameType)
			    frametype = (AdvancementFrameType) oneS;
		    }

		new CMIAdvancement().setFrame(frametype).setIcon(material).setTitle(message).show((Player) sender);
	    }
	    break;
	case plain:
	default:
	    if (sender != null) {
		// Lets send messages in json format to bypass client side delay
		if (Version.isCurrentEqualOrHigher(Version.v1_16_R1) && sender instanceof Player) {
		    rm = new RawMessage();
		    rm.addText(message);
		    rm.show(sender);
		} else {
		    if (Version.isCurrentLower(Version.v1_16_R1))
			message = CMIChatColor.stripHexColor(message);
		    sender.sendMessage(message);
		}
	    }
	    break;
	}
	return true;
    }

    public boolean isTranslateColors() {
	return translateColors;
    }

    public void setTranslateColors(boolean translateColors) {
	this.translateColors = translateColors;
    }

    public boolean isUpdateSnd() {
	return updateSnd;
    }

    public void setUpdateSnd(boolean updateSnd) {
	this.updateSnd = updateSnd;
    }

    public CMIMultiMessageType getType() {
	return type;
    }

    public String getMessage() {
	return message;
    }

    public List<Object> getExtra() {
	if (extra == null)
	    extra = new ArrayList<Object>();
	return extra;
    }

    public boolean isMultiType() {
	return type != null && !type.equals(CMIMultiMessageType.plain);
    }

    public boolean isTranslatePlaceholders() {
	return translatePlaceholders;
    }

    public void setTranslatePlaceholders(boolean translatePlaceholders) {
	this.translatePlaceholders = translatePlaceholders;
    }

    public void setType(CMIMultiMessageType type) {
        this.type = type;
    }
}
