package ru.nsu.sidey383.lab1.model.file.link;

import ru.nsu.sidey383.lab1.model.file.*;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

public class ExceptionLinkDUFile extends BaseDUFile implements LinkDUFile<ExceptionDUFile>, ExceptionDUFile {

    private ExceptionDUFile linkedFile;

    public ExceptionLinkDUFile(long size, Path path, ExceptionDUFile linkedFile) {
        super(size, path);
        this.linkedFile = linkedFile;
    }

    @Override
    public DUPathException getPathException() {
        return linkedFile.getPathException();
    }

    @Override
    public DUFileType getFileType() {
        return DUFileType.OTHER_LINK;
    }

    @Override
    public ExceptionDUFile getLinkedFile() {
        return linkedFile;
    }

    @Override
    public ExceptionDUFile updateLinkedFile(DUFile file) {
        if (file instanceof ExceptionDUFile ef && linkedFile.equals(ef)) {
            ExceptionDUFile old = linkedFile;
            linkedFile = ef;
            return old;
        } else {
            throw new IllegalArgumentException("Incorrect file to update");
        }
    }
}
