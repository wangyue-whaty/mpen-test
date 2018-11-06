/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.mp.shared.common.BookInfo;
import com.mp.shared.common.FullBookInfo;
import com.mp.shared.common.LearnWordStructureInfo;
import com.mp.shared.common.NetworkResult;
import com.mp.shared.common.Page;
import com.mp.shared.common.PageInfo;
import com.mp.shared.service.MpResourceDecoder.UrlGenerator;
import com.mpen.api.bean.Hot;
import com.mpen.api.bean.Program;
import com.mpen.api.domain.DdbResourceCode;
import com.mpen.api.util.FileUtils;

public class Constants {
    public static final String WECHAT_PUB_ID = "wxf390010e2fa94deb"; // 微信公众号appId
    public static final String WECHAT_PUB_SECRET = "6ff15185161441a5158aaa09b3392a94"; // 微信公众号secret
    public static final String GET_WECHAT_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    public static final String GET_WECHAT_JSSDK_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public static volatile long WECHAT_TOKEN_OVERTIME; //微信公众号token失效时间                                                                                                            
    public static volatile String WECHAT_TOKEN; // 微信公众号token
    public static volatile String WECHAT_TICKET; // 微信公众号ticket
    public static List<Program> programs;
    public static volatile String API_TOKEN;
    public static volatile long API_TOKEN_OVERTIME;
    public static Map<String, List<List<Hot>>> hotAreas;
    public static volatile Page<FullBookInfo> fullBookInfos;
    public static volatile Page<BookInfo> bookInfos;
    public static volatile Page<LearnWordStructureInfo> learnWordStructureInfos;
    public static volatile Page<PageInfo> pageInfos;
    public static volatile Map<String, Page<PageInfo>> pageInfoList = new HashMap<String, Page<PageInfo>>(); // 存储每本书的pageInfo信息
    public static Boolean bookInfoIsLock = false;
    public static Boolean pageInfoIsLock = false;
    public static Boolean globalPageInfoIsLock = false;
    public static Boolean createCodeIsLock = false;
    public static Map<String, ArrayList<DdbResourceCode>> codeMap;
    public static final Gson GSON = new Gson();
    public static final ExecutorService CACHE_THREAD_POOL = Executors.newCachedThreadPool();
    // TODO 最终通过验证用户group来决定访问权限
    public static final String ACCESS_CONTROL_KEY = "mpen2017..";
    public static final String CDN_SECRET = "mpen201705";
    public static final String BOOK_KEY = "J+K/+>*N$/%\"$66F";
    public static final String BOOK_KEY_ENCRYPT = "SsJzSOsdtbcu5NZRcAnk0U0PrugRxqMDsRqWINRIuYw=";
    public static final String PUBLISH_KEY = "037ccd251d1b48f1b138270c3c79a092";
    public static final String UPDATE_CACHE_SECURE_KEY = "UPDATECACHESECUREKEYFORMANAGER";
    public static final String USERCENTER_BASE_URL = "https://www.mpen.com.cn";
    public static final String USERCENTER_LOGIN_ADDS = USERCENTER_BASE_URL + "/uc/user/login.do";
    public static final String USERCENTER_GET_USER = USERCENTER_BASE_URL + "/uc/user/getUser.do";
    public static final String USERCENTER_GET_USER_WITH_SESSIONID= USERCENTER_BASE_URL + "/uc/user/getUserWithSessionId.do";
    public static final String USERCENTER_UPDATE_USER = USERCENTER_BASE_URL + "/uc/user/updateUser.do";
    public static final String NEW_USERCENTER_LOGIN_ADDS = USERCENTER_BASE_URL + "/uc/authorize";
    public static final String NEW_USERCENTER_USER = USERCENTER_BASE_URL + "/uc/v2/user/";
    public static final String NEW_USERCENTER_LIST_PHOTO = USERCENTER_BASE_URL + "/uc/v2/user/class/photos";
    public static final String SCHOOL_NO = "ddb";
    public static final String CONTENT_TYPE = "text/html;charset=UTF-8";
    public static final String GBK_ENCODING = "GBK";
    public static final String UTF8_ENCODING = "UTF-8";
    public static final String TRUE = "true";
    public static final String SO_PATH = "config/libmpdecoder.so";
    public static final String SSL_SO_PATH = "config/libcrypto.so.1.1";
    public static final String AIENGINE_SO_PATH = "config/libaiengine.so";
    public static final String AIENGINE_PRO_SO = "config/aiengine.provision";
    public static final Integer DEFAULT_PAGENO = 1;
    public static final String[] CODE_FILES = { "ff80808152f8de080152f8e542cf0002.txt",
        "ff80808156ca3d900156caac68a50007.txt", "ff80808156ca3d900156cac8350c0009.txt",
        "ff80808156ca3d900156cacd16e8000d.txt", "ff80808156ca3d900156cace3056000f.txt",
        "ff80808156ca3d900156cacee3fc0011.txt", "ff80808156ca3d900156cacf93420013.txt",
        "ff80808156ca3d900156cb19feff004c.txt", "ff808081533ba5a801533bbd358f0003.txt",
        "ff808081533ba5a801533bbddf220006.txt", "ff808081533ba5a801533bbe58360008.txt",
        "ff808081581deb4101581e74ac7d0088.txt", "ff8080815847b3010158489abb5600ae.txt",
        "ff808081567761c2015691ef1d2e06a6.txt", "ff808081567761c2015691fd171d06a9.txt",
        "ff808081567761c2015691ff8b7e06ab.txt", "ff808081567761c20156920b17fb06bd.txt",
        "ff808081567761c20156920d03f706c0.txt", "ff808081567761c20156920f6c5006c4.txt",
        "ff808081567761c201569201cfde06ad.txt", "ff808081567761c201569206d9d206b7.txt",
        "ff808081567761c201569212caef06ca.txt", "ff808081567761c201569213fdca06cc.txt",
        "ff808081567761c2015692106cfa06c6.txt", "ff808081567761c2015692119de006c8.txt",
        "ff808081567761c2015692154c5506ce.txt", "ff808081567761c201569203814e06af.txt",
        "ff808081567761c201569208005d06b9.txt", "ff808081567761c201569205449706b5.txt",
        "ff808081567761c201569209573106bb.txt" };
    public static final String LIMIT_APP_VERSION = "3.55";

