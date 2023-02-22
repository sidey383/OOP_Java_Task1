package ru.nsu.sidey383.lab1.write;

import org.jetbrains.annotations.NotNull;

public enum SizeSuffixISU implements SizeSuffix {

    GIGABYTE("GB", null, null), MEGABYTE("MB", GIGABYTE, 1000), KILOBYTE("KB", MEGABYTE, 1000), BYTE("Byte", KILOBYTE, 1000);

    private final String suffix;

    private final SizeSuffixISU nextSuffix;

    private final Integer nextSize;

    SizeSuffixISU(String suffix, SizeSuffixISU nextSuffix, Integer nextSize) {
        this.suffix = suffix;
        this.nextSuffix = nextSuffix;
        this.nextSize = nextSize;
    }

    @NotNull
    public String getSuffix(long size) {
        if (size < nextSize)
            return String.format("%d %s", size, suffix);
        return getSuffix(size, 0);
    }

    @NotNull
    public SizeSuffix getBaseSuffix() {
        return BYTE;
    }

    private String getSuffix(long size, double prevPart) {
        if (size < 0 || prevPart < 0 || prevPart > 1)
            throw new IllegalArgumentException(
                    String.format("%s size=%d previsionPart=%f", this, size, prevPart)
            );
        if (nextSize == null || nextSuffix == null || size < nextSize) {
            return String.format("%.2f %s", size + prevPart, suffix);
        }
        return nextSuffix.getSuffix(size / nextSize, ((double) (size % nextSize)) / nextSize);
    }


}
