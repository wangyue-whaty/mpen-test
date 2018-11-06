package com.mpen.api.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.mpen.api.common.Constants;
import com.mpen.api.util.FileUtils;

/**
 * 小程序节日语音贺卡图片资源类
 * @author wangyue
 *
 */
public final class Program implements Serializable {
    private static final long serialVersionUID = 3629820826421978881L;
    public int id;
    // 图片名字前缀 yulan0.jpg yulan1.jpg yulan2.jpg
    public String yulan;
    // 图片名字前缀 share0.jpg share1.jpg share2.jpg
    public String share;
    // 图片名字前缀 circle0.jpg circle1.jpg circle2.jpg
    public String circle;
    // 图片名字前缀 background0.jpg background1.jpg background2.jpg
    public String background;
    // 图片音频
    public String backgroundAudio;
    // 图片文件路径
    public String image;
    // 图片文件名字
    public String name;
    // 音频域名路径
    public String audio;

    /**
     * 初始化小程序资源
     * TODO programs要动态获取和更新，不适合放在Constants里面
     */
    public static void init() {
        Constants.programs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final Program program = new Program();
            program.id = i;
            program.yulan = FileUtils.getFullRequestPath("/incoming/bainian/yulan" + i + ".jpg");
            program.share = FileUtils.getFullRequestPath("/incoming/bainian/share" + i + ".jpg");
            program.circle = FileUtils.getFullRequestPath("/incoming/bainian/circle" + i + ".jpg");
            program.background = FileUtils.getFullRequestPath("/incoming/bainian/background" + i + ".jpg");
            program.backgroundAudio = FileUtils.getFullRequestPath("/incoming/bainian/background" + i + ".mp3");
            Constants.programs.add(program);
        }
    }
}
