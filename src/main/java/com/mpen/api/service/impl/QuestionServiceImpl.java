package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mpen.api.bean.QuestionItem;
import com.mpen.api.domain.DdbQuestion;
import com.mpen.api.domain.DdbQuestionItem;
import com.mpen.api.mapper.QuestionItemMapper;
import com.mpen.api.mapper.QuestionMapper;
import com.mpen.api.service.QuestionService;

@Component
public class QuestionServiceImpl implements QuestionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private QuestionItemMapper questionItemMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<QuestionItem> getQuestionItems() {
        final List<QuestionItem> itemsResult = new ArrayList<>();
        final List<DdbQuestionItem> items = questionItemMapper.get();
        items.forEach((item) -> {
            final QuestionItem questionItem = new QuestionItem();
            final List<DdbQuestion> questions = questionMapper.getByItemId(item.getId());
            if (questions != null && questions.size() > 0) {
                questionItem.setItem(item);
                questionItem.setQuestionList(questions);
                itemsResult.add(questionItem);
            }
        });
        return itemsResult;
    }

    @Override
    public DdbQuestion getQuestion(String id) {
        return questionMapper.getById(id);
    }

}
