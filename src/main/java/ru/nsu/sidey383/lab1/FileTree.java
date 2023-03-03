package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.options.FileTreeOptions;
import ru.nsu.sidey383.lab1.walker.FileVisitor;
import ru.nsu.sidey383.lab1.walker.NextAction;
import ru.nsu.sidey383.lab1.walker.SystemFileWalker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileTree {

    private final Path basePath;

    private final boolean followLinks;

    private SystemFileWalker walker = null;

    private List<TreeBuildError> errors;

    public FileTree(FileTreeOptions options) {
        this.basePath = options.getFilePath();
        this.followLinks = options.followLink();
    }

    public void calculateTree() throws IOException {
        errors = new ArrayList<>();
        walker = null;
        walker = SystemFileWalker.walkFiles(basePath, new TreeVisitor());
        List<IOException> exceptionList = walker.getSuppressedExceptions();
        if (!exceptionList.isEmpty()) {
            System.err.println("File walker errors:");
            for (IOException exception : exceptionList) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Base file for this tree.
     * File lying on the basePath
     * @return not null after correct complete of {@link FileTree#calculateTree()}
     * **/
    @Nullable
    public File getBaseFile() {
        return walker == null ? null : walker.getRootFile();
    }

    /**
     * @return all {@link IOException}, created in time of visit files.
     * **/
    public List<TreeBuildError> getErrors() {
        return List.copyOf(errors);
    }

    /**
     * Check {@link FileTree#getErrors()} on empty list.
     * **/
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private class TreeVisitor implements FileVisitor {

        private final HashSet<File> passedLinks = new HashSet<>();

        private void addChildToParent(File f) {
            DirectoryFile parent = f.getParent();
            if (parent != null)
                parent.addChild(f);
        }

        /**
         * Synchronizes the parent-child relation
         * **/
        @Override
        public void visitFile(File file) {
            addChildToParent(file);
        }

        /**
         * Check directories on cycle.
         * @return {@link NextAction#STOP} is this is second pass of this link or in configurations set followLink on false.
         * **/
        @Override
        public NextAction preVisitDirectory(DirectoryFile directory) {
            if (directory.getFileType().isLink()) {
                if (followLinks) {
                    if (passedLinks.contains(directory)) {
                        for (File f : passedLinks) {
                            if (directory.equals(f)) {
                                DirectoryFile parent = directory.getParent();
                                if (parent != null)
                                    parent.addChild(f);
                                break;
                            }
                        }
                        return NextAction.STOP;
                    } else {
                        passedLinks.add(directory);
                    }
                } else {
                    addChildToParent(directory);
                    return NextAction.STOP;
                }
            }
            return NextAction.CONTINUE;
        }

        /**
         * Synchronizes the parent-child relation
         * **/
        @Override
        public void postVisitDirectory(DirectoryFile directory) {
            addChildToParent(directory);
        }

        /**
         * Collect all errors.
         *
         * @see FileTree#getErrors()
         **/
        @Override
        public void pathVisitError(@Nullable Path path, @NotNull IOException e) {
            errors.add(new TreeBuildError(path, null, e));
        }

    }

}
