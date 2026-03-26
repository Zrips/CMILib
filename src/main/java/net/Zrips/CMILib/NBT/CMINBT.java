package net.Zrips.CMILib.NBT;

import java.util.List;
import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.Version.Version;

public class CMINBT {

    CMINBTInterface face;

    public enum nmbtType {
        item, block, entity, custom;
    }

    public CMINBT() {
        if (Version.isMojangMappings()) {
            face = new CMINBTMojang();
        } else {
            face = new CMINBTLegacy();
        }
    }

    public CMINBT(Object nbtTagCompound) {
        if (Version.isMojangMappings()) {
            face = new CMINBTMojang(nbtTagCompound);
        } else {
            face = new CMINBTLegacy(nbtTagCompound);
        }
    }

    public CMINBT(ItemStack item) {
        if (Version.isMojangMappings()) {
            face = new CMINBTMojang(item);
        } else {
            face = new CMINBTLegacy(item);
        }
    }

    public CMINBT(Block block) {
        if (Version.isMojangMappings()) {
            face = new CMINBTMojang(block);
        } else {
            face = new CMINBTLegacy(block);
        }
    }

    public CMINBT(Entity entity) {
        if (Version.isMojangMappings()) {
            face = new CMINBTMojang(entity);
        } else {
            face = new CMINBTLegacy(entity);
        }
    }

    public Integer getInt(String path) {
        return face.getInt(path);
    }

    public Byte getByte(String path) {
        return face.getByte(path);
    }

    public Long getLong(String path) {
        return face.getLong(path);
    }

    public Boolean getBoolean(String path) {
        return face.getBoolean(path);
    }

    public float getFloat(String path) {
        return face.getFloat(path);
    }

    public Short getShort(String path) {
        return face.getShort(path);
    }

    public double getDouble(String path) {
        return face.getDouble(path);
    }

    public byte[] getByteArray(String path) {
        return face.getByteArray(path);
    }

    public int[] getIntArray(String path) {
        return face.getIntArray(path);
    }

    public long[] getLongArray(String path) {
        return face.getLongArray(path);
    }

    public String getString(String path) {
        return face.getString(path);
    }

    public List<String> getList(String path) {
        return face.getList(path);
    }

    public List<String> getList(String path, int type) {
        return face.getList(path, type);
    }

    public Object getObjectList(String path, int type) {
        return face.getObjectList(path, type);
    }

    public Object setBoolean(String path, Boolean value) {
        return face.setBoolean(path, value);
    }

    public Object setByte(String path, Byte value) {
        return face.setByte(path, value);
    }

    public Object setShort(String path, Short value) {
        return face.setShort(path, value);
    }

    public Object setStringList(String path, List<String> value) {
        return face.setStringList(path, value);
    }

    public Object setString(String path, String value) {
        return face.setString(path, value);
    }

    public Object setInt(String path, Integer value) {
        return face.setInt(path, value);
    }

    public Object setIntArray(String path, int[] value) {
        return face.setIntArray(path, value);
    }

    public Object setByteArray(String path, byte[] value) {
        return face.setByteArray(path, value);
    }

    public Object setLongArray(String path, long[] value) {
        return face.setLongArray(path, value);
    }

    public Object setLong(String path, Long value) {
        return face.setLong(path, value);
    }

    public Object setDouble(String path, Double value) {
        return face.setDouble(path, value);
    }

    public Object remove(String path) {
        return face.remove(path);
    }

    public Object set(String path, Object nbtbase) {
        return face.set(path, nbtbase);
    }

    public boolean hasNBT() {
        return face.hasNBT();
    }

    public boolean hasNBT(String key) {
        return face.hasNBT(key);
    }

    public Object getNbt() {
        return face.getNbt();
    }

    public Set<String> getKeys() {
        return face.getKeys();
    }

    public Object get(String path) {
        return face.get(path);
    }

    public Object getCompound(String path) {
        return face.getCompound(path);
    }

    public byte getTypeId() {
        return face.getTypeId();
    }

    public static ItemStack setTag(ItemStack item, Object tag) {
        if (Version.isMojangMappings())
            return CMINBTMojang.setTag(item, tag);
        else
            return CMINBTLegacy.setTag(item, tag);
    }

    public static Object getNbt(Entity entity) {
        if (Version.isMojangMappings())
            return CMINBTMojang.getNbt(entity);
        else
            return CMINBTLegacy.getNbt(entity);
    }

    public static Object getNbt(ItemStack item) {
        if (Version.isMojangMappings())
            return CMINBTMojang.getNbt(item);
        else
            return CMINBTLegacy.getNbt(item);
    }

    public static Object getNbt(Block block) {
        if (Version.isMojangMappings())
            return CMINBTMojang.getNbt(block);
        else
            return CMINBTLegacy.getNbt(block);
    }

    public static Object asItemStackTemplate(ItemStack item) {
        if (!Version.isMojangMappings())
            return null;
        return CMINBTMojang.asItemTemplate(item);
    }

    public static Object asNMSCopy(ItemStack item) {
        if (Version.isMojangMappings())
            return CMINBTMojang.asNMSCopy(item);
        else
            return CMINBTLegacy.asNMSCopy(item);
    }

    public static Object asBukkitCopy(Object item) {
        if (Version.isMojangMappings())
            return CMINBTMojang.asBukkitCopy(item);
        else
            return CMINBTLegacy.asBukkitCopy(item);
    }

    public static Object getEntityHandle(Entity ent) {
        if (Version.isMojangMappings())
            return CMINBTMojang.getEntityHandle(ent);
        else
            return CMINBTLegacy.getEntityHandle(ent);
    }

    public static boolean isNBTSimilar(ItemStack is, ItemStack is2) {
        if (Version.isMojangMappings())
            return CMINBTMojang.isNBTSimilar(is, is2);
        else
            return CMINBTLegacy.isNBTSimilar(is, is2);
    }

    @Deprecated
    public static ItemStack HideFlag(ItemStack item, int state) {
        if (Version.isMojangMappings())
            return CMINBTMojang.HideFlag(item, state);
        else
            return CMINBTLegacy.HideFlag(item, state);
    }

    public static ItemStack modifyItemStack(ItemStack stack, String arguments) {
        if (Version.isMojangMappings())
            return CMINBTMojang.modifyItemStack(stack, arguments);
        else
            return CMINBTLegacy.modifyItemStack(stack, arguments);
    }

    public static String toJson(ItemStack item) {
        if (Version.isMojangMappings())
            return CMINBTMojang.toJson(item);
        else
            return CMINBTLegacy.toJson(item);
    }

    // For 1.13 and older servers
    public static EntityType getEggType(ItemStack item) {
        if (Version.isMojangMappings())
            return CMINBTMojang.getEggType(item);
        else
            return CMINBTLegacy.getEggType(item);
    }

    // For 1.13 and older servers
    public static ItemStack setEggType(ItemStack item, EntityType etype) {
        if (Version.isMojangMappings())
            return CMINBTMojang.setEggType(item, etype);
        else
            return CMINBTLegacy.setEggType(item, etype);
    }

    public static Object newNBTTagList(int type) {
        if (Version.isMojangMappings())
            return CMINBTMojang.newNBTTagList(type);
        else
            return CMINBTLegacy.newNBTTagList(type);
    }

    public static void addToList(Object list, int size, Object data) {
        if (Version.isMojangMappings())
            CMINBTMojang.addToList(list, size, data);
        else
            CMINBTLegacy.addToList(list, size, data);
    }

    public nmbtType getType() {
        return face.getType();
    }
}
