package net.lunglet.array4j.matrix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import net.lunglet.array4j.Constants;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.util.AssertUtils;
import org.apache.commons.lang.NotImplementedException;

public final class FloatDenseUtils {
    /**
     * Memory map HTK file into matrix.
     * <p>
     * Mapped files can't reliably be deleted after having been mapped. If this
     * presents a problem, use readHTK instead.
     */
    public static FloatDenseMatrix mapHTK(final File file) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        ByteBuffer buf = channel.map(MapMode.READ_ONLY, 0, channel.size()).order(ByteOrder.BIG_ENDIAN);
        int nsamples = buf.getInt(0);
        int period = buf.getInt(4);
        int size = buf.getShort(8) & 0xffff;
        int kind = buf.getShort(10) & 0xffff;
        FloatBuffer data = ((ByteBuffer) buf.position(12)).asFloatBuffer();
        if (data.remaining() != nsamples * size / Constants.FLOAT_BYTES) {
            throw new IOException();
        }
        int columns = data.remaining() / nsamples;
//        return new FloatDenseMatrix(data, nsamples, columns, 0, 1, Order.ROW);
        return null;
    }

    public static FloatDenseMatrix mapHTK(final String filename) throws IOException {
        return mapHTK(new File(filename));
    }

    public static FloatDenseMatrix readHTK(final File file) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) channel.size());
        channel.read(buf);
        channel.close();
        int nsamples = buf.getInt(0);
        int period = buf.getInt(4);
        int size = buf.getShort(8) & 0xffff;
        int kind = buf.getShort(10) & 0xffff;
        FloatBuffer data = ((ByteBuffer) buf.position(12)).asFloatBuffer();
        if (data.remaining() != nsamples * size / Constants.FLOAT_BYTES) {
            throw new IOException();
        }
        int columns = data.remaining() / nsamples;
//        return new FloatDenseMatrix(data, nsamples, columns, 0, 1, Order.ROW);
        return null;
    }

    /**
     * Get a submatrix spanning the column range [column0, column1).
     */
    public static FloatDenseMatrix subMatrixColumns(final FloatDenseMatrix x, final int column0, final int column1) {
        AssertUtils.checkArgument(column0 >= 0 && column0 <= x.columns(), String.format(
            "column0=%d not in range [0, %d]", column0, x.columns()));
        AssertUtils.checkArgument(column1 >= column0 && column1 <= x.columns(), String.format(
            "column1=%d not in range [%d, %d]", column1, column0, x.columns()));
        if (column0 == 0 && column1 == x.columns()) {
            return x;
        }
        int cols = column1 - column0;
        if (x.order().equals(Order.COLUMN)) {
//            return new FloatDenseMatrix(x, x.rows(), cols, x.columnOffset(column0), x.stride, x.order());
            throw new NotImplementedException();
        } else {
            FloatDenseMatrix newMatrix = DenseFactory.floatMatrix(x.rows(), cols, x.order(), x.storage());
            for (int i = column0, j = 0; i < column1; i++, j++) {
                newMatrix.setColumn(j, x.column(i));
            }
            return newMatrix;
        }
    }

    private FloatDenseUtils() {
    }
}
