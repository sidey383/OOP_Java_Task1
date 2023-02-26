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
        walker = SystemFileWalker.walkFiles(basePath, new TreeVisitor());
        List<IOException> exceptionList = walker.getSuppressedExceptions();
        if (!exceptionList.isEmpty()) {
            System.err.println("File walker errors:");
            for (IOException exception : exceptionList) {
                exception.printStackTrace();
            }
        }
    }

    @Nullable
    public File getBaseFile() {
        return walker == null ? null : walker.getRootFile();
    }

    public List<TreeBuildError> getErrors() {
        return List.copyOf(errors);
    }

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

        @Override
        public void visitFile(File file) {
            addChildToParent(file);
        }

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
                                return NextAction.STOP;
                            }
                        }
                        return NextAction.STOP;
                    } else {
                        passedLinks.add(directory);
                    }
                } else {
                    return NextAction.STOP;
                }
            }
            return NextAction.CONTINUE;
        }

        @Override
        public void postVisitDirectory(DirectoryFile directory) {
            addChildToParent(directory);
        }

        @Override
        public NextAction pathVisitError(@Nullable Path path, @NotNull IOException e) {
            errors.add(new TreeBuildError(path, null, e));
            return NextAction.CONTINUE;
        }

    }

}
