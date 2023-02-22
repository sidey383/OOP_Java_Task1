package ru.nsu.sidey383.lab1.write;

import org.jetbrains.annotations.NotNull;

public enum SizeSuffixIEC implements SizeSuffix {
    GIGABYTE("GiB", null, null), MEGABYTE("MiB",GIGABYTE, 1024), KILOBYTE("KiB", MEGABYTE, 1024), BYTE("Byte", KILOBYTE, 1024);

    private final String suffix;

    private final SizeSuffixIEC nextSuffix;

    private final Integer nextSize;

    SizeSuffixIEC(String suffix, SizeSuffixIEC nextSuffix, Integer nextSize) {
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
        return nextSuffix.getSuffix(size / nextSize, ((double)(size % nextSize)) / nextSize);
    }

}

