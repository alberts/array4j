package net.lunglet.hdf;

import java.util.Arrays;

public final class DataSpace extends IdComponent {
    private static final int H5S_ALL = 0;

    public static final DataSpace ALL = new DataSpace(H5S_ALL);

    private static final int H5S_UNLIMITED = -1;

    private static int init(final long[] dims, final long[] maxdims) {
        if (maxdims != null && dims.length != maxdims.length) {
            throw new IllegalArgumentException();
        }
        int rank = dims.length;
        // reverse dimensions
        long[] rdims = new long[dims.length];
        for (int i = 0; i < rdims.length; i++) {
            rdims[i] = dims[dims.length - i - 1];
        }
        final long[] rmaxdims;
        if (maxdims != null) {
            rmaxdims = new long[maxdims.length];
            for (int i = 0; i < rmaxdims.length; i++) {
                rmaxdims[i] = rmaxdims[rmaxdims.length - i - 1];
            }
        } else {
            rmaxdims = null;
        }
        int id = H5Library.INSTANCE.H5Screate_simple(rank, rdims, rmaxdims);
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate_simple failed");
        }
        return id;
    }

    DataSpace(final int id) {
        super(id);
    }

    DataSpace(final long[] dims) {
        this(dims, null);
    }

    DataSpace(final long[] dims, final long[] maxdims) {
        super(init(dims, maxdims));
    }

    private int getNDims() {
        int ndims = H5Library.INSTANCE.H5Sget_simple_extent_ndims(getId());
        if (ndims < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_ndims failed");
        }
        return ndims;
    }

    public int[] getDims() {
        int[] dims = new int[getNDims()];
        int err = H5Library.INSTANCE.H5Sget_simple_extent_dims(getId(), dims, null);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_dims failed");
        }
        return dims;
    }

    public int[] getMaxDims() {
        int[] maxdims = new int[getNDims()];
        int err = H5Library.INSTANCE.H5Sget_simple_extent_dims(getId(), null, maxdims);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_dims failed");
        }
        return maxdims;
    }

    public void close() {
        if (getId() != H5S_ALL) {
            // not a constant, should call H5Sclose
            int err = H5Library.INSTANCE.H5Sclose(getId());
            if (err < 0) {
                throw new H5DataSpaceException("H5Sclose failed");
            }
            invalidate();
        } else {
            // cannot close a constant
            throw new H5DataSpaceException("Cannot close a constant");
        }
    }

    @Override
    public String toString() {
        return "DataSpace[dims=" + Arrays.toString(getDims()) + ", maxdims=" + Arrays.toString(getMaxDims()) + "]";
    }

    // TODO use an enum for type
    private static int init(final int type) {
        int id = H5Library.INSTANCE.H5Screate(type);
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate failed");
        }
        return id;
    }
}