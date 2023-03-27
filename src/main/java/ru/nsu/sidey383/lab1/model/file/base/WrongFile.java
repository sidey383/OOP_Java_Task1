package ru.nsu.sidey383.lab1.model.file.base;

import ru.nsu.sidey383.lab1.model.file.ExceptionFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

public class WrongFile extends OtherFile implements ExceptionFile {

    private final DUPathException exception;

    public WrongFile(long size, Path path, DUPathException exception) {
        super(size, path);
        this.exception = exception;
    }

    @Override
    public DUPathException getPathException() {
        return exception;
    }
}
