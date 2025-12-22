package net.Zrips.CMILib.NBT;

import java.lang.reflect.InvocationTargetException;
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
import net.Zrips.CMILib.PersistentData.CMIPersistentDataContainer;
import net.Zrips.CMILib.Version.Version;

public class CMINBT {

    private static Class<?> NBTBase;
    private static Class<?> NBTTagString;
    private static Class<?> nbtTagCompound;
    private static Class<?> nbtTagList;
    private static Class<?> holderLookupAClass;
    private static Method met_getInt;
    private static Method met_getByte;
    private static Method met_getLong;
    private static Method met_getBoolean;
    private static Method met_getFloat;
    private static Method met_getDouble;
    private static Method met_getByteArray;
    private static Method met_getIntArray;
    private static Method met_getLongArray;
    private static Method met_getList;
    private static Method met_get;
    private static Method met_remove;
    private static Method met_getShort;
    private static Method met_getString;
    private static Method met_getCompound;
    private static Method met_toString;
    private static Method met_setBoolean;
    private static Method met_setDouble;
    private static Method met_setByte;
    private static Method met_setShort;
    private static Method met_setString;
    private static Method met_setInt;
    private static Method met_setLong;
    private static Method met_setIntArray;
    private static Method met_setByteArray;
    private static Method met_setLongArray;
    private static Method met_set;
    private static Method met_add;

    private static Method met_tagStringValueOf;

    private static Class<?> CraftItemStack;
    private static Class<?> IStack;
    private static Class<?> CraftEntity;
    private static Class<?> MojangsonParser;

    private static Object CODEC;
    private static Object serializator;
    private static Method encodeMethod;

    private static String getTagName = "getTag";
    private static String setTagName = "setTag";

    private static String getStringName = "getString";
    private static String getIntName = "getInt";
    private static String getByteName = "getByte";
    private static String getLongName = "getLong";
    private static String getBooleanName = "getBoolean";
    private static String getFloatName = "getFloat";
    private static String getShortName = "getShort";
    private static String getDoubleName = "getDouble";
    private static String getListName = "getList";
    private static String getByteArrayName = "getByteArray";
    private static String getIntArrayName = "getIntArray";
    private static String getLongArrayName = "getLongArray";

    private static String listGetName = "get";

    private static String setBooleanName = "setBoolean";
    private static String setByteName = "setByte";
    private static String setShortName = "setShort";
    private static String setStringName = "setString";
    private static String setIntName = "setInt";
    private static String setLongName = "setLong";
    private static String setDoubleName = "setDouble";
    private static String setIntArrayName = "setIntArray";
    private static String setByteArrayName = "setByteArray";
    private static String setLongArrayName = "a";

    private static String setName = "set";
    private static String getName = "get";
    private static String removeName = "remove";
    private static String getCompoundName = "getCompound";
    private static String asStringName = "asString";

    private static String saveName = "save";
    private static String parseName = "parse";
    private static String itemSaveName = "save";

    private static String hasKeyName = "hasKey";
    private static String getKeysName = "getKeys";

    private static String getTypeIdName = "getTypeId";

    private static String listAddMethod = "add";

    static {
        if (Version.isMojangMappings()) {

            try {
                CraftItemStack = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
                IStack = Class.forName("net.minecraft.world.item.ItemStack");
                CraftEntity = Class.forName("org.bukkit.craftbukkit.entity.CraftEntity");
                Class<?> dynamicOps = Class.forName("com.mojang.serialization.DynamicOps");

                Method met_createSerializationContext = getRegistry().getClass().getMethod("createSerializationContext", dynamicOps);

                CODEC = IStack.getField("CODEC").get(null);

                Class<?> dops = Class.forName("net.minecraft.nbt.NbtOps");
                Object dops_Instance = dops.getField("INSTANCE").get(null);

                nbtTagCompound = Class.forName("net.minecraft.nbt.CompoundTag");
                NBTBase = Class.forName("net.minecraft.nbt.Tag");
                NBTTagString = Class.forName("net.minecraft.nbt.StringTag");
                nbtTagList = Class.forName("net.minecraft.nbt.ListTag");

                serializator = met_createSerializationContext.invoke(getRegistry(), dops_Instance);

                encodeMethod = CODEC.getClass().getMethod("encodeStart", dynamicOps, Object.class);

            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                holderLookupAClass = Class.forName("net.minecraft.core.HolderLookup$Provider");
            } catch (Throwable e) {
            }

        } else if (Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {
            try {
                CraftItemStack = getBukkitClass("inventory.CraftItemStack");
                IStack = Class.forName("net.minecraft.world.item.ItemStack");
                CraftEntity = getBukkitClass("entity.CraftEntity");

                Class<?> dynamicOps = Class.forName("com.mojang.serialization.DynamicOps");
                Method met_createSerializationContext = null;
                Object dops_Instance = null;
                if (Version.isPaperBranch()) {
                    met_createSerializationContext = getRegistry().getClass().getMethod("createSerializationContext", dynamicOps);
                    CODEC = IStack.getField("CODEC").get(null);
                    Class<?> dops = Class.forName("net.minecraft.nbt.NbtOps");
                    dops_Instance = dops.getDeclaredField("INSTANCE").get(dops);
                    nbtTagCompound = Class.forName("net.minecraft.nbt.CompoundTag");
                    NBTBase = Class.forName("net.minecraft.nbt.Tag");
                    NBTTagString = Class.forName("net.minecraft.nbt.StringTag");
                    nbtTagList = Class.forName("net.minecraft.nbt.ListTag");
                } else {
                    met_createSerializationContext = getRegistry().getClass().getMethod("a", dynamicOps);
                    CODEC = IStack.getField("b").get(null);
                    Class<?> dops = Class.forName("net.minecraft.nbt.DynamicOpsNBT");
                    dops_Instance = dops.getDeclaredField("a").get(dops);
                    nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
                    NBTBase = Class.forName("net.minecraft.nbt.NBTBase");
                    NBTTagString = Class.forName("net.minecraft.nbt.NBTTagString");
                    nbtTagList = Class.forName("net.minecraft.nbt.NBTTagList");
                }
                serializator = met_createSerializationContext.invoke(getRegistry(), dops_Instance);

                encodeMethod = CODEC.getClass().getMethod("encodeStart", dynamicOps, Object.class);

            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                holderLookupAClass = Class.forName("net.minecraft.core.HolderLookup$a");
            } catch (Throwable e) {
            }

            // As of 1.21.5, the NBTBase class is removed so we can skip all this
        } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                NBTBase = Class.forName("net.minecraft.nbt.NBTBase");
                nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
                nbtTagList = Class.forName("net.minecraft.nbt.NBTTagList");
                CraftItemStack = getBukkitClass("inventory.CraftItemStack");
                IStack = Class.forName("net.minecraft.world.item.ItemStack");
                CraftEntity = getBukkitClass("entity.CraftEntity");
                MojangsonParser = Class.forName("net.minecraft.nbt.MojangsonParser");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                NBTTagString = Class.forName("net.minecraft.nbt.NBTTagString");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                holderLookupAClass = Class.forName("net.minecraft.core.HolderLookup$a");
            } catch (Throwable e) {
            }
        } else {
            try {
                NBTBase = getMinecraftClass("NBTBase");
                nbtTagCompound = getMinecraftClass("NBTTagCompound");
                nbtTagList = getMinecraftClass("NBTTagList");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                NBTTagString = getMinecraftClass("NBTTagString");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CraftItemStack = getBukkitClass("inventory.CraftItemStack");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                IStack = getMinecraftClass("ItemStack");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CraftEntity = getBukkitClass("entity.CraftEntity");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                MojangsonParser = getMinecraftClass("MojangsonParser");
            } catch (Throwable e) {
            }
        }

