package ru.nsu.sidey383.lab1.model.file.base;

import ru.nsu.sidey383.lab1.model.file.BaseFile;
import ru.nsu.sidey383.lab1.model.file.FileType;

import java.nio.file.Path;

public class RegularFile extends BaseFile {

    public RegularFile(long size, Path path) {
        super(size, path);
    }

    @Override
    public FileType getFileType() {
        return FileType.REGULAR_FILE;
    }

}
