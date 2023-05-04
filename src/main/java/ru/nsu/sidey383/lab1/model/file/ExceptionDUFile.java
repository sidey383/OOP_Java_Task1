package ru.nsu.sidey383.lab1.model.file;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

public interface ExceptionDUFile extends DUFile {

    @NotNull
    DUPathException getPathException();

}
