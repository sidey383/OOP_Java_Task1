package ru.nsu.sidey383.lab1.model;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.utils.SizeSuffix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.stream.Stream;

public abstract sealed class File permits RegularFile, DirectoryFile, SymLinkFile, OtherFile {

    protected Long size;

    protected final Path path;

    protected FileStatus status = FileStatus.OK;

    protected File(@NotNull Path path) {
        this.path = path;
        try {
            size = Files.size(path);
        } catch (SecurityException e) {
            this.status = FileStatus.SECURITY_EXCEPTION;
        } catch (IOException e) {
            this.status = FileStatus.IO_EXCEPTION;
        }
    }

    @NotNull
    public Long getSize() {
        return size;
    }

    @NotNull
    public Path getPath() {
        return path;
    }

    @NotNull
    public FileStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof File file) && file.getClass() == this.getClass() && file.getPath().equals(this.getPath());
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", path.getFileName(), SizeSuffix.BYTE.getSuffix(getSize()));
    }

}
