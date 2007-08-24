package net.lunglet.hdf;

import com.sun.jna.ptr.LongByReference;

public final class H5File extends IdComponent {
    /* create non-existing files */
    private static final int H5F_ACC_CREAT = 0x0010;

    /* print debug info */
    private static final int H5F_ACC_DEBUG = 0x0008;

    /* fail if file already exists */
    private static final int H5F_ACC_EXCL = 0x0004;

    /* absence of rdwr => rd-only */
    public static final int H5F_ACC_RDONLY = 0x0000;

    /* open for read and write */
    public static final int H5F_ACC_RDWR = 0x0001;

    /* overwrite existing files */
    private static final int H5F_ACC_TRUNC = 0x0002;

    private static int init(final String name, final int flags, final FileCreatePropList createPlist,
            final FileAccessPropList accessPlist) {
        final int id;
        // These bits only set for creation, so if any of them are set,
        // create the file.
        if ((flags & (H5F_ACC_EXCL | H5F_ACC_TRUNC | H5F_ACC_DEBUG)) != 0) {
            int createPlistId = createPlist.getId();
            int accessPlistId = accessPlist.getId();
            id = H5Library.INSTANCE.H5Fcreate(name, flags, createPlistId, accessPlistId);
            if (id < 0) {
                // throw an exception when open/create fail
                throw new H5FileException("H5Fcreate failed");
            }
        } else {
            // Open the file if none of the bits above are set.
            int accessPlistId = accessPlist.getId();
            id = H5Library.INSTANCE.H5Fopen(name, flags, accessPlistId);
            if (id < 0) {
                // throw an exception when open/create fail
                throw new H5FileException("H5Fopen failed");
            }
        }
        return id;
    }

    private final Group rootGroup;

    public H5File(final String name) {
        this(name, H5F_ACC_TRUNC);
    }

    public H5File(final String name, final int flags) {
        this(name, flags, FileCreatePropList.DEFAULT, FileAccessPropList.DEFAULT);
    }

    public H5File(final String name, final int flags, final FileCreatePropList createPlist,
            final FileAccessPropList accessPlist) {
        super(init(name, flags, createPlist, accessPlist));
        this.rootGroup = new Group(getId(), "/");
    }

    public void close() {
        int err = H5Library.INSTANCE.H5Fclose(getId());
        if (err < 0) {
            throw new H5FileException("H5Fclose failed");
        }
        invalidate();
    }

    @Override
    protected void finalize() throws Throwable {
        if (isValid()) {
            close();
        }
        super.finalize();
    }

    public String getFileName() {
        // Preliminary call to H5Fget_name to get the length of the file name
        int nameSize = H5Library.INSTANCE.H5Fget_name(getId(), null, 0);

        // If H5Aget_name returns a negative value, raise an exception,
        if (nameSize < 0) {
            throw new H5IdComponentException("H5Fget_name failed");
        }

        // TODO implement the rest of this function
        return null;
    }

    public long getFileSize() {
        LongByReference psize = new LongByReference();
        int err = H5Library.INSTANCE.H5Fget_filesize(getId(), psize);
        if (err < 0) {
            throw new H5FileException("H5Fget_filesize failed");
        }
        return psize.getValue();
    }

    public Group getRootGroup() {
        return rootGroup;
    }
}