package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.LinkFile;
import ru.nsu.sidey383.lab1.model.file.ParentFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.base.WrongFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;
import ru.nsu.sidey383.lab1.options.FileTreeOptions;
import ru.nsu.sidey383.lab1.walker.DUFileVisitor;
import ru.nsu.sidey383.lab1.walker.DUAction;
import ru.nsu.sidey383.lab1.walker.DUSystemFileWalker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileTree {

    private final Path basePath;

    private final boolean followLinks;

    private DUSystemFileWalker walker = null;

    private List<DUPathException> errors = List.of();

    public FileTree(FileTreeOptions options) {
        this.basePath = options.getFilePath();
        this.followLinks = options.followLink();
    }

    public void calculateTree() throws IOException, DUPathException {
        errors = new ArrayList<>();
        /*
        DUSystemFileWalker.walkFiles can throw exceptions.
        walker = null guarantees the correct state of the object.
         */
        walker = null;
        walker = DUSystemFileWalker.walkFiles(basePath, new TreeVisitor());
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
     * @return все {@link DUPathException}, созданные и подавленные при вызове {@link DUSystemFileWalker#walkFiles(Path, DUFileVisitor)}.
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

        private final HashSet<File> visitedFiles = new HashSet<>();

        private void addChildToParent(File f) {
            ParentFile parent = f.getParent();
            if (parent != null)
                parent.addChild(f);
        }

        /**
         * Функция для оригинальных файлов.
         * <p>Проверяет пройден ли файл. Если он уже пройден (по ссылке), то устанавливает ему корректного родителя.
         * @return true если файл уже был пройдет
         * <p> false если файл ещё не был пройден
         * **/
        boolean checkOriginFile(File file) {
            if (!visitedFiles.add(file)) {
                for (File f : visitedFiles) {
                    if (f.equals(file)) {
                        ParentFile parent = file.getParent();
                        if (parent != null) {
                            parent.addChild(f);
                            f.setParent(parent);
                        }
                        return true;
                    }
                }
                throw new IllegalStateException("File already contains in Set, but no equal file was found");
            }
            return false;
        }

        /**
         * Функция для ссылко на файл.
         * <p>Проверяет пройден ли файл, на который указывает ссылка. Если он уже пройден, то корректирует файл на который ссылается ссылка.
         * @return true если файл уже был пройдет
         * <p> false если файл ещё не был пройден
         * **/
        boolean checkLinkFile(LinkFile<? extends File> link) {
            File linkedFile = link.getLinkedFile();
            if (!visitedFiles.add(linkedFile)) {
                for (File f : visitedFiles) {
                    if (f.equals(linkedFile)) {
                        link.updateLinkedFile(f);
                        return true;
                    }
                }
                throw new IllegalStateException("File already contains in Set, but no equal file was found");
            }
            return false;
        }

        void checkWrongFile(File f) {
            if (f instanceof WrongFile wrf)
                errors.add(wrf.getPathException());
        }

        /**
         * Синхронизует отношения файлов потомок-родитель.
         */
        @Override
        public void visitFile(File file) {
            checkWrongFile(file);
            if (checkOriginFile(file))
                return;
            addChildToParent(file);
            if (file instanceof LinkFile<? extends File> link) {
                checkLinkFile(link);
            }
        }

        @Override
        public DUAction preVisitParentFile(ParentFile directory) {
            checkWrongFile(directory);
            if (checkOriginFile(directory))
                return DUAction.STOP;

            addChildToParent(directory);
            if (directory instanceof LinkFile<? extends File> link) {
                if (!followLinks)
                    return DUAction.STOP;
                if (checkLinkFile(link)) {
                    return DUAction.STOP;
                }
            }
            return DUAction.CONTINUE;
        }

        @Override
        public void postVisitDirectory(ParentFile directory) {
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

    }

}
