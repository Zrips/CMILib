package net.Zrips.CMILib.NBT;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Entities.CMIEntityType;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Version.Version;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

public class CMINBT {

    /**
     * The {@code NBTBase} {@link Class}, stored here for top performance.
     */
    @Nullable
    private static Class<?> nbtBaseClass;

    /**
     * The {@code NBTTagCompound} {@link Class}, stored here for top performance.
     */
    @Nullable
    private static Class<?> nbtTagCompoundClass;

    /**
     * The {@code NBTTagList} {@link Class}, stored here for top performance.
     */
    @Nullable
    private static Class<?> nbtTagListClass;

    /**
     * The {@code getInt} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getIntMethod;

    /**
     * The {@code getByte} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getByteMethod;

    /**
     * The {@code getLong} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getLongMethod;

    /**
     * The {@code getBoolean} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getBooleanMethod;

    /**
     * The {@code getFloat} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getFloatMethod;

    /**
     * The {@code getDouble} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getDoubleMethod;

    /**
     * The {@code getByteArray} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getByteArrayMethod;

    /**
     * The {@code getIntArray} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getIntArrayMethod;

    /**
     * The {@code getLongArray} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getLongArrayMethod;

    /**
     * The {@code getList} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getListMethod;

    /**
     * The {@code get} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getMethod;

    /**
     * The {@code remove} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method removeMethod;

    /**
     * The {@code getShort} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getShortMethod;

    /**
     * The {@code getString} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getStringMethod;

    /**
     * The {@code getCompound} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method getCompoundMethod;

    /**
     * The {@code toString} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method toStringMethod;

    /**
     * The {@code setBoolean} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setBooleanMethod;

    /**
     * The {@code setDouble} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setDoubleMethod;

    /**
     * The {@code setByte} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setByteMethod;

    /**
     * The {@code setShort} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setShortMethod;

    /**
     * The {@code setString} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setStringMethod;

    /**
     * The {@code setInt} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setIntMethod;

    /**
     * The {@code setLong} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setLongMethod;

    /**
     * The {@code setIntArray} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setIntArrayMethod;

    /**
     * The {@code setByteArray} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setByteArrayMethod;

    /**
     * The {@code setLongArray} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setLongArrayMethod;

    /**
     * The {@code set} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method setMethod;

    /**
     * The {@code add} {@link Method}, stored here for top performance.
     */
    @Nullable
    private static Method addMethod;

    /**
     * The {@code CraftItemStack} {@link Class}, stored here for top performance.
     */
    @Nullable
    private static Class<?> craftItemStackClass;

    /**
     * The {@code ItemStack} {@link Class}, stored here for top performance.
     */
    @Nullable
    private static Class<?> itemStackClass;

    /**
     * The {@code CraftEntity} {@link Class}, stored here for top performance.
     */
    @Nullable
    private static Class<?> craftEntityClass;

    /**
     * The {@code MojangsonParser} {@link Class}, stored here for top performance.
     */
    @Nullable
    private static Class<?> mojangsonParserClass;

    /**
     * The getTag method name.
     */
    @NotNull
    private static String getTagName = "getTag";

    /**
     * The setTag method name.
     */
    @NotNull
    private static String setTagName = "setTag";

    /**
     * The getTag method name.
     */
    @NotNull
    private static String getStringName = "getString";

    /**
     * The getInt method name.
     */
    @NotNull
    private static String getIntName = "getInt";

    /**
     * The getByte method name.
     */
    @NotNull
    private static String getByteName = "getByte";

    /**
     * The getLong method name.
     */
    @NotNull
    private static String getLongName = "getLong";

    /**
     * The getBoolean method name.
     */
    @NotNull
    private static String getBooleanName = "getBoolean";

    /**
     * The getFloat method name.
     */
    @NotNull
    private static String getFloatName = "getFloat";

    /**
     * The getShort method name.
     */
    @NotNull
    private static String getShortName = "getShort";

    /**
     * The getDouble method name.
     */
    @NotNull
    private static String getDoubleName = "getDouble";

    /**
     * The getList method name.
     */
    @NotNull
    private static String getListName = "getList";

    /**
     * The getByteArray method name.
     */
    @NotNull
    private static String getByteArrayName = "getByteArray";

    /**
     * The getIntArray method name.
     */
    @NotNull
    private static String getIntArrayName = "getIntArray";

    /**
     * The getLongArray method name.
     */
    @NotNull
    private static String getLongArrayName = "getLongArray";

    /**
     * The get method name.
     */
    @NotNull
    private static String listGetName = "get";

    /**
     * The setBoolean method name.
     */
    @NotNull
    private static String setBooleanName = "setBoolean";

    /**
     * The setByte method name.
     */
    @NotNull
    private static String setByteName = "setByte";

    /**
     * The setShort method name.
     */
    @NotNull
    private static String setShortName = "setShort";

    /**
     * The setString method name.
     */
    @NotNull
    private static String setStringName = "setString";

    /**
     * The setInt method name.
     */
    @NotNull
    private static String setIntName = "setInt";

    /**
     * The setLong method name.
     */
    @NotNull
    private static String setLongName = "setLong";

    /**
     * The setDouble method name.
     */
    @NotNull
    private static String setDoubleName = "setDouble";

    /**
     * The setIntArray method name.
     */
    @NotNull
    private static String setIntArrayName = "setIntArray";

    /**
     * The setByteArray method name.
     */
    @NotNull
    private static String setByteArrayName = "setByteArray";

    /**
     * The setLongArray method name.
     */
    @NotNull
    private static String setLongArrayName = "a";

    /**
     * The set method name.
     */
    @NotNull
    private static String setName = "set";

    /**
     * The get method name.
     */
    @NotNull
    private static String getName = "get";

    /**
     * The remove method name.
     */
    @NotNull
    private static String removeName = "remove";

