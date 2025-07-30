package me.samsuik.sakura.explosion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LegacyExplosionClipping {
    public static BlockHitResult.Type clipLegacy(Level level, Vec3 from, Vec3 to) {
        int toX = Mth.floor(to.x);
        int toY = Mth.floor(to.y);
        int toZ = Mth.floor(to.z);
        int fromX = Mth.floor(from.x);
        int fromY = Mth.floor(from.y);
        int fromZ = Mth.floor(from.z);

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(fromX, fromY, fromZ);
        LevelChunk chunk = level.getChunkIfLoaded(fromX >> 4, fromZ >> 4);
        if (chunk == null) {
            return BlockHitResult.Type.MISS;
        }

        BlockState state = chunk.getBlockState(mutableBlockPos);
        VoxelShape shape = state.getShape(level, mutableBlockPos);
        for (AABB bb : shape.toAabbs()) {
            if (clip(bb, mutableBlockPos, from, to)) {
                return BlockHitResult.Type.BLOCK;
            }
        }

        for (int steps = 0; steps < 16; ++steps) {
            if (fromX == toX && fromY == toY && fromZ == toZ) {
                return BlockHitResult.Type.MISS;
            }

            boolean moveX = true;
            boolean moveY = true;
            boolean moveZ = true;
            double d0 = 999.0D;
            double d1 = 999.0D;
            double d2 = 999.0D;

            if (toX > fromX) {
                d0 = (double) fromX + 1.0D;
            } else if (toX < fromX) {
                d0 = (double) fromX + 0.0D;
            } else {
                moveX = false;
            }

            if (toY > fromY) {
                d1 = (double) fromY + 1.0D;
            } else if (toY < fromY) {
                d1 = (double) fromY + 0.0D;
            } else {
                moveY = false;
            }

            if (toZ > fromZ) {
                d2 = (double) fromZ + 1.0D;
            } else if (toZ < fromZ) {
                d2 = (double) fromZ + 0.0D;
            } else {
                moveZ = false;
            }

            double d3 = 999.0D;
            double d4 = 999.0D;
            double d5 = 999.0D;
            double d6 = to.x - from.x;
            double d7 = to.y - from.y;
            double d8 = to.z - from.z;

            if (moveX) d3 = (d0 - from.x) / d6;
            if (moveY) d4 = (d1 - from.y) / d7;
            if (moveZ) d5 = (d2 - from.z) / d8;

            if (d3 == -0.0D) d3 = -1.0E-4D;
            if (d4 == -0.0D) d4 = -1.0E-4D;
            if (d5 == -0.0D) d5 = -1.0E-4D;

            Direction moveDir;
            if (d3 < d4 && d3 < d5) {
                moveDir = toX > fromX ? Direction.WEST : Direction.EAST;
                from = new Vec3(d0, from.y + d7 * d3, from.z + d8 * d3);
            } else if (d4 < d5) {
                moveDir = toY > fromY ? Direction.DOWN : Direction.UP;
                from = new Vec3(from.x + d6 * d4, d1, from.z + d8 * d4);
            } else {
                moveDir = toZ > fromZ ? Direction.NORTH : Direction.SOUTH;
                from = new Vec3(from.x + d6 * d5, from.y + d7 * d5, d2);
            }

            fromX = Mth.floor(from.x) - (moveDir == Direction.EAST ? 1 : 0);
            fromY = Mth.floor(from.y) - (moveDir == Direction.UP ? 1 : 0);
            fromZ = Mth.floor(from.z) - (moveDir == Direction.SOUTH ? 1 : 0);
            mutableBlockPos.set(fromX, fromY, fromZ);

            int chunkX = fromX >> 4;
            int chunkZ = fromZ >> 4;
            if (chunkX != chunk.locX || chunkZ != chunk.locZ) {
                chunk = level.getChunkIfLoaded(chunkX, chunkZ);
            }
            if (chunk == null) {
                return BlockHitResult.Type.MISS;
            }

            state = chunk.getBlockState(mutableBlockPos);
            shape = state.getShape(level, mutableBlockPos);
            for (AABB bb : shape.toAabbs()) {
                if (clip(bb, mutableBlockPos, from, to)) {
                    return BlockHitResult.Type.BLOCK;
                }
            }
        }
        return BlockHitResult.Type.MISS;
    }

    private static boolean clip(AABB bb, BlockPos pos, Vec3 from, Vec3 to) {
        from = from.subtract(pos.getX(), pos.getY(), pos.getZ());
        to = to.subtract(pos.getX(), pos.getY(), pos.getZ());

        double x = to.x - from.x;
        double y = to.y - from.y;
        double z = to.z - from.z;

        double minXd = clip(bb.minX, x, from.x);
        double minYd = clip(bb.minY, y, from.y);
        double minZd = clip(bb.minZ, z, from.z);
        double maxXd = clip(bb.maxX, x, from.x);
        double maxYd = clip(bb.maxY, y, from.y);
        double maxZd = clip(bb.maxZ, z, from.z);

        return clipX(from, bb, minXd, y, z) || clipY(from, bb, minYd, x, z) || clipZ(from, bb, minZd, x, y)
            || clipX(from, bb, maxXd, y, z) || clipY(from, bb, maxYd, x, z) || clipZ(from, bb, maxZd, x, y);
    }

    private static double clip(double bound, double axisD, double axisN) {
        if (axisD * axisD < 1.0000000116860974E-7D) {
            return -1.0;
        }
        return (bound - axisN) / axisD;
    }

    private static boolean clipX(Vec3 from, AABB bb, double n, double y, double z) {
        if (n < 0.0 || n > 1.0) {
            return false;
        }
        y = from.y + y * n;
        z = from.z + z * n;
        return y >= bb.minY && y <= bb.maxY && z >= bb.minZ && z <= bb.maxZ;
    }

    private static boolean clipY(Vec3 from, AABB bb, double n, double x, double z) {
        if (n < 0.0 || n > 1.0) {
            return false;
        }
        x = from.x + x * n;
        z = from.z + z * n;
        return x >= bb.minX && x <= bb.maxX && z >= bb.minZ && z <= bb.maxZ;
    }

    private static boolean clipZ(Vec3 from, AABB bb, double n, double x, double y) {
        if (n < 0.0 || n > 1.0) {
            return false;
        }
        x = from.x + x * n;
        y = from.y + y * n;
        return x >= bb.minX && x <= bb.maxX && y >= bb.minY && y <= bb.maxY;
    }
}
