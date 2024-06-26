package net.Zrips.CMILib.PersistentData;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.Zrips.CMILib.CMILib;

public class CMIBlockPersistentDataContainer extends CMIPersistentDataContainer {

    private final Chunk chunk;
    private final NamespacedKey key;

    public CMIBlockPersistentDataContainer(Block block) {
        this(block.getLocation());
    }

    public CMIBlockPersistentDataContainer(Location loc) {
        this.chunk = loc == null ? null : loc.getChunk();
        this.key = loc == null ? null : getNamespacedKey(loc);
        this.persistentDataContainer = loc == null ? null : getDataContainer();
    }

    public boolean hasBlockData() {
        return this.chunk.getPersistentDataContainer().has(key, PersistentDataType.TAG_CONTAINER);
    }

    private PersistentDataContainer getDataContainer() {
        if (!chunk.isLoaded())
            return null;
        
        PersistentDataContainer chunkData = chunk.getPersistentDataContainer();
        PersistentDataContainer blockPDC = chunkData.get(key, PersistentDataType.TAG_CONTAINER);
        if (blockPDC != null)
            return blockPDC;

        blockPDC = chunkData.getAdapterContext().newPersistentDataContainer();
        chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, blockPDC);
        return blockPDC;
    }

    private static NamespacedKey getNamespacedKey(Location loc) {
        return new NamespacedKey(CMILib.getInstance(), getKey(loc));
    }

    private static String getKey(Location loc) {
        return (loc.getBlockX() & 0x000F) + "." + loc.getBlockY() + "." + (loc.getBlockZ() & 0x000F);
    }

    @Override
    public void save() {

        if (persistentDataContainer == null)
            return;

        if (persistentDataContainer.isEmpty())
            chunk.getPersistentDataContainer().remove(this.key);
        else {
            chunk.getPersistentDataContainer().set(this.key, PersistentDataType.TAG_CONTAINER, persistentDataContainer);
        }
    }
}
