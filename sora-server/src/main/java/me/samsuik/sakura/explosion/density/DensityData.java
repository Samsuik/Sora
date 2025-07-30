package me.samsuik.sakura.explosion.density;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class DensityData {
    private AABB source;
    private AABB known;
    private AABB entity;
    private final float density;
    private final boolean complete;

    public DensityData(Vec3 explosion, Entity entity, float density) {
        this.source = new AABB(explosion, explosion);
        this.known = new AABB(entity.position(), entity.position());
        this.entity = entity.getBoundingBox();
        this.density = density;
        this.complete = Math.abs(density - 0.5f) == 0.5f;
    }

    public float density() {
        return this.density;
    }

    public boolean complete() {
        return this.complete;
    }

    public boolean hasPosition(Vec3 explosion, AABB entity) {
        return this.isExplosionPosition(explosion) && this.entity.isAABBInBounds(entity);
    }

    public boolean isKnownPosition(Vec3 point) {
        return this.entity.isVec3InBounds(point);
    }

    public boolean isExplosionPosition(Vec3 explosion) {
        return this.source.isVec3InBounds(explosion);
    }

    public void expand(Vec3 explosion, Entity entity) {
        this.source = this.source.expand(explosion);
        this.known  = this.known.expand(entity.position());
        this.entity = this.entity.minmax(entity.getBoundingBox());
    }
}
