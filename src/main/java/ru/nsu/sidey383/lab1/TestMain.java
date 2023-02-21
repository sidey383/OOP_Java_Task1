package ru.nsu.sidey383.lab1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class TestMain {

    public static void main(String[] args) throws IOException {
        Path p = Path.of("C:\\Users\\sidey\\IdeaProjects\\OOP_Java_Task1\\.atest\\link");
        printAttributes(p);
        printAttributes_(p);
    }

    private static void printAttributes(Path path) throws IOException {
        System.out.println(path);
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        System.out.println("link: " + attributes.isSymbolicLink());
        System.out.println("dir: " + attributes.isDirectory());
        System.out.println("reg: " + attributes.isRegularFile());
        System.out.println("other: " + attributes.isOther());
    }

    private static void printAttributes_(Path path) throws IOException {
        System.out.println(path);
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        System.out.println("link: " + attributes.isSymbolicLink());
        System.out.println("dir: " + attributes.isDirectory());
        System.out.println("reg: " + attributes.isRegularFile());
        System.out.println("other: " + attributes.isOther());
    }

}
