package ru.nsu.sidey383.lab1.model.file.base;

import ru.nsu.sidey383.lab1.model.file.BaseDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class DirectoryDUFile extends BaseDUFile implements ParentDUFile {

    private final Set<DUFile> child = new HashSet<>();

    public DirectoryDUFile(long size, Path path) {
        super(size, path);
    }

    @Override
    public DUFileType getFileType() {
        return DUFileType.DIRECTORY;
    }

    @Override
    public Set<DUFile> getChildren() {
        return child;
    }

    @Override
    public void addChild(DUFile file) {
        if (child.add(file))
            size += file.getSize();
    }
}
