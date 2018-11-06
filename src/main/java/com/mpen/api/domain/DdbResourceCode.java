/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.mp.shared.utils.FUtils;
import com.mpen.api.common.Constants;

public final class DdbResourceCode implements java.io.Serializable {
    private static final long serialVersionUID = -7422948657316855459L;
    private String id;
    private Integer code;
    private String text;
    private String fkBookId;
    private String fkCatalogId;
    private String page;
    private Integer leftOffset;
    private Integer topOffset;
    private Integer rightOffset;
    private Integer bottomOffset;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getLeftOffset() {
        return leftOffset;
    }

    public void setLeftOffset(Integer leftOffset) {
        this.leftOffset = leftOffset;
    }

    public Integer getTopOffset() {
        return topOffset;
    }

    public void setTopOffset(Integer topOffset) {
        this.topOffset = topOffset;
    }

    public Integer getRightOffset() {
        return rightOffset;
    }

    public void setRightOffset(Integer rightOffset) {
        this.rightOffset = rightOffset;
    }

    public Integer getBottomOffset() {
        return bottomOffset;
    }

    public void setBottomOffset(Integer bottomOffset) {
        this.bottomOffset = bottomOffset;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFkBookId() {
        return fkBookId;
    }

    public void setFkBookId(String fkBookId) {
        this.fkBookId = fkBookId;
    }

    public String getFkCatalogId() {
        return fkCatalogId;
    }

    public void setFkCatalogId(String fkCatalogId) {
        this.fkCatalogId = fkCatalogId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void initBookCodeDetail() {
        final Map<String, ArrayList<DdbResourceCode>> bookCodeMap = new HashMap<String, ArrayList<DdbResourceCode>>();
        InputStream input = null;
        for (String fileName : Constants.CODE_FILES) {
            try {
                input = DdbResourceCode.class.getResourceAsStream("bookCode/" + fileName);
                final String str = FUtils.inputToString(input);
                final ArrayList<DdbResourceCode> codeList = Constants.GSON.fromJson(str,
                    new TypeToken<ArrayList<DdbResourceCode>>() {
                    }.getType());
                bookCodeMap.put(codeList.get(0).getFkBookId(), codeList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Constants.codeMap = bookCodeMap;

    }

    public static DdbResourceCode getCode(String bookId, int code) {
        final ArrayList<DdbResourceCode> codeList = Constants.codeMap.get(bookId);
        if (codeList == null) {
            return null;
        }
        final DdbResourceCode resourceCode = new DdbResourceCode();
        resourceCode.setCode(code);
        final Comparator<DdbResourceCode> c = (c1, c2) -> {
            return c1.getCode() - c2.getCode();
        };
        final int index = Collections.binarySearch(codeList, resourceCode, c);
        if (index < 0) {
            return null;
        }
        return codeList.get(index);
    }

}
