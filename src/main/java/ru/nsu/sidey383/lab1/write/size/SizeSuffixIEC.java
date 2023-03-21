package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public enum SizeSuffixIEC implements DefaultSizeSuffix {
    GIGABYTE("GiB", null, 0, false),
    MEGABYTE("MiB",GIGABYTE, 1024, false),
    KILOBYTE("KiB", MEGABYTE, 1024, false),
    BYTE("Byte", KILOBYTE, 1024, true);

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private final String suffix;

    private final SizeSuffixIEC nextSuffix;

    private final int nextSize;

    private final boolean isAtomic;

    SizeSuffixIEC(String suffix, SizeSuffixIEC nextSuffix, int nextSize, boolean isAtomic) {
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
    @NotNull
    public String getSuffix() {
        return suffix;
    }

    @Override
    @Nullable
    public SizeSuffixIEC getNextSuffix() {
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

