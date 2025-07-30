package me.samsuik.sakura.utils.collections;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class UnorderedIndexedList<T> extends ObjectArrayList<T> {
    private final Int2IntOpenHashMap elementToIndex;

    public UnorderedIndexedList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public UnorderedIndexedList(int capacity) {
        super(capacity);
        this.elementToIndex = new Int2IntOpenHashMap();
        this.elementToIndex.defaultReturnValue(-1);
    }

    @Override
    public boolean add(final T t) {
        this.elementToIndex.put(t.hashCode(), size());
        return super.add(t);
    }

    @Override
    public T remove(final int index) {
        final int tail = size() - 1;
        final T at = a[index];

        if (index != tail) {
            final T tailObj = a[tail];
            if (tailObj != null)
                this.elementToIndex.put(tailObj.hashCode(), index);
            this.a[index] = tailObj;
        }

        if (at != null)
            this.elementToIndex.remove(at.hashCode());
        this.a[tail] = null;
        this.size = tail;
        return at;
    }

    @Override
    public void clear() {
        this.elementToIndex.clear();
        super.clear();
    }

    @Override
    public int indexOf(final Object k) {
        if (k == null) return -1;
        // entities uses their id as a hashcode
        return this.elementToIndex.get(k.hashCode());
    }

    @Override
    public void add(final int index, final T t) {
        throw new UnsupportedOperationException();
    }
}
