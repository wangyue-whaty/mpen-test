package com.mpen.api.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.mp.shared.utils.FUtils;
import com.mpen.api.bean.Book.DotParam;
import com.mpen.api.bean.Book.PageParam;
import com.mpen.api.bean.Book.SubPageParam;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.domain.DdbResourcePageCode;
import com.mpen.api.domain.DdbResourcePageScope;

public class MpCodeBuilderTest {
    MpCodeBuilder builder;
    PageParam[] pageParams;
    DdbResourcePageCode page;
    String[] expectFileMd5;

    @Before
    public void preparr() {
        final DdbResourceBook book = new DdbResourceBook();
        book.setId(CommUtil.genRecordKey());
        builder = new MpCodeBuilder(book);
        pageParams = new PageParam[3];
        expectFileMd5 = new String[3];
        final String[] tifPath = { "/tif/0.tif", "/tif/1.tif", "/tif/2.tif" };
        for (int i = 0; i < 3; i++) {
            expectFileMd5[i] = FUtils.getMD5ByFile(new File(getClass().getResource(tifPath[i]).getPath()));
            pageParams[i] = new PageParam();
            pageParams[i].setNum(1);
            pageParams[i].setHeightMm(297.0f);
            pageParams[i].setWidthMm(210f);
            pageParams[i].setDotParam(new DotParam());
            SubPageParam[] subpages = null;
            switch (i) {
            case 0:
                // 一页中存在部分块铺码
                subpages = new SubPageParam[3];
                subpages[0] = new SubPageParam(0, 0, 70, 100, 0);
                subpages[1] = new SubPageParam(100, 50, 170, 150, 1);
                subpages[2] = new SubPageParam(200, 100, 270, 200, 2);
                break;
            case 1:
                // 全页铺码
                subpages = new SubPageParam[1];
                subpages[0] = new SubPageParam(0, 0, 297, 210, 0);
                break;
            default:
                // 为空时默认为全页铺码
                break;
            }
            pageParams[i].setSubPages(subpages);
        }
        page = new DdbResourcePageCode("test", 1, book.getId(), 210, 297);
    }

    @Test
    public void createCodeTest() throws Exception {
        final File file = new File(getClass().getResource("/pageInfos/pageScopes.json").getPath().replace("%20", " "));
        final String json = FUtils.fileToString(file);
        final DdbResourcePageScope[] expectScope = Constants.GSON.fromJson(json, DdbResourcePageScope[].class);
        for (int i = 0; i < 3; i++) {
            final DdbResourcePageScope scope = builder.createCode(0l, i, pageParams[i], page);
            final String fileMd5 = FUtils
                .getMD5ByFile(new File(FileUtils.getFileSaveRealPath(scope.getTifLink(), false)));
            assertEquals(fileMd5, expectFileMd5[i]);
            assertEquals(scope.getCodeStart(), expectScope[i].getCodeStart());
            assertEquals(scope.getCodeEnd(), expectScope[i].getCodeEnd());
            assertEquals(scope.getSubPages(), expectScope[i].getSubPages());
        }
    }

}
