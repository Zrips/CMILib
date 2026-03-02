package net.Zrips.CMILib.Effects;

import net.Zrips.CMILib.Items.CMIMaterial;

public class CMIParticleMaterial extends CMIParticleOptions {

    private CMIMaterial material = CMIMaterial.STONE;

    public CMIParticleMaterial(CMIMaterial material) {
        this.material = material;
    }

    public CMIMaterial getMaterial() {
        return material;
    }

    public void setMaterial(CMIMaterial material) {
        this.material = material;
    }
}
