package ru.nsu.sidey383.lab1.model.file;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.generator.SimpleFileSystemGenerator;

import java.nio.file.Path;
import java.util.List;

@Order(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class DUFileTest {

    @RegisterExtension
    private static final SimpleFileSystemGenerator fileSystem = new SimpleFileSystemGenerator();

    @Test
    @Order(1)
    @DisplayName("Base check File#equals()")
    void simpleEqualsTest() {

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
    void differentPathEqualsTest() {

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

    @Test
    @Order(3)
    private void notDirectoryException() {
        Path path = Path.of("C:\\Users\\sidey\\programming\\java\\study\\oop_java\\OOP_Java_Task1\\.atest\\dir\\link\\wlink\\link\\wlink\\link\\text.txt");
        DUFile file = DUFile.readFile(path);
    }

    private void checkFileEquals(Path p1, Path p2) {
        DUFile f1 = DUFile.readFile(p1);
        DUFile f2 = DUFile.readFile(p2);
        assertEquals(f1, f2,
                "Compare files\n " +
                p1 + ": " + f1.toString() + "\n" +
                p2 + ": " + f2.toString() + "\n");
    }

    private void checkFileNotEquals(Path p1, Path p2) {
        DUFile f1 = DUFile.readFile(p1);
        DUFile f2 = DUFile.readFile(p2);
        assertNotEquals(f1, f2,
                "Compare files\n " +
                        p1 + ": " + f1.toString() + "\n" +
                        p2 + ": " + f2.toString() + "\n");
    }

}