    public static final DateTimeFormatter DATA_FORMART = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String RECORDSKEY = "records";
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final Integer MAX_PAGESIZE = 10000;

    public static final int READING_LIMIT = 1000;

    public static final Integer INT_ZERO = 0;
    public static final Integer INT_ONE = 1;
    public static final Integer INT_SEVEN = 7;
    public static final Integer INT_TEN = 10;
    public static final Integer INT_TWENTY = 20;
    public static final Integer INT_FIFTY = 50;
    public static final Integer INT_HUNDRED = 100;
    public static final Float FLOAT_ZERO = 0.0f;
    public static final Float FLOAT_ONE = 1.0f;
    public static final Float FLOAT_SIXTY = 60.0f;
    public static final String LAST_READING_PREFIX = " 点读";
    public static final String LAST_READING_SUFFIX = "分钟";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String P_STRING = "P";

    // 解码相关
    public static final String MPCODE = "MP";
    public static final String SHCODE = "SH";
    public static final String XORPATH = "/test/dp.xor";

    public static final String STUDY_STRING = "课本学习";
    public static final String READ_STRING = "课外阅读";
    public static final String TEST_STRING = "课后练习";
    public static final String SPOKEN_STRING = "口语评测";
    public static final String MODULE = "Module";
    public static final String UNIT = "Unit";
    public static final String ACTIVITY = "Activity";
    public static final String CACHE_STUDY_PREFIX = "bookStudy_";
    public static final String CACHE_SPOKEN_PREFIX = "bookSpoken_";
    public static final String CACHE_ORALTEST_PREFIX="oralTest";
    public static final String CACHE_USER_STUDY_PREFIX = "userStudy_";
    public static final String CACHE_USER_WEEKLY_PREFIX = "weekly";
    public static final String CACHE_FULLBOOKINFO_VERSION_KEY = "ddb_fullBookInfo_version";
    public static final String CACHE_BOOKINFO_VERSION_KEY = "ddb_bookInfo_version";
    public static final String CACHE_PAGEINFO_VERSION_KEY = "ddb_pageInfo_version";
    public static final String CACHE_STRUCTUREINFO_VERSION_KEY = "ddb_structureInfo_version";
    public static final String CACHE_THREAD_BOOKINFO_KEY = "threadBookInfos";
    public static final String CACHE_WRONG_TIME_PEN = "wrong_time_pen";
    // 返回值相关参数.
    public static final String ID = "id";
    public static final String RESOURCE_SIZE = "resSize";
    public static final String NAME = "name";
    public static final String DOWNLOAD_URL = "downloadUrl";
    public static final String CODE = "code";
    public static final String PHOTO = "photo";
    public static final String PATH = "path";
    public static final String VIDEOS = "videos";
    public static final String DEFAULT_BATTERY = "20";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String PUSH_ERROR = "推送失败";
    public static final String VEDIO_PUSH = "打开App观看视频！";
    public static final String PRE_BATTERY_PUSH = "电量低于";
    public static final String SUF_BATTERY_PUSH = "%，请充电!";
    public static final String NO_MACHING_PEN = "笔信息不存在！";
    public static final String NO_MACHING_BOOK = "书籍信息不存在！";
    public static final String IS_BUSY = "系统繁忙，请稍后再试！";
    public static final String INDEX = "index";
    public static final String VERSION_FROM = "versionFrom";
    public static final String VERSION_TO = "versionTo";
    public static final String DESCRIPTION = "description";
    public static final String SIZE = "size";
    public static final String MD5 = "md5";
    public static final String PASSWORD = "password";
    public static final String SITECODE = "siteCode";
    public static final String JSON = "json";
    public static final String IP_PARAM = "ip";
    public static final String RESULT = "result";
    public static final String MPEN = "MPEN";
    public static final String DATE = "date";
    public static final String TOTAL_TIME = "totalTime";
    public static final String DAYS = "days";
    public static final String DATE_MAP = "dateMap";
    public static final String BOOK_LIST = "bookList";
    public static final String TIP = "tip";
    public static final String TICKET = "ticket";
    public static final String NEST_STUDY_DETAIL = "nextStydyDetail";
    public static final String WEEKLY = "weekly";
    public static final String BATTERY = "battery";

