package ru.nsu.sidey383.lab1.model.file.base;

import ru.nsu.sidey383.lab1.model.file.ExceptionDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

public class WrongDUFile extends OtherDUFile implements ExceptionDUFile {

    private final DUPathException exception;

    public WrongDUFile(long size, Path path, DUPathException exception) {
        super(size, path);
        this.exception = exception;
    }

    @Override
    public DUPathException getPathException() {
        return exception;
    }
}
