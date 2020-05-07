package me.decentos.service;

import me.decentos.model.Question;
import me.decentos.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findQuestionsByTicket(int ticket) {
        return questionRepository.findQuestionsByTicket(ticket);
    }
}
