package net.lunglet.array4j.matrix.dense;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.MatrixTestSupport;
import org.junit.Test;

public final class FloatDenseTest {
    private void checkSerialization(final FloatMatrix x) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(x);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        FloatMatrix y = (FloatMatrix) ois.readObject();
        ois.close();
        assertEquals(x.getClass(), y.getClass());
        assertEquals(x.rows(), y.rows());
        assertEquals(x.columns(), y.columns());
        for (int i = 0; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++) {
                assertEquals(x.get(i, j), y.get(i, j), 0);
            }
        }
        if (x instanceof DenseMatrix) {
            DenseMatrix dx = (DenseMatrix) x;
            DenseMatrix dy = (DenseMatrix) y;
            assertEquals(dx.data().isDirect(), dy.data().isDirect());
            assertEquals(dx.storage(), dy.storage());
            assertEquals(dx.order(), dy.order());
        }
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        List<FloatMatrix> matrices = new ArrayList<FloatMatrix>();
        matrices.add(DenseFactory.floatMatrix(0, 0));
        matrices.add(DenseFactory.floatMatrix(1, 1));
        matrices.add(DenseFactory.floatMatrix(2, 3));
        matrices.add(DenseFactory.floatVector(0));
        matrices.add(DenseFactory.floatVector(1));
        for (FloatVector v : DenseFactory.floatMatrix(3, 4).columnsIterator()) {
            matrices.add(v);
        }
        for (FloatVector v : DenseFactory.floatMatrix(3, 4).rowsIterator()) {
            matrices.add(v);
        }
        for (FloatMatrix x : matrices) {
            MatrixTestSupport.populateMatrix(x);
            checkSerialization(x);
        }
    }
}
