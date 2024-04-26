package net.Zrips.CMILib.PersistentData;

import org.bukkit.entity.Entity;

public class CMIEntityPersistentDataContainer extends CMIPersistentDataContainer {

    public CMIEntityPersistentDataContainer(Entity entity) {
        this.persistentDataContainer = entity == null ? null : entity.getPersistentDataContainer();
    }

    @Override
    void save() {
    }
}
