package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DirectoryFile;
import ru.nsu.sidey383.lab1.model.file.File;
import ru.nsu.sidey383.lab1.model.file.exception.PathException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
        assertAll(
                "Expect empty path sets after visiting all files",
                () -> assertEquals(new HashSet<>(), files, "Expect void set after visiting all files"),
                () -> assertEquals(new HashSet<>(), preVisitDirs),
                () -> assertEquals(new HashSet<>(), postVisitDirs)
        );

    }


    @Override
    public void visitFile(File file) {
        assertTrue(files.remove(file), file + " not expected in visitFile");
    }

    @Override
    public NextAction preVisitDirectory(DirectoryFile directory) {
        if (directory.getFileType().isLink())
            return NextAction.STOP;
        assertTrue(preVisitDirs.remove(directory), directory + " not expected in preVisitDirectory");
        return NextAction.CONTINUE;
    }

    @Override
    public void postVisitDirectory(DirectoryFile directory) {
        assertTrue(postVisitDirs.remove(directory), directory + " not expected in postVisitDirectory");
    }

    @Override
    public void pathVisitError(@Nullable Path path, @NotNull PathException e) {
        assertDoesNotThrow(() -> {
            throw e;
        }, "call pathVisitError for correct paths");
    }
}
