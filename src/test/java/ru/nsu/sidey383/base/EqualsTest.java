package ru.nsu.sidey383.base;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.model.files.File;
import ru.nsu.sidey383.utils.FileSystemExtension;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EqualsTest {

    @RegisterExtension
    final FileSystemExtension extension = new FileSystemExtension();

    private FileSystem fileSystem;

    private static Path testDir;
    private static Path attachedDir;

    private static List<Path> simpleFiles = new ArrayList<>();

    private static List<Path> firstEqualsList = new ArrayList<>();

    private static List<Path> secondEqualsList = new ArrayList<>();


    @BeforeEach
    public void createFiles() throws IOException {
        createDirs();
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
            Path path = testDir.resolve("file"+i);
            Files.createFile(path);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
                os.write(("test file " + i + "data").getBytes());
            }
            simpleFiles.add(path);
        }
    }

    private void createDirs() throws IOException {
        this.fileSystem = extension.getFileSystem();
        testDir = fileSystem.getPath("root/testDir");
        Files.createDirectories(testDir);

        attachedDir = testDir.resolve("attached");
        Files.createDirectories(attachedDir);
    }

    @AfterAll
    public void deleteFiles() throws IOException {
        for (Path file : simpleFiles) {
            Files.delete(file);
        }
        for (Path file : firstEqualsList) {
            Files.delete(file);
        }
        Files.delete(attachedDir);
        Files.delete(testDir);
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
        files.add(File.readFile(testDir));
        for (int i = 0; i < files.size(); i++) {
            for (int j = 0; j < files.size(); j++) {
                if (i == j) {
                    Assertions.assertEquals(files.get(i), files.get(j));
                    Assertions.assertEquals(files.get(i).hashCode(), files.get(j).hashCode());
                    Assertions.assertEquals(files.get(i).getFileLore(), files.get(j).getFileLore());
                    Assertions.assertEquals(files.get(i).getFileLore().hashCode(), files.get(j).getFileLore().hashCode());
                } else {
                    Assertions.assertNotEquals(files.get(i), files.get(j));
                    Assertions.assertNotEquals(files.get(i).getFileLore(), files.get(j).getFileLore());
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
    }
}
