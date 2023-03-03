package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;

/**
 * Converter the file size into a pretty {@link String}
 * **/
public interface SizeSuffix {

    /**
     * Convert the file size into a pretty {@link String}
     * **/
    @NotNull
    String getSuffix(long size);

    /**
     * @return base instance of this class. This instance will correctly build string by size in bytes.
     * **/
    @NotNull
    SizeSuffix getByteSuffix();

}
