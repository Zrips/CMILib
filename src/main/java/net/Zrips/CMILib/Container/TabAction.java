package net.Zrips.CMILib.Container;

import net.Zrips.CMILib.TabComplete;

public enum TabAction {

    na,
    allPlayername("All players including vanished ones"),
    playername("All players excluding vanished ones"),
    mutedplayername("All online muted players"),
    damageCause("Damage cause variations"),
    bannedplayername("Banned player names"),
    gamemode("Game modes"),
    worlds("Worlds"),
    itemname("Materials"),
    EntityType("Entity Types"),
    cleanEntityType("Entity Types without _"),
    kit("Kit names by access"),
    kitnames("Kit config names by access"),
    kitp("Kits by preview access"),
    chatroom("Chat rooms"),
    biome("Biomes"),
    treeType("Tree types"),
    maxplayers("Server max player limit"),
    potioneffect("Potion effects"),
    effect("Particle effects"),
    merchants("Villager professions"),
    enchant("Enchant names"),
    halfViewRange("Half of max server view range"),
    doubleViewRange("Double of max server view range"),
    ViewRange("Server view range"),
    maxenchantlevel("Max enchant level. Uses previous variable to determine enchantment"),
    currentItemName("Item name in main hand"),
    loreLine("Lists numbers of lore lines of item in main hand"),
    currentItemLore("Lists lore of item in main hand"),
    currentX("Current player X position"),
    currentY("Current player Y position"),
    currentZ("Current player Z position"),
    currentWorld("World name player is in"),
    currentPitch("Players pitch"),
    currentYaw("Players yaw"),
    itemFlag("Item flag values"),
    nickName("Users display name"),
    nickNames("All online users nick names"),
    homes("Users home list"),
    warps("Warps by access to them"),
    allwarps("All warps"),
    playerwarps("Warps by access to them"),
    rankname("Rank names"),
    statstype("Statistics names"),
    statssubtype("Sub statistics names. Uses previous variable to determine main statistic"),
    motd("Servers motd"),
    bungeeserver("Bungee servers"),
    scheduleName("Schedule names"),
    ctext("Custom text names"),
    jail("Jail names"),
    cellId("Cewll id's. Uses previous variable to determine jail"),
    sound("Sound names"),
    customalias("Custom alias list"),
    dbusercollumsshort,
    placeholders("Placeholders"),
    warncategory("Warn categories"),
    projectiletype("Projectile types"),
    holograms("Hologram names"),
    mobtype("Mob types"),
    signLine("Sign line text. Uses previous variable to determine line number");

    private String desc;

    TabAction() {
    }

    TabAction(String desc) {
	this.desc = desc;
    }

    public static TabAction getAction(String name) {
	if (name.startsWith("_"))
	    return TabAction.na;
	TabAction got = TabComplete.map.get(name.toLowerCase());
	return got == null ? TabAction.na : got;
    }

    public String getDesc() {
	return desc;
    }
}
