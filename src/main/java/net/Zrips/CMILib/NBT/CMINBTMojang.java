package net.Zrips.CMILib.NBT;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Entities.CMIEntityType;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.NBT.CMINBT.nmbtType;
import net.Zrips.CMILib.PersistentData.CMIPersistentDataContainer;
import net.Zrips.CMILib.Version.Version;

public class CMINBTMojang implements CMINBTInterface {

    private static Object CODEC;
    private static Object serializator;
    private static Method encodeMethod;

    private static Class<?> CRAFT_ITEM_STACK;
    private static Class<?> NMS_ITEM_STACK;
    private static Class<?> DATA_COMPONENTS;
    private static Class<?> CUSTOM_DATA;
    private static Class<?> COMPOUND_TAG;
    private static Class<?> COMPOUND_TAGLIST;

    private static Method AS_NMS_COPY;
    private static Method AS_BUKKIT_COPY;

    private static Method NMS_GET_COMPONENT;
    private static Method NMS_SET_COMPONENT;

    private static Field CUSTOM_DATA_COMPONENT;

    private static Method CUSTOMDATA_OF;
    private static Method CUSTOMDATA_COPYTAG;

    private static Method TAG_CONTAINS;
    private static Method TAG_GET;
    private static Method TAG_GET_STRING;
    private static Method TAG_PUT_STRING;

    private static Method TAG_EQUALS;
    private static Method TAG_GET_BOOLEAN;
    private static Method TAG_GET_BYTE;
    private static Method TAG_GET_SHORT;
    private static Method TAG_GET_INT;
    private static Method TAG_GET_LONG;
    private static Method TAG_GET_FLOAT;
    private static Method TAG_GET_DOUBLE;
    private static Method TAG_GET_BYTE_ARRAY;
    private static Method TAG_GET_INT_ARRAY;
    private static Method TAG_GET_LONG_ARRAY;
    private static Method TAG_GET_LIST;

//    private static Method TAG_PUT_BOOLEAN;
//    private static Method TAG_PUT_BYTE;
//    private static Method TAG_PUT_SHORT;
//    private static Method TAG_PUT_INT;
//    private static Method TAG_PUT_LONG;
//    private static Method TAG_PUT_FLOAT;
//    private static Method TAG_PUT_DOUBLE;
//    private static Method TAG_PUT_BYTE_ARRAY;
//    private static Method TAG_PUT_INT_ARRAY;
//    private static Method TAG_PUT_LONG_ARRAY;
//    private static Method TAG_PUT_LIST;

    private static Method TAG_GET_COMPOUND;
    private static Method TAG_PUT_COMPOUND;

    private static Method TAG_REMOVE;

