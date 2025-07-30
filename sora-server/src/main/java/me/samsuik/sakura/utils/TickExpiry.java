package me.samsuik.sakura.utils;

import com.google.common.base.Preconditions;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TickExpiry {
    private long tick;
    private final int expiration;

    public TickExpiry(long tick, int expiration) {
        Preconditions.checkArgument(expiration > 0, "expiration must be greater than 0");
        this.tick = tick;
        this.expiration = expiration;
    }

    public void refresh(long tick) {
        this.tick = tick;
    }

    public boolean isExpired(long tick) {
        return this.tick < tick - this.expiration;
    }
}
