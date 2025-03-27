package net.Zrips.CMILib.Container;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.Version.Version;

public class CMIPlayerInventory {

    private static Method topInventory = null;

    public static @NotNull ItemStack[] getTopInventoryContents(Player player) {
        Inventory inv = getTopInventory(player);
        if (inv == null)
            return new ItemStack[54];
        return inv.getContents();
    }

    public static @Nullable Inventory getTopInventory(Player player) {
        if (Version.isCurrentEqualOrHigher(Version.v1_21_R1))
            return player.getOpenInventory().getTopInventory();

        // As of later versions InventoryView changed from class to interface
        try {
            InventoryView inv = player.getOpenInventory();
            if (topInventory == null)
                topInventory = InventoryView.class.getMethod("getTopInventory");
            return ((Inventory) topInventory.invoke(inv));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

}
