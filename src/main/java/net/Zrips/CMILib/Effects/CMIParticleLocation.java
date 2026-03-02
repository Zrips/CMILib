package net.Zrips.CMILib.Effects;

import net.Zrips.CMILib.Container.CMIVector3D;

public class CMIParticleLocation extends CMIParticleOptions {

    private CMIVector3D offset = new CMIVector3D(0, 0, 0);

    public CMIParticleLocation() {
    }

    public CMIParticleLocation(CMIVector3D offset) {
        setOffset(offset);
    }

    public CMIVector3D getOffset() {
        return offset;
    }

    public void setOffset(CMIVector3D offset) {
        this.offset = offset == null ? this.offset : offset;
    }
}
