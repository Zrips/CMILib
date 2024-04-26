package net.Zrips.CMILib.PersistentData;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CMIItemPersistentDataContainer extends CMIPersistentDataContainer {

    private final ItemMeta meta;
    private final ItemStack item;

    public CMIItemPersistentDataContainer(ItemStack item) {
        this.item = item;
        this.meta = item == null ? null : item.getItemMeta();
        this.persistentDataContainer = this.meta == null ? null : this.meta.getPersistentDataContainer();
    }

    @Override
    void save() {
        item.setItemMeta(meta);
    }
}
