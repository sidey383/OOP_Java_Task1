package ru.nsu.sidey383.lab1;

import ru.nsu.sidey383.lab1.options.DiskUsageOptions;
import ru.nsu.sidey383.lab1.utils.FilePrintUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private Main() {}

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println(usage());
            return;
        }
        DiskUsageOptions options = readOptions(args);
        if (options == null) {
            System.out.println(usage());
            return;
        }
        FileTree fileTree = new FileTree(options.getFilePath());
        fileTree.calculateTree();
        FilePrintUtils.printFiles(fileTree.getBaseFile(), options);
    }

    private static DiskUsageOptions readOptions(String[] args) {
        final var builder = DiskUsageOptions.builder();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--depth" -> {
                    if (++i < args.length) {
                        Integer n = parsePositiveInt(args[i]);
                        if (n == null)
                            return null;
                        builder.withMaxDepth(n);
                    } else {
                        return null;
                    }
                }
                case "-L" -> {
                    builder.withFollowLinks(true);
                    return null;
                }
                case "--limit" -> {
                    if (++i < args.length) {
                        Integer n = parsePositiveInt(args[i]);
                        if (n == null)
                            return null;
                        builder.withFileInDirLimit(n);
                    } else {
                        return null;
                    }
                }
                default -> {
                    Path path = Path.of(args[i]);
                    if (!Files.exists(path))
                        return null;
                    builder.withFilePath(path);
                }
            }
        }
        return builder.build();
    }

    private static Integer parsePositiveInt(String arg) {
        try {
            int n = Integer.parseInt(arg);
            if (n > 0) {
                return n;
            } else {
                System.out.println(usage());
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println(usage());
            return null;
        }
    }

    private static String usage() {
        return """
                jdu [options] <path to file>
                """;
    }

}
