package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class FileTree {

    private Map<Path, DirectoryFile> directories = new HashMap<>();
    private Map<Path, SymLinkFile> symLinks = new HashMap<>();

    private Path basePath;

    private File baseFile;

    public FileTree(Path path) {
        this.basePath = path.toAbsolutePath();
    }

    @Nullable
    public File getBaseFile() {
        return baseFile;
    }

    public void initTree() throws IOException {
        switch (FileType.getFileType(basePath)) {
            case DIRECTORY -> {
                baseFile = new DirectoryFile(basePath);
                directories.put(basePath, (DirectoryFile) baseFile);
                Files.walkFileTree(basePath, new Visitor());
            }
            case REGULAR -> baseFile = new RegularFile(basePath);
            case SYM_LINK -> baseFile = new SymLinkFile(basePath);
            default -> baseFile = new OtherFile(basePath);
        }
    }

    private class Visitor implements FileVisitor<Path> {

        private Visitor() {}

        private void addToParent(@NotNull File f) {
            Path parent = f.getPath().getParent();
            if (parent != null && directories.containsKey(parent)) {
                directories.get(parent).addChild(f);
            }
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            dir = dir.toAbsolutePath();
            DirectoryFile file = new DirectoryFile(dir);
            if (!baseFile.equals(file))
                directories.put(dir, file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
            path = path.toAbsolutePath();
            File file = null;
            if (attrs.isSymbolicLink()) {
                file = new SymLinkFile(path);
                symLinks.put(path, (SymLinkFile) file);
            }
            if (attrs.isRegularFile()) {
                file = new RegularFile(path);
            }
            if (file == null) {
                file = new OtherFile(path);
            }
            addToParent(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            System.out.println("visitFileFailed: " + file.toString() + (exc != null ? exc.getMessage() : ""));
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            DirectoryFile file = directories.get(dir);
            if (file != null) {
                addToParent(file);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
