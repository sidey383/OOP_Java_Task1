package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.utils.SizeSuffix;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public sealed class DirectoryFile extends File permits DirectoryLinkFile {

    protected List<File> children = new ArrayList<>();

    DirectoryFile(Path path, BasicFileAttributes attributes) {
        super(path, attributes);
    }

    public void addChild(File file) {
        if (children.contains(file))
            return;
        children.add(file);
        this.size += file.getSize();
    }

    public List<File> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public String toString() {
        return String.format("/%s [%s]", path.getFileName(), SizeSuffix.BYTE.getSuffix(getSize()));
    }

}
