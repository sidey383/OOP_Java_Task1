package ru.nsu.sidey383.lab1.tree;

import ru.nsu.sidey383.lab1.core.FileSystemGenerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeTestFileSystem extends FileSystemGenerator {

    private final Map<Path, Set<Path>> parentMap = new HashMap<>();

    private final Map<Path, Set<Path>> childMap = new HashMap<>();

    private final Set<Path> emptyFolders = new HashSet<>();

    private final Set<Path> allFiles = new HashSet<>();

    private final Set<Path> allLinks = new HashSet<>();

    private void addParentAndChild(Path parent, Path child) {

        parentMap.compute(child, (path, paths) -> paths == null ?
                Set.of(parent) :
                Stream.concat(paths.stream(), Set.of(parent).stream()).collect(Collectors.toSet()));

        childMap.compute(parent, (path, paths) -> paths == null ?
                Set.of(child) :
                Stream.concat(paths.stream(), Set.of(child).stream()).collect(Collectors.toSet()));
    }

    @Override
    protected void createFileTree(Path root) throws Exception {

        for (int i = 0; i < 5; i++) {
            Path folder = root.resolve("folder" + i);
            Path rootLink = folder.resolve("rootLink");
            allLinks.add(rootLink);

            Files.createDirectory(folder);
            addParentAndChild(root, folder);

            Files.createSymbolicLink(rootLink, root);
            addParentAndChild(folder, rootLink);
            addParentAndChild(rootLink, root);

            for (int j = 0; j < 5; j++) {
                Path folderLink = folder.resolve("folder"+j+"Link");
                Path linkedFolder = root.resolve("folder"+j);
                Path file = folder.resolve("file" + j);
                Path emptyFolder = folder.resolve("emptyFolder");

                allLinks.add(folderLink);
                allFiles.add(file);
                emptyFolders.add(emptyFolder);

                Files.createSymbolicLink(folderLink, linkedFolder);
                addParentAndChild(folder, folderLink);
                addParentAndChild(folderLink, linkedFolder);

                Files.createFile(file);
                addParentAndChild(folder, file);
                Files.write(file, new byte[20]);

                Files.createDirectories(emptyFolder);
                addParentAndChild(folder, emptyFolder);
            }
        }
    }

    public boolean isParent(Path child, Path parent) {
        return parentMap.containsKey(child) && parentMap.get(child).contains(parent);
    }

    public boolean isAllChildren(Path parent, Collection<Path> children) {
        return (childMap.containsKey(parent) && childMap.get(parent).containsAll(children) && children.containsAll(childMap.get(parent))) ||
                (!childMap.containsKey(parent) && children.isEmpty());
    }

    public Collection<Path> getEmptyFolders() {
        return emptyFolders;
    }

    public Collection<Path> getFiles() {
        return allFiles;
    }

    public Collection<Path> getLinks() {
        return allLinks;
    }

}
