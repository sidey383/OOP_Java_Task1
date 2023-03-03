package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SystemFileWalker {

    private final FileVisitor visitor;

    private final File rootFile;

    private List<IOException> exceptionList;

    private SystemFileWalker(File dirFile, FileVisitor visitor) {
        this.rootFile = dirFile;
        this.visitor = visitor;
    }

    private static class DirectoryNode {
        private final DirectoryFile file;

        private final Iterator<Path> iterator;

        private final DirectoryStream<Path> stream;

        public DirectoryNode(@NotNull DirectoryFile file) throws IOException {
            this.file = file;
            this.stream = Files.newDirectoryStream(this.file.getResolvedPath());
            this.iterator = this.stream.iterator();
        }

        @NotNull
        public DirectoryFile getDir() {
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
        if (rootFile instanceof DirectoryFile rootDir) {
            ArrayDeque<DirectoryNode> queue = new ArrayDeque<>();
            visitor.preVisitDirectory(rootDir);
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

    private void visitFile(Path path, ArrayDeque<DirectoryNode> queue, DirectoryFile parent) {
        try {
            File file = File.readFile(path);
            file.setParent(parent);
            if (file instanceof DirectoryFile directoryFile) {
                if (visitor.preVisitDirectory(directoryFile) == NextAction.CONTINUE) {
                    queue.add(new DirectoryNode(directoryFile));
                }
            } else {
                visitor.visitFile(file);
            }
        } catch (IOException e) {
            visitor.pathVisitError(path, e);
        }
    }

    private void initQueue(ArrayDeque<DirectoryNode> queue, DirectoryFile rootDir) {
        try {
            queue.add(new DirectoryNode(rootDir));
        } catch (IOException e) {
            visitor.pathVisitError(rootDir.getOriginalPath(), e);
        }
    }

    private void suppressedNodeClose(DirectoryNode node) {
        try {
            node.close();
        } catch (IOException e) {
            exceptionList.add(e);
        }
    }

    public File getRootFile() {
        return rootFile;
    }

    public List<IOException> getSuppressedExceptions() {
        return new ArrayList<>(exceptionList);
    }

    public static SystemFileWalker walkFiles(Path path, FileVisitor visitor) throws IOException {
        File rootFile = File.readFile(path);
        SystemFileWalker walker = new SystemFileWalker(rootFile, visitor);
        walker.walk();
        return walker;
    }

}
