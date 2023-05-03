package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

public interface DUFileVisitor {

    /**
     * Вызывается при посещении файла не являющегося {@link ParentDUFile}.
     */
    void visitFile(DUFile file);

    /**
     * Вызывается при первой встрече директории {@link ParentDUFile}, до обхода её потомков.
     *
     * @return необходимо ли обходить потомков этой директории.
     */
    DUAction preVisitParentFile(ParentDUFile directory);

    /**
     * Вызывается после обхода всех потомков этой директории.
     * <p>Не вызывается после возвращения {@link DUAction#STOP} из {@link DUFileVisitor#preVisitParentFile(ParentDUFile)}
     */
    void postVisitDirectory(ParentDUFile directory);

    /**
     * Вызывается при ошибке чтения метаданных файла.
     */
    void pathVisitError(@Nullable Path path, @NotNull DUPathException e);

    void directoryCloseError(@NotNull Path path, @NotNull DUPathException e);

}
