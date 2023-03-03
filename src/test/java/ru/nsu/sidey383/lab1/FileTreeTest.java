package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;
import ru.nsu.sidey383.lab1.options.FileTreeOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FileTreeTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    public void testWithoutLinks() throws IOException {
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

        Assertions.assertNull(tree.getBaseFile());

        tree.calculateTree();
        File f = tree.getBaseFile();
        Assertions.assertNotNull(f);
        Assertions.assertEquals(fileSystem.getRootDir(), f.getOriginalPath());

        testFilesInDirectory(f,
                Map.of(
                        FileType.REGULAR_FILE, 5L,
                        FileType.DIRECTORY, 1L
                ));

        Optional<DirectoryFile> attached = ((DirectoryFile) f).getChildren().stream().filter((f_) -> f_ instanceof DirectoryFile).map(f_ -> (DirectoryFile) f_).findFirst();
        Assertions.assertTrue(attached.isPresent());

        testFilesInDirectory(attached.get(),
                Map.of(
                        FileType.REGULAR_FILE, 5L,
                        FileType.DIRECTORY_LINK, 1L
                ));

    }

    public void testFilesInDirectory(File dir , Map<FileType, Long> fileCount) {
        Assertions.assertTrue(dir.getFileType().isDirectory());
        Set<File> files =  ((DirectoryFile)dir).getChildren();
        Assertions.assertEquals(fileCount.values().stream().mapToLong(Long::longValue).sum(), files.size());
        for (Map.Entry<FileType, Long> entry : fileCount.entrySet()) {
            Assertions.assertEquals(entry.getValue(), files.stream().filter(f -> f.getFileType().equals(entry.getKey())).count());
        }
    }

}
