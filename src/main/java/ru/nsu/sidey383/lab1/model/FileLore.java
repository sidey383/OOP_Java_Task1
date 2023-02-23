package ru.nsu.sidey383.lab1.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class FileLore {

    private final BasicFileAttributes originalAttributes;

    private final Path originalPath;

    private final BasicFileAttributes resolvedAttributes;

    private final Path resolvedPath;

    private final FileType fileType;

    private FileLore(FileType fileType, Path originalPath, BasicFileAttributes originalAttributes, Path resolvedPath, BasicFileAttributes resolvedAttributes) {
        this.fileType = fileType;
        this.originalAttributes = originalAttributes;
        this.originalPath = originalPath;
        this.resolvedAttributes = resolvedAttributes;
        this.resolvedPath = resolvedPath;
    }

    public BasicFileAttributes getOriginalAttributes() {
        return originalAttributes;
    }

    public Path getOriginalPath() {
        return originalPath;
    }

    public BasicFileAttributes getResolvedAttributes() {
        return resolvedAttributes;
    }

    public Path getResolvedPath() {
        return resolvedPath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public static FileLore createFileLore(Path path) throws IOException {
        Path originalPath;
        try {
            originalPath = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (NotDirectoryException e) {
            originalPath = path.toRealPath();
        }
        final BasicFileAttributes originalAttributes = Files.readAttributes(originalPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        if (originalAttributes == null)
            throw new AttributesResolveException("Can't read attributes by path "+ path);
        if (originalAttributes.isSymbolicLink()) {
            final Path linkPath = path.toRealPath();
            BasicFileAttributes linkAttributes = Files.readAttributes(linkPath, BasicFileAttributes.class);
            if (linkAttributes == null)
                throw new AttributesResolveException("Can't read attributes of link " + originalPath + " by path " + linkPath);
            FileType type = FileType.toSimpleType(linkAttributes);
            return new FileLore(type.toLink() ,originalPath, originalAttributes, linkPath, linkAttributes);
        }
        FileType type = FileType.toSimpleType(originalAttributes);
        return new FileLore(type, originalPath, originalAttributes, originalPath, originalAttributes);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof FileLore lore) && originalPath.equals(lore.originalPath) && resolvedPath.equals(lore.resolvedPath);
    }

    @Override
    public String toString() {
        return "FileLore{" +
                "fileType=" + fileType +
                ", originalAttributes=" + originalAttributes +
                ", originalPath=" + originalPath +
                ", resolvedAttributes=" + resolvedAttributes +
                ", resolvedPath=" + resolvedPath +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalPath, resolvedPath);
    }
}
