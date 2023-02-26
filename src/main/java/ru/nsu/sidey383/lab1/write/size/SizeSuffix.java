package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;

public interface SizeSuffix {

    @NotNull
    String getSuffix(long size);

    @NotNull
    SizeSuffix getBaseSuffix();

}
