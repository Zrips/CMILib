package net.Zrips.CMILib.Effects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Chat.ChatMessageEdit;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMINumber;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.Container.CMIVector3D;
import net.Zrips.CMILib.Container.MaterialPicker;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticle;
import net.Zrips.CMILib.GUI.CMIGUIUtil;
import net.Zrips.CMILib.GUI.CMIGui;
import net.Zrips.CMILib.GUI.CMIGuiButton;
import net.Zrips.CMILib.GUI.GUIManager.GUIClickType;
import net.Zrips.CMILib.GUI.GUIManager.GUIRows;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Locale.LC;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.Zrips.CMILib.Version.Schedulers.CMITask;

public class CMIEffectPicker {

    private CMIEffect effect = new CMIEffect(CMIParticle.DUST);
    private Player player = null;
    private int tick = 0;
    private int tickRate = 1;
    private boolean showSerializeButton = false;
    private CMITask task = null;

    private static Map<UUID, CMIEffectPicker> openPickers = new HashMap<>();

    private void addOpenPicker(CMIEffectPicker picker) {
        if (picker.player == null)
            return;
        openPickers.put(picker.player.getUniqueId(), picker);
    }

    private static void recheckCache() {
        openPickers.entrySet().removeIf(entry -> {
            Player p = Bukkit.getPlayer(entry.getKey());
            return p == null || !p.isOnline();
        });
    }

    public CMIEffectPicker(Player player) {
        this(player, null);
    }

    public CMIEffectPicker(Player player, CMIParticle particle) {

        this.player = player;

        if (particle != null && particle.isParticle())
            effect = new CMIEffect(particle);
        else if (player != null && openPickers.containsKey(player.getUniqueId())) {
            CMIEffectPicker existingPicker = openPickers.get(player.getUniqueId());
            if (existingPicker != null) {
                effect = existingPicker.getEffect();
                tickRate = existingPicker.tickRate;
            }
        }
        addOpenPicker(this);
    }

    public void onUIClose() {

    }

    public void onUpdate() {

    }

    public void onSerializeClick() {

    }

    private Location getNextLocation() {
        Location eye = player.getEyeLocation();
        Vector forward = eye.getDirection().normalize();

        Vector up = new Vector(0, 1, 0);

        if (Math.abs(forward.dot(up)) > 0.99) {
            up = new Vector(1, 0, 0);
        }

        Vector right = forward.clone().crossProduct(up).normalize();
        Vector realUp = right.clone().crossProduct(forward).normalize();

        double radius = 2;
        double angle = -(tick / tickRate) * 0.2;

        double x = radius * Math.cos(angle);
        double y = radius * Math.sin(angle);

        Vector circleOffset = right.multiply(x).add(realUp.multiply(y));

        return eye.add(forward.clone().multiply(3.5)).add(circleOffset);
    }

    private void showParticle() {
        if (task != null) {
            task.cancel();
        }

        task = CMIScheduler.runTimerAsync(CMILib.getInstance(), () -> {

            tick++;

            if (tick % tickRate != 0)
                return;

            getEffect().show(player, getNextLocation());

        }, 1, 1);

    }

    private void stopParticle() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private CMIGuiButton createbutton(int slot, CMIParticle part) {
        CMIGuiButton button = new CMIGuiButton(slot, part.getSafeIcon()) {
            @Override
            public void click(GUIClickType type) {
                getEffect().setParticle(part);
                onUpdate();
                open();
            }
        };
        button.setName("&7" + part.getName());
        button.hideItemFlags();
        return button;
    }

    private void updateTitle(CMIGui gui) {
        if (gui == null)
            return;
        gui.updateTitle(getEffect().getParticle().getName());
    }

