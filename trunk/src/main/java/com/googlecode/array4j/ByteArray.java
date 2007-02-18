package com.googlecode.array4j;

import static com.googlecode.array4j.kernel.Interface.kernel;

import java.nio.ByteBuffer;

/*
 * NumPy's get algorithm:
 * <CODE>
 * if (op is integer) {
 *     array_item_nice:
 *     if (nd == 1) {
 *         return array scalar using index2ptr
 *     } else {
 *         use index2ptr to get get pointer to data
 *         returned array has nd - 1; first dimension and stride are stripped off
 *         return array_big_item(i) through PyArray_Return
 *     }
 * }
 * if (op is tuple with exactly nd elements) {
 *     return array scalar using PyArray_GetPtr to get value
 * }
 *
 * else: call array_subscript with op, which does:
 *     if (PyString_Check(op) || PyUnicode_Check(op)) {
 *         look for a field named op
 *     }
 *     if nd == 0 {
 *         if op == ellipsis
 *             return self (for array scalars)
 *         if op == None
 *             return self with a new axis
 *         if op is a tuple
 *             if tuple is empty
 *                 return self
 *             if ((nd = count_new_axes_0d(op)) == -1)
 *                 return NULL;
 *             return add_new_axes_0d(self, nd);
 *     }
 *     check if op implies fancy indexing
 *     if (op is fancy) {
 *         ...
 *     }
 *     else: call array_subscript_simple, which does:
 *         if (op can be converted to intp) {
 *             return array_big_item(op as intp)
 *         }
 *         // this can return an error
 *         calculate nd = parse_index(self, op, dimensions, strides, &offset)
 *         parse_index does:
 *             if op is slice, ellipsis or None:
 *                 incref op
 *                 set n = 1
 *                 set is_slice to 1
 *             else:
 *                 if op is not a sequence, throw
 *                 n = sequence length of op
 *                 is_slice = 0
 *             for (int i : 0 -> i < n)
 *                 if (!is_slice)
 *                     check that i is a valid index into op (which is a known to be a sequence)
 *                 start = parse_subindex(op1, &step_size, &n_steps, XXX conditional)
 *                 if start == -1: break
 *                 XXX big conditional
 *             XXX another conditional
 *
 *
 *
 *         return array with new nd, same dimensions, same strides, data pointer at: data+offset
 *
 * if array_subscript returned array with nd == 0:
 *     do various checks related to op being an ellipsis
 *     return an array scalar in some cases
 *
 * </CODE>
 */

public class ByteArray<E extends ByteArray> extends AbstractArray<E> {
    private ByteBuffer fBuffer;

    public ByteArray(final int capacity) {
        this.fBuffer = kernel().createByteBuffer(capacity);
    }

    private void reconfigureBuffer(final int capacity) {
        this.fBuffer = kernel().createByteBuffer(capacity);
    }

    public final byte get(final int... indexes) {
        return fBuffer.get(indexesToIndex(indexes));
    }

    public final E get(final Object... indexes) {
        checkIndexes(indexes);
//        throw new UnsupportedOperationException();
        return null;
    }

    public static ByteArray arange(final int stop) {
        return arange(0, stop, 1);
    }

    public static ByteArray arange(final int start, final int stop) {
        return arange(start, stop, 1);
    }

    public static ByteArray arange(final int start, final int stop, final int step) {
        return arange(new ByteArray(0), valueOf(start), valueOf(stop), valueOf(step));
    }

    /**
     * This method takes an instance of E to allow wrappers to pass in an
     * existing instance of be reconfigured.
     */
    protected static <E extends ByteArray> E arange(final E out, final byte start, final byte stop, final byte step) {
        // TODO this isn't quite right yet
        int capacity = (stop - start) / step;
        if (capacity < 0) {
            capacity = 0;
        }
        out.reconfigureBuffer(capacity);
        // TODO test for possible overflow problems here
        for (byte x = start; x < stop; x += step) {
            out.fBuffer.put(x);
        }
        out.reconfigureShapeStrides(new int[]{out.fBuffer.capacity()}, new int[]{1});
        return out;
    }

    private static byte valueOf(final int i) {
        if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("integer out of bounds");
        }
        return (byte) i;
    }
}
