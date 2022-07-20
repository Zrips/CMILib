package net.Zrips.CMILib.Container;

public enum CMIServerProperties {
    max_players, motd, rate_limit, viewDistance;
    public String getPath() {
	return this.name().replace("_", "-");
    }
}
