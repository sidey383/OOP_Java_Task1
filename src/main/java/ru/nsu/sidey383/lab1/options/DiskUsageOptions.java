package ru.nsu.sidey383.lab1.options;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.write.size.SizeSuffix;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixIEC;

import java.nio.file.Path;

/**
 * конфигурация disk usage
 * <p> Для создание конфигурации используйте {@link DiskUsageOptions#builder()}
 * @see ru.nsu.sidey383.lab1.options.FileTreeOptions
 * @see ru.nsu.sidey383.lab1.options.FilesPrintOptions
 * **/
public class DiskUsageOptions implements FileTreeOptions, FilesPrintOptions {

    private final boolean followLinks;

    private final int maxDepth;

    private final int fileInDirLimit;

    private final Path filePath;

    private final SizeSuffix sizeSuffix;

    /*
    * CR: Too much construction parameters length (> 120). Use line breaks.
    * */
    private DiskUsageOptions(boolean followLinks, int maxDepth, int fileInDirLimit, @NotNull Path filePath, @NotNull SizeSuffix sizeSuffix) {
        this.followLinks = followLinks;
        this.maxDepth = maxDepth;
        this.fileInDirLimit = fileInDirLimit;
        this.filePath = filePath;
        this.sizeSuffix = sizeSuffix;
    }

    @Override
    public boolean followLink() {
        return followLinks;
    }

    @NotNull
    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public int getMaxDepth() {
        return maxDepth;
    }

    @Override
    public int getFileInDirLimit() {
        return fileInDirLimit;
    }

    @Override
    public SizeSuffix getByteSizeSuffix() {
        return sizeSuffix;
    }

    public static DiskUsageOptionsBuilder builder() {
        return new DiskUsageOptionsBuilder();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static final class DiskUsageOptionsBuilder {
        private boolean followLinks = false;
        private int maxDepth = 10;
        private int fileInDirLimit = Integer.MAX_VALUE;
        private Path filePath = Path.of(".");

        private SizeSuffix sizeSuffix = SizeSuffixIEC.BYTE;

        private DiskUsageOptionsBuilder() {}

        public DiskUsageOptionsBuilder withFollowLinks(boolean followLinks) {
            this.followLinks = followLinks;
            return this;
        }

        public DiskUsageOptionsBuilder withMaxDepth(int maxDepth) {
            if (maxDepth <= 0) {
                throw new IllegalArgumentException("Max depth must de over zero");
            }
            this.maxDepth = maxDepth;
            return this;
        }

        public DiskUsageOptionsBuilder withFileInDirLimit(int fileInDirLimit) {
            if (fileInDirLimit <= 0) {
                throw new IllegalArgumentException("Limit files in dir must de over zero");
            }
            this.fileInDirLimit = fileInDirLimit;
            return this;
        }

        public DiskUsageOptionsBuilder withFilePath(Path filePath) {
            if (filePath == null) {
                throw new IllegalArgumentException("File path can't be null");
            }
            this.filePath = filePath;
            return this;
        }

        public DiskUsageOptionsBuilder withSizeSuffix(SizeSuffix sizeSuffix) {
            if (sizeSuffix == null) {
                throw new IllegalArgumentException("File path can't be null");
            }
            this.sizeSuffix = sizeSuffix;
            return this;
        }

        public DiskUsageOptions build() {
            return new DiskUsageOptions(followLinks, maxDepth, fileInDirLimit, filePath, sizeSuffix);
        }
    }
}
