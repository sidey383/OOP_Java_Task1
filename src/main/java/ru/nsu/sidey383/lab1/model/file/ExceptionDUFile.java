package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

public interface ExceptionDUFile extends DUFile {

    DUPathException getPathException();

}
