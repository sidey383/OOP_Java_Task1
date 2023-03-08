package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;
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

    private List<PathException> errors;

    public FileTree(FileTreeOptions options) {
        this.basePath = options.getFilePath();
        this.followLinks = options.followLink();
    }

    public void calculateTree() throws IOException, PathException {
        errors = new ArrayList<>();
        walker = null;
        walker = SystemFileWalker.walkFiles(basePath, new TreeVisitor());
        errors.addAll(walker.getSuppressedExceptions());
    }

    /**
     * Возвращает null если метод {@link FileTree#calculateTree()} не был вызван или выкинул исключение.
     *
     * @return корневой файл дерева.
     */
    @Nullable
    public File getBaseFile() {
        return walker == null ? null : walker.getRootFile();
    }

    /**
     * @return все {@link PathException}, созданные и подавленные при вызове {@link SystemFileWalker#walkFiles(Path, FileVisitor)}.
     */
    public List<PathException> getErrors() {
        return List.copyOf(errors);
    }

    /**
     * Проверяет {@link FileTree#getErrors()} на пустой список.
     */
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
         * Синхронизует отношения файлов потомок-родитель.
         */
        @Override
        public void visitFile(File file) {
            addChildToParent(file);
        }

        @Override
        public NextAction preVisitDirectory(DirectoryFile directory) {
            if (!directory.getFileType().isLink()) {
                return NextAction.CONTINUE;
            }

            if (!followLinks) {
                addChildToParent(directory);
                return NextAction.STOP;
            }

            if (!passedLinks.contains(directory)) {
                passedLinks.add(directory);
                return NextAction.CONTINUE;
            }

            for (File f : passedLinks) {
                if (directory.equals(f)) {
                    DirectoryFile parent = directory.getParent();
                    if (parent != null)
                        parent.addChild(f);
                    break;
                }
            }

            return NextAction.STOP;
        }

        @Override
        public void postVisitDirectory(DirectoryFile directory) {
            addChildToParent(directory);
        }

        /**
         * Собирает все ошибки.
         *
         * @see FileTree#getErrors()
         */
        @Override
        public void pathVisitError(@Nullable Path path, @NotNull PathException e) {
            errors.add(e);
        }

    }

}
