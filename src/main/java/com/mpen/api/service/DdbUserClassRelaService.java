package com.mpen.api.service;

import java.util.List;

import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.UserSession;
import com.mpen.api.domain.DdbUserClassRela;
import com.mpen.api.exception.SdkException;

/**
 * <p>
 * 班级学生表  接口
 * </p>
 *
 * @author hzy
 * @since 2018-07-03
 */
public interface DdbUserClassRelaService {

    boolean save(ClassInfo classInfo,UserSession user);

    List<DdbUserClassRela> listByClassId(String classId);
    
    List<DdbUserClassRela> listPhotos(List<DdbUserClassRela> userClassRelas) throws SdkException ;
}