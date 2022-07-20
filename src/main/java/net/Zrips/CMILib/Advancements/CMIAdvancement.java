package net.Zrips.CMILib.Advancements;

import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Advancements.AdvancementManager.FrameType;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Version;

public class CMIAdvancement {

    private static final Gson gson = new Gson();

    private NamespacedKey id;
    private String parent;
    private String icon;
    private String background;
    private String title;
    private String description;
    private int data;
    private FrameType frame;
    private boolean announce = true;
    private boolean toast = true;
    private boolean hidden = true;

    private Object ads = null;

    private static Method advancemethod;
    static {

    }

    private CMIAdvancement(NamespacedKey id, String parent, String icon, int data, String background, String title, String description, FrameType frame, boolean announce, boolean toast, boolean hidden) {
	this.id = id;
	this.parent = parent;
	this.icon = icon;
	this.data = data;
	this.background = background;
	this.title = title;
	this.description = description;
	this.frame = frame;
	this.announce = announce;
	this.toast = toast;
	this.hidden = hidden;
    }

    public static AdvancementBuilder builder(NamespacedKey id) {
	return new AdvancementBuilder().id(id);
    }

    public String getJSON() {
	JsonObject json = new JsonObject();

	JsonObject icon = new JsonObject();
	icon.addProperty("item", getIcon());
	if (Version.isCurrentLower(Version.v1_13_R1))
	    icon.addProperty("data", getData());

	JsonObject display = new JsonObject();
	display.add("icon", icon);
	display.add("title", getJsonFromComponent(getTitle()));
	display.add("description", getJsonFromComponent(getDescription()));
//	display.add("background", getJsonFromComponent(getBackground()));
	display.addProperty("background", getBackground());
	display.addProperty("frame", getFrame().toString());
	display.addProperty("announce_to_chat", announce);
	display.addProperty("show_toast", toast);
	display.addProperty("hidden", hidden);

//	json.addProperty("parent", getParent());

	JsonObject criteria = new JsonObject();

	JsonObject triggerObj = new JsonObject();
	triggerObj.addProperty("trigger", "minecraft:impossible");
	criteria.add("IMPOSSIBLE", triggerObj);

	json.add("criteria", criteria);
	json.add("display", display);

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	return gson.toJson(json);
    }

    public String getIcon() {
	return this.icon;
    }

    public int getData() {
	return this.data;
    }

    public static JsonElement getJsonFromComponent(String textComponent) {

	StringBuffer sb = new StringBuffer();

	textComponent = textComponent.replace("\\\\n", "XlineBreakX");
	for (int i = 0; i < textComponent.length(); i++) {
	    char ch = textComponent.charAt(i);
	    switch (ch) {
	    case '"':
		sb.append("\\\"");
		break;
	    case '\\':
		sb.append("\\\\");
		break;
	    case '\b':
		sb.append("\\b");
		break;
	    case '\f':
		sb.append("\\f");
		break;
	    case '\n':
		sb.append("\\n");
		break;
	    case '\r':
		sb.append("\\r");
		break;
	    case '\t':
		sb.append("\\t");
		break;
	    case '/':
		sb.append("\\/");
		break;
	    default:
		sb.append(ch);
		break;
	    }
	}

	textComponent = sb.toString();
	textComponent = textComponent.replace("XlineBreakX", "\n");
	return gson.fromJson("\"" + textComponent + "\"", JsonElement.class);
    }

    public String getTitle() {
	return this.title;
    }

    public String getDescription() {
	return this.description;
    }

    public String getBackground() {
	return this.background;
    }

    public FrameType getFrame() {
	return this.frame;
    }

    public String getParent() {
	return this.parent;
    }

    public CMIAdvancement show(JavaPlugin plugin, final Player... players) {
	if (Version.isCurrentEqualOrLower(Version.v1_15_R1))
	    return this;
	add();
	grant(players);
	final CMIAdvancement ad = this;
	Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
	    @Override
	    public void run() {
		revoke(players);
//		Bukkit.getUnsafe().removeAdvancement(ad.getId());
		CMILib.getInstance().getReflectionManager().removeAdvancement(ad);
	    }
	}, 20L);
	return this;
    }

    public CMIAdvancement add() {
	try {
	    CMILib.getInstance().getReflectionManager().loadAdvancement(this, getJSON());
	} catch (IllegalArgumentException e) {
	}
	return this;
    }

    public CMIAdvancement grant(Player... players) {
	Advancement advancement = getAdvancement();
	for (Player player : players) {
	    if (player.getAdvancementProgress(advancement).isDone())
		continue;
	    Collection<String> remainingCriteria = player.getAdvancementProgress(advancement).getRemainingCriteria();
	    for (String remainingCriterion : remainingCriteria) {
		player.getAdvancementProgress(getAdvancement()).awardCriteria(remainingCriterion);
	    }
	}
	return this;
    }

    public CMIAdvancement revoke(Player... players) {
	Advancement advancement = getAdvancement();
	if (advancement == null)
	    return this;
	for (Player player : players) {
	    if (player.getAdvancementProgress(advancement) == null || !player.getAdvancementProgress(advancement).isDone())
		continue;
	    Collection<String> awardedCriteria = player.getAdvancementProgress(advancement).getAwardedCriteria();
	    for (String awardedCriterion : awardedCriteria) {
		player.getAdvancementProgress(getAdvancement()).revokeCriteria(awardedCriterion);
	    }
	}
	return this;
    }

    public Advancement getAdvancement() {
	return Bukkit.getAdvancement(id);
    }

    public NamespacedKey getId() {
	return this.id;
    }

    public boolean isAnnounce() {
	return this.announce;
    }

    public boolean isToast() {
	return this.toast;
    }

    public boolean isHidden() {
	return this.hidden;
    }

    public Object getMinecraftKey() {
	return ads;
    }

    public void setMinecraftKey(Object ads) {
	this.ads = ads;
    }

    public static class AdvancementBuilder {
	private NamespacedKey id;
	private String parent;
	private String icon;
	private String background;
	private String title;
	private String description;
	private FrameType frame;
	private boolean announce;
	private boolean toast;
	private boolean hidden;
	private int data;

	AdvancementBuilder() {
	}

	public AdvancementBuilder title(String title) {
	    this.title = title;
	    return this;
	}

	public AdvancementBuilder description(String description) {
	    this.description = description;
	    return this;
	}

	public AdvancementBuilder id(NamespacedKey id) {
	    this.id = id;
	    return this;
	}

	public AdvancementBuilder parent(String parent) {
	    this.parent = parent;
	    return this;
	}

	public AdvancementBuilder icon(String icon) {
	    this.icon = icon;
	    return this;
	}

	public AdvancementBuilder data(int data) {
	    this.data = data;
	    return this;
	}

	public AdvancementBuilder background(String background) {
	    this.background = background;
	    return this;
	}

	public AdvancementBuilder frame(FrameType frame) {
	    this.frame = frame;
	    return this;
	}

	public AdvancementBuilder announce(boolean announce) {
	    this.announce = announce;
	    return this;
	}

	public AdvancementBuilder toast(boolean toast) {
	    this.toast = toast;
	    return this;
	}

	public AdvancementBuilder hidden(boolean hidden) {
	    this.hidden = hidden;
	    return this;
	}

	public CMIAdvancement build() {
	    return new CMIAdvancement(id, parent, icon, data, background, title, description, frame, announce, toast, hidden);
	}
    }
}
