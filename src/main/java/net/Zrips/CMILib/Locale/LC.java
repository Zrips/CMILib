package net.Zrips.CMILib.Locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.Zrips.CMILib.Messages.CMIMessages;

public enum LC {
    info_prefix("&e[&aCMI&e] ", "Use !prefix! in any locale line to automatically include this prefix"),
    info_NoPermission("&cYou don't have permission!"),
    info_CantHavePermission("&cYou can't have this permission!"),
    info_WrongGroup("&cYou are in wrong group for this!"),
    info_NoPlayerPermission("&c[playerName] doesn't have permission for: [permission]"),
    info_Ingame("&cYou can only use this in game!"),
    info_NoInformation("&cNo information found!"),
    info_Console("&6Server"),
    info_FromConsole("&cYou can only use this in the console!"),
    info_NotOnline("&cThe player is not online!"),
    info_NobodyOnline("&cThere is no one online!"),
    info_NoPlayer("&cCan't find player with this name!"),
    info_NoCommand("&cThere is no command by this name!"),
    info_cantFindCommand("&5Couldn't find &7[%1]&5 command, did you mean &7[%2]&5?"),
    info_nolocation("&4Can't find suitable location"),
    info_FeatureNotEnabled("&cThis feature is not enabled!"),
    info_ModuleNotEnabled("&cThis module is not enabled!"),
    info_versionNotSupported("&cServer version is not supported for this feature"),
    info_spigotNotSupported("&cYou need PaperSpigot branch type server for this to work"),
    info_bungeeNoGo("&cThis feature will not work on bungee network based servers"),
    info_clickToTeleport("&eClick to teleport"),
    info_UseMaterial("&4Please use material names!"),
    info_IncorrectMaterial("&4Incorrect material name!"),
    info_UseInteger("&4Please use numbers!"),
    info_UseBoolean("&4Please use True or False!"),
    info_NoLessThan("&4Number can't be less than [amount]!"),
    info_NoMoreThan("&4Value can't be more than [amount]"),
    info_NoWorld("&4Can't find world with this name!"),
    info_IncorrectLocation("&4Location defined incorrectly!"),
    info_Show("&eShow"),
    info_Remove("&cRemove"),
    info_Back("&eBack"),
    info_Forward("&eForward"),
    info_Update("&eUpdate"),
    info_Save("&eSave"),
    info_Delete("&cDelete"),
    info_Click("&eClick"),
    info_Preview("&ePreview"),
    info_PasteOld("&ePaste old"),
    info_ClickToPaste("&eClick to paste into chat"),
    info_CantTeleportWorld("&eYou can't teleport to this world"),
    info_CantTeleportNoWorld("&cTarget world doesn't exist. Teleportation canceled"),
    info_ClickToConfirmDelete("&eClick to confirm removal of &6[name]"),
    info_teleported("&eYou have been teleported."),
    info_PlayerSpliter("&e----- &6[playerDisplayName] &e-----"),
    info_Spliter("&e--------------------------------------------------"),
    info_SpliterValue("&e------------------ &6[value] &e------------------"),
    info_singleSpliter("&e-"),
    info_SpliterMiddle(" &6[value] "),
    info_ListSpliter(", "),
    info_ProgressBarFill("&2\u258F"),
    info_ProgressBarEmpty("&e\u258F"),
    info_nothingInHand("!actionbar!&eYou need to hold item in hand"),
    info_nothingInHandLeather("&eYou need to hold leather item in hand"),
    info_nothingToShow("&eNothing to show"),
    info_noItem("&cCan't find item"),
    info_dontHaveItem("&cYou don't have &6[amount]x [itemName] &cin your inventory"),
    info_wrongWorld("&cCan't do this in this world"),
    info_differentWorld("&cDifferent worlds"),
    info_HaveItem("&cYou have &6[amount]x [itemName] &cin your inventory"),
    info_cantDoInGamemode("&eYou can't do this in this game mode"),
    info_cantDoForPlayer("&eYou can't do this to &6[playerDisplayName]"),
    info_cantDoForYourSelf("&eYou can't do this to yourself"),
    info_cantDetermineMobType("&cCan't determine mob type from &e[type] &cvariable"),
    info_cantRename("!actionbar!&eYou can't use this name"),
    info_confirmRedefine("&eClick to confirm redefining"),
    info_cantEdit("&eYou can't edit this"),
    info_wrongName("&cWrong name"),
    info_unknown("unknown"),
    info_invalidName("&cInvalid name"),
    info_alreadyexist("&4This name is taken"),
    info_dontexist("&4Nothing found by this name"),
    info_worldDontExist("&cTarget world can't be accessed anymore. Can't teleport you there!"),

