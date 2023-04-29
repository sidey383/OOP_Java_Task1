package ru.nsu.sidey383.lab1.write;

import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.LinkDUFile;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
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
    public void printTree(PrintStream stream, DUFile root) {
        Stack<Iterator<DUFile>> dirStack = new Stack<>();
        DUFile now = root;
        do {
            stream.print("  ".repeat(dirStack.size()));
            stream.println(prettyFileString(now));
            if (now instanceof ParentDUFile dir && dirStack.size() < maxDepth) {
                dirStack.add(
                        dir.getChildren().stream()
                                .sorted((f1, f2) -> (int) Math.signum(f2.getSize() - f1.getSize()))
                                .limit(fileInDirLimit)
                                .iterator()
                        );
            }
            now = null;
            while (!dirStack.isEmpty()) {
                Iterator<DUFile> iterator = dirStack.peek();
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
    public String prettyFileString(DUFile file) {
        StringBuilder builder = new StringBuilder();
        DUFileType type = file.getFileType();

        if (type.isDirectory()) {
            builder.append("/");
        }

        // CR: maybe add method getName for DUFile and move this logic to this method?
        Path fileName = file.getPath().getFileName();
        // getFileName() will return null for root of file system, check this
        if (fileName == null) {
            builder.append(file.getPath()).append(" ");
        } else {
            builder.append(fileName).append(" ");
        }

        if (file instanceof LinkDUFile<?> linkFile) {
            builder.append("[link ").append(linkFile.getLinkedFile().getPath()).append("]");
        } else if (type == DUFileType.OTHER) {
            builder.append("[unknown type]");
        } else {
            builder.append("[").append(sizeSuffix.getValue(file.getSize())).append("]");
        }

        return builder.toString();
    }

}
