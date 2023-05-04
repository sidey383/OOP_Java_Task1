package ru.nsu.sidey383.lab1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DUFileTreeTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    /*
    CR: more tests for different trees, e.g.:

    - empty dir is a root of tree
    - file is a root of tree
    - symlink is a root of tree
    - dir with one regular file is a root of tree
    - dir with one symlink is a root of tree
    - dir with one empty dir is a root of tree
    - file without read permissions

    (same cases for printer)
     */
    @Test
    public void testWithoutLinks() {

        FileTree tree = FileTree.calculateTree(fileSystem.getRootDir(), false);

        DUFile f = tree.getBaseFile();
        assertNotNull(f, "FileTree#getBaseFile() must be not null");
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
                        DUFileType.LINK, 1L
                ));

    }

    public void testFilesInDirectory(DUFile dir , Map<DUFileType, Long> fileCount) {

        assertAll("Check child of directory file",
                () -> assertNotSame(dir.getFileType(), DUFileType.DIRECTORY, "File not directory"),
                () -> assertEquals(fileCount.values().stream().mapToLong(Long::longValue).sum(), ((ParentDUFile)dir).getChildren().size(), "Wrong count of child"),
                () -> {
                    Collection<DUFile> files = ((ParentDUFile)dir).getChildren();
                    for (Map.Entry<DUFileType, Long> entry : fileCount.entrySet()) {
                        assertEquals(entry.getValue(), files.stream().filter(f -> f.getFileType().equals(entry.getKey())).count(), "Wrong type of " + entry.getKey() + " files");
                    }
                });
    }

}
