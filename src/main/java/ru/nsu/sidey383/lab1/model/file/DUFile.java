package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.base.DirectoryDUFile;
import ru.nsu.sidey383.lab1.model.file.base.OtherDUFile;
import ru.nsu.sidey383.lab1.model.file.base.RegularDUFile;
import ru.nsu.sidey383.lab1.model.file.base.WrongDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;
import ru.nsu.sidey383.lab1.model.file.link.ExceptionLinkDUFile;
import ru.nsu.sidey383.lab1.model.file.link.ParentLinkDUFile;
import ru.nsu.sidey383.lab1.model.file.link.SimpleLinkDUFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Базовый инетрфейс файла.
 */
public interface DUFile {

    long getSize();

    /**
     * Получить родительский файл.
     * <p> Нет гарантий, что данный файл является потомком возвращаемой директории.
     *
     * @see ParentDUFile#getChildren()
     */
    ParentDUFile getParent();

    /**
     * Изменяет только состоянние данного файла.
     * <p> Не влияет на состояние родительской директории.
     *
     * @return null или старая родительская директория.
     */
    @SuppressWarnings("UnusedReturnValue")
    ParentDUFile setParent(ParentDUFile file);

    DUFileType getFileType();

    Path getPath();

    /**
     * Фабричный метод для создания {@link DUFile}.
     * <p> Перед созданием объекта разрешает путь до файла, а для ссылок переходит по ссылке с помощью {@link Path#toRealPath(LinkOption...)}.
     * <p> Все ошибки создания отражаются в созданном файле {@link ExceptionDUFile}
     *
     * @see Path#toRealPath(LinkOption...)
     * @see Files#readAttributes(Path, Class, LinkOption...)
     */
    static DUFile readFile(Path path) {
        return readFile(path, false);
    }

    private static DUFile readFile(Path path, boolean resolveLink) {
        LinkOption[] linkOptions = resolveLink ? new LinkOption[0] : new LinkOption[] {LinkOption.NOFOLLOW_LINKS};
        Path originalPath;

        try {
            originalPath = path.toRealPath(linkOptions);
        } catch (NotDirectoryException e1) {
            try {
                originalPath = path.toRealPath();
            } catch (IOException e2) {
                return new WrongDUFile(0, path, new DUPathException(path, e2));
            }
        } catch (IOException e) {
            return new WrongDUFile(0, path, new DUPathException(path, e));
        }

        BasicFileAttributes originalAttributes;

        try {
            originalAttributes = Files.readAttributes(originalPath, BasicFileAttributes.class, linkOptions);
        } catch (IOException e) {
            return new WrongDUFile(0, originalPath, new DUPathException(path, e));
        }

        long originalSize = originalAttributes.size();
        DUFileType originalType = DUFileType.toSimpleType(originalAttributes);

        if (originalSize < 0)
            originalSize = 0;

        return switch (originalType) {
            case REGULAR_FILE -> new RegularDUFile(originalSize, originalPath);
            case DIRECTORY -> new DirectoryDUFile(originalSize, originalPath);
            case OTHER_LINK -> {
                if (resolveLink) {
                    yield new WrongDUFile(originalSize, originalPath, new DUPathException(path, new IllegalStateException("Resoled file has link type")));
                } else {
                    DUFile resolvedFile = readFile(originalPath, true);
                    if (resolvedFile instanceof ParentDUFile resolvedParent) {
                        yield new ParentLinkDUFile(originalSize, originalPath, resolvedParent);
                    }
                    if (resolvedFile instanceof WrongDUFile resolvedWrongFile) {
                        yield new ExceptionLinkDUFile(originalSize, originalPath, resolvedWrongFile);
                    }
                    yield new SimpleLinkDUFile(originalSize, originalPath, readFile(originalPath, true));
                }

            }
            case OTHER -> new OtherDUFile(originalSize, originalPath);
            default -> new WrongDUFile(originalSize, originalPath, new DUPathException(path, new IllegalStateException("Incorrect file type")));
        };
    }

}
