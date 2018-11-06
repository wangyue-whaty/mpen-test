/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.bean;

import java.io.Serializable;
import java.util.Comparator;

public final class StudyTraceComparator implements Comparator<String>, Serializable {
    private static final long serialVersionUID = 8502085251820576650L;

    /**
     * 页数以p开头大于以s开头，例如p1>s1，字母相同数字大的大，s2>s1.
     * 
     * 
     */
    @Override
    public int compare(String o1, String o2) {
        if (o1.toLowerCase().contains("s") && !o2.toLowerCase().contains("s")) {
            return -1;
        } else if (!o1.toLowerCase().contains("s") && o2.toLowerCase().contains("s")) {
            return 1;
        } else {
            if (o1.length() > o2.length()) {
                return 1;
            } else if (o1.length() < o2.length()) {
                return -1;
            } else {
                return o1.compareTo(o2);
            }
        }
    }

}
