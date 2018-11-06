package com.mp.shared.common;

import java.io.Serializable;

/**
 * Created by feng on 3/5/17.
 * <p>
 * 指定一个资源的版本。版本现在有两个含义： 1）格式版本：定义资源文件／数据的格式
 * 考虑到资源文件包可能的升级，以及笔app代码的升级的步调可能会不一致，我们
 * 需要定义一个formatVersion；这样老版本的代码不会下载新版本的资源文件 2）内容版本：资源文件有时可能会有内容更新，同时格式保持不变。
 * 这个时候用资源更新时间比较好
 */

public final class ResourceVersion implements Comparable<ResourceVersion>, Serializable {
    private static final long serialVersionUID = 557225872615206151L;

    /**
     * 资源内容更新时间
     */
    public final long updateTime;

    /**
     * 资源格式版本号
     */
    public final int formatVersion;

    public ResourceVersion(long updateTime, int formatVersion) {
        this.updateTime = updateTime;
        this.formatVersion = formatVersion;
    }

    /**
     * @return compare updateTime and formatVersion 1: if newer; 0: if same; -1:
     * is old will throw NullPointerException
     */
    @Override
    public int compareTo(ResourceVersion other) {
        // first compare formatVersion, then updateTime
        if (formatVersion > other.formatVersion) {
            return 1;
        } else if (formatVersion < other.formatVersion) {
            return -1;
        } else {
            if (updateTime > other.updateTime) {
                return 1;
            } else if (updateTime < other.updateTime) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * @return 可以被解析回ResourceVersion的String表达
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(STR_PREFIX);
        sb.append(updateTime).append('_').append(formatVersion);
        return sb.toString();
    }

    public static final String STR_PREFIX = "ResVer_";

    /**
     * 从String解析出ResourceVersion
     *
     * @param version 通过toString生成的字符串
     * @return ResourceVerison 如果能正确解析；null 如果不能
     */
    public static ResourceVersion fromString(String version) {
        if (version == null || !version.startsWith(STR_PREFIX)) {
            return null;
        }
        final String[] parts = version.split("_");
        if (parts.length == 3) {
            try {
                return new ResourceVersion(Long.parseLong(parts[1]), Integer.parseInt(parts[2]));
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
