package com.mp.shared.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

public class LearnWordStructureInfo implements Serializable, Comparable<LearnWordStructureInfo>, IsValid {
    private static final long serialVersionUID = 6992957228367028229L;
    // TODO jdk1.7不支持 lambda表达式
    private static final Comparator<UnitInfo> COMPARATOR = new Comparator<LearnWordStructureInfo.UnitInfo>() {
        @Override
        public int compare(UnitInfo u1, UnitInfo u2) {
            return u1.range.compareTo(u2.range);
        }
    };
    // TODO 单词学习封面码值范围
    public static final int LEARN_WORK_START_COVER_CODE = 1073741825;
    public static final int LEARN_WORK_END_COVER_CODE = 1073751825;
    public String bookId;
    public UnitInfo[] unitInfos;
    public ResourceVersion version;
    public int sequence; // 序列号

    public LearnWordStructureInfo() {
    }

    public LearnWordStructureInfo(String bookId, UnitInfo[] unitInfos, ResourceVersion version, int sequence) {
        this.bookId = bookId;
        this.unitInfos = unitInfos;
        this.version = version;
        this.sequence = sequence;
    }

    /**
     * 校验是否为单词学习
     * 
     */
    public static boolean isLearnWord(BookInfo bookInfo) {
        if (bookInfo != null && bookInfo.coverCodes != null) {
            for (final ShCode shCode : bookInfo.coverCodes) {
                if (shCode.code >= LEARN_WORK_START_COVER_CODE && shCode.code <= LEARN_WORK_END_COVER_CODE) {
                    return true;
                }
            }
        }
        return false;
    }

    public UnitInfo getUnitInfo(ShCode code) {
        if (unitInfos == null || code == null) {
            return null;
        }
        final UnitInfo unit = new UnitInfo(null, new ShCode.Range(code.code, 0, code.subType));
        int index = Arrays.binarySearch(unitInfos, unit, COMPARATOR);
        if (index == -1) {
            return null;
        } else if (index < -1) {
            index = Math.abs(index) - 2;
        }
        if (unitInfos[index].range.checkInRange(code)) {
            return unitInfos[index];
        }
        return null;
    }

    public static final class UnitInfo implements Serializable {
        private static final long serialVersionUID = 3693541721793458485L;
        public final String name;
        public final ShCode.Range range;

        public UnitInfo(String name, ShCode.Range range) {
            this.name = name;
            this.range = range;
        }
    }

    @Override
    public int compareTo(LearnWordStructureInfo o) {
        return this.sequence - o.sequence;
    }
    
    /**
     * 基本数据合法性校验
     * @return
     */
    public boolean isValid() {
        return !(bookId == null || bookId.length() == 0 || version == null);
    }
}
