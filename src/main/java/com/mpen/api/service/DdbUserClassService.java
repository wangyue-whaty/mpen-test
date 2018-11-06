package com.mpen.api.service;

import java.util.List;

import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.Student;
import com.mpen.api.bean.UserSession;
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.exception.CacheException;

/**
 *
 * @author hzy
 * @since 2018-07-03
 */
public interface DdbUserClassService {

    boolean saveClassInfo(UserSession userSession, ClassInfo classInfo);

    boolean updateClassInfo(ClassInfo classInfo);

    boolean deleteClassInfo(ClassInfo classInfo);

    List<Student> getStudentLists(UserSession userSession, ClassInfo classInfo);

    boolean deleteStudentInfo(UserSession userSession, Student student);

    List<ClassInfo> getClassLists(UserSession userSession);

    List<Student> getLevelOrRanking(ClassInfo classInfo, UserSession userSession);

    DdbUserClass getClassByLoginId(String loginId);

    DdbUserClass getClassByClassNumber(String classNumber);

    DdbUserClass editClass(ClassInfo classInfo, UserSession user);

    boolean quitClass(String LoginId, String classId);


}