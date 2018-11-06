package com.mp.shared.common;

import java.io.Serializable;
import java.util.Comparator;

import static java.lang.Math.max;

/**
 * Created by feng on 3/3/17.
 * <p>
 * 定义了MPen码印刷的一页的信息
 * <p>
 * 需要定义一个 sqllite的数据表，把每一页的页面信息都放进去。这样可以方便查询：
 * <p>
 * 名字定义：
 * pixel －－ 图像的像素点
 * point/dot －－ 黑点
 * matrix/code －－ 点阵
 */
public class PageInfo implements Serializable, Comparable<PageInfo>, Comparator<PageInfo>, IsValid {
    private static final long serialVersionUID = 325529719795278823L;
    public String id;  // special UUID string, one without -
    public String bookId; // special UUID string, one without -
    public int pageNum; // 由于印刷原因，一页可能会有不同的MPen铺码。所以可能有多个页面有相同pageNum
    /**
     * 一页 x 总的黑点数是：
     * xCodeNum*matrixSize + (xCodeNum-1)*matrixGap + margin[0] + margin[2]
     * 一页 y 总的黑点数是：
     * yCodeNum*matrixSize + (yCodeNum-1)*matrixGap + margin[1] + margin[3]
     * <p>
     * 一个给定code在本页的code坐标
     * 可以由 xPos = (code - startCode)%xCodeNum
     * yPos = (code - startCode)/xCodeNum
     */
    public MpCode startCode; // endCode = startCode + xCodeNum*yCodeNum - 1
    public int xCodeNum;  // x 轴点阵计数
    public int yCodeNum;  // y 轴点阵计数
    public byte matrixGap; // 两个点阵键的填充黑点个数，一般0，或者1
    public int[] margin; // left, top, right, bottom 页边界margin 黑点计数，不铺code matrix
    public byte matrixSize; // 5, 6, 7, 8

    public byte dotDistanceInPixels = 16; // 黑点间距：缺省 16 像素点
    // 以下的在笔里关系不太大，加上保持全面
    public byte dotSize; // 黑点大小，一般是 2:2X2 或者 3:3X3
    public byte dotShift; // 数据黑点偏移的pixel数量，一般是2

    public ResourceVersion version;

    public SubPageInfo[] subPageInfos;

    public static final class SubPageInfo {
        public int xCodeNum;  // x 轴点阵计数
        public int yCodeNum;  // y 轴点阵计数
        public MpCode startCode; // endCode = startCode + xCodeNum*yCodeNum - 1
        public int[] margin;
        public int num;

        public SubPageInfo(int xCodeNum, int yCodeNum, MpCode startCode, int[] margin, int num) {
            this.xCodeNum = xCodeNum;
            this.yCodeNum = yCodeNum;
            this.startCode = startCode;
            this.margin = margin;
            this.num = num;
        }

        public boolean checkInPage(MpCode mpCode, int[] xy){
            final long temp = mpCode.subtract(this.startCode);
            if(temp >= 0 && temp < this.xCodeNum * this.yCodeNum) {
                xy[0] = (int) (temp % this.xCodeNum);
                xy[1] = (int) (temp / this.xCodeNum);
                return true;
            }
            return false;
        }
    }

    /**
     * TODO 单元测试 binary search
     * 只是对比startCode
     */
    @Override
    public int compare(PageInfo lhs, PageInfo rhs) {
        return lhs.startCode.compareTo(rhs.startCode);
    }

    /**
     * TODO 单元测试 binary search
     * 只是对比startCode
     */
    @Override
    public int compareTo(PageInfo another) {
        return this.startCode.compareTo(another.startCode);
    }

    /**
     * @return a new copy with the id and version to be set and same; everything else is null
     *         for network call purpose
     */
    public PageInfo cloneSkeleton() {
        final PageInfo pageInfo = new PageInfo();
        pageInfo.id = this.id;
        pageInfo.version = this.version;
        return pageInfo;
    }

