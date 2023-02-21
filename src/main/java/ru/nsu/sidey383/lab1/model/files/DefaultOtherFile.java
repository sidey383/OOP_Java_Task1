package ru.nsu.sidey383.lab1.model.files;

import ru.nsu.sidey383.lab1.model.FileLore;

public class DefaultOtherFile extends BaseFile {

    public DefaultOtherFile(FileLore lore) {
        super(lore);
    }

    @Override
    public long getSize() {
        return getResolvedSize();
    }

}
