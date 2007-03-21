package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayDescr;
import com.googlecode.array4j.ArrayFunctions;
import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.ScalarKind;

// TODO probably need extra functions for ufuncs that have multiple output arguments

public interface UFunc {
    int nin();

    int nout();

    int nargs();

    Object[] selectTypes(ArrayType[] argtypes, ScalarKind[] scalars, Object typetup);

    void call(ArrayFunctions functions, ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void call(DenseArray... args);

    DenseArray call(ArrayDescr dtype, DenseArray... args);

    DenseArray reduce(DenseArray arr);

    DenseArray reduce(DenseArray arr, ArrayDescr dtype);

    DenseArray reduce(DenseArray arr, int axis);

    DenseArray reduce(DenseArray arr, int axis, ArrayDescr dtype);

    DenseArray accumulate(DenseArray arr);

    DenseArray accumulate(DenseArray arr, ArrayDescr dtype);

    DenseArray accumulate(DenseArray arr, int axis);

    DenseArray accumulate(DenseArray arr, int axis, ArrayDescr dtype);

    DenseArray reduceat(DenseArray arr);

    DenseArray reduceat(DenseArray arr, ArrayDescr dtype);

    DenseArray reduceat(DenseArray arr, int axis);

    DenseArray reduceat(DenseArray arr, int axis, ArrayDescr dtype);

    DenseArray reduceat(DenseArray arr, int[] indices);

    DenseArray reduceat(DenseArray arr, int[] indices, ArrayDescr dtype);

    DenseArray reduceat(DenseArray arr, int axis, int... indices);

    DenseArray reduceat(DenseArray arr, int axis, int[] indices, ArrayDescr dtype);

    DenseArray outer(DenseArray arr1, DenseArray arr2);

    DenseArray outer(DenseArray arr1, DenseArray arr2, ArrayDescr dtype);
}
