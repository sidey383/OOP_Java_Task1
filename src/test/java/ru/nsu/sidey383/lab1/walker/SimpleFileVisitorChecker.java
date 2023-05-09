package ru.nsu.sidey383.lab1.walker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.file.DUFile;
import ru.nsu.sidey383.lab1.model.file.DUFileType;
import ru.nsu.sidey383.lab1.model.file.ParentDUFile;
import ru.nsu.sidey383.lab1.model.file.exception.DUPathException;

import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleFileVisitorChecker implements DUFileVisitor {

    private final FirstWalkerTestFileSystem fileSystem;

    public SimpleFileVisitorChecker(FirstWalkerTestFileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public void visitFile(DUFile file) {
        assertTrue(file.getParent().isEmpty() ||
                        fileSystem.isParent(file.getPath(), file.getParent().get().getPath()),
                "Wrong parent of " + file);
        file.getParent().ifPresent(p -> p.addChild(file));
    }

    @Override
    public DUAction preVisitParentFile(ParentDUFile directory) {
        assertTrue(directory.getParent().isEmpty() ||
                        fileSystem.isParent(directory.getPath(), directory.getParent().get().getPath()),
                "Wrong parent of " + directory);
        directory.getParent().ifPresent(p -> p.addChild(directory));
        if (directory.getFileType() == DUFileType.LINK) {
            return DUAction.STOP;
        }
        return DUAction.CONTINUE;
    }

    @Override
    public void postVisitParentFile(ParentDUFile directory) {
        directory.getParent().ifPresent(p -> p.addChild(directory));
        assertTrue(
                fileSystem.isAllChildren(
                        directory.getPath(),
                        directory.getChildren().stream().map(DUFile::getPath).toList()),

                () -> "Wrong children of " + directory + ":\n" +
                        directory.getChildren()
                                .stream()
                                .map(f -> f.getPath().toString())
                                .collect(Collectors.joining("\n\t", "\n\t", "\n")));
    }

    @Override
    public void pathVisitError(@Nullable Path path, @NotNull DUPathException e) {
        fail("Path visit error " + path + "Exception:" + e);
    }

    @Override
    public void directoryCloseError(@NotNull Path path, @NotNull DUPathException e) {
        fail("Path visit error " + path + "Exception:"  + e);
    }
}
