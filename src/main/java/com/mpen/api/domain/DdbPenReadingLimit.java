package com.mpen.api.domain;

import java.io.Serializable;
import java.time.Instant;

public class DdbPenReadingLimit implements Serializable {
    private static final long serialVersionUID = 5645604105524919083L;

    private String id;
    private String fkPenId;
    private int times;
    private Instant editDate;

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final String getFkPenId() {
        return fkPenId;
    }

    public final void setFkPenId(String fkPenId) {
        this.fkPenId = fkPenId;
    }

    public final int getTimes() {
        return times;
    }

    public final void setTimes(int times) {
        this.times = times;
    }

    public final Instant getEditDate() {
        return editDate;
    }

    public final void setEditDate(Instant editDate) {
        this.editDate = editDate;
    }

}
