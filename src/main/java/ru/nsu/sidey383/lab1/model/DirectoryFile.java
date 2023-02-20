package ru.nsu.sidey383.lab1.model;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.utils.SizeSuffix;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public non-sealed class DirectoryFile extends File {

    private final List<File> files = new ArrayList<>();

    public DirectoryFile(Path path) throws IllegalArgumentException {
        super(path);
        if(!Files.isDirectory(path))
            throw new IllegalArgumentException(path + " is not directory");
    }

    public Stream<File> getFilesStream() {
        return files.stream();
    }

    public void addChild(@NotNull File f) {
        this.files.add(f);
        this.size += f.getSize();
    }

    @Override
    public String toString() {
        return String.format("/%s [%s]", path.getFileName(), SizeSuffix.BYTE.getSuffix(getSize()));
    }

}
