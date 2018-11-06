package com.mpen.api.bean;

public final class Cache {
    private String key;
    private String bookId;
    private String loginId;
    private String id;
    private String version; // 将pageInfo或pageInfo更新为正式版本时使用的唯一标识

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final String getLoginId() {
        return loginId;
    }

    public final void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