        if (Version.isMojangMappings()) {
            
            asStringName = "toString";
            hasKeyName = "contains";
            listGetName = "getString";
            getKeysName = "keySet";
            getTypeIdName = "getId";

        } else if (Version.isPaperBranch() && Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {
            // Keeping defaults
            asStringName = "toString";
            hasKeyName = "contains";
            listGetName = "getString";
            getKeysName = "keySet";
            getTypeIdName = "getId";
        } else {
            if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
                getStringName = "l";
                setTagName = "c";
                getIntName = "h";
                getByteName = "f";
                getLongName = "i";
                getBooleanName = "q";
                getFloatName = "j";
                getShortName = "g";
                getDoubleName = "k";
                getListName = "c";
                getByteArrayName = "m";
                getIntArrayName = "n";
                getLongArrayName = "o";

                setBooleanName = "a";
                setByteName = "a";
                setShortName = "a";
                setStringName = "a";
                setIntName = "a";
                setLongName = "a";
                setDoubleName = "a";
                setIntArrayName = "a";
                setByteArrayName = "a";

                listGetName = "k";

                setName = "a";
                getName = "c";
                removeName = "r";
                getCompoundName = "p";
                asStringName = "e_";

                hasKeyName = "e";
                getKeysName = "d";

                saveName = "f";
                parseName = "a";
                itemSaveName = "b";

                getTypeIdName = "a";

            }
            if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
                listAddMethod = "b";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                listAddMethod = "add";
            }

