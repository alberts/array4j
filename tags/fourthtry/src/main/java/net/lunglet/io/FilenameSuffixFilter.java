package net.lunglet.io;

import java.io.File;
import java.io.FilenameFilter;

public final class FilenameSuffixFilter implements FilenameFilter {
    private final String suffix;

    private final boolean ignoreCase;

    public FilenameSuffixFilter(final String suffix) {
        this(suffix, false);
    }

    public FilenameSuffixFilter(final String suffix, final boolean ignoreCase) {
        this.suffix = suffix;
        this.ignoreCase = ignoreCase;
    }

    public boolean accept(final File dir, final String name) {
        File file = new File(dir, name);
        if (ignoreCase) {
            return file.isFile() && file.getName().toLowerCase().endsWith(suffix.toLowerCase());
        } else {
            return file.isFile() && file.getName().endsWith(suffix);
        }
    }
}
