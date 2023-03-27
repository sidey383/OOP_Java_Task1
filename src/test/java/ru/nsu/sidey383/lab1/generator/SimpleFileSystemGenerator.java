package ru.nsu.sidey383.lab1.generator;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * generate file system
 * /
 *   /file0
 *   /file1
 *   /file2
 *   /file3
 *   /file4
 *   /attached
 *      /link [link to /]
 *      /file0
 *      /file1
 *      /file2
 *      /file3
 *      /file4
 * **/
public class SimpleFileSystemGenerator implements BeforeAllCallback, AfterAllCallback {

    private FileSystem fileSystem;

    private final List<Path> allPaths = new ArrayList<>();

    private final List<List<Path>> equalsPathsLists = new ArrayList<>();

    private Path rootDir;

    private final Random random = new Random(5123);

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        fileSystem = MemoryFileSystemBuilder.newEmpty().build("name");
        rootDir = fileSystem.getPath("/");
        Files.createDirectories(rootDir);

        Path attachedDir = rootDir.resolve("attached");
        Files.createDirectories(attachedDir);

        Path linkTestDir = attachedDir.resolve("link");
        Files.createSymbolicLink(linkTestDir, rootDir);

        allPaths.add(rootDir);
        allPaths.add(attachedDir);
        allPaths.add(linkTestDir);

        for (int i = 0; i < 5; i++) {
            Path path = rootDir.resolve("file" + i);
            Files.createFile(path);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
                long size = random.nextLong(20000);
                for (long j = 0L; j < size; j++) {
                    os.write(random.nextInt());
                }
            }
            allPaths.add(path);
        }

        equalsPathsLists.add(new ArrayList<>());
        equalsPathsLists.add(new ArrayList<>());
        equalsPathsLists.add(new ArrayList<>());

        for (int i = 0; i < 5; i++) {
            Path path = attachedDir.resolve("file" + i);
            Files.createFile(path);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
                long size = random.nextLong(20000);
                for (long j = 0L; j < size; j++) {
                    os.write(random.nextInt());
                }
            }
            equalsPathsLists.get(0).add(path);
            allPaths.add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = attachedDir.resolve("..").resolve(attachedDir.getFileName().toString()).resolve("file" + i);
            equalsPathsLists.get(1).add(path);
        }
        for (int i = 0; i < 5; i++) {
            Path path = linkTestDir.resolve(attachedDir.getFileName().toString()).resolve("file" + i);
            equalsPathsLists.get(2).add(path);
        }
    }

    public List<Path> getAllPaths() {
        return allPaths;
    }

    public List<List<Path>> getEqualsPathsLists() {
        return equalsPathsLists;
    }

    public Path getRootDir() {
        return rootDir;
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        fileSystem.close();
    }
}