    /**
     * The getCompound method name.
     */
    @NotNull
    private static String getCompoundName = "getCompound";

    /**
     * The asString method name.
     */
    @NotNull
    private static String asStringName = "asString";

    /**
     * The save method name.
     */
    @NotNull
    private static String saveName = "save";

    /**
     * The parse method name.
     */
    @NotNull
    private static String parseName = "parse";

    /**
     * The save method name.
     */
    @NotNull
    private static String itemSaveName = "save";

    /**
     * The hasKey method name.
     */
    @NotNull
    private static String hasKeyName = "hasKey";

    /**
     * The getKeys method name.
     */
    @NotNull
    private static String getKeysName = "getKeys";

    /**
     * The getTypeId method name.
     */
    @NotNull
    private static String getTypeIdName = "getTypeId";

    /**
     * The add method name.
     */
    @NotNull
    private static String listAddMethod = "add";

    static {
        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
            try {
                nbtBaseClass = net.minecraft.nbt.NBTBase.class;
                nbtTagCompoundClass = net.minecraft.nbt.NBTTagCompound.class;
                nbtTagListClass = net.minecraft.nbt.NBTTagList.class;
                craftItemStackClass = getBukkitClass("inventory.CraftItemStack");
                itemStackClass = net.minecraft.world.item.ItemStack.class;
                craftEntityClass = getBukkitClass("entity.CraftEntity");
                mojangsonParserClass = net.minecraft.nbt.MojangsonParser.class;
            } catch (final Throwable e) {
                e.printStackTrace();
            }
        else {
            try {
                nbtBaseClass = getMinecraftClass("NBTBase");
                nbtTagCompoundClass = getMinecraftClass("NBTTagCompound");
                nbtTagListClass = getMinecraftClass("NBTTagList");
            } catch (final Throwable e) {
                e.printStackTrace();
            }

            try {
                craftItemStackClass = getBukkitClass("inventory.CraftItemStack");
            } catch (final Throwable e) {
                e.printStackTrace();
            }

            try {
                itemStackClass = getMinecraftClass("ItemStack");
            } catch (final Throwable e) {
                e.printStackTrace();
            }

            try {
                craftEntityClass = getBukkitClass("entity.CraftEntity");
            } catch (final Throwable e) {
                e.printStackTrace();
            }

            try {
                mojangsonParserClass = getMinecraftClass("MojangsonParser");
            } catch (final Throwable e) {
            }
        }

        if (Version.isCurrentEqualOrHigher(Version.v1_16_R1))
            setLongArrayName = "a";

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

        if (Version.isCurrentEqualOrHigher(Version.v1_14_R1))
            listAddMethod = "b";
        else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1))
            listAddMethod = "add";

        if (Version.isCurrentEqual(Version.v1_18_R1))
            getTagName = "s";

        if (Version.isCurrentEqualOrHigher(Version.v1_18_R2))
            getTagName = "t";

        if (Version.isCurrentEqualOrHigher(Version.v1_19_R1))
            getTagName = "u";

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

        try {
            getStringMethod = nbtTagCompoundClass.getMethod(getStringName, String.class);
            getIntMethod = nbtTagCompoundClass.getMethod(getIntName, String.class);
            getByteMethod = nbtTagCompoundClass.getMethod(getByteName, String.class);
            getLongMethod = nbtTagCompoundClass.getMethod(getLongName, String.class);
            getBooleanMethod = nbtTagCompoundClass.getMethod(getBooleanName, String.class);
            getFloatMethod = nbtTagCompoundClass.getMethod(getFloatName, String.class);
            getShortMethod = nbtTagCompoundClass.getMethod(getShortName, String.class);
            getDoubleMethod = nbtTagCompoundClass.getMethod(getDoubleName, String.class);

            getListMethod = nbtTagCompoundClass.getMethod(getListName, String.class, int.class);

            getByteArrayMethod = nbtTagCompoundClass.getMethod(getByteArrayName, String.class);
            getIntArrayMethod = nbtTagCompoundClass.getMethod(getIntArrayName, String.class);

            if (Version.isCurrentEqualOrHigher(Version.v1_13_R1))
                getLongArrayMethod = nbtTagCompoundClass.getMethod(getLongArrayName, String.class);

            getMethod = nbtTagCompoundClass.getMethod(getName, String.class);
            removeMethod = nbtTagCompoundClass.getMethod(removeName, String.class);

            getCompoundMethod = nbtTagCompoundClass.getMethod(getCompoundName, String.class);

            toStringMethod = nbtTagCompoundClass.getMethod(asStringName);

            setBooleanMethod = nbtTagCompoundClass.getMethod(setBooleanName, String.class, boolean.class);
            setByteMethod = nbtTagCompoundClass.getMethod(setByteName, String.class, byte.class);
            setShortMethod = nbtTagCompoundClass.getMethod(setShortName, String.class, short.class);
            setStringMethod = nbtTagCompoundClass.getMethod(setStringName, String.class, String.class);
            setIntMethod = nbtTagCompoundClass.getMethod(setIntName, String.class, int.class);
            setLongMethod = nbtTagCompoundClass.getMethod(setLongName, String.class, long.class);
            setDoubleMethod = nbtTagCompoundClass.getMethod(setDoubleName, String.class, double.class);

            setIntArrayMethod = nbtTagCompoundClass.getMethod(setIntArrayName, String.class, int[].class);

            try {
                setByteArrayMethod = nbtTagCompoundClass.getMethod(setByteArrayName, String.class, byte[].class);

                if (Version.isCurrentEqualOrHigher(Version.v1_13_R1))
                    setLongArrayMethod = nbtTagCompoundClass.getMethod(setLongArrayName, String.class, long[].class);
            } catch (final Throwable ex) {
                ex.printStackTrace();
            }

            setMethod = nbtTagCompoundClass.getMethod(setName, String.class, nbtBaseClass);

            // This will fail on 1.7.10 servers.
            try {
                addMethod = nbtTagListClass.getMethod(listAddMethod, int.class, nbtBaseClass);
            } catch (final Throwable e) {
            }
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * The NBT tag {@link Object} associated with this instance.
     */
    @Nullable
    final Object tag;

    /**
     * The {@link Object} associated with this instance.
     */
    @Nullable
    Object object;

    /**
     * The type of NBT associated with this instance.
     */
    @NotNull
    private final nmbtType type;

    /**
     * Represents the different types of NBT.
     */
    public enum nmbtType {
        /**
         * Represents the NBT on an {@link ItemStack}.
         */
        item,

        /**
         * Represents the NBT on a {@link Block}.
         */
        block,

        /**
         * Represents the NBT on an {@link Entity}.
         */
        entity,

        /**
         * Represents a custom NBT type.
         */
        custom
    }

    /**
     * Creates a new NBT object with the given compound.
     *
     * @param nbtTagCompound the compound to associate with this instance.
     */
    public CMINBT(@Nullable final Object nbtTagCompound) {
        this.tag = nbtTagCompound;
        this.type = nmbtType.custom;
    }

    /**
     * Creates a new NBT object from the given {@link ItemStack}.
     *
     * @param item the {@link ItemStack} to extract the NBT data from.
     */
    public CMINBT(@Nullable final ItemStack item) {
        this.object = item;
        this.tag = getNbt(item);
        this.type = nmbtType.item;
    }

    /**
     * Creates a new NBT object from the given {@link Block}.
     *
     * @param block the {@link Block} to extract the NBT data from.
     */
    public CMINBT(@Nullable final Block block) {
        this.tag = getNbt(block);
        this.object = block;
        this.type = nmbtType.block;
    }

    /**
     * Creates a new NBT object from the given {@link Entity}.
     *
     * @param entity the {@link Entity} to extract the NBT data from.
     */
    public CMINBT(@Nullable final Entity entity) {
        this.tag = getNbt(entity);
        this.object = entity;
        this.type = nmbtType.entity;
    }

    /**
     * Returns an {@link Integer} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the {@link Integer} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public Integer getInt(@NotNull final String path) {
        if (!this.hasNBT(path))
            return null;

        try {
            if (this.tag != null && path.contains(".")) {
                final List<String> keys = new ArrayList<>(Arrays.asList(path.split("\\.")));

                try {
                    Object nbtBase = getMethod.invoke(this.tag, keys.get(0));

                    for (int i = 1; i < keys.size(); i++)
                        if (i + 1 < keys.size())
                            nbtBase = getMethod.invoke(nbtBase, keys.get(i));
                        else {
                            if (nbtBase == null)
                                return (Integer) getIntMethod.invoke(this.tag, path);

                            return (Integer) getStringMethod.invoke(nbtBase, keys.get(i));
                        }
                } catch (final Throwable e) {
                }
            }

            return (Integer) getIntMethod.invoke(this.tag, path);
        } catch (final Exception e) {
        }

        return null;
    }

    /**
     * Returns a {@link Byte} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the {@link Byte} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public Byte getByte(@NotNull final String path) {
        if (!this.hasNBT(path))
            return null;

        try {
            return (Byte) getByteMethod.invoke(this.tag, path);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a {@link Long} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the {@link Long} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public Long getLong(@NotNull final String path) {
        if (!this.hasNBT(path))
            return null;

        try {
            return (Long) getLongMethod.invoke(this.tag, path);
        } catch (final Exception e) {
        }

        return null;
    }

    /**
     * Returns a {@link Boolean} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the {@link Boolean} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public Boolean getBoolean(@NotNull final String path) {
        if (!this.hasNBT(path))
            return null;

        try {
            return (Boolean) getBooleanMethod.invoke(this.tag, path);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a float from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the float from the NBT path, or {@code 0} if the path is not found.
     */
    public float getFloat(@NotNull final String path) {
        if (!this.hasNBT(path))
            return 0F;

        try {
            return (Float) getFloatMethod.invoke(this.tag, path);
        } catch (final Exception e) {
        }

        return 0F;
    }

    /**
     * Returns a {@link Short} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the {@link Short} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public Short getShort(@NotNull final String path) {
        if (this.tag == null)
            return null;

        try {
            return (Short) getShortMethod.invoke(this.tag, path);
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Returns a double from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the double from the NBT path, or {@code 0} if the path is not found.
     */
    public double getDouble(@NotNull final String path) {
        if (!this.hasNBT(path))
            return 0.0D;
        try {
            return (Double) getDoubleMethod.invoke(this.tag, path);
        } catch (final Exception e) {
        }
        return 0.0D;
    }

    /**
     * Returns a byte array from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the byte array from the NBT path, or an empty array if the path is not found.
     */
    public byte @NotNull [] getByteArray(@NotNull final String path) {
        if (!this.hasNBT(path))
            return new byte[0];

        try {
            return (byte[]) getByteArrayMethod.invoke(this.tag, path);
        } catch (final Exception e) {
        }

        return new byte[0];
    }

    /**
     * Returns an integer array from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the integer array from the NBT path, or an empty array if the path is not found.
     */
    public int @NotNull [] getIntArray(@NotNull final String path) {
        if (!this.hasNBT(path))
            return new int[0];
        try {
            return (int[]) getIntArrayMethod.invoke(this.tag, path);
        } catch (final Exception e) {
        }
        return new int[0];
    }

    /**
     * Returns a long array from the specified NBT path.
     * <p>
     * This method is for servers on 1.13 and newer.
     *
     * @param path the path to the NBT data.
     * @return the long array from the NBT path, or an empty array if the path is not found.
     */
    public long @NotNull [] getLongArray(@NotNull final String path) {
        if (!this.hasNBT(path) || !Version.isCurrentEqualOrHigher(Version.v1_13_R1))
            return new long[0];

        try {
            return (long[]) getLongArrayMethod.invoke(this.tag, path);
        } catch (final Exception e) {
        }

        return new long[0];
    }

    /**
     * Returns a {@link String} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the {@link String} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public String getString(@NotNull final String path) {
        if (this.tag == null || !this.hasNBT(path))
            return null;

        try {
            if (path.contains(".")) {
                final List<String> keys = new ArrayList<>(Arrays.asList(path.split("\\.")));

                try {
                    Object nbtBase = getMethod.invoke(this.tag, keys.get(0));

                    for (int i = 1; i < keys.size(); i++)
                        if (i + 1 < keys.size())
                            nbtBase = getMethod.invoke(nbtBase, keys.get(i));
                        else {
                            if (nbtBase == null)
                                return (String) getStringMethod.invoke(this.tag, path);

                            return (String) getStringMethod.invoke(nbtBase, keys.get(i));
                        }
                } catch (final Throwable e) {
                }
            }

            return (String) getStringMethod.invoke(this.tag, path);
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Returns a {@link String} {@link List} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the {@link String} {@link List} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public List<@NotNull String> getList(@NotNull final String path) {
        return this.getList(path, -1);
    }

    /**
     * Returns a {@link String} {@link List} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @param type the type of the list elements. Use a negative value for automatic detection.
     * @return the {@link String} {@link List} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public List<@NotNull String> getList(String path, final int type) {
        if (this.tag == null || !this.hasNBT(path))
            return null;

        final List<String> list = new ArrayList<>();

        try {
            Object tag = this.tag;

            if (path.contains(".")) {
                final List<String> keys = new ArrayList<>(Arrays.asList(path.split("\\.")));

                try {
                    Object nbtBase = getMethod.invoke(tag, keys.get(0));

                    for (int i = 1; i < keys.size(); i++)
                        if (i + 1 < keys.size())
                            nbtBase = getMethod.invoke(nbtBase, keys.get(i));
                        else {
                            tag = nbtBase;

                            path = keys.get(i);
                            break;
                        }
                } catch (final Throwable e) {
                }
            }

            if (tag == null)
                return list;

            Object getListResult = getListMethod.invoke(tag, path, type < 0 ? 8 : type);
            int size = (int) getListResult.getClass().getMethod("size").invoke(getListResult);

            if (size == 0 && type < 0) {
                getListResult = getListMethod.invoke(tag, path, 10);
                size = (int) getListResult.getClass().getMethod("size").invoke(getListResult);
            }

            Method method = getListResult.getClass().getMethod(listGetName, int.class);

            if (Version.isCurrentEqualOrLower(Version.v1_12_R1)) {
                method = getListResult.getClass().getMethod("getString", int.class);

                for (int i = 0; i < size; i++) {
                    final Object result = method.invoke(getListResult, i);

                    final String line = (String) result;
                    list.add(line);
                }
            } else
                for (int i = 0; i < size; i++)
                    list.add(toStringMethod.invoke(method.invoke(getListResult, i)).toString());

            return list;
        } catch (final Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Returns an {@link Object} {@link List} from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @param type the type of the list elements. Use a negative value for automatic detection.
     * @return the {@link Object} {@link List} from the NBT path, or {@code null} if the path is not found.
     */
    @Nullable
    public Object getObjectList(@NotNull String path, final int type) {
        if (this.tag == null || !this.hasNBT(path))
            return null;

        try {
            Object tag = this.tag;

            if (path.contains(".")) {
                final List<String> keys = new ArrayList<>(Arrays.asList(path.split("\\.")));

                try {
                    Object nbtBase = getMethod.invoke(tag, keys.get(0));

                    for (int i = 1; i < keys.size(); i++)
                        if (i + 1 < keys.size())
                            nbtBase = getMethod.invoke(nbtBase, keys.get(i));
                        else {
                            tag = nbtBase;

                            path = keys.get(i);
                            break;
                        }
                } catch (final Throwable e) {
                }
            }

            if (tag == null)
                return null;

            Object getListResult = getListMethod.invoke(tag, path, type < 0 ? 8 : type);
            int size = (int) getListResult.getClass().getMethod("size").invoke(getListResult);

            if (size == 0 && type < 0) {
                getListResult = getListMethod.invoke(tag, path, 10);
                size = (int) getListResult.getClass().getMethod("size").invoke(getListResult);
            }

            return getListResult;
        } catch (final Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Sets a {@link Boolean} value at the specified NBT path.
     *
     * @param path  the path to the NBT data.
     * @param value the {@link Boolean} value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setBoolean(@NotNull final String path, @Nullable final Boolean value) {
        if (this.type == nmbtType.item)
            try {
                if (value == null)
                    removeMethod.invoke(this.tag, path);
                else
                    setBooleanMethod.invoke(this.tag, path, value);

                return setTag((ItemStack) this.object, this.tag);
            } catch (final Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();

                return this.object;
            }

        return this.object;
    }

    /**
     * Sets a {@link Byte} value at the specified NBT path.
     *
     * @param path  the path to the NBT data.
     * @param value the {@link Byte} value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setByte(@NotNull final String path, @Nullable final Byte value) {
        if (this.type == nmbtType.item)
            try {
                if (value == null)
                    removeMethod.invoke(this.tag, path);
                else
                    setByteMethod.invoke(this.tag, path, value);

                return setTag((ItemStack) this.object, this.tag);
            } catch (final Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();

                return this.object;
            }

        return this.object;
    }

    /**
     * Sets a {@link Short} value at the specified NBT path.
     *
     * @param path  the path to the NBT data.
     * @param value the {@link Short} value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setShort(@NotNull final String path, @Nullable final Short value) {
        if (this.type == nmbtType.item)
            try {
                if (value == null)
                    removeMethod.invoke(this.tag, path);
                else
                    setShortMethod.invoke(this.tag, path, value);

                return setTag((ItemStack) this.object, this.tag);
            } catch (final Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                    e.printStackTrace();

                return this.object;
            }

        return this.object;
    }

    /**
     * Sets a {@link String} value at the specified NBT path.
     *
     * @param path  the path to the NBT data.
     * @param value the {@link String} value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setString(@NotNull final String path, @Nullable final String value) {
        try {
            Object nbtBase = this.tag;
            String realPath = path;

            if (this.tag != null && path.contains(".")) {
                final List<String> keys = new ArrayList<>(Arrays.asList(path.split("\\.")));

                try {
                    nbtBase = getMethod.invoke(this.tag, keys.get(0));

                    for (int i = 1; i < keys.size(); i++)
                        if (i + 1 < keys.size())
                            nbtBase = getMethod.invoke(nbtBase, keys.get(i));
                        else {
                            realPath = keys.get(i);

                            break;
                        }
                } catch (final Throwable e) {
                }
            }

            if (value == null)
                removeMethod.invoke(nbtBase, realPath);
            else
                setStringMethod.invoke(nbtBase, realPath, value);
        } catch (final Throwable e) {
            //if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
            //	e.printStackTrace();

            return this.object;
        }

        switch (this.type) {
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
            default:
                break;
        }

        return this.object;
    }

    /**
     * Sets an {@link Integer} value at the specified NBT path.
     *
     * @param path  the path to the NBT data.
     * @param value the {@link Integer} value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setInt(@NotNull final String path, @Nullable final Integer value) {
        try {
            if (value == null)
                removeMethod.invoke(this.tag, path);
            else
                setIntMethod.invoke(this.tag, path, value);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();

            return this.object;
        }

        switch (this.type) {
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
            default:
                break;
        }

        return this.object;
    }

    /**
     * Sets an integer array value at the specified NBT path.
     *
     * @param path  the path to the NBT path.
     * @param value the integer array value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setIntArray(@NotNull final String path, final int @Nullable [] value) {
        try {
            if (value == null)
                removeMethod.invoke(this.tag, path);
            else
                setIntArrayMethod.invoke(this.tag, path, value);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();

            return this.object;
        }

        switch (this.type) {
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
            default:
                break;
        }

        return this.object;
    }

    /**
     * Sets a byte array value at the specified NBT path.
     *
     * @param path  the path to the NBT path.
     * @param value the byte array value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setByteArray(@NotNull final String path, final byte @Nullable [] value) {
        try {
            if (value == null)
                removeMethod.invoke(this.tag, path);
            else
                setByteArrayMethod.invoke(this.tag, path, value);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();

            return this.object;
        }

        switch (this.type) {
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
            default:
                break;
        }

        return this.object;
    }

    /**
     * Sets a long array value at the specified NBT path.
     *
     * @param path  the path to the NBT path.
     * @param value the long array value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setLongArray(@NotNull final String path, final long @Nullable [] value) {
        try {
            if (value == null)
                removeMethod.invoke(this.tag, path);
            else
                setLongArrayMethod.invoke(this.tag, path, value);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();

            return this.object;
        }

        switch (this.type) {
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
            default:
                break;
        }

        return this.object;
    }

    /**
     * Sets a {@link Long} value at the specified NBT path.
     *
     * @param path  the path to the NBT data.
     * @param value the {@link Long} value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setLong(@NotNull final String path, @Nullable final Long value) {
        switch (this.type) {
            case custom:
                try {
                    if (value == null)
                        removeMethod.invoke(this.tag, path, null);
                    else
                        setLongMethod.invoke(this.tag, path, value);

                    return this.tag;
                } catch (final Throwable e) {
                    if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                        e.printStackTrace();

                    return this.object;
                }
            case item:
                try {
                    if (value == null)
                        removeMethod.invoke(this.tag, path, value);
                    else
                        setLongMethod.invoke(this.tag, path, value);

                    return setTag((ItemStack) this.object, this.tag);
                } catch (final Throwable e) {
                    if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                        e.printStackTrace();

                    return this.object;
                }
            default:
                break;
        }

        return this.object;
    }

    /**
     * Sets a {@link Double} value at the given NBT path.
     *
     * @param path  the path to the NBT data.
     * @param value the {@link Double} value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object setDouble(@NotNull final String path, @Nullable final Double value) {
        try {
            if (value == null)
                removeMethod.invoke(this.tag, path, null);
            else
                setDoubleMethod.invoke(this.tag, path, value);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();

            return this.object;
        }

        switch (this.type) {
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
            default:
                break;
        }

        return this.object;
    }

    /**
     * Removes the NBT value at the specified path.
     *
     * @param path the path to the NBT data.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object remove(@NotNull final String path) {
        try {
            removeMethod.invoke(this.tag, path);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();

            return this.object;
        }

        switch (this.type) {
            case entity:
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
            default:
                break;
        }

        return this.object;
    }

    /**
     * Sets the NBT value at the specified path.
     *
     * @param path    the path to the NBT value.
     * @param nbtBase the NBT value to be set.
     * @return the updated {@link Object}.
     */
    @Nullable
    public Object set(@NotNull final String path, @NotNull final Object nbtBase) {
        try {
            setMethod.invoke(this.tag, path, nbtBase);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();
        }

        switch (this.type) {
            case block:
            case entity:
                break;
            case custom:
                return this.tag;
            case item:
                return setTag((ItemStack) this.object, this.tag);
        }

        return this.object;
    }

	/*@Nullable
	@Override
	public ItemStack setNBTList(@NotNull final ItemStack item, @NotNull final String name, @Nullable final List<@NotNull String> list) {
		final net.minecraft.server.v1_16_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);

		if (nmsItemStack == null)
			return null;

		try {
			NBTTagCompound tag = nmsItemStack.getTag();

			if (tag == null)
				tag = new NBTTagCompound();

			final NBTTagList tagList = new NBTTagList();

			if (list != null)
				for (final String one : list)
					tagList.add(NBTTagString.a(one));
			if (list == null)
				tag.remove(name);
			else
				tag.set(name, tagList);

			nmsItemStack.setTag(tag);
			return CraftItemStack.asBukkitCopy(nmsItemStack);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Nullable
	public Object setList(@NotNull final String path, @NotNull final List<@NotNull String> list) {
		if (this.type == nmbtType.item)
			try {
				final Method method = this.tag.getClass().getMethod("setLong", String.class, long.class);

				method.invoke(this.tag, path, value);
				return CMI.getInstance().getReflectionManager().setTag((ItemStack) this.object, this.tag);
			} catch (final Throwable e) {
				if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
					e.printStackTrace();

				return this.object;
			}

		return this.object;
	}*/

    /**
     * Checks if this object has NBT data.
     *
     * @return {@code true} if this object has NBT data, {@code false} otherwise.
     */
    public boolean hasNBT() {
        return this.tag != null;
    }

    /**
     * Checks if there is NBT data at the given key.
     *
     * @param key the key to check.
     * @return {@code true} if the key has NBT data, {@code false} otherwise.
     */
    public boolean hasNBT(@NotNull final String key) {
        if (this.tag != null && key.contains(".")) {
            final List<String> keys = new ArrayList<>(Arrays.asList(key.split("\\.")));

            try {
                Object nbtBase = getMethod.invoke(this.tag, keys.get(0));

                for (int i = 1; i < keys.size(); i++)
                    if (i + 1 < keys.size())
                        nbtBase = getMethod.invoke(nbtBase, keys.get(i));
                    else {
                        if (nbtBase == null)
                            return (Boolean) this.tag.getClass().getMethod(hasKeyName, String.class).invoke(this.tag, key);

                        return (Boolean) nbtBase.getClass().getMethod(hasKeyName, String.class).invoke(nbtBase, keys.get(i));
                    }
            } catch (final Throwable e) {
                e.printStackTrace();
            }

            return false;
        }

        try {
            return this.tag != null && (Boolean) this.tag.getClass().getMethod(hasKeyName, String.class).invoke(this.tag, key);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Returns the NBT tag associated with this instance.
     *
     * @return the NBT tag associated with this instance.
     */
    public Object getNbt() {
        return this.tag;
    }

    /**
     * Returns the keys representing this NBT tag compound.
     *
     * @return a {@link Set} of keys representing this NBT tag compound.
     */
    @NotNull
    public Set<@NotNull String> getKeys() {
        Set<String> keys = new HashSet<>();

        if (!this.hasNBT())
            return keys;

        try {
            keys = (Set<String>) this.getNbt().getClass().getMethod(getKeysName).invoke(this.getNbt());
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return keys;
    }

    /**
     * Returns the value at the specified path.
     *
     * @param path the path to the NBT data.
     * @return the value at the path, or {@code null} if the path is not found.
     */
    @Nullable
    public Object get(@NotNull final String path) {
        try {
            return getMethod.invoke(this.getNbt(), path);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a compound from the specified NBT path.
     *
     * @param path the path to the NBT data.
     * @return the compound from the path, or {@code null} if the path is not found.
     */
    @Nullable
    public Object getCompound(@NotNull final String path) {
        try {
            return getCompoundMethod.invoke(this.getNbt(), path);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the type ID of this NBT tag.
     *
     * @return the type ID of this NBT tag, or {@code 0} if there isn't one.
     */
    public byte getTypeId() {
        try {
            return (byte) this.getNbt().getClass().getMethod(getTypeIdName).invoke(this.getNbt());
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Sets the NBT tag for the given {@link ItemStack}.
     *
     * @param item the {@link ItemStack} to set the NBT tag for.
     * @param tag  the NBT tag {@link Object} to set.
     * @return the updated {@link ItemStack}.
     */
    @Nullable
    public static ItemStack setTag(@Nullable final ItemStack item, @Nullable final Object tag) {
        try {
            final Object nmsItemStack = asNMSCopy(item);

            if (nmsItemStack == null)
                return null;

            nmsItemStack.getClass().getMethod(setTagName, nbtTagCompoundClass).invoke(nmsItemStack, tag);
            return (ItemStack) asBukkitCopy(nmsItemStack);
        } catch (final Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_7_R4))
                e.printStackTrace();

            return item;
        }
    }

    /**
     * Returns the NBT tag for the given {@link Entity}.
     *
     * @param entity the {@link Entity} to retrieve the NBT tag of.
     * @return the NBT tag {@link Object} associated with the {@link Entity}, or {@code null} if there is none.
     */
    @Nullable
    public static Object getNbt(@Nullable final Entity entity) {
        if (entity == null)
            return null;

        try {
            Object tag = nbtTagCompoundClass.getDeclaredConstructor().newInstance();
            final Object nmsEntity = getEntityHandle(entity);
            final Method tagMethod = nmsEntity.getClass().getMethod(saveName, nbtTagCompoundClass);

            tag = tagMethod.invoke(nmsEntity, tag);
            return tag;
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Returns the NBT tag for the given {@link ItemStack}.
     *
     * @param item the {@link ItemStack} to retrieve the NBT tag of.
     * @return the NBT tag {@link Object} associated with the {@link ItemStack}, or {@code null} if there is none.
     */
    @Nullable
    public static Object getNbt(@Nullable final ItemStack item) {
        if (item == null)
            return null;

        try {
            final Object nmsItemStack = asNMSCopy(item);

            if (nmsItemStack == null)
                return null;

            final Method tagMethod = nmsItemStack.getClass().getMethod(getTagName);
            Object tag = tagMethod.invoke(nmsItemStack);

            if (tag == null)
                tag = nbtTagCompoundClass.getDeclaredConstructor().newInstance();

            return tag;
        } catch (final Exception e) {
            //e.printStackTrace();

            return null;
        }
    }

    /**
     * Returns the NBT tag for the given {@link Block}.
     *
     * @param block the {@link Block} to retrieve the NBT tag of.
     * @return the NBT tag {@link Object} associated with the {@link Block}, or {@code null} if there is none.
     */
    @Nullable
    public static Object getNbt(@Nullable final Block block) {
        if (block == null)
            return false;

        try {
            final Object tile = CMILib.getInstance().getReflectionManager().getTileEntityAt(block.getLocation());

            // TileEntity -> getUpdateTag
            final String getUpdateTagName;

            switch (Version.getCurrent()) {
                case v1_7_R1:
                case v1_7_R2:
                case v1_7_R3:
                case v1_7_R4:
                case v1_8_R1:
                case v1_8_R3:
                case v1_8_R2:
                case v1_9_R1:
                case v1_9_R2:
                case v1_10_R1:
                case v1_11_R1:
                case v1_12_R1:
                    getUpdateTagName = "d";

                    break;
                case v1_13_R1:
                case v1_13_R2:
                case v1_18_R2:
                    getUpdateTagName = "aa_";

                    break;
                case v1_17_R1:
                case v1_18_R1:
                    getUpdateTagName = "Z_";

                    break;
                case v1_19_R1:
                    getUpdateTagName = Version.isCurrentSubEqual(0) ? "ab_" : "aa_";

                    break;
                case v1_19_R2:
                    getUpdateTagName = "ad_";

                    break;
                case v1_19_R3:
                    getUpdateTagName = "aq_";

                    break;
                case v1_20_R1:
                    getUpdateTagName = "ao_";

                    break;
                case v1_14_R1:
                case v1_15_R1:
                default:
                    getUpdateTagName = "b";

                    break;
            }

            if (tile == null)
                return null;

            final Method tagMethod = tile.getClass().getMethod(getUpdateTagName);
            return tagMethod.invoke(tile);
        } catch (final Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Returns a Bukkit {@link Class} corresponding to the given NMS class name.
     * <p>
     * The current version is automatically included.
     *
     * @param nmsClassString the NMS class name.
     * @return the Bukkit {@link Class} corresponding to the NMS class, or {@code null} if the class is not found.
     */
    @Nullable
    private static Class<?> getBukkitClass(@NotNull final String nmsClassString) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + Version.getCurrent().toString() + "." + nmsClassString);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a Minecraft {@link Class} corresponding to the given NMS class name.
     * <p>
     * The current version is automatically included.
     *
     * @param nmsClassString the NMS class name.
     * @return the Bukkit {@link Class} corresponding to the NMS class, or {@code null} if the class is not found.
     */
    @Nullable
    private static Class<?> getMinecraftClass(@NotNull final String nmsClassString) {
        try {
            return Class.forName("net.minecraft.server." + Version.getCurrent().toString() + "." + nmsClassString);
        } catch (final ClassNotFoundException e) {
        }
        
        return null;
    }

    /**
     * Converts the given Bukkit {@link ItemStack} into its corresponding NMS ItemStack representation.
     *
     * @param item the Bukkit {@link ItemStack} to convert.
     * @return the NMS representation of the Bukkit {@link ItemStack}, or {@code null}.
     */
    @Nullable
    public static Object asNMSCopy(@Nullable final ItemStack item) {
        try {
            final Method method = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);

            return method.invoke(craftItemStackClass, item);
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Converts the given NMS ItemStack into its corresponding Bukkit {@link ItemStack} representation.
     *
     * @param item the NMS ItemStack to convert.
     * @return the Bukkit {@link ItemStack} representation of the NMS ItemStack, or {@code null}.
     */
    @Nullable
    public static Object asBukkitCopy(@Nullable final Object item) {
        try {
            final Method method = craftItemStackClass.getMethod("asBukkitCopy", itemStackClass);

            return method.invoke(craftItemStackClass, item);
        } catch (final Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Returns the NMS handle {@link Object} for the given Bukkit {@link Entity}.
     *
     * @param entity the Bukkit {@link Entity} to retrieve the handle of.
     * @return the NMS handle {@link Object} for the Bukkit {@link Entity}, or {@code null}.
     */
    @Nullable
    public static Object getEntityHandle(@NotNull final Entity entity) {
        Object handle = null;

        try {
            handle = craftEntityClass.cast(entity).getClass().getMethod("getHandle").invoke(craftEntityClass.cast(entity));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return handle;
    }

    /**
     * Checks if two {@link ItemStack}s have similar NBT tags.
     *
     * @param firstItemStack  the first {@link ItemStack} to compare.
     * @param secondItemStack the second {@link ItemStack} to compare.
     * @return {@code true} if the two {@link ItemStack}s have similar NBT tags, {@code false} otherwise.
     */
    public static boolean isNBTSimilar(@Nullable final ItemStack firstItemStack, @Nullable final ItemStack secondItemStack) {
        try {
            final Object firstNMSItemStack = asNMSCopy(firstItemStack);
            final Object secondNMSItemStack = asNMSCopy(secondItemStack);

            final Method method;

            // ItemStack -> tagMatches.
            if (Version.isCurrentEqualOrLower(Version.v1_17_R1))
                method = firstNMSItemStack.getClass().getMethod("equals", itemStackClass, itemStackClass);
            else
                method = firstNMSItemStack.getClass().getMethod("a", itemStackClass, itemStackClass);

            return (boolean) method.invoke(itemStackClass, firstNMSItemStack, secondNMSItemStack);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Hides or shows a specific flag on an {@link ItemStack}.
     *
     * @param item  the {@link ItemStack} to modify.
     * @param state the state to set the flag to ({@code 0} to show, {@code 1} to hide).
     * @return the updated {@link ItemStack}.
     * @deprecated badly named, use {@link #hideFlag(ItemStack, int)} instead.
     */
    @Deprecated
    @Nullable
    public static ItemStack HideFlag(@NotNull final ItemStack item, final int state) {
        return hideFlag(item, state);
    }

    /**
     * Hides or shows a specific flag on an {@link ItemStack}.
     *
     * @param item  the {@link ItemStack} to modify.
     * @param state the state to set the flag to ({@code 0} to show, {@code 1} to hide).
     * @return the updated {@link ItemStack}.
     */
    @Nullable
    public static ItemStack hideFlag(@NotNull final ItemStack item, final int state) {
        final Object nmsItemStack = asNMSCopy(item);

        try {
            final Method tagMethod = nmsItemStack.getClass().getMethod(getTagName);
            Object tag = tagMethod.invoke(nmsItemStack);

            if (tag == null)
                tag = nbtTagCompoundClass.newInstance();

            setIntMethod.invoke(tag, "HideFlags", state);
            final Method method = nmsItemStack.getClass().getMethod(setTagName, nbtTagCompoundClass);

            method.invoke(nmsItemStack, tag);
            return (ItemStack) asBukkitCopy(nmsItemStack);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    /**
     * Modifies an {@link ItemStack} based on the provided arguments using Mojangson syntax.
     *
     * @param item      the {@link ItemStack} to modify.
     * @param arguments the Mojangson syntax {@link String} representing the modifications.
     * @return the updated {@link ItemStack}.
     */
    @Nullable
    public static ItemStack modifyItemStack(@Nullable final ItemStack item, @Nullable final String arguments) {
        final Object nmsItemStack = asNMSCopy(item);

        try {
            final Object result = mojangsonParserClass.getMethod(parseName, String.class).invoke(mojangsonParserClass, arguments);
            nmsItemStack.getClass().getMethod(setTagName, nbtTagCompoundClass).invoke(nmsItemStack, result);

            final Object meta = craftItemStackClass.getMethod("getItemMeta", itemStackClass).invoke(craftItemStackClass, nmsItemStack);
            item.setItemMeta((ItemMeta) meta);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return item;
    }

    /**
     * Converts an {@link ItemStack} into its JSON representation.
     *
     * @param item the {@link ItemStack} to convert.
     * @return the JSON {@link String} representing the {@link ItemStack}.
     */
    @Nullable
    public static String toJson(@Nullable final ItemStack item) {
        if (item == null)
            return null;

        final Object nmsStack = asNMSCopy(item);

        try {
            final Method method = itemStackClass.getMethod(itemSaveName, nbtTagCompoundClass);
            
            final Object result = method.invoke(nmsStack, nbtTagCompoundClass.newInstance());
            return result.toString();
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the {@link EntityType} of the given {@link ItemStack}.
     * <p>
     * This method is for servers on 1.13 or older.
     *
     * @param item the {@link ItemStack} to check.
     * @return the {@link EntityType} of the {@link ItemStack}, or {@code null} if the {@link ItemStack} is not a spawn
     * egg.
     */
    @Nullable
    public static EntityType getEggType(@NotNull final ItemStack item) {
        if (!CMIMaterial.isMonsterEgg(item.getType()))
            return null;

        if (Version.isCurrentEqual(Version.v1_12_R1))
            try {
                if (Version.isCurrentEqualOrLower(Version.v1_11_R1)) {
                    final CMIEntityType cmiType = CMIEntityType.getById(item.getData().getData());

                    if (cmiType != null)
                        return cmiType.getType();
                }

                final Object tag = getNbt(item);
                final Object base = getCompoundMethod.invoke(tag, "EntityTag");

                final String type = (String) getStringMethod.invoke(base, "id");
                return EntityType.fromName(type.replace("minecraft:", "").toUpperCase());
            } catch (final Exception e) {
                return null;
            }

        final CMIEntityType type = CMIEntityType.getByName(item.getType().toString().replace("_SPAWN_EGG", ""));
        return type == null ? null : type.getType();
    }

    /**
     * Sets the {@link EntityType} of the given {@link ItemStack}.
     * <p>
     * This method is for servers on 1.13 and older.
     *
     * @param item the {@link ItemStack} to modify.
     * @param type the {@link EntityType} to set.
     * @return the updated {@link ItemStack}, or {@code null} if the {@link ItemStack} is not a spawn egg.
     */
    @Nullable
    public static ItemStack setEggType(@NotNull final ItemStack item, @NotNull final EntityType type) {
        if (!item.getType().toString().contains("_EGG"))
            return null;
        
        try {
            final Object finalTag = getNbt(item);
            Object tag = getCompoundMethod.invoke(finalTag, "EntityTag");

            if (tag == null)
                tag = nbtTagCompoundClass.newInstance();

            final CMIEntityType cmiType = CMIEntityType.getByType(type);

            if (cmiType == null)
                return item;

            setStringMethod.invoke(tag, "id", cmiType.getName());
            setMethod.invoke(finalTag, "EntityTag", tag);

            setTag(item, finalTag);
            return (ItemStack) asBukkitCopy(asNMSCopy(item));
        } catch (final Exception e) {
            //e.printStackTrace();

            return null;
        }
    }

    /**
     * Creates a new NBT tag list with the given type.
     *
     * @param type the type of the list.
     * @return the new NBT tag list, or {@code null}.
     */
    @Nullable
    public static Object newNBTTagList(final int type) {
        try {
            return nbtTagListClass.newInstance();
        } catch (final Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds an element to the specified NBT tag list.
     *
     * @param list the NBT tag list to add the element to.
     * @param size the size of the NBT tag list.
     * @param data the data to add to the NBT tag list.
     */
    public static void addToList(@NotNull final Object list, final int size, @NotNull final Object data) {
        if (addMethod == null)
            return;

        try {
            addMethod.invoke(list, size, data);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the type of NBT associated with this instance.
     *
     * @return the type of NBT associated with this instance.
     */
    @NotNull
    public nmbtType getType() {
        return this.type;
    }
}
