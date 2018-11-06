package com.mp.shared.record;

import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mp.shared.record.YZSResult.Line;
import com.mp.shared.record.YZSResult.Word;

/*
 * 云知声或驰声口语评测原始数据转换类
 */
public final class RecognizeTxtParser {
    public static final class RecognizeInfo {
        // 单词文本
        String text;
        // 分数
        float score;
        // 类型
        int type;

    }

    /**
     * 解析云之声或者驰声原始数据
     * @param userRecognizeTxt
     * @return recognizeInfoList 返回给app的评测单词信息
     */
    public static ArrayList<RecognizeInfo> ParseYZSOrChivox(String userRecognizeTxt) {
        ArrayList<RecognizeInfo> recognizeInfoList = new ArrayList<>();
        if (userRecognizeTxt != null && !userRecognizeTxt.isEmpty()) {
            // 解析云知声原始数据
            final YZSResult yzsResult = new Gson().fromJson(userRecognizeTxt, YZSResult.class);
            final ArrayList<Line> lines = yzsResult.lines;
            // 使用yzsResult.lines是否为空判断云知声或驰声评测结果
            if (yzsResult.lines != null) {
                for (Line line : lines) {
                    final ArrayList<Word> yzswords = line.words;
                    for (Word word : yzswords) {
                        final RecognizeInfo recognizeInfo = new RecognizeInfo();
                        recognizeInfo.text = word.text;
                        recognizeInfo.type = word.type;
                        recognizeInfo.score = word.score;
                        recognizeInfoList.add(recognizeInfo);
                    }
                }
            } else {
                //解析驰声原始数据
                String words = ChivoxResult.getWords(userRecognizeTxt);
                recognizeInfoList = new Gson().fromJson(words, new TypeToken<List<RecognizeInfo>>() {}.getType()); 
            }
            return recognizeInfoList;
        }
        return null;
    }
}
