package com.mp.tools;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GameAnalysisTest {
    @Test
    public void analyzeGameTest() {
    	final String baseDir = "/Users/feng/Downloads/gameFiles";
    	final String[] games = {
    			"一起一下Game", "一起三上Game", "一起三下Game", "一起二下Game", 
    			"一起五上Game", "一起五下Game", "一起六上Game", "一起六下Game", 
    			"一起四下Game", "三起三下Game", "三起五上Game", "三起五下Game", 
    			"三起六上Game", "三起六下Game", "三起四下Game", "一起一上（2016秋）Game", 
    			"三起三上（2016秋）Game", "一起一上（全国版）Game", 
    			"三起三上（全国版）Game", "一起二上（非分省版本）Game", 
    			"一起四上（非分省版本）Game", "三起四上（非分省版本）Game"
    	};
    	Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    	GameAnalysis.printException = true;
    	for (final String game: games) {
    		System.out.println("\nprocess " + game + " --> ");
            final GameAnalysisResult analyzeGame = GameAnalysis.analyzeGame(baseDir + "/" + game);
            if (analyzeGame.errorFile != null) {
            	System.err.println(prettyGson.toJson(analyzeGame.errorFile));
            } else {
            	System.out.println(prettyGson.toJson(analyzeGame.errorFile));
            }
            System.out.println(analyzeGame.toString());
    	}
    }
}
