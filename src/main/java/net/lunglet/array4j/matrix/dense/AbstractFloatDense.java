package net.lunglet.array4j.matrix.dense;

import java.nio.FloatBuffer;
import java.util.Arrays;
import net.lunglet.array4j.Constants;
import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import net.lunglet.util.BufferUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Abstract base class for dense floating point matrix and vector.
 */
abstract class AbstractFloatDense extends AbstractDenseMatrix<FloatDenseVector, float[]> {
    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_STRIDE = 1;

    private static final int ELEMENT_SIZE = 1;

    private static final int ELEMENT_SIZE_BYTES = Constants.FLOAT_BYTES;

    private static final long serialVersionUID = 1L;

    protected static void copy(final FloatMatrix src, final FloatDenseMatrix dest) {
        if (src.rows() != dest.rows() || src.columns() != dest.columns()) {
            throw new IllegalArgumentException();
        }
        // TODO optimize this
        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.columns(); j++) {
                dest.set(i, j, src.get(i, j));
            }
        }
    }

    private static FloatBuffer createBuffer(final int size, final Storage storage) {
        return BufferUtils.createFloatBuffer(size, storage);
    }

    /** Data buffer. */
    protected transient FloatBuffer data;

    /**
     * Constructor for vector with a base member.
     */
    public AbstractFloatDense(final AbstractFloatDense base, final int size, final int offset, final int stride,
            final Direction direction) {
        this(base, vectorRows(size, direction), vectorColumns(size, direction), offset, stride, Order.COLUMN);
    }

    /**
     * Constructor for matrix with a base member.
     */
    public AbstractFloatDense(final AbstractFloatDense base, final int rows, final int columns, final int offset,
            final int stride, final Order order) {
        // TODO if stride is >= 0, slice buffer and make offset 0
        super(base, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, order);
        checkData(base.data);
        this.data = base.data;
    }

    /**
     * Constructor for new vector.
     */
    public AbstractFloatDense(final int size, final Direction direction, final Storage storage) {
        this(vectorRows(size, direction), vectorColumns(size, direction), direction.order(), storage);
    }

    /**
     * Constructor for new matrix.
     */
    public AbstractFloatDense(final int rows, final int columns, final Order order, final Storage storage) {
        super(null, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, DEFAULT_OFFSET, DEFAULT_STRIDE, order);
        this.data = createBuffer(rows * columns, storage);
        checkData(this.data);
    }

    /** {@inheritDoc} */
    @Override
    public final FloatDenseVector column(final int column) {
        return new FloatDenseVectorImpl(this, rows, columnOffset(column), rowStride, Direction.COLUMN);
    }

    @Override
    protected final float[] createArray(final int length) {
        return new float[length];
    }

    @Override
    protected final float[][] createArrayArray(final int length) {
        return new float[length][];
    }

    /** {@inheritDoc} */
    public final FloatBuffer data() {
        return ((FloatBuffer) data.position(offset)).slice();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractFloatDense)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }

    @Override
    protected final void fillWithElement(final float[] dest, final int srcPos) {
        Arrays.fill(dest, data.get(srcPos));
    }

    public final float get(final int index) {
        return data.get(elementOffset(index));
    }

    public final float get(final int row, final int column) {
        return data.get(elementOffset(row, column));
    }

    public final int length() {
        return length;
    }

    @Override
    public final FloatDenseVector row(final int row) {
        return new FloatDenseVectorImpl(this, columns, rowOffset(row), columnStride, Direction.ROW);
    }

    public final void set(final int index, final float value) {
        data.put(elementOffset(index), value);
    }

    public final void set(final int row, final int column, final float value) {
        data.put(elementOffset(row, column), value);
    }

    public final void setColumn(final int column, final FloatVector columnVector) {
        checkColumnVector(columnVector);
        int targetOffset = columnOffset(column);
        int targetStride = rowStride;
        // TODO this could be optimized
        for (int i = 0; i < rows; i++) {
            data.put(targetOffset + i * targetStride, columnVector.get(i));
        }
    }

    @Override
    protected final void elementCopy(final int srcPos, final float[] dest, final int destPos) {
        dest[destPos] = data.get(srcPos);
    }

    public final void setRow(final int row, final FloatVector rowVector) {
        checkRowVector(rowVector);
        int targetOffset = rowOffset(row);
        int targetStride = columnStride;
        // TODO this could be optimized
        for (int i = 0; i < columns; i++) {
            data.put(targetOffset + i * targetStride, rowVector.get(i));
        }
    }

    /** {@inheritDoc} */
    public final Storage storage() {
        return data.isDirect() ? Storage.DIRECT : Storage.HEAP;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
        return FloatMatrixUtils.toString((FloatMatrix) this);
    }

    public abstract FloatDenseMatrix transpose();
}
