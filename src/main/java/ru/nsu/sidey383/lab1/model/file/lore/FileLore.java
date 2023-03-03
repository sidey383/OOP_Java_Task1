package ru.nsu.sidey383.lab1.model.file.lore;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.FileType;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


/**
 * Additional {@link ru.nsu.sidey383.lab1.model.file.File} data
 * **/
public interface FileLore {


    long originalSize();

    @NotNull
    Path originalPath();

    long resolvedSize();

    @NotNull
    Path resolvedPath();

    @NotNull
    FileType fileType();

    /**
     * Factory method for creating {@link FileLore}.
     * <p> Before creating {@link FileLore} resolve path to file by {@link Path#toRealPath(LinkOption...)}
     * @throws SecurityException when don't have permission to resolve path or read file size
     * @throws IOException if file doesn't exist or an I/O error occurs
     * @see Path#toRealPath(LinkOption...)
     * @see Files#readAttributes(Path, Class, LinkOption...)
     * @see Files#size(Path)
     * **/
    static FileLore createFileLore(@NotNull Path path) throws IOException {
        Path originalPath;
        FileType originalType;
        long originalSize;

        try {
            originalPath = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (NotDirectoryException e1) {
            originalPath = path.toRealPath();
        }

        try {
            BasicFileAttributes originalAttributes = Files.readAttributes(originalPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            originalSize = originalAttributes.size();
            originalType = FileType.toSimpleType(originalAttributes);
        } catch (UnsupportedOperationException | FileSystemException e) {
            originalSize = Files.size(originalPath);
            originalType = FileType.UNDEFINED;
        }
        if (originalSize < 0)
            originalSize = 0;

        if (originalType.isLink()) {
            Path resolvedPath;
            FileType resolvedType;
            long resolvedSize;

            resolvedPath = path.toRealPath();

            BasicFileAttributes originalAttributes = Files.readAttributes(resolvedPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            resolvedSize = originalAttributes.size();
            resolvedType = FileType.toSimpleType(originalAttributes);

            if (resolvedSize < 0)
                resolvedSize = 0;

            return new DefaultFileLore(resolvedType == FileType.UNDEFINED ? FileType.UNDEFINED_LINK : resolvedType.toLink(), originalPath, originalSize, resolvedPath, resolvedSize);
        } else {
            return new DefaultFileLore(originalType, originalPath, originalSize, originalPath, originalSize);
        }
    }

    /**
     * Must generate only by path of file
     * **/
    @Override
    int hashCode();

    /**
     * Must compare only by path of file
     * **/
    @Override
    boolean equals(Object obj);

}
