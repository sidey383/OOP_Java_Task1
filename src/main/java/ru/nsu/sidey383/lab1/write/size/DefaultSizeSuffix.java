package ru.nsu.sidey383.lab1.write.size;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public interface DefaultSizeSuffix extends SizeSuffix {

    @NotNull
    DecimalFormat getDecimalFormat();

    @NotNull
    String getSuffix();

    @Nullable
    DefaultSizeSuffix getNextSuffix();

    int getNextSize();

    boolean isIsAtomic();

    @Override
    @NotNull
    default String getValue(long size) {
        return getSuffix(size, 0);
    }

    @NotNull
    default String getSuffix(long size, double prevPart) {
        if (size < 0 || prevPart < 0 || prevPart > 1)
            throw new IllegalArgumentException(
                    String.format("%s size=%d previsionPart=%f", this, size, prevPart)
            );
        if (getNextSuffix() == null || getNextSize() <= 0 || size < getNextSize()) {
            if (isIsAtomic()) {
                return size + " " + getSuffix();
            } else {
                return getDecimalFormat().format(size + prevPart) + " " + getSuffix();
            }
        }
        return getNextSuffix().getSuffix(size / getNextSize(), ((double)(size % getNextSize())) / getNextSize());
    }

}
