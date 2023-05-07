package ru.nsu.sidey383.lab1.walker;

import ru.nsu.sidey383.lab1.core.FileSystemGenerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WalkerTestFileSystem extends FileSystemGenerator {

    private Map<Path, Set<Path>> parentMap = new HashMap<>();

    private void addParent(Path parent, Path child) {
        parentMap.compute(child, (path, paths) -> paths == null ?
                Set.of(parent) :
                Stream.concat(paths.stream(), Set.of(parent).stream()).collect(Collectors.toSet()));
    }

    @Override
    protected void createFileTree(Path root) throws Exception {

        for (int i = 0; i < 5; i++) {
            Path folder = root.resolve("folder" + i);
            Files.createDirectory(folder);
            addParent(root, folder);
            Files.createSymbolicLink(folder.resolve("rootLink"), root);
            for (int j = 0; j < 5; j++) {
                Files.createSymbolicLink(folder.resolve("folder"+j+"Link"), root.resolve("folder"+j));
                Files.createFile(folder.resolve("file" + j));
            }
        }

    }

}
