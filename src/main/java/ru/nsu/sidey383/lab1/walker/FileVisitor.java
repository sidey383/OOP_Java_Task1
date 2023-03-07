package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;

import java.nio.file.Path;

public interface FileVisitor {

    /**
     * Вызывается при посещении файла не являющегося {@link DirectoryFile}.
     * **/
    void visitFile(File file);

    /**
     * Вызывается при первой встрече директории {@link DirectoryFile}, до обхода её потомков.
     * @return Необходимо ли обходить потомков этой директории.
     * **/
    NextAction preVisitDirectory(DirectoryFile directory);

    /**
     * Вызывается после обхода всех потомков этой директории.
     * Не вызывается после возвращения {@link NextAction#STOP} из {@link FileVisitor#preVisitDirectory(DirectoryFile)}
     * **/
    void postVisitDirectory(DirectoryFile directory);

    /**
     * Вызывается при ошибке чтения метаданных файла
     **/
    void pathVisitError(@Nullable Path path, @NotNull PathException e);

}
