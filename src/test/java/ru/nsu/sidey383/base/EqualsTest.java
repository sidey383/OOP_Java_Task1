package ru.nsu.sidey383.base;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.*;
import ru.nsu.sidey383.lab1.model.files.File;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EqualsTest {

    private static FileSystem fileSystem;

    private static Path testDir;
    private static Path attachedDir;

    private static List<Path> simpleFiles = new ArrayList<>();

    private static List<Path> firstEqualsList = new ArrayList<>();

    private static List<Path> secondEqualsList = new ArrayList<>();

    private static List<Path> thirdEqualsList = new ArrayList<>();

    private static Path linkTestDir;


    @BeforeAll
    public static void createFiles() throws IOException {
        fileSystem = MemoryFileSystemBuilder.newEmpty().build("name");
        createDirs();
        linkTestDir = testDir.resolve("link");
        Files.createSymbolicLink(linkTestDir, attachedDir);
        simpleFiles.add(linkTestDir);
        for (int i = 0; i < 5; i++) {
            Path path = attachedDir.resolve( "file"+i);
            Files.createFile(path);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
                os.write(("test file " + i + "data").getBytes());
            }
            firstEqualsList.add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = attachedDir.resolve("..").resolve(attachedDir.getFileName().toString()).resolve( "file"+i);
            secondEqualsList.add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = linkTestDir.resolve("..").resolve(attachedDir.getFileName().toString()).resolve( "file"+i);
            thirdEqualsList.add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = testDir.resolve("file"+i);
            Files.createFile(path);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
                os.write(("test file " + i + "data").getBytes());
            }
            simpleFiles.add(path);
        }
    }

    private static void createDirs() throws IOException {
        testDir = fileSystem.getPath("/");
        Files.createDirectories(testDir);

        attachedDir = testDir.resolve("attached");
        Files.createDirectories(attachedDir);
    }

    @AfterAll
    public static void closeFileSystem() throws IOException {
        if (fileSystem != null) {
            fileSystem.close();
        }
    }

    @Test
    void FileEqualsTest() throws IOException {
        List<File> files = new ArrayList<>(simpleFiles.stream().map(p -> {
            try {
                return File.readFile(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList());
        files.add(File.readFile(testDir));
        for (int i = 0; i < files.size(); i++) {
            for (int j = 0; j < files.size(); j++) {
                if (i == j) {
                    File f1 = files.get(i);
                    File f2 = files.get(j);
                    Assertions.assertEquals(f1, f2);
                    Assertions.assertEquals(f1.hashCode(), f2.hashCode());
                    Assertions.assertEquals(f1.getFileLore(), f2.getFileLore());
                    Assertions.assertEquals(f1.getFileLore().hashCode(), f2.getFileLore().hashCode());
                } else {
                    File f1 = files.get(i);
                    File f2 = files.get(j);
                    Assertions.assertNotEquals(f1, f2);
                    Assertions.assertNotEquals(f2.getFileType(), f2.getFileLore());
                }
            }
        }
    }

    @Test
    void twoDotEqualsTest() throws IOException {
        List<File> files1 = new ArrayList<>(firstEqualsList.stream().map(p -> {
            try {
                return File.readFile(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList());
        List<File> files2 = new ArrayList<>(secondEqualsList.stream().map(p -> {
            try {
                return File.readFile(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList());
        List<File> files3 = new ArrayList<>(secondEqualsList.stream().map(p -> {
            try {
                return File.readFile(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList());
        for (int i = 0; i < files1.size(); i++) {
            for (int j = 0; j < files2.size(); j++) {
                if (i == j) {
                    Assertions.assertEquals(files1.get(i), files2.get(j));
                    Assertions.assertEquals(files1.get(i).hashCode(), files2.get(j).hashCode());
                    Assertions.assertEquals(files1.get(i).getFileLore(), files2.get(j).getFileLore());
                    Assertions.assertEquals(files1.get(i).getFileLore().hashCode(), files2.get(j).getFileLore().hashCode());
                } else {
                    Assertions.assertNotEquals(files1.get(i), files2.get(j));
                    Assertions.assertNotEquals(files1.get(i).getFileLore(), files2.get(j).getFileLore());
                }
            }
        }
        for (int i = 0; i < files2.size(); i++) {
            for (int j = 0; j < files3.size(); j++) {
                if (i == j) {
                    Assertions.assertEquals(files1.get(i), files2.get(j));
                    Assertions.assertEquals(files1.get(i).hashCode(), files2.get(j).hashCode());
                    Assertions.assertEquals(files1.get(i).getFileLore(), files2.get(j).getFileLore());
                    Assertions.assertEquals(files1.get(i).getFileLore().hashCode(), files2.get(j).getFileLore().hashCode());
                } else {
                    Assertions.assertNotEquals(files1.get(i), files2.get(j));
                    Assertions.assertNotEquals(files1.get(i).getFileLore(), files2.get(j).getFileLore());
                }
            }
        }
        for (int i = 0; i < files1.size(); i++) {
            for (int j = 0; j < files3.size(); j++) {
                if (i == j) {
                    Assertions.assertEquals(files1.get(i), files2.get(j));
                    Assertions.assertEquals(files1.get(i).hashCode(), files2.get(j).hashCode());
                    Assertions.assertEquals(files1.get(i).getFileLore(), files2.get(j).getFileLore());
                    Assertions.assertEquals(files1.get(i).getFileLore().hashCode(), files2.get(j).getFileLore().hashCode());
                } else {
                    Assertions.assertNotEquals(files1.get(i), files2.get(j));
                    Assertions.assertNotEquals(files1.get(i).getFileLore(), files2.get(j).getFileLore());
                }
            }
        }
        Assertions.assertNotEquals(File.readFile(linkTestDir), File.readFile(testDir));
        Assertions.assertNotEquals(File.readFile(linkTestDir).getFileLore(), File.readFile(testDir).getFileLore());
    }
}
