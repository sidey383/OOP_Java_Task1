package ru.nsu.sidey383.lab1.walker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.ParentFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WalkerTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    @DisplayName("Check all file visiting for simple sile system")
    public void allFileTest() {
        List<File> files = fileSystem.getAllPaths().stream().map(File::readFile)
                .filter(f -> !(f instanceof ParentFile))
                .toList();
        List<ParentFile> dirs = fileSystem.getAllPaths().stream().map(File::readFile)
                .filter(f -> f.getFileType() == FileType.DIRECTORY)
                .map(f -> (ParentFile) f)
                .toList();
        TestFileVisitor visitor = new TestFileVisitor(files, dirs);
        DUSystemFileWalker walker = DUSystemFileWalker.walkFiles(fileSystem.getRootDir(), visitor);
        assertEquals(new ArrayList<>(), walker.getSuppressedExceptions(), "Some suppressed exception in file walker ");
        visitor.checkOnEmpty();

    }

}
