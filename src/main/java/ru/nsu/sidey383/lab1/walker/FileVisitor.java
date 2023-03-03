package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;

import java.io.IOException;
import java.nio.file.Path;

public interface FileVisitor {

    /**
     * Call on visiting a file
     * **/
    void visitFile(File file);

    /**
     * Visit the directory before going through the list of its files
     * @return action for this directory. When action is {@link NextAction#CONTINUE} will visit children,
     * else ignore them and don't call {@link FileVisitor#postVisitDirectory(DirectoryFile)} for this directory.
     * **/
    NextAction preVisitDirectory(DirectoryFile directory);

    /**
     * Visit the directory after going through the list of its files
     * **/
    void postVisitDirectory(DirectoryFile directory);

    /**
     * Called when there is an error getting the file metadata
     **/
    void pathVisitError(@Nullable Path path, @NotNull IOException e);

}
