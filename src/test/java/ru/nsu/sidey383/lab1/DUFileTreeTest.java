package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;
import ru.nsu.sidey383.lab1.options.FileTreeOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DUFileTreeTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    public void testWithoutLinks() throws IOException, DUPathException {
        FileTree tree = new FileTree(new FileTreeOptions() {
            @Override
            public boolean followLink() {
                return false;
            }

            @Override
            public @NotNull Path getFilePath() {
                return fileSystem.getRootDir();
            }
        });

        assertNull(tree.getBaseFile(), "FileTree#getBaseFile() before FileTree#calculateTree() must return null");

        tree.calculateTree();
        DUFile f = tree.getBaseFile();
        assertNotNull(f, "FileTree#getBaseFile() after correct and of FileTree#calculateTree() must return null");
        assertEquals(fileSystem.getRootDir(), f.getPath(), "Path to root dir in FileTree constructor and path of base file in tree must be equals");

        testFilesInDirectory(f,
                Map.of(
                        DUFileType.REGULAR_FILE, 5L,
                        DUFileType.DIRECTORY, 1L
                ));

        Optional<ParentDUFile> attached = ((ParentDUFile) f).getChildren().stream().filter((f_) -> f_ instanceof ParentDUFile).map(f_ -> (ParentDUFile) f_).findFirst();
        assertTrue(attached.isPresent());

        testFilesInDirectory(attached.get(),
                Map.of(
                        DUFileType.REGULAR_FILE, 5L,
                        DUFileType.DIRECTORY_LINK, 1L
                ));

    }

    public void testFilesInDirectory(DUFile dir , Map<DUFileType, Long> fileCount) {

        assertAll("Check child of directory file",
                () -> assertTrue(dir.getFileType().isDirectory(), "File not directory"),
                () -> assertEquals(fileCount.values().stream().mapToLong(Long::longValue).sum(), ((ParentDUFile)dir).getChildren().size(), "Wrong count of child"),
                () -> {
                    Set<DUFile> files = ((ParentDUFile)dir).getChildren();
                    for (Map.Entry<DUFileType, Long> entry : fileCount.entrySet()) {
                        assertEquals(entry.getValue(), files.stream().filter(f -> f.getFileType().equals(entry.getKey())).count(), "Wrong type of " + entry.getKey() + " files");
                    }
                });
    }

}
