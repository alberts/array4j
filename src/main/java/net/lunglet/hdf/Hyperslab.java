package net.lunglet.hdf;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class Hyperslab {
    private final Point end;

    private final Point start;

    Hyperslab(final Point start, final Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Hyperslab)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Hyperslab other = (Hyperslab) obj;
        return new EqualsBuilder().append(start, other.start).append(end, other.end).isEquals();
    }

    public Point getEndPoint() {
        return end;
    }

    public Point getStartPoint() {
        return start;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(start).append(end).toHashCode();
    }

    @Override
    public String toString() {
        return "Hyperslab[start=" + start + ", end=" + end + "]";
    }
}
