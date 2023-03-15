package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.exception.PathFileSystemException;
import ru.nsu.sidey383.lab1.model.file.exception.PathSecurityException;
import ru.nsu.sidey383.lab1.model.file.exception.PathUnsupportedOperationException;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;
import ru.nsu.sidey383.lab1.model.file.lore.FileLore;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 * Базовый инетрфейс файла.
 */
public interface File {

    FileLore getFileLore();

    long getSize();

    /**
     * Получить родительский файл.
     * <p> Нет гарантий, что данный файл является потомком возвращаемой директории.
     *
     * @see DirectoryFile#getChildren()
     */
    DirectoryFile getParent();

    /**
     * Изменяет только состоянние данного файла.
     * <p> Не влияет на состояние родительской директории.
     *
     * @return null или старая родительская директория.
     */
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
     * Фабричный метод для создания {@link File}.
     * <p> Перед созданием объекта разрешает путь до файла, а для ссылок переходит по ссылке с помощью {@link Path#toRealPath(LinkOption...)}.
     *
     * @throws PathUnsupportedOperationException если невозможно получить атрибуты файла.
     * @throws PathSecurityException если нет прав на работу с данным файлом.
     * @throws PathFileSystemException при ошибке файловой системы.
     * @throws IOException если файл не существует или в случае I/O exception.
     *
     * @see FileLore#createFileLore(Path)
     */
    static File readFile(Path path) throws PathException, IOException {
        FileLore lore = FileLore.createFileLore(path);
        return switch (lore.fileType()) {
            case DIRECTORY -> new DefaultDirectoryFile(lore);
            case DIRECTORY_LINK -> new DefaultLinkDirectoryFile(lore);
            case REGULAR_FILE, REGULAR_FILE_LINK, OTHER, OTHER_LINK, UNDEFINED, UNDEFINED_LINK  -> new DefaultFile(lore);
        };
    }

}
