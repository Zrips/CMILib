package net.Zrips.CMILib.Container;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.Container.PageInfo;
import net.Zrips.CMILib.GUI.CMIGui;
import net.Zrips.CMILib.GUI.CMIGuiButton;
import net.Zrips.CMILib.GUI.GUIManager.GUIClickType;
import net.Zrips.CMILib.Items.CMIMaterial;

public class MaterialPicker {

    Player player;

    public MaterialPicker(Player player) {
        this.player = player;
    }

    public boolean validMaterial(CMIMaterial one) {
        return one.isValidAsItemStack();
    }

    public void pickedMaterial(GUIClickType type, CMIMaterial material) {
    }

    public void onUIClose() {

    }

    public boolean openPicker(int page, String title) {

        CMIGui gui = new CMIGui(player) {
            @Override
            public void pageChange(int page) {
                openPicker(page, title);
            }

            @Override
            public void onClose() {
                onUIClose();
            }
        };
        gui.setTitle(title);

        Map<String, CMIMaterial> map = new TreeMap<>();

        for (CMIMaterial one : CMIMaterial.values()) {
            if (!validMaterial(one))
                continue;
            map.put(one.toString(), one);
        }

        PageInfo pi = new PageInfo(45, map.size(), page);

        for (CMIMaterial one : map.values()) {
            if (!validMaterial(one))
                continue;

            if (pi.isContinue())
                continue;
            if (pi.isBreak())
                break;

            CMIGuiButton button = new CMIGuiButton(one) {
                @Override
                public void click(GUIClickType type) {
                    pickedMaterial(type, one);
                }
            };

            gui.addButton(button);
        }

        gui.addPagination(pi);

        gui.fillEmptyButtons();
        gui.open();
        return true;
    }

}