    public boolean open() {

        CMIGui gui = new CMIGui(player) {
            @Override
            public void onClose() {
                stopParticle();
                onUIClose();
                recheckCache();
            }
        };

        CMIParticle currentParticle = getEffect().getParticle();

        updateTitle(gui);

        gui.setInvSize(GUIRows.r6);

        CMIParticle prev = currentParticle.getPrevParticleEffect();
        gui.addButton(createbutton(3, prev));
        prev = prev.getPrevParticleEffect();
        gui.addButton(createbutton(2, prev));
        prev = prev.getPrevParticleEffect();
        gui.addButton(createbutton(1, prev));
        prev = prev.getPrevParticleEffect();
        gui.addButton(createbutton(0, prev));

        CMIParticle next = currentParticle.getNextParticleEffect();

        gui.addButton(createbutton(5, next));
        next = next.getNextParticleEffect();
        gui.addButton(createbutton(6, next));
        next = next.getNextParticleEffect();
        gui.addButton(createbutton(7, next));
        next = next.getNextParticleEffect();
        gui.addButton(createbutton(8, next));

        CMIGuiButton button = new CMIGuiButton(13, currentParticle.getSafeIcon()) {

            @Override
            public void click(GUIClickType type) {
                switch (type) {
                case Left:
                case LeftShift:
                    getEffect().setParticle(getEffect().getParticle().getNextParticleEffect());
                    break;
                case MiddleMouse:
                case Q:
                    break;
                case Right:
                case RightShift:
                    getEffect().setParticle(getEffect().getParticle().getPrevParticleEffect());
                    break;
                default:
                    break;
                }
                open();
            }

            @Override
            public void updateLooks() {
                setName("&7" + currentParticle.getName());
            }
        };
        button.hideItemFlags();
        gui.addButton(button);

        button = new CMIGuiButton(45, CMIMaterial.STICK) {
            @Override
            public void click(GUIClickType type) {

                if (type.equals(GUIClickType.Q) || type.equals(GUIClickType.MiddleMouse)) {
                    tickRate = 1;
                } else {
                    tickRate = CMINumber.clamp(tickRate + returnChange(type), 1);
                }
                updateLooks();
                update(gui);
                updateTitle(gui);
            }

            @Override
            public void updateLooks() {
                setName("Update speed: " + tickRate);
            }
        };
        gui.addButton(button);

        if (this.isShowSerializeButton()) {
            button = new CMIGuiButton(49, CMIMaterial.GREEN_WOOL) {
                @Override
                public void click(GUIClickType type) {
                    RawMessage rm = new RawMessage();
                    rm.addText(getEffect().serialize());
                    rm.addSuggestion(getEffect().serialize());
                    rm.show(player);
                    onSerializeClick();
                }

                @Override
                public void updateLooks() {
                    setName("Serialize and show in chat");
                }
            };
            gui.addButton(button);
        }

        button = new CMIGuiButton(21, CMIMaterial.CHEST) {
            @Override
            public void click(GUIClickType type) {

                if (type.equals(GUIClickType.Q) || type.equals(GUIClickType.MiddleMouse)) {
                    getEffect().setAmount(1);
                } else {
                    getEffect().setAmount(CMINumber.clamp(getEffect().getAmount() + returnChange(type), 1));
                }
                getEffect().setParticleParameters(null);
                updateLooks();
                update(gui);
                updateTitle(gui);
            }

            @Override
            public void updateLooks() {
                setName("Amount: " + getEffect().getAmount());
            }
        };
        gui.addButton(button);

        button = new CMIGuiButton(22, CMIMaterial.ACACIA_BOAT) {
            @Override
            public void click(GUIClickType type) {

                switch (type) {
                case MiddleMouse:
                case Q:
                    getEffect().setSpeed(0);
                    break;
                default:
                    getEffect().setSpeed(CMINumber.clamp(Math.round((getEffect().getSpeed() + (returnChange(type) / 10F)) * 1000) / 1000F, -100f));
                    break;
                }

                getEffect().setParticleParameters(null);

                updateLooks();
                update(gui);
                updateTitle(gui);
            }

            @Override
            public void updateLooks() {
                setName("Speed: " + getEffect().getSpeed());
            }
        };
        gui.addButton(button);

        button = new CMIGuiButton(23, CMIMaterial.STONE_BRICK_WALL) {
            @Override
            public void click(GUIClickType type) {
                switch (type) {
                case Q:
                    getEffect().setOffset(new Vector(0, 0, 0));
                    updateLooks();
                    update(gui);
                    getEffect().resetParticleParameters(player.getLocation());
                    updateTitle(gui);
                    break;
                default:

                    CMIGUIUtil.openVectorEditor(player, new CMIVector3D(getEffect().getOffset()), true, 0.01, 0, b -> open(), c -> {

                        c.setX(CMINumber.clamp(c.getX(), -999, 999));
                        c.setY(CMINumber.clamp(c.getY(), -999, 999));
                        c.setZ(CMINumber.clamp(c.getZ(), -999, 999));

                        getEffect().setOffset(new Vector(c.getX(), c.getY(), c.getZ()));

                        getEffect().setParticleParameters(null);
                        updateTitle(gui);
                    });
                    break;
                }
            }

            @Override
            public void updateLooks() {
                setName(LC.info_fullVector.getLocale(getEffect().getOffset()));
            }
        };
        button.addLore(LC.info_reset.getLocale());
        gui.addButton(button);

        @NotNull
        CMIParticleOptions options = getEffect().getOptions();

        if (options instanceof CMIParticleColor) {
            CMIParticleColor option = (CMIParticleColor) options;
            gui.addButton(addColorButton(28, "{gcp}Color from: [color]Example", player, option.getColor(), newColor -> option.setColor(newColor.getRGBColor())));
        } else if (options instanceof CMIParticleTrail) {
            CMIParticleTrail option = (CMIParticleTrail) options;
            gui.addButton(addColorButton(28, "{gcp}Color from: [color]Example", player, option.getColor(), newColor -> option.setColor(newColor.getRGBColor())));
        }

        if (options instanceof CMIParticleDustTransition) {
            CMIParticleDustTransition option = (CMIParticleDustTransition) options;
            gui.addButton(addColorButton(29, "{gcp}Color to: [color]Example", player, option.getColorTo(), newColor -> option.setColorTo(newColor.getRGBColor())));
        }

        if (options instanceof CMIParticleLocation) {
            CMIParticleLocation option = (CMIParticleLocation) options;

            button = new CMIGuiButton(30, CMIMaterial.STONE_BRICK_WALL) {
                @Override
                public void click(GUIClickType type) {
                    switch (type) {
                    case Q:
                        option.setOffset(new CMIVector3D(player.getLocation().toVector()));
                        updateLooks();
                        update(gui);
                        getEffect().resetParticleParameters(player.getLocation());
                        updateTitle(gui);
                        break;
                    default:

                        CMIVector3D offset = option.getOffset();

                        CMIGUIUtil.openVectorEditor(player, offset, true, 0.01, 0, b -> open(), c -> {

                            c.setX(CMINumber.clamp(c.getX(), -999, 999));
                            c.setY(CMINumber.clamp(c.getY(), -999, 999));
                            c.setZ(CMINumber.clamp(c.getZ(), -999, 999));

                            option.setOffset(c);
                            getEffect().setParticleParameters(null);
                            updateTitle(gui);
                        });
                        break;
                    }
                }

                @Override
                public void updateLooks() {
                    setName(LC.info_fullVector.getLocale(option.getOffset()));
                }
            };
            button.addLore(LC.info_reset.getLocale());
            gui.addButton(button);
        }

        if (options instanceof CMIParticleMaterial) {
            CMIParticleMaterial option = (CMIParticleMaterial) options;

            CMIMaterial mat = option.getMaterial() == null ? CMIMaterial.STONE : option.getMaterial();

            button = new CMIGuiButton(31, mat) {
                @Override
                public void click(GUIClickType type) {

                    MaterialPicker picker = new MaterialPicker(player) {
                        @Override
                        public void pickedMaterial(GUIClickType type, CMIMaterial material) {

                            if (material == null)
                                return;

                            if (options instanceof CMIParticleBlockData) {
                                ((CMIParticleBlockData) options).setMaterial(material);
                            } else if (options instanceof CMIParticleItemStack) {
                                ((CMIParticleItemStack) options).setMaterial(material);
                            }

                            getEffect().setParticleParameters(null);
                            CMIScheduler.runTask(CMILib.getInstance(), () -> open());
                            updateTitle(gui);
                        }

                        @Override
                        public boolean validMaterial(CMIMaterial one) {

                            if (one.equals(CMIMaterial.STONE) && Material.STONE.equals(one.getMaterial()))
                                return true;

                            if (!one.equals(CMIMaterial.STONE) && Material.STONE.equals(one.getMaterial()))
                                return false;

                            if (options instanceof CMIParticleBlockData)
                                return one.getMaterial() != null && one.getMaterial().isBlock();

                            return one.getMaterial() != null && !one.getMaterial().isBlock();
                        }

                        @Override
                        public void onUIClose() {
                            getEffect().setParticleParameters(null);
                            CMIScheduler.runTask(CMILib.getInstance(), () -> open());
                        }
                    };
                    picker.openPicker(1, LC.info_pickIcon.getLocale());
                }

                @Override
                public void updateLooks() {

                }
            };
            button.setName(mat.toString());
            gui.addButton(button);

        }

        if (options instanceof CMIParticleTrail) {
            CMIParticleTrail option = (CMIParticleTrail) options;
            button = new CMIGuiButton(32, CMIMaterial.GREEN_WOOL) {
                @Override
                public void click(GUIClickType type) {

                    if (type.equals(GUIClickType.Q) || type.equals(GUIClickType.MiddleMouse)) {
                        option.setDuration(1);
                    } else {
                        option.setDuration(CMINumber.clamp(option.getDuration() + returnChange(type), 1));
                    }
                    getEffect().setParticleParameters(null);

                    updateLooks();
                    update(gui);
                    updateTitle(gui);
                }

                @Override
                public void updateLooks() {
                    setName("Duration: " + option.getDuration());
                }
            };
            gui.addButton(button);
        }

        if (options instanceof CMIParticleDustOptions) {
            CMIParticleDustOptions option = (CMIParticleDustOptions) options;

            button = new CMIGuiButton(33, CMIMaterial.BLUE_WOOL) {
                @Override
                public void click(GUIClickType type) {

                    if (type.equals(GUIClickType.Q) || type.equals(GUIClickType.MiddleMouse)) {
                        option.setSize(1);
                    } else {
                        option.setSize(((option.getSize() * 10) + returnChange(type)) / 10F);
                    }
                    getEffect().setParticleParameters(null);

                    updateLooks();
                    update(gui);
                    updateTitle(gui);
                }

                @Override
                public void updateLooks() {
                    setName("Size: " + option.getSize());
                }
            };

            gui.addButton(button);
        }

        if (options instanceof CMIParticleFloat) {
            CMIParticleFloat option = (CMIParticleFloat) options;

            button = new CMIGuiButton(34, CMIMaterial.ACACIA_DOOR) {
                @Override
                public void click(GUIClickType type) {
                    if (type.equals(GUIClickType.Q) || type.equals(GUIClickType.MiddleMouse)) {
                        option.setValue(0);
                    } else {
                        option.setValue(((option.getValue() * 10) + returnChange(type)) / 10F);
                    }
                    getEffect().setParticleParameters(null);

                    updateLooks();
                    update(gui);
                    updateTitle(gui);
                }

                @Override
                public void updateLooks() {
                    setName("Value: " + option.getValue());
                }
            };

            gui.addButton(button);
        }

        if (options instanceof CMIParticleInteger) {
            CMIParticleInteger option = (CMIParticleInteger) options;

            button = new CMIGuiButton(35, CMIMaterial.TRAPPED_CHEST) {
                @Override
                public void click(GUIClickType type) {
                    if (type.equals(GUIClickType.Q) || type.equals(GUIClickType.MiddleMouse)) {
                        option.setValue(0);
                    } else {
                        option.setValue(option.getValue() + returnChange(type));
                    }
                    getEffect().setParticleParameters(null);

                    updateLooks();
                    update(gui);
                    updateTitle(gui);
                }

                @Override
                public void updateLooks() {
                    setName("Value: " + option.getValue());
                }
            };

            gui.addButton(button);
        }

//        button = new CMIGuiButton(45, CMILib.getInstance().getConfigManager().getGUIPreviousPage()) {
//            @Override
//            public void click(GUIClickType type) {
//                onBackButtonClick();
//            }
//        };
//        button.setName(LC.info_Back.getLocale());
//        gui.addButton(button);

        gui.fillEmptyButtons();
        gui.open();
        showParticle();
        return true;
    }

