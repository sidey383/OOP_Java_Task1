package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.lore.FileLore;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Common file interface
 * **/
public interface File {

    FileLore getFileLore();

    long getSize();

    DirectoryFile getParent();

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
     * Factory method for creating {@link File}
     * @throws SecurityException when don't have permission to resolve path or read file size
     * @throws IOException if file doesn't exist or an I/O error occurs
     * @see FileLore#createFileLore(Path)
     * **/
    static File readFile(Path path) throws IOException {
        FileLore lore = FileLore.createFileLore(path);
        return switch (lore.fileType()) {
            case DIRECTORY -> new DefaultDirectoryFile(lore);
            case DIRECTORY_LINK -> new DefaultLinkDirectoryFile(lore);
            case REGULAR_FILE, REGULAR_FILE_LINK, OTHER, OTHER_LINK, UNDEFINED, UNDEFINED_LINK  -> new DefaultFile(lore);
        };
    }

}