            if (Version.isCurrentEqual(Version.v1_18_R1)) {
                getTagName = "s";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_18_R2)) {
                getTagName = "t";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                getTagName = "u";
            }

            if (Version.isCurrentEqualOrLower(Version.v1_12_R1)) {
                asStringName = "toString";
                getKeysName = "c";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_19_R2)) {
                asStringName = "f_";
                getKeysName = "e";
                getTypeIdName = "b";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                asStringName = "m_";
                getTagName = "v";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                asStringName = "toString";
                listGetName = "j";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                getTagName = "v";
                setTagName = "b";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_21_R4)) {
                getListName = "o";
                itemSaveName = "a";

                saveName = "h";

                getName = "a";
                hasKeyName = "b";

                getStringName = "i";
                getIntName = "e";
                getByteName = "c";
                getLongName = "f";
                getBooleanName = "q";
                getFloatName = "g";
                getShortName = "d";
                getDoubleName = "h";
                getByteArrayName = "j";
                getIntArrayName = "k";
                getLongArrayName = "l";

                listGetName = "c";
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {
                listGetName = "m";
            }
        }

        try {

            if (Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {

                met_getString = nbtTagCompound.getMethod(getStringName, String.class);
                met_getInt = nbtTagCompound.getMethod(getIntName, String.class);
                met_getByte = nbtTagCompound.getMethod(getByteName, String.class);
                met_getLong = nbtTagCompound.getMethod(getLongName, String.class);
                met_getBoolean = nbtTagCompound.getMethod(getBooleanName, String.class);
                met_getFloat = nbtTagCompound.getMethod(getFloatName, String.class);
                met_getShort = nbtTagCompound.getMethod(getShortName, String.class);
                met_getDouble = nbtTagCompound.getMethod(getDoubleName, String.class);

                met_getList = nbtTagCompound.getMethod(getListName, String.class);

                met_getByteArray = nbtTagCompound.getMethod(getByteArrayName, String.class);
                met_getIntArray = nbtTagCompound.getMethod(getIntArrayName, String.class);

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    met_getLongArray = nbtTagCompound.getMethod(getLongArrayName, String.class);
                }

                met_get = nbtTagCompound.getMethod(getName, String.class);
                met_remove = nbtTagCompound.getMethod(removeName, String.class);

                met_getCompound = nbtTagCompound.getMethod(getCompoundName, String.class);

                met_toString = nbtTagCompound.getMethod(asStringName);

                try {
                    met_tagStringValueOf = NBTTagString.getMethod("a", String.class);
                } catch (Throwable e) {
                    try {
                        met_tagStringValueOf = NBTTagString.getMethod("valueOf", String.class);
                    } catch (Throwable ex) {

                    }
                }

            } else {
                met_getString = nbtTagCompound.getMethod(getStringName, String.class);
                met_getInt = nbtTagCompound.getMethod(getIntName, String.class);
                met_getByte = nbtTagCompound.getMethod(getByteName, String.class);
                met_getLong = nbtTagCompound.getMethod(getLongName, String.class);
                met_getBoolean = nbtTagCompound.getMethod(getBooleanName, String.class);
                met_getFloat = nbtTagCompound.getMethod(getFloatName, String.class);
                met_getShort = nbtTagCompound.getMethod(getShortName, String.class);
                met_getDouble = nbtTagCompound.getMethod(getDoubleName, String.class);

                if (Version.isCurrentEqualOrHigher(Version.v1_21_R4)) {
                    met_getList = nbtTagCompound.getMethod(getListName, String.class);
                } else
                    met_getList = nbtTagCompound.getMethod(getListName, String.class, int.class);

                met_getByteArray = nbtTagCompound.getMethod(getByteArrayName, String.class);
                met_getIntArray = nbtTagCompound.getMethod(getIntArrayName, String.class);

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                    met_getLongArray = nbtTagCompound.getMethod(getLongArrayName, String.class);
                }

                met_get = nbtTagCompound.getMethod(getName, String.class);
                met_remove = nbtTagCompound.getMethod(removeName, String.class);

                met_getCompound = nbtTagCompound.getMethod(getCompoundName, String.class);

                met_toString = nbtTagCompound.getMethod(asStringName);

                met_setBoolean = nbtTagCompound.getMethod(setBooleanName, String.class, boolean.class);
                met_setByte = nbtTagCompound.getMethod(setByteName, String.class, byte.class);
                met_setShort = nbtTagCompound.getMethod(setShortName, String.class, short.class);
                met_setString = nbtTagCompound.getMethod(setStringName, String.class, String.class);
                met_setInt = nbtTagCompound.getMethod(setIntName, String.class, int.class);
                met_setLong = nbtTagCompound.getMethod(setLongName, String.class, long.class);
                met_setDouble = nbtTagCompound.getMethod(setDoubleName, String.class, double.class);

                met_setIntArray = nbtTagCompound.getMethod(setIntArrayName, String.class, int[].class);

                try {
                    met_setByteArray = nbtTagCompound.getMethod(setByteArrayName, String.class, byte[].class);
                    if (Version.isCurrentEqualOrHigher(Version.v1_13_R1))
                        met_setLongArray = nbtTagCompound.getMethod(setLongArrayName, String.class, long[].class);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }

                met_set = nbtTagCompound.getMethod(setName, String.class, NBTBase);

                // Will fail on 1.7.10 servers
                try {
                    met_add = nbtTagList.getMethod(listAddMethod, int.class, NBTBase);
                } catch (Throwable e) {
                }

                try {
                    met_tagStringValueOf = NBTTagString.getMethod("a", String.class);
                } catch (Throwable e) {
                    try {
                        met_tagStringValueOf = NBTTagString.getMethod("valueOf", String.class);
                    } catch (Throwable ex) {

                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    Object tag;
    Object object;

    private nmbtType type;

    public enum nmbtType {
        item, block, entity, custom;
    }

    static {

    }

    public CMINBT() {
        try {
            tag = nbtTagCompound.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        type = nmbtType.custom;
    }

    public CMINBT(Object nbtTagCompound) {
        tag = nbtTagCompound;
        type = nmbtType.custom;
    }

    public CMINBT(ItemStack item) {
        object = item;
        tag = getNbt(item);
        type = nmbtType.item;
    }

    public CMINBT(Block block) {
        tag = getNbt(block);
        object = block;
        type = nmbtType.block;
    }

    public CMINBT(Entity entity) {
        tag = getNbt(entity);
        object = entity;
        type = nmbtType.entity;
    }

    private Object get(String path, Method getMethod) {
        if (tag == null)
            return null;
        try {
            if (!path.contains("."))
                return getMethod.invoke(tag, path);

            Object t = updateLegacyTag(tag);

            List<String> keys = new ArrayList<String>();
            keys.addAll(Arrays.asList(path.split("\\.")));
            try {
                Object nbtbase = met_get.invoke(t, keys.get(0));
                for (int i = 1; i < keys.size(); i++) {
                    if (i + 1 < keys.size()) {
                        nbtbase = met_get.invoke(nbtbase, keys.get(i));
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
        }
        return null;
    }

    public Integer getInt(String path) {
        if (!this.hasNBT(path))
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                Integer v = persistentDataContainer.getInt(path);
                if (v != null)
                    return v;
            }
        }

        try {
            Object res = getFromOptional(get(path, met_getInt));
            return res == null ? null : (Integer) res;
        } catch (Exception e) {
        }
        return null;
    }

    public Byte getByte(String path) {
        if (!this.hasNBT(path))
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                Byte v = CMIPersistentDataContainer.get(object).getByte(path);
                if (v != null)
                    return v;
            }
        }

        try {
            Object res = getFromOptional(get(path, met_getByte));
            return res == null ? null : (Byte) res;
        } catch (Exception e) {
        }
        return null;
    }

    public Long getLong(String path) {
        if (!this.hasNBT(path))
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                Long v = persistentDataContainer.getLong(path);

                if (v != null)
                    return v;
            }
        }

        try {
            Object res = getFromOptional(get(path, met_getLong));
            return res == null ? null : (Long) res;
        } catch (Exception e) {
        }
        return null;
    }

    public Boolean getBoolean(String path) {
        if (!this.hasNBT(path))
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                Boolean v = persistentDataContainer.getBoolean(path);

                if (v != null)
                    return v;
            }
        }

        try {
            Object res = getFromOptional(get(path, met_getBoolean));
            return res == null ? null : (Boolean) res;
        } catch (Exception e) {
        }
        return null;
    }

    public float getFloat(String path) {
        if (!this.hasNBT(path))
            return 0.0F;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                Float v = persistentDataContainer.getFloat(path);

                if (v != null)
                    return v;
            }
        }

        try {
            Object res = getFromOptional(get(path, met_getFloat));
            return res == null ? 0.0F : (Float) res;
        } catch (Exception e) {
        }
        return 0.0F;
    }

    public Short getShort(String path) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                Short v = persistentDataContainer.getShort(path);
                if (v != null)
                    return v;
            }
        }

        if (tag == null)
            return null;

        try {
            Object res = getFromOptional(get(path, met_getShort));
            return res == null ? null : (Short) res;
        } catch (Exception e) {
        }
        return null;
    }

    public double getDouble(String path) {
        if (!this.hasNBT(path))
            return 0.0D;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                Double v = persistentDataContainer.getDouble(path);

                if (v != null)
                    return v;
            }
        }

        try {
            Object res = getFromOptional(get(path, met_getDouble));
            return res == null ? 0.0D : (Double) res;
        } catch (Exception e) {
        }
        return 0.0D;
    }

    public byte[] getByteArray(String path) {
        if (!this.hasNBT(path))
            return new byte[0];

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                byte[] v = persistentDataContainer.getByteArray(path);

                if (v != null)
                    return v;
            }
        }

        try {
            Object res = getFromOptional(get(path, met_getByteArray));
            return res == null ? new byte[0] : (byte[]) res;
        } catch (Exception e) {
        }
        return new byte[0];
    }

    public int[] getIntArray(String path) {
        if (!this.hasNBT(path))
            return new int[0];

        try {
            Object res = getFromOptional(get(path, met_getIntArray));
            return res == null ? new int[0] : (int[]) res;
        } catch (Exception e) {
        }
        return new int[0];
    }

    public long[] getLongArray(String path) {
        if (!this.hasNBT(path))
            return new long[0];

        if (!Version.isCurrentEqualOrHigher(Version.v1_13_R1))
            return new long[0];

        try {
            Object res = getFromOptional(get(path, met_getLongArray));
            return res == null ? new long[0] : (long[]) res;
        } catch (Exception e) {
        }
        return new long[0];
    }

    public String getString(String path) {

        if (!this.hasNBT(path))
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                String v = persistentDataContainer.getString(path);
                if (v != null) {
                    return v;
                }
            }
        }

        if (tag == null)
            return null;

        try {
            Object res = getFromOptional(get(path, met_getString));
            return res == null ? null : (String) res;
        } catch (Exception e) {
        }
        return null;
    }

    public List<String> getList(String path) {
        return getList(path, -1);
    }

    private Object updateLegacyTag(Object t) {

        if (getType().equals(nmbtType.item) && Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            t = getFromOptional(new CMINBT(t).get("components"));
            t = getFromOptional(new CMINBT(t).get("minecraft:custom_data"));
        }
        return t;
    }

    private static Object getFromOptional(Object optional) {
        if (Version.isCurrentEqualOrLower(Version.v1_21_R3))
            return optional;
        if (optional instanceof Optional)
            return ((Optional) optional).isPresent() ? ((Optional) optional).get() : null;
        return optional;
    }

    public List<String> getList(String path, int type) {
        if (!this.hasNBT(path))
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistentDataContainer = CMIPersistentDataContainer.get(object);
            if (persistentDataContainer != null) {
                List<String> v = persistentDataContainer.getListString(path);
                if (v != null) {
                    return new ArrayList<String>(v);
                }
            }
        }

        if (tag == null)
            return null;

        List<String> list = new ArrayList<String>();
        try {

            Object t = updateLegacyTag(tag);

            if (t == null)
                return list;

            if (path.contains(".")) {
                List<String> keys = new ArrayList<String>();
                keys.addAll(Arrays.asList(path.split("\\.")));
                try {
                    Object nbtbase = met_get.invoke(t, keys.get(0));
                    for (int i = 1; i < keys.size(); i++) {
                        if (i + 1 < keys.size()) {
                            nbtbase = met_get.invoke(nbtbase, keys.get(i));
                        } else {
                            t = nbtbase;
                            path = keys.get(i);
                            break;
                        }
                    }
                } catch (Throwable e) {
                }
            }

            Object ls = getFromOptional(Version.isCurrentEqualOrLower(Version.v1_21_R3) ? met_getList.invoke(t, path, type < 0 ? 8 : type) : met_getList.invoke(t, path));
            int size = (int) ls.getClass().getMethod("size").invoke(ls);

            if (size == 0 && type < 0) {
                ls = getFromOptional(Version.isCurrentEqualOrLower(Version.v1_21_R3) ? met_getList.invoke(t, path, type < 0 ? 10 : 8) : met_getList.invoke(t, path));
                size = (int) ls.getClass().getMethod("size").invoke(ls);
            }

            Method method = ls.getClass().getMethod(listGetName, int.class);

            if (Version.isCurrentEqualOrLower(Version.v1_12_R1)) {
                method = ls.getClass().getMethod("getString", int.class);
                for (int i = 0; i < size; i++) {
                    Object ress = getFromOptional(method.invoke(ls, i));
                    String line = (String) ress;
                    list.add(line);
                }
            } else {
                for (int i = 0; i < size; i++) {
                    Object ress = null;
                    if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                        ress = getFromOptional(method.invoke(ls, i));
                    } else {
                        ress = getFromOptional(met_toString.invoke(method.invoke(ls, i)));
                    }
                    list.add(ress == null ? "" : ress.toString());
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getObjectList(String path, int type) {
        if (tag == null)
            return null;
        if (!this.hasNBT(path))
            return null;

        try {
            Object t = updateLegacyTag(tag);

            if (t != null && path.contains(".")) {
                List<String> keys = new ArrayList<String>();
                keys.addAll(Arrays.asList(path.split("\\.")));
                try {
                    Object nbtbase = met_get.invoke(t, keys.get(0));
                    for (int i = 1; i < keys.size(); i++) {
                        if (i + 1 < keys.size()) {
                            nbtbase = met_get.invoke(nbtbase, keys.get(i));
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

            Object ls = getFromOptional(Version.isCurrentEqualOrLower(Version.v1_21_R3) ? met_getList.invoke(t, path, type < 0 ? 8 : type) : met_getList.invoke(t, path));
            int size = (int) ls.getClass().getMethod("size").invoke(ls);

            if (size == 0 && type < 0) {
                ls = getFromOptional(Version.isCurrentEqualOrLower(Version.v1_21_R3) ? met_getList.invoke(t, path, 10) : met_getList.invoke(t, path));
                size = (int) ls.getClass().getMethod("size").invoke(ls);
            }

            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object setBoolean(String path, Boolean value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        switch (type) {
        case block:
            break;
        case entity:
            break;
        case item:
            try {
                if (value == null) {
                    met_remove.invoke(tag, path);
                } else {
                    met_setBoolean.invoke(tag, path, value);
                }
                return setTag((ItemStack) object, tag);
            } catch (Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();
                return object;
            }
        default:
            break;
        }
        return object;
    }

    public Object setByte(String path, Byte value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        switch (type) {
        case block:
            break;
        case entity:
            break;
        case item:
            try {
                if (value == null) {
                    met_remove.invoke(tag, path);
                } else {
                    met_setByte.invoke(tag, path, value);
                }
                return setTag((ItemStack) object, tag);
            } catch (Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();
                return object;
            }
        default:
            break;
        }
        return object;
    }

    public Object setShort(String path, Short value) {
        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        switch (type) {
        case block:
            break;
        case entity:
            break;
        case item:
            try {
                if (value == null) {
                    met_remove.invoke(tag, path);
                } else {
                    met_setShort.invoke(tag, path, value);
                }
                return setTag((ItemStack) object, tag);
            } catch (Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();
                return object;
            }
        default:
            break;
        }
        return object;
    }

    public Object setStringList(String path, List<String> value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        try {

            Object nbtbase = tag;
            String realPath = path;

            if (tag != null && path.contains(".")) {
                List<String> keys = new ArrayList<String>();
                keys.addAll(Arrays.asList(path.split("\\.")));
                try {
                    nbtbase = met_get.invoke(tag, keys.get(0));
                    for (int i = 1; i < keys.size(); i++) {
                        if (i + 1 < keys.size()) {
                            nbtbase = met_get.invoke(nbtbase, keys.get(i));
                        } else {
                            realPath = keys.get(i);
                            break;
                        }
                    }
                } catch (Throwable e) {
                }
            }

            if (value == null) {
                met_remove.invoke(nbtbase, realPath);
            } else {
                Object tagList = nbtTagList.newInstance();

                for (int i = 0; i < value.size(); i++) {
                    try {
                        addToList(tagList, i, met_tagStringValueOf.invoke(null, value.get(i)));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                met_set.invoke(nbtbase, realPath, tagList);
            }
        } catch (Throwable e) {
            return object;
        }
        switch (type) {
        case custom:
            return tag;
        case block:
            break;
        case entity:
            break;
        case item:

            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object setString(String path, String value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        try {

            Object nbtbase = tag;
            String realPath = path;

            if (tag != null && path.contains(".")) {
                List<String> keys = new ArrayList<String>();
                keys.addAll(Arrays.asList(path.split("\\.")));
                try {
                    nbtbase = met_get.invoke(tag, keys.get(0));
                    for (int i = 1; i < keys.size(); i++) {
                        if (i + 1 < keys.size()) {
                            nbtbase = met_get.invoke(nbtbase, keys.get(i));
                        } else {
                            realPath = keys.get(i);
                            break;
                        }
                    }
                } catch (Throwable e) {
                }
            }

            if (value == null) {
                met_remove.invoke(nbtbase, realPath);
            } else {
                met_setString.invoke(nbtbase, realPath, value);
            }
        } catch (Throwable e) {
//		if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
//		    e.printStackTrace();
            return object;
        }
        switch (type) {
        case custom:
            return tag;
        case block:
            break;
        case entity:
            break;
        case item:

            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object setInt(String path, Integer value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        try {
            if (value == null) {
                met_remove.invoke(tag, path);
            } else {
                met_setInt.invoke(tag, path, value);
            }
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return object;
        }
        switch (type) {
        case custom:
            return tag;
        case block:
            break;
        case entity:
            break;
        case item:
            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object setIntArray(String path, int[] value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        try {
            if (value == null) {
                met_remove.invoke(tag, path);
            } else {
                met_setIntArray.invoke(tag, path, value);
            }
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return object;
        }
        switch (type) {
        case custom:
            return tag;
        case block:
            break;
        case entity:
            break;
        case item:
            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object setByteArray(String path, byte[] value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        try {
            if (value == null) {
                met_remove.invoke(tag, path);
            } else {
                met_setByteArray.invoke(tag, path, value);
            }
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return object;
        }
        switch (type) {
        case custom:
            return tag;
        case block:
            break;
        case entity:
            break;
        case item:
            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object setLongArray(String path, long[] value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        try {
            if (value == null) {
                met_remove.invoke(tag, path);
            } else {
                met_setLongArray.invoke(tag, path, value);
            }
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return object;
        }
        switch (type) {
        case custom:
            return tag;
        case block:
            break;
        case entity:
            break;
        case item:
            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object setLong(String path, Long value) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        switch (type) {
        case custom:
            try {
                if (value == null) {
                    met_remove.invoke(tag, path, value);
                } else {
                    met_setLong.invoke(tag, path, value);
                }
                return tag;
            } catch (Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();
                return object;
            }
        case block:
            break;
        case entity:
            break;
        case item:
            try {
                if (value == null) {
                    met_remove.invoke(tag, path, value);
                } else {
                    met_setLong.invoke(tag, path, value);
                }
                return setTag((ItemStack) object, tag);
            } catch (Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();
                return object;
            }
        default:
            break;
        }
        return object;
    }

    public Object setDouble(String path, Double value) {
        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            if (value == null)
                CMIPersistentDataContainer.get(object).remove(path).save();
            else
                CMIPersistentDataContainer.get(object).set(path, value).save();
            return object;
        }

        try {
            if (value == null) {
                met_remove.invoke(tag, path, value);
            } else {
                met_setDouble.invoke(tag, path, value);
            }
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return object;
        }

        switch (type) {
        case custom:
            return tag;
        case block:
            break;
        case entity:
            break;
        case item:
            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object remove(String path) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            CMIPersistentDataContainer.get(object).remove(path).save();
            return object;
        }

        try {
            met_remove.invoke(tag, path);
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return object;
        }
        switch (type) {
        case block:
            break;
        case entity:
        case custom:
            return tag;
        case item:
            return setTag((ItemStack) object, tag);
        default:
            break;
        }
        return object;
    }

    public Object set(String path, Object nbtbase) {

        try {
            met_set.invoke(tag, path, nbtbase);
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
            return tag;
        case item:
            return setTag((ItemStack) object, tag);
        }
        return object;
    }
//    @Override
//    public ItemStack setNBTList(ItemStack item, String name, List<String> list) {
//	net.minecraft.server.v1_16_R3.ItemStack nmsIs = CraftItemStack.asNMSCopy(item);
//	if (nmsIs == null)
//	    return null;
//	try {
//	    NBTTagCompound tag = nmsIs.getTag();
//	    if (tag == null)
//		tag = new NBTTagCompound();
//
//	    NBTTagList tagList = new NBTTagList();
//	    if (list != null)
//		for (String one : list) {
//		    tagList.add(NBTTagString.a(one));
//		}
//	    if (list == null)
//		tag.remove(name);
//	    else
//		tag.set(name, tagList);
//	    nmsIs.setTag(tag);
//	    return CraftItemStack.asBukkitCopy(nmsIs);
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//	return null;
//    }

//    public Object setList(String path, List<String> list) {
//	switch (type) {
//	case block:
//	    break;
//	case entity:
//	    break;
//	case item:
//	    try {
//		
//		
//		
//		Method meth = tag.getClass().getMethod("setLong", String.class, long.class);
//		meth.invoke(tag, path, value);
//		return CMI.getInstance().getReflectionManager().setTag((ItemStack) object, tag);
//	    } catch (Throwable e) {
//		if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
//		    e.printStackTrace();
//		return object;
//	    }
//	default:
//	    break;
//	}
//	return object;
//    }

    public boolean hasNBT() {
        return tag != null;
    }

    public boolean hasNBT(String key) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            @Nullable
            CMIPersistentDataContainer persistent = CMIPersistentDataContainer.get(object);
            if (persistent != null && persistent.hasKey(key))
                return true;
        }

        Object t = updateLegacyTag(tag);
//        Object t = tag;

        if (t == null)
            return false;

        Method hasKeyMethod = null;
        try {
            hasKeyMethod = t.getClass().getMethod(hasKeyName, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (key.contains(".")) {

            try {
                if ((boolean) hasKeyMethod.invoke(t, key))
                    return true;
            } catch (Throwable e) {
                e.printStackTrace();
            }

            List<String> keys = new ArrayList<String>();
            keys.addAll(Arrays.asList(key.split("\\.")));
            try {
                Object nbtbase = getFromOptional(met_get.invoke(t, keys.get(0)));
                if (nbtbase == null)
                    return false;
                for (int i = 1; i < keys.size(); i++) {
                    if (i + 1 < keys.size()) {
                        nbtbase = getFromOptional(met_get.invoke(nbtbase, keys.get(i)));
                    } else {
                        if (nbtbase == null)
                            break;
                        return (Boolean) getFromOptional(nbtbase.getClass().getMethod(hasKeyName, String.class).invoke(nbtbase, keys.get(i)));
                    }
                }

                return (Boolean) getFromOptional(hasKeyMethod.invoke(t, key));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return false;
        }
        try {
            return (Boolean) getFromOptional((hasKeyMethod.invoke(t, key)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object getNbt() {
        return tag;
    }

    public Set<String> getKeys() {
        Set<String> keys = new HashSet<String>();
        if (!hasNBT())
            return keys;

        try {
            keys = (Set<String>) getNbt().getClass().getMethod(getKeysName).invoke(getNbt());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return keys;
    }

    public Object get(String path) {
        Object nbt = getNbt();
        if (nbt == null)
            return null;
        try {
            return met_get.invoke(nbt, path);
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
            return met_getCompound.invoke(nbt, path);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte getTypeId() {
        try {
            return (byte) getNbt().getClass().getMethod(getTypeIdName).invoke(getNbt());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static Method setTagMethod = null;

    private static Method getSetTagMethod(Object nmsStack) {
        try {
            if (setTagMethod == null) {
                if (Version.isCurrentEqualOrHigher(Version.v1_20_R4))
                    setTagMethod = nmsStack.getClass().getMethod(setTagName, holderLookupAClass, NBTBase);
                else
                    setTagMethod = nmsStack.getClass().getMethod(setTagName, nbtTagCompound);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return setTagMethod;
    }

    public static ItemStack setTag(ItemStack item, Object tag) {
        if (item == null)
            return item;
        try {
            Object nmsStack = asNMSCopy(item);
            return (ItemStack) asBukkitCopy(setTag(nmsStack, tag));
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return item;
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

    private static Object setTag(Object nmsStack, Object tag) {
        try {
            if (nmsStack == null)
                return null;

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                return nmsStack;
//                getSetTagMethod(nmsStack).invoke(nmsStack, getRegistry(), tag);
            }

            getSetTagMethod(nmsStack).invoke(nmsStack, tag);

            return nmsStack;
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
            return nmsStack;
        }
    }

    public static Object getNbt(Entity entity) {
        if (entity == null)
            return null;
        try {
            Object tag = nbtTagCompound.getDeclaredConstructor().newInstance();
            Object nmsStack = getEntityHandle(entity);
            Method methTag = nmsStack.getClass().getMethod(saveName, nbtTagCompound);
            return methTag.invoke(nmsStack, tag);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method met_getOrThrow = null;

    public static Object getNbt(ItemStack item) {

        if (item == null || item.getType().equals(Material.AIR))
            return null;

        Object nmsStack = asNMSCopy(item);
        if (nmsStack == null)
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {
            try {
                Object res = encodeMethod.invoke(CODEC, serializator, nmsStack);
                if (met_getOrThrow == null)
                    met_getOrThrow = res.getClass().getMethod("getOrThrow");
                return met_getOrThrow.invoke(res);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        try {

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                if (nbtMethod == null) {
                    if (Version.isCurrentEqualOrHigher(Version.v1_21_R1)) {
                        nbtMethod = IStack.getMethod("a", holderLookupAClass);
                    } else
                        nbtMethod = IStack.getMethod("b", holderLookupAClass);
                }
                return nbtMethod.invoke(nmsStack, getRegistry());
            }

            Method methTag = nmsStack.getClass().getMethod(getTagName);
            Object tag = methTag.invoke(nmsStack);
            if (tag == null)
                tag = nbtTagCompound.getDeclaredConstructor().newInstance();

            return tag;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getNbt(Block block) {
        if (block == null)
            return null;

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4))
            return null;

        try {

            Object tile = CMILib.getInstance().getReflectionManager().getTileEntityAt(block.getLocation());

            // TileEntity -> getUpdateTag
            String ff = "d";
            switch (Version.getCurrent()) {
            case v1_7_R4:
            case v1_8_R1:
            case v1_8_R3:
            case v1_8_R2:
            case v1_9_R1:
            case v1_9_R2:
            case v1_10_R1:
            case v1_11_R1:
            case v1_12_R1:
                ff = "d";
                break;
            case v1_13_R1:
            case v1_13_R2:
                ff = "aa_";
                break;
            case v1_14_R1:
            case v1_15_R1:
            case v1_16_R1:
            case v1_16_R2:
            case v1_16_R3:
                ff = "b";
                break;
            case v1_17_R1:
            case v1_18_R1:
                ff = "Z_";
                break;
            case v1_18_R2:
                ff = "aa_";
                break;
            case v1_19_R1:
                if (Version.isCurrentSubEqual(0))
                    ff = "ab_";
                else
                    ff = "aa_";
                break;
            case v1_19_R2:
                ff = "ad_";
                break;
            case v1_19_R3:
                ff = "aq_";
                break;
            case v1_20_R1:
                ff = "ao_";
                break;
            case v1_20_R2:
                ff = "as_";
                break;
            case v1_20_R3:
            default:
                ff = "ax_";
                break;
            }

            if (tile == null)
                return null;

            Method methTag = tile.getClass().getMethod(ff);
            return methTag.invoke(tile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Class<?> getBukkitClass(String nmsClassString) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + Version.getCurrent().toString() + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> getMinecraftClass(String nmsClassString) {
        try {
            return Class.forName("net.minecraft.server." + Version.getCurrent().toString() + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    private static Method asNMSCopy = null;

    public static Object asNMSCopy(ItemStack item) {
        if (item == null)
            return null;
        try {
            if (asNMSCopy == null)
                asNMSCopy = CraftItemStack.getMethod("asNMSCopy", ItemStack.class);
            return asNMSCopy.invoke(CraftItemStack, item);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method asBukkitCopy = null;

    public static Object asBukkitCopy(Object item) {
        try {
            if (asBukkitCopy == null)
                asBukkitCopy = CraftItemStack.getMethod("asBukkitCopy", IStack);
            return asBukkitCopy.invoke(CraftItemStack, item);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Method EntityHandle = null;

    public static Object getEntityHandle(Entity ent) {
        Object handle = null;
        try {
            if (EntityHandle == null)
                EntityHandle = CraftEntity.getMethod("getHandle");
            handle = EntityHandle.invoke(CraftEntity.cast(ent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handle;
    }

    public static boolean isNBTSimilar(ItemStack is, ItemStack is2) {
        try {
            Object i1 = asNMSCopy(is);
            Object i2 = asNMSCopy(is2);
            Method meth = null;
            if (Version.isCurrentEqualOrLower(Version.v1_17_R1))
                meth = i1.getClass().getMethod("equals", IStack, IStack);
            else {
                // ItemStack -> tagMatches
                meth = i1.getClass().getMethod("a", IStack, IStack);
            }

            return (boolean) meth.invoke(IStack, i1, i2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    @Deprecated
    public static ItemStack HideFlag(ItemStack item, int state) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
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

        Object nmsStack = asNMSCopy(item);
        try {
            Object tag = getNbt(item);
            met_setInt.invoke(tag, "HideFlags", state);

            nmsStack = setTag(nmsStack, tag);

            return (ItemStack) asBukkitCopy(nmsStack);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return item;
    }

    public static ItemStack modifyItemStack(ItemStack stack, String arguments) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            return stack;
        }

        Object nmsStack = asNMSCopy(stack);
        try {
            Object res = MojangsonParser.getMethod(parseName, String.class).invoke(MojangsonParser, arguments);

            nmsStack = setTag(nmsStack, res);

            Object meta = CraftItemStack.getMethod("getItemMeta", IStack).invoke(CraftItemStack, nmsStack);
            stack.setItemMeta((ItemMeta) meta);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return stack;
    }

    private static Method saveOptionalMethod = null;

    public static String toJson(ItemStack item) {
        if (item == null)
            return null;

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {

//                String meta = item.hasItemMeta() ? item.getItemMeta().getAsString() : "";
//                StringBuilder sb = new StringBuilder();
//                sb.append("{");
//                if (!meta.isEmpty()) {
//                    sb.append("components:").append(meta).append(",");
//                }
//                sb.append("count:").append(item.getAmount()).append(",");
//                sb.append("id:\"").append(item.getType().getKey().toString()).append("\"");
//                sb.append("}");
//                return sb.toString();

                return getNbt(item).toString();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Object nmsStack = asNMSCopy(item);

        try {

            if (saveOptionalMethod == null) {
                if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                    saveOptionalMethod = IStack.getMethod(itemSaveName, holderLookupAClass);
                } else
                    saveOptionalMethod = IStack.getMethod(itemSaveName, nbtTagCompound);
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                return saveOptionalMethod.invoke(nmsStack, getRegistry()).toString();
            }

            return saveOptionalMethod.invoke(nmsStack, nbtTagCompound.newInstance()).toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    // For 1.13 and older servers
    public static EntityType getEggType(ItemStack item) {
        if (!CMIMaterial.isMonsterEgg(item.getType()))
            return null;

        if (Version.isCurrentEqual(Version.v1_12_R1)) {
            try {
                if (Version.isCurrentEqualOrLower(Version.v1_11_R1)) {
                    CMIEntityType cmiType = CMIEntityType.getById(item.getData().getData());
                    if (cmiType != null)
                        return cmiType.getType();
                }
                Object tag = getNbt(item);
                Object base = met_getCompound.invoke(tag, "EntityTag");
                String type = (String) met_getString.invoke(base, "id");
                return EntityType.fromName(type.replace("minecraft:", "").toUpperCase());
            } catch (Exception e) {
                return null;
            }
        }

        CMIEntityType type = CMIEntityType.getByName(item.getType().toString().replace("_SPAWN_EGG", ""));
        return type == null ? null : type.getType();
    }

    // For 1.13 and older servers
    public static ItemStack setEggType(ItemStack item, EntityType etype) {

        if (!item.getType().toString().contains("_EGG"))
            return null;
        try {
            Object tag = getNbt(item);

            Object ttag = met_getCompound.invoke(tag, "EntityTag");

            if (ttag == null)
                ttag = nbtTagCompound.newInstance();

            CMIEntityType ce = CMIEntityType.getByType(etype);
            if (ce == null)
                return item;

            met_setString.invoke(ttag, "id", ce.getName());
            met_set.invoke(tag, "EntityTag", ttag);

            setTag(item, tag);
            return (ItemStack) asBukkitCopy(asNMSCopy(item));
        } catch (Exception e) {
//	    e.printStackTrace();
            return null;
        }
    }

    public static Object newNBTTagList(int type) {
        try {
            return nbtTagList.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addToList(Object list, int size, Object data) {
        if (met_add == null)
            return;
        try {
            met_add.invoke(list, size, data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public nmbtType getType() {
        return type;
    }
}
