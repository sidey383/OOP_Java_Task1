package ru.nsu.sidey383.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.sidey383.lab1.write.size.SizeSuffix;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixIEC;
import ru.nsu.sidey383.lab1.write.size.SizeSuffixISU;

import java.util.List;

public class SizeSuffixTest {

    private record SuffixTestData(SizeSuffix suffix, Long val, String result) {

        public void test() {
            Assertions.assertEquals(suffix.getSuffix(val), result);
        }

    }

    private final List<SuffixTestData> baseTest = List.of(
            new SuffixTestData(
                    SizeSuffixISU.BYTE, 200L, "200 Byte"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.BYTE, 200L, "200 Byte"
            ),
            new SuffixTestData(
                    SizeSuffixISU.BYTE, 234L, "234 Byte"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.BYTE, 234L, "234 Byte"
            ),
            new SuffixTestData(
                    SizeSuffixISU.MEGABYTE, 200L, "200,00 MB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.MEGABYTE, 200L, "200,00 MiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.MEGABYTE, 234L, "234,00 MB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.MEGABYTE, 234L, "234,00 MiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.KILOBYTE, 200L, "200,00 KB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.KILOBYTE, 200L, "200,00 KiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.KILOBYTE, 234L, "234,00 KB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.KILOBYTE, 234L, "234,00 KiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.GIGABYTE, 200L, "200,00 GB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.GIGABYTE, 200L, "200,00 GiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.GIGABYTE, 234L, "234,00 GB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.GIGABYTE, 234L, "234,00 GiB"
            )
    );


    @Test
    public void baseTest() {
        for (SuffixTestData data : baseTest) {
            data.test();
        }
    }

    private final List<SuffixTestData> nextSizeTest = List.of(
            new SuffixTestData(
                    SizeSuffixISU.BYTE, 2000L, "2,00 KB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.BYTE, 2000L, "1,95 KiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.BYTE, 2342L, "2,34 KB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.BYTE, 2342L, "2,29 KiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.MEGABYTE, 2000L, "2,00 GB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.MEGABYTE, 2000L, "1,95 GiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.MEGABYTE, 2342L, "2,34 GB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.MEGABYTE, 2342L, "2,29 GiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.KILOBYTE, 2000L, "2,00 MB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.KILOBYTE, 2000L, "1,95 MiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.KILOBYTE, 2342L, "2,34 MB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.KILOBYTE, 2342L, "2,29 MiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.GIGABYTE, 2000L, "2000,00 GB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.GIGABYTE, 2000L, "2000,00 GiB"
            ),
            new SuffixTestData(
                    SizeSuffixISU.GIGABYTE, 2342L, "2342,00 GB"
            ),
            new SuffixTestData(
                    SizeSuffixIEC.GIGABYTE, 2342L, "2342,00 GiB"
            )
    );

    @Test
    public void nextSizeTest() {
        for (SuffixTestData data : nextSizeTest) {
            data.test();
        }
    }

}
