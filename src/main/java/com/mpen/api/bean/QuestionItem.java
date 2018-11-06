package com.mpen.api.bean;

import java.io.Serializable;
import java.util.List;

import com.mpen.api.domain.DdbQuestion;
import com.mpen.api.domain.DdbQuestionItem;

public final class QuestionItem implements Serializable {
    private static final long serialVersionUID = 4243281639421521971L;
    private DdbQuestionItem item;
    private List<DdbQuestion> questionList;

    public DdbQuestionItem getItem() {
        return item;
    }

    public void setItem(DdbQuestionItem item) {
        this.item = item;
    }

    public List<DdbQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<DdbQuestion> questionList) {
        this.questionList = questionList;
    }

}
