package ru.nsu.sidey383.lab1.model.file;

import java.util.Set;

public interface DirectoryFile extends File {

    Set<File> getChildren();

    void addChild(File file);

}
