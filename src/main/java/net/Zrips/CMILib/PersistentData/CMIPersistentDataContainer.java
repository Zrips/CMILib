package net.Zrips.CMILib.PersistentData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Zrips.CMI.CMI;

public class CMIPersistentDataContainer {

    PersistentDataContainer persistentDataContainer;

    public CMIPersistentDataContainer() {
        this.persistentDataContainer = null;
    }

    void save() {
    }

    private static NamespacedKey getKey(String key) {
        return new NamespacedKey(CMI.getInstance(), key);
    }

    public boolean hasKey(String key) {
        if (persistentDataContainer == null)
            return false;
        return persistentDataContainer.has(getKey(key));
    }

    public @NotNull Set<NamespacedKey> getKeys() {
        if (persistentDataContainer == null)
            return new HashSet<NamespacedKey>();
        return persistentDataContainer.getKeys();
    }

    public CMIPersistentDataContainer set(String key, String value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.STRING, value);
        save();
        return this;
    }

    public CMIPersistentDataContainer remove(String key) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.remove(getKey(key));
        save();
        return this;
    }

    public @Nullable String getString(String key) {
        return persistentDataContainer.get(getKey(key), PersistentDataType.STRING);
    }

    public CMIPersistentDataContainer set(String key, int value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.INTEGER, value);
        save();
        return this;
    }

    public @Nullable Integer getInt(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.INTEGER);
    }

    public CMIPersistentDataContainer set(String key, long value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.LONG, value);
        save();
        return this;
    }

    public @Nullable Long getLong(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.LONG);
    }

    public CMIPersistentDataContainer set(String key, long... value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.LONG_ARRAY, value);
        save();
        return this;
    }

    public long @Nullable [] getLongArray(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.LONG_ARRAY);
    }

    public CMIPersistentDataContainer set(String key, boolean value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.BOOLEAN, value);
        save();
        return this;
    }

    public @Nullable Boolean getBoolean(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.BOOLEAN);
    }

    public CMIPersistentDataContainer set(String key, float value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.FLOAT, value);
        save();
        return this;
    }

    public @Nullable Float getFloat(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.FLOAT);
    }

    public CMIPersistentDataContainer set(String key, short value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.SHORT, value);
        save();
        return this;
    }

    public @Nullable Short getShort(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.SHORT);
    }

    public CMIPersistentDataContainer set(String key, double value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.DOUBLE, value);
        save();
        return this;
    }

    public @Nullable Double getDouble(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.DOUBLE);
    }

    public CMIPersistentDataContainer set(String key, byte... value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.BYTE_ARRAY, value);
        save();
        return this;
    }

    public byte @Nullable [] getByteArray(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.BYTE_ARRAY);
    }

    public CMIPersistentDataContainer set(String key, int... value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.INTEGER_ARRAY, value);
        save();
        return this;
    }

    public int @Nullable [] getIntArray(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.INTEGER_ARRAY);
    }

    public CMIPersistentDataContainer set(String key, byte value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.BYTE, value);
        save();
        return this;
    }

    public @Nullable Byte getByte(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.BYTE);
    }

    public CMIPersistentDataContainer set(String key, List<String> value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.LIST.strings(), value);
        save();
        return this;
    }

    public @Nullable List<String> getListString(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.LIST.strings());
    }
}
