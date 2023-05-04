package ru.nsu.sidey383.lab1.model.file.base;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.BaseDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;

import java.nio.file.Path;

public class RegularDUFile extends BaseDUFile {

    public RegularDUFile(long size, Path path) {
        super(size, path);
    }

    @Override
    public @NotNull DUFileType getFileType() {
        return DUFileType.REGULAR_FILE;
    }

}
