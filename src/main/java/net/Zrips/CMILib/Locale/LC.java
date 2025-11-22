package net.Zrips.CMILib.Locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.Zrips.CMILib.Messages.CMIMessages;

public enum LC {
	info_prefix("{gcp}[{gcn}CMI{gcp}] ", "Use !prefix! in any locale line to automatically include this prefix"),
	info_NoPermission("{gcw}You don't have permission!"),
	info_CantHavePermission("{gcw}You can't have this permission!"),
	info_WrongGroup("{gcw}You are in wrong group for this!"),
	info_NoPlayerPermission("{gcw}[playerName] doesn't have permission for: [permission]"),
	info_Ingame("{gcw}You can only use this in game!"),
	info_NoInformation("{gcw}No information found!"),
	info_Console("{gcp}Server"),
	info_FromConsole("{gcw}You can only use this in the console!"),
	info_NotOnline("{gcw}The player is not online!"),
	info_NobodyOnline("{gcw}There is no one online!"),
	info_NoPlayer("{gcw}Can't find player with this name! ({gcs}[name]{gcw})"),
	info_NoCommand("{gcw}There is no command by this name!"),
	info_cantFindCommand("{gcn}Couldn't find {gcs}[%1] {gcn}command, did you mean {gcs}[%2]{gcn}?"),
	info_nolocation("{gcp}Can't find suitable location"),
	info_FeatureNotEnabled("{gcw}This feature is not enabled!"),
	info_ModuleNotEnabled("{gcw}This module is not enabled!"),
	info_versionNotSupported("{gcw}Server version is not supported for this feature"),
	info_spigotNotSupported("{gcw}You need PaperSpigot branch type server for this to work"),
	info_bungeeNoGo("{gcw}This feature will not work on bungee network based servers"),
	info_clickToTeleport("{gcp}Click to teleport"),
	info_UseMaterial("{gcd}Please use material names!"),
	info_IncorrectMaterial("{gcd}Incorrect material name!"),
	info_UseInteger("{gcd}Please use numbers!"),
	info_UseBoolean("{gcd}Please use True or False!"),
	info_NoLessThan("{gcd}Number can't be less than {gcs}[amount]{gcd}!"),
	info_NoMoreThan("{gcd}Value can't be more than {gcs}[amount]"),
	info_NoWorld("{gcd}Can't find world with this name!"),
	info_IncorrectLocation("{gcd}Location defined incorrectly!"),
	info_Show("{gcp}Show"),
	info_Remove("{gcd}Remove"),
	info_Back("{gcp}Back"),
	info_Forward("{gcp}Forward"),
	info_Update("{gcp}Update"),
	info_Save("{gcp}Save"),
	info_Delete("{gcd}Delete"),
	info_Click("{gcp}Click"),
	info_Preview("{gcp}Preview"),
	info_PasteOld("{gcp}Paste old"),
	info_ClickToPaste("{gcp}Click to paste into chat"),
	info_CantTeleportWorld("{gcp}You can't teleport to this world"),
	info_CantTeleportNoWorld("{gcw}Target world doesn't exist. Teleportation canceled"),
	info_ClickToConfirmDelete("{gcp}Click to confirm removal of {gcs}[name]"),
	info_ClickToConfirm("{gcp}Click to confirm"),
	info_teleported("{gcp}You have been teleported."),
	info_PlayerSpliter("{gcp}----- {gcs}[playerDisplayName] {gcp}-----"),
	info_Spliter("{gcp}--------------------------------------------------"),
	info_SpliterValue("{gcp}------------------ {gcs}[value] {gcp}------------------"),
	info_singleSpliter("{gcp}-"),
	info_SpliterMiddle(" {gcs}[value] "),
	info_ListSpliter(", "),
	info_ProgressBarFill("{gcu}\u258F"),
	info_ProgressBarEmpty("{gcn}\u258F"),
	info_nothingInHand("!actionbar!{gcp}You need to hold item in hand"),
	info_nothingInHandLeather("{gcp}You need to hold leather item in hand"),
	info_nothingToShow("{gcp}Nothing to show"),
	info_noItem("{gcw}Can't find item"),
	info_dontHaveItem("{gcw}You don't have {gcs}[amount]x [itemName] {gcw}in your inventory"),
	info_wrongWorld("{gcw}Can't do this in this world"),
	info_differentWorld("{gcw}Different worlds"),
	info_HaveItem("{gcw}You have {gcs}[amount]x [itemName] {gcw}in your inventory"),
	info_cantDoInGamemode("{gcp}You can't do this in this game mode"),
	info_cantDoForPlayer("{gcp}You can't do this to {gcs}[playerDisplayName]"),
	info_cantDoForYourSelf("{gcp}You can't do this to yourself"),
	info_cantDetermineMobType("{gcw}Can't determine mob type from {gcs}[type] {gcw}variable"),
	info_cantRename("!actionbar!{gcp}You can't use this name"),
	info_confirmRedefine("{gcp}Click to confirm redefining"),
	info_cantEdit("{gcp}You can't edit this"),
	info_wrongName("{gcw}Wrong name"),
	info_unknown("unknown"),
	info_invalidName("{gcw}Invalid name"),
	info_alreadyexist("{gcd}This name is taken"),
	info_dontexist("{gcd}Nothing found by this name"),
	info_worldDontExist("{gcw}Target world can't be accessed anymore. Can't teleport you there!"),

