package ru.nsu.sidey383.lab1.model.file.link;

import ru.nsu.sidey383.lab1.model.file.BaseDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.LinkDUFile;

import java.nio.file.Path;

public class SimpleLinkDUFile extends BaseDUFile implements LinkDUFile<DUFile> {

    private DUFile linkedFile;

    private final DUFileType type;

    public SimpleLinkDUFile(long size, Path originalPath, DUFile linkedFile) {
        super(size, originalPath);
        this.linkedFile = linkedFile;
        this.type = linkedFile.getFileType().toLink();
    }

    @Override
    public DUFileType getFileType() {
        return type;
    }

    @Override
    public DUFile getLinkedFile() {
        return linkedFile;
    }

    @Override
    public DUFile updateLinkedFile(DUFile file) {
        if (linkedFile.equals(file)) {
            DUFile old = linkedFile;
            linkedFile = file;
            return old;
        } else {
            throw new IllegalArgumentException("Incorrect file to update");
        }
    }

}
