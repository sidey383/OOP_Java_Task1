package ru.nsu.sidey383.lab1.model.file.exception;

import java.nio.file.FileSystemException;
import java.nio.file.Path;

public class PathFileSystemException extends PathException {

    public PathFileSystemException(Path path, FileSystemException e) {
        super(path, "File system operation fails", e);
    }

}
