package ru.nsu.sidey383.lab1.tree.write;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.FileTree;
import ru.nsu.sidey383.lab1.options.FilesPrintOptions;
import ru.nsu.sidey383.lab1.tree.TreeTestFileSystem;
import ru.nsu.sidey383.lab1.write.FileTreePrinter;
import ru.nsu.sidey383.lab1.write.size.SizeSuffix;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixIEC;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DUFileTreePrinterTest {

    @RegisterExtension
    public static final TreeTestFileSystem fileSystem = new TreeTestFileSystem();

    private static FilesPrintOptions getOptions(int maxDepth, int fileLimit) {
        return new FilesPrintOptions() {
            @Override
            public int getMaxDepth() {
                return maxDepth;
            }
            @Override
            public int getFileInDirLimit() {
                return fileLimit;
            }
            @Override
            public SizeSuffix getByteSizeSuffix() {
                return SizeSuffixIEC.BYTE;
            }
        };
    }

    private void deepTest(Path p, int maxDepth) {
        FileTree tree = FileTree.calculateTree(p, true);
        FileTreePrinter printer = new FileTreePrinter(getOptions(maxDepth, 100));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer.printTree(new PrintStream(outputStream), tree.getBaseFile());
        String str = outputStream.toString(Charset.defaultCharset());
        Arrays.stream(str.split("\n"))
                .forEach(line ->
                        assertFalse(
                                line.startsWith(" ".repeat(maxDepth * 2 + 1)),
                                "Too many space in printed tree")
                );
        assertTrue(str.contains(" ".repeat(maxDepth * 2)), "Not enough deep");
    }

    @Test
    @DisplayName("Simple deep limit test")
    public void deepTest() {
        for (int i = 0; i < 11; i++) {
            deepTest(fileSystem.getRoot(), i);
        }
    }

    private void fileDeepTest(Path p, int maxDepth) {
        FileTree tree = FileTree.calculateTree(p, true);
        FileTreePrinter printer = new FileTreePrinter(getOptions(maxDepth, 100));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer.printTree(new PrintStream(outputStream), tree.getBaseFile());
        String str = outputStream.toString(Charset.defaultCharset());
        Arrays.stream(str.split("\n"))
                .forEach(line ->
                        assertFalse(
                                line.startsWith(" "),
                                "Too many space in printed tree")
                );
    }

    @Test
    @DisplayName("Checking a printed deep from a single file")
    public void fileTreeTest() {
        for (Path p : fileSystem.getFiles()) {
            for (int i = 0; i < 10; i++) {
                fileDeepTest(p, i);
            }
        }
    }

    @Test
    @DisplayName("Checking a printed deep from a empty folders")
    public void directoryTreeTest() {
        for (Path p : fileSystem.getEmptyFolders()) {
            for (int i = 0; i < 10; i++) {
                fileDeepTest(p, i);
            }
        }
    }

    private void linkDeepTest(Path p, int maxDepth) {
        FileTree tree = FileTree.calculateTree(p, false);
        FileTreePrinter printer = new FileTreePrinter(getOptions(maxDepth, 100));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer.printTree(new PrintStream(outputStream), tree.getBaseFile());
        String str = outputStream.toString(Charset.defaultCharset());
        Arrays.stream(str.split("\n"))
                .forEach(line ->
                        assertFalse(
                                line.startsWith(" "),
                                "Too many space in printed tree")
                );
    }

    @Test
    @DisplayName("Checking a printed deep from a links")
    public void linkTreeTest() {
        for (Path p : fileSystem.getLinks()) {
            for (int i = 0; i < 10; i++) {
                linkDeepTest(p, i);
            }
        }
    }

    void fileLimit(Path p, int fileLimit) {
        FileTree tree = FileTree.calculateTree(p, false);
        FileTreePrinter printer = new FileTreePrinter(getOptions(7, fileLimit));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer.printTree(new PrintStream(outputStream), tree.getBaseFile());
        String[] lines = outputStream.toString(Charset.defaultCharset()).split("\n");
        int spaceCount = 0;
        int fileCount = 0;
        boolean flage = false;
        for (String line : lines) {
            int newSpaceCount = 0;
            char[] lineAr = line.toCharArray();
            for (int i = 0; i < lineAr.length && lineAr[i] == ' '; i++) {
                newSpaceCount++;
            }
            if (spaceCount == newSpaceCount) {
                fileCount++;
            } else {
                spaceCount = newSpaceCount;
                fileCount = 1;
            }
            if (fileCount == fileLimit)
                flage = true;
            assertNotEquals(fileLimit + 1, fileCount, "Too many files with one count of spaces in row");
        }
        assertTrue(flage, "Don't found max count of files in some directory for dir " + p + " and file count " + fileLimit);
    }

    @Test
    @DisplayName("file limit test")
    public void fileLimitTest() {
        for (int i = 1; i < 11; i++) {
            fileLimit(fileSystem.getRoot(), i);
        }
    }

}
