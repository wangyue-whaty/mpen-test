package com.mp.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.mp.shared.common.GameInfo;
import com.mp.shared.common.HotArea;
import com.mp.shared.service.GameParser;
import com.mp.tools.GameAnalysisResult.ErrorFile;
import com.mp.tools.GameAnalysisResult.GameResult;
import com.mp.tools.GameAnalysisResult.QuestionAnswerResult;
import com.mp.tools.GameAnalysisResult.Type;

/**
 * 游戏分析工具类.
 * 
 * @author zyt
 *
 */
public final class GameAnalysis {
    public static Gson gson = new Gson();
    // 是否打印错误信息
    public static boolean printException = true;

    /**
     * TODO 对游戏进行分析的具体方法.
     * 
     * 获取到游戏文件夹中的所有游戏文件，通过文件名中码值前缀，对文件进行分类，区分出不同的游戏，
     * 在不同的游戏中通过文件名中的语言类型后缀，去除游戏中语言类型最大的一个，此文件就是此游戏
     * 的游戏内容二进制文件，对此文件解析，获取所有游戏的GameInfo对象。对获取到的GameInfo对象
     * 进行归类，创建出游戏分析结果的GameAnalysisResult对象，并返回.
     * 
     * @param folderPath
     *            游戏文件夹地址.
     * @return
     */
    public static GameAnalysisResult analyzeGame(String folderPath) {
        HotArea.LanguageInfo[] gamePromptVoices = new HotArea.LanguageInfo[20];
        for (int i = 0; i < gamePromptVoices.length; i++) {
            gamePromptVoices[i] = new HotArea.LanguageInfo();
            gamePromptVoices[i].soundFile = String.format("p_%02d", i);
        }
        HotArea.LanguageInfo[] gameVoices = new HotArea.LanguageInfo[10];
        for (int i = 0; i < gameVoices.length; i++) {
            gameVoices[i] = new HotArea.LanguageInfo();
            gameVoices[i].soundFile = String.format("g_%02d", i);
        }
        File files = new File(folderPath);
        File[] listFiles = files.listFiles();
        // TODO 获取所有游戏中的内容二进制文件.
        Map<String, File> map = new HashMap<String, File>();
        for (File file : listFiles) {
            String[] split = file.getName().split("_");
            // TODO 提示音特殊处理.
            if (split[0].equals("8FFFFFFF")) {
                continue;
            }
            File gameFile = map.get(split[0]);
            if (gameFile == null) {
                map.put(split[0], file);
                continue;
            }
            if (split[1].compareTo(gameFile.getName().split("_")[1]) > 0) {
                map.put(split[0], file);
            }
        }
        List<ErrorFile> errorFileList = null;
        // TODO 解析获取所有gameInfo并归类
        Map<Type, Map<Integer, List<GameInfo>>> gameMap = new HashMap<GameAnalysisResult.Type, Map<Integer, List<GameInfo>>>();
        Map<Integer, List<GameInfo>> questionMap = null;
        ErrorFile errorFile = null;
        int questionNum = 0;
        for (String str : map.keySet()) {
            File file = map.get(str);
            GameInfo gameInfo;
            try {
                gameInfo = GameParser.build(gamePromptVoices, gameVoices, file);
                questionNum = gameInfo.questionAnswers.length;
                List<GameInfo> list = null;
                Type type = null;
                if (gameInfo.questionAnswers[0].answerCodes[0].playCodeOwnVoice) {
                    type = Type.LINK;
                } else {
                    type = Type.QUESTION;
                }
                questionMap = gameMap.get(type);
                if (questionMap == null) {
                    questionMap = new HashMap<Integer, List<GameInfo>>();
                }
                list = questionMap.get(questionNum);
                if (list == null) {
                    list = new ArrayList<GameInfo>();
                }
                list.add(gameInfo);
                questionMap.put(questionNum, list);
                gameMap.put(type, questionMap);
            } catch (Exception e) {
                if (printException) {
                    System.out.println("file parsing error --> " + file.getName());
                    e.printStackTrace();
                }
                if (errorFileList == null) {
                    errorFileList = new ArrayList<ErrorFile>();
                }
                errorFile = new ErrorFile();
                errorFile.fileName = file.getName();
                errorFile.errorMsg = e.getMessage();
                if (StringUtils.isEmpty(errorFile.errorMsg)) {
                    errorFile.errorMsg = e.toString();
                }
                errorFileList.add(errorFile);
            }
        }
        // TODO 创建GameAnalysisResult对象并赋值.
        GameAnalysisResult gameAnalysisResult = new GameAnalysisResult();
        if (errorFileList != null) {
            gameAnalysisResult.errorFile = errorFileList.toArray(new ErrorFile[errorFileList.size()]);
        }
        gameAnalysisResult.gameResult = new GameResult[gameMap.keySet().size()];
        int i = 0;
        for (Type type : gameMap.keySet()) {
            Map<Integer, List<GameInfo>> gameResultMap = gameMap.get(type);
            gameAnalysisResult.gameResult[i] = new GameResult();
            gameAnalysisResult.gameResult[i].result = new QuestionAnswerResult[gameResultMap.keySet().size()];
            gameAnalysisResult.gameResult[i].gameType = type;
            int j = 0;
            int count = 0;
            for (Integer num : gameResultMap.keySet()) {
                List<GameInfo> list = gameResultMap.get(num);
                gameAnalysisResult.gameResult[i].result[j] = new QuestionAnswerResult();
                gameAnalysisResult.gameResult[i].result[j].questionNum = num;
                gameAnalysisResult.gameResult[i].result[j].gameNum = list.size();
                gameAnalysisResult.gameResult[i].result[j].games = list.toArray(new GameInfo[list.size()]);
                count += list.size();
                j++;
            }
            gameAnalysisResult.gameResult[i].gameNum = count;
            i++;
        }
        return gameAnalysisResult;
    }
}