	info_notSet("not set"),
	info_lookAtSign("{gcp}Look at sign"),
	info_lookAtBlock("{gcp}Look at block"),
	info_lookAtEntity("{gcp}Look at entity"),
	info_noSpace("{gcp}Not enough free space"),
	info_notOnGround("{gcp}You can't perform this while flying"),

	info_bungee_Online("{gcu}Online"),
	info_bungee_Offline("{gcn}Offline"),
	info_bungee_not("{gcw}Server doesn't belong to bungee network"),
	info_bungee_noserver("{gcw}Can't find server by this name!"),
	info_bungee_server("{gcp}Server: {gcs}[name]"),

	info_variables_am("AM"),
	info_variables_pm("PM"),

	info_variables_Online("{gcu}Online"),
	info_variables_Offline("{gcd}Offline"),
	info_variables_TrueColor("{gcu}"),
	info_variables_FalseColor("{gcd}"),
	info_variables_True("{gcu}True"),
	info_variables_False("{gcd}False"),
	info_variables_Enabled("{gcu}Enabled"),
	info_variables_Disabled("{gcd}Disabled"),
	info_variables_survival("Survival"),
	info_variables_creative("Creative"),
	info_variables_adventure("Adventure"),
	info_variables_spectator("Spectator"),
	info_variables_flying("Flying"),
	info_variables_notflying("Not flying"),

	info_Inventory_Full("{gcd}Your inventory is full. Can't add item"),
	info_Inventory_FullDrop("{gcd}Not all items fit in your inventory. They have been dropped on ground"),

	info_TimeNotRecorded("{gcp}-No record-"),
	info_months("{gcs}[months] {gcp}months "),
	info_oneMonth("{gcs}[months] {gcp}month "),
	info_weeks("{gcs}[weeks] {gcp}weeks "),
	info_oneWeek("{gcs}[weeks] {gcp}week "),
	info_years("{gcs}[years] {gcp}years "),
	info_oneYear("{gcs}[years] {gcp}year "),
	info_day("{gcs}[days] {gcp}days "),
	info_oneDay("{gcs}[days] {gcp}day "),
	info_hour("{gcs}[hours] {gcp}hours "),
	info_oneHour("{gcs}[hours] {gcp}hour "),
	info_min("{gcs}[mins] {gcp}min "),
	info_sec("{gcs}[secs] {gcp}sec "),

