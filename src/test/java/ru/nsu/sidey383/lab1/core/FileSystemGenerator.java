package ru.nsu.sidey383.lab1.core;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class FileSystemGenerator implements BeforeAllCallback, AfterAllCallback {

    private FileSystem fileSystem;

    private Path root;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        fileSystem = MemoryFileSystemBuilder.newEmpty().build("test");
        root = fileSystem.getPath("/");
        Files.createDirectories(root);
        createFileTree(root);
    }

    protected abstract void createFileTree(Path root) throws Exception;

    public Path getRoot() {
        return root;
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        fileSystem.close();
    }

}
