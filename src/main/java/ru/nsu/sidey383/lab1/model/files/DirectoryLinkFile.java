package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.utils.SizeSuffix;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public final class DirectoryLinkFile extends DirectoryFile {

    DirectoryLinkFile(Path path, BasicFileAttributes attributes) {
        super(path, attributes);
    }

    @Override
    public void addChild(File file) {
        if (!children.contains(file))
            children.add(file);
    }

    @Override
    public String toString() {
        return String.format("/%s [link %s]", path.getFileName(), SizeSuffix.BYTE.getSuffix(getSize()));
    }
}
