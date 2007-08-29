package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.sun.jna.ptr.IntByReference;

public final class H5Test {
    @Test
    public void testH5get_libversion() {
        IntByReference pmajnum = new IntByReference();
        IntByReference pminnum = new IntByReference();
        IntByReference prelnum = new IntByReference();
        int err = H5Library.INSTANCE.H5get_libversion(pmajnum, pminnum, prelnum);
        assertEquals(0, err);
        assertEquals(1, pmajnum.getValue());
        assertEquals(6, pminnum.getValue());
        assertEquals(6, prelnum.getValue());
    }

    @Test
    public void testHyperslabReadWrite() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
//        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
//        H5File h5 = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        H5File h5 = new H5File("sds.h5");

        FloatDenseMatrix data = new FloatDenseMatrix(5, 6, Orientation.ROW, Storage.DIRECT);
        for (int j = 0; j < data.rows(); j++) {
            for (int i = 0; i < data.columns(); i++) {
                data.set(j, i, (float) i + j);
            }
        }
        System.out.println(data);

        Group root = h5.getRootGroup();
        DataType dtype = PredefinedType.IEEE_F32LE;
        DataSpace dataspace = new DataSpace(data.rows(), data.columns());
        String name = "FloatArray";
        DataSet dataset = root.createDataSet(name, dtype, dataspace);
        dataset.write(data.data(), PredefinedType.IEEE_F32LE);
        dataspace.close();
        dataset.close();

        DataSet dataset2 = root.openDataSet(name);
        DataSpace dataspace2 = dataset2.getSpace();
        long[] start = {1, 2};
        long[] count = {3, 4};
        dataspace2.selectHyperslab(SelectionOperator.SET, start, count);
        FloatDenseMatrix out = new FloatDenseMatrix(data.rows(), data.columns());
        dataset2.read(out.data(), dtype, DataSpace.ALL, dataspace2);
        dataspace2.close();
        dataset2.close();

        h5.close();

        System.out.println(out);
    }
}