    private int returnChange(GUIClickType type) {
        switch (type) {
        case Left:
            return 1;
        case LeftShift:
            return 10;
        case MiddleMouse:
        case Q:
            return 1;
        case Right:
            return -1;
        case RightShift:
            return -10;
        default:
            break;
        }
        return 0;
    }

    private CMIGuiButton addColorButton(int slot, String name, Player player, Color color, Consumer<CMIChatColor> consumer) {
        ItemStack item = CMIMaterial.LEATHER_CHESTPLATE.newItemStack();
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);

        CMIGuiButton button = new CMIGuiButton(slot, item) {
            @Override
            public void click(GUIClickType type) {

                ChatMessageEdit chatEdit = new ChatMessageEdit(player, toHex(color)) {
                    @Override
                    public void run(String message) {

                        if (!CMIText.isValidString(message)) {
                            return;
                        }

                        if (!message.contains("#")) {
                            message = "#" + message;
                        }

                        if (!message.startsWith("{")) {
                            message = "{" + message + "}";
                        }

                        CMIChatColor color = CMIChatColor.getColor(message);

                        if (color == null || !color.isColor()) {
                            LC.info_incorrectColor.sendMessage(player);
                            return;
                        }

                        consumer.accept(color);
                        getEffect().setParticleParameters(null);
                        updateTitle(getGui());
                    }

                    @Override
                    public void onDisable() {
                        open();
                    }
                };
                chatEdit.setCheckForCancel(true);
                chatEdit.printMessage();
                this.getGui().getPlayer().closeInventory();

            }
        };
        button.setName(name.replace("[color]", toHex(color)));
        button.hideItemFlags();
        return button;
    }

    public CMIEffect getEffect() {
        return effect;
    }

    private String toHex(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        try {
            int a = color.getAlpha();
            if (a < 255) {
                return String.format("{#%02X%02X%02X%02X}", r, g, b, a);
            }
        } catch (NoSuchMethodError ignored) {
        }

        return String.format("{#%02X%02X%02X}", r, g, b);
    }

    public boolean isShowSerializeButton() {
        return showSerializeButton;
    }

    public void setShowSerializeButton(boolean showSerializeButton) {
        this.showSerializeButton = showSerializeButton;
    }
}
