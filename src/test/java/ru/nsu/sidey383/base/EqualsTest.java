package ru.nsu.sidey383.base;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.*;
import ru.nsu.sidey383.lab1.model.file.File;

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

    private static final List<Path> simpleFiles = new ArrayList<>();

    private static final List<List<Path>> equalsPathsLists = new ArrayList<>();


    @BeforeAll
    public static void createFiles() throws IOException {
        fileSystem = MemoryFileSystemBuilder.newEmpty().build("name");
        createDirs();
        final Path linkTestDir = testDir.resolve("link");
        Files.createSymbolicLink(linkTestDir, attachedDir);
        simpleFiles.add(linkTestDir);
        equalsPathsLists.add(new ArrayList<>());
        equalsPathsLists.add(new ArrayList<>());
        equalsPathsLists.add(new ArrayList<>());
        for (int i = 0; i < 5; i++) {
            Path path = attachedDir.resolve("file" + i);
            Files.createFile(path);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
                os.write(("test file " + i + "data").getBytes());
            }
            equalsPathsLists.get(0).add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = attachedDir.resolve("..").resolve(attachedDir.getFileName().toString()).resolve("file" + i);
            equalsPathsLists.get(1).add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = linkTestDir.resolve("file" + i);
            equalsPathsLists.get(2).add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = testDir.resolve("file" + i);
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
    void simpleEqualsTest() {

        List<File> files = new ArrayList<>(simpleFiles.stream().map(p -> {
            try {
                return File.readFile(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList());

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

        List<List<File>> equalsFilesLists = equalsPathsLists.stream()
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
        Assertions.assertEquals(f1.getFileLore().hashCode(), f2.getFileLore().hashCode());
    }

    private void checkFileNotEquals(File f1, File f2) {
        Assertions.assertNotEquals(f1, f2);
        Assertions.assertNotEquals(f1.getFileLore(), f2.getFileLore());
    }
}
