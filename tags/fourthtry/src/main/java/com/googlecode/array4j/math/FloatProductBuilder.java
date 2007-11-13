package com.googlecode.array4j.math;

import com.googlecode.array4j.FloatMatrix;

public final class FloatProductBuilder {
    public FloatProductBuilder times(final float value) {
        return this;
    }

    public FloatProductBuilder times(final FloatMatrix<?, ?> matrix) {
        return this;
    }

    public FloatMatrix<?, ?> build() {
        return null;
    }
}