    // 方法请求相关参数.
    public static final String PASSQA = "passQa";
    public static final String UPLOAD_SERIAL_NUMBER = "uploadSerialNumber";
    public static final String FILE_RSA_KEY = "fileRSAkey";
    public static final String PREDOWNLOAD = "preDownload";
    public static final String AUDIOS_TEST = "audiosTest";
    public static final String PUSH_APP = "pushApp";
    public static final String CHECK_BIND = "checkBind";
    public static final String UPGRADE_APP = "upgradeApp";
    public static final String UPGRADE_PC = "upgradePC";
    public static final String UPGRADE_ROM = "upgradeRom";
    public static final String UPLOAD_CMD_FILE = "uploadCmdFile";
    public static final String UPLOAD_ORALTEST_RECORDING = "uploadOralTestRecording";
    public static final String UPLOAD_PERSISTENCE_FILE = "uploadPersistenceFile";
    public static final String DOWNLOAD_BOOK = "downloadBook";
    public static final String DOWNLOAD_BOOK_ZIP = "downloadBookZip";
    public static final String SAVE_BINDRELATIONSHIP = "saveBindRelationship";
    public static final String UN_BINDRELATIONSHIP = "unBindRelationship";
    public static final String ADB_ADMIT = "adbAdmit";
    public static final String GET_CMD = "getCmd";
    public static final String READ = "read";
    public static final String UN_BIND_PEN = "unBindPen";
    public static final String COMPLETE_MAC = "completeMac";
    public static final String GET_TOP_GOODS = "getTopGoods";
    public static final String GET_GOODS = "getGoods";
    public static final String GET_GOODS_TEACHLINK = "getGoodsTeachLink";
    public static final String COMPLETE_USER_STUDY_INFO = "completeUserStudyInfo";
    public static final String USER_DATE_STUDY_TIME = "userDateStudyTime";
    public static final String BOOK_STUDY_INFO = "bookStudyInfo";
    public static final String BOOK_CONTENT_STUDY_DETAIL = "bookContentStudyDetail";
    public static final String BOOK_CONTENT_SPOKEN_DETAIL = "bookContentSpokenDetail";
    public static final String GET_WEEKLY_LIST = "getWeeklyList";
    public static final String GET_WEEKLY = "getWeekly";
    public static final String CHANGE_USER_INFO = "changeUserInfo";
    public static final String SAVE_ADDRESS = "saveAddress";
    public static final String GET_BOOKS_PHOTO = "getBooksPhoto";
    public static final String GET_USER_LABELS = "getUserLabels";
    public static final String GET_GOODS_BY_BOOK_ID = "getGoodsByBookId";
    public static final String GET_GOODS_BY_GOOD_ID = "getGoodsByGoodId";
    public static final String GET_VALID_BOOKS = "getValidBooks";
    public static final String GET_BOOK_PAGES = "getBookPages";
    public static final String GET_LEARNWORD_STRUCTUREINFO = "getLearnWordStructureInfo";
    public static final String GET_ALL_ORALTEST_INFO = "getAllOralTestInfo";
    public static final String GET_ORALTEST_INFO = "getOralTestInfo";
    public static final String CREATE_BOOK = "createBook";
    public static final String CREATE_CODE = "createCode";
    public static final String CREATE_GOODS = "createGoods";
    public static final String REMOVE_GOODS = "removeGoods";
    public static final String GET_PROGRESS = "getProgress";
    public static final String GET_PAGEINFO = "getPageInfo";
    public static final String UPLOAD_BOOK = "uploadBook";
    public static final String GET_PENIDANDMAC = "PenIdAndMac";
    public static final String GET_PENINFOBYSERIALNUMORMAC= "getPenInfoBySerialNumOrMac";
    public static final String GET_PENINFOBYMOBILE= "getPenInfoByMobile";
    public static final String GET_UERPHOTO = "UserPhoto";
    public static final String GET_EXAM_DETAIL = "getExamDetail";
    public static final String GET_READING_RESOURCE = "getReadingResource";
    public static final String SAVE_EXAM_DETAIL = "saveExamDetail";
    public static final String TEACHER_PEN = "teacherPen";
    public static final String PEN_VERSION_INFO = "penVersionInfo";
    public static final String PUBLISH = "publish";
    public static final String GET_QR_CODE = "qrcode";
    
