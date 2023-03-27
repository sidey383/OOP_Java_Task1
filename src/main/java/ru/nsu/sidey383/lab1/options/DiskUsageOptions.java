package ru.nsu.sidey383.lab1.options;

import org.jetbrains.annotations.NotNull;
import ru.nsu.sidey383.lab1.exception.DUOptionReadException;
import ru.nsu.sidey383.lab1.write.size.SizeSuffix;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixIEC;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixISU;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * конфигурация disk usage.
 * <p> Для создание конфигурации используйте {@link DiskUsageOptions#builder()}.
 *
 * @see ru.nsu.sidey383.lab1.options.FileTreeOptions
 * @see ru.nsu.sidey383.lab1.options.FilesPrintOptions
 */
public class DiskUsageOptions implements FileTreeOptions, FilesPrintOptions {

    private final boolean followLinks;

    private final int maxDepth;

    private final int fileInDirLimit;

    private final Path filePath;

    private final SizeSuffix sizeSuffix;

    private final boolean help;

    private DiskUsageOptions(boolean help,
                             boolean followLinks,
                             int maxDepth,
                             int fileInDirLimit,
                             @NotNull Path filePath,
                             @NotNull SizeSuffix sizeSuffix) {
        this.help = help;
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

    public boolean help() {
        return help;
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

        private boolean help = false;

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

        public DiskUsageOptionsBuilder setHelp(boolean help) {
            this.help = help;
            return this;
        }

        public DiskUsageOptionsBuilder applyConsoleArgs(String[] args) throws DUOptionReadException {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--depth" -> {
                        if (++i < args.length) {
                            withMaxDepth(parsePositiveInt(args[i]));
                        } else {
                            throw new DUOptionReadException(getDepthError());
                        }
                    }
                    case "-L" -> withFollowLinks(true);
                    case "--size-format" -> {
                        if (++i >= args.length)
                            throw new DUOptionReadException(getSizeFormatError());
                        switch (args[i]) {
                            case "IEC" -> withSizeSuffix(SizeSuffixIEC.BYTE);
                            case "ISU" -> withSizeSuffix(SizeSuffixISU.BYTE);
                            default -> throw new DUOptionReadException(getSizeFormatError());
                        }
                    }
                    case "--limit" -> {
                        if (++i >= args.length)
                            throw new DUOptionReadException(getLimitError());
                        withFileInDirLimit(parsePositiveInt(args[i]));
                    }
                    case "-h", "-help" -> setHelp(true);
                    default -> {
                        Path path = Path.of(args[i]);
                        if (!Files.exists(path))
                            throw new DUOptionReadException(getFileError());
                        withFilePath(path);
                    }
                }
            }
            return this;
        }

        private static int parsePositiveInt(String arg) throws DUOptionReadException {
            try {
                int n = Integer.parseInt(arg);
                if (n > 0) {
                    return n;
                } else {
                    throw new DUOptionReadException("Received " + arg + ". A positive number is expected");
                }
            } catch (NumberFormatException e) {
                throw new DUOptionReadException("Received " + arg + ". A positive number is expected");
            }
        }

        private static String getDepthError() {
            return "The depth must be an integer greater than zero";
        }

        private static String getSizeFormatError() {
            return "Size format can take IEC or ISU values";
        }

        private static String getLimitError() {
            return "The limit must be an integer greater than zero";
        }

        private static String getFileError() {
            return "The file must exist";
        }

        public DiskUsageOptions build() {
            return new DiskUsageOptions(help, followLinks, maxDepth, fileInDirLimit, filePath, sizeSuffix);
        }
    }
}
