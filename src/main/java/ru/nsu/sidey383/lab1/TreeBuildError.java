package ru.nsu.sidey383.lab1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.sidey383.lab1.model.files.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

public record TreeBuildError(@Nullable Path path, @Nullable File file, @NotNull IOException exception) {

    @Override
    public String toString() {
        StringWriter errors = new StringWriter();
        PrintWriter writer = new PrintWriter(errors);
        if (file != null)
            writer.println(file);
        else if (path != null)
            writer.println(path);
        exception.printStackTrace(writer);
        return errors.toString();
    }
}
