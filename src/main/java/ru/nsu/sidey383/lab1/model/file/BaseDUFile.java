package ru.nsu.sidey383.lab1.model.file;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * Базовая реализация {@link DUFile}.
 */
public abstract class BaseDUFile implements DUFile {

    protected ParentDUFile parent;

    protected Path path;

    protected long size;

    protected BaseDUFile(long size, Path path) {
        this.parent = null;
        this.size = size;
        this.path = path;
    }

    public ParentDUFile getParent() {
        return parent;
    }

    @Nullable
    public ParentDUFile setParent(ParentDUFile parent) {
        ParentDUFile p = this.parent;
        this.parent = parent;
        return p;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DUFile file) && getPath().equals(file.getPath());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "size=" + size +
                ", path= " + path +
                ", parent=" + (parent == null ? null : parent.getPath()) +
                '}';
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
