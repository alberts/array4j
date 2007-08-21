package net.lunglet.hdf;

public enum DataSpaceClass {
    NO_CLASS(-1), SCALAR(0), SIMPLE(1), COMPLEX(2);

    private final int value;

    DataSpaceClass(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
