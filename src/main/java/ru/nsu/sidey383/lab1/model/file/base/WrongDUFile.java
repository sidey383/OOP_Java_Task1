package ru.nsu.sidey383.lab1.model.file.base;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.ExceptionDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

public class WrongDUFile extends OtherDUFile implements ExceptionDUFile {

    private final DUPathException exception;

    public WrongDUFile(long size, @NotNull Path path, @NotNull DUPathException exception) {
        super(size, path);
        this.exception = exception;
    }

    @Override
    public @NotNull DUPathException getPathException() {
        return exception;
    }
}
