package ru.nsu.sidey383.lab1.options;

import ru.nsu.sidey383.lab1.write.SizeSuffix;

public interface FilesPrintOptions {

    int getMaxDepth();

    int getFileInDirLimit();

    SizeSuffix getByteSizeSuffix();

}
