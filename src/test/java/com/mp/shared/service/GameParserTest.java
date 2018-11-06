package com.mp.shared.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mp.shared.common.GameInfo;
import com.mp.shared.common.HotArea;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by feng on 4/9/17.
 *
 * 解析游戏二进制文件，并且对比预定json
 */

public final class GameParserTest {
    HotArea.LanguageInfo[] gamePromptVoices;
    HotArea.LanguageInfo[] gameVoices;
    Gson prettyGson;
    Gson gson;
    @Before
    public void setupGameInfo() {
        gamePromptVoices = new HotArea.LanguageInfo[20];
        for (int i = 0; i < gamePromptVoices.length; i++) {
            gamePromptVoices[i] = new HotArea.LanguageInfo();
            gamePromptVoices[i].soundFile = String.format("p_%02d", i);
        }
        gameVoices = new HotArea.LanguageInfo[10];
        for (int i = 0; i < gameVoices.length; i++) {
            gameVoices[i] = new HotArea.LanguageInfo();
            gameVoices[i].soundFile = String.format("g_%02d", i);
        }
        prettyGson = new GsonBuilder().setPrettyPrinting().create();
        gson = new Gson();
    }
    @Test
    public void testGameParser() {
        final String[] testFiles = {
            "/gameFiles/hot.mp3",
            "/gameFiles/lianXian.mp3",
            "/gameFiles/wenDa.mp3",
            "/gameFiles/wenDas.mp3",
            "/gameFiles/codes.mp3",
        };
        for (final String testFile: testFiles) {
            try {
                final GameInfo gameInfo = GameParser.build(
                        gamePromptVoices, gameVoices,
                        new File(getClass().getResource(testFile).getPath()));
                /*
                System.out.println(testFile);
                System.out.println("--->");
                System.out.println(prettyGson.toJson(gameInfo));
                */
                final String parsedGameJson = gson.toJson(gameInfo);
                // read json
                final GameInfo jsonGame = gson.fromJson(
                        new BufferedReader(new FileReader(
                                new File(getClass().getResource(testFile+".json").getPath()))),
                        GameInfo.class);
                final String jsonGameJson = gson.toJson(jsonGame);
                assertEquals(parsedGameJson, jsonGameJson);
            } catch (Exception e) {
                e.printStackTrace();
                fail("failed parsing " + testFile);
            }
        }
    }
}
