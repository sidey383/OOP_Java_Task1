package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.LinkDUFile;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DUSystemFileWalker {

    private final DUFileVisitor visitor;

    private final DUFile rootFile;

    private List<DUPathException> exceptionList;

    private DUSystemFileWalker(DUFile dirFile, DUFileVisitor visitor) {
        this.rootFile = dirFile;
        this.visitor = visitor;
    }

    // CR(minor): DirectoryNode -> WalkerDirectoryNode
    private static class DirectoryNode {
        private final ParentDUFile file;

        private final Iterator<Path> iterator;

        private final DirectoryStream<Path> stream;

        public DirectoryNode(@NotNull ParentDUFile file) throws DUPathException {
            this.file = file;
            try {
                // CR: seems redundant, would be better to handle link in the same manner as directory, does not matter if
                // CR: it links to a regular file or a directory
                if (file instanceof LinkDUFile<?> pfLink && pfLink.getLinkedFile() instanceof ParentDUFile pf) {
                    this.stream = Files.newDirectoryStream(pf.getPath());
                } else {
                    this.stream = Files.newDirectoryStream(file.getPath());
                }
            } catch (IOException e) {
                throw new DUPathException(file.getPath(), e);
            }
            this.iterator = this.stream.iterator();
        }

        @NotNull
        public ParentDUFile getDir() {
            return file;
        }

        @NotNull
        public Iterator<Path> getIterator() {
            return iterator;
        }

        // CR: make DirectoryNode implements Closeable, would be more readable
        public void close() throws IOException {
            stream.close();
        }
    }

    private void walk() {
        exceptionList = new ArrayList<>();
        if (rootFile instanceof ParentDUFile rootDir) {
            Deque<DirectoryNode> queue = new ArrayDeque<>();
            visitor.preVisitParentFile(rootDir);
            try {
                initQueue(queue, rootDir);
                while (!queue.isEmpty()) {
                    DirectoryNode node = queue.peek();
                    Iterator<Path> iterator = node.getIterator();
                    if (iterator.hasNext()) {
                        visitFile(iterator.next(), queue, node.getDir());
                    } else {
                        queue.remove();
                        suppressedNodeClose(node);
                        visitor.postVisitDirectory(node.getDir());
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

    private void visitFile(Path path, Deque<DirectoryNode> queue, ParentDUFile parent) {
        try {
            DUFile file = DUFile.readFile(path);
            file.setParent(parent);
            if (file instanceof ParentDUFile parentFile) {
                if (visitor.preVisitParentFile(parentFile) == DUAction.CONTINUE) {
                    queue.add(new DirectoryNode(parentFile));
                }
            } else {
                visitor.visitFile(file);
            }
        } catch (DUPathException e) {
            visitor.pathVisitError(path, e);
        }
    }

    private void initQueue(Deque<DirectoryNode> queue, ParentDUFile rootDir) {
        try {
            queue.add(new DirectoryNode(rootDir));
        } catch (DUPathException e) {
            visitor.pathVisitError(rootDir.getPath(), e);
        }
    }

    private void suppressedNodeClose(DirectoryNode node) {
        try {
            node.close();
        } catch (IOException e) {
            exceptionList.add(new DUPathException(node.getDir().getPath(), e));
        }
    }

    public DUFile getRootFile() {
        return rootFile;
    }

    public List<DUPathException> getSuppressedExceptions() {
        return new ArrayList<>(exceptionList);
    }

    /**
     * Проходит по дереву файлов.
     *
     * @param path корневой файл.
     *
     * @return объект, обходивший файлы.
     *
     * @see DUSystemFileWalker#getRootFile()
     * @see DUSystemFileWalker#getSuppressedExceptions()
     */
    public static DUSystemFileWalker walkFiles(Path path, DUFileVisitor visitor) {
        DUFile rootFile = DUFile.readFile(path);
        DUSystemFileWalker walker = new DUSystemFileWalker(rootFile, visitor);
        walker.walk();
        return walker;
    }

}
