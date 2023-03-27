package ru.nsu.sidey383.lab1.model.file.link;

import ru.nsu.sidey383.lab1.model.file.*;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

public class ExceptionLinkFile extends BaseFile implements LinkFile<ExceptionFile>, ExceptionFile {

    private ExceptionFile linkedFile;

    public ExceptionLinkFile(long size, Path path, ExceptionFile linkedFile) {
        super(size, path);
        this.linkedFile = linkedFile;
    }

    @Override
    public DUPathException getPathException() {
        return linkedFile.getPathException();
    }

    @Override
    public FileType getFileType() {
        return FileType.OTHER_LINK;
    }

    @Override
    public ExceptionFile getLinkedFile() {
        return linkedFile;
    }

    @Override
    public ExceptionFile updateLinkedFile(File file) {
        if (file instanceof ExceptionFile ef && linkedFile.equals(ef)) {
            ExceptionFile old = linkedFile;
            linkedFile = ef;
            return old;
        } else {
            throw new IllegalArgumentException("Incorrect file to update");
        }
    }
}
