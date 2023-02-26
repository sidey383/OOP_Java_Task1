package ru.nsu.sidey383.lab1.model.file;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;

import java.io.IOException;
import java.util.List;

public class FileTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    void simpleEqualsTest() {

        List<File> files = fileSystem.getAllPaths().stream().map(p -> {
            try {
                return File.readFile(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        for (int i = 0; i < files.size(); i++) {
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

        List<List<File>> equalsFilesLists = fileSystem.getEqualsPathsLists().stream()
                .map((list) ->
                        list.stream().map((path -> {
                            try {
                                return File.readFile(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        )).toList())
                .toList();

        for (int i = 1; i < equalsFilesLists.size(); i++) {
            List<File> fileList1 = equalsFilesLists.get(i);
            for (int j = 0; j < i; j++) {
                List<File> fileList2 = equalsFilesLists.get(j);
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

    private void checkFileEquals(File f1, File f2) {
        Assertions.assertEquals(f1, f2);
        Assertions.assertEquals(f1.hashCode(), f2.hashCode());
        Assertions.assertEquals(f1.getFileLore(), f2.getFileLore());
    }

    private void checkFileNotEquals(File f1, File f2) {
        Assertions.assertNotEquals(f1, f2);
        Assertions.assertNotEquals(f1.hashCode(), f2.hashCode());
        Assertions.assertNotEquals(f1.getFileLore(), f2.getFileLore());
    }
}