    static {

        try {

            CRAFT_ITEM_STACK = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
            NMS_ITEM_STACK = Class.forName("net.minecraft.world.item.ItemStack");
            DATA_COMPONENTS = Class.forName("net.minecraft.core.component.DataComponents");
            CUSTOM_DATA = Class.forName("net.minecraft.world.item.component.CustomData");
            COMPOUND_TAG = Class.forName("net.minecraft.nbt.CompoundTag");
            COMPOUND_TAGLIST = Class.forName("net.minecraft.nbt.NBTTagList");

            AS_NMS_COPY = CRAFT_ITEM_STACK.getMethod("asNMSCopy", ItemStack.class);
            AS_BUKKIT_COPY = CRAFT_ITEM_STACK.getMethod("asBukkitCopy", NMS_ITEM_STACK);

            NMS_GET_COMPONENT = NMS_ITEM_STACK.getMethod("get", Class.forName("net.minecraft.core.component.DataComponentType"));
            NMS_SET_COMPONENT = NMS_ITEM_STACK.getMethod("set", Class.forName("net.minecraft.core.component.DataComponentType"), Object.class);

            CUSTOM_DATA_COMPONENT = DATA_COMPONENTS.getField("CUSTOM_DATA");

            CUSTOMDATA_OF = CUSTOM_DATA.getMethod("of", COMPOUND_TAG);
            CUSTOMDATA_COPYTAG = CUSTOM_DATA.getMethod("copyTag");

            TAG_CONTAINS = COMPOUND_TAG.getMethod("contains", String.class);

            TAG_GET = COMPOUND_TAG.getMethod("get", String.class);

            TAG_EQUALS = NMS_ITEM_STACK.getMethod("matches", NMS_ITEM_STACK, NMS_ITEM_STACK);

            TAG_GET_STRING = COMPOUND_TAG.getMethod("getString", String.class);
            TAG_GET_BOOLEAN = COMPOUND_TAG.getMethod("getBoolean", String.class);
            TAG_GET_BYTE = COMPOUND_TAG.getMethod("getByte", String.class);
            TAG_GET_SHORT = COMPOUND_TAG.getMethod("getShort", String.class);
            TAG_GET_INT = COMPOUND_TAG.getMethod("getInt", String.class);
            TAG_GET_LONG = COMPOUND_TAG.getMethod("getLong", String.class);
            TAG_GET_FLOAT = COMPOUND_TAG.getMethod("getFloat", String.class);
            TAG_GET_DOUBLE = COMPOUND_TAG.getMethod("getDouble", String.class);

            Class<?> dynamicOps = Class.forName("com.mojang.serialization.DynamicOps");

            Method met_createSerializationContext = getRegistry().getClass().getMethod("createSerializationContext", dynamicOps);
            CODEC = NMS_ITEM_STACK.getField("CODEC").get(null);
            Class<?> dops = Class.forName("net.minecraft.nbt.NbtOps");
            Object dops_Instance = dops.getDeclaredField("INSTANCE").get(dops);

            serializator = met_createSerializationContext.invoke(getRegistry(), dops_Instance);
            encodeMethod = CODEC.getClass().getMethod("encodeStart", dynamicOps, Object.class);

//            TAG_PUT_BOOLEAN = COMPOUND_TAG.getMethod("putBoolean", String.class, boolean.class);
//            TAG_PUT_BYTE = COMPOUND_TAG.getMethod("putByte", String.class, byte.class);
//            TAG_PUT_SHORT = COMPOUND_TAG.getMethod("putShort", String.class, short.class);
//            TAG_PUT_INT = COMPOUND_TAG.getMethod("putInt", String.class, int.class);
//            TAG_PUT_LONG = COMPOUND_TAG.getMethod("putLong", String.class, long.class);
//            TAG_PUT_FLOAT = COMPOUND_TAG.getMethod("putFloat", String.class, float.class);
//            TAG_PUT_DOUBLE = COMPOUND_TAG.getMethod("putDouble", String.class, double.class);
//            TAG_PUT_LONG_ARRAY = COMPOUND_TAG.getMethod("putLongArray", String.class, long[].class);
//            TAG_PUT_BYTE_ARRAY = COMPOUND_TAG.getMethod("putByteArray", String.class, byte[].class);
//            TAG_PUT_INT_ARRAY = COMPOUND_TAG.getMethod("putIntArray", String.class, int[].class);

            TAG_GET_LIST = COMPOUND_TAG.getMethod("getList", String.class);
            TAG_GET_INT_ARRAY = COMPOUND_TAG.getMethod("getIntArray", String.class);
            TAG_GET_BYTE_ARRAY = COMPOUND_TAG.getMethod("getByteArray", String.class);
            TAG_GET_LONG_ARRAY = COMPOUND_TAG.getMethod("getLongArray", String.class);

            TAG_GET_COMPOUND = COMPOUND_TAG.getMethod("getCompound", String.class);
            TAG_PUT_COMPOUND = COMPOUND_TAG.getMethod("put", String.class, Class.forName("net.minecraft.nbt.Tag"));

            TAG_REMOVE = COMPOUND_TAG.getMethod("remove", String.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static Object registry = null;
    private static Method nbtMethod = null;

    private static Object getRegistry() {
        if (registry == null) {
            try {
                if (Version.isMojangMappings())
                    registry = Class.forName("org.bukkit.craftbukkit.CraftRegistry").getMethod("getMinecraftRegistry").invoke(null);
                else if (Version.isCurrentEqualOrHigher(Version.v1_21_R4))
                    registry = getBukkitClass("CraftRegistry").getMethod("getMinecraftRegistry").invoke(null);
                else {
                    Object server = CMILib.getInstance().getReflectionManager().getDedicatedServer();
                    registry = server.getClass().getMethod("getDefaultRegistryAccess").invoke(server);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return registry;
    }

    private static Class<?> getBukkitClass(String nmsClassString) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + Version.getCurrent().toString() + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    Object tag;
    Object object;

    private nmbtType type;

    static {

    }

    public CMINBTMojang() {
        type = nmbtType.custom;
    }

    public CMINBTMojang(Object nbtTagCompound) {
        tag = nbtTagCompound;
        type = nmbtType.custom;
    }

    public CMINBTMojang(ItemStack item) {
        object = item;
        tag = getNbt(item);
        type = nmbtType.item;
    }

    public CMINBTMojang(Block block) {
        tag = getNbt(block);
        object = block;
        type = nmbtType.block;
    }

    public CMINBTMojang(Entity entity) {
        tag = getNbt(entity);
        object = entity;
        type = nmbtType.entity;
    }

    private Object get(String path, Method getMethod) {
        if (getNbt() == null)
            return null;
        try {

            Object t = updateLegacyTag(getNbt());

            if (!path.contains(".")) {
                return getMethod.invoke(t, path);
            }

            List<String> keys = new ArrayList<String>();
            keys.addAll(Arrays.asList(path.split("\\.")));
            try {
                Object nbtbase = TAG_GET.invoke(t, keys.get(0));
                for (int i = 1; i < keys.size(); i++) {
                    if (i + 1 < keys.size()) {
                        nbtbase = TAG_GET.invoke(nbtbase, keys.get(i));
                    } else {
                        if (nbtbase == null)
                            break;
                        return getMethod.invoke(nbtbase, keys.get(i));
                    }
                }
            } catch (Throwable e) {
            }

            return getMethod.invoke(t, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getInt(String path) {

        if (!this.hasNBT(path))
            return null;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            Integer v = persistentDataContainer.getInt(path);
            if (v != null)
                return v;
        }

        try {
            Object res = getFromOptional(get(path, TAG_GET_INT));
            return res == null ? null : (Integer) res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Byte getByte(String path) {
        if (!this.hasNBT(path))
            return null;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            Byte v = CMIPersistentDataContainer.get(object).getByte(path);
            if (v != null)
                return v;
        }

        try {
            Object res = getFromOptional(get(path, TAG_GET_BYTE));
            return res == null ? null : (Byte) res;
        } catch (Exception e) {
        }
        return null;
    }

    public Long getLong(String path) {
        if (!this.hasNBT(path))
            return null;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            Long v = persistentDataContainer.getLong(path);

            if (v != null)
                return v;
        }

        try {
            Object res = getFromOptional(get(path, TAG_GET_LONG));
            return res == null ? null : (Long) res;
        } catch (Exception e) {
        }
        return null;
    }

    public Boolean getBoolean(String path) {
        if (!this.hasNBT(path))
            return null;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            Boolean v = persistentDataContainer.getBoolean(path);

            if (v != null)
                return v;
        }

        try {
            Object res = getFromOptional(get(path, TAG_GET_BOOLEAN));
            return res == null ? null : (Boolean) res;
        } catch (Exception e) {
        }
        return null;
    }

    public float getFloat(String path) {
        if (!this.hasNBT(path))
            return 0.0F;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            Float v = persistentDataContainer.getFloat(path);

            if (v != null)
                return v;
        }

        try {
            Object res = getFromOptional(get(path, TAG_GET_FLOAT));
            return res == null ? 0.0F : (Float) res;
        } catch (Exception e) {
        }
        return 0.0F;
    }

    public Short getShort(String path) {

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            Short v = persistentDataContainer.getShort(path);
            if (v != null)
                return v;
        }

        if (tag == null)
            return null;

        try {
            Object res = getFromOptional(get(path, TAG_GET_SHORT));
            return res == null ? null : (Short) res;
        } catch (Exception e) {
        }
        return null;
    }

    public double getDouble(String path) {
        if (!this.hasNBT(path))
            return 0.0D;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            Double v = persistentDataContainer.getDouble(path);

            if (v != null)
                return v;
        }

        try {
            Object res = getFromOptional(get(path, TAG_GET_DOUBLE));
            return res == null ? 0.0D : (Double) res;
        } catch (Exception e) {
        }
        return 0.0D;
    }

    public byte[] getByteArray(String path) {
        if (!this.hasNBT(path))
            return new byte[0];

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            byte[] v = persistentDataContainer.getByteArray(path);

            if (v != null)
                return v;
        }

        try {
            Object res = getFromOptional(get(path, TAG_GET_BYTE_ARRAY));
            return res == null ? new byte[0] : (byte[]) res;
        } catch (Exception e) {
        }
        return new byte[0];
    }

    public int[] getIntArray(String path) {
        if (!this.hasNBT(path))
            return new int[0];

        try {
            Object res = getFromOptional(get(path, TAG_GET_INT_ARRAY));
            return res == null ? new int[0] : (int[]) res;
        } catch (Exception e) {
        }
        return new int[0];
    }

    public long[] getLongArray(String path) {
        if (!this.hasNBT(path))
            return new long[0];

        try {
            Object res = getFromOptional(get(path, TAG_GET_LONG_ARRAY));
            return res == null ? new long[0] : (long[]) res;
        } catch (Exception e) {
        }
        return new long[0];
    }

    public String getString(String path) {

        if (!this.hasNBT(path))
            return null;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            String v = persistentDataContainer.getString(path);
            if (v != null) {
                return v;
            }
        }

        if (getNbt() == null)
            return null;

        try {
            Object res = getFromOptional(get(path, TAG_GET_STRING));
            return res == null ? null : (String) res;
        } catch (Exception e) {
        }
        return null;
    }

    public List<String> getList(String path) {
        return getList(path, -1);
    }

    private Object updateLegacyTag(Object t) {

        if (getType().equals(nmbtType.item)) {
            CMINBTMojang mt = new CMINBTMojang(t);
            if (mt.hasNBT("components")) {
                t = getFromOptional(new CMINBTMojang(t).get("components"));
                mt = new CMINBTMojang(t);
                if (mt.hasNBT("minecraft:custom_data"))
                    t = getFromOptional(new CMINBTMojang(t).get("minecraft:custom_data"));
            }
        }
        return t;
    }

    private static Object getFromOptional(Object optional) {
        if (optional instanceof Optional)
            return ((Optional<?>) optional).orElse(null);
        return optional;
    }

    public List<String> getList(String path, int type) {
        if (!this.hasNBT(path))
            return null;

        @Nullable
        CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
        if (persistentDataContainer != null) {
            List<String> v = persistentDataContainer.getListString(path);
            if (v != null) {
                return new ArrayList<String>(v);
            }
        }

        if (getNbt() == null)
            return null;

        List<String> list = new ArrayList<String>();
        try {

            Object t = updateLegacyTag(getNbt());

            if (t == null)
                return list;

            if (path.contains(".")) {
                List<String> keys = new ArrayList<String>();
                keys.addAll(Arrays.asList(path.split("\\.")));
                try {
                    Object nbtbase = TAG_GET.invoke(t, keys.get(0));
                    for (int i = 1; i < keys.size(); i++) {
                        if (i + 1 < keys.size()) {
                            nbtbase = TAG_GET.invoke(nbtbase, keys.get(i));
                        } else {
                            t = nbtbase;
                            path = keys.get(i);
                            break;
                        }
                    }
                } catch (Throwable e) {
                }
            }

            Object ls = getFromOptional(TAG_GET_LIST.invoke(t, path));
            int size = (int) ls.getClass().getMethod("size").invoke(ls);

            if (size == 0 && type < 0) {
                ls = getFromOptional(TAG_GET_LIST.invoke(t, path));
                size = (int) ls.getClass().getMethod("size").invoke(ls);
            }

            Method method = ls.getClass().getMethod("getString", int.class);

            for (int i = 0; i < size; i++) {
                Object ress = getFromOptional(method.invoke(ls, i));
                list.add(ress == null ? "" : ress.toString());
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getObjectList(String path, int type) {

        if (!hasNBT())
            return null;

        if (!this.hasNBT(path))
            return null;

        try {
            Object t = updateLegacyTag(getNbt());

            if (t != null && path.contains(".")) {
                List<String> keys = new ArrayList<String>();
                keys.addAll(Arrays.asList(path.split("\\.")));
                try {
                    Object nbtbase = TAG_GET.invoke(t, keys.get(0));
                    for (int i = 1; i < keys.size(); i++) {
                        if (i + 1 < keys.size()) {
                            nbtbase = TAG_GET.invoke(nbtbase, keys.get(i));
                        } else {
                            t = nbtbase;
                            path = keys.get(i);
                            break;
                        }
                    }
                } catch (Throwable e) {
                }
            }

            if (t == null)
                return null;

            Object ls = getFromOptional(TAG_GET_LIST.invoke(t, path));
            int size = (int) ls.getClass().getMethod("size").invoke(ls);

            if (size == 0 && type < 0) {
                ls = getFromOptional(TAG_GET_LIST.invoke(t, path));
                size = (int) ls.getClass().getMethod("size").invoke(ls);
            }

            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object removeItemTag(String path) {

        if (!type.equals(nmbtType.item))
            return object;

        if (!hasNBT(path))
            return object;

        try {
            Object t = updateLegacyTag(getNbt());
            TAG_REMOVE.invoke(t, path);
            return setTag((ItemStack) object, t);
        } catch (Throwable e) {
            e.printStackTrace();
            return object;
        }
    }

    public Object setBoolean(String path, Boolean value) {

        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setByte(String path, Byte value) {

        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setShort(String path, Short value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setStringList(String path, List<String> value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setString(String path, String value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setInt(String path, Integer value) {

        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;

    }

    public Object setIntArray(String path, int[] value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setByteArray(String path, byte[] value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setLongArray(String path, long[] value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setLong(String path, Long value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;
    }

    public Object setDouble(String path, Double value) {
        if (value == null) {
            return remove(path);
        } else
            CMIPersistentDataContainer.get(object).set(path, value).save();
        return object;

    }

    public Object remove(String path) {
        @Nullable
        CMIPersistentDataContainer it = CMIPersistentDataContainer.get(object);
        it.remove(path).save();

        if (getType().equals(nmbtType.item)) {
            tag = getNbt((ItemStack) object);
        }

        return removeItemTag(path);
    }

    public Object set(String path, Object nbtbase) {

        try {
            TAG_PUT_COMPOUND.invoke(getNbt(), path, nbtbase);
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
        }
        switch (type) {
        case block:
            break;
        case entity:
            break;
        case custom:
            return getNbt();
        case item:
            return setTag((ItemStack) object, updateLegacyTag(getNbt()));
        }
        return object;
    }

    public boolean hasNBT() {
        return tag != null;
    }

    public boolean hasNBT(String key) {
        @Nullable
        CMIPersistentDataContainer persistent = CMIPersistentDataContainer.get(object);
        if (persistent != null && persistent.hasKey(key))
            return true;

        if (!hasNBT())
            return false;

        boolean contains = false;
        try {
            contains = (boolean) TAG_CONTAINS.invoke(updateLegacyTag(getNbt()), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contains;
    }

    public Object getNbt() {
        return tag;
    }

    public Set<String> getKeys() {
        if (!hasNBT())
            return new HashSet<String>();

        try {
            return (Set<String>) getNbt().getClass().getMethod("keySet").invoke(getNbt());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return new HashSet<String>();
    }

    public Object get(String path) {
        Object nbt = getNbt();
        if (nbt == null)
            return null;
        try {
            return TAG_GET.invoke(updateLegacyTag(getNbt()), path);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getCompound(String path) {
        Object nbt = getNbt();
        if (nbt == null)
            return null;
        try {
            return getFromOptional(TAG_GET_COMPOUND.invoke(updateLegacyTag(getNbt()), path));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte getTypeId() {
        try {
            return (byte) getNbt().getClass().getMethod("getId").invoke(getNbt());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ItemStack setTag(ItemStack item, Object tag) {
        if (item == null)
            return item;

        Object nms = asNMSCopy(item);

        try {

            Object newCustom = CUSTOMDATA_OF.invoke(null, tag);
            NMS_SET_COMPONENT.invoke(nms, CUSTOM_DATA_COMPONENT.get(null), newCustom);

            return (ItemStack) asBukkitCopy(nms);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return item;
    }

//    private static Object setTag(Object nmsStack, Object tag) {
//        try {
//            if (nmsStack == null)
//                return null;
//
//            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
//
////                return nmsStack;
//                return getSetTagMethod(nmsStack).invoke(nmsStack, getRegistry(), tag);
//            }
//
//            getSetTagMethod(nmsStack).invoke(nmsStack, tag);
//
//            return nmsStack;
//        } catch (Throwable e) {
//            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
//                e.printStackTrace();
//            return nmsStack;
//        }
//    }

    public static Object getNbt(Entity entity) {
        return null;
    }

    private static Method met_getOrThrow = null;

    public static Object getNbt(ItemStack item) {

        if (item == null || item.getType().equals(Material.AIR))
            return null;

        Object nmsStack = asNMSCopy(item);
        if (nmsStack == null)
            return null;

        try {
            Object res = encodeMethod.invoke(CODEC, serializator, nmsStack);
            if (met_getOrThrow == null)
                met_getOrThrow = res.getClass().getMethod("getOrThrow");
            return met_getOrThrow.invoke(res);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//        try {
//            Object nms = asNMSCopy(item);
//
//            Object componentType = CUSTOM_DATA_COMPONENT.get(null);
//            Object customData = NMS_GET_COMPONENT.invoke(nms, componentType);
//
//            if (customData == null)
//                return null;
//
//            return CUSTOMDATA_COPYTAG.invoke(customData);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
    }

    public static Object getNbt(Block block) {
        return null;
    }

    public static Object asNMSCopy(ItemStack item) {
        if (item == null)
            return null;
        try {
            return AS_NMS_COPY.invoke(CRAFT_ITEM_STACK, item);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object asBukkitCopy(Object item) {
        try {
            return AS_BUKKIT_COPY.invoke(CRAFT_ITEM_STACK, item);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getEntityHandle(Entity ent) {
        return null;
    }

    public static boolean isNBTSimilar(ItemStack is, ItemStack is2) {
        try {
            Object i1 = asNMSCopy(is);
            Object i2 = asNMSCopy(is2);
            return (boolean) TAG_EQUALS.invoke(NMS_ITEM_STACK, i1, i2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    @Deprecated
    public static ItemStack HideFlag(ItemStack item, int state) {

        ItemMeta meta = item.getItemMeta();

        if (Version.isPaperBranch()) {
            // Paper doesn't initialize this for some reason
            try {
                Multimap<Attribute, AttributeModifier> modifiers = meta.getAttributeModifiers();
                if (modifiers == null) {
                    modifiers = HashMultimap.create();
                    meta.setAttributeModifiers(modifiers);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        for (ItemFlag one : ItemFlag.values()) {
            if ((state & 1 << one.ordinal()) != 0) {
                meta.addItemFlags(one);
            }
        }
        item.setItemMeta(meta);
        return item;

    }

    public static ItemStack modifyItemStack(ItemStack stack, String arguments) {
        return stack;
    }

    public static String toJson(ItemStack item) {
        if (item == null)
            return null;

        return getNbt(item).toString();
    }

    // For 1.13 and older servers
    public static EntityType getEggType(ItemStack item) {
        if (!CMIMaterial.isMonsterEgg(item.getType()))
            return null;

        CMIEntityType type = CMIEntityType.get(item.getType().toString().replace("_SPAWN_EGG", ""));
        return type == null ? null : type.getType();
    }

    // For 1.13 and older servers
    public static ItemStack setEggType(ItemStack item, EntityType etype) {

        if (!item.getType().toString().contains("_EGG"))
            return null;
        try {
            Object tag = getNbt(item);

            Object ttag = TAG_GET_COMPOUND.invoke(tag, "EntityTag");

            if (ttag == null)
                ttag = COMPOUND_TAG.getDeclaredConstructor().newInstance();

            CMIEntityType ce = CMIEntityType.get(etype);
            if (ce == null)
                return item;

            TAG_PUT_STRING.invoke(ttag, "id", ce.getName());
            TAG_PUT_COMPOUND.invoke(tag, "EntityTag", ttag);

            setTag(item, tag);
            return (ItemStack) asBukkitCopy(asNMSCopy(item));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object newNBTTagList(int type) {
        try {
            return COMPOUND_TAGLIST.getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addToList(Object list, int size, Object data) {
    }

    public nmbtType getType() {
        return type;
    }
}