	info_nextPageConsole("{gcn}For next page perform {gcs}[command]"),
	info_prevPage("{gcn}----<< {gcp}Prev "),
	info_prevCustomPage("{gcn}----<< {gcp}[value] "),
	info_prevPageGui("{gcp}Previous page "),
	info_prevPageClean("{gcp}Prev "),
	info_prevPageOff("{gcn}----<< {gcp}Prev "),
	info_prevCustomPageOff("{gcn}----<< {gcp}[value] "),
	info_prevPageHover("{gcp}<<<"),
	info_firstPageHover("{gcp}|<"),
	info_nextPage("{gcp} Next {gcn}>>----"),
	info_nextCustomPage("{gcp} [value] {gcn}>>----"),
	info_nextPageGui("{gcp}Next Page"),
	info_nextPageClean("{gcp} Next"),
	info_nextPageOff("{gcp} Next {gcn}>>----"),
	info_nextCustomPageOff("{gcs} [value] {gcn}>>----"),
	info_nextPageHover("{gcp}>>>"),
	info_lastPageHover("{gcp}>|"),
	info_pageCount("{gcn}[current]{gcp}/{gcn}[total]"),
	info_pageCountHover("{gcs}[totalEntries] entries"),

	info_skullOwner("!actionbar!{gcp}Owner:&r [playerName]"),
//    info_playerHeadName("{gcp}Head of &7[playerName]"),
	info_mobHeadName("{gcp}Head of {gcs}[mobName]"),

	info_circle("{gcp}Circle"),
	info_square("{gcp}Square"),
	info_clear("{gcp}Clear"),

	info_protectedArea("{gcw}Protected area. Can't do this here."),

	info_valueToLong("{gcp}Value is too high. Max: [max]"),
	info_valueToShort("{gcp}Value is too low. Min: [min]"),

	info_pickIcon("{gcp}Pick icon"),

	dialog_signEditor("Sign Editor"),
	dialog_update("Update"),
	dialog_save("Save"),
	dialog_close("Close"),
	dialog_line("Line [line]"),

	direction_n("North"),
	direction_ne("North East"),
	direction_e("East"),
	direction_se("South East"),
	direction_s("South"),
	direction_sw("South West"),
	direction_w("West"),
	direction_nw("North West"),

	modify_middlemouse("{gcn}Middle mouse click to edit"),
	modify_qButtonEdit("{gcn}Click Q to edit"),
	modify_newItem("{gcp}Place new item here"),
	modify_newLine("{gcn}<NewLine>"),
	modify_newLineHover("{gcp}Add new line"),
	modify_newPage("{gcn}<NewPage>"),
	modify_newPageHover("{gcp}Create new page"),
	modify_removePage("{gcw}<RemovePage>"),
	modify_removePageHover("{gcw}Remove page"),
	modify_deleteSymbol(" {gcw}[X]"),
	modify_deleteSymbolHover("{gcw}Delete {gcs}[text]"),
	modify_extraEditSymbol(" {gcp}!"),
	modify_addSymbol("{gcu}[+]"),
	modify_addSymbolHover("{gcu}Add new"),
	modify_cancelSymbol(" {gcn}&l[X]"),
	modify_cancelSymbolHover("{gcn}Cancel"),
	modify_acceptSymbol(" {gcu}&l[✔]"),
	modify_acceptSymbolHover("{gcu}Accept"),
	modify_denySymbol(" {gcd}&l[X]"),
	modify_denySymbolHover("{gcw}Deny"),
	modify_enabledSymbol("{gcu}[+]"),
	modify_disabledSymbol("{gcw}[-]"),
	modify_enabled("{gcu}Enabled"),
	modify_disabled("{gcw}Disabled"),
	modify_running("{gcp}Running"),
	modify_paused("{gcw}Paused"),
	modify_editSymbol("{gcs}\u270E"),
	modify_editSymbolHover("{gcp}Edit {gcs}[text]"),
	modify_editLineColor("&f"),
	modify_listUpSymbol("{gcp}\u21D1"),
	modify_listUpSymbolHover("{gcp}Up"),
	modify_listDownSymbol("{gcp}\u21D3"),
	modify_listDownSymbolHover("{gcp}Down"),
	modify_listNumbering("{gcs}[number]. "),
	modify_listAlign("&80"),
	modify_ChangeHover("{gcp}Click to change"),
	modify_ChangeCommands("{gcp}Commands"),

