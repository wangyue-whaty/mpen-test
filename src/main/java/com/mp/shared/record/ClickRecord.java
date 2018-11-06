package com.mp.shared.record;

import com.mp.shared.CompatUtils;
import com.mp.shared.common.Code;
import com.mp.shared.common.CodeInfo;
import com.mp.shared.common.Utils;

/**
 * Created by zyt .
 * 
 * 学情数据中作为TaskRecord中extra部分，转换成字符串时需要使用Gson.
 */
public class ClickRecord {
    public String bookId;
    // TODO 暂时只处理SH码
    public Code code;
    public long clickTime;
    public Integer language;
    // TODO 口语评测时需要的补充数据.
    public String text;
    public float score;
    // 云知声评测原始数据
    public String userRecognizeTxt;

    public static ClickRecord fromCodeInfo(CodeInfo codeInfo) {
        if (codeInfo.getBookInfo() == null) {
            CompatUtils.impl.error("ClickRecord", "BookInfo is null when finished ok. ERROR!");
            return null;
        } else {
            final ClickRecord clickRecord = new ClickRecord();
            // 1. bookId
            clickRecord.bookId = codeInfo.getBookInfo().id;
            // 2. code
            if (codeInfo.quickCodeInfo.code.shCode != null) {
                clickRecord.code = codeInfo.quickCodeInfo.code;
            } else if (codeInfo.hotAreaCodes != null
                    && !Utils.isEmpty(codeInfo.hotAreaCodes.shCodes)) {
                clickRecord.code = codeInfo.hotAreaCodes.shCodes[0].toCode();
            } else {
                clickRecord.code = codeInfo.quickCodeInfo.code;
            }
            // 3. clickTime
            clickRecord.clickTime = System.currentTimeMillis();
            // 4. language
            clickRecord.language = codeInfo.quickCodeInfo.language;
            return clickRecord;
        }
    }
}

/*
一、点读数据ClickRecord所需参数说明：
1、SH普通点读：
bookId（书籍id）、code.type=SH（码值类型）、code.shCode（SH码值）、language（语音类型）、clickTime（点读时间）；
2、SH口语评测：
bookId（书籍id）、code.type=SH（码值类型）、code.shCode（SH码值）、language（语音类型）、clickTime（点读时间）、text（文本）、score（评测分数）；
ClickRecord 定义了通用的java class来反应上面的数据，这样可以方便 Gson.to/fromJson来解析

二、实现说明：
1、客户端构造TaskRecord，通过codeInfo使用setExtra方法对extra属性赋值；
2、客户端通过TaskRecord中toActionRecord方法构造ActionRecord；
3、客户端构造ActionRecords，满足提交条件后向服务器提交；
4、服务端收到请求后逐条保存ActionRecord；
5、通过ActionRecord获取TaskRecord，通过ClickRecord.formRecord还原taskRecord.extra；
6、通过clickRecord创建ddbRecordUserBook对象并保存数据库。

*/
