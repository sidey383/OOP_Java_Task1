package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public enum SizeSuffixISU implements SizeSuffix {

    GIGABYTE("GB", null, null, false), MEGABYTE("MB", GIGABYTE, 1000, false), KILOBYTE("KB", MEGABYTE, 1000, false), BYTE("Byte", KILOBYTE, 1000, true);

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private final String suffix;

    private final SizeSuffixISU nextSuffix;

    private final Integer nextSize;

    private final boolean isAtomic;

    SizeSuffixISU(String suffix, SizeSuffixISU nextSuffix, Integer nextSize, boolean isAtomic) {
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
        return nextSuffix.getSuffix(size / nextSize, ((double) (size % nextSize)) / nextSize);
    }


}
