package ru.nsu.sidey383.lab1.walker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;

import java.util.List;

public class WalkerTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    @DisplayName("Check all file visiting for simple sile system")
    public void allFileTest() {
        List<DUFile> files = fileSystem.getAllPaths().stream().map(DUFile::readFile)
                .filter(f -> !(f instanceof ParentDUFile))
                .toList();
        List<ParentDUFile> dirs = fileSystem.getAllPaths().stream().map(DUFile::readFile)
                .filter(f -> f.getFileType() == DUFileType.DIRECTORY)
                .map(f -> (ParentDUFile) f)
                .toList();
        TestFileVisitor visitor = new TestFileVisitor(files, dirs);
        DUSystemFileWalker walker = DUSystemFileWalker.walkFiles(fileSystem.getRootDir(), visitor);
        visitor.checkOnEmpty();

    }

}
