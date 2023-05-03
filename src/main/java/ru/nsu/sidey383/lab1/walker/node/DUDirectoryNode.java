package ru.nsu.sidey383.lab1.walker.node;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.base.DirectoryDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class DUDirectoryNode implements DUWalkerNode {
    private final DirectoryDUFile file;

    private final Iterator<Path> iterator;

    private final DirectoryStream<Path> stream;

    public DUDirectoryNode(@NotNull DirectoryDUFile file) throws DUPathException {
        this.file = file;
        try {
            this.stream = Files.newDirectoryStream(file.getPath());
        } catch (IOException e) {
            throw new DUPathException(file.getPath(), e);
        }
        this.iterator = this.stream.iterator();
    }

    @NotNull
    public ParentDUFile getParent() {
        return file;
    }

    @NotNull
    public Iterator<Path> getPathIterator() {
        return iterator;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
