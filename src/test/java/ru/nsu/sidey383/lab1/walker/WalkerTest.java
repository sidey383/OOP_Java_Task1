package ru.nsu.sidey383.lab1.walker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WalkerTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    @DisplayName("Check all file visiting for simple sile system")
    public void allFileTest() throws IOException, PathException {
        List<File> files = fileSystem.getAllPaths().stream().map(path -> {
            try {
                return File.readFile(path);
            } catch (IOException | PathException e) {
                throw new RuntimeException(e);
            }
        }).filter(f -> !(f instanceof DirectoryFile)).toList();
        List<DirectoryFile> dirs = fileSystem.getAllPaths().stream().map(path -> {
            try {
                return File.readFile(path);
            } catch (IOException | PathException e) {
                throw new RuntimeException(e);
            }
        }).filter(f -> f.getFileType() == FileType.DIRECTORY).map(f -> (DirectoryFile) f).toList();
        TestFileVisitor visitor = new TestFileVisitor(files, dirs);
        SystemFileWalker walker = SystemFileWalker.walkFiles(fileSystem.getRootDir(), visitor);
        assertEquals(new ArrayList<>(), walker.getSuppressedExceptions(), "Some suppressed exception in file walker ");
        visitor.checkOnEmpty();

    }

}
