package cz.vutbr.fit.speech.phnrec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixMath;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class PhonemeUtil {
    private PhonemeUtil() {
    }

    public static FloatDenseVector calculateNGrams(final FloatDenseMatrix posteriors) {
        if (posteriors.columns() < 2) {
            throw new IllegalArgumentException();
        }
        FloatDenseVector monograms = FloatMatrixUtils.columnSum(posteriors);
        monograms.plusEquals(1.0f);
        monograms.divideEquals(FloatMatrixUtils.sum(monograms));
        FloatMatrixMath.logEquals(monograms);
        monograms.minusEquals(FloatMatrixUtils.mean(monograms));

        FloatDenseMatrix b1 = posteriors.subMatrixColumns(0, posteriors.columns() - 1);
        FloatDenseMatrix b2 = posteriors.subMatrixColumns(1, posteriors.columns());
        FloatMatrix<?, ?> bigrams = b1.times(b2.transpose());
        bigrams.plusEquals(1.0f);
        bigrams.divideEquals(FloatMatrixUtils.sum(bigrams));
        FloatMatrixMath.logEquals(bigrams);
        bigrams.minusEquals(FloatMatrixUtils.mean(bigrams));

        FloatDenseVector ngrams = FloatMatrixUtils.concatenate(monograms, bigrams.asVector());
        return ngrams;
    }

    public static List<MasterLabel> readMasterLabels(final Reader reader) throws IOException {
        List<MasterLabel> labels = new ArrayList<MasterLabel>();
        BufferedReader bufReader = new BufferedReader(reader);
        String line = bufReader.readLine();
        while (line != null) {
            String[] parts = line.split("\\s+");
            String label = parts[2];
            long startTime = Long.valueOf(parts[0]);
            long endTime = Long.valueOf(parts[1]);
            float score = Float.valueOf(parts[3]);
            labels.add(new MasterLabel(label, startTime, endTime, score));
            line = bufReader.readLine();
        }
        // TODO maybe this close shouldn't be here
        bufReader.close();
        return labels;
    }
}
