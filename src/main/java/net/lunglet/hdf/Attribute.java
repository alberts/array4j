package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.lang.NotImplementedException;

// TODO Attribute#getName should return full name or make a new getFullName

public final class Attribute extends AbstractDs {
    private static final CloseAction CLOSE_ACTION = new CloseAction() {
        @Override
        public void close(final int id) {
            int err = H5Library.INSTANCE.H5Aclose(id);
            if (err < 0) {
                throw new H5AttributeException("H5Aclose failed");
            }
        }
    };

    static Attribute create(final int locId, final String name, final DataType type, final DataSpace space) {
        final int id;
        synchronized (H5Library.INSTANCE) {
            id = H5Library.INSTANCE.H5Acreate(locId, name, type.getId(), space.getId(), H5Library.H5P_DEFAULT);
            if (id < 0) {
                throw new H5AttributeException("H5Acreate failed");
            }
        }
        return new Attribute(id);
    }

    static Attribute open(final int locId, final String name) {
        final int id;
        synchronized (H5Library.INSTANCE) {
            id = H5Library.INSTANCE.H5Aopen_name(locId, name);
            if (id < 0) {
                throw new H5AttributeException("H5Aopen_name failed");
            }
        }
        return new Attribute(id);
    }

    private Attribute(final int id) {
        super(id, CLOSE_ACTION);
    }

    @Override
    public String getName() {
        NativeLong size = H5Library.INSTANCE.H5Aget_name(getId(), new NativeLong(0), null);
        if (size.longValue() < 0) {
            throw new H5AttributeException("H5Aget_name failed");
        }
        byte[] buf = new byte[size.intValue() + 1];
        NativeLong err = H5Library.INSTANCE.H5Aget_name(getId(), new NativeLong(buf.length), buf);
        if (err.longValue() < 0) {
            throw new H5AttributeException("H5Aget_name failed");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(super.getName());
        builder.append("/");
        builder.append(new String(Arrays.copyOf(buf, buf.length - 1), Charset.forName("US-ASCII")));
        return builder.toString();
    }

    @Override
    public DataSpace getSpace() {
        final int dataspaceId;
        synchronized (H5Library.INSTANCE) {
            dataspaceId = H5Library.INSTANCE.H5Aget_space(getId());
            if (dataspaceId < 0) {
                throw new H5AttributeException("H5Aget_space failed");
            }
        }
        return new DataSpace(dataspaceId, true);
    }

    @Override
    public long getStorageSize() {
        throw new NotImplementedException();
    }

    @Override
    public DataType getType() {
        final int typeId;
        synchronized (H5Library.INSTANCE) {
            typeId = H5Library.INSTANCE.H5Aget_type(getId());
            if (typeId < 0) {
                throw new H5AttributeException("H5Aget_type failed");
            }
        }
        return DataType.createTypeFromId(typeId);
    }

    @Override
    public void read(final Buffer buf, final DataType memType) {
        checkBuffer(buf, memType);
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Aread(getId(), memType.getId(), buf);
            if (err < 0) {
                throw new H5AttributeException("H5Aread failed");
            }
        }
    }

    public void read(final byte[] arr, final DataType memType) {
        read(ByteBuffer.wrap(arr), memType);
    }

    @Override
    public String toString() {
        if (isOpen()) {
            return "Attribute[name=" + getName() + "]";
        } else {
            return "Attribute[invalid]";
        }
    }

    @Override
    public void write(final Buffer buf, final DataType memType) {
        checkBuffer(buf, memType);
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Awrite(getId(), memType.getId(), buf);
            if (err < 0) {
                throw new H5AttributeException("H5Awrite failed");
            }
        }
    }

    public void write(final byte[] arr, final DataType memType) {
        write(ByteBuffer.wrap(arr), memType);
    }
}
