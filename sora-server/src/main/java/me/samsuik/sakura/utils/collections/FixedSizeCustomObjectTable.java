package me.samsuik.sakura.utils.collections;

import it.unimi.dsi.fastutil.HashCommon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public final class FixedSizeCustomObjectTable<T> {
    private final ToIntFunction<T> keyFunction;
    private final T[] contents;
    private final int mask;

    public FixedSizeCustomObjectTable(int size, @NotNull ToIntFunction<T> keyFunction) {
        if (size < 0) {
            throw new IllegalArgumentException("Table size cannot be negative");
        } else {
            int n = HashCommon.nextPowerOfTwo(size - 1);
            this.keyFunction = keyFunction;
            this.contents = (T[]) new Object[n];
            this.mask = (n - 1);
        }
    }

    private int key(T value) {
        return this.keyFunction.applyAsInt(value);
    }

    public @Nullable T get(T value) {
        return this.get(this.key(value));
    }

    public @Nullable T get(int key) {
        return this.contents[key & this.mask];
    }

    public void write(int key, T value) {
        this.contents[key & this.mask] = value;
    }

    public @Nullable T getAndWrite(T value) {
        int key = this.key(value);
        T found = this.get(key);
        this.write(key, value);
        return found;
    }

    public void clear() {
        int size = this.contents.length;
        for (int i = 0; i < size; ++i) {
            this.contents[i] = null;
        }
    }
}
