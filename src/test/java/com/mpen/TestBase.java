/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mp.shared.common.Code;
import com.mp.shared.common.Exam.SubTopic;
import com.mp.shared.common.FullBookInfo;
import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.ShCode;
import com.mp.shared.record.ActionRecord;
import com.mp.shared.record.ActionRecords;
import com.mp.shared.record.ClickRecord;
import com.mp.shared.record.TaskRecord;
import com.mpen.api.bean.ClassAssignments;
import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.ClassLearnContent;
import com.mpen.api.bean.EnglishBookContent.Activity;
import com.mpen.api.bean.ExamDetail;
import com.mpen.api.bean.Goods;
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.HomeworkAfterClass;
import com.mpen.api.bean.HomeworkResourceUrl;
import com.mpen.api.bean.MyMedal;
import com.mpen.api.bean.OralExamination;
import com.mpen.api.bean.OralTestInfo.OralTestPaper;
import com.mpen.api.bean.Pen;
import com.mpen.api.bean.PreviewBeforeClass;
import com.mpen.api.bean.PreviewContent;
import com.mpen.api.bean.TextBookLearning;
import com.mpen.api.bean.User;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.InviteCodeTools;
import com.mpen.api.domain.DdbBookCoreDetail;
import com.mpen.api.domain.DdbLearnLogBookDetailTrace;
import com.mpen.api.domain.DdbLearnLogBookSumTrace;
import com.mpen.api.domain.DdbLearnLogBookTrace;
import com.mpen.api.domain.DdbLearnLogDayTrace;
import com.mpen.api.domain.DdbPageDetail;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbRecordUserBook;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.domain.DdbResourcePageCode;
import com.mpen.api.domain.DdbResourcePageScope;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserClassRela;
import com.mpen.api.domain.DdbUserDynamicPraise;
import com.mpen.api.domain.DdbUserDynamicRecord;
import com.mpen.api.domain.DdbUserHomework;
import com.mpen.api.domain.DdbUserHomeworkState;
import com.mpen.api.domain.DdbUserIntegralRecord;
import com.mpen.api.domain.DdbUserMedalDictionary;
import com.mpen.api.domain.DdbUserMedalRecord;
import com.mpen.api.domain.DdbUserMessage;
import com.mpen.api.domain.DdbUserPraiseRelationship;
import com.mpen.api.domain.DdbUserRelationship;
import com.mpen.api.domain.MobileApp;
import com.mpen.api.domain.OralTestDetail;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;

/**
 * 测试基础类.
 *
 * @author kai
 *
 */
public class TestBase {
    @Autowired
    private TestRestTemplate restTemplate;
    