    public static final String SAVEN_HOMEWORK = "saveHomework";
    public static final String SAVE_PREVIEW = "savePreview";
    public static final String UPLOAD_TEACHER_FILE = "uploadTeacherFiles";
    public static final String UPLOAD_STUDENT_FILE = "uploadStudentFiles";
    // audiosTest相关参数.
    public static final String AUDIOS1_NAME = "燕归巢 - 许嵩";
    public static final String AUDIOS1_PATH = "http://cc.stream.qqmusic.qq.com/C100000Nz08A0aZNuz.m4a?fromtag=52";
    public static final String AUDIOS2_NAME = "Sorry - Justin Bieber";
    public static final String AUDIOS2_PATH = "http://cc.stream.qqmusic.qq.com/C100002homRe0dmkTn.m4a?fromtag=52";
    public static final String AUDIOS3_NAME = "What Do You Mean? (2015维多利亚的秘密秀秀场音乐) - Justin Bieber";
    public static final String AUDIOS3_PATH = "http://cc.stream.qqmusic.qq.com/C100004HAF5J0HxEZk.m4a?fromtag=52";
    public static final String AUDIOS4_NAME = "单词1";
    public static final String AUDIOS4_PATH = FileUtils.cdnDomain + "/incoming/course/voice/s_001.mp3";
    public static final String AUDIOS5_NAME = "单词2";
    public static final String AUDIOS5_PATH = FileUtils.cdnDomain + "/incoming/course/voice/s_002.mp3";
    public static final String AUDIOS6_NAME = "单词3";
    public static final String AUDIOS6_PATH = FileUtils.cdnDomain + "/incoming/course/voice/s_003.mp3";
    public static final String AUDIOS7_NAME = "单词4";
    public static final String AUDIOS7_PATH = FileUtils.cdnDomain + "/incoming/course/voice/s_004.mp3";
    public static final String AUDIOS8_NAME = "单词5";
    public static final String AUDIOS8_PATH = FileUtils.cdnDomain + "/incoming/course/voice/s_005.mp3";
    public static final String AUDIOS9_NAME = "单词6";
    public static final String AUDIOS9_PATH = FileUtils.cdnDomain + "/incoming/course/voice/z1-001.mp3";
    public static final String UPDATE_PROMPT_VOICE = "/incoming/updateprompt.mp3";
    // Medal
    public static final String MEDAL_ID = "1234567890";
    public static final String MEDAL_NAME = "好学";
    public static final String MEDAL_PHOTO = "http://q.qlogo.cn/qqapp/1105541442/0296DAC896E89A0FF5A7422B664F3CB4/40";

