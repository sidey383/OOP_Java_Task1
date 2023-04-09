package ru.nsu.sidey383.lab1.options;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

// CR(minor): we leak information about tree build and printing to options level.
// also would be hard to maintain if we have too many different stages
public interface FileTreeOptions {

    boolean followLink();

    @NotNull
    Path getFilePath();

}
