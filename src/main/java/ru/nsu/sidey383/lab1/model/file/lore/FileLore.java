package ru.nsu.sidey383.lab1.model.file.lore;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.FileType;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public interface FileLore {


    long originalSize();

    @NotNull
    Path originalPath();

    long resolvedSize();

    @NotNull
    Path resolvedPath();

    @NotNull
    FileType fileType();

    static FileLore createFileLore(@NotNull Path path) throws IOException {
        Path originalPath;
        FileType originalType;
        long originalSize;

        try {
            originalPath = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (NotDirectoryException | SecurityException e1) {
            try {
                originalPath = path.toRealPath();
            } catch (SecurityException e2) {
                originalPath = path;
            }
        }

        try {
            BasicFileAttributes originalAttributes = Files.readAttributes(originalPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            originalSize = originalAttributes.size();
            originalType = FileType.toSimpleType(originalAttributes);
        } catch (UnsupportedOperationException | FileSystemException | SecurityException e) {
            originalSize = Files.size(originalPath);
            originalType = FileType.UNDEFINED;
        }
        if (originalSize < 0)
            originalSize = 0;

        if (originalType.isLink()) {
            Path resolvedPath;
            FileType resolvedType;
            long resolvedSize;

            try {
                resolvedPath = path.toRealPath();
            } catch (SecurityException e2) {
                resolvedPath = originalPath;
            }

            try {
                BasicFileAttributes originalAttributes = Files.readAttributes(resolvedPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                resolvedSize = originalAttributes.size();
                resolvedType = FileType.toSimpleType(originalAttributes);
            } catch (UnsupportedOperationException | FileSystemException | SecurityException e) {
                resolvedSize = Files.size(originalPath);
                resolvedType = FileType.UNDEFINED;
            }

            if (resolvedSize < 0)
                resolvedSize = 0;

            return new DefaultFileLore(resolvedType == FileType.UNDEFINED ? FileType.UNDEFINED_LINK : resolvedType.toLink(), originalPath, originalSize, resolvedPath, resolvedSize);
        } else {
            return new DefaultFileLore(originalType, originalPath, originalSize, originalPath, originalSize);
        }
    }

}
