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

    private final LinkOption[] createFileOptions;

    private final LinkOption[] realPathOptions;

    private final FileVisitor visitor;

    private final File rootFile;

    private List<IOException> exceptionList;

    private final boolean useRealPath;

    private SystemFileWalker(File dirFile, FileVisitor visitor, boolean useRealPath, LinkOption[] createFileOptions, LinkOption[] realPathOptions) {
        this.rootFile = dirFile;
        this.useRealPath = useRealPath;
        this.createFileOptions = createFileOptions;
        this.realPathOptions = realPathOptions;
        this.visitor = visitor;
    }

    private static class DirectoryNode {
        private final DirectoryFile file;

        private final Iterator<Path> iterator;

        private final DirectoryStream<Path> stream;

        public DirectoryNode(DirectoryFile file) throws IOException {
            this.file = file;
            this.stream = Files.newDirectoryStream(this.file.getPath());
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
            File file = File.createFile(path, useRealPath, createFileOptions);
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
            visitor.pathVisitError(rootDir.getPath(), e);
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

    public static SystemFileWalker walkFiles(Path path, FileVisitor visitor, SystemFileWalkerOptions... options) throws IOException {
        LinkOption[] createFileOptions = new LinkOption[0];
        boolean useRealPath = false;
        LinkOption[] realPathOptions = new LinkOption[0];
        for (SystemFileWalkerOptions option : options) {
            switch (option) {
                case NO_FOLLOW_LINKS -> createFileOptions = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
                case TO_REAL_PATH -> useRealPath = true;
                case NO_FOLLOW_LINKS_IN_REAL_PATH -> realPathOptions = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
            }
        }
        File rootFile = File.createFile(path, useRealPath, createFileOptions);
        SystemFileWalker walker = new SystemFileWalker(rootFile, visitor, useRealPath, createFileOptions, realPathOptions);
        walker.walk();
        return walker;
    }

}
