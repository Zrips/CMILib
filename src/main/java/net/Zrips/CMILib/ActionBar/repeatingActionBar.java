package net.Zrips.CMILib.ActionBar;

import org.bukkit.entity.Player;

public class repeatingActionBar {

    private Long until = 0L;
    private Object packet;
    private Object connection;
    private Player player;
    private String message = null;

    public repeatingActionBar(Player player) {
        this.player = player;
    }

    public repeatingActionBar(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    public Long getUntil() {
        return until;
    }

    public void setUntil(Long until) {
        this.until = until;
    }

    public Object getConnection() {
        return connection;
    }

    public void setConnection(Object connection) {
        this.connection = connection;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Object getPacket() {
        return packet;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
