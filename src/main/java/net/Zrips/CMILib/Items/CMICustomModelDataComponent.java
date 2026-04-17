package net.Zrips.CMILib.Items;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.NotNull;

import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.Version.Version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CMICustomModelDataComponent {

    private List<Float> floats;
    private List<String> strings;
    private List<Boolean> flags;
    private List<Color> colors;

    public CMICustomModelDataComponent() {
    }

    public CMICustomModelDataComponent(int value) {
        floats = new ArrayList<Float>(1);
        floats.add((float) value);
    }

    public CMICustomModelDataComponent(List<Float> floats, List<String> strings, List<Boolean> flags, List<Color> colors) {
        floats = floats != null ? new ArrayList<Float>(floats) : null;
        strings = strings != null ? new ArrayList<String>(strings) : null;
        flags = flags != null ? new ArrayList<Boolean>(flags) : null;
        colors = colors != null ? new ArrayList<Color>(colors) : null;
    }

    public static CMICustomModelDataComponent empty() {
        return new CMICustomModelDataComponent();
    }

    public void removeAll(CMICustomModelDataComponent other) {
        if (other == null)
            return;

        if (this.floats != null) {
            this.floats.removeAll(other.getFloats());
        }

        if (this.strings != null) {
            this.strings.removeAll(other.getStrings());
        }

        if (this.flags != null) {
            this.flags.removeAll(other.getFlags());
        }

        if (this.colors != null) {
            this.colors.removeAll(other.getColors());
        }
    }

    public void combine(CMICustomModelDataComponent other) {
        if (other == null)
            return;

        if (other.getFloats() != null && !other.getFloats().isEmpty()) {
            if (this.floats == null)
                this.floats = new ArrayList<Float>();

            for (Float f : other.getFloats()) {
                if (f == null)
                    continue;
                if (!this.floats.contains(f)) {
                    this.floats.add(f);
                }
            }
        }

        if (other.getStrings() != null && !other.getStrings().isEmpty()) {
            if (this.strings == null)
                this.strings = new ArrayList<String>();

            for (String s : other.getStrings()) {
                if (s == null)
                    continue;
                if (!this.strings.contains(s)) {
                    this.strings.add(s);
                }
            }
        }

        if (other.getFlags() != null && !other.getFlags().isEmpty()) {
            if (this.flags == null)
                this.flags = new ArrayList<Boolean>();

            for (Boolean b : other.getFlags()) {
                if (b == null)
                    continue;
                if (!this.flags.contains(b)) {
                    this.flags.add(b);
                }
            }
        }

        if (other.getColors() != null && !other.getColors().isEmpty()) {
            if (this.colors == null)
                this.colors = new ArrayList<org.bukkit.Color>();

            for (org.bukkit.Color c : other.getColors()) {
                if (c == null)
                    continue;
                if (!this.colors.contains(c)) {
                    this.colors.add(c);
                }
            }
        }
    }

    public boolean isEmpty() {
        return getFloats().isEmpty() && getStrings().isEmpty() && getFlags().isEmpty() && getColors().isEmpty();
    }

    public List<Float> getFloats() {
        return floats == null ? Collections.<Float>emptyList() : Collections.unmodifiableList(floats);
    }

    public List<String> getStrings() {
        return strings == null ? Collections.<String>emptyList() : Collections.unmodifiableList(strings);
    }

    public List<Boolean> getFlags() {
        return flags == null ? Collections.<Boolean>emptyList() : Collections.unmodifiableList(flags);
    }

    public List<Color> getColors() {
        return colors == null ? Collections.<Color>emptyList() : Collections.unmodifiableList(colors);
    }

    public Integer getLegacyValue() {
        if (floats == null || floats.isEmpty())
            return null;
        return floats.get(0).intValue();
    }

    public ItemStack apply(ItemStack item) {
        if (item == null)
            return item;

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;

        if (Version.isCurrentEqualOrHigher(Version.v1_21_4)) {
            CustomModelDataComponent component = meta.getCustomModelDataComponent();
            component.setFloats(floats != null ? floats : Collections.<Float>emptyList());
            component.setStrings(strings != null ? strings : Collections.<String>emptyList());
            component.setFlags(flags != null ? flags : Collections.<Boolean>emptyList());
            component.setColors(colors != null ? colors : Collections.<Color>emptyList());
            meta.setCustomModelDataComponent(component);
        } else {
            // legacy fallback
            if (Version.isCurrentEqualOrHigher(Version.v1_14_0)) {
                meta.setCustomModelData(getLegacyValue());
            } else {
                CMINBT nbt = new CMINBT(item);
                ItemStack newItem = (ItemStack) nbt.setInt("CustomModelData", getLegacyValue());
                meta = newItem.getItemMeta();
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public static @NotNull CMICustomModelDataComponent fromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return empty();

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return empty();

        CMICustomModelDataComponent data = new CMICustomModelDataComponent();

        if (Version.isCurrentEqualOrHigher(Version.v1_21_4)) {
            CustomModelDataComponent component = meta.getCustomModelDataComponent();
            if (component != null) {

                List<Float> f = component.getFloats();
                if (f != null && !f.isEmpty()) {
                    data.floats = new ArrayList<Float>(f);
                }

                List<String> s = component.getStrings();
                if (s != null && !s.isEmpty()) {
                    data.strings = new ArrayList<String>(s);
                }

                List<Boolean> fl = component.getFlags();
                if (fl != null && !fl.isEmpty()) {
                    data.flags = new ArrayList<Boolean>(fl);
                }

                List<Color> c = component.getColors();
                if (c != null && !c.isEmpty()) {
                    data.colors = new ArrayList<Color>(c);
                }
            }
        } else {
            // fallback from legacy-only items
            if (Version.isCurrentEqualOrHigher(Version.v1_14_0)) {
                if ((data.floats == null || data.floats.isEmpty()) && meta.hasCustomModelData()) {
                    data.floats = new ArrayList<Float>(1);
                    data.floats.add((float) meta.getCustomModelData());
                }
            } else {
                CMINBT nbt = new CMINBT(item);
                Integer value = nbt.getInt("CustomModelData");
                if (value != null) {
                    data.floats = new ArrayList<Float>(1);
                    data.floats.add((float) value);
                }
            }
        }

        return data;
    }

    public static @NotNull CMICustomModelDataComponent fromString(String input) {
        CMICustomModelDataComponent data = new CMICustomModelDataComponent();

        if (input == null || input.trim().isEmpty()) {
            return data;
        }

        String[] parts = input.split(",");

        List<Float> floats = new ArrayList<Float>();
        List<String> strings = new ArrayList<String>();
        List<Boolean> flags = new ArrayList<Boolean>();

        for (String raw : parts) {
            if (raw == null)
                continue;

            String s = raw.trim();
            if (s.isEmpty())
                continue;

            if (s.equalsIgnoreCase("true")) {
                flags.add(true);
                continue;
            }

            if (s.equalsIgnoreCase("false")) {
                flags.add(false);
                continue;
            }

            try {
                if (s.contains(".")) {
                    floats.add(Float.parseFloat(s));
                } else {
                    floats.add((float) Integer.parseInt(s));
                }
                continue;
            } catch (NumberFormatException ignored) {
            }

            strings.add(s);
        }

        if (!floats.isEmpty())
            data.floats = floats;
        if (!strings.isEmpty())
            data.strings = strings;
        if (!flags.isEmpty())
            data.flags = flags;

        return data;
    }

    @Override
    public String toString() {
        List<String> out = new ArrayList<String>();

        if (floats != null) {
            for (Float f : floats) {
                if (f == null)
                    continue;

                // print integers without .0
                if (f % 1 == 0) {
                    out.add(String.valueOf(f.intValue()));
                } else {
                    out.add(String.valueOf(f));
                }
            }
        }

        if (strings != null) {
            out.addAll(strings);
        }

        if (flags != null) {
            for (Boolean b : flags) {
                if (b != null) {
                    out.add(String.valueOf(b));
                }
            }
        }

        if (colors != null) {
            for (org.bukkit.Color c : colors) {
                if (c != null) {
                    out.add(String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue()));
                }
            }
        }

        return String.join(",", out);
    }

    @Override
    public int hashCode() {
        return Objects.hash(floats, strings, flags, colors);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CMICustomModelDataComponent))
            return false;
        CMICustomModelDataComponent other = (CMICustomModelDataComponent) obj;
        return Objects.equals(floats, other.floats) && Objects.equals(strings, other.strings) && Objects.equals(flags, other.flags) && Objects.equals(colors, other.colors);
    }
}