    // GetTopBook
    public static final String POSTER1_PATH = "/incoming/weidian.png";
    public static final String WINDIAN_PATH = "https://www.mpen.com.cn/wdtj.html";

    // 错误码相关参数.
    // TODO ZYT 在别的地方直接引用 Result.xxx 删除对应 Constants.xxxx
    public static final String MSG_SUCCESS_CODE = NetworkResult.MSG_SUCCESS_CODE;// "200";
    public static final String BAD_REQUEST_ERROR_CODE = NetworkResult.BAD_REQUEST_ERROR_CODE;// "400";
    public static final String BAD_REQUEST_ERROR_MSG = NetworkResult.BAD_REQUEST_ERROR_MSG;// "Bad
                                                                                           // request!";
    public static final String ACCESS_DENIED_ERROR_CODE = NetworkResult.ACCESS_DENIED_ERROR_CODE;// "401";
    public static final String ACCESS_DENIED_ERROR_MSG = NetworkResult.ACCESS_DENIED_ERROR_MSG;// "Access
                                                                                               // denied!";
    public static final String ACCESS_FORBIDDEN_ERROR_CODE = NetworkResult.ACCESS_FORBIDDEN_ERROR_CODE;// "403";
    public static final String ACCESS_FORBIDDEN_ERROR_MSG = NetworkResult.ACCESS_FORBIDDEN_ERROR_MSG;// "Access
                                                                                                     // forbidden!";
    public static final String NO_MACHING_ERROR_CODE = NetworkResult.NO_MACHING_ERROR_CODE;// "404";
    public static final String NO_MACHING_ERROR_MSG = NetworkResult.NO_MACHING_ERROR_MSG;// "No
                                                                                         // maching
                                                                                         // resource!";

    public static final String CACHE_USERSESSION_KEY_PREFIX = "UserSession_";
    public static final String CACHE_PENINFO_KEY_PREFIX = "PenInfo_";
    public static final String CACHE_BOOKINFO_KEY_PRIFIX = "BookInfo_";
    public static final String CACHE_CODEINFO_KEY_PRIFIX = "CodeInfo_";
    public static final String CACHE_POINT_NUM_PRIFIX = "PointNum_";
    public static final String CACHE_SEND_SMS_KEY = "sendSms";

