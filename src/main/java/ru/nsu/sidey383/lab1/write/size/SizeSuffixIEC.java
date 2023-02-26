package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;

public enum SizeSuffixIEC implements SizeSuffix {
    GIGABYTE("GiB", null, null, false), MEGABYTE("MiB",GIGABYTE, 1024, false), KILOBYTE("KiB", MEGABYTE, 1024, false), BYTE("Byte", KILOBYTE, 1024, true);

    private final String suffix;

    private final SizeSuffixIEC nextSuffix;

    private final Integer nextSize;

    private final boolean isAtomic;

    SizeSuffixIEC(String suffix, SizeSuffixIEC nextSuffix, Integer nextSize, boolean isAtomic) {
        this.suffix = suffix;
        this.nextSuffix = nextSuffix;
        this.nextSize = nextSize;
        this.isAtomic = isAtomic;
    }

    @NotNull
    public String getSuffix(long size) {
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
            if (isAtomic) {
                return String.format("%d %s", size, suffix);
            } else {
                return String.format("%.2f %s", size + prevPart, suffix);
            }
        }
        return nextSuffix.getSuffix(size / nextSize, ((double)(size % nextSize)) / nextSize);
    }

}

