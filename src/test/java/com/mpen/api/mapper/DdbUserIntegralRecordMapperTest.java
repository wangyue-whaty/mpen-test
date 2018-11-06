package com.mpen.api.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbUserIntegral;
import com.mpen.api.domain.DdbUserIntegralRecord;
import com.mpen.api.util.CommUtil;

/**
 * 
 * 用户积分Mapper层测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserIntegralRecordMapperTest extends TestBase {

    @Autowired
    private DdbUserIntegralRecordMapper ddbUserIntegralRecordMapper;

    /**
     * 测试用户积分
     */
    @Test
    public void testDdbUserIntegralRecord() {
        ddbUserIntegralRecordMapper.deleteUserAllIntegral("test01");
        ddbUserIntegralRecordMapper.deleteUserAllIntegral("test02");
        final Map<String, String> day = CommUtil.getDay();
        final String todayDate = day.get("todayDate");
        final String beforeDate = day.get("beforeDate");
        // 模拟两个用户,保存用户积分
        final int save01 = ddbUserIntegralRecordMapper.save(this.getUserIntegralRecord("test01", 1000, "课前预习"));
        Assert.assertEquals(save01, 1);
        final int save02 = ddbUserIntegralRecordMapper.save(this.getUserIntegralRecord("test01", 10000, "扫码绑定"));
        Assert.assertEquals(save02, 1);
        final int save03 = ddbUserIntegralRecordMapper.save(this.getUserIntegralRecord("test02", 10000, "点读新书"));
        Assert.assertEquals(save03, 1);
        final int save04 = ddbUserIntegralRecordMapper.save(this.getUserIntegralRecord("test02", 200, "登录1-2"));
        Assert.assertEquals(save04, 1);
        // 获取全部用户积分
        final List<DdbUserIntegralRecord> all = ddbUserIntegralRecordMapper.getAll("test01", 0, 2);
        Assert.assertEquals(all.size(), 2);
        // 获取积分排行
        final List<DdbUserIntegral> allUserRanking = ddbUserIntegralRecordMapper.getAllUserRanking(beforeDate,
                todayDate, 0, 10);
        for (DdbUserIntegral ddbUserIntegral : allUserRanking) {
            Assert.assertEquals(ddbUserIntegral.getFkLoginId(), ddbUserIntegral.getRank() == 1 ? "test01" : "test02");
        }
        // 获取用户总积分
        final DdbUserIntegral userIntegral = ddbUserIntegralRecordMapper.getUserIntegral("test01", beforeDate,
                todayDate);
        // 时间精确到天
        final String date = new java.sql.Date(new Date().getTime()).toString();
        String[] split = date.substring(0, date.lastIndexOf("-")).split("-");
        // 获取用户当月积分
        int monthIntegralSum = ddbUserIntegralRecordMapper.getMonthIntegralSum("test01", split[1], split[0]);
        Assert.assertEquals(monthIntegralSum, 11000);
        Assert.assertEquals(userIntegral.getScore(), 11000);
        final int userScore = ddbUserIntegralRecordMapper.getUserScore("test01", beforeDate, todayDate);
        Assert.assertEquals(userScore, 11000);
        DdbUserIntegral userNewIntegral = ddbUserIntegralRecordMapper.getUserNewIntegral("test02", beforeDate,
                todayDate, 1000);
        // 第一名
        Assert.assertEquals(userNewIntegral.getRank(), 1);
        // 查询出总人数
        final int count = ddbUserIntegralRecordMapper.getNumberSum(beforeDate, todayDate);
        Assert.assertEquals(count, 6);
        List<String> list = new ArrayList<>();
        list.add("test01");
        list.add("test02");
        // 好友排行情况
        final List<DdbUserIntegral> friendRanking = ddbUserIntegralRecordMapper.getFriendRanking(list, beforeDate,
                todayDate, 0, 10);
        for (DdbUserIntegral ddbUserIntegral : friendRanking) {
            Assert.assertEquals(userNewIntegral.getRank(), ddbUserIntegral.getFkLoginId() == "test01" ? 2 : 1);
        }
        final int integralSum = ddbUserIntegralRecordMapper.getIntegralSum("test01", todayDate, beforeDate);
        Assert.assertEquals(integralSum, 11000);
        final int count2 = ddbUserIntegralRecordMapper.getCount("test01");
        Assert.assertEquals(count2, 2);
        final DdbUserIntegralRecord userTodayIntegral = ddbUserIntegralRecordMapper.getUserTodayIntegral("test01",
                beforeDate, todayDate, "课前预习");
        final DdbUserIntegralRecord userIntegral2 = ddbUserIntegralRecordMapper.getUserIntegralByloginIdAndType("test01", "课前预习");
        Assert.assertEquals(userIntegral2 != null, true);
        Assert.assertEquals(userTodayIntegral.getIntegral(), 1000);
        final DdbUserIntegral allRankTop = ddbUserIntegralRecordMapper.getAllRankTop(beforeDate, todayDate);
        Assert.assertEquals(allRankTop.getScore(), 11000);
        // 当前用户在好友中的排行
        final DdbUserIntegral inFriendIntegral = ddbUserIntegralRecordMapper.getInFriendIntegral("test01", list,
                beforeDate, todayDate);
        Assert.assertEquals(inFriendIntegral.getRank(), 1);
        final DdbUserIntegral friendRankTop = ddbUserIntegralRecordMapper.getFriendRankTop("test01", beforeDate,
                todayDate);
        Assert.assertEquals(friendRankTop.getRank(), 1);
        final int deleteUserAllIntegral = ddbUserIntegralRecordMapper.deleteUserAllIntegral("test01");
        final int deleteUserAllIntegral2 = ddbUserIntegralRecordMapper.deleteUserAllIntegral("test02");
        Assert.assertEquals(deleteUserAllIntegral, 2);
        Assert.assertEquals(deleteUserAllIntegral2, 2);
    }
}
