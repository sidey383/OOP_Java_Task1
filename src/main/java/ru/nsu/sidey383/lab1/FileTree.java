package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.base.WrongDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;
import ru.nsu.sidey383.lab1.walker.DUFileVisitor;
import ru.nsu.sidey383.lab1.walker.DUAction;
import ru.nsu.sidey383.lab1.walker.DUSystemFileWalker;

import java.nio.file.Path;
import java.util.*;

public class FileTree {

    private final boolean followLinks;

    private DUSystemFileWalker walker = null;

    private final List<DUPathException> errors;

    private FileTree(boolean followLinks) {
        this.followLinks = followLinks;
        this.errors = new ArrayList<>();
    }

    public static FileTree calculateTree(Path path, boolean followLinks) {
        // CR(minor): i think it would be better to create FIleTree after walkFiles:
        // CR(minor): this way we avoid non-static class
        FileTree tree = new FileTree(followLinks);
        tree.walker = DUSystemFileWalker.walkFiles(path, tree.new TreeVisitor());
        return tree;
    }

    /**
     * @return корневой файл дерева.
     */
    @Nullable
    public DUFile getBaseFile() {
        return walker == null ? null : walker.getRootFile();
    }

    /**
     * @return все {@link DUPathException}, созданные и подавленные при построении дерева.
     */
    public List<DUPathException> getErrors() {
        return List.copyOf(errors);
    }

    /**
     * Проверяет {@link FileTree#getErrors()} на пустой список.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private class TreeVisitor implements DUFileVisitor {

        /**
         * The key and value in this map are the same object
         **/
        // CR: IdentityHashMap? (haven't checked, mb ok)
        private final Map<DUFile, DUFile> visitedLinks = new HashMap<>();

        void readErrors(DUFile f) {
            if (f instanceof WrongDUFile wrf)
                errors.add(wrf.getPathException());
        }

        /**
         * If this child contains a parent adds a parent-child relationship
         *
         * @param child child file
         * **/
        private static void addChildToParent(DUFile child) {
            child.getParent().ifPresent(p -> p.addChild(child));
        }

        /**
         * Синхронизует отношения файлов потомок-родитель.
         */
        @Override
        public void visitFile(DUFile file) {
            readErrors(file);
            addChildToParent(file);
        }

        @Override
        public DUAction preVisitParentFile(ParentDUFile directory) {
            readErrors(directory);
            if (directory.getFileType() == DUFileType.LINK) {
                if (followLinks) {
                    // CR: let's discuss on next offline code review
                    DUFile link = visitedLinks.merge(directory, directory, (duFile, duFile2) -> {
                        duFile2.getParent().ifPresent(p -> p.addChild(duFile));
                        return duFile;
                    });
                    if (link != directory)
                        return DUAction.STOP;
                } else {
                    addChildToParent(directory);
                    return DUAction.STOP;
                }
            }
            return DUAction.CONTINUE;
        }

        @Override
        public void postVisitParentFile(ParentDUFile directory) {
            addChildToParent(directory);
        }

        /**
         * Собирает все ошибки.
         *
         * @see FileTree#getErrors()
         */
        @Override
        public void pathVisitError(@Nullable Path path, @NotNull DUPathException e) {
            errors.add(e);
        }

        @Override
        public void directoryCloseError(@NotNull Path path, @NotNull DUPathException e) {
            errors.add(e);
        }

    }

}
