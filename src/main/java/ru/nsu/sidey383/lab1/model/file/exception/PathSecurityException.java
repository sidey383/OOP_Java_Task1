package ru.nsu.sidey383.lab1.model.file.exception;

import java.nio.file.Path;

public class PathSecurityException extends PathException {

    public PathSecurityException(Path path, SecurityException e) {
        super(path, "Not enough rights to find out the file size", e);
    }

}
