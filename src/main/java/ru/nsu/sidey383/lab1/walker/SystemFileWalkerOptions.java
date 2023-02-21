package ru.nsu.sidey383.lab1.walker;


import java.nio.file.LinkOption;

public enum SystemFileWalkerOptions {
    NO_FOLLOW_LINKS,
    /**
     * Apply {@link java.nio.file.Path#toRealPath(LinkOption...)} for every Path.
     * @see {@link SystemFileWalkerOptions#NO_FOLLOW_LINKS_IN_REAL_PATH}
     * **/
    TO_REAL_PATH,
    /**
     * Use {@link java.nio.file.LinkOption} in {@link java.nio.file.Path#toRealPath(LinkOption...)}
     * @see {@link SystemFileWalkerOptions#TO_REAL_PATH}
     * **/
    NO_FOLLOW_LINKS_IN_REAL_PATH,
    /**
     *
     * **/
}