    info_notSet("not set"),
    info_lookAtSign("&eLook at sign"),
    info_lookAtBlock("&eLook at block"),
    info_lookAtEntity("&eLook at entity"),
    info_noSpace("&eNot enough free space"),
    info_notOnGround("&eYou can't perform this while flying"),

    info_bungee_Online("&6Online"),
    info_bungee_Offline("&cOffline"),
    info_bungee_not("&cServer doesn't belong to bungee network"),
    info_bungee_noserver("&cCan't find server by this name!"),
    info_bungee_server("&eServer: &7[name]"),

    info_variables_am("&eAM"),
    info_variables_pm("&ePM"),

    info_variables_Online("&6Online"),
    info_variables_Offline("&cOffline"),
    info_variables_TrueColor("&2"),
    info_variables_FalseColor("&4"),
    info_variables_True("&6True"),
    info_variables_False("&cFalse"),
    info_variables_Enabled("&6Enabled"),
    info_variables_Disabled("&cDisabled"),
    info_variables_survival("&6Survival"),
    info_variables_creative("&6Creative"),
    info_variables_adventure("&6Adventure"),
    info_variables_spectator("&6Spectator"),
    info_variables_flying("&6Flying"),
    info_variables_notflying("&6Not flying"),

    info_Inventory_Full("&5Your inventory is full. Can't add item"),
    info_Inventory_FullDrop("&5Not all items fit in your inventory. They have been dropped on ground"),

    info_TimeNotRecorded("&e-No record-"),
    info_months("&e[months] &6months "),
    info_oneMonth("&e[months] &6month "),
    info_weeks("&e[weeks] &6weeks "),
    info_oneWeek("&e[weeks] &6week "),
    info_years("&e[years] &6years "),
    info_oneYear("&e[years] &6year "),
    info_day("&e[days] &6days "),
    info_oneDay("&e[days] &6day "),
    info_hour("&e[hours] &6hours "),
    info_oneHour("&e[hours] &6hour "),
    info_min("&e[mins] &6min "),
    info_sec("&e[secs] &6sec "),

    info_nextPageConsole("&fFor next page perform &5[command]"),
    info_prevPage("&2----<< &6Prev "),
    info_prevCustomPage("&2----<< &6[value] "),
    info_prevPageGui("&6Previous page "),
    info_prevPageClean("&6Prev "),
    info_prevPageOff("&2----<< &7Prev "),
    info_prevCustomPageOff("&2----<< &7[value] "),
    info_prevPageHover("&7<<<"),
    info_firstPageHover("&7|<"),
    info_nextPage("&6 Next &2>>----"),
    info_nextCustomPage("&6 [value] &2>>----"),
    info_nextPageGui("&6Next Page"),
    info_nextPageClean("&6 Next"),
    info_nextPageOff("&7 Next &2>>----"),
    info_nextCustomPageOff("&7 [value] &2>>----"),
    info_nextPageHover("&7>>>"),
    info_lastPageHover("&7>|"),
    info_pageCount("&2[current]&7/&2[total]"),
    info_pageCountHover("&e[totalEntries] entries"),

    info_skullOwner("!actionbar!&7Owner:&r [playerName]"),

    info_circle("&3Circle"),
    info_square("&5Square"),
    info_clear("&7Clear"),

    info_protectedArea("&cProtected area. Can't do this here."),

    info_valueToLong("&eValue is too high. Max: [max]"),
    info_valueToShort("&eValue is too low. Min: [min]"),

    info_pickIcon("&8Pick icon"),

    direction_n("North"),
    direction_ne("North East"),
    direction_e("East"),
    direction_se("South East"),
    direction_s("South"),
    direction_sw("South West"),
    direction_w("West"),
    direction_nw("North West"),

    modify_middlemouse("&2Middle mouse click to edit"),
    modify_qButtonEdit("&2Click Q to edit"),
    modify_newItem("&7Place new item here"),
    modify_newLine("&2<NewLine>"),
    modify_newLineHover("&2Add new line"),
    modify_newPage("&2<NewPage>"),
    modify_newPageHover("&2Create new page"),
    modify_removePage("&c<RemovePage>"),
    modify_removePageHover("&cRemove page"),
    modify_deleteSymbol(" &c[X]"),
    modify_deleteSymbolHover("&cDelete &e[text]"),
    modify_extraEditSymbol(" &6!"),
    modify_addSymbol("&2[+]"),
    modify_addSymbolHover("&2Add new"),
    modify_cancelSymbol(" &7&l[X]"),
    modify_cancelSymbolHover("&aCancel"),
    modify_acceptSymbol(" &2&l[✔]"),
    modify_acceptSymbolHover("&aAccept"),
    modify_denySymbol(" &4&l[X]"),
    modify_denySymbolHover("&cDeny"),
    modify_enabledSymbol("&2[+]"),
    modify_disabledSymbol("&c[-]"),
    modify_enabled("&2Enabled"),
    modify_disabled("&cDisabled"),
    modify_running("&2Running"),
    modify_paused("&cPaused"),
    modify_editSymbol("&e\u270E"),
    modify_editSymbolHover("&eEdit &6[text]"),
    modify_editLineColor("&f"),
    modify_listUpSymbol("&6\u21D1"),
    modify_listUpSymbolHover("&eUp"),
    modify_listDownSymbol("&6\u21D3"),
    modify_listDownSymbolHover("&eDown"),
    modify_listNumbering("&e[number]. "),
    modify_listAlign("&80"),
    modify_ChangeHover("&eClick to change"),
    modify_ChangeCommands("&eCommands"),

