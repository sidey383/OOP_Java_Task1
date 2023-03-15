package ru.nsu.sidey383.lab1.model.file.exception;

import java.io.IOException;
import java.nio.file.Path;

public class PathFileStreamException extends PathException {

    public PathFileStreamException(Path path, IOException cause) {
        super(path, "Can't open stream of files for this directory", cause);
    }

}
