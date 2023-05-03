package ru.nsu.sidey383.lab1.walker.node;

import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.base.LinkDUFile;

import java.nio.file.Path;
import java.util.Iterator;

public class DULinkNode implements DUWalkerNode {

    private final LinkDUFile file;

    private final Iterator<Path> iterator;

    public DULinkNode(LinkDUFile file) {
        this.file = file;
        this.iterator = new Iterator<>() {

            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Path next() {
                if (hasNext) {
                    hasNext = false;
                    return file.getReference();
                }
                return null;
            }
        };
    }

    @Override
    public ParentDUFile getParent() {
        return file;
    }

    @Override
    public Iterator<Path> getPathIterator() {
        return iterator;
    }

    @Override
    public void close() {
        iterator.next();
    }
}
