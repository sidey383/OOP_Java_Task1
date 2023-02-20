package ru.nsu.sidey383.lab1.model;

import java.nio.file.Path;
import java.util.stream.Stream;

public non-sealed class SymLinkFile extends File {

    public SymLinkFile(Path path) {
        super(path);
    }

    public Stream<File> getFilesStream() {
        return Stream.empty();
    }


}
