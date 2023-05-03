package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

        private final Map<DUFile, DUFile> visitedFiles = new HashMap<>();

        private void addChildToParent(DUFile f) {
            ParentDUFile parent = f.getParent();
            if (parent != null)
                parent.addChild(f);
        }

        /**
         * Функция для оригинальных файлов.
         * <p>Проверяет пройден ли файл. Если он уже пройден (по ссылке), то устанавливает ему корректного родителя.
         * @return true если файл уже был пройдет
         * <p> false если файл ещё не был пройден
         * **/
        boolean checkOriginFile(DUFile file) {
            DUFile mapValue = visitedFiles.merge(file, file, (originFile, newFile) -> {
                ParentDUFile parent = newFile.getParent();
                if (parent != null) {
                    parent.addChild(originFile);
                    originFile.setParent(parent);
                }
                return originFile;
            });
            return mapValue != file;
        }

        void checkWrongFile(DUFile f) {
            if (f instanceof WrongDUFile wrf)
                errors.add(wrf.getPathException());
        }

        /**
         * Синхронизует отношения файлов потомок-родитель.
         */
        @Override
        public void visitFile(DUFile file) {
            checkWrongFile(file);
            if (checkOriginFile(file))
                return;
            addChildToParent(file);
            if (file instanceof LinkDUFile<? extends DUFile> link) {
                checkLinkFile(link);
            }
        }

        @Override
        public DUAction preVisitParentFile(ParentDUFile directory) {
            checkWrongFile(directory);
            if (checkOriginFile(directory))
                return DUAction.STOP;

            addChildToParent(directory);
            if (directory instanceof LinkDUFile<? extends DUFile> link) {
                if (!followLinks)
                    return DUAction.STOP;
                if (checkLinkFile(link)) {
                    return DUAction.STOP;
                }
            }
            return DUAction.CONTINUE;
        }

        @Override
        public void postVisitDirectory(ParentDUFile directory) {
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
