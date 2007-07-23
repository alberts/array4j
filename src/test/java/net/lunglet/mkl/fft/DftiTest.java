package net.lunglet.mkl.fft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public final class DftiTest {
    @Test
    public void testDescriptor() throws DftiException {
        final int[] lengths = new int[]{4, 5};
        DftiDescriptor desc = new DftiDescriptor(DftiConfigValue.SINGLE, DftiConfigValue.REAL, lengths);
        assertEquals(lengths.length, desc.getIntValue(DftiConfigParam.DIMENSION));
        // TODO default descriptor name returns garbage instead of empty string
        desc.setValue(DftiConfigParam.DESCRIPTOR_NAME, "hello");
        assertEquals("hello", desc.getStringValue(DftiConfigParam.DESCRIPTOR_NAME));
        assertTrue(desc.getStringValue(DftiConfigParam.VERSION).startsWith("Intel"));
        assertEquals(DftiConfigValue.UNCOMMITTED, desc.getValue(DftiConfigParam.COMMIT_STATUS));
        assertEquals(1.0f, desc.getFloatValue(DftiConfigParam.FORWARD_SCALE));
        assertEquals(1.0f, desc.getFloatValue(DftiConfigParam.BACKWARD_SCALE));
        assertEquals(1, desc.getIntValue(DftiConfigParam.NUMBER_OF_TRANSFORMS));
        // TODO this crashes
//        assertEquals(1, desc.getIntValue(DftiConfigParam.NUMBER_OF_USER_THREADS));
        assertEquals(0, desc.getIntValue(DftiConfigParam.INPUT_DISTANCE));
        assertEquals(0, desc.getIntValue(DftiConfigParam.OUTPUT_DISTANCE));
        assertEquals(DftiConfigValue.INPLACE, desc.getValue(DftiConfigParam.PLACEMENT));
        assertEquals(DftiConfigValue.COMPLEX_COMPLEX, desc.getValue(DftiConfigParam.COMPLEX_STORAGE));
        assertEquals(DftiConfigValue.REAL_REAL, desc.getValue(DftiConfigParam.REAL_STORAGE));
        assertEquals(DftiConfigValue.COMPLEX_REAL, desc.getValue(DftiConfigParam.CONJUGATE_EVEN_STORAGE));
        assertEquals(DftiConfigValue.CCS_FORMAT, desc.getValue(DftiConfigParam.PACKED_FORMAT));
        assertEquals(DftiConfigValue.ORDERED, desc.getValue(DftiConfigParam.ORDERING));
        assertEquals(DftiConfigValue.NONE, desc.getValue(DftiConfigParam.TRANSPOSE));

        System.out.println(Arrays.toString(desc.getIntArrayValue(DftiConfigParam.LENGTHS)));
        System.out.println(Arrays.toString(desc.getIntArrayValue(DftiConfigParam.INPUT_STRIDES)));
        System.out.println(Arrays.toString(desc.getIntArrayValue(DftiConfigParam.OUTPUT_STRIDES)));

        desc.commit();
        desc.free();
    }
}
