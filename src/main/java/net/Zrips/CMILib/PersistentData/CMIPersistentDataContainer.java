package net.Zrips.CMILib.PersistentData;

import java.util.ArrayList;
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
import net.Zrips.CMILib.Version.Version;

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

        // List type added with 1.20
        try {
            DATA_TYPES.add(PersistentDataType.BOOLEAN);
        } catch (Throwable __) {
        }
        try {
            DATA_TYPES.add(PersistentDataType.TAG_CONTAINER);
        } catch (Throwable __) {
        }

        // List type added with 1.20.4
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

        if (key.contains(":"))
            return new NamespacedKey(key.split(":", 2)[0].toLowerCase(), key.split(":", 2)[1].replace(" ", "_"));

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

        // Temporary solution to check for both options
        try {
            return persistentDataContainer.get(getKey(key), PersistentDataType.INTEGER_ARRAY);
        } catch (Exception e) {
        }
        try {
            return persistentDataContainer.get(getKey(key), PersistentDataType.LIST.integers()).stream().mapToInt(i -> i).toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public CMIPersistentDataContainer setIntList(String key, List<Integer> value) {
        if (persistentDataContainer == null)
            return this;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
            persistentDataContainer.set(getKey(key), PersistentDataType.LIST.integers(), value);
            return this;
        }

        // For older servers which doesn't support LIST type
        int[] array = new int[value.size()];
        for (int i = 0; i < value.size(); i++) {
            array[i] = value.get(i);
        }
        persistentDataContainer.set(getKey(key), PersistentDataType.INTEGER_ARRAY, array);

        return this;
    }

    public @Nullable List<String> getListString(String key) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(getKey(key), PersistentDataType.LIST.strings());
    }

    public @Nullable List<Integer> getListInt(String key) {
        if (persistentDataContainer == null)
            return null;

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R3))
                return persistentDataContainer.get(getKey(key), PersistentDataType.LIST.integers());
        } catch (Throwable e) {
        }

        // For older servers which doesn't support LIST type
        int @Nullable [] array = this.getIntArray(key);

        if (array == null)
            return null;

        List<Integer> list = new ArrayList<>();
        for (int i : array)
            list.add(i);
        return list;
    }

    public @Nullable Object get(String key, PersistentDataType<?, ?> type) {
        return get(getKey(key), type);
    }

    public @Nullable Object get(NamespacedKey key, PersistentDataType<?, ?> type) {
        if (persistentDataContainer == null)
            return null;
        return persistentDataContainer.get(key, type);
    }

    private PersistentDataContainer getSubContainer(String key) {

        PersistentDataContainer container = getSubContainerIfExists(key);
        if (container == null)
            container = persistentDataContainer.getAdapterContext().newPersistentDataContainer();
        return container;
    }

    private PersistentDataContainer getSubContainerIfExists(String key) {
        if (persistentDataContainer == null)
            return null;

        if (!persistentDataContainer.has(getKey(key), PersistentDataType.TAG_CONTAINER))
            return null;

        return persistentDataContainer.get(getKey(key), PersistentDataType.TAG_CONTAINER);
    }

    public CMIPersistentDataContainer set(String key, String subKey, List<String> value) {
        PersistentDataContainer container = getSubContainer(key);

        if (container == null)
            return this;

        container.set(getKey(subKey), PersistentDataType.LIST.strings(), value);
        persistentDataContainer.set(getKey(key), PersistentDataType.TAG_CONTAINER, container);
        return this;
    }

    public CMIPersistentDataContainer set(String key, String subKey, long value) {
        PersistentDataContainer container = getSubContainer(key);

        if (container == null)
            return this;

        container.set(getKey(subKey), PersistentDataType.LONG, value);
        persistentDataContainer.set(getKey(key), PersistentDataType.TAG_CONTAINER, container);
        return this;
    }

    public CMIPersistentDataContainer set(String key, String subKey, boolean value) {
        PersistentDataContainer container = getSubContainer(key);

        if (container == null)
            return this;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
            container.set(getKey(subKey), PersistentDataType.BOOLEAN, value);
            persistentDataContainer.set(getKey(key), PersistentDataType.TAG_CONTAINER, container);
            return this;
        }

        // For older servers which doesn't support BOOLEAN type
        container.set(getKey(subKey), PersistentDataType.BYTE, (byte) (value ? 1 : 0));
        persistentDataContainer.set(getKey(key), PersistentDataType.TAG_CONTAINER, container);
        return this;
    }

    public CMIPersistentDataContainer set(String key, String subKey, int value) {
        PersistentDataContainer container = getSubContainer(key);

        if (container == null)
            return this;

        container.set(getKey(subKey), PersistentDataType.INTEGER, value);
        persistentDataContainer.set(getKey(key), PersistentDataType.TAG_CONTAINER, container);
        return this;
    }

    public CMIPersistentDataContainer setIntList(String key, String subKey, List<Integer> value) {
        PersistentDataContainer container = getSubContainer(key);

        if (container == null)
            return this;

        container.set(getKey(subKey), PersistentDataType.LIST.integers(), value);
        persistentDataContainer.set(getKey(key), PersistentDataType.TAG_CONTAINER, container);
        return this;
    }

    public CMIPersistentDataContainer remove(String key, String subKey) {
        PersistentDataContainer container = getSubContainerIfExists(key);

        if (container == null)
            return this;

        container.remove(getKey(subKey));
        persistentDataContainer.set(getKey(key), PersistentDataType.TAG_CONTAINER, container);

        return this;
    }

    public @Nullable List<String> getListString(String key, String subKey) {
        if (persistentDataContainer == null)
            return null;
        @Nullable
        PersistentDataContainer container = getSubContainerIfExists(key);
        if (container == null)
            return null;

        return container.get(getKey(subKey), PersistentDataType.LIST.strings());
    }

    public @Nullable Boolean getBoolean(String key, String subKey) {
        if (persistentDataContainer == null)
            return null;
        @Nullable
        PersistentDataContainer container = getSubContainerIfExists(key);
        if (container == null)
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R1))
            return container.get(getKey(subKey), PersistentDataType.BOOLEAN);

        // For older servers which doesn't support BOOLEAN type
        @Nullable
        Byte b = container.get(getKey(subKey), PersistentDataType.BYTE);
        return b == null ? null : b == 1;
    }

    public @Nullable Long getLong(String key, String subKey) {
        if (persistentDataContainer == null)
            return null;
        @Nullable
        PersistentDataContainer container = getSubContainerIfExists(key);
        if (container == null)
            return null;

        return container.get(getKey(subKey), PersistentDataType.LONG);
    }

    public @Nullable Integer getInt(String key, String subKey) {
        if (persistentDataContainer == null)
            return null;
        @Nullable
        PersistentDataContainer container = getSubContainerIfExists(key);
        if (container == null)
            return null;

        return container.get(getKey(subKey), PersistentDataType.INTEGER);
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
