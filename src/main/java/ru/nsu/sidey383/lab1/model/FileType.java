package ru.nsu.sidey383.lab1.model;

import java.nio.file.attribute.BasicFileAttributes;

public enum FileType {
    DIRECTORY(false, true),
    REGULAR_FILE(false, false),
    OTHER(false, false),
    UNDEFINED(false, false),
    DIRECTORY_LINK(true, true),
    REGULAR_FILE_LINK(true, false),
    OTHER_LINK(true, false),
    UNDEFINED_LINK(true, false);

    private final boolean isLink;

    private final boolean isDirectory;

    FileType(boolean isLink, boolean isDirectory) {
        this.isLink = isLink;
        this.isDirectory = isDirectory;
    }

    public boolean isLink() {
        return isLink;
    }

    public boolean isDirectory() {return isDirectory;}

    public static FileType toSimpleType(BasicFileAttributes attributes) {
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
            return UNDEFINED_LINK;
        }
        return UNDEFINED;
    }

    /**
     * Convert simple {@link FileType} in link on this type <br>
     * Saves links and {@link FileType#UNDEFINED}
     */
    public FileType toLink() {
        return switch (this) {
            case DIRECTORY -> DIRECTORY_LINK;
            case REGULAR_FILE -> REGULAR_FILE_LINK;
            case OTHER -> OTHER_LINK;
            default -> this;
        };
    }

    /**
     * Convert link {@link FileType} in simple type <br>
     * Saves simple types <br>
     */
    public FileType toReal() {
        return switch (this) {
            case DIRECTORY_LINK -> DIRECTORY;
            case REGULAR_FILE_LINK -> REGULAR_FILE;
            case OTHER_LINK -> OTHER;
            case UNDEFINED_LINK -> UNDEFINED;
            default -> this;
        };
    }

}
