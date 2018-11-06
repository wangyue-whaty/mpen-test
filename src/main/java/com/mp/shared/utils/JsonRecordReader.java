package com.mp.shared.utils;

import com.google.gson.Gson;
import com.mp.shared.record.RecordBase;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by feng on 4/18/17.
 *
 * 读取文本文件记录。假定每一行是json String。以 ＃ 开始的行会作为注释忽略。
 */

public class JsonRecordReader {
    private static final String TAG = "RecordReader";

    private final String filename;
    public JsonRecordReader(String filename) {
        this.filename = filename;
    }

    private RandomAccessFile file;
    private long fileLengthLimit;  // the current reading fileLengthLimit
    /**
     * 打开文件，并且跳到知道位置
     */
    public void init(long position) throws IOException {
        this.file = new RandomAccessFile(filename, "r");
        if (position > 0) {
            this.file.seek(position);
        }
        fileLengthLimit = this.file.length();
    }

    /**
     * seek to position
     */
    public void seek(long position) throws IOException {
        this.file.seek(position);
    }
    /**
     * 关闭文件
     */
    public void close() throws IOException {
        if (file != null) {
            file.close();
            file = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    /**
     * 从当前位置，试图读取下一个完整的classOfT json 记录;
     * 并且保持文件指针位置：1）null： 原始位置； 2）not null：紧接当前记录位置
     * @return  null 表明读到当前文件尾部没有发现
     * @throws IOException
     */
    public <T extends RecordBase> T read(Class<T> classOfT) throws IOException {
        final long originalOffset = file.getFilePointer();
        final T result = readInternal(classOfT);
        if (result == null) {
            file.seek(originalOffset);
        }
        return result;
    }

    /**
     * internal implementation
     */
    private <T extends RecordBase> T readInternal(Class<T> classOfT) throws IOException {
        T obj = null;
        while (obj == null) {
            final String originalLine = file.readLine();
            if (originalLine == null) {
                return null;
            } else if (originalLine.isEmpty() || originalLine.startsWith("#")) {
                continue;
            }
            try {
                final String utfLine = new String(originalLine.getBytes("ISO-8859-1"), "UTF-8");
                obj = Json.GSON.fromJson(utfLine, classOfT);
            } catch (Exception e) {
                obj = null;
            }
            final long curPosition = file.getFilePointer();
            if (curPosition > fileLengthLimit) {
                return null;
            }
            if (obj != null && !obj.isValid()) {
                obj = null;  // continue
            }
        }
        return obj;
    }

    /**
     * @return 当前文件偏移
     */
    public long getOffset() throws IOException {
        if (file != null) {
            return file.getFilePointer();
        } else {
            return 0;
        }
    }

    /**
     * 从当前offsetToEnd位置，试图读取接下来完整的classOfT json 记录，返回最后一个有效纪录
     * @return  null 表明读到当前文件尾部没有发现
     * @throws IOException
     */
    public <T extends RecordBase> T readLast(long offsetToEnd, Class<T> classOfT) throws IOException {
        if (offsetToEnd <= fileLengthLimit) {
            file.seek(fileLengthLimit - offsetToEnd);
        } else {
            file.seek(0);
        }
        T lastObj = read(classOfT);
        if (lastObj != null) {
            T nextObj = read(classOfT);
            while (nextObj != null) {
                lastObj = nextObj;
                nextObj = read(classOfT);
            }
        } else if (offsetToEnd < fileLengthLimit) {
            return readLast(fileLengthLimit, classOfT);
        }
        return lastObj;
    }
}