    public static final String UCENTERKEY = "ucenterKey";
    public static final String LOGINIDKEY = "loginId";
    public static final String SESSIONKEY = "sessionId";
    public static final String PENKEY = "penId";
    public static final String KEY_FOR_WYS_INVENTORY_MANAGER = "KEYFORWYSINVENTORYMANAGER";
    public static final String AGENT_OPERATE_KEY = "AGENTOPERATEKEYWITHNOUSERSESSION";
    public static final String GLOBAL_BOOKID_KEY = "globalBookId";
    public static final String SHADOW_KEY_FOR_STUDY_COUNT = "SDKMPV587";
    
    public static final String LOGINTOKENKEY = "token";

    public static final String PENREQUESTFLAGKEY = "pen";

    public static final String ONE = "1";

    public static final String ZERO = "0";

    public static final String TWO = "2";

    public static final String DECODE_FAILED_NO = "4294967295";

    public static final String PEN_FLAG_ACTIVE = "FlagActive";

    public static final String GOODS_FLAG_PUBLISH = "FlagPublish";

    public static final String INVALID_LOGINID_ERROR = "{\n" + "    \"httpStatus\": \"403\",\n"
        + "    \"message\": \"Invalid login id in cookies\"\n" + "}";
    public static final String INVALID_PARAMETER_ERROR = "{\n" + "    \"httpStatus\": \"402\",\n"
        + "    \"message\": \"Invalid parameter\"\n" + "}";
    public static final String INVALID_PARAMRTER_MESSAGE = "Invalid parameter";
    // seconds 0 not expir
    public static final Integer DEFAULT_CACHE_EXPIRATION = 0;
    public static final Integer FOUR_HOUR_CACHE_EXPIRATION = 4 * 60 * 60;
    public static final String NO_MATCHING_METHOD = "No matching method";
    public static final String WRONG_IDENTIFIACTION = "Wrong identifiaction";
    public static final String WRONG_REFERER = "Wrong referer";
    public static final String WRONG_USERAGENT = "Wrong user-agent";
    public static final String TIME_ERROR = "Time error";
    public static final String NO_BOOKINFO_EVER = "No BookInfo Ever";

    public static final String NO_MATCHING_USER = "用户名不存在！";

    public static final String WRONG_PASSWORD = "密码错误!";

    public static final String UCENTER_ERROR = "服务器繁忙!";
    public static final String UNKNOW = "未知";
    public static final String PEN_IS_BIND = "笔已经被绑定了!";
    public static final String NO_RESOURCE = "没有资源!";
    public static final String GET_INFO_FAILURE = "获取信息失败！";
    public static final String FILE_OPEN_FAILED = "文件打开失败！";
    public static final String WEEKLY_MSG = "点击查看本周学情周报";
    public static final String CACHE_ERROR = "缓存异常";
    
    // AIENGINE_YZS 云知声评测 AIENGINE_CHIVOX 驰声评测
    public static final int AIENGINE_YZS = 0;
    public static final int AIENGINE_CHIVOX = 1;
    
    // 1 课后作业 2 课前导学
    public static final int HOMEWORK_AFTER = 1;
    public static final int PREVIEW = 2;
    
    /**
     * 消息推送类型:作业详情页、阅读评测页、积分总榜排行榜、积分排行榜、我的动态页面、发现-书页面、勋章页面、我的（其他）
     */
    public enum StudentMsgPushType{
        HOMEWORKDETAIL, READLEVEL, INTEGRALSUM, INTEGRAL, MYDYNAMIC, DISCOVERY, MEDAL, OTHER
    }
    
