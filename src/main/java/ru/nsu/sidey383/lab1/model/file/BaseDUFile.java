package ru.nsu.sidey383.lab1.model.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

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

    public @NotNull Optional<ParentDUFile> getParent() {
        return Optional.ofNullable(parent);
    }

    @NotNull
    public Optional<ParentDUFile> setParent(ParentDUFile parent) {
        Optional<ParentDUFile> p = Optional.ofNullable(this.parent);
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
                "size=" + getSize() +
                ", path= " + getPath() +
                ", parent=" + getParent().map(ParentDUFile::getPath).map(Object::toString).orElse("") +
                '}';
    }

    @Override
    public @NotNull Path getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
