package com.googlecode.array4j;

public class DoubleArray<E extends DoubleArray> extends AbstractArray<E> {
    public final E get(final Object... indexes) {
        // TODO this is going to require us to call the constructor of the
        // derived type
        checkIndexes(indexes);
        return null;
    }
}
