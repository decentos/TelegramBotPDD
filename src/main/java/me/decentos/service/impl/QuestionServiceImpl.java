package me.decentos.service.impl;

import me.decentos.model.Question;
import me.decentos.repository.QuestionRepository;
import me.decentos.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> findQuestionsByTicket(int ticket) {
        return questionRepository.findQuestionsByTicket(ticket);
    }
}
