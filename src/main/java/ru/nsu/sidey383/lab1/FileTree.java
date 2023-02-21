package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.files.DirectoryFile;
import ru.nsu.sidey383.lab1.model.files.DirectoryLinkFile;
import ru.nsu.sidey383.lab1.model.files.File;
import ru.nsu.sidey383.lab1.options.FileTreeOptions;
import ru.nsu.sidey383.lab1.walker.FileVisitor;
import ru.nsu.sidey383.lab1.walker.NextAction;
import ru.nsu.sidey383.lab1.walker.SystemFileWalker;
import ru.nsu.sidey383.lab1.walker.SystemFileWalkerOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileTree {

    private final Path basePath;

    private SystemFileWalker walker = null;

    public FileTree(Path basePath) {
        this.basePath = basePath;
    }

    public void calculateTree(FileTreeOptions options) throws IOException {
        calculateTree(options.getWalkerOptions());
    }

    public void calculateTree(SystemFileWalkerOptions... options) throws IOException {
        walker = SystemFileWalker.walkFiles(basePath, new TreeVisitor(), options);
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

        private final Set<DirectoryLinkFile> linkFiles = new HashSet<>();

        private void addParent(File f) {
            DirectoryFile parent = f.getParent();
            if (parent != null)
                parent.addChild(f);
        }

        @Override
        public void visitFile(File file) {
            addParent(file);
        }

        @Override
        public NextAction preVisitDirectory(DirectoryFile directory) {
            System.out.println(directory.getClass());
            if (directory instanceof DirectoryLinkFile link) {
                if (linkFiles.contains(link))
                    return NextAction.STOP;
                linkFiles.add(link);
            }
            return NextAction.CONTINUE;
        }

        @Override
        public void postVisitDirectory(DirectoryFile directory) {
            addParent(directory);
        }

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
