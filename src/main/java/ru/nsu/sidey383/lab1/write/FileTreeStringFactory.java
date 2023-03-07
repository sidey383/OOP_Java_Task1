package ru.nsu.sidey383.lab1.write;

import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.options.FilesPrintOptions;
import ru.nsu.sidey383.lab1.write.size.SizeSuffix;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Stack;

public class FileTreeStringFactory {

    private final int maxDepth;

    private final int fileInDirLimit;

    private final SizeSuffix sizeSuffix;

    public FileTreeStringFactory(FilesPrintOptions options) {
        this.maxDepth = options.getMaxDepth();
        this.fileInDirLimit = options.getFileInDirLimit();
        this.sizeSuffix = options.getByteSizeSuffix().getByteSuffix();
    }

    /**
     * Читает все файлы в файловом дереве и первращает его в строковое представление.
     * <p> Применяет переданную конфигурацию {@link  FileTreeStringFactory#FileTreeStringFactory(FilesPrintOptions)}  FileTreeStringFactory}
     * <p> Выводит все файлы в директории в порядке уменьшения размера.
     * @return new {@link StringBuilder} containing a file tree string
     * **/
    public StringBuilder createString(File root) {
        Stack<Iterator<File>> dirStack = new Stack<>();
        File now = root;
        StringBuilder builder = new StringBuilder();
        do {
            builder.append("  ".repeat(dirStack.size())).append(prettyFileString(now));
            if (now instanceof DirectoryFile dir && dirStack.size() < maxDepth) {
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
            if (now != null)
                builder.append("\n");
        } while (now != null);
        return builder;
    }

    /**
     * @return Красивое представление файла
     * **/
    public String prettyFileString(File file) {
        StringBuilder builder = new StringBuilder();
        FileType type = file.getFileType();

        if (type.isDirectory()) {
            builder.append("/");
        }

        Path fileName = file.getOriginalPath().getFileName();
        // getFileName() will return null for root of file system, check this
        if (fileName == null) {
            builder.append(file.getOriginalPath()).append(" ");
        } else {
            builder.append(fileName).append(" ");
        }

        if (type.isLink()) {
            builder.append("[link ").append(file.getResolvedPath()).append("]");
        } else if (type == FileType.OTHER) {
            builder.append("[unknown type]");
        } else {
            builder.append("[").append(sizeSuffix.getSuffix(file.getSize())).append("]");
        }

        return builder.toString();
    }

}
