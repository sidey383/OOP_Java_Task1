package ru.nsu.sidey383.lab1.model.file.exception;

import java.nio.file.Path;

public abstract class PathException extends Exception {

    private final Path path;

    public PathException(Path path, String message) {
        super(message);
        this.path = path;
    }

    public PathException(Path path, String message, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public PathException(Path path, Throwable cause) {
        super(cause);
        this.path = path;
    }

    public PathException(Path path) {
        super();
        this.path = path;
    }

    protected PathException(Path path, String message, Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public String toUserMessage() {
        return getMessage() + ": " +
                getPath() +
                (getCause() != null ? " cause: " + getCause().getMessage() : "");
    }
}
