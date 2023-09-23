package net.Zrips.CMILib.Advancements;

public enum AdvancementBackground {
    ADVENTURE("minecraft:textures/gui/advancements/backgrounds/adventure.png"),
    END("minecraft:textures/gui/advancements/backgrounds/end.png"),
    HUSBANDRY("minecraft:textures/gui/advancements/backgrounds/husbandry.png"),
    NETHER("minecraft:textures/gui/advancements/backgrounds/nether.png"),
    STONE("minecraft:textures/gui/advancements/backgrounds/stone.png");

    private String url;

    AdvancementBackground(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
