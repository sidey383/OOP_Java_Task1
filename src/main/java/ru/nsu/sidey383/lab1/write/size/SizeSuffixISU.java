package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public enum SizeSuffixISU implements DefaultSizeSuffix {

    GIGABYTE("GB", null, 0, false), MEGABYTE("MB", GIGABYTE, 1000, false), KILOBYTE("KB", MEGABYTE, 1000, false), BYTE("Byte", KILOBYTE, 1000, true);

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private final String suffix;

    private final SizeSuffixISU nextSuffix;

    private final int nextSize;

    private final boolean isAtomic;

    SizeSuffixISU(String suffix, SizeSuffixISU nextSuffix, int nextSize, boolean isAtomic) {
        this.suffix = suffix;
        this.nextSuffix = nextSuffix;
        this.nextSize = nextSize;
        this.isAtomic = isAtomic;
    }

    @NotNull
    public SizeSuffix getByteSuffix() {
        return BYTE;
    }

    @Override
    @NotNull
    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    @Override
    public @NotNull String getSuffix() {
        return suffix;
    }

    @Override
    @Nullable
    public SizeSuffixISU getNextSuffix() {
        return nextSuffix;
    }

    @Override
    public int getNextSize() {
        return nextSize;
    }

    @Override
    public boolean isIsAtomic() {
        return isAtomic;
    }


}
