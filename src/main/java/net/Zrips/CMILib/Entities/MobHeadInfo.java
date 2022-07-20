package net.Zrips.CMILib.Entities;

import java.util.ArrayList;
import java.util.List;

public class MobHeadInfo {

    private String customName = null;

    private List<CMIEntitySubType> criterias = new ArrayList<CMIEntitySubType>();
    
    private List<String> lore = new ArrayList<String>();

    public MobHeadInfo() {
    }

    public String getCustomName() {
	return customName;
    }

    public void setCustomName(String customName) {
	this.customName = customName;
    }

    public List<CMIEntitySubType> getCriterias() {
	return criterias;
    }

    public void setCriterias(List<CMIEntitySubType> criterias) {
	this.criterias = criterias;
    }

    public void addCriterias(CMIEntitySubType criteria) {
	this.criterias.add(criteria);
    }

    public List<String> getLore() {
	return lore;
    }

    public void setLore(List<String> lore) {
	this.lore = lore;
    }

}
