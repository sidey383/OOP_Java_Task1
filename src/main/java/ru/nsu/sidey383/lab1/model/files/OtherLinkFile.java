package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.utils.SizeSuffix;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public final class OtherLinkFile extends OtherFile {

    OtherLinkFile(Path path, BasicFileAttributes attributes) {
        super(path, attributes);
    }

    @Override
    public String toString() {
        return String.format("%s [link %s]", path.getFileName(), SizeSuffix.BYTE.getSuffix(getSize()));
    }

}
