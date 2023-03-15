package ru.nsu.sidey383.lab1;

import ru.nsu.sidey383.lab1.exception.OptionReadException;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;
import ru.nsu.sidey383.lab1.options.DiskUsageOptions;
import ru.nsu.sidey383.lab1.write.FileTreeStringCreator;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixIEC;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixISU;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private Main() {}

    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equals("-h") || args[0].equals("-help"))) {
            System.out.println(usage());
            return;
        }
        DiskUsageOptions options;
        try {
            options = readOptions(args);
        } catch (OptionReadException e) {
            System.out.println(e.getMessage());
            System.out.println(usage());
            return;
        }
        FileTree fileTree = new FileTree(options);
        try {
            fileTree.calculateTree();
        } catch (PathException e) {
            System.err.println(e.toUserMessage());
            return;
        } catch (IOException e) {
            System.err.println("File tree build error");
            return;
        }
        if (fileTree.hasErrors())
            for (PathException error : fileTree.getErrors())
                System.err.println(error.toUserMessage());
        FileTreeStringCreator printer = new FileTreeStringCreator(options);
        System.out.println(printer.createString(fileTree.getBaseFile()));
    }

    private  static DiskUsageOptions readOptions(String[] args) throws OptionReadException {
        final var builder = DiskUsageOptions.builder();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--depth" -> {
                    if (++i < args.length) {
                        Integer n = parsePositiveInt(args[i]);
                        if (n == null)
                            throw new OptionReadException(getDepthError());
                        builder.withMaxDepth(n);
                    } else {
                        throw new OptionReadException(getDepthError());
                    }
                }
                case "-L" -> builder.withFollowLinks(true);
                case "--size-format" -> {
                    if (++i < args.length) {
                        switch (args[i]) {
                            case "IEC" -> builder.withSizeSuffix(SizeSuffixIEC.BYTE);
                            case "ISU" -> builder.withSizeSuffix(SizeSuffixISU.BYTE);
                            default -> throw new OptionReadException(getSizeFormatError());
                        }
                    } else {
                        throw new OptionReadException(getSizeFormatError());
                    }
                }
                case "--limit" -> {
                    if (++i < args.length) {
                        Integer n = parsePositiveInt(args[i]);
                        if (n == null)
                            throw new OptionReadException(getLimitError());
                        builder.withFileInDirLimit(n);
                    } else {
                        throw new OptionReadException(getLimitError());
                    }
                }
                default -> {
                    Path path = Path.of(args[i]);
                    if (!Files.exists(path))
                        throw new OptionReadException(getFileError());
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
                    --depth n
                        depth of recursion
                    -L
                        check symlink
                    --limit n
                        show the n heaviest files and/or directories
                    --size-format [IEC | ISU]
                        file size format
                """;
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


}
