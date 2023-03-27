package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.base.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.base.OtherFile;
import ru.nsu.sidey383.lab1.model.file.base.RegularFile;
import ru.nsu.sidey383.lab1.model.file.base.WrongFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;
import ru.nsu.sidey383.lab1.model.file.link.ExceptionLinkFile;
import ru.nsu.sidey383.lab1.model.file.link.ParentLinkFile;
import ru.nsu.sidey383.lab1.model.file.link.SimpleLinkFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Базовый инетрфейс файла.
 */
public interface File {

    long getSize();

    /**
     * Получить родительский файл.
     * <p> Нет гарантий, что данный файл является потомком возвращаемой директории.
     *
     * @see ParentFile#getChildren()
     */
    ParentFile getParent();

    /**
     * Изменяет только состоянние данного файла.
     * <p> Не влияет на состояние родительской директории.
     *
     * @return null или старая родительская директория.
     */
    @SuppressWarnings("UnusedReturnValue")
    ParentFile setParent(ParentFile file);

    FileType getFileType();

    Path getPath();

    /**
     * Фабричный метод для создания {@link File}.
     * <p> Перед созданием объекта разрешает путь до файла, а для ссылок переходит по ссылке с помощью {@link Path#toRealPath(LinkOption...)}.
     * <p> Все ошибки создания отражаются в созданном файле {@link ExceptionFile}
     *
     * @see Path#toRealPath(LinkOption...)
     * @see Files#readAttributes(Path, Class, LinkOption...)
     */
    static File readFile(Path path) {
        return readFile(path, false);
    }

    static File readFile(Path path, boolean resolveLink) {
        LinkOption[] linkOptions = resolveLink ? new LinkOption[0] : new LinkOption[] {LinkOption.NOFOLLOW_LINKS};
        Path originalPath;

        try {
            originalPath = path.toRealPath(linkOptions);
        } catch (NotDirectoryException e1) {
            try {
                originalPath = path.toRealPath();
            } catch (IOException e2) {
                return new WrongFile(0, path, new DUPathException(path, e2));
            }
        } catch (IOException e) {
            return new WrongFile(0, path, new DUPathException(path, e));
        }

        BasicFileAttributes originalAttributes;

        try {
            originalAttributes = Files.readAttributes(originalPath, BasicFileAttributes.class, linkOptions);
        } catch (IOException e) {
            return new WrongFile(0, originalPath, new DUPathException(path, e));
        }

        long originalSize = originalAttributes.size();
        FileType originalType = FileType.toSimpleType(originalAttributes);

        if (originalSize < 0)
            originalSize = 0;

        return switch (originalType) {
            case REGULAR_FILE -> new RegularFile(originalSize, originalPath);
            case DIRECTORY -> new DirectoryFile(originalSize, originalPath);
            case OTHER_LINK -> {
                if (resolveLink) {
                    yield new WrongFile(originalSize, originalPath, new DUPathException(path, new IllegalStateException("Resoled file has link type")));
                } else {
                    File reslovedFile = readFile(originalPath, true);
                    if (reslovedFile instanceof ParentFile resolvedParent) {
                        yield  new ParentLinkFile(originalSize, originalPath, resolvedParent);
                    }
                    if (reslovedFile instanceof WrongFile resolvedWrongFile) {
                        yield new ExceptionLinkFile(originalSize, originalPath, resolvedWrongFile);
                    }
                    yield new SimpleLinkFile(originalSize, originalPath, readFile(originalPath, true));
                }

            }
            case OTHER -> new OtherFile(originalSize, originalPath);
            default -> new WrongFile(originalSize, originalPath, new DUPathException(path, new IllegalStateException("Incorrect file type")));
        };
    }

}
