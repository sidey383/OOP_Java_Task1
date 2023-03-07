package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.exception.PathSecurityException;
import ru.nsu.sidey383.lab1.model.file.exception.PathUnsupportedOperationException;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;
import ru.nsu.sidey383.lab1.model.file.lore.FileLore;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Базовый инетрфейс файла
 * **/
public interface File {

    FileLore getFileLore();

    long getSize();

    /**
     * Получить родительский файл.
     * Нет гарантий, что данный файл является потомком возвращаемой директории
     * @see DirectoryFile#getChildren()
     * **/
    DirectoryFile getParent();

    /**
     * Изменяет только состоянние данного файла
     * Не влияет на состояние родительской директории
     * @return null или старая родительская директория
     * **/
    @SuppressWarnings("UnusedReturnValue")
    DirectoryFile setParent(DirectoryFile file);

    default FileType getFileType() {
        return getFileLore().fileType();
    }

    default Path getOriginalPath() {
        return getFileLore().originalPath();
    }

    default Path getResolvedPath() {
        return getFileLore().resolvedPath();
    }

    default long getOriginalSize() {
        return getFileLore().originalSize();
    }

    default long getResolvedSize() {
        return getFileLore().resolvedSize();
    }

    /**
     * Фабричный метод для создания {@link File}
     * @throws PathUnsupportedOperationException если невозможно получить атрибуты файла
     * @throws PathSecurityException если нет прав на работу с данным файлом
     * @throws IOException если файл не существует или в случае I/O exception
     * @see FileLore#createFileLore(Path)
     * **/
    static File readFile(Path path) throws PathException, IOException {
        FileLore lore = FileLore.createFileLore(path);
        return switch (lore.fileType()) {
            case DIRECTORY -> new DefaultDirectoryFile(lore);
            case DIRECTORY_LINK -> new DefaultLinkDirectoryFile(lore);
            case REGULAR_FILE, REGULAR_FILE_LINK, OTHER, OTHER_LINK, UNDEFINED, UNDEFINED_LINK  -> new DefaultFile(lore);
        };
    }

}
