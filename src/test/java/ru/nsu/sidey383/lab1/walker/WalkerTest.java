package ru.nsu.sidey383.lab1.walker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;

import java.io.IOException;
import java.util.List;

public class WalkerTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    public void allFileTest() throws IOException {
        List<File> files = fileSystem.getAllPaths().stream().map(path -> {
            try {
                return File.readFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).filter(f -> !(f instanceof DirectoryFile)).toList();
        List<DirectoryFile> dirs = fileSystem.getAllPaths().stream().map(path -> {
            try {
                return File.readFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).filter(f -> f.getFileType() == FileType.DIRECTORY).map(f -> (DirectoryFile) f).toList();
        TestFileVisitor visitor = new TestFileVisitor(files, dirs);
        SystemFileWalker walker = SystemFileWalker.walkFiles(fileSystem.getRootDir(), visitor);
        Assertions.assertEquals(0, walker.getSuppressedExceptions().size());
        visitor.checkOnEmpty();

    }

}
