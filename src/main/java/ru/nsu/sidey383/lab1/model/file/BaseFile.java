package ru.nsu.sidey383.lab1.model.file;

import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.lore.FileLore;

/**
 * Base realization of {@link File}
 * Con
 * **/
public class BaseFile implements File {

    private final FileLore fileLore;


    protected DirectoryFile parent;

    BaseFile(FileLore fileLore) {
        this.fileLore = fileLore;
    }

    /**
     * Get the parent file. There is no guarantee that this file is a child of this parent.
     * **/
    public DirectoryFile getParent() {
        return parent;
    }

    /**
     * Change only local file state.
     * Does not affect the state of the parent.
     * @return old parent file or null
     * **/
    @Nullable
    public DirectoryFile setParent(DirectoryFile parent) {
        DirectoryFile p = this.parent;
        this.parent = parent;
        return p;
    }

    public FileLore getFileLore() {
        return fileLore;
    }

    @Override
    public long getSize() {
        return getResolvedSize();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BaseFile file) && file.fileLore.equals(this.fileLore);
    }

    @Override
    public String toString() {
        return "BaseFile{" +
                "fileLore=" + fileLore +
                ", parent=" + (parent == null ? null : parent.getOriginalPath()) +
                '}';
    }

    @Override
    public int hashCode() {
        return fileLore.hashCode();
    }
}
