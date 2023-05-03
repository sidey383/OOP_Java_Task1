package ru.nsu.sidey383.lab1.model.file;

import java.util.Collection;

/**
 * Расширенный файловый интерфейс для работы с дочерними файлами
 * **/
public interface ParentDUFile extends DUFile {

    /**
     * @return коллекцию дочерних файлов
     * **/
    Collection<DUFile> getChildren();

    /**
     * Добавить потомка для данного файла. Игнорирует копии.
     * <p> Не влияет на состояние передаваемого файла, в частности на {@link DUFile#getParent()}
     * **/
    void addChild(DUFile file);

}
