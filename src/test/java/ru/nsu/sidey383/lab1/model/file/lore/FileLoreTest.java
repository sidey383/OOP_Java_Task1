package ru.nsu.sidey383.lab1.model.file.lore;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.FileWithSize;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class FileLoreTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    @Order(1)
    @DisplayName("Base check FileLore#equals()")
    void simpleEqualsTest() throws IOException, PathException {
        List<Path> files = fileSystem.getAllPaths();
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
    @Order(2)
    @DisplayName("Check FileLore#equals() by different path")
    void differentPathEqualsTest() throws IOException, PathException {
        List<List<Path>> equalsFilesLists = fileSystem.getEqualsPathsLists();

        for (int i = 1; i < equalsFilesLists.size(); i++) {
            List<Path> fileList1 = equalsFilesLists.get(i);
            for (int j = 0; j < i; j++) {
                List<Path> fileList2 = equalsFilesLists.get(j);
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
    @Order(3)
    @DisplayName("Check size of file calculating")
    public void sizeTest() throws IOException, PathException {
        for (FileWithSize fws : fileSystem.getFileWithSizes()) {
            FileLore lore = FileLore.createFileLore(fws.path());
            Assertions.assertEquals(lore.resolvedSize(), fws.size());
        }
    }

    private void checkFileEquals(Path p1, Path p2) throws IOException, PathException {
        FileLore f1 = FileLore.createFileLore(p1);
        FileLore f2 = FileLore.createFileLore(p2);
        assertAll("Compare file lore from " + p1 + " and " + p2 + ". Except equals.",
                () -> assertEquals(f1, f2, "fileLore1.equals(fileLore2) return false"),
                () -> assertEquals(f1.hashCode(), f2.hashCode(), "fileLore1.hashCode() == fileLore2.hashCode() return false"),
                () -> assertEquals(f1.originalSize(), f2.originalSize(), "fileLore1.originalSize() == fileLore2.originalSize() return false"),
                () -> assertEquals(f1.resolvedSize(), f2.resolvedSize(), "fileLore1.resolvedSize() == fileLore2.resolvedSize() return false"),
                () -> assertEquals(f1.originalPath(), f2.originalPath(), "fileLore1.originalPath().equals(fileLore2.originalPath()) return false"),
                () -> assertEquals(f1.resolvedPath(), f2.resolvedPath(), "fileLore1.resolvedPath().equals(fileLore2.resolvedPath()) return false")
        );
    }

    private void checkFileNotEquals(Path p1, Path p2) throws IOException, PathException {
        FileLore f1 = FileLore.createFileLore(p1);
        FileLore f2 = FileLore.createFileLore(p2);
        assertAll("Compare file lore from " + p1 + " and " + p2 + ". Except not equals.",
                () -> assertNotEquals(f1, f2, "fileLore1.equals(fileLore2) return true"),
                () -> assertFalse(f1.originalPath().equals(f2.originalPath()) && f1.resolvedPath().equals(f2.resolvedPath()), "Original and resolved path in FileLore equals")
        );
    }


}
