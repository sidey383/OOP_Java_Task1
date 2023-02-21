package ru.nsu.sidey383.lab1.walker;

import ru.nsu.sidey383.lab1.model.files.DirectoryFile;
import ru.nsu.sidey383.lab1.model.files.File;

import java.io.IOException;
import java.nio.file.Path;

public interface FileVisitor {

    /**
     * Visit not directory file
     * **/
    void visitFile(File file);

    /**
    * Visiting the directory before going through the list of its files
    * **/
    NextAction preVisitDirectory(DirectoryFile directory);

    /**
     * Visiting the directory after going through the list of its files
     * **/
    void postVisitDirectory(DirectoryFile directory);

    /**
     * Called when there is an error getting the file metadata
     * **/
    NextAction pathVisitError(Path path, IOException e);

    NextAction realPathError(Path path, IOException e);

}
