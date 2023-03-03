package ru.nsu.sidey383.lab1.walker;

import org.junit.jupiter.api.Assertions;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.FileType;

import java.util.Map;
import java.util.Set;

public final class WalkerTestUtils {

    private WalkerTestUtils() {}

    public static void testFilesInDirectory(File dir , Map<FileType, Long> fileCount) {
        Assertions.assertTrue(dir.getFileType().isDirectory());
        Set<File> files =  ((DirectoryFile)dir).getChildren();
        Assertions.assertEquals(fileCount.values().stream().mapToLong(Long::longValue).sum(), files.size());
        for (Map.Entry<FileType, Long> entry : fileCount.entrySet()) {
            Assertions.assertEquals(entry.getValue(), files.stream().filter(f -> f.getFileType().equals(entry.getKey())).count());
        }
    }

}
