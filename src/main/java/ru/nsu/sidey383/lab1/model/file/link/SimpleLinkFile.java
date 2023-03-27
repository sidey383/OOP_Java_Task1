package ru.nsu.sidey383.lab1.model.file.link;

import ru.nsu.sidey383.lab1.model.file.BaseFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.model.file.LinkFile;

import java.nio.file.Path;

public class SimpleLinkFile extends BaseFile implements LinkFile<File> {

    private File linkedFile;

    private final FileType type;

    public SimpleLinkFile(long size, Path originalPath, File linkedFile) {
        super(size, originalPath);
        this.linkedFile = linkedFile;
        this.type = linkedFile.getFileType().toLink();
    }

    @Override
    public FileType getFileType() {
        return type;
    }

    @Override
    public File getLinkedFile() {
        return linkedFile;
    }

    @Override
    public File updateLinkedFile(File file) {
        if (linkedFile.equals(file)) {
            File old = linkedFile;
            linkedFile = file;
            return old;
        } else {
            throw new IllegalArgumentException("Incorrect file to update");
        }
    }

}
