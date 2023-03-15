package ru.nsu.sidey383.lab1.options;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface FileTreeOptions {

    boolean followLink();

    @NotNull
    Path getFilePath();

}
