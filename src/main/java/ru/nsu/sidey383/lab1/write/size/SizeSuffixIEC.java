package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public enum SizeSuffixIEC implements SizeSuffix {
    GIGABYTE("GiB", null, null, false), MEGABYTE("MiB",GIGABYTE, 1024, false), KILOBYTE("KiB", MEGABYTE, 1024, false), BYTE("Byte", KILOBYTE, 1024, true);

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

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
    public SizeSuffix getByteSuffix() {
        return BYTE;
    }

    // CR: Duplicate code. Maybe use Abstract class to generify it?
    private String getSuffix(long size, double prevPart) {
        if (size < 0 || prevPart < 0 || prevPart > 1)
            throw new IllegalArgumentException(
                    String.format("%s size=%d previsionPart=%f", this, size, prevPart)
            );
        if (nextSize == null || nextSuffix == null || size < nextSize) {
            if (isAtomic) {
                return size + " " + suffix;
            } else {
                return decimalFormat.format(size + prevPart) + " " + suffix;
            }
        }
        return nextSuffix.getSuffix(size / nextSize, ((double)(size % nextSize)) / nextSize);
    }

}

