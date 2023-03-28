package ru.nsu.sidey383.lab1.model.file;

public interface LinkDUFile<T extends DUFile> extends DUFile {

    T getLinkedFile();

    T updateLinkedFile(DUFile file);

}
