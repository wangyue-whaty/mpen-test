package com.mpen.api.bean;

import java.io.Serializable;

public final class WrongTimePenInfo implements Serializable {
    private static final long serialVersionUID = -4855393049496629098L;
    public final String penId;
    public final long wrongTime;
    public final long currentTime;

    public WrongTimePenInfo(String penId, long wrongTime) {
        this.penId = penId;
        this.wrongTime = wrongTime;
        this.currentTime = System.currentTimeMillis();
    }
}
