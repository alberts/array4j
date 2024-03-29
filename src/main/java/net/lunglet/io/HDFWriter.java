package net.lunglet.io;

import java.io.Closeable;
import java.io.File;
import net.jcip.annotations.NotThreadSafe;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.packed.FloatPackedMatrix;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.DataType;
import net.lunglet.hdf.FloatType;
import net.lunglet.hdf.H5File;

// TODO handle any order, stride, offset, size, etc.

// TODO write via an intermediate direct buffer if matrix is
// stored on the heap and exceeds some maximum size

// TODO recursively create non-existant groups

// TODO allow a large matrix to be created and return a handle
// that allows it be written one row or column at a time

// TODO make it easy to write to an in-memory file and then only
// commit data to disk once everything is okay (transactions work-alike)

@NotThreadSafe
public final class HDFWriter implements Closeable {
    private final H5File h5file;

    public HDFWriter(final File file) {
        this(new H5File(file, H5File.H5F_ACC_TRUNC));
    }

    public HDFWriter(final H5File h5file) {
        this.h5file = h5file;
    }

    public HDFWriter(final String name) {
        this(new H5File(name, H5File.H5F_ACC_TRUNC));
    }

    public void close() {
        h5file.close();
    }

    public void createGroup(final String name) {
        h5file.getRootGroup().createGroup(name).close();
    }

    public void flush() {
        h5file.flush();
    }

    public H5File getH5File() {
        return h5file;
    }

    public void write(final String name, final FloatDenseMatrix matrix) {
        if (!matrix.order().equals(Order.ROW) && matrix.rows() > 1 && matrix.columns() > 1) {
            throw new IllegalArgumentException();
        }
        DataType memType = FloatType.IEEE_F32LE;
        DataSet dataset = h5file.getRootGroup().createDataSet(name, memType, matrix.rows(), matrix.columns());
        try {
            dataset.write(matrix.data(), memType);
        } finally {
            dataset.close();
        }
    }

    public void write(final String name, final FloatPackedMatrix matrix) {
        DataSet dataset = null;
        try {
            long n = matrix.rows();
            long k = n * (n + 1) / 2;
            DataType memType = FloatType.IEEE_F32LE;
            dataset = h5file.getRootGroup().createDataSet(name, memType, k);
            dataset.write(matrix.data(), memType);
        } finally {
            if (dataset != null) {
                dataset.close();
            }
        }
    }
}
