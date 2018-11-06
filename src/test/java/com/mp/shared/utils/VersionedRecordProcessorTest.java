package com.mp.shared.utils;

import com.google.gson.Gson;
import com.mp.shared.record.ActionRecords;
import com.mp.shared.record.RecordBase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by feng on 4/19/17.
 */

public final class VersionedRecordProcessorTest {
    static class Record implements RecordBase {
        int num;
        String optional;
        Record(int num) {
            this.num = num;
        }
        boolean isEqual(JsonRecordReaderTest.Record other) {
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
        final File directory = folder.newFolder();
        final VersionedFile vfRecords = new VersionedFile(directory.getAbsolutePath(), "recordsVF");
        final VersionedFile vfCommits = new VersionedFile(directory.getAbsolutePath(), "commitsVF");
        final VersionedRecordProcessor<Record> processor = new VersionedRecordProcessor<>(
                vfRecords, vfCommits, Record.class);
        processor.init();
        processor.setMaxFileSize(100, 1000);
        int totalNumRecords = 0;
        // 1. write one record
        {
            final VersionedRecordProcessor.ProcessUnit<Record> unit = processor.getProcessUnit(2);
            assertTrue(unit.isEmpty());
        }
        processor.appendRecord(new Record(0), "123");
        totalNumRecords++;
        {
            final String[] recordsFiles = vfRecords.getAll();
            assertEquals(1, recordsFiles.length);
            final String expected = "\n#123\n{\"num\":0}\n";
            assertEquals(expected, FUtils.fileToString(recordsFiles[0]));
            assertEquals(expected.getBytes().length, FUtils.getFileSize(recordsFiles[0]));
        }
        final int loopNum1 = 5;
        for (int idx = 0; idx < loopNum1; ++idx) {  // always read the same because no commit
            final VersionedRecordProcessor.ProcessUnit<Record> unit = processor.getProcessUnit(1);
            assertEquals(1, unit.records.length);
            assertEquals(1, unit.getNumRecords());
            assertEquals(0, unit.records[0].num);
            if (idx == loopNum1 - 1) {
                unit.runAfterProcess.run();
                final VersionedRecordProcessor.ProcessUnit<Record> unit2 = processor.getProcessUnit(2);
                assertTrue(unit2.isEmpty());
            }
        }
        // 2. write 2 more records
        for (; totalNumRecords < 3; ++totalNumRecords) {
            processor.appendRecord(new Record(totalNumRecords), null);
        }
        for (int idx = 0; idx < loopNum1; ++idx) {  // always read the same because no commit
            final VersionedRecordProcessor.ProcessUnit<Record> unit = processor.getProcessUnit(1);
            assertEquals(1, unit.records.length);
            assertEquals(1, unit.getNumRecords());
            assertEquals(1, unit.records[0].num);
        }
        for (int idx = 0; idx < loopNum1; ++idx) {  // always read the same because no commit
            final VersionedRecordProcessor.ProcessUnit<Record> unit = processor.getProcessUnit(2);
            assertEquals(2, unit.records.length);
            assertEquals(2, unit.getNumRecords());
            assertEquals(1, unit.records[0].num);
            assertEquals(2, unit.records[1].num);
            assertTrue(unit.isFull());
            if (idx == loopNum1 - 1) {
                unit.runAfterProcess.run();
                final VersionedRecordProcessor.ProcessUnit<Record> unit2 = processor.getProcessUnit(2);
                assertTrue(unit2.isEmpty());
            }
        }
        // 3. write one bad string
        {
            final String badStr1 = "\n{\"num\":";
            FUtils.stringToFile(vfRecords.get(), badStr1, true);
            for (int idx = 0; idx < loopNum1; ++idx) {  // always read the same because no commit
                final VersionedRecordProcessor.ProcessUnit<Record> unit2 = processor.getProcessUnit(2);
                assertTrue(unit2.isEmpty());
            }
        }
        // 4. write one records in two parts:
        totalNumRecords++;
        final String record4Part1 = "\n{\"num\":";
        final String record4Part2 = "3}\n";
        FUtils.stringToFile(vfRecords.get(), record4Part1, true);
        for (int idx = 0; idx < loopNum1; ++idx) {  // always read the same because no commit
            final VersionedRecordProcessor.ProcessUnit<Record> unit2 = processor.getProcessUnit(2);
            assertTrue(unit2.isEmpty());
        }
        FUtils.stringToFile(vfRecords.get(), record4Part2, true);
        for (int idx = 0; idx < loopNum1; ++idx) {  // always read the same because no commit
            final VersionedRecordProcessor.ProcessUnit<Record> unit = processor.getProcessUnit(1);
            assertFalse(unit.isEmpty());
            assertEquals(1, unit.records.length);
            assertEquals(1, unit.getNumRecords());
            assertEquals(3, unit.records[0].num);
            if (idx == loopNum1 - 1) {
                unit.runAfterProcess.run();
                final VersionedRecordProcessor.ProcessUnit<Record> unit2 = processor.getProcessUnit(2);
                assertTrue(unit2.isEmpty());
            }
        }
        // 5. put in 2 threads and do random read, write tests
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        final int numAdditionalRecords = 1000;
        final int startRecodeNum = totalNumRecords;
        executor.submit(new Runnable() {
            @Override
            public void run() {
                for (int idx = 0; idx < numAdditionalRecords; ++idx) {
                    try {
                        processor.appendRecord(new Record(idx + startRecodeNum), null);
                        //System.out.println("append #" + idx);
                    } catch (IOException e) {
                        fail("failed to append record #" + idx);
                    }
                }
                System.out.println("append done!~");
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                for (int idx = 0; idx < numAdditionalRecords; ++idx) {
                    try {
                        final VersionedRecordProcessor.ProcessUnit<Record> unit = processor.getProcessUnit(1);
                        if (unit.isEmpty()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            --idx;
                        } else {
                            assertEquals(idx + startRecodeNum, unit.records[0].num);
                            unit.runAfterProcess.run();
                        }
                        //System.out.println("read #" + idx);
                    } catch (IOException e) {
                        fail("failed to read record #" + idx);
                    }
                }
                System.out.println("read done!~");
            }
        });
        try {
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                fail("took too long to finish");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("failed in waiting");
        }
        System.out.println("after test, there are recordsFiles:" + vfRecords.getAll().length
                + " commitsFiles:" + vfCommits.getAll().length + " @directory:" + directory.getAbsolutePath());
        /*
        try {
            Thread.sleep(100*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }
}
