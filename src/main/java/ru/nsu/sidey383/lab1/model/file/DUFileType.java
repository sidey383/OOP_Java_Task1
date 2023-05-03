package ru.nsu.sidey383.lab1.model.file;

import java.nio.file.attribute.BasicFileAttributes;

public enum DUFileType {
    DIRECTORY, REGULAR_FILE, OTHER, LINK;


    /**
     * Возвращает тип файла исходя из состояния {@link BasicFileAttributes}.
     */
    public static DUFileType toSimpleType(BasicFileAttributes attributes) {
        if (attributes.isRegularFile()) {
            return REGULAR_FILE;
        }
        if (attributes.isDirectory()) {
            return DIRECTORY;
        }
        if (attributes.isOther()) {
            return OTHER;
        }
        if (attributes.isSymbolicLink()) {
            return LINK;
        }
        return OTHER;
    }

}
