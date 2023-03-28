package ru.nsu.sidey383.lab1.model.file;

import java.nio.file.attribute.BasicFileAttributes;

public enum DUFileType {
    DIRECTORY(false, true),
    REGULAR_FILE(false, false),
    DIRECTORY_LINK(true, true),
    REGULAR_FILE_LINK(true, false),
    OTHER(false, false),
    OTHER_LINK(true, false);

    private final boolean isLink;

    private final boolean isDirectory;

    DUFileType(boolean isLink, boolean isDirectory) {
        this.isLink = isLink;
        this.isDirectory = isDirectory;
    }

    public boolean isLink() {
        return isLink;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Возвращает тип файла исходя из состояния {@link BasicFileAttributes}.
     * <p> Не проверяет куда ведет ссылка. Для всех ссылок возвращает {@link DUFileType#OTHER_LINK}.
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
            return OTHER_LINK;
        }
        return OTHER;
    }

    /**
     * Прекращает обычный {@link DUFileType} в его ссылочный аналог.
     */
    public DUFileType toLink() {
        return switch (this) {
            case DIRECTORY -> DIRECTORY_LINK;
            case REGULAR_FILE -> REGULAR_FILE_LINK;
            case OTHER -> OTHER_LINK;
            default -> this;
        };
    }

    /**
     * Превращает ссылочный {@link DUFileType} в обычный вид.
     * <p> сохраняет обычные типы.
     */
    @SuppressWarnings("unused")
    public DUFileType toReal() {
        return switch (this) {
            case DIRECTORY_LINK -> DIRECTORY;
            case REGULAR_FILE_LINK -> REGULAR_FILE;
            case OTHER_LINK -> OTHER;
            default -> this;
        };
    }

}
