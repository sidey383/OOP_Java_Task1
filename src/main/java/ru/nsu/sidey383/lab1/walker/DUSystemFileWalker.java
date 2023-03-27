package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.LinkFile;
import ru.nsu.sidey383.lab1.model.file.ParentFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DUSystemFileWalker {

    private final DUFileVisitor visitor;

    private final File rootFile;

    private List<DUPathException> exceptionList;

    private DUSystemFileWalker(File dirFile, DUFileVisitor visitor) {
        this.rootFile = dirFile;
        this.visitor = visitor;
    }

    private static class DirectoryNode {
        private final ParentFile file;

        private final Iterator<Path> iterator;

        private final DirectoryStream<Path> stream;

        public DirectoryNode(@NotNull ParentFile file) throws DUPathException {
            this.file = file;
            try {
                if (file instanceof LinkFile<?> pfLink && pfLink.getLinkedFile() instanceof ParentFile pf) {
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
        public ParentFile getDir() {
            return file;
        }

        @NotNull
        public Iterator<Path> getIterator() {
            return iterator;
        }

        public void close() throws IOException {
            stream.close();
        }
    }

    private void walk() {
        exceptionList = new ArrayList<>();
        if (rootFile instanceof ParentFile rootDir) {
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

    private void visitFile(Path path, Deque<DirectoryNode> queue, ParentFile parent) {
        try {
            File file = File.readFile(path);
            file.setParent(parent);
            if (file instanceof ParentFile parentFile) {
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

    private void initQueue(Deque<DirectoryNode> queue, ParentFile rootDir) {
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

    public File getRootFile() {
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
        File rootFile = File.readFile(path);
        DUSystemFileWalker walker = new DUSystemFileWalker(rootFile, visitor);
        walker.walk();
        return walker;
    }

}
