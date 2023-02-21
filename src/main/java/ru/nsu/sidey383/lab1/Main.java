package ru.nsu.sidey383.lab1;

import ru.nsu.sidey383.lab1.options.DiskUsageOptions;
import ru.nsu.sidey383.lab1.utils.FilePrintUtils;

import java.io.IOException;

public class Main {

    private Main() {}

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println(usage());
            return;
        }
        DiskUsageOptions options = readOptions(args);
        FileTree fileTree = new FileTree(options.getFilePath());
        fileTree.calculateTree();
        FilePrintUtils.printFiles(fileTree.getBaseFile(), options);
    }

    private static DiskUsageOptions readOptions(String[] args) {
        DiskUsageOptions.DiskUsageOptionsBuilder builder = DiskUsageOptions.builder();
        return builder.useSymLink(true).setMaxDepth(3).build();
    }

    private static String usage() {
        return """
                jdu <path to file>
                """;
    }

}
