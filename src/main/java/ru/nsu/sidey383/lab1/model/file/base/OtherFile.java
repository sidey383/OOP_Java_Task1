package ru.nsu.sidey383.lab1.model.file.base;

import ru.nsu.sidey383.lab1.model.file.BaseFile;
import ru.nsu.sidey383.lab1.model.file.FileType;

import java.nio.file.Path;

public class OtherFile extends BaseFile {

    public OtherFile(long size, Path path) {
        super(size, path);
    }

    @Override
    public FileType getFileType() {
        return FileType.OTHER;
    }
}
