package ru.nsu.sidey383.lab1.utils;

public enum SizeSuffix {
    GIGABYTE("Gb", null, null), MEGABYTE("Mb",GIGABYTE, 1024), KILOBYTE("Kb", MEGABYTE, 1024), BYTE("Byte", KILOBYTE, 1024);

    private final String suffix;

    private final SizeSuffix nextSuffix;

    private final Integer nextSize;

    SizeSuffix(String suffix, SizeSuffix nextSuffix, Integer nextSize) {
        this.suffix = suffix;
        this.nextSuffix = nextSuffix;
        this.nextSize = nextSize;
    }

    public String getSuffix(long size) {
        if (size < nextSize)
            return String.format("%d %s", size, suffix);
        return getSuffix(size, 0);
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

