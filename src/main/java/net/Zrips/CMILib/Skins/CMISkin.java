package net.Zrips.CMILib.Skins;

import java.util.UUID;

public class CMISkin {

    private String name;
    private UUID uuid;
    private String skin;
    private String signature;
    private Long lastUpdate = 0L;

    public CMISkin(String name, UUID uuid, String skin, String signature) {
	this.name = name;
	this.uuid = uuid;
	this.skin = skin;
	this.signature = signature;
    }

    public String getSkin() {
	return skin;
    }

    public void setSkin(String skin) {
	this.skin = skin;
    }

    public String getSignature() {
	return signature;
    }

    public void setSignature(String signature) {
	this.signature = signature;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public Long getLastUpdate() {
	return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
	this.lastUpdate = lastUpdate;
    }

}
