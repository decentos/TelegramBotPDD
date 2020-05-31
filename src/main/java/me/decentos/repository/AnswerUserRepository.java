package me.decentos.repository;

import me.decentos.model.AnswerUser;
import me.decentos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerUserRepository extends JpaRepository<AnswerUser, Integer> {
    List<AnswerUser> findAnswerUserByUser(User user);
}
