package net.Zrips.CMILib.PersistentData;

import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.Zrips.CMI.CMI;

public class CMIBlockPersistentDataContainer extends CMIPersistentDataContainer {

    private final Chunk chunk;
    private final Block block;
    private final NamespacedKey key;

    public CMIBlockPersistentDataContainer(Block block) {
        this.block = block;
        this.chunk = block == null ? null : block.getChunk();
        this.key = block == null ? null : getNamespacedKey(block);
        this.persistentDataContainer = block == null ? null : getDataContainer();
    }

    public boolean hasBlockData() {
        return block.getChunk().getPersistentDataContainer().has(getNamespacedKey(block), PersistentDataType.TAG_CONTAINER);
    }

    private PersistentDataContainer getDataContainer() {
        PersistentDataContainer chunkData = chunk.getPersistentDataContainer();
        PersistentDataContainer blockPDC = chunkData.get(key, PersistentDataType.TAG_CONTAINER);
        if (blockPDC != null)
            return blockPDC;

        blockPDC = chunkData.getAdapterContext().newPersistentDataContainer();
        chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, blockPDC);
        return blockPDC;
    }

    private static NamespacedKey getNamespacedKey(Block block) {
        return new NamespacedKey(CMI.getInstance(), getKey(block));
    }

    private static String getKey(Block block) {
        return (block.getX() & 0x000F) + "." + block.getY() + "." + (block.getZ() & 0x000F);
    }

    @Override
    void save() {
        if (persistentDataContainer == null)
            return;
        
        if (persistentDataContainer.isEmpty())
            chunk.getPersistentDataContainer().remove(this.key);
        else
            chunk.getPersistentDataContainer().set(this.key, PersistentDataType.TAG_CONTAINER, persistentDataContainer);
    }
}
