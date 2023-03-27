package ru.nsu.sidey383.lab1.model.file;

import java.util.Set;

/**
 * Расширенный файловый интерфейс для работы с дочерними файлами
 * **/
public interface ParentFile extends File {

    /**
     * @return копию множества дочерних файлов
     * **/
    Set<File> getChildren();

    /**
     * Добавить потомка для данного файла. Игнорирует копии.
     * <p> Не влияет на состояние передаваемого файла, в частности на {@link File#getParent()}
     * **/
    void addChild(File file);

}
