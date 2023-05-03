package ru.nsu.sidey383.lab1.model.file;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.base.*;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

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
     * @return simple name of file
     * **/
    @NotNull
    default String getSimpleName() {
        Path fileName = getPath().getFileName();
        // getFileName() will return null for root of file system, check this
        if (fileName == null) {
            return getPath().toString();
        } else {
            return fileName.toString();
        }
    }

    /**
     * Фабричный метод для создания {@link DUFile}.
     * <p> Перед созданием объекта разрешает путь до файла, а для ссылок переходит по ссылке с помощью {@link Path#toRealPath(LinkOption...)}.
     * <p> Все ошибки создания отражаются в созданном файле {@link ExceptionDUFile}
     *
     * @see Path#toRealPath(LinkOption...)
     * @see Files#readAttributes(Path, Class, LinkOption...)
     */
    public static DUFile readFile(Path path) {
        return readFile(path, false);
    }

    public static DUFile readFile(Path path, boolean resolveLink) {
        LinkOption[] linkOptions = resolveLink ? new LinkOption[0] : new LinkOption[] {LinkOption.NOFOLLOW_LINKS};
        Path originalPath;
        try {
            originalPath = getRealPath(path, linkOptions);
        } catch (IOException e) {
            return new WrongDUFile(0, path, new DUPathException(path, e));
        }

        BasicFileAttributes originalAttributes;

        try {
            originalAttributes = Files.readAttributes(originalPath, BasicFileAttributes.class, linkOptions);
        } catch (IOException e) {
            return new WrongDUFile(0, originalPath, new DUPathException(path, e));
        }

        DUFileType fileType = DUFileType.toSimpleType(originalAttributes);

        return switch (fileType) {
            case REGULAR_FILE -> new RegularDUFile(originalAttributes.size(), originalPath);
            case DIRECTORY -> new DirectoryDUFile(0, originalPath);
            case LINK -> {
                if (resolveLink) {
                    yield new WrongDUFile(0, originalPath, new DUPathException(path, new IllegalStateException("Resoled file has link type")));
                    // CR: also strange idea to create and carry exceptions around, can't we move this method to DUSystemFileWalker and add exceptions to exception list?
                } else {
                    try {
                        yield new LinkDUFile(0, originalPath, getRealPath(originalPath));
                    }  catch (IOException e) {
                        yield new WrongDUFile(0, originalPath, new DUPathException(originalPath ,e));
                    }
                }

            }
            case OTHER -> new OtherDUFile(0, originalPath);
            default -> new WrongDUFile(0, originalPath, new DUPathException(path, new IllegalStateException("Incorrect file type")));
        };
    }

    /**
     * When path contain weak links {@link Path#toRealPath(LinkOption...)} with {@link LinkOption#NOFOLLOW_LINKS} can produce {@link NotDirectoryException}.
     * <p> In this case use {@link Path#toRealPath(LinkOption...)} without parameters.
     *
     * @throws IOException see exceptions in {@link Path#toRealPath(LinkOption...)}
     *
     * @return real path if than possible
     * **/
    private static Path getRealPath(Path path, LinkOption... linkOptions) throws IOException {
        try {
            return path.toRealPath(linkOptions);
        } catch (NotDirectoryException e1) {
            return path.toRealPath();
        }
    }

}
