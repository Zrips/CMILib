package net.Zrips.CMILib.PersistentData;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.Zrips.CMILib.CMILib;

public class CMIChunkPersistentDataContainer extends CMIPersistentDataContainer {

    private final Chunk chunk;
    private final NamespacedKey key;

    public CMIChunkPersistentDataContainer(String key, Chunk chunk) {
        this.chunk = chunk;
        this.key = getNamespacedKey(key);
        this.persistentDataContainer = chunk == null ? null : getDataContainer();
    }

    public boolean hasData() {
        return chunk.getPersistentDataContainer().has(key, PersistentDataType.TAG_CONTAINER);
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

    private static NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(CMILib.getInstance(), key);
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
