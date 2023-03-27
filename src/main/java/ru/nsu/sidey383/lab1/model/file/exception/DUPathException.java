package ru.nsu.sidey383.lab1.model.file.exception;

import java.nio.file.Path;

public class DUPathException extends Exception {

    private final Path path;

    public DUPathException(Path path, Throwable cause) {
        super(cause);
        this.path = path;
    }

    protected DUPathException(Path path, String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public String toUserMessage() {
        return getPath() +
                (getCause() != null ? " cause: " + getCause().getClass().getName() + " " + getCause().getMessage() : "");
    }
}
