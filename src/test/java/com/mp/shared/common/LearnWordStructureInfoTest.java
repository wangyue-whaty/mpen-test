package com.mp.shared.common;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.mp.shared.common.LearnWordStructureInfo.UnitInfo;

/**
 * Created by ZYT on 16/6/17.
 *
 * 测试 isLearnWord getUnitInfo
 */
public class LearnWordStructureInfoTest {
    BookInfo[] bookInfos;
    LearnWordStructureInfo learnWordStructureInfo;
    Gson gson;

    @Before
    public void dataPreparation() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        bookInfos = new BookInfo[2];
        bookInfos[0] = new BookInfo();
        bookInfos[0].coverCodes = new ShCode[1];
        bookInfos[0].coverCodes[0] = new ShCode(62016, 1);
        bookInfos[1] = new BookInfo();
        bookInfos[1].coverCodes = new ShCode[1];
        bookInfos[1].coverCodes[0] = new ShCode(1073741867, 1);
        gson = new Gson();
        learnWordStructureInfo = gson.fromJson(
            new BufferedReader(new FileReader(new File(getClass().getResource("/learnWord/learnWord.json").getPath()))),
            LearnWordStructureInfo.class);
    }

    @Test
    public void testLearnWordStructureInfo() {
        final boolean[] exceptResult = { false, true };
        for (int i = 0; i < bookInfos.length; i++) {
            assertEquals(LearnWordStructureInfo.isLearnWord(bookInfos[i]), exceptResult[i]);
        }

        final ShCode[] codesTest = { new ShCode(1, 1), new ShCode(60, 1), new ShCode(840, 1) };
        final UnitInfo[] exceptUnitInfo = { learnWordStructureInfo.unitInfos[0], learnWordStructureInfo.unitInfos[1],
            learnWordStructureInfo.unitInfos[23] };
        for (int i = 0; i < codesTest.length; i++) {
            final UnitInfo unitInfo = learnWordStructureInfo.getUnitInfo(codesTest[i]);
            assertEquals(unitInfo.name, exceptUnitInfo[i].name);
            assertEquals(unitInfo.range, exceptUnitInfo[i].range);
        }

        final ShCode[] badCodes = { new ShCode(0, 1), new ShCode(850, 1) };
        for (ShCode shCode : badCodes) {
            final UnitInfo unitInfo = learnWordStructureInfo.getUnitInfo(shCode);
            assertEquals(unitInfo, null);
        }
    }

}
