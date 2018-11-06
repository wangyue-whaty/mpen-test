package com.mp.shared.utils;

import com.google.gson.Gson;
import com.mp.shared.common.Utils;
import com.mp.shared.record.RecordBase;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;

/**
 * Created by feng on 4/19/17.
 *
 * 结合VersionedFile 和 JsonRecordReader，建立以下处理模型
 * 1）内容有两个VersionedFile组成
 * 一个是存储record有效内容；一个是存储record处理的位置
 */

public final class VersionedRecordProcessor<T extends RecordBase> {
    private static final String TAG = "VersionedRecordProcessor";

    public static final String COMMENT_PREFIX = "#";
    public static final Gson GSON = new Gson();

    private final VersionedFile recordsFile;
    private final VersionedFile commitsFile;
    private final Class<T> classOfT;
    public VersionedRecordProcessor(VersionedFile recordsFile, VersionedFile commitsFile,
                                    Class<T> classOfT) {
        this.recordsFile = recordsFile;
        this.commitsFile = commitsFile;
        this.classOfT = classOfT;
    }

    private static String[] createNewFile(VersionedFile vf) throws IOException {
        final String nextFile = vf.getNext();
        final RandomAccessFile rf = new RandomAccessFile(nextFile, "rw");
        rf.close();
        return vf.getAll();
    }

    private String[] recordsFiles;
    private String[] commitsFiles;
    public void init() throws IOException {
        this.recordsFiles = recordsFile.getAll();
        if (Utils.isEmpty(this.recordsFiles)) {
            this.recordsFiles = createNewFile(recordsFile);
        }
        this.commitsFiles = commitsFile.getAll();
        if (Utils.isEmpty(this.commitsFiles)) {
            this.commitsFiles = createNewFile(commitsFile);
        }
    }

    private long maxRecordsFileSize = 40 * 1024 * 1024;
    private long maxCommitFileSize = 100 * 1024;
    /**
     * set the internal max file size -- i.e. if exceeding, will need to write to a new versioned file
     */
    public void setMaxFileSize(long maxRecordsFileSize, long maxCommitFileSize) {
        this.maxCommitFileSize = maxCommitFileSize;
        this.maxRecordsFileSize = maxRecordsFileSize;
    }

    /**
     * append a record to the versioned records file. create new file if needed
     * @param record the record
     * @param comments the comments to add the the log
     */
    public void appendRecord(T record, String comments) throws IOException {
        if (record == null) {
            return;
        }
        if (appendRecordInternal(GSON.toJson(record), comments,
                recordsFiles[recordsFiles.length - 1], maxRecordsFileSize)) {
            this.recordsFiles = createNewFile(recordsFile);
            //System.out.println("create new recordsFiles:" + recordsFiles.length);
        }
    }

    private static boolean appendRecordInternal(String recordStr, String comments,
                                             String fileToWrite, long maxSize) throws IOException {
        // right now open file each time. TODO: keep open OutputStream
        final StringBuilder sb = new StringBuilder("\n");
        if (!Utils.isEmpty(comments)) {
            sb.append(COMMENT_PREFIX).append(comments).append("\n");
        }
        sb.append(recordStr).append("\n");
        if (!FUtils.stringToFile(fileToWrite, sb.toString(), true)) {
            throw new IOException("append to file failure:" + fileToWrite);
        }
        return FUtils.getFileSize(fileToWrite) > maxSize;
    }
    /**
     * record a process position: identified by:
     * which recordsFile, the position within the file after process, timestamp
     */
    static class CommitRecord implements RecordBase {
        String recordsFile;
        long position;
        long timeInMs;

        @Override
        public boolean isValid() {
            return !Utils.isEmpty(recordsFile) && position >= 0;
        }
    }

    public static final class ProcessUnit<T> {
        public T[] records;
        public Runnable runAfterProcess;

        void add(T record) {
            if (numRecords < records.length) {
                records[numRecords++] = record;
            }
        }

        private int numRecords;
        boolean isFull() {
            return numRecords >= records.length;
        }
        public int getNumRecords() {
            return numRecords;
        }
        public boolean isEmpty() {
            return numRecords == 0;
        }
    }

    private final long COMMIT_READ_OFFSET_TO_END = 2048;
    public ProcessUnit<T> getProcessUnit(int numRecords) throws IOException {
        final ProcessUnit<T> unit = new ProcessUnit<>();
        if (numRecords <= 0) {
            return unit;
        }
        // 1. read last commit
        CommitRecord lastCommit = null;
        for (int idx = commitsFiles.length - 1; lastCommit == null && idx >= 0; --idx) {
            JsonRecordReader rr = null;
            try {
                final String latestCommitFile = commitsFiles[idx];
                rr = new JsonRecordReader(latestCommitFile);
                rr.init(0);
                lastCommit = rr.readLast(COMMIT_READ_OFFSET_TO_END, CommitRecord.class);
            } catch (IOException e) {
            } finally {
                if (rr != null) {
                    try {
                        rr.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        if (lastCommit == null) {
            lastCommit = new CommitRecord();
            lastCommit.recordsFile = recordsFiles[0];
            lastCommit.position = 0;
        }
        // 2. check start record file
        int startRecordFileIdx = 0;
        final int numRecordFiles = recordsFiles.length;
        for (; startRecordFileIdx < numRecordFiles; ++startRecordFileIdx) {
            if (recordsFiles[startRecordFileIdx].equals(lastCommit.recordsFile)) {
                break;
            }
        }
        if (startRecordFileIdx == numRecordFiles) {
            startRecordFileIdx = 0;
            lastCommit.recordsFile = recordsFiles[0];
            lastCommit.position = 0;
        }
        // 3. read the record file
        unit.records = (T[]) Array.newInstance(classOfT, numRecords);
        long startPosition = lastCommit.position;
        for (; !unit.isFull() && startRecordFileIdx < numRecordFiles;
             ++startRecordFileIdx) {
            //System.out.println("in search loop, startRecordFileIdx:" + startRecordFileIdx);
            JsonRecordReader rr = null;
            try {
                final String thisRecordFile = recordsFiles[startRecordFileIdx];
                rr = new JsonRecordReader(thisRecordFile);
                rr.init(startPosition);
                startPosition = 0;  // next time in the loop, this should start from 0
                while (!unit.isFull()) {
                    final T recordObj = rr.read(classOfT);
                    if (recordObj != null) {
                        lastCommit.recordsFile = thisRecordFile;
                        lastCommit.position = rr.getOffset();
                        lastCommit.timeInMs = System.currentTimeMillis();
                        unit.add(recordObj);
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
            } finally {
                if (rr != null) {
                    try {
                        rr.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        // prepare return result
        if (!unit.isEmpty()) {
            final String commitRecordStr = GSON.toJson(lastCommit);
            unit.runAfterProcess = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (appendRecordInternal(commitRecordStr, "",
                                commitsFiles[commitsFiles.length - 1],
                                maxCommitFileSize)) {
                            //System.out.println("create new commitsFile:" + commitsFiles.length);
                            VersionedRecordProcessor.this.commitsFiles = createNewFile(commitsFile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        return unit;
    }
}
