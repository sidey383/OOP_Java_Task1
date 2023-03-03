package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.lore.FileLore;

import java.util.HashSet;
import java.util.Set;

public class DefaultDirectoryFile extends BaseFile implements DirectoryFile {

    protected final Set<File> children = new HashSet<>();

    protected long size;

    public DefaultDirectoryFile(FileLore lore) {
        super(lore);
        this.size = getResolvedSize();
    }

    @Override
    public Set<File> getChildren() {
        return Set.copyOf(children);
    }


    @Override
    public void addChild(File file) {
        if (this.children.contains(file))
            return;
        this.size += file.getSize();
        this.children.add(file);
    }

    @Override
    public long getSize() {
        return this.size;
    }
}