    /**
     * 检查 mpCode是不是在这个page里面
     *
     * @param mpCode
     * @return {x, y} 如果在； null 如果不在
     */
    public int[] checkInPage(MpCode mpCode) {
        final long diff = mpCode.subtract(startCode);
        if (diff == MpCode.INVALID_DIFF) {
            return null;
        }
        if (diff < 0 || diff >= xCodeNum * yCodeNum) {
            return null;
        }
        final int[] xy = new int[3];
        if(subPageInfos != null && subPageInfos.length > 0){
            for(int i = 0; i < subPageInfos.length; i++) {
                if(subPageInfos[i].checkInPage(mpCode, xy)){
                    xy[2] = i;
                    break;
                }
            }
        } else {
            xy[0] = (int) (diff % xCodeNum);
            xy[1] = (int) (diff / xCodeNum);
            xy[2] = -1;
        }
        return xy;
    }

    /**
     * compute MpCode.Point
     */
    public MpCode.FPoint crtcFPoint(MpCode mpCode, float prx, float pry) {
        final int[] crxy = checkInPage(mpCode);
        if (crxy == null) {
            return null;
        }
        int[] margin;
        int xCodeNum;
        int yCodeNum;
        if(crxy[2] != -1) {
            margin = subPageInfos[crxy[2]].margin;
            xCodeNum = subPageInfos[crxy[2]].xCodeNum;
            yCodeNum = subPageInfos[crxy[2]].yCodeNum;
        } else {
            margin = this.margin;
            xCodeNum = this.xCodeNum;
            yCodeNum = this.yCodeNum;
        }
        final int codeOffset = (matrixSize + matrixGap) * dotDistanceInPixels;
        final int codeRealPixelX = margin[0]/* * dotDistanceInPixels*/ + crxy[0] * codeOffset;
        final int codeRealPixelY = margin[1]/* * dotDistanceInPixels*/ + crxy[1] * codeOffset;
        final int width = (xCodeNum * matrixSize + (xCodeNum - 1) * matrixGap) * dotDistanceInPixels + margin[0] + margin[2] - 1;
        final int height = (yCodeNum * matrixSize + (yCodeNum - 1) * matrixGap) * dotDistanceInPixels + margin[1] + margin[3] - 1;

        final MpCode.FPoint fpoint = new MpCode.FPoint();
        fpoint.x = (prx + codeRealPixelX) / max(width, 1);
        fpoint.y = (pry + codeRealPixelY) / max(height, 1);
        fpoint.pageNum = this.pageNum;
        return fpoint;
    }

    public MpCode.Point crtc(MpCode mpCode, float prx, float pry) {
        final MpCode.FPoint fpoint = crtcFPoint(mpCode, prx, pry);
        return (fpoint == null) ? null : fpoint.toPoint();
    }

    /**
     * compute anchor x,y
     */
    public float[] getAnchorXYs(MpCode mpCode) {
        final int[] crxy = checkInPage(mpCode);
        if (crxy == null) {
            return null;
        }
        int[] margin = null;
        if(subPageInfos != null) {
            margin = subPageInfos[crxy[2]].margin;
        } else {
            margin = this.margin;
        }
        final int codeOffset = (matrixSize + matrixGap) * dotDistanceInPixels;
        final int codeRealPixelX = margin[0]/* * dotDistanceInPixels*/ + crxy[0] * codeOffset;
        final int codeRealPixelY = margin[1]/* * dotDistanceInPixels*/ + crxy[1] * codeOffset;

        final int centerPos = dotDistanceInPixels / 2 + dotSize / 2;
        final int edgeLength = (matrixSize - 1) * dotDistanceInPixels;

        final int startX = codeRealPixelX + centerPos;
        final int startY = codeRealPixelY + centerPos;
        final int endX = startX + edgeLength;
        final int endY = startY + edgeLength;
        final float[] anchorXYs = {
                startX, startY, endX, startY, startX, endY, endX, endY,
        };
        return anchorXYs;
    }

    /**
     * 是否数据合法
     * @return
     */
    @Override
    public boolean isValid() {
        return !(bookId == null || bookId.length() == 0 ||
                id == null || id.length() == 0 || version == null ||
                startCode == null);
    }
}