    modify_enabledColor("&6"),
    modify_disabledColor("&7"),

    modify_commandTitle(" &e--- &6[name] &e---"),
    modify_commandList(" &e[command]  "),
    modify_emptyLine("&7[Empty line]"),
    modify_commandEdit("&eEdit list"),

    modify_nameAddInfo("&eEnter new name. Type &6cancel &eto cancel"),
    modify_lineAddInfo("&eEnter new line. Type &6cancel &eto cancel"),
    modify_commandAddInfo("&eEnter new command. Type &6cancel &eto cancel"),
    modify_commandAddInformationHover("&e[playerName] can be used to get player name \n"
	+ "&eTo include delay in commands: \n"
	+ "&edelay! 5 \n"
	+ "&eSpecialized commands are supported. More info at \n"
	+ "&ehttps://www.zrips.net/cmi/commands/specialized/"),
    modify_commandEditInfo("&eClick to paste old text. Type &6cancel &eto cancel action"),
    modify_listLimit("&eList can't be bigger than &6[amount] &eentries"),
    modify_commandEditInfoHover("&eClick to paste old text"),

    teleportation_relocation("!actionbar!&4Your teleport location was obstructed. You have been teleported to a safe location."),
    econ_noMoney("&cYou have no money"),
    econ_charged("!actionbar!&fCharged: &6[amount]"),
    econ_notEnoughMoney("&cYou dont have enough money. Required (&6[amount]&c)"),
    econ_tooMuchMoney("&cYou have too much money"),

    Selection_SelectPoints("&cSelect 2 points with selection tool! AKA: &6[tool]"),
    Selection_PrimaryPoint("&ePlaced &6Primary &eSelection Point [point]"),
    Selection_SecondaryPoint("&ePlaced &6Secondary &eSelection Point [point]"),
    Selection_CoordsTop("&eX:&6[x] &eY:&6[y] &eZ:&6[z]"),
    Selection_CoordsBottom("&eX:&6[x] &eY:&6[y] &eZ:&6[z]"),
    Selection_Area("&7[world] &f(&6[x1]:[y1]:[z1] &e[x2]:[y2]:[z2]&f)"),

    Location_Title("&8Players location"),
    Location_Killer("&eKiller: &6[killer]"),
    Location_OneLiner("&eLocation: &6[location]"),
    Location_DeathReason("&eDeath Reason: &6[reason]"),
    Locations("&7Locations: "),
    Location_Full("&7[world] &f[x]&7:&f[y]&7:&f[z]"),
    Location_World("&eWorld: &6[world]"),
    Location_X("&eX: &6[x]"),
    Location_Y("&eY: &6[y]"),
    Location_Z("&eZ: &6[z]"),
    Location_Pitch("&ePitch: &6[pitch]"),
    Location_Yaw("&eYaw: &6[yaw]");

    private String text;
    private List<String> comments = new ArrayList<String>();

    private LC(String text) {
	this(text, "");
    }

//    private LC(String text, String comment) {
//	this.text = text;
//	if (comment != null)
//	    comments.add(comment);
//    }

    private LC(String text, String... comment) {
	this.text = text;
	if (comment != null && comment.length > 0)
	    for (String one : comment) {
		if (one.isEmpty())
		    continue;
		comments.add(one);
	    }
    }

    private LC(List<String> ls) {
	this(ls, "");
    }

    private LC(List<String> ls, String... comment) {
	if (this.text == null)
	    this.text = "";
	for (String one : ls) {
	    if (!this.text.isEmpty())
		this.text += " /n";
	    this.text += one;
	}

	if (comment != null && comment.length > 0)
	    for (String one : comment) {
		if (one.isEmpty())
		    continue;
		comments.add(one);
	    }
    }

    public String getText() {
	return text;
    }

    public String getPt() {
	return this.name().replace("_", ".");
    }

    public List<String> getComments() {
	return comments;
    }

    public String get(Object... values) {
	return CMIMessages.getMsg(this, values);
    }
    
    public String getLocale(Object... values) {
	return CMIMessages.getMsg(this, values);
    }

    public void sendMessage(Object sender, Object... variables) {
	CMIMessages.sendMessage(sender, this, variables);
    }
}
