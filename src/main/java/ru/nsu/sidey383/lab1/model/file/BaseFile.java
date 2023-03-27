package ru.nsu.sidey383.lab1.model.file;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * Базовая реализация {@link File}.
 */
public abstract class BaseFile implements File {

    protected ParentFile parent;

    protected Path path;

    protected long size;

    protected BaseFile(long size, Path path) {
        this.parent = null;
        this.size = size;
        this.path = path;
    }

    public ParentFile getParent() {
        return parent;
    }

    @Nullable
    public ParentFile setParent(ParentFile parent) {
        ParentFile p = this.parent;
        this.parent = parent;
        return p;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof File file) && getPath().equals(file.getPath());
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
