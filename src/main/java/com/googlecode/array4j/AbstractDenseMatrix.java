package com.googlecode.array4j;

import com.googlecode.array4j.internal.ToArraysConverter;

public abstract class AbstractDenseMatrix<M extends DenseMatrix<M, V>, V extends DenseVector<V>, S extends DenseMatrixSupport<M, V>, ValueArray>
        extends AbstractMatrix<M, V> implements DenseMatrix<M, V> {
    private final transient ToArraysConverter<M, ValueArray> arraysConverter;

    final int offset;

    final Orientation orientation;

    final int stride;

    protected transient S support;

    public AbstractDenseMatrix(final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(rows, columns);
        this.offset = offset;
        this.stride = stride;
        this.orientation = orientation;
        // only create the arrays converter after other fields have been set
        this.arraysConverter = createArraysConverter();
    }

    public final V column(final int column) {
        return support.column(column);
    }

    protected abstract ToArraysConverter<M, ValueArray> createArraysConverter();

    public final int offset() {
        return offset;
    }

    public final Orientation orientation() {
        return orientation;
    }

    public final V row(final int row) {
        return support.row(row);
    }

    public final int stride() {
        return stride;
    }

    public final ValueArray[] toColumnArrays() {
        return arraysConverter.toArrays(columns, rows, false);
    }

    public final ValueArray[] toRowArrays() {
        return arraysConverter.toArrays(rows, columns, true);
    }
}
