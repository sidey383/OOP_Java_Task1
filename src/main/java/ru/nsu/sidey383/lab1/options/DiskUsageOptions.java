package ru.nsu.sidey383.lab1.options;

import ru.nsu.sidey383.lab1.walker.SystemFileWalkerOptions;

import java.nio.file.Path;

public class DiskUsageOptions implements FileTreeOptions, FilesPrintOptions {

    private final SystemFileWalkerOptions[] options;

    private final int maxDepth;

    private final int fileInDirLimit;

    private final Path filePath;

    private DiskUsageOptions(Path filePath, SystemFileWalkerOptions[] options, int maxDepth, int fileInDirLimit) {
        this.options = options;
        this.maxDepth = maxDepth;
        this.fileInDirLimit = fileInDirLimit;
        this.filePath = filePath;
    }

    public SystemFileWalkerOptions[] getWalkerOptions() {
        return options;
    }

    public Path getFilePath() {
        return filePath;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getFileInDirLimit() {
        return fileInDirLimit;
    }

    public static DiskUsageOptionsBuilder builder() {
        return new DiskUsageOptionsBuilder();
    }

    public static class DiskUsageOptionsBuilder {

        private boolean useSymLink = false;

        private int maxDepth = 30;

        private int fileInDirLimit = Integer.MIN_VALUE;

        private Path path = Path.of(".");

        private DiskUsageOptionsBuilder() {}

        public DiskUsageOptionsBuilder setMaxDepth(int maxDepth) {
            if (maxDepth <= 0)
                throw new IllegalArgumentException("Max depth must be over zero");
            this.maxDepth = maxDepth;
            return this;
        }

        public DiskUsageOptionsBuilder setFileInDirLimit(int fileInDirLimit) {
            if (fileInDirLimit <= 0)
                throw new IllegalArgumentException("Limit of count files in directory must be over zero");
            this.fileInDirLimit = fileInDirLimit;
            return this;
        }

        public DiskUsageOptionsBuilder useSymLink(boolean useSymLink) {
            this.useSymLink = useSymLink;
            return this;
        }

        public DiskUsageOptionsBuilder setBasePath(Path path) {
            if (path == null)
                throw new IllegalArgumentException("Base path can't be null");
            this.path = path;
            return this;
        }

        public DiskUsageOptions build() {
            final SystemFileWalkerOptions[] options;
            if (useSymLink) {
                options = new SystemFileWalkerOptions[]{
                        SystemFileWalkerOptions.TO_REAL_PATH};
            } else {
                options = new SystemFileWalkerOptions[]{
                        SystemFileWalkerOptions.TO_REAL_PATH,
                        SystemFileWalkerOptions.NO_FOLLOW_LINKS_IN_REAL_PATH,
                        SystemFileWalkerOptions.NO_FOLLOW_LINKS};
            }
            return new DiskUsageOptions(path, options, maxDepth, fileInDirLimit);
        }

    }


}
