package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mp.shared.common.Code;
import com.mpen.TestBase;
import com.mpen.api.bean.BookLearningInfo;
import com.mpen.api.bean.DateStudyTime;
import com.mpen.api.bean.ExamDetail;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbRecordExamDetail;
import com.mpen.api.domain.DdbRecordUserBook;
import com.mpen.api.domain.ReadingLevelResource;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.service.RecordUserBookService;

/**
 * TODO 学情单元测试，还需修改.
 * 
 * 模拟连续19次点读，每次间隔一分半种，分两次上传数据
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecordUserBookServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    RecordUserBookService recordUserBookService;

    Map<String, Object> map = null;
    List<DdbRecordUserBook> list = null;
    float totalTime = Constants.INT_ZERO;
    Set<String> days = null;
    Map<String, DateStudyTime> dateMap = null;
    List<BookLearningInfo> bookList = null;
    DdbRecordUserBook nodeDeatil = null;
    long date = 0;
    DdbRecordUserBook detail = null;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        map = new HashMap<String, Object>();
        days = new HashSet<String>();
        dateMap = new HashMap<String, DateStudyTime>();
        bookList = new ArrayList<BookLearningInfo>();
        list = new ArrayList<DdbRecordUserBook>();
        date = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            detail = new DdbRecordUserBook();
            detail.setId(i + "");
            detail.setLoginId("13661309890");
            detail.setFkBookId("1126f6d05a844227bc07fb14a7965153");
            detail.setCode("62272");
            detail.setClickTime(new Date(date - i * 90000));
            detail.setType("0");
            detail.setFunction("0");
            detail.setTime(0);
            detail.setCodeType(Code.Type.SH);
            list.add(detail);
        }
    }

    @Test
    public void testDealRecordUserBook() throws SdkException, CacheException {
        recordUserBookService.dealRecordUserBook(nodeDeatil, totalTime, dateMap, list, map, days, bookList);
        nodeDeatil = (DdbRecordUserBook) map.get(Constants.NEST_STUDY_DETAIL);
        totalTime = (float) map.get(Constants.TOTAL_TIME);
        days = (Set<String>) map.get(Constants.DAYS);
        dateMap = (Map<String, DateStudyTime>) map.get(Constants.DATE_MAP);
        bookList = (List<BookLearningInfo>) map.get(Constants.BOOK_LIST);

        Assert.assertEquals(totalTime == 14.5, true);
        list = new ArrayList<DdbRecordUserBook>();
        date += 9 * 90000;
        for (int i = 0; i < 10; i++) {
            detail = new DdbRecordUserBook();
            detail.setId(i + "");
            detail.setLoginId("13661309890");
            detail.setFkBookId("1126f6d05a844227bc07fb14a7965153");
            detail.setCode("62272");
            detail.setClickTime(new Date(date - i * 90000));
            detail.setType("0");
            detail.setFunction("0");
            detail.setTime(0);
            detail.setCodeType(Code.Type.SH);
            list.add(detail);
        }
        recordUserBookService.dealRecordUserBook(nodeDeatil, totalTime, dateMap, list, map, days, bookList);
        nodeDeatil = (DdbRecordUserBook) map.get(Constants.NEST_STUDY_DETAIL);
        totalTime = (float) map.get(Constants.TOTAL_TIME);
        days = (Set<String>) map.get(Constants.DAYS);
        dateMap = (Map<String, DateStudyTime>) map.get(Constants.DATE_MAP);
        bookList = (List<BookLearningInfo>) map.get(Constants.BOOK_LIST);
        Assert.assertEquals(totalTime == 28, true);
    }
    
    /**
     * 阅读分级考试详情测试
     */
    @Test
    public void testSaveExamDetail() {
        ExamDetail examDetail = getTestExamDetail();
        UserSession userSession = getTestUserSession();
        List<DdbRecordExamDetail> examDetails = new ArrayList<>();
        examDetails = recordUserBookService.getExamDetail(userSession);
        // 先 Get -- 应该为空
        Assert.assertEquals(examDetails.size() == 0, true);
        // 再Save
        ReadingLevelResource resource = recordUserBookService.saveExamDetail(userSession, examDetail);
        Assert.assertEquals(resource != null, true);
        // 再Get，这个时候Get的内容应该和Save的内容是一样的。
        examDetails = recordUserBookService.getExamDetail(userSession);
        for (DdbRecordExamDetail ddbRecordExamDetail : examDetails) {
            Assert.assertEquals(ddbRecordExamDetail.getExamId(), examDetail.getExamId());
            Assert.assertEquals(ddbRecordExamDetail.getDuration(), examDetail.getDuration());
            Assert.assertEquals(ddbRecordExamDetail.getLevel(), examDetail.getLevel());
        }
        recordUserBookService.deleteExamDetailByID(examDetail.getExamId());
    }

    /**
     * 根据阅读等级获取对应资源信息测试
     * 目前阅读等级为1-20级
     */
    @Test
    public void testGetReadingSourceByLevel() {
        for (int level = 1; level <= 20; level++) {
            ReadingLevelResource resource = recordUserBookService.getReadingLevelResource(level);
            Assert.assertEquals(resource != null, true);
            Assert.assertEquals(resource.getBookIds() != null, true);
            Assert.assertEquals(resource.getText() != null, true);
        }
    }
}
