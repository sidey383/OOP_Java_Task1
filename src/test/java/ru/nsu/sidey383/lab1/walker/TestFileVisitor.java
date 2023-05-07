package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestFileVisitor implements DUFileVisitor {

    private final Set<DUFile> files;

    private final Set<ParentDUFile> preVisitDirs;

    private final Set<ParentDUFile> postVisitDirs;

    public TestFileVisitor(Collection<DUFile> files, Collection<ParentDUFile> directories) {
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
    public void visitFile(DUFile file) {
        assertTrue(files.remove(file), file + " not expected in visitFile");
    }

    @Override
    public DUAction preVisitParentFile(ParentDUFile directory) {
        if (directory.getFileType() == DUFileType.LINK)
            return DUAction.STOP;
        assertTrue(preVisitDirs.remove(directory), directory + " not expected in preVisitDirectory");
        return DUAction.CONTINUE;
    }

    @Override
    public void postVisitDirectory(ParentDUFile directory) {
        assertTrue(postVisitDirs.remove(directory), directory + " not expected in postVisitDirectory");
    }

    @Override
    public void pathVisitError(@Nullable Path path, @NotNull DUPathException e) {
        assertDoesNotThrow(() -> {
            throw e;
        }, "call pathVisitError for correct paths");
    }

    @Override
    public void directoryCloseError(@NotNull Path path, @NotNull DUPathException e) {
        assertNull(e, "Some suppressed exception in file walker");
    }
}
