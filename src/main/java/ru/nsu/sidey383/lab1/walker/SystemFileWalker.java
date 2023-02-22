package ru.nsu.sidey383.lab1.walker;

import ru.nsu.sidey383.lab1.model.files.DirectoryFile;
import ru.nsu.sidey383.lab1.model.files.File;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
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

        public DirectoryNode(DirectoryFile file) throws IOException {
            this.file = file;
            this.stream = Files.newDirectoryStream(this.file.getResolvedPath());
            this.iterator = this.stream.iterator();
        }

        public DirectoryFile getDir() {
            return file;
        }

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
            try {
                if(!initQueue(queue, rootDir))
                    return;
                do {
                    DirectoryNode node = queue.peek();
                    Iterator<Path> iterator = node.getIterator();
                    if (iterator.hasNext()) {
                        visitFile(iterator.next(), queue, node.getDir());
                    } else {
                        queue.remove();
                        suppressedNodeClose(node);
                        visitor.postVisitDirectory(node.getDir());
                    }
                } while (!queue.isEmpty());
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

    private boolean initQueue(ArrayDeque<DirectoryNode> queue, DirectoryFile rootDir) {
        try {
            queue.add(new DirectoryNode(rootDir));
        } catch (IOException e) {
            visitor.pathVisitError(rootDir.getOriginalPath(), e);
        }
        return !queue.isEmpty();
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

    /**
     * Collects all {@link DirectoryStream} closure exceptions. <br>
     * All file opening exceptions are returned via {@link FileVisitor#pathVisitError(Path, IOException)} <br>
     * All errors produced by {@link Path#toRealPath(LinkOption...)} are returned vai {@link FileVisitor#realPathError(Path, IOException)}
     *
     * @return list of suppressed IOExceptions
     **/
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
