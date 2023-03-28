package ru.nsu.sidey383.lab1.model.file.link;

import ru.nsu.sidey383.lab1.model.file.*;

import java.nio.file.Path;
import java.util.Set;

public class ParentLinkDUFile extends BaseDUFile implements LinkDUFile<ParentDUFile>, ParentDUFile {

    private ParentDUFile linkedFile;

    public ParentLinkDUFile(long size, Path path, ParentDUFile linkedFile) {
        super(size, path);
        this.linkedFile = linkedFile;
    }

    @Override
    public DUFileType getFileType() {
        return DUFileType.DIRECTORY_LINK;
    }

    @Override
    public ParentDUFile getLinkedFile() {
        return linkedFile;
    }

    @Override
    public ParentDUFile updateLinkedFile(DUFile file) {
        if (file instanceof ParentDUFile pf && linkedFile.equals(pf)) {
            ParentDUFile old = linkedFile;
            linkedFile = pf;
            return old;
        } else {
            throw new IllegalArgumentException("Incorrect file to update");
        }
    }

    @Override
    public Set<DUFile> getChildren() {
        return linkedFile.getChildren();
    }

    @Override
    public void addChild(DUFile file) {
        linkedFile.addChild(file);
    }

}
