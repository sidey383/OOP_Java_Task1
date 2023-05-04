package ru.nsu.sidey383.lab1.model.file.base;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.BaseDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;

import java.nio.file.Path;

public class OtherDUFile extends BaseDUFile {

    public OtherDUFile(long size, @NotNull Path path) {
        super(size, path);
    }

    @Override
    public @NotNull DUFileType getFileType() {
        return DUFileType.OTHER;
    }
}
