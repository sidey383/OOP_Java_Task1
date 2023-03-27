package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

public interface ExceptionFile extends File {

    DUPathException getPathException();

}
