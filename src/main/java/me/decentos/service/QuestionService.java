package me.decentos.service;

import me.decentos.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> findQuestionsByTicket(int ticket);
}
