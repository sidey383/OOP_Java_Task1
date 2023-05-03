package ru.nsu.sidey383.lab1.model.file;

import java.nio.file.Path;

public interface ReferenceDUFile extends ParentDUFile {

    Path getReference();

    void freeChild();


}
