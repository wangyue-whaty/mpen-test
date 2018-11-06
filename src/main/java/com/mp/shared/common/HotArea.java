package com.mp.shared.common;

import java.io.Serializable;

/**
 * Created by feng on 2/23/17.
 *
 * 热区相关信息。定义： ＊ RelatedCodes ＊ Area ＊ LanguageInfo
 */
public final class HotArea implements Serializable {
    private static final long serialVersionUID = 6078051900614153879L;
    public static final Function[] FUNCTIONS = Function.values();
    

    /**
     * 热区标识码
     */
    public static final class RelatedCodes implements Serializable {
        private static final long serialVersionUID = -7450913210307051101L;
        public String id; // TODO 热区要加入UUID － 孔猛
        public ShCode[] shCodes;

        public boolean hasIn(ShCode shCode) {
            return ShCode.isIn(shCodes, shCode);
        }
    }

    /**
     * 热区坐标
     */
    public static final class Area implements Serializable {
        private static final long serialVersionUID = 6818345036368805106L;
        private final int pageNum;
        private final int[][] rects; // left_x, top_y, right_x, bottom_y

        public Area(int pageNum, int[][] rects) {
            this.pageNum = pageNum;
            this.rects = rects;
        }

        /**
         * @return 判断给定坐标是不是在这个热区里面：在true；不在false
         */
        public boolean hasIn(int pageNum, float x, float y) {
            if (pageNum == this.pageNum) {
                // TODO 优化搜索
                for (final int[] rect : rects) {
                    final int leftX = rect[0];
                    final int topY = rect[1];
                    final int rightX = rect[2];
                    final int bottomY = rect[3];
                    if (leftX <= x && x <= rightX && topY <= y && y <= bottomY) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * @return 判断给定坐标是不是在这个热区里面：在true；不在false
         */
        public boolean hasIn(MpCode.Point mpPoint) {
            return mpPoint != null && hasIn(mpPoint.pageNum, mpPoint.x, mpPoint.y);
        }
    }

    /**
     * 和语言以及点读联系在一起的信息
     */
    public static final class LanguageInfo implements Serializable {
        private static final long serialVersionUID = -7519043722139476902L;
        public String language; // English, Chinese, 等等
        public String text; // 对应课文
        /**
         * soundBytes > soundFile > soundUrls 都指向相关发音信息。按优先级使用（soundBytes最高）
         */
        public byte[] soundBytes;
        public String soundFile;
        public String[] soundUrls; // 有多个url，是因为可能有多个CDN提供，客户端选一个就好
        // TODO 兼容考虑，半年后删除
        public String videoInfo;
       
        public VideoInfo[] videos;
        public Function funciton;
        // 最新功能码定义
        public int numFunc;
        public String extra;
        /**
         * @return  优先返回 soundFile，然后是soundUrls
         */
        public String getVoice() {
            if (soundFile != null) {
                return soundFile;
            } else if (soundUrls != null && soundUrls.length > 0) {
                return soundUrls[0];
            } else {
                return null;
            }
        }
    }
    
    /**
     * 不能随便修改顺序以及删除,可以增加
     * INTO_ROLE: 进入角色阅读
     * OUT_ROLE: 退出角色阅读
     * ROLE_A: 角色A
     * ROLE_B: 角色B
     * INTO_EXAM: 点击进入考试
     * START_RECORDING: 开始录音
     * STOP_RECORDING: 停止录音
     * PLAY_RECORDING: 播放录音
     * UPLOAD_RECORDING: 上传录音
     * UPLOAD_ALL_RECORDING: 一键上传所有答案
     */
    public enum Function {
        INTO_ROLE, OUT_ROLE, ROLE_A, ROLE_B, INTO_EXAM, START_RECORDING, STOP_RECORDING, PLAY_RECORDING, UPLOAD_RECORDING, UPLOAD_ALL_RECORDING
    }
    
    public static final class VideoInfo implements Serializable {
        private static final long serialVersionUID = -117270358225316290L;
        public VideoType type;
        public String typeName;
        public String path;
    }
    
    public enum VideoType{
        MSWK("名师微课"), JCDH("教材动画");
        String name;

        VideoType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
