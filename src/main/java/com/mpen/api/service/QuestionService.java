package com.mpen.api.service;

import java.util.List;

import com.mpen.api.bean.QuestionItem;
import com.mpen.api.domain.DdbQuestion;

public interface QuestionService {
    List<QuestionItem> getQuestionItems();

    DdbQuestion getQuestion(String id);

}
