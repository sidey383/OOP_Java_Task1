package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.core.FileSystemGenerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileTestFileSystem extends FileSystemGenerator {

    private final List<Path>[] sameFiles = new List[11];

    private Path wrongLink;

    /**
     * <ul>
     *     root
     *     <ul>
     *              folder1
     *         <ul>
     *             file1 <p>
     *             file2 <p>
     *             file3 <p>
     *             file4 <p>
     *             file5 <p>
     *             rootLink <p>
     *             folder1Link <p>
     *             folder2Link <p>
     *             folder3Link <p>
     *             folder4Link <p>
     *             folder5Link <p>
     *         <ul/>
     *     <ul/>
     * <ul/>
     * ....
     * **/
    @Override
    protected void createFileTree(Path root) throws Exception {
        wrongLink = root.resolve("wrongReference");
        Files.createSymbolicLink(wrongLink, getWrongPath());
        for (int i = 0; i < 5; i++) {
            Path folder = root.resolve("folder" + i);
            Files.createDirectory(folder);
            Files.createSymbolicLink(folder.resolve("rootLink"), root);
            for (int j = 0; j < 5; j++) {
                Files.createSymbolicLink(folder.resolve("folder"+j+"Link"), root.resolve("folder"+j));
                Files.createFile(folder.resolve("file" + j));
            }
        }
        List<Path> rootList = sameFiles[0] = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                rootList.add(root.resolve("folder"+i).resolve("file"+j));
            }
        }

        for (int i_ = 0; i_ < 5; i_++) {
            Path rootLink = root.resolve("folder"+i_).resolve("rootLink");
            List<Path> rootLinkList = sameFiles[1 + i_] = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    rootLinkList.add(rootLink.resolve("folder"+i).resolve("file"+j));
                }
            }
        }

        for (int i_ = 0; i_ < 5; i_++) {
            Path folder = root.resolve("folder"+i_);
            List<Path> folderLinkList = sameFiles[i_ + 6] = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    folderLinkList.add(folder.resolve("folder"+i+"Link").resolve("file"+j));
                }
            }
        }

    }

    /**
     * Identical file lists.
     * {@code  identicalFileLists()[0].get(i)} file will be equals to {@code identicalFileLists()[1].get(i)}
     * @return array of identical files lists
     * **/
    public List<Path>[] getIdenticalFileLists() {
        return sameFiles;
    }

    public Path getWrongPath() {
        return getRoot().resolve("wrongPath");
    }

    public Path getWrongPathLink() {
        return wrongLink;
    }

}
