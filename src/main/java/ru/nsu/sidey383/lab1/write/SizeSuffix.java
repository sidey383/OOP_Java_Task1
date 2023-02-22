package ru.nsu.sidey383.lab1.write;

import org.jetbrains.annotations.NotNull;

public interface SizeSuffix {

    @NotNull
    public String getSuffix(long size);

    @NotNull
    public SizeSuffix getBaseSuffix();

}
