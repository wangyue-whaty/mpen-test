package com.mpen.api.service;

import java.util.ArrayList;
import java.util.List;

import com.mp.shared.common.Page;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.ClassAssignments;
import com.mpen.api.bean.ClassInfo;
import com.mpen.api.bean.HomeWorkDetail;
import com.mpen.api.bean.HomeWorkInfo;
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.HomeworkAfterClass;
import com.mpen.api.bean.PreviewBeforeClass;
import com.mpen.api.bean.Student;
import com.mpen.api.bean.UserSession;
import com.mpen.api.domain.DdbOraltestBook;
import com.mpen.api.domain.DdbUserComment;

public interface HomeWorkService {
    boolean savePreviewOrHomework(UserSession userSession, PreviewBeforeClass previewBeforeClass,
            HomeworkAfterClass homeworkAfterClass);

    List<ClassInfo> getAssignments(UserSession userSession, ClassInfo classInfo);

    HomeWorkDetail getHomeWorkList(ClassInfo classInfo, Student student, int type);

    List<DdbUserComment> getAllComments();

    ClassAssignments getNumber(String homeworkId, UserSession userSession);

    List<Student> getReviewMember(String id, String reviewType, UserSession userSession);

    List<Book> getAllBook();

    List<HomeworkAfterClass> getBookContentDetail(Book book, UserSession userSession);

    ArrayList<DdbOraltestBook> getAllOralBook(UserSession userSession);

    Object getReviewDetails(ClassAssignments classAssignments);

    boolean saveStudentHomeWorkDetails(UserSession userSession, Student student);

    boolean RushJob(ClassAssignments classAssignments);

    HomeWorkDetail getSudentHomeworkDetails(Student student) throws Exception;

    Page<HomeWorkInfo> pageHomeWork(UserSession user, HomeWorks homeWorks);

    HomeWorkDetail getHomeWorkDetail(UserSession userSession, HomeWorks homeWorks) throws Exception;

    List<String> getPushBookVideo();

}
