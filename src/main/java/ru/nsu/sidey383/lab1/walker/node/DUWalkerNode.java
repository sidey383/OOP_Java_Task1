package ru.nsu.sidey383.lab1.walker.node;

import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.ReferenceDUFile;
import ru.nsu.sidey383.lab1.model.file.base.DirectoryDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.io.Closeable;
import java.nio.file.Path;
import java.util.Iterator;

public interface DUWalkerNode extends Closeable {

    ParentDUFile getParent();

    Iterator<Path> getPathIterator();

    static DUWalkerNode create(ParentDUFile file) throws DUPathException {
        if (file instanceof DirectoryDUFile dir) {
            return new DUDirectoryNode(dir);
        }
        if (file instanceof ReferenceDUFile referenceDUFile) {
            return new DULinkNode(referenceDUFile);
        }
        return new DUWalkerNode() {
            @Override
            public ParentDUFile getParent() {
                return file;
            }

            @Override
            public Iterator<Path> getPathIterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Path next() {
                        return null;
                    }
                };
            }

            @Override
            public void close() {}
        };
    }

}
