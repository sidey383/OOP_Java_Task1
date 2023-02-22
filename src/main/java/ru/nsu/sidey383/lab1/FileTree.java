package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.files.DirectoryFile;
import ru.nsu.sidey383.lab1.model.files.File;
import ru.nsu.sidey383.lab1.walker.FileVisitor;
import ru.nsu.sidey383.lab1.walker.NextAction;
import ru.nsu.sidey383.lab1.walker.SystemFileWalker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public class FileTree {

    private final Path basePath;

    private SystemFileWalker walker = null;

    public FileTree(Path basePath) {
        this.basePath = basePath;
    }

    public void calculateTree() throws IOException {
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
                if (passedLinks.contains(directory)) {
                    for (File f : passedLinks) {
                        if (directory.equals(f)) {
                            DirectoryFile parent = directory.getParent();
                            if (parent != null)
                                parent.addChild(f);
                            return NextAction.STOP;
                        }
                    }
                    //addParent(directory);
                    return NextAction.STOP;
                } else {
                    passedLinks.add(directory);
                }
            }
            addChildToParent(directory);
            return NextAction.CONTINUE;
        }

        @Override
        public void postVisitDirectory(DirectoryFile directory) {}

        @Override
        public NextAction pathVisitError(Path path, IOException e) {
            System.err.format("Can't read metadata of file %s\n", path);
            e.printStackTrace();
            return NextAction.CONTINUE;
        }

        @Override
        public NextAction realPathError(Path path, IOException e) {
            System.err.format("Error when trying to get the real path of file %s\n", path);
            e.printStackTrace();
            return NextAction.TRY_OTHER;
        }
    }

}
