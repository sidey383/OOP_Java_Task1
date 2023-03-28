package ru.nsu.sidey383.lab1.model.file.base;

import ru.nsu.sidey383.lab1.model.file.BaseDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;

import java.nio.file.Path;

public class OtherDUFile extends BaseDUFile {

    public OtherDUFile(long size, Path path) {
        super(size, path);
    }

    @Override
    public DUFileType getFileType() {
        return DUFileType.OTHER;
    }
}
