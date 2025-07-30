package me.samsuik.sakura.utils.collections;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.minecraft.server.level.ChunkMap;

public final class TrackedEntityChunkMap extends Int2ObjectOpenHashMap<ChunkMap.TrackedEntity> {
    private final ObjectArrayList<ChunkMap.TrackedEntity> entityList = new UnorderedIndexedList<>();

    @Override
    public ChunkMap.TrackedEntity put(int k, ChunkMap.TrackedEntity trackedEntity) {
        ChunkMap.TrackedEntity tracked = super.put(k, trackedEntity);
        if (tracked != null) {
            this.entityList.remove(trackedEntity);
        }
        this.entityList.add(trackedEntity);
        return tracked;
    }

    @Override
    public ChunkMap.TrackedEntity remove(int k) {
        ChunkMap.TrackedEntity tracked = super.remove(k);
        this.entityList.remove(tracked);
        return tracked;
    }

    @Override
    public ObjectCollection<ChunkMap.TrackedEntity> values() {
        return this.entityList;
    }
}
