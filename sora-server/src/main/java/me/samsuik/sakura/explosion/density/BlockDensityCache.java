package me.samsuik.sakura.explosion.density;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * This is a replacement for papers explosion density cache to be more lenient and efficient.
 */
public final class BlockDensityCache {
    public static final float UNKNOWN_DENSITY = -1.0f;

    private final Int2ObjectOpenHashMap<DensityData> densityDataMap = new Int2ObjectOpenHashMap<>();
    private DensityData data;
    private int key;
    private boolean knownSource;

    public float getDensity(Vec3 explosion, Entity entity) {
        int key = getKey(explosion, entity);
        DensityData data = this.densityDataMap.get(key);

        if (data != null && data.hasPosition(explosion, entity.getBoundingBox())) {
            return data.density();
        } else {
            this.knownSource = data != null && data.complete() && data.isExplosionPosition(explosion);
            this.data = data;
            this.key = key;
            return UNKNOWN_DENSITY;
        }
    }

    public float getKnownDensity(Vec3 point) {
        if (this.knownSource && this.data.isKnownPosition(point)) {
            return this.data.density();
        } else {
            return UNKNOWN_DENSITY;
        }
    }

    public void putDensity(Vec3 explosion, Entity entity, float density) {
        if (this.data == null || !this.data.complete()) {
            this.densityDataMap.put(this.key, new DensityData(explosion, entity, density));
        } else if (this.data.density() == density) {
            this.data.expand(explosion, entity);
        }
    }

    public void invalidate() {
        this.densityDataMap.clear();
    }

    private static int getKey(Vec3 explosion, Entity entity) {
        int key        = Mth.floor(explosion.x());
        key = 31 * key + Mth.floor(explosion.y());
        key = 31 * key + Mth.floor(explosion.z());
        key = 31 * key + entity.getBlockX();
        key = 31 * key + entity.getBlockY();
        key = 31 * key + entity.getBlockZ();
        return key;
    }
}
