package ru.nsu.sidey383.lab1.model.file;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Order(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class FileTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    @Order(1)
    @DisplayName("Base check File#equals()")
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
    @DisplayName("Check File#equals() by different path")
    void differentPathEqualsTest() throws IOException, PathException {

        List<List<Path>> equalsPathLists = fileSystem.getEqualsPathsLists();

        for (int i = 1; i < equalsPathLists.size(); i++) {
            List<Path> fileList1 = equalsPathLists.get(i);
            for (int j = 0; j < i; j++) {
                List<Path> fileList2 = equalsPathLists.get(j);
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

    private void checkFileEquals(Path p1, Path p2) throws IOException, PathException {
        File f1 = File.readFile(p1);
        File f2 = File.readFile(p2);
        assertAll("Compare files from " + p1 + " and " + p2 + ". Except equals.",
                () -> assertEquals(f1, f2, "file1.equals(file2) return false"),
                () -> assertEquals(f1.hashCode(), f2.hashCode(), "file1.hashCode() == file2.hashCode() return false"),
                () -> assertEquals(f1.getFileLore(), f2.getFileLore(), "file1.getFileLore().equals(file2.getFileLore()) return false"));
    }

    private void checkFileNotEquals(Path p1, Path p2) throws IOException, PathException {
        File f1 = File.readFile(p1);
        File f2 = File.readFile(p2);
        assertAll("Compare files from " + p1 + " and " + p2 + ". Expect not equals.z",
                () -> assertNotEquals(f1, f2, "file1.equals(file2) return true"),
                () -> assertNotEquals(f1.getFileLore(), f2.getFileLore(), "file1.getFileLore().equals(file2.getFileLore()) return true"));
    }
}
