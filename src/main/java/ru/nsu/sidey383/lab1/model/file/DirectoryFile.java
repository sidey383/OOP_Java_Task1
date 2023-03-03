package ru.nsu.sidey383.lab1.model.file;

import java.util.Set;

/**
 * Extends of File interface for working with child files
 * **/
public interface DirectoryFile extends File {

    /**
     * @return copy of set of children files
     * **/
    Set<File> getChildren();

    /**
     * Add children for this file. Does not add a copies of file.
     * <p> No effect on {@link File#getParent()}
     * **/
    void addChild(File file);

}
