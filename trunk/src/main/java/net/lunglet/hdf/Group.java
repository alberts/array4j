package net.lunglet.hdf;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.util.HashSet;
import java.util.Set;
import net.lunglet.hdf.H5Library.H5G_iterate_t;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class Group extends H5Object {
    private static final int H5G_DATASET = 2;

    private static final int H5G_GROUP = 1;

    private static final int H5G_LINK = 0;

    private static final int H5G_TYPE = 3;

    Group(final int id) {
        super(id);
    }

    public void close() {
        // never close the root group
        if (getName().equals("/")) {
            invalidate();
            return;
        }
        int err = H5Library.INSTANCE.H5Gclose(getId());
        if (err < 0) {
            throw new H5GroupException("H5Gclose failed");
        }
        invalidate();
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace) {
        return createDataSet(name, dataType, dataSpace, DataSetCreatePropList.DEFAULT);
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace,
            final DataSetCreatePropList createPlist) {
        // Obtain identifiers for C API
        int typeId = dataType.getId();
        int spaceId = dataSpace.getId();
        int createPlistId = createPlist.getId();

        // Call C routine H5Dcreate to create the named dataset
        int datasetId = H5Library.INSTANCE.H5Dcreate(getId(), name, typeId, spaceId, createPlistId);

        // If the creation of the dataset failed, throw an exception
        if (datasetId < 0) {
            throw new H5GroupException("H5Dcreate failed");
        }

        // No failure, create and return the DataSet object
        return new DataSet(datasetId);
    }

    public DataSet createDataSet(final String name, final DataType dataType, final long... dims) {
        DataSpace space = new DataSpace(dims);
        try {
            DataSet dataset = createDataSet(name, dataType, space);
            return dataset;
        } finally {
            space.close();
        }
    }

    public Group createGroup(final String name) {
        int groupId = H5Library.INSTANCE.H5Gcreate(getId(), name, 0);
        if (groupId < 0) {
            throw new H5GroupException("H5Gcreate failed");
        }
        return new Group(groupId);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Group)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }

    public Set<DataSet> getDataSets() {
        final IntByReference idx = new IntByReference(0);
        final HashSet<DataSet> datasets = new HashSet<DataSet>();
        final H5G_iterate_t operator = new H5G_iterate_t() {
            @Override
            public int callback(final int locId, final String datasetName, final Pointer data) {
                int type = H5Library.INSTANCE.H5Gget_objtype_by_idx(locId, idx.getValue());
                if (type == H5G_DATASET) {
                    datasets.add(openDataSet(datasetName));
                }
                return 0;
            }
        };
        H5Library.INSTANCE.H5Giterate(getId(), ".", idx, operator, null);
        return datasets;
    }

    public Set<Group> getGroups() {
        final IntByReference idx = new IntByReference(0);
        final HashSet<Group> groups = new HashSet<Group>();
        final H5G_iterate_t operator = new H5G_iterate_t() {
            @Override
            public int callback(final int locId, final String groupName, final Pointer data) {
                int type = H5Library.INSTANCE.H5Gget_objtype_by_idx(locId, idx.getValue());
                if (type == H5G_GROUP) {
                    groups.add(openGroup(groupName));
                }
                return 0;
            }
        };
        H5Library.INSTANCE.H5Giterate(getId(), ".", idx, operator, null);
        return groups;
    }

    public DataSet openDataSet(final String name) {
        int datasetId = H5Library.INSTANCE.H5Dopen(getId(), name);
        if (datasetId < 0) {
            throw new H5GroupException("H5Dopen failed");
        }
        return new DataSet(datasetId);
    }

    public Group openGroup(final String name) {
        int groupId = H5Library.INSTANCE.H5Gopen(getId(), name);
        if (groupId < 0) {
            throw new H5GroupException("H5Gopen failed");
        }
        return new Group(groupId);
    }
}
