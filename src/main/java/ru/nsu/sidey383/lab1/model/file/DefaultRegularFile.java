package ru.nsu.sidey383.lab1.model.file;

import ru.nsu.sidey383.lab1.model.file.lore.FileLore;

public class DefaultRegularFile extends BaseFile {

    public DefaultRegularFile(FileLore lore) {
        super(lore);
    }

    @Override
    public long getSize() {
        return getResolvedSize();
    }
}
