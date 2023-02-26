package ru.nsu.sidey383.lab1.generator;

import java.nio.file.Path;

public record FileWithSize(long size, Path path) {
}
