package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import me.decentos.model.Question;
import me.decentos.model.Ticket;
import me.decentos.repository.QuestionRepository;
import me.decentos.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> findQuestionsByTicket(Ticket ticket) {
        return questionRepository.findQuestionsByTicket(ticket);
    }
}
