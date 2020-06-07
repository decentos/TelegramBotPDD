package me.decentos.service;

import me.decentos.model.Question;
import me.decentos.model.Ticket;

import java.util.List;

public interface QuestionService {
    List<Question> findQuestionsByTicket(Ticket ticket);
}
