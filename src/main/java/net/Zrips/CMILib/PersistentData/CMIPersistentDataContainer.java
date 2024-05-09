package net.Zrips.CMILib.PersistentData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.Zrips.CMILib.CMILib;

public class CMIPersistentDataContainer {

    // 1.15+ servers only
    PersistentDataContainer persistentDataContainer;

    private static Set<PersistentDataType<?, ?>> DATA_TYPES = new HashSet<>();

    static {

        DATA_TYPES.add(PersistentDataType.BYTE);
        DATA_TYPES.add(PersistentDataType.SHORT);
        DATA_TYPES.add(PersistentDataType.INTEGER);
        DATA_TYPES.add(PersistentDataType.LONG);
        DATA_TYPES.add(PersistentDataType.FLOAT);
        DATA_TYPES.add(PersistentDataType.DOUBLE);
        DATA_TYPES.add(PersistentDataType.STRING);
        DATA_TYPES.add(PersistentDataType.BYTE_ARRAY);
        DATA_TYPES.add(PersistentDataType.INTEGER_ARRAY);
        DATA_TYPES.add(PersistentDataType.LONG_ARRAY);

        try {
            DATA_TYPES.add(PersistentDataType.BOOLEAN);
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.TAG_CONTAINER);
        } catch (Throwable __) {
        }

        try {
            DATA_TYPES.add(PersistentDataType.LIST.bytes());
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.LIST.booleans());
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.LIST.doubles());
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.LIST.floats());
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.LIST.integers());
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.LIST.strings());
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.LIST.shorts());
        } catch (Throwable __) {
        }
    }

    public CMIPersistentDataContainer() {
        this.persistentDataContainer = null;
    }

    public static CMIPersistentDataContainer get(ItemStack item) {
        return new CMIItemPersistentDataContainer(item);
    }

    public static CMIPersistentDataContainer get(Entity entity) {
        return new CMIEntityPersistentDataContainer(entity);
    }

    public static CMIPersistentDataContainer get(Block block) {
        return new CMIBlockPersistentDataContainer(block);
    }

    public static @Nullable CMIPersistentDataContainer get(Object object) {
        if (object instanceof ItemStack) {
            return new CMIItemPersistentDataContainer((ItemStack) object);
        } else if (object instanceof Block) {
            return new CMIBlockPersistentDataContainer((Block) object);
        } else if (object instanceof Entity) {
            return new CMIEntityPersistentDataContainer((Entity) object);
        }
        return null;
    }

    public void save() {
    }

    public PersistentDataType<?, ?> getType(String key) {
        return getType(getKey(key));
    }

    public PersistentDataType<?, ?> getType(NamespacedKey key) {
        for (PersistentDataType<?, ?> dataType : DATA_TYPES) {
            if (persistentDataContainer.has(key, dataType))
                return dataType;
        }
        return null;
    }

    private static NamespacedKey getKey(String key) {
        return new NamespacedKey(CMILib.getInstance(), key.replace(" ", "_"));
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
        return this;
    }

    public CMIPersistentDataContainer remove(String key) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.remove(getKey(key));
        return this;
    }

    public @Nullable String getString(String key) {
        return persistentDataContainer.get(getKey(key), PersistentDataType.STRING);
    }

    public CMIPersistentDataContainer set(String key, int value) {
        if (persistentDataContainer == null)
            return this;
        persistentDataContainer.set(getKey(key), PersistentDataType.INTEGER, value);
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
        return this;
    }

    public @Nullable List<String> getListString(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.LIST.strings());
    }

    public @Nullable Object get(String key, PersistentDataType<?, ?> type) {
        return get(getKey(key), type);
    }

    public @Nullable Object get(NamespacedKey key, PersistentDataType<?, ?> type) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(key, type);
    }

    public @Nullable Object get(String key) {
        return get(getKey(key));
    }

    public @Nullable Object get(NamespacedKey key) {
        PersistentDataType<?, ?> type = this.getType(key);
        if (type == null || persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(key, type);
    }
}
