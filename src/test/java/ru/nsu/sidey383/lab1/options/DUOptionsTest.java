package ru.nsu.sidey383.lab1.options;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.nsu.sidey383.lab1.exception.DUOptionReadException;
import ru.nsu.sidey383.lab1.write.size.SizeSuffix;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixIEC;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixISU;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(OrderAnnotation.class)
public class DUOptionsTest {

    private static final boolean followLinksDefault = false;
    private static final int maxDepthDefault = 10;
    private static final int fileInDirLimitDefault = Integer.MAX_VALUE;
    private static final Path filePathDefault = Path.of(".");

    private static final SizeSuffix sizeSuffixDefault = SizeSuffixIEC.BYTE;

    private static final boolean helpDefault = false;

    @Test
    @Order(0)
    @DisplayName("No parameters test")
    public void simpleTest() {
        assertOptions("no args",
                new String[]{},
                followLinksDefault,
                maxDepthDefault,
                fileInDirLimitDefault,
                filePathDefault,
                sizeSuffixDefault,
                helpDefault);
    }

    @Test
    @Order(0)
    @DisplayName("test depth parameter")
    public void depthTest() {
        for (int i = 1; i < 1000; i++) {
            assertOptions("depth " + i,
                    new String[]{"--depth", Integer.toString(i)},
                    followLinksDefault,
                    i,
                    fileInDirLimitDefault,
                    filePathDefault,
                    sizeSuffixDefault,
                    helpDefault);
        }

        for (int i = 0; i > -100; i--) {
            String[] args = new String[]{"--depth", Integer.toString(i)};
            assertThrowsExactly(
                    DUOptionReadException.class,
                    () -> DiskUsageOptions.builder().applyConsoleArgs(args).build(),
                    "Input: " + String.join(" ", args));
        }
        assertThrowsExactly(
                DUOptionReadException.class,
                () -> DiskUsageOptions.builder().applyConsoleArgs(new String[]{"--depth"}).build(),
                "Input: " + String.join(" ", new String[]{"--depth"}));
    }

    @Test
    @Order(0)
    @DisplayName("test link parameter")
    public void linkTest() {
        assertOptions("-L param",
                new String[]{"-L"},
                !followLinksDefault,
                maxDepthDefault,
                fileInDirLimitDefault,
                filePathDefault,
                sizeSuffixDefault,
                helpDefault);
    }

    @Test
    @Order(0)
    @DisplayName("Size format test")
    public void sizeFormatTest() {
        assertOptions("size format IEC ",
                new String[]{"--size-format", "IEC"},
                followLinksDefault,
                maxDepthDefault,
                fileInDirLimitDefault,
                filePathDefault,
                SizeSuffixIEC.BYTE,
                helpDefault);
        assertOptions("size format ISU ",
                new String[]{"--size-format", "ISU"},
                followLinksDefault,
                maxDepthDefault,
                fileInDirLimitDefault,
                filePathDefault,
                SizeSuffixISU.BYTE,
                helpDefault);
        assertThrowsExactly(
                DUOptionReadException.class,
                () -> DiskUsageOptions.builder().applyConsoleArgs(new String[]{"--size-format"}).build(),
                "Input: " + String.join(" ", new String[]{"--size-format"}));
    }

    @Test
    @Order(0)
    @DisplayName("File in dir limit test")
    public void filesInDirLimitTest() {
        for (int i = 1; i < 10000; i++) {
            assertOptions("limit " + i,
                    new String[]{"--limit", Integer.toString(i)},
                    followLinksDefault,
                    maxDepthDefault,
                    i,
                    filePathDefault,
                    sizeSuffixDefault,
                    helpDefault);
        }
        for (int i = 0; i > -100; i--) {
            String[] args = new String[]{"--limit", Integer.toString(i)};
            assertThrowsExactly(
                    DUOptionReadException.class,
                    () -> DiskUsageOptions.builder().applyConsoleArgs(args).build(),
                    "Input: " + String.join(" ", args));
        }
        assertThrowsExactly(
                DUOptionReadException.class,
                () -> DiskUsageOptions.builder().applyConsoleArgs(new String[]{"--limit"}).build(),
                "Input: " + String.join(" ", new String[]{"--limit"}));
    }

    @Test
    @Order(0)
    @DisplayName("Help test")
    public void helpTest() {
        assertOptions("-h",
                new String[]{"-h"},
                followLinksDefault,
                maxDepthDefault,
                fileInDirLimitDefault,
                filePathDefault,
                sizeSuffixDefault,
                !helpDefault);
        assertOptions("-help",
                new String[]{"-help"},
                followLinksDefault,
                maxDepthDefault,
                fileInDirLimitDefault,
                filePathDefault,
                sizeSuffixDefault,
                !helpDefault);
    }