    public static final String GET_ALL_ENGLISHBOOK = "getAllBooks";
    public static final String GET_BOOK_CONTENT = "getBookContent";
    public static final String GET_BOOK_CONTENT_DETAIL = "getBookContentDetail";
    public static final String GET_ALL_ORALTEST = "getAllOraltest";
    public static final String GET_ORALTEST_CONTENT = "getOraltestcontent";
    public static final String CACHE_ENGLISHBOOK_KEY = "ddb_englishbook";
    public static final String GET_ALL_COMMENTS = "getAllComments";
    public static final String GET_HOMEWORK_LIST = "getHomeWorkList";
    public static final String GET_CLASSHOMEWORK_LIST = "getClassHomeWorkList";
    public static final String GET_NUMBER = "getNumber";
    public static final String GET_MEMBER = "getMember";
    public static final String SAVE_HOMEWORK_DETAILS = "saveDetails";
    public static final String GET_DETAILS = "getDetails";
    public static final String CLASS_PREVIEW = "classPreview";
    public static final String CLASS_PREVIEW_DETAIL = "classPreviewDetail";
    public static final String SUBMIT_JOB = "submitJob";
    public static final String CLASS_AFTER = "classAfter";
    public static final String CLASS_AFTER_DETAIL = "classAfterDetail";
    public static final String GET_PUSHBOOK_LIST = "getPushBookList";
    public static final String VEDIO_BOOK_PATH = "config/videobook.txt";
    public static Map<String, String> vedioBookMap;
    // 1 个人 2班级
    public static final int PERSONAL = 0;
    public static final int COLLECTIVE = 1;
    // 学情-计算
    public static final int LEARN_CALCULATE = 0;
    // 学情-pipeline统计 直接获取
    public static final int LEARN_DIRECT_ACQUIRE = 1;
    public static final String SAVE_CLASS_INFO = "saveClassInfo";
    public static final String UPDATE_CLASS_INFO = "updateClassInfo";
    public static final String GET_CLASS_LISTS = "getClassLists";
    public static final String DELETE_CLASS_INFO = "deleteClassInfo";
    public static final String DELETE_STUDENT_INFO = "deleteStudentInfo";
    public static final String GET_STUDENT_LISTS = "getStudentLists";
    public static final String GET_LEVEL_OR_RANKING = "getlevelOrRanking";
    public static final String GET_HOMEWROK_DETAILS = "getDetails";
    public static final String GET_STUDENT_DETAILS = "getStudentDetails";
    public static final String REMARK_UPDATE = "update";
    public static final String REMARK_SAVE = "save";
    public static final String REMARK_DELETE = "delete";
    public static final String GET_ANSWER = "getAnswer";
    public static final String HOMEWORK_MSG = "点击查看未完成作业详情";
    public static final String FABULOUS_ME = "谁赞了我";
    public static final String RUSH_JOB = "rushJob";
    public static final String INTEGRALLIST = "integralList";
    public static final String MEDALWEAR = "medalWear";
    public static final String MEDALOFF = "medalOff";
    public static final String GET_RANKING_LIST = "getRankingList";
    public static final String GET_FRIENDS_LIST = "getFriendsList";
    public static final String GET_PERSONAL_RANKING = "getPersonalRanking";
    public static final String GET_RECENT_INTEGRAL = "getRecentIntegral";
    public static final String GET_INTEGRAL_INSTRUCTION = "getIntegralInstruction";
    public static final String UPDATEPRAISENUM = "updatePraiseNum";
    public static final String SEARCH = "search";
    public static final String GET_NEWFRIENDLIST = "getNewFriendList";
    public static final String FRIEND_LIST = "friendList";
    public static final String APPLY = "apply";
    public static final String DELETE_FRIEND = "deleteFriend";
    public static final String PASS_FRIEND = "passFriend";
    public static final String CHANGE_REMARK = "changeRemark";
    public static final String DELETE_NEW_FRIEND = "deleteNewFriend";
    public static final String USER_CLASS = "userClass";
    public static final String CLASS_INFO = "classInfo";
    public static final String JOIN_CLASS = "joinClass";
    public static final String EDIT_CLASS = "editClass";
    public static final String QUIT_CLASS = "quitClass";
    public static final String IMPROVINGCLASSINF = "完善班级信息";
    public static final String COMPLETEEXERCISE = "完成预习/练习";
    public static final String MEDAL_RECORD = "荣誉勋章";
    public static final String MYDYNAMIC = "myDynamic";
    public static final String FRIENDDYNAMIC = "friendDynamic";
    public static final String DYNAMIC = "dynamic";
    public static final String COVER = "cover";
    public static final String PRAISE = "praise";
    public static final String PUSH_SYSTEM_MESSAGE = "pushSystemMessage";
    public static final String LIST_USER_MESSAGE = "listUserMessage";
    public static final String ONE_USER_MESSAGE = "oneUserMessage";
    public static final String DELETE_USER_MESSAGE = "deletetUserMessage";
    public static final int MESSAGE_IS_READ = 1;// 用户消息已经读取
    public static final int MESSAGE_IS_DEL = 1;// 用户消息已经删除

