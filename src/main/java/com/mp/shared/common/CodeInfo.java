package com.mp.shared.common;

import java.io.Serializable;

/**
 * Created by feng on 2/23/17.
 * <p>
 * 定义和一个点读码可以从资源文件获取的相关信息集合
 */

public class CodeInfo implements Serializable {
    private static final long serialVersionUID = -8276641794827201737L;
    public static final String VIDEO_TRUE = "true";
    public QuickCodeInfo quickCodeInfo;
    public boolean isGame;
    public boolean isCover;
    public boolean isRegular;

    /**
     * 当前对应的热区相关语言点读信息，所有码都有
     */
    public HotArea.LanguageInfo[] languageInfos;

    /**
     * 当前对应的热区相关码，所有码都有
     */
    public HotArea.RelatedCodes hotAreaCodes;
    // public HotArea.Area hostArea; // 热区范围；这个现在先不提取

    /**
     * 当前点读码对应的 bookInfo. 所有码都有
     */
    public BookInfo bookInfo;

    /**
     * 对应GameInfo。所有码都有
     */
    public GameInfo gameInfo;

    /**
     * 对应PageInfo。现在是 MPen 码有
     */
    public PageInfo pageInfo;
    
    /**
     * 对应ExtraBookInfo。现在是单词学习有
     */
    public ExtraBookInfo extraBookInfo;

    /**
     * @return the bookInfo associated with the codeInfo
     */
    public BookInfo getBookInfo() {
        if (bookInfo != null) {
            return bookInfo;
        } else if (quickCodeInfo != null) {
            return quickCodeInfo.getBookInfo();
        } else {
            return null;
        }
    }
    
    public boolean isVideo(){
        return this.languageInfos != null && this.languageInfos.length > 0 && VIDEO_TRUE.equals(this.languageInfos[0].videoInfo);
    }
}
