package ru.nsu.sidey383.lab1.model.file;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.base.*;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Optional;

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
    @NotNull
    Optional<ParentDUFile> getParent();

    /**
     * Изменяет только состоянние данного файла.
     * <p> Не влияет на состояние родительской директории.
     *
     * @return null или старая родительская директория.
     */
    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    Optional<ParentDUFile> setParent(ParentDUFile file);

    @NotNull
    DUFileType getFileType();

    @NotNull
    Path getPath();

    /**
     * @return simple name of file
     **/
    @NotNull
    default String getSimpleName() {
        Path fileName = getPath().getFileName();
        // getFileName() will return null for root of file system, check this
        return Objects.requireNonNullElseGet(fileName, this::getPath).toString();
    }

    /**
     * Фабричный метод для создания {@link DUFile}.
     * <p> Перед созданием объекта разрешает путь до файла, а для ссылок переходит по ссылке с помощью {@link Path#toRealPath(LinkOption...)}.
     * <p> Все ошибки создания отражаются в созданном файле {@link ExceptionDUFile}
     *
     * @see Path#toRealPath(LinkOption...)
     * @see Files#readAttributes(Path, Class, LinkOption...)
     */
    static DUFile readFile(Path path) {
        LinkOption[] linkOptions = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
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
                try {
                    yield new LinkDUFile(0, originalPath, Files.readSymbolicLink(originalPath));
                } catch (IOException e) {
                    yield new WrongDUFile(0, originalPath, new DUPathException(originalPath, e));
                }
            }
            case OTHER -> new OtherDUFile(0, originalPath);
        };
    }

    /**
     * When path contain weak links {@link Path#toRealPath(LinkOption...)} with {@link LinkOption#NOFOLLOW_LINKS} can produce {@link NotDirectoryException}.
     * <p> In this case use {@link Path#toRealPath(LinkOption...)} without parameters.
     *
     * @return real path if than possible
     * @throws IOException see exceptions in {@link Path#toRealPath(LinkOption...)}
     **/
    private static Path getRealPath(Path path, LinkOption... linkOptions) throws IOException {
        try {
            return path.toRealPath(linkOptions);
        } catch (NotDirectoryException e1) {
            return path.toRealPath();
        }
    }

}
