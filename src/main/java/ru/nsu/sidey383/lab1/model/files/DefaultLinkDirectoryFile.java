package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.model.FileLore;

public class DefaultLinkDirectoryFile extends DefaultDirectoryFile {

    public DefaultLinkDirectoryFile(FileLore lore) {
        super(lore);
    }

    @Override
    public long getSize() {
        return getOriginalSize();
    }
}
