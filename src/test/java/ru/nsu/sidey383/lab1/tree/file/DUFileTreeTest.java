package ru.nsu.sidey383.lab1.tree.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.nsu.sidey383.lab1.FileTree;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.tree.TreeTestFileSystem;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DUFileTreeTest {

    @RegisterExtension
    public static final TreeTestFileSystem fileSystem = new TreeTestFileSystem();

    @Test
    @DisplayName("Checking the passage through files without clicking on links")
    public void simpleTest() {
        FileTree tree = FileTree.calculateTree(fileSystem.getRoot(), false);
        assertFalse(tree.hasErrors(), "Has error in FileTree");
        assertTrue(tree.getErrors().isEmpty(), "No error, but list of errors not empty");
        assertNotNull(tree.getBaseFile(), "Base file can't be null for correct path");
        checkRecursive(tree.getBaseFile());
    }

    private void checkRecursive(DUFile file) {
        file.getParent().ifPresent(p -> assertTrue(fileSystem.isParent(file.getPath(), p.getPath()), ""));
        if (file instanceof ParentDUFile parent) {
            fileSystem.isAllChildren(parent.getPath(), parent.getChildren().stream().map(DUFile::getPath).toList());
            parent.getChildren().forEach(this::checkRecursive);
        }
    }

    @Test
    @DisplayName("Checking a tree from a single file")
    public void fileTreeTest() {
        for (Path p : fileSystem.getFiles()) {
            FileTree tree1 = FileTree.calculateTree(p, true);
            assertFalse(tree1.hasErrors(), "Has error in FileTree");
            assertTrue(tree1.getErrors().isEmpty(), "No error, but list of errors not empty");
            assertNotNull(tree1.getBaseFile(), "Base file can't be null for correct path");
            assertTrue(tree1.getBaseFile().getParent().isEmpty(), "Parent of base file not empty");
            assertEquals(DUFileType.REGULAR_FILE, tree1.getBaseFile().getFileType(), "Wrong file type");
            FileTree tree2 = FileTree.calculateTree(p, false);
            assertFalse(tree2.hasErrors(), "Has error in FileTree");
            assertTrue(tree2.getErrors().isEmpty(), "No error, but list of errors not empty");
            assertNotNull(tree2.getBaseFile(), "Base file can't be null for correct path");
            assertTrue(tree2.getBaseFile().getParent().isEmpty(), "Parent of base file not empty");
            assertEquals(DUFileType.REGULAR_FILE, tree2.getBaseFile().getFileType(), "Wrong file type");
        }
    }

    @Test
    @DisplayName("Checking a tree from a link file")
    public void linkTreeTest() {
        for (Path p : fileSystem.getLinks()) {
            FileTree tree1 = FileTree.calculateTree(p, false);
            assertFalse(tree1.hasErrors(), "Has error in FileTree");
            assertTrue(tree1.getErrors().isEmpty(), "No error, but list of errors not empty");
            assertNotNull(tree1.getBaseFile(), "Base file can't be null for correct path");
            assertEquals(DUFileType.LINK, tree1.getBaseFile().getFileType(), "Wrong file type");
            if (tree1.getBaseFile() instanceof ParentDUFile parentDUFile) {
                if (!parentDUFile.getChildren().isEmpty()) {
                    fail("Children of the link to are empty, but the followLink parameter was set to false");
                }
            } else {
                fail("Link file didn't realize ParentDUFile interface");
            }
            FileTree tree2 = FileTree.calculateTree(p, true);
            assertFalse(tree2.hasErrors(), "Has error in FileTree");
            assertTrue(tree2.getErrors().isEmpty(), "No error, but list of errors not empty");
            assertNotNull(tree2.getBaseFile(), "Base file can't be null for correct path");
            assertEquals(DUFileType.LINK, tree2.getBaseFile().getFileType(), "Wrong file type");
            checkCyclical(tree2.getBaseFile(), new HashSet<>());
        }
    }

    private void checkCyclical(DUFile file, Set<DUFile> visitedFiles) {
        file.getParent().ifPresent(p -> assertTrue(fileSystem.isParent(file.getPath(), p.getPath()), ""));
        visitedFiles.add(file);
        if (file instanceof ParentDUFile parent) {
            fileSystem.isAllChildren(parent.getPath(), parent.getChildren().stream().map(DUFile::getPath).toList());
            parent.getChildren().stream().filter(f -> !visitedFiles.contains(f)).forEach(f -> checkCyclical(f, visitedFiles));
        }
    }

    @Test
    @DisplayName("Checking a tree from a empty folder")
    public void emptyFolderVisitTest() {
        for (Path p : fileSystem.getEmptyFolders()) {
            FileTree tree1 = FileTree.calculateTree(p, false);
            assertFalse(tree1.hasErrors(), "Has error in FileTree " + p);
            assertTrue(tree1.getErrors().isEmpty(), "No error, but list of errors not empty " + p);
            assertNotNull(tree1.getBaseFile(), "Base file can't be null for correct path " + p);
            assertEquals(DUFileType.DIRECTORY, tree1.getBaseFile().getFileType(), "Wrong file type " + tree1.getBaseFile());
            if (tree1.getBaseFile() instanceof ParentDUFile parentDUFile) {
                assertEquals(0, parentDUFile.getChildren().size(), "Empty folder has children " + tree1.getBaseFile());
            } else {
                fail("Directory " + tree1.getBaseFile() + " not instance of ParentDUFile");
            }
            FileTree tree2 = FileTree.calculateTree(p, true);
            assertFalse(tree2.hasErrors(), "Has error in FileTree " + p);
            assertTrue(tree2.getErrors().isEmpty(), "No error, but list of errors not empty " + p);
            assertNotNull(tree2.getBaseFile(), "Base file can't be null for correct path " + p);
            assertEquals(DUFileType.DIRECTORY, tree2.getBaseFile().getFileType(), "Wrong file type " + tree1.getBaseFile());
            if (tree2.getBaseFile() instanceof ParentDUFile parentDUFile) {
                assertEquals(0, parentDUFile.getChildren().size(), "Empty folder has children " + tree1.getBaseFile());
            } else {
                fail("Directory " + tree2.getBaseFile() + " not instance of ParentDUFile");
            }
        }
    }

}