    static {
        try {
            FileUtils.localIp = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        FileUtils.root = "root";
        FileUtils.domain = "domain";
        FileUtils.cdnDomain = "cdnDomain";
    }
    
    public SsoUser getTestSsoUser() {
        SsoUser u = new SsoUser();
        u.setId("stu1");
        u.setBindemail("test@test.com");
        u.setBindmobile("134");
        u.setCreateDate(new Date());
        u.setEmail("test@test.com");
        u.setFkRoleId(CommUtil.genRecordKey());
        u.setLoginId(CommUtil.genRecordKey());
        u.setMobile("134");
        u.setNickName("nickName");
        u.setOnlineTime("10");
        u.setPassword("xxxx");
        u.setPhoto("123.png");
        u.setTrueName("test");
        u.setUpdateDate(new Date());
        u.setWeixinidentifier(CommUtil.genRecordKey());
        return u;
    }

    public DdbPeCustom getTestDdbPeCustom(SsoUser ssoUser) {
        DdbPeCustom custom = new DdbPeCustom();
        custom.setAddress("北京");
        custom.setBrithday("20160167");
        custom.setCardNo("001");
        custom.setEmail("test@test.com");
        custom.setFkLabelId(CommUtil.genRecordKey());
        custom.setFkLabelOneId(CommUtil.genRecordKey());
        custom.setFkLabelThreeId(CommUtil.genRecordKey());
        custom.setFlagGender("");
        custom.setFkUserId(ssoUser.getId());
        custom.setLoginId(ssoUser.getLoginId());
        custom.setMobilephone("134");
        custom.setNickName("nickName");
        custom.setPhone("134");
        custom.setPost("100876");
        custom.setQq("11111111");
        custom.setRegistrationDate("20160111");
        custom.setRegNo("009");
        custom.setTrueName("testuser");
        custom.setWorkUnit("test");
        custom.setZipAddress("100768");
        return custom;
    }

    public DdbPeCustom getTestDdbPeCustom() {
        DdbPeCustom custom = new DdbPeCustom();
        custom.setLoginId("15963179705");
        custom.setId("custom1");
        custom.setFkLabelId("ff80818149ea3d200149eab3e7980012");
        return custom;
    }

    public static User getTestCustom() {
        final User user = new User();
        user.setUserName("15900527848");
        user.setPassword("3986290380150-1529655095608-e4c779a27cd390537a936a82a81fe0e3");
        return user;
    }
    
    public static User getTestUser() {
        final User user = new User();
        user.setUserName("15963179705");
        user.setPassword("3986290380150-1529655095608-0d38af59630098b33fdb536e82ea032f");
        return user;
    }

    public DdbResourceBook getTestDdbResourceBook() {
        DdbResourceBook resourceBook = new DdbResourceBook();
        resourceBook.setAuthor("author");
        resourceBook.setName("test book");
        resourceBook.setCreateDatetime(new Date());
        return resourceBook;
    }

    public static DdbPePen getTestDdbPePen() {
        DdbPePen pen = new DdbPePen();
        pen.setId("9370213704384681b4f7c3a5314cb832");
        pen.setIdentifiaction("0d033922--0e128006-00000000-802a288f");
        pen.setMacAddress("42:97:1D:D0:C3:8A");
        pen.setSerialNumber("V917060006573");
        pen.setAppVersion("1.32");
        return pen;
    }

    public DdbPePen getTestIsBindDdbPePen() {
        DdbPePen pen = new DdbPePen();
        pen.setIdentifiaction("3");
        return pen;
    }

    public DdbPePen getTestNoMachingDdbPePen() {
        DdbPePen pen = new DdbPePen();
        pen.setIdentifiaction("12165231");
        pen.setMacAddress("21:3D:4D:BE:C3:8A");
        return pen;
    }

    public UserSession getTestUserSession() {
        UserSession user = new UserSession();
        user.setSsoUser(getTestSsoUser());
        user.setPeCustom(getTestDdbPeCustom());
        user.setLoginId("13581637228");
        return user;
    }
    
    public UserSession getTestUserSessionReadLevelFour() {
        UserSession user = new UserSession();
        user.setLoginId("13756279392");
        return user;
    }
    
    public UserSession getTestUserSessionReadLevelNull() {
        UserSession user = new UserSession();
        user.setLoginId("111111@qq.com");
        return user;
    }
    
    public UserSession getTestUserSessionReadLevelEleven() {
        UserSession user = new UserSession();
        user.setLoginId("18618192001");
        return user;
    }

    public TaskRecord getTestTaskRecord() {
        TaskRecord record = new TaskRecord();
        record.id = 0;
        record.name = ActionRecord.Subtype.PlayAudio;
        record.createdRealTime = System.currentTimeMillis();
        record.duration = 100l;
        ClickRecord clickRecord = new ClickRecord();
        clickRecord.bookId = "4028ac305804c097015804c143730001";
        clickRecord.clickTime = System.currentTimeMillis();
        clickRecord.language = 0;
        clickRecord.code = new Code();
        clickRecord.code.type = Code.Type.SH;
        clickRecord.code.shCode = new ShCode(10017, 1);
        clickRecord.text = "What#1s your favourite song, Ms Smart?";
        clickRecord.score = 90;
        record.extra = Constants.GSON.toJson(clickRecord);
        return record;
    }

    public ActionRecords getTestActionRecords() {
        TaskRecord record = getTestTaskRecord();
        ActionRecord actionRecord = record.toActionRecord();
        ActionRecords ar = new ActionRecords();
        ar.setPenId("27b4231d--1da14004-00000000-9200108f");
        ar.setUploadUuid(CommUtil.genRecordKey());
        ArrayList<ActionRecord> list = new ArrayList<ActionRecord>();
        list.add(actionRecord);
        ar.setRecords(list);
        ar.setNumRecords(list.size());
        return ar;
    }

    public Pen getTestPen(String serialNumber) {
        Pen pen = new Pen();
        pen.setIdentifiaction("2a552127--1da14004-00000000-9200108f");
        pen.setSerialNumber(serialNumber);
        return pen;
    }

    public Pen getTestOnePen() {
        Pen pen = new Pen();
        pen.setIdentifiaction("27b4231d--1da14004-00000000-9200108f");
        pen.setSerialNumber("V917050000002");
        return pen;
    }

    public Pen getTestTwoPen() {
        Pen pen = new Pen();
        pen.setIdentifiaction("2a552127--1da14004-00000000-9200108f");
        pen.setSerialNumber("V917060000001");
        return pen;
    }
    
    public Pen getTestThreePen() {
        Pen pen = new Pen();
        pen.setIdentifiaction("2a552127--1da14004-00000000-9200108f");
        pen.setSerialNumber("V917050000001");
        return pen;
    }
    
    public Goods getTestGoodsTeachLink() {
        Goods goods = new Goods();
        goods.setNeedTeachLink(true);
        goods.setSystemType(MobileApp.Type.GARDENER);
        return goods;
    }
    
    public ExamDetail getTestExamDetail() {
        ExamDetail examDetail = new ExamDetail();
        examDetail.setExamId("test1");
        examDetail.setCreateTime("2018-05-29 15:15:15");
        examDetail.setDuration("40");
        examDetail.setLevel(3);
        return examDetail;
    }
   
    public DdbRecordUserBook getDdbRecordUserBook01(int id,String loginId,String recordTableName){
        DdbRecordUserBook ddbRecordUserBook=new DdbRecordUserBook();
        ddbRecordUserBook.setId(String.valueOf(id));
        ddbRecordUserBook.setLoginId(loginId);
        ddbRecordUserBook.setFkBookId("ff808081581deb4101581e74ac7d0088");
        ddbRecordUserBook.setFkActivityId("ff808081592703b70159271412955467");
        ddbRecordUserBook.setCode("10138");
        ddbRecordUserBook.setText("She's a girl.");
        ddbRecordUserBook.setClickTime(this.getDate("2018-09-11 10:12:47"));
        ddbRecordUserBook.setFunction("2");
        return ddbRecordUserBook;
    }
    public DdbRecordUserBook getDdbRecordUserBook02(int id,String loginId,String recordTableName){
        DdbRecordUserBook ddbRecordUserBook=new DdbRecordUserBook();
        ddbRecordUserBook.setId(String.valueOf(id));
        ddbRecordUserBook.setLoginId(loginId);
        ddbRecordUserBook.setFkBookId("ff808081581deb4101581e74ac7d0088");
        ddbRecordUserBook.setFkActivityId("ff8080815926f55b015926f8af630004");
        ddbRecordUserBook.setText("This is Hua Mulan.");
        ddbRecordUserBook.setCode("10012");
        ddbRecordUserBook.setClickTime(this.getDate("2018-09-11 10:10:47"));
        ddbRecordUserBook.setFunction("2");
        return ddbRecordUserBook;
    }
    public DdbRecordUserBook getDdbRecordUserBook03(int id,String loginId,String recordTableName){
        DdbRecordUserBook ddbRecordUserBook=new DdbRecordUserBook();
        ddbRecordUserBook.setId(String.valueOf(id));
        ddbRecordUserBook.setLoginId(loginId);
        ddbRecordUserBook.setFkBookId("ff808081581deb4101581e74ac7d0088");
        ddbRecordUserBook.setFkActivityId("ff8080815926f55b015926f8afce0005");
        ddbRecordUserBook.setCode("10138");
        ddbRecordUserBook.setText("This is my grandpa.");
        ddbRecordUserBook.setClickTime(this.getDate("2018-09-11 10:11:47"));
        ddbRecordUserBook.setFunction("2");
        return ddbRecordUserBook;
    }
    public DdbRecordUserBook getDdbRecordUserBook04(String id,String loginId){
        DdbRecordUserBook ddbRecordUserBook=new DdbRecordUserBook();
        ddbRecordUserBook.setId(id);
        ddbRecordUserBook.setLoginId(loginId);
        ddbRecordUserBook.setFkBookId("ff808081581deb4101581e74ac7d0088");
        ddbRecordUserBook.setFkActivityId("ff8080815926f55b015926f8afce0005");
        ddbRecordUserBook.setCode("10138");
        ddbRecordUserBook.setText("This is my grandpa.");
        ddbRecordUserBook.setClickTime(this.getDate("2018-09-11 10:11:47"));
        ddbRecordUserBook.setFunction("2");
        ddbRecordUserBook.setScore(80);
        return ddbRecordUserBook;
    }
    public DdbRecordUserBook getDdbRecordUserBook05(String id,String loginId){
        DdbRecordUserBook ddbRecordUserBook=new DdbRecordUserBook();
        ddbRecordUserBook.setId(id);
        ddbRecordUserBook.setLoginId(loginId);
        ddbRecordUserBook.setFkBookId("ff808081581deb4101581e74ac7d0088");
        ddbRecordUserBook.setFkActivityId("ff8080815926f55b015926f8afce0005");
        ddbRecordUserBook.setCode("10138");
        ddbRecordUserBook.setText("This is my grandpa.");
        ddbRecordUserBook.setClickTime(this.getDate("2018-09-11 10:11:47"));
        ddbRecordUserBook.setFunction("0");
        return ddbRecordUserBook;
    }
    public List<OralTestDetail>  getOralTestDetail(){
        List<OralTestDetail> oralTestDetails=new ArrayList<>();
        OralTestDetail oralTestDetail01=new OralTestDetail();
        oralTestDetail01.setId(581);
        oralTestDetail01.setLoginId("17710859660");
        oralTestDetail01.setPenId("04981311--07328806-00000000-905bdc8f");
        oralTestDetail01.setFkBookId("0fb29e4243bf4cb5bf523f18e72a41c4");
        oralTestDetail01.setSerialNumber(1);
        oralTestDetail01.setRecordingUrl("/oraltest/1525940350100-2.mp3");
        oralTestDetail01.setNum(1);
        oralTestDetails.add(oralTestDetail01);
        OralTestDetail oralTestDetail02=new OralTestDetail();
        oralTestDetail02.setId(587);
        oralTestDetail02.setLoginId("17710859660");
        oralTestDetail02.setPenId("04981311--07328806-00000000-905bdc8f");
        oralTestDetail02.setFkBookId("0fb29e4243bf4cb5bf523f18e72a41c4");
        oralTestDetail02.setSerialNumber(1);
        oralTestDetail02.setNum(7);
        oralTestDetail02.setRecordingUrl("/oraltest/1525940353007-2.mp3");
        oralTestDetails.add(oralTestDetail02);
        OralTestDetail oralTestDetail03=new OralTestDetail();
        oralTestDetail03.setId(588);
        oralTestDetail03.setLoginId("17710859660");
        oralTestDetail03.setPenId("04981311--07328806-00000000-905bdc8f");
        oralTestDetail03.setFkBookId("0fb29e4243bf4cb5bf523f18e72a41c4");
        oralTestDetail03.setSerialNumber(1);
        oralTestDetail03.setNum(8);
        oralTestDetail03.setRecordingUrl("/oraltest/1525940353498-2.mp3");
        oralTestDetails.add(oralTestDetail03);
        return  oralTestDetails;
    }
    public List<SubTopic> getSubTopics(){
        List<SubTopic> subTopics=new ArrayList<>();
        SubTopic subTopic01=new SubTopic();
        subTopic01.refText="sunny hurry";
        subTopic01.coreType=0;
        subTopics.add(subTopic01);
        SubTopic subTopic02=new SubTopic();
        subTopic02.coreType=1;
        subTopic02.refText="My name is Lisa. I’m at the park with my family. We’re having a picnic.  My mother is flying a kite. My father is reading a newspaper.  My little brother is singing a song. My grandma is sleeping. And I’m drawing a picture of my family! The weather is good. It’s sunny and warm.";
        subTopics.add(subTopic02);
        SubTopic subTopic03=new SubTopic();
        subTopic03.refText="It’s hot.";
        subTopic03.coreType=2;
        subTopic03.keyWords="hot";
        subTopics.add(subTopic03);
        return subTopics;
    }
    public static  List<Map<String, Double>> getScore(){
        List<Map<String, Double>> scorelists=new ArrayList<>();
        Map<String, Double> scoreMap01= new HashMap<>();
        scoreMap01.put("score", 0.31);
        scoreMap01.put("fluency",  50.00);
        scoreMap01.put("integrity",  67.00);
        scoreMap01.put("pronunciation",  30.00);
        scorelists.add(scoreMap01);
        Map<String, Double> scoreMap02= new HashMap<>();
        scoreMap02.put("score", 0.23);
        scoreMap02.put("fluency", 98.5);
        scoreMap02.put("integrity", 27.5);
        scoreMap02.put("pronunciation", 95.5);
        scorelists.add(scoreMap02);
        Map<String, Double> scoreMap03= new HashMap<>();
        scoreMap03.put("score", 0.92);
        scoreMap03.put("fluency", 88.00);
        scoreMap03.put("integrity", 100.00);
        scoreMap03.put("pronunciation", 95.00);
        scorelists.add(scoreMap03);
        return scorelists;
    }

    public OralTestDetail getOralTest(int engine) {
        OralTestDetail oralTestDetail = new OralTestDetail();
        oralTestDetail.setUploadTime(Instant.now());
        oralTestDetail.setPenId("04981311--07328806-00000000-905bdc8f");
        oralTestDetail.setLoginId("17910859660");
        oralTestDetail.setFkBookId("0fb29e4243bf4cb5bf523f18e72a41c4");
        oralTestDetail.setAnswerPenTime(Instant.ofEpochMilli(1529198608));
        oralTestDetail.setNum(1);
        oralTestDetail.setRecordingUrl("/incoming/ddb/recording/20180510/1525940350100-2.mp3");
        oralTestDetail.setIsDeal(Constants.INT_ZERO);
        oralTestDetail.setAssessmentType(engine);
        oralTestDetail.setShardNum(0);
        return oralTestDetail;
    }

    public OralTestDetail updateOralTestDetail(OralTestDetail oralTestDetail, int engine) {
        switch (engine) {
        case 0:
            oralTestDetail.setScore(0.73);
            oralTestDetail.setIsDeal(1);
            oralTestDetail.setFluency(91.078);
            oralTestDetail.setPronunciation(100);
            oralTestDetail.setIntegrity(73.304);
            oralTestDetail.setRecognizeTxt(
                    "[{\"score\":5.892,\"text\":\"sunny\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":8.1,\"text\":\"hurry\",\"type\":2}]");
            break;
        case 1:
            oralTestDetail.setScore(0.82);
            oralTestDetail.setIsDeal(1);
            oralTestDetail.setFluency(91.8);
            oralTestDetail.setPronunciation(99);
            oralTestDetail.setIntegrity(74.4);
            oralTestDetail.setRecognizeTxt(
                    "[{\"score\":5.92,\"text\":\"sunny\"},{\"score\":0.0,\"text\":\" \"},{\"score\":8.1,\"text\":\"hurry\"}]");
            break;
        default:
            break;
        }
        return oralTestDetail;

    }
    
    
    /**
     * 日期String类型转换Date类型
     */
    public Date getDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public DdbResourceBook getDdbResourceBook(int isLineRead) {
        DdbResourceBook book = new DdbResourceBook();
        book.setId(CommUtil.genRecordKey());
        book.setName("testName");
        book.setIsbn("1");
        book.setCreateDatetime(Date.from(Instant.now()));
        book.setType(FullBookInfo.Type.OTHER);
        book.setIsLineRead(isLineRead);
        return book;
    }
    
    public List<String> getFileNameList(){
        List<String> strings = new ArrayList<>();
        strings.add("测试书1");
        strings.add("测试书2");
        strings.add("测试书3");
        return strings;
    }
    
    public  DdbBookCoreDetail getDdbBookCoreDetail() {
        DdbBookCoreDetail ddbBookCoreDetail = new DdbBookCoreDetail();
        ddbBookCoreDetail.setVersion("testVersion");
        ddbBookCoreDetail.setBookInfos("测试数据123456123456123456");
        ddbBookCoreDetail.setIsActive(false);
        return ddbBookCoreDetail;
    }
    
    public DdbPageDetail getDdbPageDetail() {
        DdbPageDetail ddbPageDetail = new DdbPageDetail();
        ddbPageDetail.setBookId("testBook123456");
        ddbPageDetail.setPageInfos("测试数据123456");
        ddbPageDetail.setVersion("testVersion");
        ddbPageDetail.setIsActive(false);
        return ddbPageDetail;
    }
    
    /**
     * 登录认证
     */
    public String getLoginToken(String userName,String password) {
        final User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setType("mobile");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        final HttpEntity<User> request = new HttpEntity<User>(user, headers);
        final ResponseEntity<Object> entity = restTemplate.exchange("/v1/user/login", HttpMethod.POST, request,
                Object.class);
        final JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(entity.getBody()));
        final JSONObject object = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("data")));
        final JSONObject parseObject = JSONObject.parseObject(JSON.toJSONString(object.get("token")));
        final String type = (String) parseObject.get("token_type");
        final String token = (String) parseObject.get("access_token");
        final String userId = (String) parseObject.get("userId");
        return type + " " + token + "," + userId;
    }
    public HomeworkAfterClass getHomeworkAfterClass() {
        final HomeworkAfterClass homeworkAfterClass = new HomeworkAfterClass();
        homeworkAfterClass.setAction(Constants.SAVEN_HOMEWORK);
        homeworkAfterClass.setClassName("五年级一班");
        homeworkAfterClass.setEndDate("2018-09-28 09:18:11");
        homeworkAfterClass.setGrade("一班");
        homeworkAfterClass.setRemark("一定要完成");
        final ClassLearnContent content = new ClassLearnContent();
        homeworkAfterClass.setContent(content);
        content.setTitle("作业标题");
        final ArrayList<OralExamination> oralExaminations = new ArrayList<>();
        content.setOralExaminations(oralExaminations);
        final OralExamination oralExamination = new OralExamination();
        oralExamination.setBookId("2e20683c44c245e89572e4668e46b47a");
        oralExamination.setName("一起一下听说考试");
        final List<OralTestPaper> examPaper = new ArrayList<>();
        oralExamination.setExamPaper(examPaper);
        final OralTestPaper oralTestPaper = new OralTestPaper();
        oralTestPaper.name = "一起一下听说考试模拟训练";
        oralTestPaper.num = 1;
        examPaper.add(oralTestPaper);
        oralExaminations.add(oralExamination);
        final ArrayList<TextBookLearning> textBookLeanings = new ArrayList<>();
        content.setTextBookLearnings(textBookLeanings);
        final TextBookLearning textBookLeaning = new TextBookLearning();
        textBookLeaning.setBookId("ff808081567761c201569201cfde06ad");
        textBookLeaning.setBookName("英语（新标准）一年级起点三年级下册学生用书（修订版）");
        textBookLeaning.setModel("Module 1");
        textBookLeaning.setName("She's very nice.");
        textBookLeaning.setUnit("1");
        ArrayList<Activity> activities = new ArrayList<>();
        textBookLeaning.setActivities(activities);
        textBookLeaning.setType("0");
        Activity activity = new Activity();
        activity.id = "ff808081592703b7015927053acf0004";
        activity.sort = "Activity 1";
        activity.name = "Listen, point and say.";
        activities.add(activity);
        textBookLeanings.add(textBookLeaning);
        return homeworkAfterClass;
    }

    public PreviewBeforeClass getPreviewBeforeClass() {
        final PreviewBeforeClass previewBeforeClass = new PreviewBeforeClass();
        previewBeforeClass.setEndDate("2018-06-22 09:18:11");
        previewBeforeClass.setClassName("五年级一班");
        previewBeforeClass.setAction("savePreview");
        PreviewContent previewContent = new PreviewContent();
        previewBeforeClass.setPreviewContent(previewContent);
        previewContent.setName("课前导学名称");
        previewContent.setTask("课前导学任务");
        List<HomeworkResourceUrl> resourceUrls = new ArrayList<>();
        HomeworkResourceUrl resourceUrl = new HomeworkResourceUrl();
        resourceUrl.setType("mp3");
        resourceUrl.setUrl("课前导学音频url");
        previewContent.setUrls(resourceUrls);
        final ArrayList<String> submits = new ArrayList<>();
        previewContent.setSubmits(submits);
        submits.add("图片");
        submits.add("音频");
        submits.add("音频");
        return previewBeforeClass;
    }
    public List<DdbUserClass> getDdbUserClass() {
        final List<DdbUserClass> ddbUserClasses = new ArrayList<>();
        final DdbUserClass ddbUserClass = new DdbUserClass();
        ddbUserClass.setId(CommUtil.genRecordKey());
        ddbUserClass.setClassName("一年级一班");
        ddbUserClass.setEnglishTeacher("englishTeacher");
        ddbUserClass.setInvitationCode(InviteCodeTools.getInviteCode());
        ddbUserClass.setSchool("幸福小学");
        ddbUserClass.setFkLoginId("15003232810");
        ddbUserClass.setCreateTime(new Date());
        ddbUserClass.setUpdateTime(new Date());
        final DdbUserClass ddbUserClass01 = new DdbUserClass();
        ddbUserClass01.setId(CommUtil.genRecordKey());
        ddbUserClass01.setClassName("一年级二班");
        ddbUserClass01.setType(1);
        ddbUserClass01.setEnglishTeacher("student");
        ddbUserClass01.setInvitationCode(InviteCodeTools.getInviteCode());
        ddbUserClass01.setSchool("幸福小学");
        ddbUserClass01.setFkLoginId("15113232810");
        ddbUserClass01.setCreateTime(new Date());
        ddbUserClass01.setUpdateTime(new Date());
        ddbUserClasses.add(ddbUserClass);
        ddbUserClasses.add(ddbUserClass01);
        return ddbUserClasses;
    }
    public DdbUserClassRela getUserClassRela01(String classId) {
        final DdbUserClassRela ddbUserClass = new DdbUserClassRela();
        ddbUserClass.setId(CommUtil.genRecordKey());
        ddbUserClass.setFkClassId(classId);
        ddbUserClass.setFkLoginId("18931333230");
        ddbUserClass.setTrueName("test");
        ddbUserClass.setCreateTime(new Date());
        ddbUserClass.setUpdateTime(new Date());
        return ddbUserClass;
    }

    public DdbUserHomeworkState getDdbUserHomeworkState() {
        DdbUserHomeworkState homeworkState = new DdbUserHomeworkState();
        homeworkState.setId(CommUtil.genRecordKey());
        homeworkState.setFkClassId(CommUtil.genRecordKey());
        homeworkState.setFkLoginId("test01");
        homeworkState.setFkHomeworkId(CommUtil.genRecordKey());
        homeworkState.setIsCommit("0");
        homeworkState.setIsMarking("0");
        homeworkState.setUploadTime(new Date());
        homeworkState.setResourceUrl("testurl");
        homeworkState.setCreateTime(new Date());
        homeworkState.setUpdateTime(new Date());
        return homeworkState;
    }
    public List<DdbUserHomework> getDdbUserHomework() {
        final List<DdbUserHomework> ddbUserHomeworks = new ArrayList<>();
        final DdbUserHomework ddbUserHomework01 = new DdbUserHomework();
        ddbUserHomework01.setId(CommUtil.genRecordKey());
        ddbUserHomework01.setCreateDate(new Date());
        ddbUserHomework01.setType(Constants.PREVIEW);
        ddbUserHomework01.setFkLoginId("15113232810");
        ddbUserHomework01.setFkClassId(CommUtil.genRecordKey());
        ddbUserHomework01.setEndDate(new Date());
        ddbUserHomework01.setContent(
                "{\"name\":\"课前导学名称\",\"task\":\"课前导学任务\",\"ImgUrls\":\"课前导学图片urls\",\"audioUrls\":\"课前导学音频urls\",\"submits\":[\"图片\",\"视频\",\"音频\"]}");
        final DdbUserHomework ddbUserHomework02 = new DdbUserHomework();
        ddbUserHomework02.setId(CommUtil.genRecordKey());
        ddbUserHomework02.setCreateDate(new Date());
        ddbUserHomework02.setType(Constants.HOMEWORK_AFTER);
        ddbUserHomework02.setFkLoginId("15113232810");
        ddbUserHomework02.setFkClassId(CommUtil.genRecordKey());
        ddbUserHomework02.setEndDate(new Date());
        ddbUserHomework02.setContent(
                "{\"oralExaminations\":[{\"bookId\":\"2e20683c44c245e89572e4668e46b47a\",\"examPaper\":[{\"name\":\"一起一下听说考试模拟训练\",\"num\":1,\"score\":0}],\"name\":\"一起一下听说考试\"}],\"textBookLeanings\":[{\"activities\":[{\"id\":\"ff808081592703b7015927053acf0004\",\"name\":\"Listen, point and say.\",\"sort\":\"Activity 1\",\"type\":\"课本学习\"},{\"id\":\"ff808081592703b7015927053acf0004\",\"name\":\"Listen, point and say.\",\"sort\":\"Activity 1\",\"type\":\"跟读对比-Listen and say\"},{\"id\":\"ff808081592703b7015927053acf0004\",\"name\":\"Listen, point and say.\",\"sort\":\"Activity 1\",\"type\":\"口语评测-Let's say\"}],\"bookId\":\"ff808081567761c201569201cfde06ad\",\"bookName\":\"\",\"model\":\"Module 1\",\"name\":\"She's very nice.\",\"unit\":\"1\"}],\"title\":\"布置作业的标题\"}");
        ddbUserHomeworks.add(ddbUserHomework01);
        ddbUserHomeworks.add(ddbUserHomework02);
        return ddbUserHomeworks;
    }
    public HomeWorks getHomeWorks() {
        final HomeWorks homeWorks = new HomeWorks();
        homeWorks.setEndDate(getDay(new Date(), +1));
        homeWorks.setStartDate(getDay(new Date(), -1));
        homeWorks.setPageNo(1);
        homeWorks.setPageSize(10);
        return homeWorks;
    }
    public Date getDay(Date date, int i) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }
    public DdbUserHomeworkState getStudent01(DdbUserHomeworkState homeworkState) {
        homeworkState.setRemark("很好");
        homeworkState.setRewardNum("5");
        homeworkState.setAudioRemarkUrls("mp3");
        homeworkState.setUpdateTime(new Date());
        return homeworkState;
    }

    public DdbUserClassRela getUserClassRela() {
        final DdbUserClassRela ddbUserClass = new DdbUserClassRela();
        ddbUserClass.setId(CommUtil.genRecordKey());
        ddbUserClass.setFkClassId(CommUtil.genRecordKey());
        ddbUserClass.setFkLoginId("1111111111");
        ddbUserClass.setTrueName("test");
        ddbUserClass.setCreateTime(new Date());
        ddbUserClass.setUpdateTime(new Date());
        return ddbUserClass;
    }
    
    public DdbResourcePageCode getDdbResourcePageCode() {
        DdbResourcePageCode ddbResourcePageCode = new DdbResourcePageCode();
        ddbResourcePageCode.setId(CommUtil.genRecordKey());
        ddbResourcePageCode.setName("测试123");
        ddbResourcePageCode.setPageNum(1);
        ddbResourcePageCode.setFkBookId(CommUtil.genRecordKey());
        ddbResourcePageCode.setHeight(0);
        ddbResourcePageCode.setWidth(0);
        return ddbResourcePageCode;
    }
    
    public DdbResourcePageScope getDdbResourcePageScope() {
        DdbResourcePageScope dPageScope = new DdbResourcePageScope();
        dPageScope.setId(CommUtil.genRecordKey());
        return dPageScope;
    }
    /**
     * Controller层post请求测试基础方法
     * 
     * @param userName
     * @param password
     * @param t
     */
    public <T> void postControllerTest(String userName, String password, T t, String url) {
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        String loginToken = getLoginToken(userName, password);
        headers.add("Authorization", loginToken.split(",")[0]);
        // 通过REST URL 发送请求
        HttpEntity<T> request = new HttpEntity<T>(t, headers);
        ResponseEntity<Object> entity = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
        // 校验返回状态码和结果
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    public List<DdbLearnLogBookDetailTrace> getDdbLearnLogBookDetailTraces() {
        List<DdbLearnLogBookDetailTrace> ddbLearnLogBookDetailTraces = new ArrayList<>();
        DdbLearnLogBookDetailTrace ddbLearnLogBookDetailTrace01 = new DdbLearnLogBookDetailTrace();
        ddbLearnLogBookDetailTrace01.setId("2222");
        ddbLearnLogBookDetailTrace01.setFkLoginId(CommUtil.genRecordKey());
        ddbLearnLogBookDetailTrace01.setTextMd5("d41d8cd98f00b2e9800998ecf8427e");
        ddbLearnLogBookDetailTrace01.setFkBookId("ff80808156ca3d900156cb19feff004c");
        ddbLearnLogBookDetailTrace01.setFkActivityId("ff8080815c770728015c77382f5203fd");
        ddbLearnLogBookDetailTrace01.setType("0");
        ddbLearnLogBookDetailTrace01.setNumber(15);
        ddbLearnLogBookDetailTrace01.setLatestDate(new Date());
        DdbLearnLogBookDetailTrace ddbLearnLogBookDetailTrace02 = new DdbLearnLogBookDetailTrace();
        ddbLearnLogBookDetailTrace02.setId("3333");
        ddbLearnLogBookDetailTrace02.setFkLoginId(CommUtil.genRecordKey());
        ddbLearnLogBookDetailTrace02.setTextMd5("d41d8cd98f00b204e980cf8427e");
        ddbLearnLogBookDetailTrace02.setFkBookId("ff80808156ca3d900156cb19feff004c");
        ddbLearnLogBookDetailTrace02.setFkActivityId("ff8080815c770728015c77382f5203fd");
        ddbLearnLogBookDetailTrace02.setType("2");
        ddbLearnLogBookDetailTrace02.setText("It's green.");
        ddbLearnLogBookDetailTrace02.setScore(90f);
        ddbLearnLogBookDetailTrace02.setLatestDate(new Date());
        ddbLearnLogBookDetailTraces.add(ddbLearnLogBookDetailTrace01);
        ddbLearnLogBookDetailTraces.add(ddbLearnLogBookDetailTrace02);
        return ddbLearnLogBookDetailTraces;
    }

    public DdbLearnLogBookSumTrace getDdbLearnLogBookSumTrace() {
        DdbLearnLogBookSumTrace ddbLearnLogBookSumTrace = new DdbLearnLogBookSumTrace();
        ddbLearnLogBookSumTrace.setId("test");
        ddbLearnLogBookSumTrace.setFkLoginId("test01");
        ddbLearnLogBookSumTrace.setFkBookId("6be526350b5646e090dce0ec256cb5");
        ddbLearnLogBookSumTrace.setSumTime(22);
        ddbLearnLogBookSumTrace.setSpokenTestTime(11);
        Date curDate=new Date();
        ddbLearnLogBookSumTrace.setLatestDate(curDate);
        ddbLearnLogBookSumTrace.setCreateDate(curDate);
        ddbLearnLogBookSumTrace.setUpdateDate(curDate);
        return ddbLearnLogBookSumTrace;
    }

    public DdbLearnLogBookTrace getDdbLearnLogBookTrace() {
        DdbLearnLogBookTrace ddbLearnLogBookTrace = new DdbLearnLogBookTrace();
        ddbLearnLogBookTrace.setId("test");
        ddbLearnLogBookTrace.setFkLoginId("test01");
        ddbLearnLogBookTrace.setFkBookId("6be526350b5646e090dce0ec256cb5");
        ddbLearnLogBookTrace.setStudyDate(new Date());
        ddbLearnLogBookTrace.setBookStudyTime(20);
        ddbLearnLogBookTrace.setSpokenTestTime(30);
        ddbLearnLogBookTrace.setLearnPage("P2,P3");
        ddbLearnLogBookTrace.setSpeakPage("P2");
        Date curDate=new Date();
        ddbLearnLogBookTrace.setCreateDate(curDate);
        ddbLearnLogBookTrace.setUpdateDate(curDate);
        return ddbLearnLogBookTrace;
    }
    
    public DdbLearnLogDayTrace getDdbLearnLogDayTrace() {
        DdbLearnLogDayTrace ddbLearnLogDayTrace = new DdbLearnLogDayTrace();
        ddbLearnLogDayTrace.setId("test");
        ddbLearnLogDayTrace.setFkLoginId("test01");
        ddbLearnLogDayTrace.setStudyDate(this.getDate("2018-10-21 18:00:41"));
        ddbLearnLogDayTrace.setCountTime(60.00f);
        ddbLearnLogDayTrace.setBookStudyTime(50f);
        ddbLearnLogDayTrace.setExercisesTime(40f);
        ddbLearnLogDayTrace.setSpokenTestTime(40f);
        ddbLearnLogDayTrace.setReadTime(50f);
        ddbLearnLogDayTrace.setOtherTime(0f);
        Date curDate=new Date();
        ddbLearnLogDayTrace.setCreateDate(curDate);
        ddbLearnLogDayTrace.setUpdateDate(curDate);
        return ddbLearnLogDayTrace;
    }
    /**
     * Controller层get请求基础方法
     * 
     * @param userName
     * @param password
     * @param t
     * @param url
     */
    public <T> void getControllerTest(String userName, String password,  String url) {
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        String loginToken = getLoginToken(userName, password);
        headers.add("Authorization", loginToken.split(",")[0]); // 通过REST URL  发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
        // 校验返回状态码和结果
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
    
    public List<DdbLearnLogDayTrace> listLogDayTraces() {
        final List<DdbLearnLogDayTrace> logDayTraces = new ArrayList<>();
        final Date currentDate = new Date();
        for (int i = 0; i < 3; i++) {
            float time = 1.0f * i;
            final DdbLearnLogDayTrace logDayTrace = new DdbLearnLogDayTrace();
            logDayTrace.setId(CommUtil.genRecordKey());
            logDayTrace.setFkLoginId("13906030919");
            logDayTrace.setStudyDate(currentDate);
            logDayTrace.setCountTime(time);
            logDayTrace.setBookStudyTime(time);
            logDayTrace.setSpokenTestTime(time);
            logDayTrace.setExercisesTime(time);
            logDayTrace.setReadTime(time);
            logDayTrace.setOtherTime(time);
            logDayTrace.setCreateDate(currentDate);
            logDayTrace.setUpdateDate(currentDate);
            logDayTraces.add(logDayTrace);
        }
        return logDayTraces;
    }
    
    public List<DdbLearnLogBookDetailTrace> listLogBookDetailTraces() {
        final List<DdbLearnLogBookDetailTrace> logBookDetailTraces = new ArrayList<>();
        final Date currentDate = new Date();
        for (int i = 0; i < 3; i++) {
            final DdbLearnLogBookDetailTrace bookDetailTrace = new DdbLearnLogBookDetailTrace();
            bookDetailTrace.setCreateDate(currentDate);
            bookDetailTrace.setFkActivityId("ff8080815926f55b015926ff23e15d8d");
            bookDetailTrace.setFkBookId("ff808081567761c201569208005d06b9");
            bookDetailTrace.setFkLoginId("13906030919");
            bookDetailTrace.setId(CommUtil.genRecordKey());
            bookDetailTrace.setLatestDate(currentDate);
            bookDetailTrace.setNumber(i + 1);
            bookDetailTrace.setScore(1.0f * i);
            bookDetailTrace.setText(null);
            bookDetailTrace.setTextMd5("d41d8cd98f00b204e9800998ecf8427e");
            bookDetailTrace.setTime(0f);
            bookDetailTrace.setType(i % 2 == 0 ? "0" :"2");
            bookDetailTrace.setUpdateDate(currentDate);
            bookDetailTrace.setUserRecognizeBytes(null);
            byte[] bytes = {54, 34, 56, 45};
            bookDetailTrace.setUserRecognizeBytesStr(new String(bytes));
            logBookDetailTraces.add(bookDetailTrace);
        }
        return logBookDetailTraces;
    }
    
    public List<DdbLearnLogBookSumTrace> listLogBookSumTraces() {
        final List<DdbLearnLogBookSumTrace> logBookSumTraces = new ArrayList<>();
        final Date currentDate = new Date();
        for (int i = 0; i < 3; i++) {
            final DdbLearnLogBookSumTrace bookSumTrace = new DdbLearnLogBookSumTrace();
            bookSumTrace.setCreateDate(currentDate);
            bookSumTrace.setFkBookId("ff8080815847b3010158489abb5600ae");
            bookSumTrace.setFkLoginId("13906030919");
            bookSumTrace.setId(CommUtil.genRecordKey());
            bookSumTrace.setLatestDate(currentDate);
            bookSumTrace.setSpokenTestTime(1.0f * i);
            bookSumTrace.setSumTime(1.0f * i);
            bookSumTrace.setUpdateDate(currentDate);
            logBookSumTraces.add(bookSumTrace);
        }
        return logBookSumTraces;
    }
    
    public List<DdbLearnLogBookTrace> listLogBookTraces() {
        final List<DdbLearnLogBookTrace> logBookTraces = new ArrayList<>();
        final Date currentDate = new Date();
        for (int i = 0; i < 3; i++) {
            final DdbLearnLogBookTrace logBookTrace = new DdbLearnLogBookTrace();
            logBookTrace.setBookStudyTime(1.0f * i);
            logBookTrace.setCreateDate(currentDate);
            logBookTrace.setFkBookId("ff808081581deb4101581e74ac7d0088");
            logBookTrace.setId(CommUtil.genRecordKey());
            logBookTrace.setLearnPage("P14, P15, P16, P17, P18, P19");
            logBookTrace.setSpeakPage("");
            logBookTrace.setSpokenTestTime(0);
            logBookTrace.setStudyDate(currentDate);
            logBookTrace.setUpdateDate(currentDate);
            logBookTrace.setFkLoginId("13906030919");
            logBookTraces.add(logBookTrace);
        }
        return logBookTraces;
    }
    
    public ClassInfo getClassInfo() {
        final ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("一年级一班");
        classInfo.setAction("saveClassInfo");
        return classInfo;
    }
    public DdbUserIntegralRecord getUserIntegralRecord(String loginId, int integral, String integralType) {
        final DdbUserIntegralRecord ddbUserIntegralRecord = new DdbUserIntegralRecord();
        final Date curDate = new Date();
        ddbUserIntegralRecord.setCreateTime(curDate);
        ddbUserIntegralRecord.setUpdateTime(curDate);
        ddbUserIntegralRecord.setId(CommUtil.genRecordKey());
        ddbUserIntegralRecord.setFkLoginId(loginId);
        ddbUserIntegralRecord.setIntegral(integral);
        ddbUserIntegralRecord.setIntegralType(integralType);
        return ddbUserIntegralRecord;
    }
    public DdbUserPraiseRelationship getDdbuserPraiseRelationship(){
        final DdbUserPraiseRelationship ddbUserPraiseRelationship = new DdbUserPraiseRelationship();
        ddbUserPraiseRelationship.setByPraiseLoginId("test01");
        ddbUserPraiseRelationship.setCreateTime(CommUtil.parseTimeFormattoDate("2018-10-21 18:00:41"));
        ddbUserPraiseRelationship.setId(CommUtil.genRecordKey());
        ddbUserPraiseRelationship.setPraiseLoginId("test02");
        return ddbUserPraiseRelationship;
    }

    public DdbUserMedalDictionary getddbUserMedalDictionary() {
        final DdbUserMedalDictionary ddbUserMedalDictionary = new DdbUserMedalDictionary();
        ddbUserMedalDictionary.setId(CommUtil.genRecordKey());
        ddbUserMedalDictionary.setMedalType("读书");
        ddbUserMedalDictionary.setMedalName("写字");
        ddbUserMedalDictionary.setMedalDynamic("打游戏");
        ddbUserMedalDictionary.setCreateTime(CommUtil.parseTimeToString(new Date()));
        ddbUserMedalDictionary.setUpdateTime(CommUtil.parseTimeToString(new Date()));
        ddbUserMedalDictionary.setSlogan("敲代码");
        ddbUserMedalDictionary.setMedalRule("完美");
        return ddbUserMedalDictionary;
    }

    public DdbUserDynamicRecord getDynamicRecord() {
        final DdbUserDynamicRecord dynamicRecord = new DdbUserDynamicRecord();
        dynamicRecord.setId(CommUtil.genRecordKey());
        dynamicRecord.setCreateTime(new Date());
        dynamicRecord.setUpdateTime(new Date());
        dynamicRecord.setFkLoginId("13553006478");
        dynamicRecord.setDynamicContent("动态内容测试");
        return dynamicRecord;
    }

    public DdbUserMedalRecord getddbUserMedalRecord() {
        final DdbUserMedalRecord ddbUserMedalRecord = new DdbUserMedalRecord();
        ddbUserMedalRecord.setId(CommUtil.genRecordKey());
        ddbUserMedalRecord.setFkLoginId("15151514563");
        ddbUserMedalRecord.setFkMedalDicId("4");
        ddbUserMedalRecord.setCreateTime(CommUtil.parseTimeToString(new Date()));
        ddbUserMedalRecord.setMedalNum(6);
        ddbUserMedalRecord.setMedalState(1);
        ddbUserMedalRecord.setWearState(0);
        return ddbUserMedalRecord;
    }

    public ClassAssignments getClassAssignments() {
        final ClassAssignments classAssignments = new ClassAssignments();
        List<String> loginds = new ArrayList<>();
        loginds.add("15003232810");
        loginds.add("15727302212");
        classAssignments.setAction("rushJob");
        classAssignments.setLoginIds(loginds);
        return classAssignments;
    }

    public MyMedal myMedalWear() {
        final MyMedal myMedal = new MyMedal();
        myMedal.setMedalId("2");
        myMedal.setAction("medalWear");
        return myMedal;
    }
    public MyMedal myMedalOff(){
        final MyMedal myMedal = new MyMedal();
        myMedal.setMedalId("2");
        myMedal.setAction("medalOff");
        return myMedal;
    }
    
    public List<DdbUserRelationship> getDdbUserRelationships() {
        List<DdbUserRelationship> ddbUserRelationships = new ArrayList<>();

        // 用户user的五个好友
        for (int i = 0; i < 5; i++) {
            final DdbUserRelationship ddbUserRelationship = new DdbUserRelationship();
            ddbUserRelationship.setId(CommUtil.genRecordKey());
            ddbUserRelationship.setUserLoginId("user");
            ddbUserRelationship.setFriendLoginId("friend" + i);
            ddbUserRelationship.setAliasUser("userAlias");
            ddbUserRelationship.setAliasFriend("friendAlias" + i);
            ddbUserRelationship.setReqMsg("my name is user");
            final Date curDate=new Date();
            ddbUserRelationship.setCreateTime(curDate);
            ddbUserRelationship.setUpdateTime(curDate);
            ddbUserRelationships.add(ddbUserRelationship);
        }
        return ddbUserRelationships;
    }
    public List<DdbUserMessage> getPageDdbUserMessage() {
        final List<DdbUserMessage> listDdbUserMessage = new ArrayList<DdbUserMessage>();
        // 循环生成10条消息
        for (int i = 0; i < 10; i++) {
            final DdbUserMessage ddbUserMessage01 = new DdbUserMessage();
            // ddbUserMessage01.setId("a"+i);
            ddbUserMessage01.setId(CommUtil.genRecordKey());
            ddbUserMessage01.setLoginId("13717521920");
            ddbUserMessage01.setType(Constants.FABULOUS_ME);
            ddbUserMessage01.setContent("老师已经发布了4.10课前预习作业" + i);
            ddbUserMessage01.setIsRead(0);
            ddbUserMessage01.setIsDel(0);
            ddbUserMessage01.setTypeDetail("homeWork");
            Date curDate=new Date();
            ddbUserMessage01.setCreateTime(curDate);
            ddbUserMessage01.setUpdateTime(curDate);
            listDdbUserMessage.add(ddbUserMessage01);
        }

        return listDdbUserMessage;
    }
    public List<DdbUserMessage> getDdbUserMessage() {
        final DdbUserMessage ddbUserMessage01 = new DdbUserMessage();
        final DdbUserMessage ddbUserMessage02 = new DdbUserMessage();
        final List<DdbUserMessage> listDdbUserMessage = new ArrayList<DdbUserMessage>();

        ddbUserMessage01.setId(CommUtil.genRecordKey());
        ddbUserMessage01.setLoginId("13717521920");
        ddbUserMessage01.setType(Constants.FABULOUS_ME);
        ddbUserMessage01.setContent("老师已经发布了4.10课前预习作业");
        ddbUserMessage01.setIsRead(0);
        ddbUserMessage01.setIsDel(0);
        ddbUserMessage01.setCreateTime(new Date());
        ddbUserMessage01.setUpdateTime(new Date());

        ddbUserMessage02.setId(CommUtil.genRecordKey());
        ddbUserMessage02.setLoginId("13717521920");
        ddbUserMessage02.setType(Constants.FABULOUS_ME);
        ddbUserMessage02.setContent("赞了我，口语测评获得200分");
        ddbUserMessage02.setIsRead(0);
        ddbUserMessage02.setIsDel(0);
        ddbUserMessage02.setCreateTime(new Date());
        ddbUserMessage02.setUpdateTime(new Date());

        listDdbUserMessage.add(ddbUserMessage01);
        listDdbUserMessage.add(ddbUserMessage02);

        return listDdbUserMessage;
    }

    public DdbUserDynamicPraise getDdbUserDynamicPraise() {
        final DdbUserDynamicPraise ddbUserDynamicPraise = new DdbUserDynamicPraise();
        ddbUserDynamicPraise.setFkDynamicId("test");
        final Date curDate = new Date();
        ddbUserDynamicPraise.setCreateTimeInMs(curDate.getTime());
        ddbUserDynamicPraise.setFkLoginId("18931334240");
        ddbUserDynamicPraise.setId("test");
        ddbUserDynamicPraise.setUpdateTimeInMs(curDate.getTime());
        return ddbUserDynamicPraise;

    }

}
