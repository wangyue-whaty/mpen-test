package com.mp.shared.utils;

import com.mp.shared.common.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Created by feng on 4/15/17.
 *
 * VersionedFile 定义了一个序列文件，有 共同的 前缀 ＋ 分隔符 ＋ 数字后缀，以及相关操作
 *
 * 序列文件例子，records 文件序列的第00000 和 第00001 个文件
 * records__VF__000000
 * records__VF__000001
 *
 */

public final class VersionedFile {
    private static final String TAG = "VersionedFile";
    public static final String SEP = "__VF__";

    private File directoryFile;
    private final String directory;
    private final String baseName;
    private final String prefix;
    private final int prefixLen;
    private final FilenameFilter filter;
    public VersionedFile(String directory, final String basenName) {
        this.directoryFile = new File(directory);
        this.directory = directoryFile.getPath();  // remove possible trailing "/" of directory
        this.baseName = basenName;
        this.prefix = basenName + SEP;
        this.prefixLen = prefix.length();
        this.filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return getFileVersion(filename) >= 0;
            }
        };
    }

    /**
     * get the file sequence number
     * @param filename assume it is the basename (as returned by File.list())
     * @return -1 if not a valid VersionedFile
     */
    private int getFileVersion(String filename) {
        /**
         * file name is something like:
         * baseName__VF__000xx
         */
        if (filename.length() == prefixLen + 7 && filename.startsWith(prefix)) {
            try {
                return Integer.parseInt(filename.substring(prefixLen));
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * @return all VersionedFile of same origin;
     */
    public String[] getAll() {
        final String[] files = directoryFile.list(filter);
        if (files != null) {
            Arrays.sort(files);
            final int numFiles = files.length;
            // make the return to have full path
            for (int idx = 0; idx < numFiles; ++idx) {
                files[idx] = directory + File.separator + files[idx];
            }
        }
        return files;
    }

    /**
     * @return latest VersionedFile if any
     */
    public String get() {
        final String[] files = getAll();
        if (Utils.isEmpty(files)) {
            return null;
        }
        return files[files.length - 1];
    }

    /**
     * @return next VersionedFile that should be created
     */
    public String getNext() {
        final String latestFile = get();
        final int nextIdx = latestFile == null ? 0
                : getFileVersion(new File(latestFile).getName()) + 1;
        final String fileBase = String.format("%s%s%07d", baseName, SEP, nextIdx);
        return directory + File.separator + fileBase;
    }
}
