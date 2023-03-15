package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;
import ru.nsu.sidey383.lab1.options.FileTreeOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FileTreeTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    public void testWithoutLinks() throws IOException, PathException {
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
        File f = tree.getBaseFile();
        assertNotNull(f, "FileTree#getBaseFile() after correct and of FileTree#calculateTree() must return null");
        assertEquals(fileSystem.getRootDir(), f.getOriginalPath(), "Path to root dir in FileTree constructor and path of base file in tree must be equals");

        testFilesInDirectory(f,
                Map.of(
                        FileType.REGULAR_FILE, 5L,
                        FileType.DIRECTORY, 1L
                ));

        Optional<DirectoryFile> attached = ((DirectoryFile) f).getChildren().stream().filter((f_) -> f_ instanceof DirectoryFile).map(f_ -> (DirectoryFile) f_).findFirst();
        assertTrue(attached.isPresent());

        testFilesInDirectory(attached.get(),
                Map.of(
                        FileType.REGULAR_FILE, 5L,
                        FileType.DIRECTORY_LINK, 1L
                ));

    }

    public void testFilesInDirectory(File dir , Map<FileType, Long> fileCount) {

        assertAll("Check child of directory file",
                () -> assertTrue(dir.getFileType().isDirectory(), "File not directory"),
                () -> assertEquals(fileCount.values().stream().mapToLong(Long::longValue).sum(), ((DirectoryFile)dir).getChildren().size(), "Wrong count of child"),
                () -> {
                    Set<File> files = ((DirectoryFile)dir).getChildren();
                    for (Map.Entry<FileType, Long> entry : fileCount.entrySet()) {
                        assertEquals(entry.getValue(), files.stream().filter(f -> f.getFileType().equals(entry.getKey())).count(), "Wrong type of " + entry.getKey() + " files");
                    }
                });
    }

}
