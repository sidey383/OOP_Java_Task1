package ru.nsu.sidey383.lab1;

import ru.nsu.sidey383.lab1.exception.DUOptionReadException;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;
import ru.nsu.sidey383.lab1.options.DiskUsageOptions;
import ru.nsu.sidey383.lab1.write.FileTreePrinter;

import java.io.IOException;

public class Main {

    private Main() {}

    public static void main(String[] args) {
        DiskUsageOptions options;
        try {
            options = DiskUsageOptions.builder().applyConsoleArgs(args).build();
        } catch (DUOptionReadException e) {
            System.err.println(e.getMessage());
            System.err.println(usage());
            return;
        }

        if (options.help()) {
            System.out.println(usage());
            return;
        }

        FileTree fileTree = new FileTree(options);

        try {
            fileTree.calculateTree();
        } catch (DUPathException e) {
            System.err.println(e.toUserMessage());
            return;
        } catch (IOException e) {
            System.err.println("File tree build error");
            e.printStackTrace(System.err);
            return;
        }

        if (fileTree.hasErrors())
            for (DUPathException error : fileTree.getErrors())
                System.err.println(error.toUserMessage());
        FileTreePrinter printer = new FileTreePrinter(options);

        printer.printTree(System.out, fileTree.getBaseFile());
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


}
