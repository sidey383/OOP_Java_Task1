package ru.nsu.sidey383.lab1.model.files;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public sealed class OtherFile extends File permits OtherLinkFile {

    OtherFile(Path path, BasicFileAttributes attributes) {
        super(path, attributes);
    }

}
