package com.mp.shared.common;

import java.io.Serializable;

/**
 * Created by feng on 3/4/17.
 * <p>
 * 给定 code，这里存放可以通过查询内部状态来快速获取的 信息
 */
public final class QuickCodeInfo implements Serializable {
    private static final long serialVersionUID = -7131897689678386998L;

    /* 相关 code, always set */
    public Code code;

    public int language; // default to be 0
    /* below are possible quick info to get. can be null if can not get */
    public PageInfo pageInfo; // this code's related page
    public BookInfo bookInfo; // this code's related book; mostly for MP code

    public BookInfo curBookInfo; // current book info, may be different from
    // bookInfo
    public ExtraBookInfo extraBookInfo;
    // 功能模式值（65001~65020），默认值为65001
    private int functionCode;
    
    public static final int FUNCTION_CODE_MAX = 65020;
    
    public static final int FUNCTION_CODE_MIN = 65001;

    public boolean isValid() {
        return code != null && code.hasValid();
    }
    
    public int getFunctionCode() {
        if( functionCode < FUNCTION_CODE_MIN || functionCode > FUNCTION_CODE_MAX) {
            functionCode = FUNCTION_CODE_MIN;
        }
        return functionCode;
    }

    public void setFunctionCode(int functionCode) {
        this.functionCode = functionCode;
    }
    
    /**
     * @return a new copy with the bookInfo/quickBookInfo/pageInfo to be skeleton
     *         for network call purpose
     */
    public QuickCodeInfo cloneSkeleton() {
        final QuickCodeInfo info = new QuickCodeInfo();
        info.code = this.code;
        info.language = this.language;
        info.functionCode = this.functionCode;
        info.pageInfo = this.pageInfo == null ? null : this.pageInfo.cloneSkeleton();
        info.bookInfo = this.bookInfo == null ? null : this.bookInfo.cloneSkeleton();
        info.curBookInfo = this.curBookInfo == null ? null : this.curBookInfo.cloneSkeleton();
        return info;
    }

    /**
     * @return whether mpPoint is set
     */
    public boolean hasMpPoint() {
    	return code != null && code.mpPoint != null;
    }
    
    /**
     * @return 获取当前相关bookInfo （bookInfo，然后curBookInfo，然后null）
     */
    public BookInfo getBookInfo() {
        assert isValid();
        switch (code.type) {
            case POSITION:
            case SH:
            case OID3:
                if (bookInfo != null) {
                    return bookInfo;
                } else if (curBookInfo != null) {
                    return curBookInfo;
                } else {
                    return null;
                }
            case MP:
                return bookInfo;
            case MPR:
            default:
                return null;
        }
    }

    /**
     * compute mpPoint if there is pageInfo for MP
     * @return true if mpPoint is set
     */
    public boolean mayComputeMp() {
        if (code.mpPoint != null) {
            return true;
        }
        if (code.type == Code.Type.MP && pageInfo != null) {
            code.mpPoint = pageInfo.crtc(code.mpCode, code.prx, code.pry);
        }
        return code.mpPoint != null;
    }
    
    public static QuickCodeInfo getSimpleSHQuickCodeInfo(String bookId, int num, ShCode code){
        final QuickCodeInfo quickCodeInfo = new QuickCodeInfo();
        quickCodeInfo.bookInfo = new BookInfo();
        quickCodeInfo.bookInfo.id = bookId;
        quickCodeInfo.language = num;
        quickCodeInfo.code = code.toCode();
        return quickCodeInfo;
    }

    public MpCode.FPoint computeFPoint() {
        if (code.type == Code.Type.MP && pageInfo != null) {
            return pageInfo.crtcFPoint(code.mpCode, code.prx, code.pry);
        }
        return null;
    }
}
