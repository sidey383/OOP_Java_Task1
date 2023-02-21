package ru.nsu.sidey383.lab1.model;

import java.io.IOException;

public class AttributesResolveException extends IOException {

    AttributesResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    AttributesResolveException(String message) {
        super(message);
    }

    AttributesResolveException(Throwable cause) {
        super(cause);
    }

    AttributesResolveException() {
        super();
    }

}
