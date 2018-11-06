package com.mp.shared.common;

import java.io.Serializable;

/**
 * Created by feng on 2/25/17.
 * <p>
 * 定义一个 灰度图像。
 */

public final class GreyImg implements Serializable {
    private static final long serialVersionUID = 3743920497704721012L;
    /**
     * greyImg is a special representation of greyscale image It's at least
     * width*height bytes long. Its first width*height bytes are a row-based
     * representation of pixel's grey value: each pixel grey scale is one byte
     */
    public final byte[] greyImg;
    public final int width;
    public final int height;

    /**
     * @return a full YUV image or just a barebone representation of pixels'
     * grey scale
     */
    public boolean isFullImg() {
        return greyImg.length >= (4 * width * height);
    }

    public GreyImg(byte[] greyImg, int width, int height) {
        this.greyImg = greyImg;
        this.width = width;
        this.height = height;
    }
}
