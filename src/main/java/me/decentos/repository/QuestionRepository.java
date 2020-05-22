package me.decentos.repository;

import me.decentos.model.Question;
import me.decentos.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findQuestionsByTicket(Ticket ticket);
}
