package net.lunglet.fft;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.ComplexFloat;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.CFloatDenseVector;
import com.googlecode.array4j.dense.FloatDenseVector;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FFTTest {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new MKLFFT(), Storage.DIRECT}});
    }

    private final FFT fft;

    private final Storage storage;

    public FFTTest(final FFT fft, final Storage storage) {
        this.fft = fft;
        this.storage = storage;
    }

    @Test
    public void test() {
        FloatDenseVector x = new FloatDenseVector(storage, 1.0f, 2.0f, 3.0f);
        CFloatDenseVector y = fft.fft(x, x.length());
        assertEquals(new ComplexFloat(6.0f, 0.0f), y.get(0));
        assertEquals(new ComplexFloat(-1.5f, 0.8660254f), y.get(1));
        assertEquals(new ComplexFloat(-1.5f, -0.8660254f), y.get(2));
        y = fft.fft(x, 4);
        assertEquals(new ComplexFloat(6.0f, 0.0f), y.get(0));
        assertEquals(new ComplexFloat(-2.0f, -2.0f), y.get(1));
        assertEquals(new ComplexFloat(2.0f, 0.0f), y.get(2));
        assertEquals(new ComplexFloat(-2.0f, 2.0f), y.get(3));
    }
}