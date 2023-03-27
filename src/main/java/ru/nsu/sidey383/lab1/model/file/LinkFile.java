package ru.nsu.sidey383.lab1.model.file;

public interface LinkFile<T extends File> extends File {

    T getLinkedFile();

    T updateLinkedFile(File file);

}
