package ru.nsu.sidey383.lab1.write;

import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.model.file.LinkFile;
import ru.nsu.sidey383.lab1.model.file.ParentFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.options.FilesPrintOptions;
import ru.nsu.sidey383.lab1.write.size.SizeSuffix;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Stack;

public class FileTreePrinter {

    private final int maxDepth;

    private final int fileInDirLimit;

    private final SizeSuffix sizeSuffix;

    public FileTreePrinter(FilesPrintOptions options) {
        this.maxDepth = options.getMaxDepth();
        this.fileInDirLimit = options.getFileInDirLimit();
        this.sizeSuffix = options.getByteSizeSuffix().getByteSuffix();
    }

    /**
     * Читает все файлы в файловом дереве и выводит в поток.
     * <p> Применяет переданную конфигурацию {@link  FileTreePrinter#FileTreePrinter(FilesPrintOptions)}
     * <p> Выводит все файлы в директории в порядке уменьшения размера.
     */
    public void printTree(PrintStream stream, File root) {
        Stack<Iterator<File>> dirStack = new Stack<>();
        File now = root;
        do {
            stream.print("  ".repeat(dirStack.size()));
            stream.println(prettyFileString(now));
            if (now instanceof ParentFile dir && dirStack.size() < maxDepth) {
                dirStack.add(
                        dir.getChildren().stream()
                                .sorted((f1, f2) -> (int) Math.signum(f2.getSize() - f1.getSize()))
                                .limit(fileInDirLimit)
                                .iterator()
                        );
            }
            now = null;
            while (!dirStack.isEmpty()) {
                Iterator<File> iterator = dirStack.peek();
                if (iterator.hasNext()) {
                    now = iterator.next();
                    break;
                }
                dirStack.pop();
            }
        } while (now != null);
    }

    /**
     * @return красивое представление файла.
     */
    public String prettyFileString(File file) {
        StringBuilder builder = new StringBuilder();
        FileType type = file.getFileType();

        if (type.isDirectory()) {
            builder.append("/");
        }

        Path fileName = file.getPath().getFileName();
        // getFileName() will return null for root of file system, check this
        if (fileName == null) {
            builder.append(file.getPath()).append(" ");
        } else {
            builder.append(fileName).append(" ");
        }

        if (file instanceof LinkFile<?> linkFile) {
            builder.append("[link ").append(linkFile.getLinkedFile().getPath()).append("]");
        } else if (type == FileType.OTHER) {
            builder.append("[unknown type]");
        } else {
            builder.append("[").append(sizeSuffix.getValue(file.getSize())).append("]");
        }

        return builder.toString();
    }

}
