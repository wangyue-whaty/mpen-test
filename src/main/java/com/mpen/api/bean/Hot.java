package com.mpen.api.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.mp.shared.utils.FUtils;
import com.mpen.api.common.Constants;

/**
 * 微信小程序语音拜年贺卡课本图片信息
 * @author wangyue
 *
 */
public class Hot {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(Hot.class);

    //图片的左边界坐标
    public int left;
    //图片的上边界坐标
    public int top;
    //图片的宽度
    public int height;
    //图片的高度
    public int width;
    //对应页数
    public int num;
    public int code;
    public Map<String,Message> map = new HashMap<>();
    
    public static final class Message{
        //存储课本声音的url
        public String url;
        //对应课本
        public String text;
    }
    
    /**
     * 初始化小程序课本资源数据 
     */
    public static void init() {
        InputStream input = null;
        try {
            if (null == Constants.hotAreas) {
                // TODO：数据以后放在数据库，来生成json,hotAreas要动态获取和更新，不适合放在Constants里面
                input = Hot.class.getResourceAsStream("hot.txt");
                final String str = FUtils.inputToString(input);
                Constants.hotAreas = Constants.GSON.fromJson(str, new TypeToken<Map<String, List<List<Hot>>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
}
