package com.mp.shared.common;

/**
 * Created by wutingyou on 2017/7/3.
 * 出版社定义
 */

public final class Publisher {
    public static final String MP_STR = "麦片出版";
    public static final String WYS_STR = "外语教学与研究出版社";
    public static final String RJS_STR = "人民教育出版社";
    public static final String ZSS_STR = "中国少年儿童新闻出版总社";
    public static final String BJTU_STR = "北京交通大学出版社";
    public static final String BNU_STR = "北京师范大学出版社";

    public enum PublisherID {MP, WYS, RJS, ZSS, BJTU, BNU, OTHER}

    /**
     * 通过出版社String得到出版社enum
     * @param publisherName
     * @return
     */
    public static final PublisherID fromString(String publisherName) {
        if (publisherName == null) {
            return PublisherID.WYS;
        }
        PublisherID publisherID = PublisherID.WYS;
        switch (publisherName) {
            case MP_STR:
            case "MP":
                publisherID = PublisherID.MP;
                break;
            case WYS_STR:
            case "WYS":
                publisherID = PublisherID.WYS;
                break;
            case RJS_STR:
            case "RJS":
                publisherID = PublisherID.RJS;
                break;
            case ZSS_STR:
            case "ZSS":
                publisherID = PublisherID.ZSS;
                break;
            case BJTU_STR:
            case "BJTU":
                publisherID = PublisherID.BJTU;
                break;
            case BNU_STR:
            case "BNU":
                publisherID = PublisherID.BNU;
                break;
            default:
                publisherID = PublisherID.WYS;
                break;
        }
        return publisherID;
    }

    /**
     * 通过enum获取出版社名字
     * @param publisherID
     * @return
     */
    public static final String getPublisherDisplayName(PublisherID publisherID) {
        String displayName = WYS_STR;
        switch (publisherID) {
            case MP:
                displayName = MP_STR;
                break;
            case WYS:
                displayName = WYS_STR;
                break;
            case RJS:
                displayName = RJS_STR;
                break;
            case ZSS:
                displayName = ZSS_STR;
                break;
            case BJTU:
                displayName = BJTU_STR;
                break;
            case BNU:
                displayName = BNU_STR;
                break;
            default:
                displayName = WYS_STR;
                break;
        }
        return displayName;
    }

    /**
     * 是否出版社匹配
     * @param bookPublisher 书本列表当前项的出版社
     * @param publisher 本地设置的出版社
     * @return
     */
    public static final boolean isAllowed(PublisherID bookPublisher, PublisherID publisher){
        return bookPublisher == null || publisher == null || publisher.equals(PublisherID.MP) || bookPublisher.equals(publisher);
    }
}
