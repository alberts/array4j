package net.lunglet.array4j.matrix.dense;

import java.nio.Buffer;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.AbstractMatrix;
import net.lunglet.array4j.matrix.Matrix;
import net.lunglet.util.BufferUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Abstract base class for dense (full) matrices.
 */
public abstract class AbstractDenseMatrix<V extends DenseVector, T> extends AbstractMatrix<V> implements DenseMatrix {
    private static final long serialVersionUID = 1L;

    protected static Order orderLike(final Matrix matrix) {
        if (matrix instanceof DenseMatrix) {
            return ((DenseMatrix) matrix).order();
        } else {
            return Order.DEFAULT;
        }
    }

    protected static Storage storageLike(final Matrix matrix) {
        if (matrix instanceof DenseMatrix) {
            return ((DenseMatrix) matrix).storage();
        } else {
            return Storage.DEFAULT;
        }
    }

    /** Stride between elements in a column. */
    protected final int columnStride;

    /** Size of each element in buffer positions. */
    protected final int elementSize;

    /** Size of each element in bytes. */
    protected final int elementSizeBytes;

    /** Offset in storage where matrix data begins. */
    protected final int offset;

    protected final Order order;

    /** Stride between elements in a row. */
    protected final int rowStride;

    /** Stride between elements. */
    protected final int stride;

    public AbstractDenseMatrix(final AbstractDenseMatrix<V, T> base, final int elementSize, final int elementSizeBytes,
            final int rows, final int columns, final int offset, final int stride, final Order order) {
        super(base, rows, columns);
        this.elementSize = elementSize;
        this.elementSizeBytes = elementSizeBytes;
        this.offset = offset;
        this.stride = stride;
        this.order = order;
        if (order.equals(Order.ROW)) {
            this.rowStride = stride * columns;
            this.columnStride = stride;
        } else {
            this.rowStride = stride;
            this.columnStride = stride * rows;
        }
    }

    protected final void checkData(final Buffer data) {
        int start = offset * elementSizeBytes;
        int end = start + (Math.max(0, length - 1) * stride + (length > 0 ? 1 : 0)) * elementSizeBytes;
        int capacity = BufferUtils.getBytesCapacity(data);
        if (start > capacity || end > capacity) {
            throw new IllegalArgumentException();
        }
    }

    protected final void checkIndex(final int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(String.format("Index out of bounds [0,%d): %d", length, index));
        }
    }

    /**
     * Calculate the offset of the beginning of the specified column.
     */
    protected final int columnOffset(final int column) {
        checkColumnIndex(column);
        if (length > 0) {
            return offset + column * elementSize * columnStride;
        } else {
            return offset;
        }
    }

    protected abstract T createArray(int length);

    protected abstract T[] createArrayArray(int length);

    protected final int elementOffset(final int index) {
        if (length > 0) {
            checkIndex(index);
            return offset + index * elementSize * stride;
        } else {
            return offset;
        }
    }

    protected final int elementOffset(final int row, final int column) {
        if (length > 0) {
            checkColumnIndex(column);
            return rowOffset(row) + column * elementSize * columnStride;
        } else {
            return offset;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractDenseMatrix)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AbstractDenseMatrix<?, ?> other = (AbstractDenseMatrix<?, ?>) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.appendSuper(super.equals(obj));
        equalsBuilder.append(elementSize, other.elementSize);
        equalsBuilder.append(order, other.order);
        return equalsBuilder.isEquals();
    }

    /**
     * Assigns the value at the specified position to each element of specified
     * array.
     *
     * @param dest
     *                the array to be filled
     * @param srcPos
     *                the position of the value to be stored in all elements of
     *                the array
     */
    protected abstract void fillWithElement(T dest, int srcPos);

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(elementSize).append(order).toHashCode();
    }

    @Override
    public final int leadingDimension() {
        if (order.equals(Order.COLUMN)) {
            return Math.max(1, base != null ? base.rows() : rows);
        } else {
            return Math.max(1, base != null ? base.columns() : columns);
        }
    }

    public final int offset() {
        return offset;
    }

    public final Order order() {
        return order;
    }

    /**
     * Calculate the offset of the beginning of the specified row.
     */
    protected final int rowOffset(final int row) {
        if (length > 0) {
            checkRowIndex(row);
            return offset + row * elementSize * rowStride;
        } else {
            return offset;
        }
    }

    /**
     * Copies an element from the specified source position to the specified
     * position of the destination array.
     */
    protected abstract void elementCopy(int srcPos, T dest, int destPos);

    @Override
    public final int stride() {
        return stride;
    }

    public final T toArray() {
        final T arr = createArray(length);
        if (length == 0) {
            return arr;
        }
        if (stride == 0) {
            fillWithElement(arr, offset);
            return arr;
        }
        for (int i = offset, j = 0; j < length; i += elementSize * stride, j++) {
            elementCopy(i, arr, j);
        }
        return arr;
    }

    private T[] toArrays(final int m, final int n, final boolean rows) {
        final T[] arrs = createArrayArray(m);
        for (int i = 0; i < m; i++) {
            arrs[i] = createArray(n);
        }
        for (int i = 0; i < m; i++) {
            final T arr = arrs[i];
            for (int j = 0; j < n; j++) {
                int position = offset;
                if (rows) {
                    if (order.equals(Order.ROW)) {
                        position += (i * n + j) * elementSize * stride;
                    } else {
                        position += (j * m + i) * elementSize * stride;
                    }
                } else {
                    if (order.equals(Order.COLUMN)) {
                        position += (i * n + j) * elementSize * stride;
                    } else {
                        position += (j * m + i) * elementSize * stride;
                    }
                }
                elementCopy(position, arr, j);
            }
        }
        return arrs;
    }

    public final T[] toColumnArrays() {
        return toArrays(columns, rows, false);
    }

    public final T[] toRowArrays() {
        return toArrays(rows, columns, true);
    }
}
