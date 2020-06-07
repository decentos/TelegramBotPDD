package me.decentos.repository;

import me.decentos.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findOptionsByQuestionId(int questionId);
}
