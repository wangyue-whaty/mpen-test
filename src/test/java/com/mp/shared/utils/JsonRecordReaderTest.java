package com.mp.shared.utils;

import com.mp.shared.record.RecordBase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by feng on 4/19/17.
 */

public final class JsonRecordReaderTest {
    static class Record implements RecordBase {
        int num;
        String optional;
        Record(int num) {
            this.num = num;
        }
        boolean isEqual(Record other) {
            return num == other.num;
        }

        @Override
        public boolean isValid() {
            return num >= 0;
        }
    }
    static class RecordTwo {
        String num;
    }

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void test() throws IOException {
        // 1. setup the file to two parts: first and second;
        final String first = "#this is a test\n" // 16 bytes
                + "{\"num\": 0}\r\n"  // 12 bytes
                + "\"num\": 0}\r\n"  // invalid, 11
                + "{\"num\": 0\n"  // invalid, 10 bytes
                + "\n" // 1 byte
                + "{\"num\": 1}\n" // 11 bytes
                + "\n" // 1 byte
                + "{\"num\": 2"; // 9 bytes
        final String second = "}\n" // 2 bytes
                + "\n" // 1 byte
                + "{\"num\": 3, \"optional\": \"昨晚测试\"}\n" // 35 bytes
                + "\n";
        final File directory = folder.newFolder();
        final String filename = new File(directory, "records-test").getAbsolutePath();
        // 2. write the two parts in two times; after first write, create reader; after second, create readerFull
        FUtils.stringToFile(filename, first, true);
        assertEquals(first.getBytes().length, FUtils.getFileSize(filename));
        final JsonRecordReader reader = new JsonRecordReader(filename);
        reader.init(0);
        FUtils.stringToFile(filename, second, true);
        assertEquals(first.getBytes().length + second.getBytes().length,
                FUtils.getFileSize(filename));
        final JsonRecordReader readerFull = new JsonRecordReader(filename);
        readerFull.init(0);
        // 3. test each reader/readerFull on expected values
        final String[][] tests = {
                // start offset, read|offsetToEnd, result num, end offset
                {"0", "read", "0", "28"},
                {"2", "read", "0", "28"},
                {"16", "read", "0", "28"},
                {"17", "read", "1", "61"},
                {"28", "read", "1", "61"},
                {"50", "read", "1", "61"},
                {"51", "read", null, "51"},
                {"52", "read", null, "52"},
                // below test readLast
                {"0", "60", "1", "61"},
                {"60", "100", "1", "61"},
                {"60", "0", "1", "61"},

        };
        final String[][] testsFull = {
                // start offset, read|offsetToEnd, result num, end offset
                {"0", "read", "0", "28"},
                {"2", "read", "0", "28"},
                {"16", "read", "0", "28"},
                {"17", "read", "1", "61"},
                {"28", "read", "1", "61"},
                {"50", "read", "1", "61"},
                {"51", "read", "2", "73"},
                {"52", "read", "2", "73"},
                {"73", "read", "3", "113"},
                {"75", "read", null, "75"},
                // below test readLast
                {"0", "60", "3", "113"},
                {"60", "100", "3", "113"},
                {"60", "0", "3", "113"},

        };
        System.out.println("do reader");
        testOne(reader, tests);
        System.out.println("do readerFull");
        testOne(readerFull, testsFull);
        reader.close();
        readerFull.close();
    }

    private void testOne(JsonRecordReader reader, String[][] tests) throws IOException {
        for (final String[] test: tests) {
            System.out.println(Arrays.toString(test));
            final long seek = Long.parseLong(test[0]);
            reader.seek(seek);
            Record r = null;
            if ("read".equals(test[1])) {
                r = reader.read(Record.class);
            } else {
                r = reader.readLast(Long.parseLong(test[1]), Record.class);
            }
            if (test[2] == null) {
                assertTrue(r == null);
            } else {
                assertEquals(Integer.parseInt(test[2]), r.num);
            }
            assertEquals(Integer.parseInt(test[3]), reader.getOffset());
        }
    }
}
