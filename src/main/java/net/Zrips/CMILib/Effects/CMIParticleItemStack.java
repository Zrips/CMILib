package net.Zrips.CMILib.Effects;

import net.Zrips.CMILib.Items.CMIMaterial;

public class CMIParticleItemStack extends CMIParticleMaterial {
    public CMIParticleItemStack(CMIMaterial material) {
        super(material == null || !material.isValidAsItemStack() ? CMIMaterial.STICK : material);
    }
    
    public void setMaterial(CMIMaterial material) {
        if (material == null || !material.isValidAsItemStack())
            material = CMIMaterial.STICK;
        super.setMaterial(material);
    }
}
