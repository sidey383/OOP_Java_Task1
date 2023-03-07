package ru.nsu.sidey383.lab1.model.file.exception;

import java.nio.file.Path;

public class PathUnsupportedOperationException extends PathException {

    public PathUnsupportedOperationException(Path path, UnsupportedOperationException e) {
        super(path, "Unable to read file attributes", e);
    }

}