    @Test
    @Order(0)
    @DisplayName("File test")
    public void fileTest() throws IOException {
        for (int i = 0; i < 100; i++) {
            Path path = Path.of("foo" + i);
            Files.createFile(path);
            assertOptionsWithFileDelete("file foo" + i,
                    new String[]{"foo" + i},
                    followLinksDefault,
                    maxDepthDefault,
                    fileInDirLimitDefault,
                    path,
                    sizeSuffixDefault,
                    helpDefault);
        }
        for (int i = 0; i < 100; i++) {
            String[] args = new String[]{"foo" + i};
            assertThrowsExactly(
                    DUOptionReadException.class,
                    () -> DiskUsageOptions.builder().applyConsoleArgs(args).build(),
                    "Input: " + String.join(" ", args));
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test for different combinations of parameters")
    public void complexTest() throws IOException {
        Random random = new Random(32452);
        for (int i = 0; i < 1000; i++) {
            List<String[]> argsList = new ArrayList<>();
            boolean followLinks = followLinksDefault;
            int maxDepth = maxDepthDefault;
            int fileInDirLimit = fileInDirLimitDefault;
            Path filePath = filePathDefault;
            SizeSuffix sizeSuffix = sizeSuffixDefault;
            boolean help = helpDefault;

            if (random.nextBoolean()) {
                followLinks = !followLinksDefault;
                argsList.add(new String[]{"-L"});
            }

            if (random.nextBoolean()) {
                maxDepth = random.nextInt(500);
                argsList.add(new String[]{"--depth", Integer.toString(maxDepth)});
            }

            if (random.nextBoolean()) {
                fileInDirLimit = random.nextInt(500);
                argsList.add(new String[]{"--limit", Integer.toString(fileInDirLimit)});
            }

            if (random.nextBoolean()) {
                String name = "foo" + random.nextInt(500);
                filePath = Path.of(name);
                argsList.add(new String[]{name});
                Files.createFile(filePath);
            }

            if (random.nextBoolean()) {
                if (random.nextBoolean()) {
                    sizeSuffix = SizeSuffixIEC.BYTE;
                    argsList.add(new String[]{"--size-format", "IEC"});
                } else {
                    sizeSuffix = SizeSuffixISU.BYTE;
                    argsList.add(new String[]{"--size-format", "ISU"});
                }
            }

            if (random.nextBoolean()) {
                help = true;
                argsList.add(new String[]{random.nextBoolean() ? "-h" : "-help"});
            }

            Collections.shuffle(argsList, random);

            String[] args = argsList.stream()
                    .flatMap(Arrays::stream)
                    .toArray(String[]::new);
            if (filePath == filePathDefault) {
                assertOptions(
                        "random test, args " + Arrays.stream(args).collect(Collectors.joining(" ", "\"", "\"")),
                        args,
                        followLinks,
                        maxDepth,
                        fileInDirLimit,
                        filePath,
                        sizeSuffix,
                        help
                );
            } else {
                assertOptionsWithFileDelete(
                        "random test, args " + String.join(" ", args),
                        args,
                        followLinks,
                        maxDepth,
                        fileInDirLimit,
                        filePath,
                        sizeSuffix,
                        help
                );
            }
        }

    }

    private void assertOptions(
            String source,
            String[] args,
            boolean followLinks,
            int maxDepth,
            int fileInDirLimit,
            Path filePath,
            SizeSuffix sizeSuffix,
            boolean help) {
        try {
            DiskUsageOptions options = DiskUsageOptions.builder().applyConsoleArgs(args).build();
            assertEquals(followLinks, options.followLink(), source + ": Wrong followLinks");
            assertEquals(maxDepth, options.getMaxDepth(), source + ": Wrong maxDepth");
            assertEquals(fileInDirLimit, options.getFileInDirLimit(), source + ": Wrong fileInDirLimit");
            assertEquals(filePath, options.getFilePath(), source + ": Wrong filePath");
            assertEquals(sizeSuffix, options.getByteSizeSuffix(), source + ": Wrong izeSuffix");
            assertEquals(help, options.help(), source + ": Wrong help value");
        } catch (DUOptionReadException e) {
            fail("Exception with correct input: " + String.join(" ", args));
        }
    }

    private void assertOptionsWithFileDelete(
            String source,
            String[] args,
            boolean followLinks,
            int maxDepth,
            int fileInDirLimit,
            Path filePath,
            SizeSuffix sizeSuffix,
            boolean help) throws IOException {
        try {
            DiskUsageOptions options = DiskUsageOptions.builder().applyConsoleArgs(args).build();
            Files.delete(filePath);
            assertEquals(followLinks, options.followLink(), "Wrong followLinks: " + source);
            assertEquals(maxDepth, options.getMaxDepth(), "Wrong maxDepth: " + source);
            assertEquals(fileInDirLimit, options.getFileInDirLimit(), "Wrong fileInDirLimit: " + source );
            assertEquals(filePath, options.getFilePath(), "Wrong filePath: " + source);
            assertEquals(sizeSuffix, options.getByteSizeSuffix(), "Wrong sizeSuffix: " +source);
            assertEquals(help, options.help(), "Wrong help value: " + source);
        } catch (DUOptionReadException e) {
            fail("Exception with correct input: " + String.join(" ", args));
        }
    }

}
