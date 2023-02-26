package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;

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
    NextAction pathVisitError(@Nullable Path path, @NotNull IOException e);

}
