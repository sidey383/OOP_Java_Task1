package ru.nsu.sidey383.lab1.model.file.base;

import ru.nsu.sidey383.lab1.model.file.BaseFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.model.file.ParentFile;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class DirectoryFile extends BaseFile implements ParentFile {

    private final Set<File> child = new HashSet<>();

    public DirectoryFile(long size, Path path) {
        super(size, path);
    }

    @Override
    public FileType getFileType() {
        return FileType.DIRECTORY;
    }

    @Override
    public Set<File> getChildren() {
        return child;
    }

    @Override
    public void addChild(File file) {
        if (child.add(file))
            size += file.getSize();
    }
}
