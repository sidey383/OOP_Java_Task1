package ru.nsu.sidey383.lab1.model.file.link;

import ru.nsu.sidey383.lab1.model.file.*;

import java.nio.file.Path;
import java.util.Set;

public class ParentLinkFile extends BaseFile implements LinkFile<ParentFile>, ParentFile{

    private ParentFile linkedFile;

    public ParentLinkFile(long size, Path path, ParentFile linkedFile) {
        super(size, path);
        this.linkedFile = linkedFile;
    }

    @Override
    public FileType getFileType() {
        return FileType.DIRECTORY_LINK;
    }

    @Override
    public ParentFile getLinkedFile() {
        return linkedFile;
    }

    @Override
    public ParentFile updateLinkedFile(File file) {
        if (file instanceof ParentFile pf && linkedFile.equals(pf)) {
            ParentFile old = linkedFile;
            linkedFile = pf;
            return old;
        } else {
            throw new IllegalArgumentException("Incorrect file to update");
        }
    }

    @Override
    public Set<File> getChildren() {
        return linkedFile.getChildren();
    }

    @Override
    public void addChild(File file) {
        linkedFile.addChild(file);
    }

}
