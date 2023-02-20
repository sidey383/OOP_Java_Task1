package ru.nsu.sidey383.lab1;

import ru.nsu.sidey383.lab1.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private Main() {}

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println(usage());
            return;
        }
        Path path = Path.of(args[0]);
        if (!Files.exists(path)) {
            System.out.println(usage());
            return;
        }
        FileTree fileTree = new FileTree(path);
        try {
            fileTree.initTree();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        appendFilesString(builder, fileTree.getBaseFile(), 0);
        System.out.println(builder);

    }

    private static void appendFilesString(StringBuilder builder, File file, int depth) {
        if (file == null) return;
        builder.append(" ".repeat(depth));
        switch (file) {
            case DirectoryFile directoryFile -> {
                builder.append(directoryFile).append("\n");
                directoryFile.getFilesStream().forEach(f -> appendFilesString(builder, f, depth + 1));
            }
            case SymLinkFile symLinkFile -> {
                builder.append(symLinkFile).append("\n");
                symLinkFile.getFilesStream().forEach(f -> appendFilesString(builder, f, depth + 1));
            }
            default ->  {
                builder.append(file).append("\n");
            }
        }
    }

    private static String usage() {
        return """
                jdu <path to file>
                """;
    }

}