    // 学生端口语考试卷:不获取具体试卷内容 教师端口语考试卷内容
    public static final int TEACHER_ORALTEST = 0;
    public static final int STUDENT_ORALTEST = 1;

    public static final String COMPLETE_EXERCISE = "完成预习/练习";

    // 课本点读 口语评测
    public static final String TEXTBOOKREAD = "0";
    public static final String ORALTEST = "1";

    // 已批阅，未批阅，已提交，未提交
    public static final String READOVER = "0";
    public static final String NOTREAD = "1";
    public static final String SUBMITTED = "3";
    public static final String NOSUBMIT = "2";

    // 阅读等级 积分排行
    public static final int READINGLEVEL = 0;
    public static final int INTEGRALRANKING = 1;
    
    // 班级作业状态
    public static final String STATUS_COMPLETE = "已完成";
    public static final String STATUS_CLOSE = "已截止";
    public static final String STATUS_DOING = "进行中";
    
    // 积分总榜 0 好友榜 1
    public static final String TOTAL_LIST = "0";
    public static final String FRIENDS_LIST = "1";
    
    // 如何获取积分
    public static final String INTEGRAL_CLASS = "完善班级信息";
    public static final String INTEGRAL_LINK = "首次连接智能笔";
    
    public enum Sex implements Serializable {
        MALE("男"), FEMALE("女");
        private final String name;

        private Sex(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    /**
     * 笔类型
     * 目前支持两种类型：ANDROID 和 LINUX
     */
    public enum PenType {
        ANDROID, LINUX
    }

    /**
     * 小程序语音资源上传类型
     * 目前分两种小程序:DIY有声书,拜年
     */
    public enum UploadType {
        DAILY_PROGRAM, BAINIAN_2018 
    }
    
    /**
     * 加载缓存类型
     * 目前分为五种类型: CACHE_BOOK,CACHE_PAGE,CACHE_GLOBAL_PAGE,CACHE_FULL_BOOKS, CACHE_LEARN_WORD_STRUCTURE_INFOS
     */
    public enum CacheType {
        CACHE_BOOK, CACHE_PAGE, CACHE_GLOBAL_PAGE, CACHE_FULL_BOOKS, CACHE_LEARN_WORD_STRUCTURE_INFOS
    }
    
    /**
     * 更新缓存枚举类型
     */
    public enum ManualOp { book, allUser, sms, pageInfo, User, pen, activePageInfo, activeBookList, initPageInfo };

    public static final UrlGenerator URL_GENERATOR = new UrlGenerator() {
        @Override
        public String getUrl(String savePath, String localFileName) {
            return FileUtils.getFullRequestPath((savePath + "/" + localFileName).replace(FileUtils.root, ""));
        }

        @Override
        public String getFileSavePath(String url) {
            try {
                if (StringUtils.isNotBlank(url)) {
                    return FileUtils.getFileSaveRealPath(url, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    };

}