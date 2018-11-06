package com.mpen.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import com.google.gson.reflect.TypeToken;
import com.mp.shared.utils.FUtils;
import com.mpen.api.common.Constants;

/**
 * 无老师班级推送书籍资源
 * @author hzy
 *
 */
public class NoClassBookSource implements Serializable {
    private static final long serialVersionUID = -774583313803829960L;
    // 书籍id
    private String bookId;
    // 书籍别名
    private String bookAlias;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookAlias() {
        return bookAlias;
    }

    public void setBookAlias(String bookAlias) {
        this.bookAlias = bookAlias;
    }
    
    /**
     * 初始化无老师班级推送书籍vedioBookMap
     */
    public static void initVedioBookDetail() {
        final Map<String, String> vedioBookMap = new HashMap<String, String>();
        try {
            final String path = ResourceUtils.getURL(Constants.VEDIO_BOOK_PATH).getPath();
            final String fileContent = FUtils.fileToString(path);
            final ArrayList<NoClassBookSource> codeList = Constants.GSON.fromJson(fileContent,
                    new TypeToken<ArrayList<NoClassBookSource>>() {
                    }.getType());
            for (NoClassBookSource bookSource : codeList) {
                vedioBookMap.put(bookSource.getBookAlias(), bookSource.getBookId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Constants.vedioBookMap = vedioBookMap;
    }

    public static String getBookId(String bookAlias) {
        return Constants.vedioBookMap.get(bookAlias);
    }
}
