package net.Zrips.CMILib.Items;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.CMILibConfig;
import net.Zrips.CMILib.Entities.CMIEntityType;
import net.Zrips.CMILib.Entities.MobHeadInfo;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.NBT.CMINBT;

public class ItemListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        @NotNull
        ItemStack item = event.getItem().getItemStack();
        CMIMaterial type = CMIMaterial.get(item.getType());

        if (!type.isMonsterHead() && !type.isPlayerHead())
            return;

        CMINBT nbt = new CMINBT(item);

        String playerName = nbt.getString("SkullOwner.Name");

        if (playerName != null) {
            ItemMeta meta = item.getItemMeta();
            if (CMILibConfig.playerNameForItemStack)
                meta.setDisplayName(LC.info_playerHeadName.get("[playerName]", playerName));
            item.setItemMeta(meta);
        } else {
            List<String> textures = nbt.getList("SkullOwner.Properties.textures");
            if (textures != null) {
                String texture = textures.get(0).split("Value:\"", 2)[1].split("\"", 2)[0];
                CMIEntityType entityType = CMIEntityType.getByTexture(texture);
                if (entityType != null) {                    
                    MobHeadInfo headinfo = entityType.getHeadInfo(texture);
                    if (headinfo != null && headinfo.getCustomName() != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(LC.info_mobHeadName.get("[mobName]", headinfo.getCustomName()));
                        item.setItemMeta(meta);
                    } else {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(LC.info_mobHeadName.get("[mobName]", entityType.getTranslatedName()));
                        item.setItemMeta(meta);
                    }
                }
            }
        }

    }
}
