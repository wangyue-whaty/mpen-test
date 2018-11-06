package com.mpen.api.mapper;
/*
 * 用户日志测试类
 * 
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.bean.ActivityStudyDetail;
import com.mpen.api.common.CompressionTools;
import com.mpen.api.domain.DdbRecordUserBook;
import com.mpen.api.util.CommUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public final class RecordUserBookMapperTest extends TestBase {
    @Autowired
    RecordUserBookMapper recordUserBookMapper;
    

    /**
     * 测试RecordUserBookMapper类中ddb_record_user_book、ddb_record_user_book_sencond和ddb_new_record_user_book_shard_i(i=0,1,2,3)联合查询正确性
     */
    @Test
    public void ShardRecordUserBookDatatest() {
        //建立4个测试用户 loginId%4为 0,1,2,3
        final String[] loginIds = { "15003232833", "15003223834", "15003232835", "15003232836" };
        final Date startDate = this.getDate("2018-09-10 10:10:47");
        final Date endDate = this.getDate("2018-09-13 10:10:47");
        for (int i=0;i<loginIds.length;i++) {
            final String recordTableName = CommUtil.getRecordTableName(loginIds[i]);
            // 删除旧的测试数据
            recordUserBookMapper.delete(loginIds[i]);
            recordUserBookMapper.deleteOldTable(loginIds[i]);
            recordUserBookMapper.deleteShard(loginIds[i], recordTableName);
            // 向ddb_record_user_book中插入数据
            recordUserBookMapper.saveOldTable(this.getDdbRecordUserBook01(i,loginIds[i], recordTableName));
            // 判断是否插入数据以及查询出数据
            testShardRecordUserBookData(1, loginIds[i], this.getDdbRecordUserBook01(i,loginIds[i], recordTableName).getFkBookId(), startDate, endDate);
            // 向ddb_record_user_book_second中插入数据
            recordUserBookMapper.save(this.getDdbRecordUserBook02(i,loginIds[i], recordTableName));
            // 判断是否插入数据以及查询数据
            testShardRecordUserBookData(2, loginIds[i], this.getDdbRecordUserBook02(i,loginIds[i], recordTableName).getFkBookId(), startDate, endDate);
            // 向ddb_new_record_user_book_shard_i(i=0,1,2,3)中插入数据
            recordUserBookMapper.saveToShardTable( this.getDdbRecordUserBook03(i,loginIds[i], recordTableName),recordTableName);
            // 判断是否插入数据以及查询出数据
            testShardRecordUserBookData(3, loginIds[i], this.getDdbRecordUserBook03(i,loginIds[i], recordTableName).getFkBookId(), startDate, endDate);
            // 检验 userRecognizedTxt/Bytes是不是有值
            checkUserRecognizedTxtBtyes(this.getDdbRecordUserBook01(i,loginIds[i], recordTableName), loginIds[i]);
        }
        recordUserBookMapper.deleteShard(loginIds[1], CommUtil.getRecordTableName(loginIds[1]));
        DdbRecordUserBook ddbRecordUserBook04 = this.getDdbRecordUserBook04(CommUtil.genRecordKey(), loginIds[1]);
        DdbRecordUserBook ddbRecordUserBook05 = this.getDdbRecordUserBook05(CommUtil.genRecordKey(), loginIds[1]);
        int saveToShardTable = recordUserBookMapper.saveToShardTable(ddbRecordUserBook04,  CommUtil.getRecordTableName(loginIds[1]));
        Assert.assertEquals(saveToShardTable, 1);
        int saveToShardTable2 = recordUserBookMapper.saveToShardTable(ddbRecordUserBook05,  CommUtil.getRecordTableName(loginIds[1]));
        Assert.assertEquals(saveToShardTable2, 1);
        // 最大口语评测分数
        double maxScore = recordUserBookMapper.getMaxScore(ddbRecordUserBook04.getLoginId(), ddbRecordUserBook04.getFkActivityId(), ddbRecordUserBook04.getText(), "2018-09-10 10:10:47", "2018-09-14 10:10:47", CommUtil.getRecordTableName(loginIds[1]));
        Assert.assertEquals((double)maxScore, (double)80,0);
        // activity级别点读次数
        int countTimes = recordUserBookMapper.getCountTimes(ddbRecordUserBook05.getLoginId(), ddbRecordUserBook05.getFkActivityId(),  "2018-09-10 10:10:47",  "2018-09-14 10:10:47",  CommUtil.getRecordTableName(loginIds[1]));
        Assert.assertEquals(countTimes, 2);
    }
    
    /**
     * 测试recordUserBookMapper类中getSpokenDetailByLoginIdAndBookIds,getStudyDetailByLoginIdAndBookIds方法
     */
    @Test
    public void getDetailByLoginIdAndBookIdsTest() {
        final String[][] conditions = {{"13405325800", "2018-10-22 10:10:47", "2018-10-23 10:10:47"},{"13405325800", "2018-10-23 10:10:47", "2018-10-27 10:10:47"}};
        final List<String> bookIds = new ArrayList<>();
        bookIds.add("ff808081581deb4101581e74ac7d0088");
        bookIds.add("ff80808156ca3d900156cb19feff004c");
        for (int i=0; i<conditions.length; i++) {
            final String tableName = CommUtil.getRecordTableName(conditions[i][0]);
            final List<ActivityStudyDetail> spokenDetails = recordUserBookMapper.getSpokenDetailByLoginIdAndBookIds(conditions[i][0], bookIds, conditions[i][1], conditions[i][2], tableName);
            final List<ActivityStudyDetail> studyDetails = recordUserBookMapper.getStudyDetailByLoginIdAndBookIds(conditions[i][0], bookIds, conditions[i][1], conditions[i][2], tableName);
            if (i == 0) {
                Assert.assertEquals(spokenDetails.size(), 0);
                Assert.assertEquals(studyDetails.size(), 0);
                continue;
            }
            Assert.assertEquals(spokenDetails.size(), 9);
            Assert.assertEquals(studyDetails.size(), 40);
        }
        
    }
    
    /**
     * 测试6条查询学情sql的正确性
     */
    private void testShardRecordUserBookData(int size, String loginId, String bookId, Date startDate, Date endDate) {
        List<ActivityStudyDetail> activityStudyDetails = recordUserBookMapper.getSpokenDetailByLoginIdAndBookId(loginId,
                bookId,CommUtil.getRecordTableName(loginId));
        Assert.assertEquals(activityStudyDetails.size() == size, true);
        List<DdbRecordUserBook> ddbRecordUserBooks01 = recordUserBookMapper.getByLoginId(loginId,CommUtil.getRecordTableName(loginId));
        // 测试排序效果
        for (int idx = 0; idx < ddbRecordUserBooks01.size() - 1; idx ++) {
            DdbRecordUserBook lastBook = ddbRecordUserBooks01.get(idx);
            DdbRecordUserBook book = ddbRecordUserBooks01.get(idx + 1);
            // 比较前一次的时间是否大于等于后一次时间
            Assert.assertEquals(true, lastBook.getClickTime().getTime() >= book.getClickTime().getTime());
        }
        Assert.assertEquals(ddbRecordUserBooks01.size() == size, true);
        List<DdbRecordUserBook> ddbRecordUserBooks02 = recordUserBookMapper.getByLoginIdAndDate(loginId, startDate,CommUtil.getRecordTableName(loginId));
        Assert.assertEquals(ddbRecordUserBooks02.size() == size, true);
        List<DdbRecordUserBook> ddbRecordUserBooks03 = recordUserBookMapper.getByLoginIdAndBookId(loginId, bookId,CommUtil.getRecordTableName(loginId));
        Assert.assertEquals(ddbRecordUserBooks03.size() == size, true);
        List<ActivityStudyDetail> activityStudyDetails01 = recordUserBookMapper
                .getSpokenDetailByLoginIdAndBookId(loginId, bookId,CommUtil.getRecordTableName(loginId));
        Assert.assertEquals(activityStudyDetails01.size() == size, true);
        List<DdbRecordUserBook> ddbRecordUserBooks04 = recordUserBookMapper.getWeeklyRecord(loginId, startDate,
                endDate,CommUtil.getRecordTableName(loginId));
        Assert.assertEquals(ddbRecordUserBooks04.size() == size, true);
        // TODO 测试的方法因表数据量大，查询不出数据，要在新的数据pipeline中能够处理，查询该数据，此处单元测试屏蔽，影响单元测试
        /*if (size != 2) {
            List<GoodsInfo> bookCustomTimes = recordUserBookMapper.getBookCustomTimes();
            Assert.assertEquals(bookCustomTimes.size() == size, true);
            List<BookRanking> bookRandings = recordUserBookMapper.getBookRanding(startDateLocal, endDateLocal);
            Assert.assertEquals(bookRandings.size() == size, true);
        } else {
            // recordUserBookMapper中getBookRanding方法、getBookCustomTimes方法返回数据条件不一样，另设条件检验
            recordUserBookMapper.delete(loginId);
            recordUserBookMapper.deleteOldTable(loginId);
            recordUserBookMapper.save(ddbRecordUserBook);
            ddbRecordUserBook.setFkBookId("ff8080815847b3010158489abb5600ae");
            recordUserBookMapper.saveOldTable(ddbRecordUserBook);
            List<GoodsInfo> bookCustomTimes = recordUserBookMapper.getBookCustomTimes();
            Assert.assertEquals(bookCustomTimes.size() == size, true);
            List<BookRanking> bookRandings = recordUserBookMapper.getBookRanding(startDateLocal, endDateLocal);
            Assert.assertEquals(bookRandings.size() == size, true);
        }*/
    }
    
    /**
     * 检验 userRecognizedTxt/Bytes
     */
    private void checkUserRecognizedTxtBtyes(DdbRecordUserBook ddbRecordUserBook, String loginId) {
        final String userRecognizeTxt = "[{\"score\":7.979,\"text\":\"play\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":8.542,\"text\":\"way\",\"type\":2}]";
        byte[] userRecognizeTxtBytes = CompressionTools.zip(userRecognizeTxt);
        ddbRecordUserBook.setUserRecognizeBytes(userRecognizeTxtBytes);
        recordUserBookMapper.delete(loginId);
        recordUserBookMapper.save(ddbRecordUserBook);
        List<DdbRecordUserBook> ddbRecordUserBooks = recordUserBookMapper.getByLoginId(loginId,CommUtil.getRecordTableName(loginId));
        Assert.assertEquals(ddbRecordUserBooks.get(0).getUserRecognizeBytes() == null, false);
        final String userRecognizeTxtUnzip = CompressionTools.unzip(ddbRecordUserBooks.get(0).getUserRecognizeBytes());
        Assert.assertEquals(userRecognizeTxtUnzip.equals("") || userRecognizeTxtUnzip == null, false);
    }
}
