package com.mp.shared.common;

import java.io.Serializable;

public class FileModule implements Serializable, IsValid {
    private static final long serialVersionUID = -1642457219900616986L;
    public String name;
    public String md5;
    public long size;
    public String url;

    public FileModule(String name, String md5, long size, String url) {
        this.name = name;
        this.md5 = md5;
        this.size = size;
        this.url = url;
    }

    /**
     * 数据合法性检查
     * @return
     */
    public boolean isValid() {
        return !(name == null || name.length() == 0 ||
                md5 == null || md5.length() == 0 ||
                url == null || url.length() == 0 || size == 0);
    }
}
