package ru.nsu.sidey383.lab1.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

public enum FileType {
    DIRECTORY, REGULAR, SYM_LINK, OTHER;

    @NotNull
    public static FileType getFileType(Path path) {
        if (Files.isRegularFile(path)) {
            return FileType.REGULAR;
        } else if (Files.isDirectory(path)) {
            return FileType.DIRECTORY;
        } else if (Files.isSymbolicLink(path)) {
            return FileType.SYM_LINK;
        }
        return FileType.OTHER;
    }
}
