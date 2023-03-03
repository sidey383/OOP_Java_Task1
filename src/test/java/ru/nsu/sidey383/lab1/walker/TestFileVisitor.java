package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestFileVisitor implements FileVisitor {

    private final Set<File> files;

    private final Set<DirectoryFile> preVisitDirs;

    private final Set<DirectoryFile> postVisitDirs;

    public TestFileVisitor(Collection<File> files, Collection<DirectoryFile> directories) {
        this.files = new HashSet<>(files);
        this.preVisitDirs = new HashSet<>(directories);
        this.postVisitDirs = new HashSet<>(directories);
    }

    public void checkOnEmpty() {
        Assertions.assertEquals(new HashSet<>(), files);
        Assertions.assertEquals(new HashSet<>(), preVisitDirs);
        Assertions.assertEquals(new HashSet<>(), postVisitDirs);
    }


    @Override
    public void visitFile(File file) {
        Assertions.assertTrue(files.remove(file));
    }

    @Override
    public NextAction preVisitDirectory(DirectoryFile directory) {
        if (directory.getFileType().isLink())
            return NextAction.STOP;
        Assertions.assertTrue(preVisitDirs.remove(directory));
        return NextAction.CONTINUE;
    }

    @Override
    public void postVisitDirectory(DirectoryFile directory) {
        Assertions.assertTrue(postVisitDirs.remove(directory));
    }

    @Override
    public void pathVisitError(@Nullable Path path, @NotNull IOException e) {
        Assertions.assertDoesNotThrow(() -> {
            throw e;
        });
    }
}
