package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.model.FileLore;
import ru.nsu.sidey383.lab1.model.FileType;

import java.io.IOException;
import java.nio.file.Path;

public interface File {

    FileLore getFileLore();

    long getSize();

    DirectoryFile getParent();

    DirectoryFile setParent(DirectoryFile file);

    default FileType getFileType() {
        return getFileLore().getFileType();
    }

    default Path getOriginalPath() {
        return getFileLore().getOriginalPath();
    }

    default Path getResolvedPath() {
        return getFileLore().getResolvedPath();
    }

    default long getOriginalSize() {
        return getFileLore().getOriginalAttributes().size();
    }

    default long getResolvedSize() {
        return getFileLore().getResolvedAttributes().size();
    }

    static File readFile(Path path) throws IOException {
        FileLore lore = FileLore.createFileLore(path);
        return switch (lore.getFileType()) {
            case DIRECTORY -> new DefaultDirectoryFile(lore);
            case DIRECTORY_LINK -> new DefaultLinkDirectoryFile(lore);
            case REGULAR_FILE, REGULAR_FILE_LINK -> new DefaultRegularFile(lore);
            case OTHER, OTHER_LINK, UNDEFINED, UNDEFINED_LINK -> new DefaultOtherFile(lore);
        };
    }

}