	modify_enabledColor("{gcu}"),
	modify_disabledColor("{gcd}"),

	modify_commandTitle(" {gcp}--- {gcs}[name] {gcp}---"),
	modify_commandList(" {gcs}[command]  "),
	modify_emptyLine("{gcn}[Empty line]"),
	modify_commandEdit("{gcp}Edit list"),

	modify_nameAddInfo("{gcp}Enter new name. Type {gcs}cancel {gcp}to cancel"),
	modify_lineAddInfo("{gcp}Enter new line. Type {gcs}cancel {gcp}to cancel"),
	modify_commandAddInfo("{gcp}Enter new command. Type {gcs}cancel {gcp}to cancel"),
	modify_commandAddInformationHover("{gcp}[playerName] can be used to get player name \n"
			+ "{gcp}To include delay in commands: \n"
			+ "{gcp}delay! 5 \n"
			+ "{gcp}Specialized commands are supported. More info at \n"
			+ "{gcp}https://www.zrips.net/cmi/commands/specialized/"),
	modify_commandEditInfo("{gcp}Click to paste old text. Type {gcs}cancel {gcp}to cancel action"),
	modify_listLimit("{gcp}List can't be bigger than {gcs}[amount] {gcp}entries"),
	modify_commandEditInfoHover("{gcp}Click to paste old text"),

	teleportation_relocation("!actionbar!{gcd}Your teleport location was obstructed. You have been teleported to a safe location."),
	econ_noMoney("{gcw}You have no money"),
	econ_charged("!actionbar!{gcp}Charged: {gcs}[amount]"),
	econ_notEnoughMoney("{gcw}You don't have enough money. Required ({gcs}[amount]{gcw})"),
	econ_tooMuchMoney("{gcw}You have too much money"),

	Selection_SelectPoints("{gcw}Select 2 points with selection tool! AKA: {gcs}[tool]"),
	Selection_PrimaryPoint("{gcp}Placed {gcs}Primary {gcp}Selection Point [point]"),
	Selection_SecondaryPoint("{gcp}Placed {gcs}Secondary {gcp}Selection Point [point]"),
	Selection_CoordsTop("{gcp}X:{gcs}[x] {gcp}Y:{gcs}[y] {gcp}Z:{gcs}[z]"),
	Selection_CoordsBottom("{gcp}X:{gcs}[x] {gcp}Y:{gcs}[y] {gcp}Z:{gcs}[z]"),
	Selection_Area("{gcn}[world] {gcp}({gcs}[x1]:[y1]:[z1] {gcp}[x2]:[y2]:[z2]{gcp})"),

	Location_Title("{gcp}Players location"),
	Location_Killer("{gcp}Killer: {gcs}[killer]"),
	Location_OneLiner("{gcp}Location: {gcs}[location]"),
	Location_DeathReason("{gcp}Death Reason: {gcs}[reason]"),
	Locations("{gcp}Locations: "),
	Location_Full("{gcp}[world] {gcs}[x]{gcp}:{gcs}[y]{gcp}:{gcs}[z]"),
	Location_World("{gcp}World: {gcs}[world]"),
	Location_X("{gcp}X: {gcs}[x]"),
	Location_Y("{gcp}Y: {gcs}[y]"),
	Location_Z("{gcp}Z: {gcs}[z]"),
	Location_Pitch("{gcp}Pitch: {gcs}[pitch]"),
	Location_Yaw("{gcp}Yaw: {gcs}[yaw]"),

	info_Spawner("&r[type] Spawner"),
	;

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
