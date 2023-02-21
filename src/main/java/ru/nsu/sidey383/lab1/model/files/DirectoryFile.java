package ru.nsu.sidey383.lab1.model.files;

import java.util.List;

public interface DirectoryFile extends File {

    List<File> getChildren();

    void addChild(File file);

}
