package net.Zrips.CMILib.Advancements;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import com.Zrips.CMI.CMI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.Version.Version;

public class CMIAdvancement {

    public static final String identificator = "CMITOAST";

    private static final Gson gson = new Gson();

    private NamespacedKey id;
    private String parent;
    private String icon;
    private AdvancementBackground background;
    private String title;
    private String description;
    private int data;
    private int customModelData = 0;
    private AdvancementFrameType frame;
    private boolean announce = true;
    private boolean toast = true;
    private boolean hidden = true;

    public CMIAdvancement setTitle(String title) {
        this.title = title;
        return this;
    }

    public CMIAdvancement setDescription(String description) {
        this.description = description;
        return this;
    }

    public CMIAdvancement setId(NamespacedKey id) {
        this.id = id;
        return this;
    }

    public CMIAdvancement setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public CMIAdvancement setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public CMIAdvancement setIcon(CMIMaterial mat) {
        if (mat.isValidAsItemStack())
            this.icon = "minecraft:" + mat.toString().replace(" ", "_").toLowerCase();
        else
            this.icon = "minecraft:stone";

        return this;
    }

    public CMIAdvancement setData(int data) {
        this.data = data;
        return this;
    }

    public CMIAdvancement setBackground(AdvancementBackground background) {
        this.background = background;
        return this;
    }

    public CMIAdvancement setFrame(AdvancementFrameType frame) {
        this.frame = frame;
        return this;
    }

    public CMIAdvancement setAnnounce(boolean announce) {
        this.announce = announce;
        return this;
    }

    public CMIAdvancement setToast(boolean toast) {
        this.toast = toast;
        return this;
    }

    public CMIAdvancement setCustomModelData(int modelData) {
        this.customModelData = modelData;
        return this;
    }

    public CMIAdvancement setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public String getJSON() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(getJSONObject());
    }

    public JsonObject getJSONObject() {
        JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item", getIcon());
        if (Version.isCurrentLower(Version.v1_13_R1))
            icon.addProperty("data", getData());

        if (Version.isCurrentHigher(Version.v1_13_R1) && getCustomModelData() > 0)
            icon.addProperty("nbt", "{CustomModelData:" + getCustomModelData() + "}");

        JsonObject display = new JsonObject();
        display.add("icon", icon);

        RawMessage rm = new RawMessage();
        rm.addText(getTitle());
        try {
            String text = rm.getRaw();

            String[] split = text.split("extra\":");

            if (split.length > 1)
                text = split[1];
            else
                text = getTitle();
            if (text.endsWith("}]"))
                text = text.substring(0, text.length() - 2);
            display.add("title", gson.fromJson(text, JsonElement.class));
        } catch (Throwable e) {
            e.printStackTrace();
            // Outdated method just a fallback
            display.add("title", getJsonFromComponent(getTitle()));
        }

//        display.add("title", getJsonFromComponent(getTitle()));
        display.add("description", getJsonFromComponent(getDescription()));

        display.addProperty("background", getBackground().getUrl());
        display.addProperty("frame", getFrame().toString());
        display.addProperty("announce_to_chat", announce);
        display.addProperty("show_toast", toast);
        display.addProperty("hidden", hidden);

//      json.addProperty("parent", getParent());

        JsonObject criteria = new JsonObject();

        JsonObject triggerObj = new JsonObject();
        triggerObj.addProperty("trigger", "minecraft:impossible");
        criteria.add(identificator, triggerObj);

        json.add("criteria", criteria);
        json.add("display", display);

        return json;
    }

    public String getIcon() {
        return this.icon;
    }

    public int getData() {
        return this.data;
    }

    public int getCustomModelData() {
        return this.customModelData;
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

    public AdvancementBackground getBackground() {
        return this.background;
    }

    public AdvancementFrameType getFrame() {
        return this.frame;
    }

    public String getParent() {
        return this.parent;
    }

    public CMIAdvancement show(final Player... players) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
            CMILib.getInstance().getReflectionManager().showToast(this, players);
            return this;
        }

        add();
        grant(players);
        final CMIAdvancement ad = this;
        Bukkit.getScheduler().runTaskLater(CMI.getInstance(), new Runnable() {
            @Override
            public void run() {
                revoke(players);
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
}
