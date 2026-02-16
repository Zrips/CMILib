package net.Zrips.CMILib.NBT;

import java.util.List;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import net.Zrips.CMILib.NBT.CMINBT.nmbtType;

public interface CMINBTInterface {
    public Integer getInt(String path);

    public Byte getByte(String path);

    public Long getLong(String path);

    public Boolean getBoolean(String path);

    public float getFloat(String path);

    public Short getShort(String path);

    public double getDouble(String path);

    public byte[] getByteArray(String path);

    public int[] getIntArray(String path);

    public long[] getLongArray(String path);

    public String getString(String path);

    public List<String> getList(String path);

    public List<String> getList(String path, int type);

    public Object getObjectList(String path, int type);

    public Object setBoolean(String path, Boolean value);

    public Object setByte(String path, Byte value);

    public Object setShort(String path, Short value);

    public Object setStringList(String path, List<String> value);

    public Object setString(String path, String value);

    public Object setInt(String path, Integer value);

    public Object setIntArray(String path, int[] value);

    public Object setByteArray(String path, byte[] value);

    public Object setLongArray(String path, long[] value);

    public Object setLong(String path, Long value);

    public Object setDouble(String path, Double value);

    public Object remove(String path);

    public Object set(String path, Object nbtbase);

    public boolean hasNBT();

    public boolean hasNBT(String key);

    public Object getNbt();

    public Set<String> getKeys();

    public Object get(String path);

    public Object getCompound(String path);

    public byte getTypeId();
    
    public nmbtType getType();

}
