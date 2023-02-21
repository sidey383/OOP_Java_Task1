package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.model.FileLore;

public abstract class BaseFile implements File {

    private final FileLore fileLore;


    protected DirectoryFile parent;

    BaseFile(FileLore fileLore) {
        this.fileLore = fileLore;
    }

    public DirectoryFile getParent() {
        return parent;
    }

    public DirectoryFile setParent(DirectoryFile parent) {
        DirectoryFile p = this.parent;
        this.parent = parent;
        return p;
    }

    public FileLore getFileLore() {
        return fileLore;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BaseFile file) && file.fileLore.equals(this.fileLore);
    }

    @Override
    public String toString() {
        return "BaseFile{" +
                "fileLore=" + fileLore +
                ", parent=" + parent +
                '}';
    }

    @Override
    public int hashCode() {
        return fileLore.hashCode();
    }
}
