package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.lore.FileLore;

import java.io.IOException;
import java.nio.file.Path;

public interface File {

    FileLore getFileLore();

    long getSize();

    DirectoryFile getParent();

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

    static File readFile(Path path) throws IOException {
        FileLore lore = FileLore.createFileLore(path);
        return switch (lore.fileType()) {
            case DIRECTORY -> new DefaultDirectoryFile(lore);
            case DIRECTORY_LINK -> new DefaultLinkDirectoryFile(lore);
            case REGULAR_FILE, REGULAR_FILE_LINK -> new DefaultRegularFile(lore);
            case OTHER, OTHER_LINK, UNDEFINED, UNDEFINED_LINK -> new DefaultOtherFile(lore);
        };
    }

}
