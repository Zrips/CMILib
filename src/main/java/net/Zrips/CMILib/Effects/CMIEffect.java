package net.Zrips.CMILib.Effects;

import org.bukkit.Color;
import org.bukkit.util.Vector;

import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticle;
import net.Zrips.CMILib.Items.CMIMaterial;

public class CMIEffect {

    private CMIParticle particle;
    private Color color = Color.fromBGR(0, 0, 200);
    private CMIMaterial material = null;
    private Vector offset = new Vector();
    private int size = 1;
    private int amount = 1;
    private float speed = 0;

    public CMIEffect(CMIParticle particle) {
	this.particle = particle;
    }

    public CMIParticle getParticle() {
	if (particle == null)
	    particle = CMIParticle.DUST;
	return particle;
    }

    public void setParticle(CMIParticle particle) {
	this.particle = particle;
    }

    public Color getColor() {
	return color;
    }

    public void setColor(Color color) {
	this.color = color;
    }

    public Vector getOffset() {
	return offset;
    }

    public void setOffset(Vector offset) {
	this.offset = offset;
    }

    public int getAmount() {
	return amount;
    }

    public void setAmount(int amount) {
	this.amount = amount;
    }

    public float getSpeed() {
	return speed;
    }

    public void setSpeed(float speed) {
	this.speed = speed;
    }

    public int getSize() {
	return size;
    }

    public void setSize(int size) {
	this.size = size;
    }

    public CMIMaterial getMaterial() {
	return material;
    }

    public void setMaterial(CMIMaterial material) {
	this.material = material;
    }

}
