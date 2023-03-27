package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.ParentFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

public interface DUFileVisitor {

    /**
     * Вызывается при посещении файла не являющегося {@link ParentFile}.
     */
    void visitFile(File file);

    /**
     * Вызывается при первой встрече директории {@link ParentFile}, до обхода её потомков.
     *
     * @return необходимо ли обходить потомков этой директории.
     */
    DUAction preVisitParentFile(ParentFile directory);

    /**
     * Вызывается после обхода всех потомков этой директории.
     * <p>Не вызывается после возвращения {@link DUAction#STOP} из {@link DUFileVisitor#preVisitParentFile(ParentFile)}
     */
    void postVisitDirectory(ParentFile directory);

    /**
     * Вызывается при ошибке чтения метаданных файла.
     */
    void pathVisitError(@Nullable Path path, @NotNull DUPathException e);

}
