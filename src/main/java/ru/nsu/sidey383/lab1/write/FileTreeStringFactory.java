package ru.nsu.sidey383.lab1.write;

import ru.nsu.sidey383.lab1.model.FileType;
import ru.nsu.sidey383.lab1.model.files.DirectoryFile;
import ru.nsu.sidey383.lab1.model.files.File;
import ru.nsu.sidey383.lab1.options.FilesPrintOptions;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class FileTreeStringFactory {

    private final int maxDepth;

    private final int fileInDirLimit;

    private final SizeSuffix sizeSuffix;

    public FileTreeStringFactory(FilesPrintOptions options) {
        this.maxDepth = options.getMaxDepth();
        this.fileInDirLimit = options.getFileInDirLimit();
        this.sizeSuffix = options.getByteSizeSuffix().getBaseSuffix();
    }

    public StringBuilder createString(File root) {
        Stack<Iterator<File>> dirStack = new Stack<>();
        File now = root;
        StringBuilder builder = new StringBuilder();
        do {
            builder.append("  ".repeat(dirStack.size())).append(prettyFileString(now));
            if (now instanceof DirectoryFile dir && dirStack.size() < maxDepth) {
                List<File> list = dir.getChildren();
                if (list.size() > fileInDirLimit) {
                    list.sort(
                            (f1, f2) -> (int) Math.signum(f2.getSize() - f1.getSize())
                    );
                    list = list.subList(0, fileInDirLimit);
                }
                dirStack.add(list.listIterator());
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

    public String prettyFileString(File file) {
        StringBuilder builder = new StringBuilder();
        FileType type = file.getFileType();

        if (type.isDirectory()) {
            builder.append("/");
        }

        Path fileName = file.getOriginalPath().getFileName(); // getFileName() return null for root of file system
        if (fileName == null) {
            builder.append(file.getOriginalPath()).append(" ");
        } else {
            builder.append(fileName).append(" ");
        }
        if (type.isLink()) {
            builder.append("[link ").append(file.getResolvedPath()).append("]");
        } else {
            builder.append("[").append(sizeSuffix.getSuffix(file.getSize())).append("]");
        }

        return builder.toString();
    }

}
