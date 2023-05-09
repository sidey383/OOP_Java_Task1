package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DUWalkerTest {

    @RegisterExtension
    private static final FirstWalkerTestFileSystem fileSystem = new FirstWalkerTestFileSystem();

    @Test
    @DisplayName("Checking the passage through files without clicking on links")
    public void simpleVisitTest() {
        DUSystemFileWalker walker = DUSystemFileWalker.walkFiles(fileSystem.getRoot(), new SimpleFileVisitorChecker(fileSystem));
        assertNotNull(walker.getRootFile(), "Walker return null root file");
        assertEquals(fileSystem.getRoot(), walker.getRootFile().getPath(), "Walker return wrong root file");
    }

    @Test
    @DisplayName("Checking a tree from a single file")
    public void fileVisitTest() {
        for (Path p : fileSystem.getFiles()) {
            DUSystemFileWalker walker = DUSystemFileWalker.walkFiles(p, new DUFileVisitor() {
                @Override
                public void visitFile(DUFile file) {
                    assertEquals(file.getPath(), p, "Except one file in this tree");
                }

                @Override
                public DUAction preVisitParentFile(ParentDUFile directory) {
                    fail("Expect no parent files in this tree, but found " + directory);
                    return DUAction.STOP;
                }

                @Override
                public void postVisitParentFile(ParentDUFile directory) {
                    fail("Expect no parent files in this tree, but found " + directory);
                }

                @Override
                public void pathVisitError(@Nullable Path path, @NotNull DUPathException e) {
                    fail("Path visit error " + path + "Exception:" + e);
                }

                @Override
                public void directoryCloseError(@NotNull Path path, @NotNull DUPathException e) {
                    fail("Path visit error " + path + "Exception:"  + e);
                }
            });
            assertNotNull(walker.getRootFile(), "Walker return null root file");
            assertEquals(p, walker.getRootFile().getPath(), "Walker return wrong root file of tree");
        }
    }

    @Test
    @DisplayName("Checking a tree from a link file")
    public void linkVisitCheck() {
        for (Path p : fileSystem.getLinks()) {
            DUSystemFileWalker walker = DUSystemFileWalker.walkFiles(p, new SimpleFileVisitorChecker(fileSystem) {

                boolean isChecked = false;

                @Override
                public DUAction preVisitParentFile(ParentDUFile directory) {
                    if (p.equals(directory.getPath()) && !isChecked) {
                        isChecked = true;
                        assertTrue(directory.getParent().isEmpty() ||
                                        fileSystem.isParent(directory.getPath(), directory.getParent().get().getPath()),
                                "Wrong parent of " + directory);
                        directory.getParent().ifPresent(p -> p.addChild(directory));
                        return DUAction.CONTINUE;
                    }
                    return super.preVisitParentFile(directory);
                }
            });
            assertNotNull(walker.getRootFile(), "Walker return null root file");
            assertEquals(p, walker.getRootFile().getPath(), "Walker return wrong root file of tree");
        }
    }

    @Test
    @DisplayName("Checking a tree from a empty folder")
    public void emptyFolderVisitTest() {
        for (Path p : fileSystem.getEmptyFolders()) {
            DUSystemFileWalker walker = DUSystemFileWalker.walkFiles(p, new DUFileVisitor() {
                @Override
                public void visitFile(DUFile file) {
                    fail("Expect no files in this tree, but found " + file);
                }

                @Override
                public DUAction preVisitParentFile(ParentDUFile directory) {
                    assertEquals(directory.getPath(), p, "Except one parent file in this tree");
                    return DUAction.STOP;
                }

                @Override
                public void postVisitParentFile(ParentDUFile directory) {
                    assertEquals(directory.getPath(), p, "Except one parent file in this tree");
                }

                @Override
                public void pathVisitError(@Nullable Path path, @NotNull DUPathException e) {
                    fail("Path visit error " + path + "Exception:" + e);
                }

                @Override
                public void directoryCloseError(@NotNull Path path, @NotNull DUPathException e) {
                    fail("Path visit error " + path + "Exception:"  + e);
                }
            });
            assertNotNull(walker.getRootFile(), "Walker return null root file");
            assertEquals(p, walker.getRootFile().getPath(), "Walker return wrong root file of tree");
        }
    }

}
