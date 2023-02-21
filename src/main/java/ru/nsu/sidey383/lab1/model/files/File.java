package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.utils.SizeSuffix;
import ru.nsu.sidey383.lab1.walker.FileVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public abstract sealed class File permits DirectoryFile, RegularFile, OtherFile {

    protected final Path path;

    protected final BasicFileAttributes attributes;

    protected final Long actualSize;

    protected long size;

    protected DirectoryFile parent;

    File(Path path, BasicFileAttributes attributes) {
        Path path_;
        try {
            path_ = path.toRealPath();
        } catch (IOException e) {
            path_ = path.toAbsolutePath();
        }
        this.actualSize = attributes.size();
        this.path = path_;
        this.attributes = attributes;
        this.size = this.actualSize;
    }

    public long getSize() {
        return size;
    }

    public long getActualSize() {
        return actualSize;
    }

    public Path getPath() {
        return path;
    }

    public DirectoryFile getParent() {
        return parent;
    }

    public DirectoryFile setParent(DirectoryFile parent) {
        DirectoryFile p = this.parent;
        this.parent = parent;
        return p;
    }

    /**
     * @return true when it is the same file type, and they have the same paths
     * **/
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof File file) && getClass() == file.getClass() && file.getPath().equals(getPath());
    }

    public static File createFile(Path path, boolean useRealPath, LinkOption[] linkOptions) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, linkOptions);
        if (useRealPath) {
            try {
                path = path.toRealPath(linkOptions);
            } catch (IOException e1) {
                try {
                    path = path.toRealPath();
                } catch (IOException ignored) {}
            }
        }
        if (attributes.isSymbolicLink()) {
            if (attributes.isDirectory()) {
                return new DirectoryLinkFile(path, attributes);
            } else if (attributes.isRegularFile()) {
                return new RegularLinkFile(path, attributes);
            } else {
                return new OtherLinkFile(path, attributes);
            }
        } else {
            if (attributes.isDirectory()) {
                return new DirectoryFile(path, attributes);
            } else if (attributes.isRegularFile()) {
                return new RegularFile(path, attributes);
            } else {
                return new OtherFile(path, attributes);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", path.getFileName(), SizeSuffix.BYTE.getSuffix(getSize()));
    }
}
