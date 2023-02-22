package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.model.FileLore;

import java.util.ArrayList;
import java.util.List;

public class DefaultDirectoryFile extends BaseFile implements DirectoryFile {

    protected List<File> children = new ArrayList<>();

    protected long size;

    public DefaultDirectoryFile(FileLore lore) {
        super(lore);
        this.size = getResolvedSize();
    }

    @Override
    public List<File> getChildren() {
        return new ArrayList<>(children);
    }


    @Override
    public void addChild(File file) {
        if (this.children.contains(file))
            return;
        this.size += file.getSize();
        this.children.add(file);
    }

    public long recalculateSize() {
        this.size = getResolvedSize();
        for (File f : children) {
            this.size += f.getSize();
        }
        return this.size;
    }

    @Override
    public long getSize() {
        return this.size;
    }
}
