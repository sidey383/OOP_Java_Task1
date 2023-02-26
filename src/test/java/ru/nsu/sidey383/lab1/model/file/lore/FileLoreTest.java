package ru.nsu.sidey383.lab1.model.file.lore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.FileWithSize;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;

import java.io.IOException;
import java.util.List;

public class FileLoreTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    void simpleEqualsTest() {
        List<FileLore> files = fileSystem.getAllPaths().stream().map(p -> {
            try {
                return FileLore.createFileLore(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        for (int i = 0; i < files.size(); i++) {
            Assertions.assertTrue(files.get(i).originalSize() >= 0 && files.get(i).resolvedSize() >= 0, "Wrong file size "+ files.get(i));
            for (int j = 0; j < files.size(); j++) {
                if (i == j) {
                    checkFileEquals(files.get(i), files.get(j));
                } else {
                    checkFileNotEquals(files.get(i), files.get(j));
                }
            }
        }
    }

    @Test
    void differentPathEqualsTest() {
        List<List<FileLore>> equalsFilesLists = fileSystem.getEqualsPathsLists().stream()
                .map((list) ->
                        list.stream().map((path -> {
                            try {
                                return FileLore.createFileLore(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        )).toList())
                .toList();

        for (int i = 1; i < equalsFilesLists.size(); i++) {
            List<FileLore> fileList1 = equalsFilesLists.get(i);
            for (int j = 0; j < i; j++) {
                List<FileLore> fileList2 = equalsFilesLists.get(j);
                for (int k = 0; k < fileList1.size(); k++) {
                    for (int l = 0; l < fileList2.size(); l++) {
                        if (k == l)
                            checkFileEquals(fileList1.get(k), fileList2.get(l));
                        else
                            checkFileNotEquals(fileList1.get(k), fileList2.get(l));
                    }
                }
            }
        }

    }

    @Test
    public void sizeTest() throws IOException {
        for (FileWithSize fws : fileSystem.getFileWithSizes()) {
            FileLore lore = FileLore.createFileLore(fws.path());
            Assertions.assertEquals(lore.resolvedSize(), fws.size());
        }
    }

    private void checkFileEquals(FileLore f1, FileLore f2) {
        Assertions.assertEquals(f1, f2);
        Assertions.assertEquals(f1.originalSize(), f2.originalSize());
        Assertions.assertEquals(f1.originalPath(), f2.originalPath());
        Assertions.assertEquals(f1.resolvedSize(), f2.resolvedSize());
        Assertions.assertEquals(f1.resolvedPath(), f2.resolvedPath());
        Assertions.assertEquals(f1.hashCode(), f2.hashCode());
    }

    private void checkFileNotEquals(FileLore f1, FileLore f2) {
        Assertions.assertNotEquals(f1, f2);
        Assertions.assertNotEquals(f1.hashCode(), f2.hashCode());
    }


}
