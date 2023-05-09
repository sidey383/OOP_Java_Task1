package ru.nsu.sidey383.lab1.walker;

import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;
import ru.nsu.sidey383.lab1.walker.node.DUWalkerNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class DUSystemFileWalker {

    private final DUFileVisitor visitor;

    private final DUFile rootFile;

    private DUSystemFileWalker(DUFile dirFile, DUFileVisitor visitor) {
        this.rootFile = dirFile;
        this.visitor = visitor;
    }

    private void walk() {
        if (rootFile instanceof ParentDUFile rootDir) {
            Deque<DUWalkerNode> queue = new ArrayDeque<>();
            visitor.preVisitParentFile(rootDir);
            try {
                initQueue(queue, rootDir);
                while (!queue.isEmpty()) {
                    DUWalkerNode node = queue.getLast();
                    Iterator<Path> iterator = node.getPathIterator();
                    if (iterator.hasNext()) {
                        visitFile(iterator.next(), queue, node.getParent());
                    } else {
                        suppressedNodeClose(queue.removeLast());
                        visitor.postVisitParentFile(node.getParent());
                    }
                }
            } finally {
                while (!queue.isEmpty())
                    suppressedNodeClose(queue.pop());
            }
        } else {
            visitor.visitFile(rootFile);
        }
    }

    private void visitFile(Path path, Deque<DUWalkerNode> queue, ParentDUFile parent) {
        try {
            DUFile file = DUFile.readFile(path);
            file.setParent(parent);
            if (file instanceof ParentDUFile parentFile) {
                if (visitor.preVisitParentFile(parentFile) == DUAction.CONTINUE) {
                    queue.addLast(DUWalkerNode.createWalker(parentFile));
                }
            } else {
                visitor.visitFile(file);
            }
        } catch (DUPathException e) {
            visitor.pathVisitError(path, e);
        }
    }

    private void initQueue(Deque<DUWalkerNode> queue, ParentDUFile rootDir) {
        try {
            queue.add(DUWalkerNode.createWalker(rootDir));
        } catch (DUPathException e) {
            visitor.pathVisitError(rootDir.getPath(), e);
        }
    }

    private void suppressedNodeClose(DUWalkerNode node) {
        try {
            node.close();
        } catch (IOException e) {
            visitor.directoryCloseError(node.getParent().getPath(), new DUPathException(node.getParent().getPath(), e));
        }
    }

    public DUFile getRootFile() {
        return rootFile;
    }

    /**
     * Проходит по дереву файлов.
     *
     * @param path корневой файл.
     *
     * @return объект, обходивший файлы.
     *
     * @see DUSystemFileWalker#getRootFile()
     */
    public static DUSystemFileWalker walkFiles(Path path, DUFileVisitor visitor) {
        DUFile rootFile = DUFile.readFile(path);
        DUSystemFileWalker walker = new DUSystemFileWalker(rootFile, visitor);
        walker.walk();
        return walker;
    }

}
