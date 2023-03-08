package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;

/**
 * Конвертирует размер файла в строковое представление.
 */
public interface SizeSuffix {

    /**
     * Конвертирует размер файла в строковое представление.
     */
    @NotNull
    String getValue(long size);

    /**
     * @return базовый объект класса. Этот объект корректно конвертирует размер в байтах в строку.
     */
    @NotNull
    SizeSuffix getByteSuffix();

}
