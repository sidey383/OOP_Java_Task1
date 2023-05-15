package ru.nsu.sidey383.lab1.model.file;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.model.file.base.LinkDUFile;
import ru.nsu.sidey383.lab1.model.file.base.WrongDUFile;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class DUFileTest {

    @RegisterExtension
    public static final FileTestFileSystem fileSystem = new FileTestFileSystem();

    @Test
    @Order(1)
    @DisplayName("Base check DUFile#equals()")
    void simpleEqualsTest() {

        List<Path> files = fileSystem.getIdenticalFileLists()[0];

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
    @DisplayName("Check DUFile#equals() by different path")
    void differentPathEqualsTest() {

        List<Path>[] equalsPathLists = fileSystem.getIdenticalFileLists();

        for (int i = 1; i < equalsPathLists.length; i++) {
            List<Path> fileList1 = equalsPathLists[i];
            for (int j = 0; j < i; j++) {
                List<Path> fileList2 = equalsPathLists[j];
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
    @DisplayName("Check wrong files")
    void wrongPathTest() {
        DUFile wrongFile = DUFile.readFile(fileSystem.getWrongPath());
        assertEquals(wrongFile.getClass(), WrongDUFile.class, "Wrong file " + wrongFile + " class");
        assertEquals(((WrongDUFile)wrongFile).getPathException().getCause().getClass(), NoSuchFileException.class, "Wrong exception class");
        DUFile wrongLink = DUFile.readFile(fileSystem.getWrongPathLink());
        assertEquals(wrongLink.getClass(), LinkDUFile.class, "Wrong file " + wrongLink + " class");
        assertEquals(DUFile.readFile(((LinkDUFile)wrongLink).getReference()), wrongFile, "Wrong linked file");
    }

    private void checkFileEquals(Path p1, Path p2) {
        DUFile f1 = DUFile.readFile(p1);
        DUFile f2 = DUFile.readFile(p2);
        assertEquals(f1, f2,
                "Compare files\n " +
                p1 + ": " + f1.toString() + "\n" +
                p2 + ": " + f2.toString() + "\n");
        assertEquals(f1.hashCode(), f2.hashCode(),
                "Compare files hash\n " +
                        p1 + ": " + f1.hashCode() + "\n" +
                        p2 + ": " + f2.hashCode() + "\n");
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
