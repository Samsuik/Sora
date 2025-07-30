package me.samsuik.sakura.utils.collections;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;
import java.util.Comparator;

public final class OrderedComparatorList<T> extends ObjectArrayList<T> {
    private final Comparator<T> comparator;
    private boolean binarySearch = true;

    public OrderedComparatorList(int capacity, Comparator<T> comparator) {
        super(capacity);
        this.comparator = Comparator.nullsLast(comparator);
    }

    public OrderedComparatorList(Comparator<T> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    private void validateBounds(int index, T t, boolean up) {
        if (index != 0 && this.comparator.compare(get(index - 1), t) > 0) {
            this.binarySearch = false;
        } else if (up && index < size() - 1 && this.comparator.compare(get(index + 1), t) < 0) {
            this.binarySearch = false;
        }
    }

    @Override
    public boolean add(T t) {
        this.validateBounds(size(), t, false);
        return super.add(t);
    }

    @Override
    public void add(int index, T t) {
        this.validateBounds(index, t, true);
        super.add(index, t);
    }

    @Override
    public int indexOf(final Object k) {
        if (this.binarySearch) {
            return Math.max(Arrays.binarySearch(this.a, (T) k, this.comparator), -1);
        } else {
            return super.indexOf(k);
        }
    }
}
